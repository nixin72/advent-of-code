(ns day6
  (:require [clojure.string :as str]))

(def input (as-> (slurp "./input/day6.txt") $
             (str/split $ #",|\n")
             (map #(Integer/parseInt %) $)))

(defn simulate [fish duration]
  (let [fish-count (reduce #(update % %2 inc) (vec (repeat 9 0)) fish)]
    (loop [[ready & other] fish-count day 1]
      (let [new-fish (update (conj (vec other) ready) 6 #(+ % ready))
            new-fish (assoc new-fish 8 ready)]
        (if (= day duration)
          (println (apply + new-fish))
          (recur new-fish (inc day)))))))

(simulate input 80)
(simulate input 256)
