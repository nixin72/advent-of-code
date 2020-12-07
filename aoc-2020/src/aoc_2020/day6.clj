(ns aoc-2020.day6
  (:require [clojure.set :refer [union intersection]]
            [clojure.string :as s]))

(defn parse-input []
  (->> (slurp "./resources/day-6-input.txt")
       (#(s/split % #"\n\n"))
       (map (fn [x] (s/split x #"\n")))
       (map (fn [x] (map (fn [y] (->> (s/split y #"") (into #{}))) x)))))

(defn day-6-1 []
  (apply + (map (fn [x] (count (apply union x))) (parse-input))))

(defn day-6-2 []
  (apply + (map (fn [x] (count (apply intersection x))) (parse-input))))
