
(ns hello.practice)


;; problem 26 - 파보나치수열 문제

;; 다음 식이 참이되도록 빈칸에 알맞는 함수를  만들어라
;; (= (__ 3) '(1 1 2))
;; (= (__ 6) '(1 1 2 3 5 8))
;; (= (__ 8) '(1 1 2 3 5 8 13 21))

;; [1 1], [1 2], [2 3], [3 5] ...
;; [a b], [b (+ a b)] ... 와 같은 식으로 시퀀스를 만들고
(let [_ ((fn [[a b]] [b (+ a b)]) [1 1])])
;; 여기서 첫번째만 빼내면 [1 1 2 3 5....]
(map first ((fn [[a b]] [b (+ a b)]) [1 1]))

;; 이 중 원하는 길이만큼 파보나치수열을 만드는 함수는
#(take % (map first ((fn [[a b]] [b (+ a b)]) [1 1])))


;; problem 27 - Palindrome Detector

;; (false? (__ '(1 2 3 4 5)))
;; (true? (__ "racecar"))
;; (true? (__ [:foo :bar :foo]))
;; (true? (__ '(1 1 3 3 1 1)))
;; (false? (__ '(:a :b :c)))

(fn [seq]
  (cond
    (< (count seq) 2) true
    (= (first seq) (last seq)) (recur (rest (drop-last seq)))
    :else false))
