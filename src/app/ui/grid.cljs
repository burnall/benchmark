(ns app.ui.grid
  (:require [uix.core :as uix :refer [defui $]]
            [uix.dom]))

(defn- get-number [obj key multiplier]
  (if obj
    (.toFixed (* multiplier (key obj)) 2)
    "N/A"))

(defn- get-value [{:keys [old new]} key multiplier]
  ;; (prn "aa" (js->clj old) (js->clj new) (get-number old key multiplier))
  (str (get-number old key multiplier) " / " (get-number new key multiplier)))

(defn- get-regression [{:keys [old new]}]
  (if (and old new)
    (.toFixed (* 100 (- (/ (:mean new) (:mean old)) 1)) 2)
    "N/A"))

(defui grid [{:keys [benchmarks on-select-benchmark]}]
  (let [[selected-benchmark-row set-selected-benchmark-row!] (uix.core/use-state "")
        get-classes (fn [b]
                      (if (= b selected-benchmark-row)
                        "grid-row selected"
                        "grid-row"))]
    ($ :div {:class "simple-grid benchmark-grid"}

       ;; Headers
       ($ :div {:class "grid-row grid-header"}
          ($ :div {:key "benchmark" :class "grid-cell"} "Benchmark")
          ($ :div {:key "regression" :class "grid-cell"} "Regression, %")
          ($ :div {:key "means" :class "grid-cell"} "Old/new average, ms")
          ($ :div {:key "cvs" :class "grid-cell"} "Old/new CV, %"))

       ;; Rows
       (for [[benchmark value] benchmarks]
         ($ :div {:key benchmark :class (get-classes benchmark) :on-click
                  #(do (set-selected-benchmark-row! benchmark)
                       (on-select-benchmark benchmark))}
            ($ :div {:key "benchmark" :class "grid-cell"} benchmark)
            ($ :div {:key "regression" :class "grid-cell"} (get-regression value))
            ($ :div {:key "means" :class "grid-cell"} (get-value value :mean 1.0))
            ($ :div {:key "cvs" :class "grid-cell"} (get-value value :cv 100.0)))))))
