#lang racket
(require racket/vector)

(define int-code
  (list->vector
   (map string->number (string-split (car (file->lines "input")) ","))))

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

(vector-set! int-code 1 12)
(vector-set! int-code 2 2)

(println (evaluate int-code 0))
