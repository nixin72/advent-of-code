#lang racket
(require racket/vector)

(define int-codes
  (map string->number (string-split (car (file->lines "input")) ",")))

(define (evaluate int-code start-pos)
  (let* ([op-code (vector-ref int-code (+ start-pos 0))]
	 [point-1 (vector-ref int-code (+ start-pos 1))]
	 [point-2 (vector-ref int-code (+ start-pos 2))]
	 [save-to (vector-ref int-code (+ start-pos 3))]
	 [value-1 (vector-ref int-code point-1)]
	 [value-2 (vector-ref int-code point-2)])
    (cond
      [(or (= op-code 1) (= op-code 2))
       (vector-set! int-code save-to
		    (if (= op-code 1) (+ value-1 value-2) (* value-1 value-2)))
       (evaluate int-code (+ 4 start-pos))]
      [(= op-code 99) int-code]
      [else
	(println "Error: unknown opcode")
	int-code])))

(define (copy-list list)
  (cond
    [(null? list) '()]
    [else (cons (car list) (cdr list))]))

(define (try-pair int-code noun verb want)
  (set! int-code (list->vector int-code))
  (vector-set! int-code 1 noun)
  (vector-set! int-code 2 verb)
  (vector-ref (evaluate int-code 0) 0))

(define (try-combinations int-code want)
  (define found #f)
  (for ([i 99]) #:break (equal? found #t)
       (for ([j 99]) #:break (equal? found #t)
            (define result (try-pair (copy-list int-code) i j want))
            (if (= want result)
                (set! found (cons i j))
	        (set! found found))))
  found)

(println (try-combinations int-codes 19690720))
