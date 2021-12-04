(ns day3
  (:require [clojure.string :as str]))

(defn transpose [matrix] (apply mapv vector matrix))
(defn mult-bit-strings [s1 s2]
  (* (Integer/parseInt s1 2) (Integer/parseInt s2 2)))

;; part 1
(let [bits (->> "./input/day3.txt"
                slurp
                str/split-lines
                (map #(str/split % #""))
                transpose
                (map frequencies)
                (map #(key (apply max-key val %))))
      inv-bits (map #(if (= % "0") "1" "0") bits)]
  (mult-bit-strings (str/join bits) (str/join inv-bits)))

;; part 2
(defn most-significant [row]
  (key (apply max-key val (select-keys (frequencies row) ["0" "1"]))))

(defn least-significant [row]
  (key (apply min-key val (select-keys (frequencies row) ["1" "0"]))))

(defn ratings [matrix sig-fn]
  (reduce (fn [a k]
            (if (> (count a) 1)
              (let [sig-bit (-> a transpose (nth k) sig-fn)]
                (filter #(= (nth % k) sig-bit) a))
              a))
          matrix
          (range (count matrix))))

(let [parsed-input (->> (slurp "./input/day3.txt")
                        str/split-lines
                        (map #(str/split % #"")))
      oxy (first (ratings parsed-input most-significant))
      co2 (first (ratings parsed-input least-significant))]
  (mult-bit-strings (str/join oxy) (str/join co2)))
