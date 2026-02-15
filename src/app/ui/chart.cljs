(ns app.ui.chart
  (:require
   ["recharts" :refer [PieChart Pie Cell Tooltip Legend]]
   [uix.core :refer [$ defui]]))

(defn get-data [total failed]
  #js [#js {:name "Passed" :value (- total failed)}
       #js {:name "Failed" :value failed}])

(defui pie-chart [{:keys [total failed]}]
  ($ PieChart {:width 200 :height 200}
     ($ Pie {:data (get-data total failed)
             :dataKey "value"
             :nameKey "name"
             :outerRadius 80}
        ($ Cell {:fill "#12e663"})
        ($ Cell {:fill "#e90909"}))
     ($ Tooltip)
     ($ Legend)))

