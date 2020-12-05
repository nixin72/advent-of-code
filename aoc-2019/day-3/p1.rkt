#lang racket
(require racket/vector)

(define wires
  (map (lambda (c) (list->vector c)) (map (lambda (w)
         (map (lambda (v)
                (cons (string-ref v 0) (string->number (substring v 1)))) w))
       (map (lambda (l) (string-split l ",")) (file->lines "input")))))

(define fx (make-hash (list (cons #\U (lambda (i) i))
                            (cons #\D (lambda (i) i))
                            (cons #\R (lambda (i) (+ i 1)))
                            (cons #\L (lambda (i) (- i 1))))))

(define fy (make-hash (list (cons #\U (lambda (i) (+ i 1)))
                            (cons #\D (lambda (i) (- i 1)))
                            (cons #\R (lambda (i) i))
                            (cons #\L (lambda (i) i)))))

(define (get-points commands)
  (let ([points (make-hash)]
        [x 0] [y 0] [path-len 0])
    (for ([cmd commands])
      (let ([op (car cmd)]
            [num (cdr cmd)])
        (for ([_ num])
          (set! x ((hash-ref fx op) x))
          (set! y ((hash-ref fy op) y))
          (set! path-len (+ path-len 1))
          (let ([key (string-append (number->string x) "," (number->string y))])
            (cond
              [(not (hash-has-key? points key))
               (hash-set! points key path-len)])))))
    points))

(define (sum-matching-keys hash-1 hash-2)
  (let ([new-hash (make-hash)])
    (for ([key (hash-keys hash-1)])
      (if (hash-has-key? hash-2 key)
          (hash-set! new-hash key
                     (+ (hash-ref hash-1 key) (hash-ref hash-2 key)))
          (void)))
    new-hash))

(let* ([p-a (get-points (car wires))]
       [p-b (get-points (cadr wires))]
       [inter-points (sum-matching-keys p-a p-b)]
       [min 999999999999])
  (for ([key (hash-keys inter-points)])
    (if (<= (hash-ref inter-points key) min)
        (set! min (hash-ref inter-points key))
        (void)))
  (println inter-points)
  (println min))
  