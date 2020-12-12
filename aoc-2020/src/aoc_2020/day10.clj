(ns aoc-2020.day10
  (:require [clojure.string :as s]))

(defn- input->numbers []
  (->> (slurp "./resources/day-10-input.txt")
       (#(s/split % #"\n"))
       (map #(bigint %))
       (#(conj % 0 (+ 3 (apply max %))))
       (sort)
       (into [])))

(defn day-10-1 []
  (let [adapter-jolts (input->numbers)
        nums (->> adapter-jolts
                  (partition 2 1)
                  (map #(- (second %) (first %)))
                  frequencies)]
    (* (nums 1N) (nums 3N))))

(defn remove-at [v i]
  (into (subvec v 0 i) (subvec v (inc i))))

(defn can-remove? [v i]
  (let [l (get v (dec i)), r (get v (inc i))]
    (and l r (<= (- r l) 3))))

(defn one-pass [chain]
  (for [i (range (count chain))
        :when (can-remove? chain i)]
    (remove-at chain i)))

(defn day-10-2 []
  (let [chain (input->numbers)]
    chain))

(day-10-1)
(day-10-2)

(one-pass [1 2 3 4 5])
