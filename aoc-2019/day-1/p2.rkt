#lang racket

(define (calc-fuel mass-or-fuel)
  (writeln mass-or-fuel)
  (let ([new-fuel (- (floor (/ mass-or-fuel 3)) 2)])
    (if (<= new-fuel 0) 0
      (+ new-fuel (calc-fuel new-fuel)))))

(writeln (apply + (map (lambda (n) (calc-fuel n)) (file->list "p1.input"))))
