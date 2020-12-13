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
(defn- floor? [grid row col] (= (get-in grid [row col]) "."))
(def not-floor? (comp not floor?))

(defn x-op [n dirs]
  (if (some #{'bottom} dirs) (inc n) (dec n)))
(defn y-op [n dirs]
  (if (some #{'right} dirs) (inc n) (dec n)))

(defn- get-bounds [grid dirs]
  (case (first dirs)
    nil #{}
    top (conj (get-bounds grid (rest dirs)) 0)
    bottom (conj (get-bounds grid (rest dirs)) (count grid))
    left (conj (get-bounds grid (rest dirs)) 0)
    right (conj (get-bounds grid (rest dirs)) (count (first grid)))))

(defn get-diag [grid row col dirs]
  (loop [x (x-op row dirs) y (y-op col dirs)]
    (cond
      (some (get-bounds grid dirs) [x y]) (and (not-floor? grid x y) [x y])
      (floor? grid x y) (recur (x-op x dirs) (y-op y dirs))
      :else [x y])))

(defn get-straight [grid row col dir]
  (let [axis (if (some '#{top bottom} [dir]) :x :y)]
    (loop [x (if (= axis :x) (x-op row [dir]) row)
           y (if (= axis :y) (y-op col [dir]) col)]
      (cond
        (some (get-bounds grid [dir]) [x y]) (and (not-floor? grid x y) [x y])
        (floor? grid x y) (recur (if (= axis :x) (x-op row [dir]) row)
                                 (if (= axis :y) (y-op col [dir]) col))
        :else [x y]))))

(defn- visible-seat? [floor dir y x]
  (let [start (mapv + [y x] dir)]
    (loop [pos start]
      (let [ch (get-in floor pos)]
        (cond
          (nil? ch) false
          (= ch \.) (recur (mapv + pos dir))
          (= ch \#) true
          :else     false)))))

(defn- valid [[y x] maxy maxx]
  (and (< -1 y maxy) (< -1 x maxx)))

(def surrounds [[-1 -1] [-1 0] [-1 1]
                [0 -1]         [0 1]
                [1 -1]  [1 0]  [1 1]])

(defn- count-visible [floor y x]
  (let [maxy (count floor)
        maxx (count (first floor))
        dirs (filter #(valid (mapv + [y x] %) maxy maxx) surrounds)]
    (apply + (for [dir dirs]
               (if (visible-seat? floor dir y x) 1 0)))))

(defn- round [grid tolerance visible-fn]
  (vec
   (for [row (range (count grid))]
     (vec
      (for [col (range (count (first grid)))]
        (cond
          (and (available? grid row col) (zero? (visible-fn grid row col)))
          "#"
          (and (occupied? grid row col) (>= (visible-fn grid row col) tolerance))
          "L"
          :else (get-in grid [row col])))))))

(defn- run-rounds [grid tolerance visible-fn]
  (loop [grid grid, prev nil]
    (if (= grid prev)
      grid
      (recur (round grid tolerance visible-fn) grid))))

(defn day-11-1 []
  (let [grid (input->grid)
        final (run-rounds grid 4 (comp count-occupied-seats get-adjacent-seats))]
    (count-occupied-seats final)))

(defn- get-visible-seats [grid row col]
  [(get-diag grid row col '[top left])
   (get-diag grid row col '[top right])
   (get-diag grid row col '[bottom left])
   (get-diag grid row col '[bottom right])
   (get-straight grid row col 'top)
   (get-straight grid row col 'bottom)
   (get-straight grid row col 'left)
   (get-straight grid row col 'right)])

(defn day-11-2 []
  (let [grid (input->grid)
        final (run-rounds grid 5 count-visible)]
    (count-occupied-seats final)))

(day-11-2)
