(ns aoc-2020.day2 (:require [clojure.string :as s]))

(defn between [n lower upper]
  (and (>= n lower) (<= n upper)))

;; wrong: 329
(defn day-2-1 []
  (let [lines (-> (slurp "./resources/day-2-input.txt")
                  (s/split #"\n")
                  ((fn [xs] (map (fn [x] (s/split x #":")) xs)))
                  ((fn [xs] (map (fn [x] [(s/split (x 0) #"\s") (x 1)]) xs)))
                  ((fn [xs] (map (fn [x] [[(s/split ((x 0) 0) #"-") ((x 0) 1)] (x 1)]) xs)))
                  ((fn [xs] (map (fn [x] [(x 0) (re-seq (re-pattern ((x 0) 1)) (x 1))]) xs)))
                  ((fn [xs] (map (fn [x] [[(Integer/parseInt (((x 0) 0) 0))
                                           (Integer/parseInt (((x 0) 0) 1))] (x 1)]) xs)))
                  ((fn [xs] (filter (fn [x] (between (count (x 1)) ((x 0) 0) ((x 0) 1))) xs))))]
    (count lines)))

(defn day-2-2 []
  (let [lines (-> (slurp "./resources/day-2-input.txt")
                  (s/split #"\n")
                  ((fn [xs] (map (fn [x] (s/split x #":")) xs)))
                  ((fn [xs] (map (fn [x] [(s/split (x 0) #"\s") (x 1)]) xs)))
                  ((fn [xs] (map (fn [x] [[(s/split ((x 0) 0) #"-") ((x 0) 1)] (x 1)]) xs)))
                  ((fn [xs] (map (fn [x] [[[(Integer/parseInt (((x 0) 0) 0))
                                            (Integer/parseInt (((x 0) 0) 1))]
                                           (nth ((x 0) 1) 0)]
                                          (x 1)]) xs)))
                  ((fn [xs] (filter (fn [x] (and (and (<= (+ 1 (((x 0) 0) 0)) (count (x 1)))
                                                      (= (nth (x 1) (+ 1 (((x 0) 0) 0))) ((x 0) 1)))
                                                 (= (nth (x 1) (+ 1 (((x 0) 0) 1))) ((x 0) 1)))) xs))))]
    (count lines)))
