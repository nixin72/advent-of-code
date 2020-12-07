(ns aoc-2020.day6
  (:require [clojure.set :refer [intersection]]
            [clojure.string :as s]))

(defn input->groups []
  (->> (slurp "./resources/day-6-input.txt")
       (#(s/split % #"\n\n"))))

(defn day-6-1 []
  (->> (input->groups)
       (map (fn [x] (s/split x #"")))
       (map (fn [x] (filter #(not= % "\n") x)))
       (map (fn [x] (into #{} x)))
       (reduce (fn [a x] (+ a (count x))) 0)))

(defn day-6-2 []
  (->> (input->groups)
       (map (fn [x] (s/split x #"\n")))
       (map (fn [x] (map (fn [y] (->> (s/split y #"")
                                      (into #{}))) x)))
       (map (fn [x] (apply intersection x)))
       (reduce (fn [a x] (+ a (count x))) 0)))
