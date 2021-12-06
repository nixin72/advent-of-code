(ns day5
  (:require [clojure.string :as str]))

(defn points-in-vert-line [x1 y1, _ y2]
  (let [[y1 y2] (sort [y1 y2])]
    (for [n (range y1 (inc y2))] [[x1 n] 1])))

(defn points-in-horz-line [x1 y1, x2 _]
  (let [[x1 x2] (sort [x1 x2])]
    (for [n (range x1 (inc x2))] [[n y1] 1])))

(defn points-in-diag-line [x1 y1, x2 y2]
  (let [dirx (if (< x1 x2) inc dec)
        diry (if (< y1 y2) inc dec)]
    (loop [x x1 y y1 coords []]
      (if (and (if (< x1 x2) (> x x2) (< x x2))
               (if (< y1 y2) (> y y2) (< y y2)))
        coords
        (recur (dirx x) (diry y) (conj coords [[x y] 1]))))))

(defn sum-merge [maps]
  (reduce #(if (contains? % (first %2))
             (update % (first %2) inc)
             (assoc % (first %2) (second %2)))
          {} maps))

(defn make-lines [lines-fn]
  (->> (slurp "./input/day5.txt")
       str/split-lines
       (map (fn [x] (map #(Integer/parseInt %) (re-seq #"\d+" x))))
       (mapcat lines-fn)
       sum-merge
       (filter #(>= (second %) 2))
       count))

(make-lines #(cond (= (nth % 0) (nth % 2)) (apply points-in-vert-line %)
                   (= (nth % 1) (nth % 3)) (apply points-in-horz-line %)
                   :else (apply points-in-diag-line %))) ; comment out for part 1
