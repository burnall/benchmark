(ns app.analysis
  (:require [app.data :as data]))

(defn get-stat [group]
  (let [cnt (count group)
        sum (apply + (map :v group))
        mean (/ sum cnt)
        variance (apply + (map (fn [{v :v}] (Math/pow (- v mean) 2))
                               group))
        std (Math/sqrt variance)
        cv (/ std mean)]
    {:cnt cnt, :mean (/ sum cnt), :cv cv}))

(defn aggregate [sample field]
  (let [groups (group-by field sample)]
    (update-vals groups get-stat)))

(defn join [old-agg new-agg]
  (let [old (update-vals old-agg (fn [val] {:old val}))
        new (update-vals new-agg (fn [val] {:new val}))]
    (merge-with merge old new)))

(defn pass? [{:keys [old new]}]
  (cond
    (and old new) (>= (:mean old) (:mean new))
    :else true))

(comment
  (aggregate [{:a "a" :v 1}, {:a "a" :v 3}, {:a "b" :v 10}] :a)
  (join {"a" {:mean 1} "b" {:mean 2}} {"a" {:mean 10} "c" {:mean 20}})
  (pass? {:old {:mean 2}, :new {:mean 1.2}})
  (pass? {:old {:mean 2}})
  (pass? {:old {:mean 2}, :new {:mean 3.2}}))
