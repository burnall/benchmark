(ns csv.parser
  (:require [clojure.string :refer [split trim]]))

(defn split-lines [text]
  (split text #"\n"))

(defn parse-line [line-number s]
  (try
    (let [parts (split (trim s) #";")
          [_ _ bench-simple bench-full _ ms unit timestamp :as all] parts]
      (assert (= 8 (count all)))
      (assert (= unit "ms"))
      {:bench_simple bench-simple
       :bench_full bench-full
       :ms (parse-double (.replace ms "," "."))
       :timestamp (parse-long timestamp)})
    (catch Throwable ex
      (throw (Exception. (str "Parsing failed" line-number))))))


(defn parse [file-name]
  (->> file-name
       (slurp)
       (split-lines)
       (drop 1)
       (map-indexed parse-line)))

(comment
  (parse "resources/old.csv")
  (parse "resources/new.csv"))
  