(ns aoc-2020.day11
  (:require [clojure.string :as s]))

(defn- input->grid []
  (->> (slurp "./resources/day-11-input.txt")
       (#(s/split % #"\n"))
       (map #(s/split % #""))
       (into [])))

(defn- count-occupied-seats [grid]
  (->> (flatten grid)
       (filter #(= % "#"))
       (count)))

(defn- -or-min [rc]
  (if (neg? (- rc 1)) rc (- rc 1)))

(defn- +or-max [a len]
  (if (< len (+ a 1)) len (+ a 1)))

(defn- get-adjacent-seats [grid row col]
  (let [top (-or-min row), bottom (+or-max row (dec (count grid)))
        left (-or-min col), right (+or-max col (dec (count (nth grid row))))]
    (vec (for [x (range top (inc bottom))]
           (vec (for [y (range left (inc right))
                      :when (not (and (= x row) (= y col)))]
                  (get-in grid [x y])))))))

(defn- available? [grid row col] (= (get-in grid [row col]) "L"))
(defn- occupied? [grid row col] (= (get-in grid [row col]) "#"))
(def count-around (comp count-occupied-seats get-adjacent-seats))

(defn- round [grid]
  (vec
   (for [row (range (count grid))]
     (vec
      (for [col (range (count (first grid)))]
        (cond
          (and (available? grid row col) (zero? (count-around grid row col)))
          "#"
          (and (occupied? grid row col) (>= (count-around grid row col) 4))
          "L"
          :else (get-in grid [row col])))))))

(defn- run-rounds [grid]
  (loop [grid grid, prev nil]
    (if (= grid prev)
      grid
      (recur (round grid) grid))))

(defn day-11-1 []
  (let [grid (input->grid)
        final (run-rounds grid)]
    (println final)
    (count-occupied-seats final)))

(day-11-1)
