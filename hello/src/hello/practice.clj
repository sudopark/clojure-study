
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


;; problem 28 - Flatten

;; (= (__ '((1 2) 3 [4 [5 6]])) '(1 2 3 4 5 6))
;; (= (__ ["a" ["b"] "c"]) '("a" "b" "c"))
;; (= (__ '((((:a))))) '(:a))

(fn flt [[x & xs]]
  (if x ;; head가 있는 경우에만 -> 없으면 nil?
    (if (sequential? x)  ;; head가 시퀀스이면
      (concat (flt x) (flt xs)) ;; head랑 rest를 flatten 해라
      (cons x (flt xs))))) ;;  header + flatten 된 rest

;; problem 30 - compress sequence

;; (= (apply str (__ "Leeeeeerrroyyy")) "Leroy")
;; (= (__ [1 1 2 3 3 2 2 3]) '(1 2 3 2 3))
;; (= (__ [[1 2] [1 2] [3 4] [1 2]]) '([1 2] [3 4] [1 2]))

reduce (fn [acc ele] (if (= (last acc) ele) acc (conj acc ele))) []
;; 다르게 생각 해보면 각 시퀀스의 엘리먼트로 partition-by하고 아 파티션들의 head 만 취해주면 된다
#(map first (partition-by identity %))
;; partition-by identity % -> identity 결과가 다를때마다 나눔


;;  problem 33 - Replicate a sequence

;; (= (__ [1 2 3] 2) '(1 1 2 2 3 3))
;; (= (__ [:a :b] 4) '(:a :a :a :a :b :b :b :b))
;; (= (__ [4 5 6] 1) '(4 5 6))
;; (= (__ [[1 2] [3 4]] 2) '([1 2] [1 2] [3 4] [3 4]))
;; (= (__ [44 33] 2) [44 44 33 33])

(fn [seq n] (reduce #(concat %1 (take n (repeat %2))) [] seq))


;; problem 34 - Implement range

;; (= (__ 1 4) '(1 2 3))
;; (= (__ -2 2) '(-2 -1 0 1))
;; (= (__ 5 8) '(5 6 7))

(fn [start end] (take (- end start) (iterate inc start)))

;; problem 38 - Maximum value

;; (= (__ 1 8 3 4) 8)
;; (= (__ 30 20) 30)
;; (= (__ 45 67 11) 67)

(fn [ & xs] (reduce #(if (< %1 %2) %2 %1) xs))


;; problem 39 - Interleave Two Seqs

;; (= (__ [1 2 3] [:a :b :c]) '(1 :a 2 :b 3 :c))
;; (= (__ [1 2] [3 4 5 6]) '(1 3 2 4))
;; (= (__ [1 2 3 4] [5]) [1 5])
;; (= (__ [30 20] [25 15]) [30 25 20 15])