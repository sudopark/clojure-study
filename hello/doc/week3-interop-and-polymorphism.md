

## 자바 코드 가져다 쓰기와 다형성

### 자바 코드 가져다 쓰기
: 클로저는 라이브러리 문제를 JVM 상에서 실행해서 자바 클래스를 가져다쓰는 방법으로 해결한다.
클로저는 자바 클래스를 사용할때 특수형식인 new와 점(.)을 이용한다.

String name = new String("name");
name.toUpperCase();

(. "name", toUpperCase)
점을 접두로 하는 방식으로 짧게 만들어 자바 코드와 같은 일을 할 수 있다.
자바 메소드의 인수들은 객체의 뒤에 나열된다. 
=> instance method의 첫번째 인자는 instance 자체이다.

;; 문자열 인수를 받는 indexOf를 이용해 "pillar"라는 문자열이 원본에서 몇 번째 인덱스인지 아는 방법
String s1 = new String("catepillar");
String s2 = new String("pillar");
s1.indexOf(s2);

(.indexOf "catepillar", "pillar")

new를 이용해 자바 클래스의 인스턴스를 생성할 수 있다.
(new String "hi")

혹은 클래스 이름 바로 뒤에 점을 사용하는 단축형을 이용할 수 있다.
(String. "Hi")

자바 클래스 불러오기
(ns catepillar.network (:import (java.net InetAddress)))
(InetAddress/getByName "localhost")
(.getHostName (InetAddress/getByName "localhost"))
;; "localhost"

;;or
(java.net.InetAddress/getNyName "localhost")

#### doto 매크로
doto는 자바 객체를 받아서 그 객체의 메소드를 연속해서 호출한다.
doto는 자바 객체의 상태를 연이어 변경할때 유용하다.

;; StringBuffer 객체를 생성한 후 append 메소드를 호출하여 그 객체에 문자열을 계속해서 추가하는 방법
(def sb (doto (StringBuffer. "Who ")
            (.append "are ")
            (.append "you?")))
(.toString sb)
;; "who are you?"
;; doto의 구문을 사용하면 다음 예제의 중첩된 형태보다 읽기가 훨씬 좋다.

(def sb 
    (.append 
        (.append 
            (StringBuffer. "Who ") 
            "are ")
            "you?"))
(.toString sb)

#### 위의 예제에서 사용한 자바 코드와의 비교
"cc".toUpperCase();     => (.toUpperCase "cc")
"cc".indexOf("c");      => (.indexOf "cc" "c")
new String("cc");       => (new String "cc")
new String("cc");       => (String. "cc")
InetAddress.getByName("localhost"); => (InetAddress/getByName "localhost")
host.getHostName();     => (.getHostName host)

