(ns aoc-2020.day2
  (:use aoc-2020.util)
  (:require [clojure.string :as s]))

(defn only-one [c1 c2 ct]
  (and (or (= c1 ct) (= c2 ct))
       (not= c1 c2)))

(defn splitting [file]
  (-> (slurp file)
      (s/split #"\n")
      ((fn [xs] (map (fn [x] (s/split x #":")) xs)))
      ((fn [xs] (map (fn [x] [(s/split (x 0) #"\s") (x 1)]) xs)))
      ((fn [xs] (map (fn [x] [[(s/split ((x 0) 0) #"-") ((x 0) 1)] (x 1)]) xs)))))

(defn day-2-1 []
  (let [lines (-> (splitting "./resources/day-2-input.txt")
                  ((fn [xs] (map (fn [x] [(x 0) (re-seq (re-pattern ((x 0) 1)) (x 1))]) xs)))
                  ((fn [xs] (map (fn [x] [[(Integer/parseInt (((x 0) 0) 0))
                                           (Integer/parseInt (((x 0) 0) 1))] (x 1)]) xs)))
                  ((fn [xs] (filter (fn [x] (between (count (x 1)) ((x 0) 0) ((x 0) 1))) xs))))]
    (count lines)))

(defn day-2-2 []
  (let [lines (-> (splitting "./resources/day-2-input.txt")
                  ((fn [xs] (map (fn [x] [[[(dec (Integer/parseInt (((x 0) 0) 0)))
                                            (dec (Integer/parseInt (((x 0) 0) 1)))]
                                           (nth ((x 0) 1) 0)]
                                          (x 1)]) xs)))
                  ((fn [xs] (filter (fn [x] (only-one
                                             (nth (x 1) (((x 0) 0) 0))
                                             (nth (x 1) (((x 0) 0) 1))
                                             ((x 0) 1))) xs))))]
    (count lines)))
