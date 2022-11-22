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