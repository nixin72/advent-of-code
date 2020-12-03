(ns aoc-2020.day1
  (:require [clojure.string :as s]))

(defn day-1-1 []
  (let [numbers (-> (slurp "./resources/day-1-input.txt")
                    (s/split #"\n"))]
    (->> (for [n1 numbers n2 numbers]
           [(Integer/parseInt n1) (Integer/parseInt n2)])
         (filter (fn [xs] (= 2020 (+ (first xs) (second xs)))))
         (first)
         (apply *))))

(defn day-1-2 []
  (let [numbers (-> (slurp "./resources/day-1-input.txt")
                    (s/split #"\n"))]
    (->> (for [n1 numbers n2 numbers n3 numbers]
           [(Integer/parseInt n1) (Integer/parseInt n2) (Integer/parseInt n3)])
         (filter (fn [xs] (= 2020 (+ (xs 0) (xs 1) (xs 2)))))
         (first)
         (apply *))))
