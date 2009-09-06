; Project Euler problems in Clojure

; Utility functions used in multiple solutions

(ns euler
  (:use clojure.contrib.command-line)
  (:use clojure.contrib.combinatorics))

(defn sqrt [n] (. Math sqrt n))

(defn factor? [n f]
    (zero? (rem n f)))

(defn str-to-int [s]
    (. (. Integer parseInt s) intValue))

(def char-to-int (comp str-to-int str))

(defn square [n]
    (* n n))

; Sucky, I know
(defn prime?
    ([x] (prime? x 2 (int (sqrt x))))
    ([x i max]
        (if (> i max)
            true
            (if (factor? x i)
                nil
                (recur x (+ i 1) max)))))

; Problem 1
; If we list all the natural numbers below 10 that are multiples of 3 or 5, we get 3, 5, 6 and 9. The sum of these multiples is 23.
; Find the sum of all the multiples of 3 or 5 below 1000.

(defn multiple? [ns potential-multiple]
    (if (zero? (count ns))
        nil
        (if (factor? potential-multiple (first ns))
            true
            (recur (rest ns) potential-multiple))))

(defn sum-of-all-multiples
    ([ns max] (sum-of-all-multiples ns max 0))
    ([ns max acc]
        (if (zero? max)
            acc
            (recur ns (- max 1) (+ acc (if (multiple? ns max) max 0))))))

;;(assert (= (sum-of-all-multiples [3 5] 9) 23))

(defn euler1 []
    (sum-of-all-multiples [3 5] 999))

;;(assert (= 233168 (euler1)))


; Alternate solution using lazy sequence + filter + anonymous functions, more clojure-thonic
; From here:
; http://ubermenschconsulting.com/blog/2008/12/01/project-euler-in-clojure-problem-1
(defn euler1-alt []
    (apply + (filter #(or (zero? (rem % 3))
                          (zero? (rem % 5)))
                     (range 1000))))

;;(assert (= 233168 (euler1-alt)))


; Problem 2
; Each new term in the Fibonacci sequence is generated by adding the previous two terms. By starting with 1 and 2, the first 10 terms will be:
; 1, 2, 3, 5, 8, 13, 21, 34, 55, 89, ...
; Find the sum of all the even-valued terms in the sequence which do not exceed four million.

(defn sum-fib [a b max acc]
    (if (>= a max)
        acc
        (recur b (+ a b) max (+ acc (if (factor? a 2) a 0)))))

(defn euler2 []
    (sum-fib 1 2 4000000 0))

;;(assert (=  4613732 (euler2)))


; Problem 3
; The prime factors of 13195 are 5, 7, 13 and 29.
; What is the largest prime factor of the number 600851475143 ?

; Lame impl, but should do the trick for now
; May also be an edge case here where we need to round sqrt up instead of truncating with int or whatever... write some tests.

; Better prime finder may be here: http://bigdingus.com/2008/07/01/finding-primes-with-erlang-and-clojure/

(defn prime-factor? [n f]
    (and (factor? n f) (prime? f)))

; May be edge case problem here with sqrt as above
(defn largest-prime-factor
    ([n] (largest-prime-factor n (int (sqrt n))))
    ([n f]
        (if (prime-factor? n f)
            f
            (recur n (- f 1)))))

;;(assert (= (largest-prime-factor 13195) 29))

(defn euler3 []
    (largest-prime-factor 600851475143))

;;(assert (= 6857 (euler3)))

; Problem 4
; Answer: 906609
; A palindromic number reads the same both ways. The largest palindrome made from the product of two 2-digit numbers is 9009 = 91  99.
; Find the largest palindrome made from the product of two 3-digit numbers.

(defn palindrome? [s]
    (let [len (count s)]
        (and
            (= (first s) (last s))
            (or (>= 1 len)
                (recur (. s (substring 1 (dec len))))))))

(defn palindrome-start-digit
    ([n] (palindrome-start-digit n ""))
    ([n s]
        (if (<= n 0)
            (str-to-int s)
            (recur (- n 1) (str s "9")))))

(defn largest-palindromic-number
    ([num-digits]
        (let [start (palindrome-start-digit num-digits)]
            (largest-palindromic-number num-digits start start start 0)))
    ([num-digits x y start acc]
        (if (< (count (str x)) num-digits)
            acc
            (if (< (count (str y)) num-digits)
                (recur num-digits (- x 1) start start acc)
                (let [n (* x y)]
                    (recur num-digits x (- y 1) start (if (and (> n acc ) (palindrome? (str n))) n acc)))))))

