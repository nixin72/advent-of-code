#lang racket

(define (strip-front list el)
  (cond [(null? list) '()]
        [(= (car list) el) (strip-front (cdr list) el)]
        [else list]))

(define (number->list n . args)
  (let ((b (if (null? args) 10 (car args))))
    (let loop ((n n) (d '()))
      (if (zero? n) d
          (loop (quotient n b) (cons (modulo n b) d))))))

(define (sorted? digits)
  (if (null? (cdr digits)) #t
    (and (<= (car digits) (cadr digits))
         (sorted? (cdr digits)))))

(define (duplicates? digits)
  (cond [(or (= (length digits) 0) (= (length digits) 1)) #f]
        [(= (length digits) 2) (= (car digits) (cadr digits))]
        [(and (= (car digits) (cadr digits)) (= (car digits) (caddr digits)))
         (duplicates? (strip-front digits (car digits)))]
        [(= (car digits) (cadr digits)) #t]
        [else (duplicates? (cdr digits))]))

(let ([hits 0])
  (for ([n (in-range 307237 769059)])
    (define digits (number->list n))
    (if (and (sorted? digits) (duplicates? digits))
        (set! hits (+ 1 hits))
        (void)))
  (println hits))