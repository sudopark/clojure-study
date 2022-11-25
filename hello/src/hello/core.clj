;; ns는 네임스페이스를 뜻함
;; (ns hello.core)

;; 실행을 위한 클래스 생성
(ns hello.core (:gen-class))

;; defn은 함수를 정의하는 구문
(defn -main [] (println "안녕하십니까"))



;; ## 데이터 구조

;; ### Boolean
;; true, false
;; true or false를 판단하는 함수
(true? true) ;; true
(true? false) ;; false
(false? true) ;; false
(false? false) ;;true

;; ### nil
;; 아무값도 없는 값을 뜻함
;; 역시 nil인지 확인해주는 nil? 함수가 있음
(nil? nil)  ;;true
(nil? false) ;; false

;; Number
;; 숫자는 내부적으로 자바의 long에 저장되고 소수는 double에 저장된다.
;; Ratio라는 분수 타입이 있는데 1/3과 같이 표현한다.

;; 사칙연산을 제공하는 함수명은 각각 +, -, *, / 이다
;; 함수는 `(함수명 파라미터값 파라미터값 ...)` 형채로 실행된다.
;; 사칙연산 함수는 인자를 여러개 가질 수 있다
(+ 1 2 3 4)
;; 같은 값인지 비교하고자할때는 = 함수를 이용한다.
(= 1 1) ;; true
(= 1 1.1) ;; false

;; Character
;; 단일 문자는 \문자 형태를 사용한다
\a
;; UTF-8도 지원하기때문에 한들도 사용할 수 있다
\가

;; String
;; 문자열은 "로 감싸서 표현한다.
"clsoure"
;; 다양한 문자열 함수들을 제공한다.
(str "hello" " " "world")   ;; "hello world"
(format "hello %s" "world") ;; "hello world"
(count "hello")     ;; 5
;; 문자열 비교도 = 함수로 하면 된다

;; 자바와 같은 static 문자열은 문자열 테이블에 있는 객체를 사용한다.
;; 따라서 = 가 제대로 비교하는지 아래와 같이 확인해봐야한다.
(= "abc" (str "a" "b" "c"))   ;; true
(identical? "abc" (str "a" "b" "c")) ;; false
(identical? "abc" "abc") ;; true
;; 실제로 = 비교는 내부적으로 자바의 equals로 비교하기때문에 문자열 값 비교가 정상적으로 동작한다.


;; List
;; 순서가 있는 값의 목록으로 (항목 항목 항목) 형태로 표현한다
;; user=> (0 1 2 3 4)  ;; ClassCastException java.lang.Long .....
;; REPL에서 실행하면 에러가남 -> REPL은 클로저 코드를 평가해 값을 계산하는데 리스트가 코드가 평가(실행)될때 함수로 동작하기 때문
;; -> 이는 0이라는 함수에 인자 1, 2, 3, 4가 입력된것처럼 작동됨
;; 어떤 값이 평가되는 것을 막기 위해 앞에 `을 붙이면된다. 값이 평가되지 않으면 그 자체로 사용된다.
;; user=> `(0 1 2 3 4)
;; (0 1 2 3 4)

