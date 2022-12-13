(ns serpent-talk.talk-test
  (:require [clojure.test :refer :all]
            [serpent-talk.talk :refer :all]))
;; 이름공간이 테스트파일로 바뀌었고
;; closure.test 라이브러리를 :refer :all로 불러와서 앞에 prefix 안붙이고도 쓸 수 있음
;; 그리고 테스트하려는 대상도 불러옴

(deftest a-test                                             ;; deftest는 테스트 함수를 정의하고
  (testing "FIXME, I fail."                                 ;; testing은 deftest에서 사용되며 무엇을 테스트 하는지에 대해 설명한다
    (is (= 1 1))))                                          ;; is는 실제 테스트를 하는 단언문(assertion)을 제공한다.

(deftest test-serpent-talk
  (testing "Cries serpent! with a snake_case version of the input"
    (is (= "Serpent! You said: hello_there" (serpent-talk "hello there")))))