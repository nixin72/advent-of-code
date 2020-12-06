(ns aoc-2020.day4
  (:use aoc-2020.util)
  (:require [clojure.string :as s]
            [clojure.set :refer [union intersection]]))

(defn has-all-fields? [id]
  (let [required-keys #{"byr" "iyr" "eyr" "hgt" "hcl" "ecl" "pid"}]
    (= (intersection required-keys (into #{} (keys id))) required-keys)))

(defn normalize-types [passport]
  (try (let [hgt (passport "hgt")
             i (or (s/index-of hgt "in") (s/index-of hgt "cm"))]
         {"byr" (and (passport "byr") (Integer/parseInt (passport "byr")))
          "iyr" (and (passport "iyr") (Integer/parseInt (passport "iyr")))
          "eyr" (and (passport "eyr") (Integer/parseInt (passport "eyr")))
          "hgt" (and i [(Integer/parseInt (subs hgt 0 i)) (subs hgt i)])
          "ecl" (and (passport "ecl") (symbol (passport "ecl")))
          "hcl" (passport "hcl")
          "pid" (passport "pid")})
       (catch NullPointerException _ {})))

(defn validate-passport [passport]
  (try (and (between (passport "byr") 1920 2002)
            (between (passport "iyr") 2010 2020)
            (between (passport "eyr") 2020 2030)
            (let [h ((passport "hgt") 0) hu ((passport "hgt") 1)]
              (if (= hu "in") (between h 59 76) (between h 150 193)))
            (re-find #"^#[a-f0-9]{6}$" (passport "hcl"))
            (re-find #"^\d{9}$" (passport "pid"))
            (some #{(passport "ecl")} '#{amb blu brn gry grn hzl oth}))
       (catch NullPointerException _ false)))

(defn input->maps [input-file]
  (->> (slurp input-file)
       ((fn [x] (s/split x #"\n\n")))
       (map (fn [x] (map (fn [y] (s/split y #":")) (s/split x #"[\n\s]"))))
       (map (fn [x] (into {} x)))))

(defn day-4-1 []
  (let [data (input->maps "./resources/day-4-input.txt")]
    (count (filter (fn [pass] (has-all-fields? pass)) data))))

(defn day-4-2 []
  (let [data (map #(normalize-types %)
                  (input->maps "./resources/day-4-input.txt"))]
    (count (filter (fn [pass] (and (has-all-fields? pass)
                                   (validate-passport pass)))
                   data))))
