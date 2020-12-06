(ns aoc-2020.day5
  (:require [clojure.string :as s]))

(defn input->instructions []
  (->> (slurp "./resources/day-5-input.txt")
       (#(s/split % #"\n"))
       (map #(s/split % #""))))

(defn split [lower upper dir]
  (if (or (= dir "F") (= dir "L"))
    (if (even? upper)
      [lower (inc (quot (- upper lower) 2))]
      [lower (+ lower (quot (- upper lower) 2))])
    (if (even? lower)
      [(+ lower (inc (quot (- upper lower) 2))) upper]
      [(+ lower (quot (- upper lower) 2)) upper])))

(defn get-id [instructions]
  (let [rl (atom 0) ru (atom 127)
        cl (atom 0) cu (atom 7)]
    (dotimes [i 8]
      (let [rn (split @rl @ru (nth instructions i))]
        (swap! rl (fn [_] (first rn)))
        (swap! ru (fn [_] (second rn)))))
    (dotimes [i 3]
      (let [cn (split @cl @cu (nth instructions (+ 7 i)))]
        (swap! cl (fn [_] (first cn)))
        (swap! cu (fn [_] (second cn)))))
    (+ (* (dec @rl) 8) @cl)))

(defn day-5-1 [] ;; 922 - but code gets 919
  (apply max (map get-id (input->instructions))))

(defn day-5-2 []
  (let [seats (apply sorted-set (sort (map get-id (input->instructions))))
        id (atom nil)]
    (println seats)
    (dotimes [i (count seats)]
      (try
        (when (= 2 (nth seats i) (nth seats (inc i)))
          (swap! id (fn [_] (inc (nth seats i)))))
        (catch Exception _ nil)))
    id))
