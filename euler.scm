;; Euler #1
;; Answer: 233168
;;
;; If we list all the natural numbers below 10 that are multiples of 3 or 5,
;; we get 3, 5, 6 and 9. The sum of these multiples is 23.
;;
;; Find the sum of all the multiples of 3 or 5 below 1000.

(define (e1-accumulate n max acc)
    (if (>= n max)
        acc
        (e1-accumulate (+ n 1) max (if (or (= 0 (modulo n 3))
                                           (= 0 (modulo n 5)))
                                       (+ acc n)
                                       acc))))

(define (euler1)
    (e1-accumulate 1 1000 0))


;; Euler #2
;; Answer: 4613732
;;
;; Each new term in the Fibonacci sequence is generated by adding the previous
;; two terms. By starting with 1 and 2, the first 10 terms will be:
;;
;; 1, 2, 3, 5, 8, 13, 21, 34, 55, 89, ...
;;
;; Find the sum of all the even-valued terms in the sequence which do not
;; exceed four million.

(define (e2-accumulate a b acc)
    (if (>= b 4000000)
        acc
        (let ((c (+ a b)))
            (e2-accumulate b c (if (even? c)
                                   (+ acc c)
                                   acc)))))

(define (euler2)
    (e2-accumulate 1 2 2))


;; Euler #3:
;; Answer: 6857
;;
;; The prime factors of 13195 are 5, 7, 13 and 29.
;;
;; What is the largest prime factor of the number 600851475143 ?

(define (prime?-acc n x)
    (if (or (< x 2) (= n 2))
        #t
        (if (= 0 (modulo n x))
            #f
            (prime?-acc n (- x 1)))))

(define (prime? n)
    (prime?-acc n (inexact->exact (ceiling (sqrt n)))))

(define (euler3-accumulate f n)
    (if (and (= 0 (modulo n f)) (prime? f))
        f
        (euler3-accumulate (- f 1) n)))

(define (euler3)
    (let ((x 600851475143))
        (euler3-accumulate (inexact->exact (ceiling (sqrt x))) x)))


;; Problem #4
;; Answer: 906609
;;
;; A palindromic number reads the same both ways. The largest
;; palindrome made from the product of two 2-digit numbers is 9009 =
;; 91 99.
;;
;; Find the largest palindrome made from the product of two 3-digit
;; numbers.

;; This is supposed to be in the language but I can't find it / figure out
;; how to import the right namespace / whatever, so...
(define (reverse-string s)
    (list->string (reverse (string->list s))))

(define (is-palindromic-number n)
    (let ((s (number->string n)))
        (string=? s (reverse-string s))))

(define (euler4-accumulate x y acc)
    (if (> y 999)
        acc
        (if (> x 999)
            (euler4-accumulate 100 (+ 1 y) acc)
            (let ((p (* x y)))
                (euler4-accumulate (+ 1 x) y (if (and (> p acc) (is-palindromic-number p))
                                                  p
                                                  acc))))))

(define (euler4)
    (euler4-accumulate 100 100 0))
