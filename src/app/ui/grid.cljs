(ns app.ui.grid
  (:require [uix.core :as uix :refer [defui $]]
            [uix.dom]))

(defn- get-number [obj key]
  (if obj
    (.toFixed (key obj) 2)
    "N/A"))

(defn- get-value [{:keys [old new]} key]
  (str (get-number old key) " / " (get-number new key)))

(defui grid [benchmarks]
  ($ :div {:class "simple-grid"}

     ;; Headers
     ($ :div {:class "grid-row grid-header"}
        ($ :div {:key "benchmark" :class "grid-cell"} "Benchmark")
        ($ :div {:key "means" :class "grid-cell"} "Old average/New average")
        ($ :div {:key "cvs" :class "grid-cell"} "Old CV/New CV"))

     ;; Rows
     (for [[benchmark value] benchmarks]
       ($ :div {:key benchmark :class "grid-row" :on-click #(js/console.log benchmark)}
          ($ :div {:key "benchmark" :class "grid-cell"} benchmark)
          ($ :div {:key "means" :class "grid-cell"} (get-value value :mean))
          ($ :div {:key "cvs" :class "grid-cell"} (get-value value :cv))))))
