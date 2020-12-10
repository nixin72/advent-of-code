(ns aoc-2020.day9
  (:require [clojure.string :as s]))

(defn- input->numbers []
  (->> (slurp "./resources/day-9-input.txt")
       (#(s/split % #"\n"))
       (map #(Long/parseLong %))
       (into [])))

(defn possible-sums [vec]
  (for [x vec y vec
        :when (< x y)]
    (+ x y)))

(defn day-9-1 []
  (let [numbers (input->numbers)
        size 25]
    (some identity
          (for [part (partition (inc size) 1 numbers)
                :let [nums (butlast part)
                      num (last part)]]
            (when-not (some #{num} (possible-sums nums))
              num)))))

(defn day-9-2 []
  (let [numbers (input->numbers)
        key (day-9-1)]
    (loop [nums numbers]
      (let [res (loop [i 1]
                  (let [rng (take i nums)
                        sum (apply + rng)]
                    (cond
                      (= sum key) (+ (apply min rng) (apply max rng))
                      (< sum key) (recur (inc i)))))]
        (or res (recur (next nums)))))))