;; uuid 사용하기
(import 'java.util.UUID) ;; uuid를 위해 해당 자바 클래스를 불러온다.
(UUID/randomUUID)


### 실용적인 다형성
자바와 같은 객체지향 언어에서는 모든것을 타입으로 처리해서 매우 다양한 타입들이 존재한다.
하지만 클로저는 타입의 수가 적은 반면에 타입마다 많은 수의 함수가 존재한다.
클로저는 자바에서 많이쓰는 다형성이 일부 상황에서는 유연하고 유용하다고 본다. -> 고로 다형성을 지원한다.

만약 어떤 함수가 입력 값의 종류에 따라 다르게 동작하도록 하고 싶다면 case와 같이 분기를 처리하는 구문을 사용하면 된다.
(defn who-are-you [input]
    (cond
        (= java.lang.String (class input)) "String - who are you?"
        (= clojure.lang.Keyword (class input)) "Keyword - who are you?"
        (= java.lang.Long (class input)) "Number - who are you?"))
(who-are-you :alice) ;; "Keyword - who are you?"
(who-are-you "alice") ;; "String - who are you?"
(who-are-you 11) ;; "Number - who are you?"
(who-are-you true) ;; nil

클로저에서 위의 메소드는 멀티 메소드(multimethod)를 이용한 다형성으로 표현할수있다.
먼저 defmulti로 멀티 메소드를 선언해야 하는데, 이때 어떻게 분기(dispatch)할지 결정하는 함수를 지정해 주어야 한다. -> 즉 이 함수가 뒤에 정의되는 메소드들 중에서 어느 것을 사용할지 결정하게 된다.

who-are-you의 경우에는 class 함수의 결과값에 따라 분기된다.
(defmulti who-are-you class) ;; who-are-you 함수가 인수가 하나인 멀티 메소드임을 선언한다.
(defmethod who-are-you java.lang.String [input]
    (str "string => " inpu))
(defmethod who-are-you clojure.lang.Keyword [input]
    (str "keyword => " inpu))
(defmethod who-are-you java.lang.Long [input]
    (str "number => " inpu))

(who-are-you true) ;; No method in multimethod -> 대응되는 메소드가 정의되어있지 않음으로 에러를 던진다.

:default 키워드를 사용하여 디폴트 메소드를 지정할 수 있다.
(defmethod who-are-you :default [input]
    (str "i don't know " input))

이전 예제에서는 class가 먼저 호출된 다음 그 반환값을 이용하여 어느 메소드를 사용할지 결정

(defmulti eat-m (fn [height]
                    (if (< height 3) :grow :shrink)))
분기 함수가 반환하는 :grow, :shrink 키워드에 대응하는 메소드를 defmethod로 각각 정의해 줄 필요가 있다.
(defmethod eat-m :grow [_]
    ("Eat more"))
(defmethod eat-m :shrink [_]
    ("Eat no more"))

#### protocol 
멀티 메소드가 한 개의 함수를 대상으로 다형성을 구현하는 반면, 프로토콜은 다수의 함수를 대상으로 다형성을 훌륭하게 구현할 수 있다.

(defprotocol BigM
    (eat-m [this]))
(extend-protocol BigM
    java.lang.String
    (eat-m [this]
        (str (.toUpperCase this) " mmm tasty!"))
    
    clojure.lang.Keyword
    (eat-m [this]
        (case this
            :grow ">>>"
            :shrink "<<<"))
    
    java.lang.Long
    (eat-m [this]
        (if (< this 3)
            "should grow"
            "should shrink")))

(eat-m "dd") ;; => "DD mmm tasty!"

프로토콜을 이용하여 기존으 자료구조에 메소드를 추가했다. 하지만 새로운 자료구조를 만드려면 어떻게 해야할까?

#### 새로운 데이터 타입 추가
클로저에서는 새로운 데이터 타입을 정의하는 방법을 제공하는데 상황에 따라서 두가지 방법을 이용할 수 있다.

1. 구조적인 데이터가 필요하다면 defrecord를 이용 => 새로운 타입의 클래스를 생성
defrecrod는 그 클래스에 포함될 필드를 정의한다.

;; 색깔과 높이 정보를 필드로 같는 버섯 defrecord
(defrecrord Mushroom [color height])

이제 점 표기법을 이용해 새로운 Mushroom 객체를 생성
(def regular-mushroom (Mushroom. "white" 2))
(class regular-mushroom) ;; Mushroom -> class 함수가 반환한 타입이 defrecord로 정의한 타입과 같음

전 대시(.-)를 필드명 바로 앞에 붙여 그 값을 읽어올 수 있다.
(.-color regular-mushroom) ;; => "white"
(.-height regular-mushroom) ;; => 2

;; 두개의 함수를 지니는 Edible 프로토콜
(defprotocol Edible
    (bite-right-side [this]
    (bite-left-side [this])))

;; 이를 구현하는 WonderLandMushroom 레코드
(defrecord WonderLandMushroom [color height]
    Edible
    (bite-right-side [this]
        (str "The " color " bite makes you grow bigger"))
    (bite-left-side [this]
        (str "The " color " bite make you grow smaller")))

;; 이번에는 WonderLandMushroom과 비슷한 RegularMushRoom 레코드를 정의
;; -> 주된 차이는 구현된 함수들이 하는 일 => 버섯을 먹어도 커지거나 작아지지 않고 단지 맛이 없음만 나타내보자

(defrecord RegularMushroom [color height]
    Edible
    (eat-right-side [this]
        (str "The " color " bites taste bad"))
    (eat-left-side [this]
        (str "The " color " bites taste too bad")))

(def alice-mushroom (WonderLandMushroom. "blue" 3))
(def reg-mushroom (RegularMushroom. "red" 1))

(bite-right-side alice-mushroom) ;; => "The blue bites make you grow bigger"
(bite-right-side reg-mushroom)  ;; => "The red bites taste bad"

#### 프로토콜의 실제 사용
프로토콜이 실제로 쓰이는 예로는 여러 종류의 데이터 저장소에 데이터를 쓰는 경우를 들 수 있다.
=> 기록하는 정보는 동일하지만 각기 다른 종류의 저장소에 기록하고자 하는 경우 

#### deftype
이전 예제에서는 구조적인 데이터를 담는 레코드를 사용했다. -> 하지만 때때로 defrecord가 제공하고 있는 구조화된 데이터나 맵 형식의 접근 기능에는 관심없고 메모리를 아끼기 위해 단지 타입이 있는 객체를 원할때가 있다. 이런 경우에 deftype 을 사용한다.

(defprotocol Edible
    (bite-right-side [this])
    (bite-left-side [this]))

(deftype WonderLandMushroom []
    Edible
        (bite-right-side [this] "right")
        (bite-left-side [this] "left"))

defrecord와 deftype의 주된 차이점은 구조화된 데이터를 사용할지의 여부이다.
-> 레코드는 타입 기반으로 분기 처리가 되면서도 맴처럼 데이터를 다룰 수 있음(그러기에 재사용하기 좋다)
-> 하지만 구조화된 데이터가 필요 없는 경우 deftype을 이용하여 부하를 줄일 수 있다.

#### 프로토콜을 사용 안하고 구현하기
프로토콜을 쓰는 대신에 간단한 맵을 써서 어떤 종류의 버섯인지 구분할 수 있다.
(defn bite-right-side [mushroom]
    (if (= (:type mushroom) "wonderland") 
        "The bite make you grow bigger"
        "The bite taste bad"))
(bite-right-side {:type "wonderland"}) ;; => "The bite make you grow bigger"

프로토콜은 자주 사용하지 않는것이 좋다. 대부분의 상황에서는 순수 함수나 멀티 메소드를 대신 사용할 수 있다.
클로저의 장점 중 하나는 맵을 사용하다가 필요할 때 레코드로 쉽게 전환할 수 있다는 점이다. -> 이로 인해 프로토콜 사용 여부의 결정을 늦출 수 있다.

여기까지 구조화된 데이터와 타입, 인터페이스를 다루는 법에 대하여 배웠다.