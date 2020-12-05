#lang racket

(define orbits (map (lambda (l) (string-split l ")")) (file->lines "input")))

(define (make-tree root elements)
  (let* ([com (cadr root)])
    (define tree (filter-map (lambda (o)
                               (and (equal? (car o) com)
                                    (make-tree o elements))) elements))
    (println tree)
    tree))

(make-tree (car (filter (lambda (o) (equal? "COM" (car o))) orbits)) orbits)
