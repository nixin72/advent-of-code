(ns day4
  (:require [clojure.string :as str]))

(defn calculate-score [{:keys [board winning-num]}]
  (* winning-num
     (reduce #(if (second %2) % (+ % (first %2))) 0 board)))

(defn winning? [board]
  (let [rows (partition 5 board)
        cols (apply mapv vector rows)]
    (or (some (fn [x] (every? #(true? (second %)) x)) rows)
        (some (fn [x] (every? #(true? (second %)) x)) cols))))

(defn play-bingo [nums board]
  (loop [nums nums board board turn 1]
    (when-not (empty? nums)
      (if (not= (.indexOf board [(first nums)]) -1)
        (let [new-board (update board (.indexOf board [(first nums)]) conj true)]
          (if (winning? new-board)
            {:turn turn :board new-board :winning-num (first nums)}
            (recur (rest nums) new-board (inc turn))))
        (recur (rest nums) board (inc turn))))))

(defn parse-board [board]
  (->> (rest board)
       (map #(re-seq #"\d+" %))
       flatten
       (reduce #(conj % [(Integer/parseInt %2)]) [])))

(defn parse-input []
  (let [[nums & boards] (-> (slurp "./input/day4.txt") (str/split-lines))
        nums (map #(Integer/parseInt %) (str/split nums #","))]
    [nums (->> (partition 6 boards) (map parse-board))]))

(let [[nums boards] (parse-input)
      games (map #(play-bingo nums %) boards)]
  (println "Part 1:" (calculate-score (apply max-key :turn games)))
  (println "Part 2:" (calculate-score (apply min-key :turn games))))
