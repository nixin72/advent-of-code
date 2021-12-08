(ns day7)

(defn do-sim [step-fn]
  (let [input (as-> (slurp "./input/day7.txt") $
                (.split $ ",|\\n")
                (map #(Integer/parseInt %) (vec $)))
        avg (Math/ceil (/ (apply + input) (count input)))
        common (key (apply max-key val (frequencies input)))]
    (->> (range common (inc avg))
         (map #(map (fn [x] (step-fn x %)) input))
         (map #(apply + %))
         (apply min))))

(do-sim #(Math/abs (- % %2)))
(do-sim #(apply + (range 0 (inc (Math/abs (- % %2))))))