;;(assert (= (largest-palindromic-number 2)))

(defn euler4 []
    (largest-palindromic-number 3))

;;(assert (= 906609 (euler4)))

;; Problem 5
;; 2520 is the smallest number that can be divided by each of the
;; numbers from 1 to 10 without any remainder.
;; What is the smallest number that is evenly divisible by all of the numbers from 1 to 20?
;
(defn divisible-by-all? [n fs]
    (every? #(factor? n %) fs))

(defn find-divisible-by-all
    ([min max] (find-divisible-by-all min max max))
    ([min max n]
        (if (divisible-by-all? n (range min max))
            n
            (recur min max (+ n 1)))))

;;(assert (= (find-divisible-by-all 1 10) 2520))

(defn euler5 []
    (find-divisible-by-all 1 20))

;;(assert (= 232792560 (euler5))) ; Too Slow.  Pls fix.


;; Problem 6
;; The sum of the squares of the first ten natural numbers is
;; 12 + 22 + ... + 102 = 385
;; The square of the sum of the first ten natural numbers is
;; (1 + 2 + ... + 10)2 = 552 = 3025
;; Hence the difference between the sum of the squares of the first ten natural numbers and the square of the sum is 3025  385 = 2640.
;;
;; Find the difference between the sum of the squares of the first one hundred natural numbers and the square of the sum.

(defn sum-of-squares [ns]
    (apply + (map square ns)))

(defn square-of-sum [ns]
    (square (apply + ns)))

(defn euler6 []
    (let [r (range 1 101)]
        (- (square-of-sum r) (sum-of-squares r))))

;;(assert (= 25164150 (euler6)))


;; Problem 7
;; By listing the first six prime numbers: 2, 3, 5, 7, 11, and 13, we can see that the 6th prime is 13.
;; What is the 10001st prime number?

(defn find-nth-prime
    ([nth] (find-nth-prime nth 2 0))
    ([nth n count]
        (if (prime? n)
            (let [current (+ count 1)]
                (if (= nth current)
                    n
                    (recur nth (+ n 1) current)))
            (recur nth (+ n 1) count))))

;;(assert (= (find-nth-prime 6) 13))

(defn euler7 []
    (find-nth-prime 10001))

;;(assert (= 104743 (euler7)))

;; Problem 8
;; Find the greatest product of five consecutive digits in the 1000-digit number.
;(def prob8-data (str "73167176531330624919225119674426574742355349194934"
;                     "96983520312774506326239578318016984801869478851843"
;                     "85861560789112949495459501737958331952853208805511"
;                     "12540698747158523863050715693290963295227443043557"
;                     "66896648950445244523161731856403098711121722383113"
;                     "62229893423380308135336276614282806444486645238749"
;                     "30358907296290491560440772390713810515859307960866"
;                     "70172427121883998797908792274921901699720888093776"
;                     "65727333001053367881220235421809751254540594752243"
;                     "52584907711670556013604839586446706324415722155397"
;                     "53697817977846174064955149290862569321978468622482"
;                     "83972241375657056057490261407972968652414535100474"
;                     "82166370484403199890008895243450658541227588666881"
;                     "16427171479924442928230863465674813919123162824586"
;                     "17866458359124566529476545682848912883142607690042"
;                     "24219022671055626321111109370544217506941658960408"
;                     "07198403850962455444362981230987879927244284909188"
;                     "84580156166097919133875499200524063689912560717606"
;                     "05886116467109405077541002256983155200055935729725"
;                     "71636269561882670428252483600823257530420752963450"))
;
;(defn product-of-n-consecutive-digits-at [n at s]
;    (apply * (map char-to-int (. s substring at (+ at 5)))))
;
;(product-of-n-consecutive-digits-at 5 0 prob8-data)
;
;(defn euler8 []
;    (apply max (map #(product-of-n-consecutive-digits-at 5 % prob8-data) (range 0 (- (count prob8-data) 5)))))
;
;(assert (= 40824 (euler8)))
;
;
;; Problem 9
;;A Pythagorean triplet is a set of three natural numbers, a  b  c, for which
;; a^2 + b^2 = c^2
;; For example, 3^2 + 4^2 = 9 + 16 = 25 = 5^2.
;;
;; There exists exactly one Pythagorean triplet for which a + b + c = 1000.
;; Find the product abc.
;
;(defn find-a-pythagorean-triplet-with-sum
;    ([sum] (find-a-pythagorean-triplet-with-sum sum 0 0 0))
;    ([sum a b c]
;        (if (and (= (+ (square a) (square b)) (square c)) (= (+ a b c) sum))
;            (list a b c)
;            (if (>= c sum)
;                (if (>= b sum)
;                    (if (>= a sum)
;                        (throw "should not get here")
;                        (recur sum (+ a 1) (+ a 2) (+ a 3)))
;                    (recur sum a (+ b 1) (+ b 2)))
;                (recur sum a b (+ c 1))))))
;
;(defn pythagorean-product [sum]
;    (apply * (find-a-pythagorean-triplet-with-sum sum)))
;
;(assert (= 60 (pythagorean-product 12)))
;
;(defn euler9 []
;    (pythagorean-product 1000))
;
;(assert (= 31875000 (euler9))) ; Slow
;
;
;; Problem 10
;; The sum of the primes below 10 is 2 + 3 + 5 + 7 = 17.
;; Find the sum of all the primes below two million.
;
;(defn sum-of-primes-below
;    ([max] (sum-of-primes-below max 2 0))
;    ([max n acc]
;        (if (>= n max)
;            acc
;            (recur max (+ n 1) (+ (if (prime? n) n 0) acc)))))
;
;(def sum-of-primes-below (memoize sum-of-primes-below))
;
;(assert (= 17 (sum-of-primes-below 10)))
;
;(defn euler10 []
;    (sum-of-primes-below 2000000))
;
;(assert (= 142913828922 (euler10)))
;
;
;;;; Problem 11
;;; The sequence of triangle numbers is generated by adding the natural numbers. So the 7^(th) triangle number would be 1 + 2 + 3 + 4 + 5 + 6 + 7 = 28. The first ten terms would be:
;;;
;;; 1, 3, 6, 10, 15, 21, 28, 36, 45, 55, ...
;;;
;;; Let us list the factors of the first seven triangle numbers:
;;;
;;;     1: 1
;;;     3: 1,3
;;;     6: 1,2,3,6
;;;    10: 1,2,5,10
;;;    15: 1,3,5,15
;;;    21: 1,3,7,21
;;;    28: 1,2,4,7,14,28
;;;
;;; We can see that 28 is the first triangle number to have over five divisors.
;;;
;;; What is the value of the first triangle number to have over five hundred divisors?
;
;;; TODO: Memoize
;(defn triangle-number [n]
;    (apply + (range 1 (inc n))))
;
;(assert (= 28 (triangle-number 7)))
;
;(defn first-n-divisors-of-m
;    ([n m]
;     "Returns the first n divisors of m.  If there are less than n, all divisors are returned."
;     (first-n-divisors-of-m n m 2 []))
;    ([n m i acc]
;     (if (or (>= (count acc) n) (> i (/ m 2)))
;         acc
;         (recur n m (+ i 1) (if (factor? m i)
;                                (conj acc i)
;                                acc)))))
;
;(defn find-lowest-triangle-number-with-more-than-n-divisors
;   ([n] find-lowest-triangle-number-with-more-than-n-divisors n 1)
;   ([n i]
;    (println "wtf")))
;;;     (let [t (triangle-number i)
;;;         divisors (first-n-divisors-of-m (inc n) t)]
;;;       (println (str "t " t " divisors " divisors))
;;;       (if (> (count divisors) n)
;;;       t
;;;       (recur n (+ i 1))))))
;
;
;;(defn euler11 []
;;  (find-lowest-triangle-number-with-more-than-n-divisors 500))

;; Problem 16
;; Answer: 1366
;; 2^(15) = 32768 and the sum of its digits is 3 + 2 + 7 + 6 + 8 = 26.
;; What is the sum of the digits of the number 2^(1000)?

(defn sum [xs]
  (apply + xs))

(defn expt
  ([n p] (expt n n p))
  ([r n p]
    (if (<= p 1)
      r
      (recur (* n r) n (dec p)))))

(defn digits [n]
  (map #(Character/digit % 10) (str n)))

(defn sum-digits [n]
  (sum (digits n)))

(defn euler16 []
  (sum-digits (expt 2 1000)))


;; Problem 20
;; Answer: 648
;; n! means n  (n * 1)  ...  3 * 2 * 1
;; Find the sum of the digits in the number 100!

(defn fac [n]
  (apply * (range 2 (inc n))))

(defn euler20 []
  (sum-digits (fac 100)))


;; Problem #29
;; Answer: 9183

(defn euler29 []
  (count
   (distinct
    (map #(apply expt %)
         (cartesian-product (range 2 101) (range 2 101))))))

;; Problem #34
;; Answer:
;; 145 is a curious number, as 1! + 4! + 5! = 1 + 24 + 120 = 145.
;; Find the sum of all numbers which are equal to the sum of the factorial of their digits.
;; Note: as 1! = 1 and 2! = 2 are not sums they are not included.

(defn fac-of-digits [n]
  (sum (map fac (digits n))))

(defn do-it-doug []
  (loop [i 10]
    (println (str "i " i ": " (fac-of-digits i)))
    (read-line)
    (recur (inc i))))


;; Problem 14
;; Answer: 837799
;; (Slow)
;;
;; The following iterative sequence is defined for the set of positive integers:
;;
;; n -> n/2 (n is even)
;; n -> 3n + 1 (n is odd)
;;
;; Using the rule above and starting with 13, we generate the following sequence:
;;
;; 13  40  20  10  5  16  8  4  2  1
;; It can be seen that this sequence (starting at 13 and finishing at 1) contains 10
;; terms. Although it has not been proved yet (Collatz Problem), it is thought that
;; all starting numbers finish at 1.
;;
;; Which starting number, under one million, produces the longest chain?
;;
;; NOTE: Once the chain starts the terms are allowed to go above one million.

(defn next-collatz [n]
  (cond (= 1 n) nil
        (even? n) (/ n 2)
        :else (+ (* 3 n) 1)))

(defn collatz [n]
  (take-while (complement nil?) (iterate next-collatz n)))

(defn find-longest-collatz-under
  ([max]
     (find-longest-collatz-under max 0 0))
  ([max len n]
     (if (= max 0)
       n
       (let [cur (collatz max)
             cur-len (count cur)]
         (if (> cur-len len)
           (recur (dec max) cur-len max)
           (recur (dec max) len n))))))


(defn euler14 []
  (find-longest-collatz-under 1000000 0 0))

;; TODO: use with-command-line-args to do some stuff.

(defn between [n a z]
  (and (< a n)
       (> z n)))

(defn between-inclusive [n a z]
  (between n (dec a) (inc z)))

(defn e40-n-at [s target start]
  (let [target-adj-for-partial-string (- target start)
        target-adj-for-string-indexing (dec target-adj-for-partial-string)
        c (.charAt s target-adj-for-string-indexing)
        i (int c)]
    (- i 48)))

(defn e40-ds
  ([ns]
     (e40-ds ns 0 1 []))
  ([ns start i rs]
     (if (<= (count ns) 0)
       rs
       (let [s (str i)
             end (+ start (count s))
             target (first ns)]
         (if (between-inclusive target start end)
           (recur (rest ns)
                  start
                  i
                  (conj rs (e40-n-at s target start)))
           (recur ns
                  end
                  (inc i)
                  rs))))))

(defn euler40 []
  (apply * (e40-ds [1 10 100 1000 10000 100000 1000000])))

(defn triangle-number?
  ([n]
     (if (= 1 n)
       true
       (triangle-number? (- n 1) 2)))
  ([n m]
     (if (= n m)
       true
       (if (> m n)
         false
         (recur (- n m) (inc m))))))

(defn alphabet-position [c]
  (- (int (Character/toLowerCase c)) 96))

(defn word-value [word]
  (sum (map alphabet-position word)))

(defn triangle-word? [word]
  (triangle-number? (word-value word)))

(defn count-triangle-words [words]
  (sum (map #(if (triangle-word? %) 1 0) words)))

(defn e42-word-seq [text]
  (re-seq #"\"([^\"]+)\",?" text))

(defn get-euler42-words [text]
  (map second (e42-word-seq text)))

(defn count-triangle-words-in-file [filename]
  (count-triangle-words (get-euler42-words (slurp filename))))

(defn euler42 []
  (count-triangle-words-in-file
   "/home/john/src/polyeuler/inputs/42/words.txt"))