(ns day8
  (:require [clojure.string :as str]
            [clojure.set :as set :refer [intersection difference]]))

(def numbers {"cf" 1 "acdeg" 2 "acdfg" 3 "bcdf" 4 "abdfg" 5
              "abdefg" 6 "acf" 7 "abcdefg" 8 "abcdfg" 9 "abcefg" 0})

(defn part-2 [input]
  (for [[signals output] input]
    (let [by-count (vec (sort-by count signals))
          occurs (-> (map vec signals) flatten frequencies)
          a (->> (difference (by-count 1) (by-count 0)) first)
          b (-> occurs set/map-invert (get 6))
          e (-> occurs set/map-invert (get 4))
          f (-> occurs set/map-invert (get 9))
          d (-> (difference (by-count 2) (by-count 0) #{b}) first)
          g (-> (apply intersection (subvec by-count 3 6)) (difference #{a d}) first)
          c (-> (difference (by-count 0) #{f}) first)
          segmap {a "a" b "b" c "c" d "d" e "e" f "f" g "g"}
          outmap (for [num output]
                   (->> (vec num) (map segmap) sort (str/join "") numbers))]
      (Integer/parseInt (str/join "" outmap)))))

(let [input (->> (slurp "./input/day8.txt")
                 str/split-lines
                 (map #(str/split % #"\s|\|"))
                 (map #(map (fn [x] (str/split x #"")) %))
                 (map #(vector (map set (drop-last 6 %)) (map set (take-last 4 %)))))
      part-1 (->> (mapcat #(map count (second %)) input)
                  (filter #{2 3 4 7})
                  (count))
      part-2 (apply + (part-2 input))]
  [part-1 part-2])
