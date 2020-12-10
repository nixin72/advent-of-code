(ns aoc-2020.day8
  (:require [clojure.string :as s]))

(defn- input->opcodes []
  (->> (slurp "./resources/day-8-input.txt")
       (#(s/split % #"\n"))
       (map (fn [x] (s/split x #" ")))
       (map (fn [x] [(symbol (first x)) (Integer/parseInt (second x))]))
       (into [])))

(defn- process-instructions [instructions accumulator]
  (let [index (atom 0)
        lines-run (atom {})]
    (loop [code (instructions @index)]
      (case (first code)
        nop (swap! index inc)
        acc (do (swap! accumulator (fn [a] (+ a (second code))))
                (swap! index inc))
        jmp (swap! index (fn [a] (+ a (second code)))))
      (when-not (@lines-run @index)
        (swap! lines-run (fn [a] (assoc a @index true)))
        (recur (instructions @index))))
    @accumulator))

(defn- find-next-op [instructions last]
  (case (first (instructions last))
    (nop jmp) last
    acc (find-next-op instructions (inc last))))

(defn- swap-code [code]
  (case (first code)
    nop ['jmp (second code)]
    jmp ['nop (second code)]))

(defn- clone [opcodes]
  (for [i (range (count opcodes))]
    (nth opcodes i)))

(defn day-8-1 []
  (let [opcodes (input->opcodes)]
    (process-instructions opcodes (atom 0))))

(defn day-8-2 []
  (let [opcodes (input->opcodes)
        accumulator (atom 0)]
    (try
      (loop [curr-test (find-next-op opcodes 0)
             temp-codes (transient opcodes)]
        (let [el-to-swap (temp-codes curr-test)]
          (assoc! temp-codes curr-test (swap-code el-to-swap))
          (process-instructions temp-codes accumulator)
          (println (str "Swapped index:" curr-test
                        "\nAccumulator:" @accumulator
                        "\nOpcodes:" (into [] (clone temp-codes))))
          (swap! accumulator (fn [_] 0))
          (recur (find-next-op opcodes (inc curr-test))
                 (transient opcodes))))
      (catch IndexOutOfBoundsException _ @accumulator))))
