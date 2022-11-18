;; ns는 네임스페이스를 뜻함
;; (ns hello.core)

;; 실행을 위한 클래스 생성
(ns hello.core (:gen-class))

;; defn은 함수를 정의하는 구문
(defn -main [] (println "안녕하십니까"))