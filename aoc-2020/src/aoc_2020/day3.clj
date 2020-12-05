(ns aoc-2020.day3
  (:require [clojure.pprint :refer [pprint]]
            [clojure.string :as s]
            [clojure.inspector :refer [atom?]]))

(defn input->matrix [file]
  (-> (slurp file)
      (s/split #"\n")
      ((fn [xs] (map (fn [x] (s/split x #"")) xs)))))

(defn tree? [matrix x y]
  (= (nth (nth matrix x) y) "#"))

(defn new-y [len y step]
  (if (< (+ y step) len)
    (+ y step)
    (- (+ y step) len)))

(defn day-3-1 [skip-x step-y]
  (let [matrix (input->matrix "./resources/day-3-input.txt")
        y (atom 0)
        trees (for [x (range (count matrix))
                    :when (= (mod x skip-x) 0)]
                (let [res (tree? matrix x @y)]
                  (swap! y (fn [v] (new-y (count (nth matrix x)) v step-y)))
                  res))]
    (count (filter (fn [x] x) trees))))

(defn day-3-2 []
  (* (day-3-1 1 1)
     (day-3-1 1 3)
     (day-3-1 1 5)
     (day-3-1 1 7)
     (day-3-1 2 1)))
