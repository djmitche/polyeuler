;; Project Euler in Common Lisp (specifically SBCL)
;; John Evans <john@jpevans.com>
;; Project Euler Account name: john-sbcl

;; Misc notes for myself:
;; To create an image:
;;     (load "euler.lisp")
;;     (sb-ext:save-lisp-and-die "foo.core")

;; Problem 1
;; Answer: 233168
;;
;; If we list all the natural numbers below 10 that are multiples of 3 or 5,
;; we get 3, 5, 6 and 9. The sum of these multiples is 23.
;;
;; Find the sum of all the multiples of 3 or 5 below 1000.

(defun e1acc (n max acc)
    (if (>= n max)
        acc
        (e1acc (+ n 1)
               max
               (if (or 
                       (= 0 (mod n 3)) 
                       (= 0 (mod n 5)))
                   (+ n acc)
                   acc))))

(defun euler1 ()
    (e1acc 1 1000 0))


;; Problem 2
;; Answer: 4613732
;;
;; Each new term in the Fibonacci sequence is generated by adding the previous 
;; two terms. By starting with 1 and 2, the first 10 terms will be:
;;
;; 1, 2, 3, 5, 8, 13, 21, 34, 55, 89, ...
;; 
;; Find the sum of all the even-valued terms in the sequence which do not 
;; exceed four million.
;; Note: This problem has been changed recently, please check that you are 
;; using the right parameters.

(defun euler2-iter (x y s)
  (if (> y 4000000)
      s
      (if (= (mod y 2) 0)
          (euler2-iter y (+ y x) (+ s y))
          (euler2-iter y (+ y x) s))))


(defun euler2 ()
    (euler2-iter 1 1 0))


(defconstant *eulers*
    '(#'euler1 
      #'euler2))

(defun main ()
   (if (> (length *posix-argv*) 1)
    ;; For each arg, run the appropriate euler
       (loop for arg in (cdr *posix-argv*) do
           (print "would eval here"))
       (loop for euler in *eulers* do
           ; I feel like the eval shouldn't be necessary...
           (print (funcall (eval euler))))))

(main)
