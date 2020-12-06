(ns aoc-2020.util)

(defn between [n lower upper]
  (and (>= n lower) (<= n upper)))
