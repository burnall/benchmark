(ns app.analysis)

(defn- get-stat [group]
  (let [cnt (count group)
        sum (apply + (map :ms group))
        mean (/ sum cnt)
        variance (as-> group $
                   (map (fn [{v :ms}] (Math/pow (- v mean) 2)) $)
                   (apply + $)
                   (/ $ cnt))
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

(defn- regression-passed? [old-v new-v regression]
  (>= regression (/ (- new-v old-v) old-v)))

(defn pass? [{:keys [old new]} {:keys [regression coeff-variation ignore-missing-new]}]
  (if new
    (and (<= (:cv new) coeff-variation)
         (regression-passed? (:mean old) (:mean new) regression))
    ignore-missing-new))

(comment
  (aggregate [{:a "a" :ms 1}, {:a "a" :ms 3}, {:a "b" :ms 10}] :a)
  (join {"a" {:mean 1} "b" {:mean 2}} {"a" {:mean 10} "c" {:mean 20}})
  (pass? {:old {:mean 2}, :new {:mean 1.5}} {:regression 0, :coeff-variation 1, :ignore-missing-new true})
  (pass? {:old {:mean 2}, :new {:mean 2.5}} {:regression 0.3, :coeff-variation 1, :ignore-missing-new true})
  (pass? {:old {:mean 2}, :new {:mean 2.5}} {:regression 0.2, :coeff-variation 1, :ignore-missing-new true})
  (pass? {:old {:mean 2}, :new {:mean 2.5, :cv 0.1}} {:regression 1, :coeff-variation 0.05, :ignore-missing-new true})
  (pass? {:old {:mean 2}, :new {:mean 2.5, :cv 0.1}} {:regression 1, :coeff-variation 0.1, :ignore-missing-new true})
  (pass? {:old {:mean 2}} {:ignore-missing-new true})
  (pass? {:old {:mean 2}} {:ignore-missing-new false}))