;; 다양한 리스트 함수가 있음
(first `(0 1 2))  ;; 0
(second `(0 1 2))  ;; 1
(last `(0 1 2))   ;; 2
(rest `(0 1 2))  ;; (1, 2)
(count `(0 1 2)) ;; 3

;; Vector
;;  리스트처럼 순서있는 항목으로 [항목 항목 항목] 형태로 표현한다.
;; 리스트는 첫번째 항목이 함수로 실행되어서 데이터로 사용하기 불편하기때문에 주로 데이터를 담을때는 벡터를 쓴다.
;; 백터에도 리스트 함수를 쓸수 있다.
[1 2 3]

;; Map
;; 키/값 을 가지는 데이터 구조로 순서는 보장되지 않는다.
;; {키 값, 키 값} 형태로 표현한다.
{"id" 1, "name" "some"}
;; 한 맵에서 키 데이터 형식은 제한이 없다.
{"cpu" 39, 123 "http"}
;; get 함수는 키로 값을 가져올 수 있다.
(get {"name" "some", "level" 30} "name")  ;; "some"
(get {"name" "some", "level" 30} "not exists") ;; nil

;; assoc 함수를 이용하여 맵에 값을 넣거나 바꿀수있다
(assoc {"name" "some", "level" 30} "new key" "new value") ;; {"new key" "new value", "name" "some", "level" 30}
(assoc {"name" "some", "level" 30} "level" 31) ;; {"name" "some", "level" 31}

;; keyword
;; 주로 맵의 키로 사용되는 데이터 형식으로 :이름 으로 표현한다.
;; 키워드를 맵의 키로 사용할때는 ,를 생략하는것이 가독성에 좋다
:id
{:id 222 :name "some"}
;; keyword라는 함수로 문자열 키워드를 만들 수 있다.
(keyword "id") ;; :id
;; 문자열과 다르게 같은 키워드는 항상 같은 인스턴스를 가르킨다.
;; 따라서 같은 이름의 키워드가 많이 생겨도 같은 인스턴스를 유지한다.
(identical? (keyword "id") (keyword (str "i", "d")))   ;; true

;; 키워드는 함수로 사용할 수 있다.
;; 키워드를 함수로 사용하면 맵에서 자신을 키로 가지는 값을 가져온다.
(:id {:id 123 :name "some"}) ;; 123
(:name {:id 123 :name "some"}) ;; "some"

;; Set
;; 중복되지않는 항목을 가지는 데이터 형태로 #{항목 항목 항목}으로 표현한다. 맵처럼 순서는 보장되지 않는다.
#{1 2 3}
;; 중복된 값을 넣으면 예외가 발생한다.
#{1 1 3} ;; error
;; map처럼 get이나 키워드로 값을 읽을수있다.
(get #{1 2 3} 1) ;; 1
(:id #{:id :name :title})  ;; :id
(:level #{:id :name :title}) ;; nil

;; contains? 함수로 값이 있는지 확인할 수 있다.
(contains? #{:id :name :title} :id) ;; true


;; Symbol
;; 그냥 문자들은 심볼 데이터이고 평가되면 연결된 값으로 바꾸는 동작을 하기 때문에 보통 이름으로 사용한다.
;; 역시 평가되지 않게 하려면 리스트처럼 `를 앞에 붙여주면 된다.
`a
;; 심볼은 값에 바인딩되어 값의 이름으로 사용될 수 있다

;; Function
;; 함수도 값이다

;; 값을 평가하는 방법
;; 아래 코드의 결과는 2이다
;; (+ 1 1)
;; 위 코드는 리스트에 3개 항목이 들어있는 데이터 구조다 -> +, 1, 1
;; 데이터 구조이지만 코드이다 -> closure는 코드를 리스트 데이터 구조에 담아서 표현한다.
;; 코드를 그 언어에서 쓰는 데이터 구조로 표현하는것을 
;; Homoiconicity라고 한다.
;; Homoiconicity 특징을 가지는 언어는 그 언어로 언어를 프로그래밍하기 쉽다는 장점이 있다 -> 메타 프로그래밍
;; 위 코드에서 결과가 2가 되는 이유는
;; 1. 리스트 타입 데이터 구조는 첫번 째 항목을 함수 값이라고 생각하고 나머지를 그 함수의 인자로 평가하기 때문이다.
;; +는 심볼이지만 함수 값인 이유는 + 심볼과 함수값을 연결해두었기 때문이다.
;; 2. 심볼 타입 데이터 구조는 그 심볼과 연결된 값으로 바꾼다.
;; 심볼과 값은 Var라는 테이블에 연결정보를 저장한다.
;; 그외 다른 값은 그 값 그대로 평가된다.

;; #Vars
;; 클로저도 다른 언어처럼 이름으로 값을 참조할수 있다.
;; def 구문으로 Var를 만들 수 있다.
(def a 1)
;; 간단한 def 구문은 (def 심볼 값) 형식을 지닌다.
;; 클로저는 심볼을 만나면 Var로 연결된 값을 찾는다 -> 연결된 값이 없다면 에러가 발생
;; 같은 심볼에 새로운값으로 만들면 값이 바뀌는것이 아니라 새로운 Var가 만들어진다

;; Var는 연결된 값에 대한 설명을 넣을 수 있는 기능이 있다.
(def a1 "A value." 1)
;; 심볼뒤에 문자열로 설명을 넣으면 된다.
;; Var 설명을 보려면 doc 함수로 볼 수 있다.

;; Var의 범위는 전역 스코프를 갖는다. 이것을 Root Var라고 한다.
;; 만약 지역적으로 사용될 이름이 필요하다면 let 구문을 쓰면 된다.
(def a2 1)
(let [b 2 c 3] (+ b c))
;; let  구문은 (let 바인딩백터 본문) 형식으로 작성한다.


;; ## Dynamic Vars
;; 바인딩 구문으로 Var 값을 지역적으로 다른 값으로 연결해서 사용할 수 있다.
(def ^:dynamic a3 1)
(binding [a3 2] (+ a3 1)) ;; 3, a3 -> 1
;; 동적 바인딩되는 Var는 Var를 만들때 심볼 앞에 ^:dynamic 이라는 메타데이터를 붙여준다(메타 데이터는 나중에..)

;; 클로저 세계에서는 :dynamic var를 사용할때 조심하라고 심볼 앞에 귀마게 표시(**)를 붙이는 네이밍 규칙을 쓴다.
(def ^:dynamic *a* 1)
(binding [*a* 2] (+ *a* 1)) ;; -> 3


;; ## RootVar를 동적으로 다시 바인딩 하기
;; binding 구문은 지역적으로 Var를 다시 연결 하는 기능을 하지만 Root Var를 동적으로 다시 바인딩 하려면
;; alter-var-root 함수를 이용한다.
(def a4 1)
(alter-var-root (var a4) (fn [origin-value] 2))  ;; a4 => 2로 원래값도 바뀜
;; ** alter-var-root 는 첫번째 인자로 Var를, 두번째 인자로 연결될 새로운 값을 리턴하는 함수를 넘겨준다 **
;; 위의 예제에서 var는 심볼에 연결된 Var를 가져오는 구문이다.
;; 짧게 쓸수있는 구문도 존재하는데 이를 #'a4라 해도 된다.
;; => def로 Var를 만들었을때 REPL에 출력되는 형식 #'user/a4 가 Var를 참조하는 구문이다.(앞에 user는 네임스페이스)

;; 클로저는 함수형 프로그래밍 스타일에 따라 대부분 값을 변경되지 않는 스타일을 사용하기 때문에 
;; alter-var-root를 변수처럼 사용하는 일은 없어야 한다
;; 참고로 alter-alter-var-root는 내부적으로 synchronized 되어있다

;; => over set 인듯?

;; alter-var-root 예제에서 두번째 함수 파라미터로는 원래 연결된 값이 넘어온다. 하지만 예제에서는 이값을 사용하지는 않았다.
;; 이런경우에는 _ 를 사용해 값을 무시하게 할 수 있다.
(alter-var-root (var a4) (fn [_] 2))

;; 위의 예제 함수는 그냥 2를 리턴하는 함수인데 이런 함수를 만들일이 종종 생겨 클로져에서는 constantly 라는 함수를 제공한다.
(def two (constantly 2))  ;; two => 2
(alter-var-root #'two (constantly 2))

;; ------------------------

;; ## 함수
;; : 함수는 값이다.

;; ### 함수 부르기
;; 함수를 부르는 방식은 (함수값 파라미터 파라미터 ...)와 같은 방법으로 부른다.
;; 리스트 첫번째 항목에 함수값과 나머지 항목에 파라미터를 넣으면 코드가 평가될때 함수로 실행된다.
;; 함수를 부르면 결과 값이 나온다.
(str "hello" " " "world")  ;; "hello world"
(+ 1 2)  ;; 3

;; 함수 안에서 함수를 부를 수 있가. 클로저는 내부적으로 중첩된 괄호가 있을때 안쪽부터 평가된다(Eager Evaluation)
(+ (+ 1 2) 3)   ;; -> (+ 3 3) -> 6

;; ### 함수 만들기
;; 새로운 함수 값을 만드는 방법은 fn 이라는 구문으로 만든다.
(fn [x y] (+ x y))
;; 함수를 만드는 간단한 형태는 리스트 첫번째 항목에 fn 구문과, 두번째 항목에 벡터 형식의 파라미터 목록,
;; 나머지 항목에 실행할 본문을 넣어주면 된다.
;; 그리고 마지막에 실행한 내용이 함수의 리턴값이 된다.
;; 위의 예제는 두개의 파라미터를 받아 + 함수를 실행한 결과를 리턴하는 함수이다.
;; 아직 값에 이름을 붙이는 방법을 설명하지 않았기 때문에 함수값에 이름이 없다.
;; ** 그래서 실행하려면 함수 값을 리스트 첫번째 항목에 넣고 실행한다.
((fn [x y] (+ x y)) 1 2)   ;; 3
;; 함수의 두번째 파라미터인 [x y] 벡터는 x와 y 심볼이 들어 있다.
;; x와 y는 함수가 실행될 때 파라미터 값에 순서대로 연결되어 함수 본문에서 쓸 수 있다.

;; 파라미터가 없는 함수는 빈 벡터를 넣어주면 된다.
(fn [] nil)
((fn [] nil)) ;; nil
;; 위 함수는 nil을 반환하는 함수이다, 파라미터가 없기 때문에 리스트 첫번째 항목에 함수 값 만 넣어주면 실행된다.

;; 함수를 간단히 만들 수 있는 #() 구문이 있다.
;; #() 구문은 fn 파라미터와 벡터를 생략하고 함수 본문을 바로 쓸수있가. 파라미터는 하나일때 %, 
;; 두개 이상일때는 첫번째 파라미터는 %1, 두번째 파라미터는 %2와 같이 쓴다(왜 첫번째부터 1이여..)
(#(+ %1 %2) 1 2) ;; 3
(#(println %) "hello world") ;; hello world;; nil -> 여기서 왜 nil? => 아마 스트링 인자를 받아서 출력을 했지만(사이드 이펙트) 리턴값은 nil 이라서

;; ### 함수값과 Var
;; 함수값을 Var로 연결하는 일은 자주 일어나기 때문에 defn 구문을 쓰면 코드가 간단해진다.
(defn add [x y] (+ x y))
(add 1 2) ;; 3

;; defn 구문도 docstring을 넣을 수 있다.
(defn add2 "Add function" [x y] (+ x y))

;; ### 함수 파라미터의 스코프
;; 함수 파라미터 백터에 있는 심볼도 Var가 생성되지 않고 지역적인 스코프를 가지는 연결을 만든다.
(defn add3 [x y] (+ x y))
;; 여기서 x, y는 함수 스코프 내에서만 유효

;; 파라미터 심볼과 같은 이름의 심볼이 let 구문에 연결되어 있다면 파라미터 심볼도 가려진다.
(defn add4 [x y] (let [x 0 y 0] (+ x y)))
(add4 1 2) ;; 0

;; ### 값으로 동작하는 함수
;; 함수는 값이기 때문에 함수에 파라미터로 전달할 수 있다.
(defn apply-one-two [f] (f 1 2))
(apply-one-two add4) ;; 3
;; 위의 에제는 어떻게 동작할지 모르는 함수를 하나 받아 1과 2를 넘겨 실행하는 함수를 만들었다.
;; 또한 함수는 값이기 때문에 리턴 값으로 사용할수있다.
(defn increase [x] (+ x 1))
(defn get-increase-function [] increase)
((get-increase-function) 1) ;; 2
;; 위 예제는 파라미터 하나를 받아 1을 더하는 increase함수와 increase 함수를 리턴하는 get-increase-function을 만들고
;; get-increase-function을 평가한 결과 (increase 함수)에 1을 넘겨 2라는 결과를 얻었다.

;; ### Closure
;; 함수에 파라미터 값을 사용하는 함수를 리턴해도 그 파라미터가 유지된다.
(defn addCurry [x] (fn [y] (+ x y)))
((addCurry 1) 2) ;; 3
;; 위 예제는 x를 인자로 받고 y를 인자로 받아 이는 x와 더하는 함수이다.
;; 여기서 y값은 일반 함수처럼 함수가 종료되고 나서 사라지지 않고 결과 함수가 사용되는 곳에서 계속 유지된다 -> 위의 예제에는 1
;; 위 예제는 커링 예제이다. 커링이란 단일 파라미터 함수로 여러개의 파라미터를 받는 함수를 표현하는 방식이다.

;; ### 가변 인자를 받는 함수
;; 가변 인자를 받는 함수를 만들 때는 마지막 인자 앞에 &을 붙여서 만든다.
;; 가변 인자로 사용된 마지막 인자는 벡터로 넘어온다, 넘기지 않으면 nil이 넘어온다.
(defn get-ys [x & y] y) ;; 가변인자 y를 받아서 y를 반환
(get-ys 1) ;; nil
(get-ys 1 2) ;; 2
(get-ys 1 2 3) ;; (2 3)

;; ### 오버로딩
;; 가변인자 함수는 인자 개수가 명시적이지 않기 때문에 정해진 갯수의 인자를 제공하는 함수의 경우에는 함수 오버로딩을 사용하는 것이 좋다.
(defn sum ([x] x) ([x y] (+ x y)))
;; 마치 파라미터를 복수개 받아서 [x] 하나의 x로 압축되는데 이 방식이 뒤에오는 함수 평가의 결과처럼 생김
;; 아니면 인자를 복수개 받거나 하나만 받을수있다. 복수개는 2개다 뜻인가?
;; 결론: [x] 하나만 들어오면 x를 출력하고, [x y]같이 두개가 들어오면 둘을 합해라
(sum 1) ;; 1
(sum 1 2) ;; 3
;; (sum 1 2 3) ;; ArityException Wrong number of args (3) passed to: user/sum  clojure.lang.AFn.throwArity (AFn.java:429)
;; 오버로딩의 정확한 규칙은 다른 설명을 봐야할거같음
(defn hello
  "힘수를 설명하기 위한 주석"
  ([] "hello world")
  ([name] (str "hello " name)))
(hello)  ;; hello world
(hello "john") ;; hello john
;; [] 비어있을때는 -> hello world를 출력하고
;; [name] 값이 하나 들어왔을때는 hello + name으로 출력해라


;; ## 조건문
;; if 구문은 조건에 따라 다른 값을 표시할 수 있다
;; if 구문의 형태는 (if 조건값 참값 거짓값) 이다 
;; 거짓값은 생략할수있고 생략하면 nil값을 나타낸다.
(if true 1 2)  ;; 1
(if false 1)   ;; nil

;; if는 조건에 따른 값을 나타내는 구문이기때문에 값이 쓰이는 곳이면 어디든 쓸 수 있다.
(let [x (if true 1 2)] (+ x 1)) ;; 2
(let [x (if false 1 2)] (+ x 1)) ;; 3
(+ (if true 1 2) 1) ;; 2

;; nil은 거짓으호 판단되고 그렇지 않는 값은 참으로 판단된다.
(if nil 2 3)  ;; 3
(if 1 1 2) ;; 1

;; 조건을 판단해주는 =, >, <, >=, <= 등이 있다.
(if (= 1 1) 1 2) ;; 1

;; 조건이 맞지 않음을 나타내는 not이 있다
(if (not(= 1 1)) 1 2) ;; 2
;; 같지 않은 것을 판단하는 일은 많이 있기 때문에 not=을 제공한다.
(if (not= 1 2) 1 2) ;; 1

;; ### and와 or
;; 여러 조건을 조합하기 위해 and와 or를 제공하낟.
(if (and (= 1 1) (= 2 2)) "o" "x")  ;; o
(if (or (= 1 1) (= 2 1)) "o" "x") ;; o

;; and는 항목의 값을 하나씩 판단하는데 먼저 나오는 거짓값을 나타낸다.
(and (+ 1 2) (+ 3 4) nil (+ 5 6)) ;; nil
(and false (+ 1 2) (+ 3 4) (+ 5 6)) ;; false

;; or도 따로 쓸 수 있다
(or 1 2) ;; 1
(or nil 2) ;; 2
;; or는 처음 나오는 참값을 나타낸다. 때에 따라서 값이 없을 때 기본값을 사용하기 좋다.
(def params {:limit 10})
(let [limit (or (:limit params) 50)] limit)  ;; 10

(def prams {})
(let [limit (or (:limit params) 44)] limit)   ;; 44

;; ### 조건문은 함수인가?
;; 다음은 조건이 참이면 참을 출력하고 조건이 거짓이면 거짓을 출력하는 함수다.
(defn print-bool [bool]
  (if bool (println "true") (println "false")))
(print-bool true) ;; true \n nil
(print-bool false) ;; false \n nil
;; 만일 if가 함수라면 함수를 평가하는 동작처럼 안쪽에 있는 구문들이 순서대로 실행되어야하고, 
;; 그렇다면 참과 거짓 모두 출력 되어야 한다.
;; if를 다음과 같이 my-if라고 만들어 보자.
;; (defn my-if [condition true-form false-form]
;;   (if condition
;;     true-form
;;     false-form))

;; (my-if (= 1 1) (println "T") (println "F"))
;; ;; T
;; ;; F
;; ;; nil
;; 설명 이상함;;


;; ## if-let
;; 아래는 get-user 라는 함수로 가져온 값을 user에 로컬 바인딩하고
;; user 값이 있으면 :id 값을 리턴하고 없으면 "user not found"라는 문자열을 리턴하는 예제이다.
(defn get-user [] 
  {:d 1 :name "user name"})

(let [user (get-user)]
  (if user
    (:id user)
    "user not found"))
;; 여기서는 get-user가 항상 값을 리턴하기 때문에 :id 값이 1이 나왔다.
;; 하지만 get-user가  nil을 리턴하는경우 위와같이 사용해서 nil인 경우에 대한 처리를 해줘야 한다.
;; 이런 경우에 간단히 사용할 수 있는 것이 if-let 구문이다.
(if-let [user (get-user)]
  (:id user)
  "user not found")
;; 1
;; 조건 바인딩을 이용하여 코드를 좀더 간단하게 쓸 수 있다.
;; 다만 let과 같이 여러개를 바인딩 할수는 없다.
;; if-let과 비슷하게 when-let고 있다. when-let은 거짓에 대하 값이 없는 경우 사용할 수 있다.
(when-let [user (get-user)]
  (:id user))
;; 1

;; ### 조건 함수에 대한 네이밍
;; 클로저에서는 true 또는 false를 리턴하는 조건 함수들은 ?로 끝나는 네이밍 규칙을 사용한다.
(if (zero? 0) 1 2) ;; 1
(if (zero? 1) 1 2) ;; 2

;; 예제
(def http-default-port 80)
(def config {:production {:host "10.0.0.1"
                          :port http-default-port}
             :developmemnt {:host "127.0.0.1"
                            :port 8080}})

(defn url [conf]
  (str "http://" (:host conf)
       (when-not (= http-default-port (:port conf))
         (str ":" (:port conf)))))

(url (:production config)) ;; "http://10.0.0.1"
(url (:developement config)) ;; "http://127.0.0.1:8080"