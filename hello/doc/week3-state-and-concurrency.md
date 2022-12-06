
## 상태와 병행성

### 현실세계의 상태와 병행성 다루기
클로저의 병행성은 클로저의 핵심인 함수형 방식과 불변 자료구조의 결합을 통해 자연스럽게 해결된다.

### 독립적인 항목에 아톰 사용하기
아톰(atom)은 독립적인 항목의 상태를 저장하기 위해 고안되었다. -> 즉 다른 상태들의 변화와는 관계없이 값을 바꿀 수 있음.

(def who-atom (atom :cc)) ;; REPL에서 atom을 평가하면 아톰 그 자체를 돌려준다.
아톰의 현재값을 보고 싶다면 앞에 @을 붙여준다
who-atom ;; => atom
@who-atom ;; => :cc

아톰의 값을 바꾸는 방식은 몇가지 있다. 아톰에서의 값의 변경은 항상 동기적이다.
1. reset!을 사용하기 -> 현재값을 새로운 값으로 바꾸고 그 값을 반환
(클로저에서는 함수가 상태를 바꾼다는 것을 표현하기위해 보통 이름에 !을 붙임)

(rest! who-atom :dd) ;; dd
@who-atom ;; => dd

2. swap! 사용하기 -> 아톰의 현재값에 함수를 적용하여 그 결과를 다시 아톰에 전달.

(defn change [state]
    (case state
        :cc :dd
        :dd :ee 
        :ff))

(swap! who-atom change) ;; => dd
@who-atom ;; => dd

swap!을 사용하는 경우 사용하는 함수가 부수효과가 없는지 명심해야한다.
-> 아톰의 값을 읽고, 그 값을 함수에 적용한 다음, 험수의 결과값을 다시 아톰에 설정하기전에 현재 아톰의 값이 다른 스레드에 의해 변경되지 않고 그대로인지 검사
-> 만약 그 사이에 바뀌었다면 swap!을 다시 시도함

아톰을 역참조해도(@) 연산을 방해하거나 멈추게 하지는 않는다.

(def counter (atom 0))
@counter  ;; => 0

(dotims [_ 5] (swap! counter inc))

@counter ;; => 5

#### 여러 스레드에서 실행하기
클로저에서는 직접적으로 스레드를 다루지 않는다, future를 사용
future는 본문을 다른 스레드에서 실행시킨다.

;; 같은 값을 병행적으로 증가시키는 3개의 스레드를 만든경우
(def counter (atom 0))
@counter ;; => 0

(let [n 5])
    (future (dotimes [_ n] (swap! counter inc))
    (future (dotimes [_ n] (swap! counter inc))
    (future (dotimes [_ n] (swap! counter inc)))

@counter ;; => 15

;; swap! 에서 사용하는 함수에 부수효과가 있는경우 -> 현재값 출력
(defn inc-print [v]
    (println v)
    (inc v))
(swap! inc-print counter) 
;; 0
;; 1

inc-print로 바꾸고 위의 예제에서 확인해보면 중복으로 현재 counter값이 출력될수도있다
이는 출력당시에 다른 스레드에서 값을 변경하고있는 경우일수있음 -> swap!이 재시도되는 상황

### 조화로운 변경을 위해 ref 사용하기
두개 이상의 상태를 조화로운 방식으로 (coordinate) 변경하려면 클러저에서는 ref를 사용한다.
ref는 트렉젝션을 처리할때 사용되며 클로저는 이를 위해 트렌젝션 메모리 라는 것을 사용.
ref는 STM(software transaction memory)을 사용하여 값을 조화롭게 변경한다.

트랜젝션 안에서 ref의 모든 동작은 다음과 같다.
1. 원자적(atomic)이다
: 트랜잭션 안에서 변경은 모든 ref에 대해 발생한다. 하지만 무언가 잘못되면 ref도 변경되지 않는다.

2. 무모순적(consistent) 이다.
: 트랜젝션을 완료하기 이전에 값을 검사하는 검증 함수를 ref에 선택적으로 사용할 수 있다.

3. 고립적(isolated)이다.
: 트랜잭션은 세계를 바라볼 때 자신만의 시야를 통해서 본다. 즉 다른 트랜잭션이 동시에 실행되더라도 이 둘은 서로 어떤 일이 일어나는지 알수 없다.

데이터베이스 트랙잭션 처리하는것과 비슷, 각 트랜잭션은 정보 처리에 필요한 자신만의 스냅샷 안에 있기 때문에 다른 스레드를 멈추게 하거나의 처리가 필요없다.

(def alice-height (ref 3))
(def right-hand-bites (ref 10))

아톰과 마찬가지로 값을 읽으려면 앞에 @을 붙인다.
@alice-height ;; => 3

; 버섯의 오른쪽을 한번 먹을때마다 엘리스의 키를 24만큼 증가시키는 함수 예제
-> alter 형식을 사용하는데 ref와 그 ref의 현재값을 인수로 받는 함수를 받음

(defn eat-from-right-hand []
    (when (pos? @right-hand-bites)
        (alter right-hand-bites dec)
        (alter alice-height #(+ % 24))))

(eat-from-right-hand) ;; => No transaction running 에러
=> 에러를 없애려면 트랜젝션 내에서 변경해야 한다. 이를 위해 dosync 형식을 사용한다.

dosync는 내부의 모든 상태 변화를 조율하면서 트랜잭션을 처리한다.
(dosyn (eat-from-right-hand)) ;; => 27
@alice-height ;; => 27
@right-hand-bites ;; => 9

dosync 트랜잭션을 eat-from-right-hand 함수 안쪽으로 옮기자. 또한 이 함수를 두번씩 호출하는 스레드를 3개 만들어서 병행성을 테스트 해보자.

(defn eat-from-right-hand []
    (do sync (when (pos? @right-hand-bites)
        (alter right-hand-bites dec)
        (alter alice-height #(+ % 24)))))

(let [n 2]
    (future (dotims [_ n] (eat-from-right-hand)))
    (future (dotims [_ n] (eat-from-right-hand)))
    (future (dotims [_ n] (eat-from-right-hand))))

@alice-height ;; => 24 * 6 + 3 = 147
@right-hand-bites ;; =>  10 - 6 = 4


#### commute
alter 처럼 반드시 트랜잭션 안에서 실행되어야 하지만 인수로 ref와 함수를 받는다
alter와 다른점은 commute는 트랜잭션 동안 재시도를 하지 않고 트랜잭션에서 최종적으로 커밋(commit)할때 트랜잭션 내부의 별도의 값으로 ref 값을 설정한다.
=> 이러한 특징으로 인해 속도가 빠르고 재시도를 막는다.
=> 하지만 commute에 들어가는 함수들은 반드시 가환적(commutative)이여야 한다 -> 더하기 처럼 순서가 바뀌어도 상관 없어야 하거나 / 마지막 스레드의 결과를 사용하는 성질이 있어야 한다.

(defn eat []
    (dosync (when (pos? @right-hand-bites)
        (commute right-hand-bites dec)
        (commute alice-height #(+ % 24))
    )))

#### re-set
하나의 ref 값이 다른 ref에 의존하고 있는 경우 유용하다.
다른 값으로부터 바로 값이 계산되는 경우에는 re-set이 유용하다.

(def x (ref 1))
(def y (ref 1))

(defn new-values []
    (dosync 
        (alter x inc)
        (re-set y (+ 2 @x))))

(let [n 2]
    (future (dotimes [_ n] new-values))
    (future (dotimes [_ n] new-values)))

@x ;; 1->2->3->4->5
@y ;; 1->4->5->6->7
