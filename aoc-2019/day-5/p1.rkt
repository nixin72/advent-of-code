#lang racket

(define int-code
  (list->vector
    (map string->number (string-split (car (file->lines "input")) ","))))

(struct opcode (a b c de) #:prefab #:extra-name OpCode)
(define (parse-opcode oc)
  (println oc)
  (define code (modulo oc 100))
  (set! oc (number->string oc))
  (cond
    [(or (= (string-length oc) 1) (= (string-length oc) 2)) (opcode #f #f #f code)]
    [(= (string-length oc) 3) (opcode #f #f #t code)]
    [(= (string-length oc) 4) (opcode #f #t (string-ref oc 1) code)]
    [(= (string-length oc) 5) (opcode #t (string-ref oc 1) (string-ref oc 2) code)]
    [else (println (string-append "Unexpected opcode" oc)) (exit)]))

(define (*p pointer) (vector-ref int-code pointer))
(define (&= pointer value) (vector-set! int-code pointer value))
(define (**p pointer) (*p (*p pointer)))
(define (++ value) (+ 1 value))

(define (get location access-mode)
  (if access-mode (vector-ref int-code location)
                  (vector-ref int-code (vector-ref int-code location))))

(define (evaluate sp)
  (let* ([op-code (parse-opcode (vector-ref int-code sp))])
    (match op-code
      [(OpCode a b c 01) (&= (if a (+ 1 sp) (*p (+ 1 sp)))
                             (+ (get (+ 1 sp) c) (get (+ 2 sp) b)))
                         (evaluate (+ sp 4))]
      [(OpCode a b c 01) (&= (get (+ 3 sp) a) (* (get (+ 1 sp) c) (get (+ 2 sp) b)))
                          (evaluate (+ sp 4))]
      [(OpCode _ _ _ 03) (&= (*p (++ sp)) (read)) (evaluate (+ 2 sp))]
      [(OpCode _ _ c 04) (println (*p sp))
                        ; (println (if c (*p (++ sp)) (**p (+ 1 sp))))
                                   (evaluate (+ 2 sp))]
      [(OpCode _ _ _ 99) int-code]
      [_ (display "Error: unknown opcode: ") (println op-code)
       int-code])))

(void (evaluate 0))
