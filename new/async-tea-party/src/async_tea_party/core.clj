(ns async-tea-party.core
  (:gen-class)                                              ;; gen-class를 추가하여 독립적으로 실행되는 클래스를 만든다.
  (:require [clojure.core.async
             :refer [>! <! >!! <!! chan close! go go-loop alts!]]))
(def google-tea-service-chan (chan 10))
(def yahoo-tea-service-chan (chan 10))
(def result-chan (chan 10))

; (def tea-channel (chan))
;;; 채널에 값을 넣고 빼는 방법에는 동기, 비동기적인 방법이 있다.
;
;;; 1. 동기적인 방식
;;; 대기(blocking) 입력과 대기 출력을 사용
;;; - 대기 입력은 >!!을 사용하는데, 채널에 데이터를 동기적으로 넣는다.
;;; - 대기 출력은 <!!을 사용하는데 채널에서 데이터를 동기적으로 빼낸다.
;;;
;;; tea-channel에 차 한잔을 넣는다고 했을때 tea-channel은 버퍼가 없어서 값을 빼낼때까지 메인 스레드가 멈추게 된다(blocking)
;;; => 이러면 REPL과 프로그램의 실행이 정지된다.
;;; 고로 버퍼를 지정하면 -> 채널에 값을 넣어도 버퍼가 다 차기 전까지는 메인 스레드의 실행이 멈추지 않는다.
;;;
;(def tea-channel (chan 10))
;(>!! tea-channel :cup-of-tea)                               ;; true
;(<!! tea-channel)                                           ;; cup-of-tea
;
;;; 채널을 닫는 경우
;(>!! tea-channel :cup-of-tea-2)                             ;; true
;(>!! tea-channel :cup-of-tea-3)                             ;; true
;(>!! tea-channel :cup-of-tea-4)                             ;; true
;
;(close! tea-channel)                                        ;; nil
;
;(<!! tea-channel)                                           ;; :cup-of-tea-2
;(<!! tea-channel)                                           ;; :cup-of-tea-3
;(<!! tea-channel)                                           ;; :cup-of-tea-4
;(<!! tea-channel)                                           ;; nil
;
;;; 2. 비동기적인 방식
;;go 블럭 안에서 비동기 입출력을 할 수 있다.
;;- >! 은 비동기 입력을 나타낸다
;;- <! 는 비동기 출력을 나타낸다.
;
;(let [tea-channel (chan)]
;  (go (>! tea-channel :cup))
;  (go (println "Thanks for the " (<! tea-channel))))
;
;;; go-loop를 사용하면 채널에 값이 들어오기를 기다렸다가 가져오기를 반복할 수 있다.
;(go-loop []
;  (println "thanks for the " (<! tea-channel))
;  (recur))
;;go-loop가 실행되면 이 반복문은 백그라운드에서 tea-channel에 값이 들어오기를 기다린다.
;;값이 들어오면 println 을 실행하고 다시 입력을 기다린다.
;
;;go-loop는 특뱔한 스레드 풀에서 관리된다. 채널에서 값을 가져올때까지 대기하게되어 실행이 멈추게 된다.
;;go-loop는 go 블럭 안에 loop가 있는것처럼 동작한다.
;;=> 즉 채널에 값이 있을때는 값을 가져오고 다시 처음으로 재귀한 후 다음 값을 기다린다.
;
;;; alts!
;;여러개의 채널에서 가장 먼저 도착한 채널의 값을 가져오는 예제
;(def tea-channel (chan 10))
;(def milk-channel (chan 10))
;(def sugar-channel (chan 10))
;
;(go-loop []
;  (let [[v ch] (alts! [tea-channel
;                       milk-channel
;                       sugar-channel])]
;    (println "Got " v "from " ch)
;    (recur)))
;
;;go 블럭은 스레드에 묶이지 않는 매우 가벼운 프로세스인데다 여러 채널에서 값을 받을 수 있어 매우 강력하다.
;;채널은 얼마든지 많이 사용할수 있다 -> 주 스레드의 실행을 정지시키지않고 네트워크로 들어오는 다수의 접속 지점들로부터 정보를 받으려고 할때 유용한 기능이다.

(defn random-add []
  (reduce + (repeat (rand-int 100000) 1)))

;; go 블럭 안에서 임의이 시간(랜덤이 계산되는 시간) 대기가 끝나면 구글 채널에 문자열을 넣어 티가 준비되었음을 알림
(defn request-google-tea-service []
  (go
    (random-add)
    (>! google-tea-service-chan "tea compliments of google")))

(defn request-yahoo-tea-service []
  (go
    (random-add)
    (>! yahoo-tea-service-chan "tea compliments of yahoo")))

;; 구글과 야후 모두에게 차를 요청하고
;; 두 채널들 중에서 먼저 값이 도착하는 값을 출력한다.
;; request-tea는 비동기적으로 동작할것이기때문에 콘솔창에서는 결과가 비동기적으로(프로그램이 종료된 이후에도) 출력될것이다.
(defn request-tea []
  (request-google-tea-service)
  (request-yahoo-tea-service)
  (go (let [[v] (alts!
                  [google-tea-service-chan
                   yahoo-tea-service-chan])]
        (>! result-chan v))))

(defn -main [& args]
  (println "Requesting tea!")
  (request-tea)
  (println (<!! result-chan)))
