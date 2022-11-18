# Introduction to hello

TODO: write [great documentation](http://jacobian.org/writing/what-to-write/)


### leiningen 프로젝트 관리툴 사용하기
: leiningen 은 클로저의 프로젝트 관리 툴

### 실행
lein run 

### 설치
closure는 jvm에서 돌아가기때문에 Closure 코드를 class 파일로 컴파일해서 java로 실행 가능

### 실행
lein run 명령어로 파일을 실행
-m 옵션의 인자로 네임스페이스를 지정해주면됨
```
$lein run -m hello.core
```

#### 컴파일해서 java로 실행하기
lein complie 명령어로 컴파일 할 수 있음
```
$lein complie hello.core
```
컴파일이되면 target/classes 디렉토리 아래 클래스 파일이 생성된다.

#### 실행하기
closure는 namespace 단위로 class를 만들지 않고 function 단위로 class를 만든다. java로 class를 실행하기 위해서는 자바 main 함수가 있는 클래스를 만들어야함.
네임스페이스를 클래스로 만드려면 ns 구문에 :gen-class 라는 지시문을 넣어주면 된다(그냥 실행할때 이 지시어가 없는 이유는 클래스를 생성하지 않고 동적으로 evaluation 하기 때문)