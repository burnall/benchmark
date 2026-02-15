(ns app.ui.details-grid
  (:require [uix.core :as uix :refer [defui $]]
            [uix.dom]))

(defn- format-number [obj]
  (if obj
    (.toFixed (:ms obj) 2)
    ""))

(defn- format-date [obj]
  (if obj
    (.toLocaleString (js/Date. (:timestamp obj)))
    ""))

(defui details-grid [{:keys [old new]}]
  (let [cnt (max (count old) (count new))]
    ($ :div {:class "simple-grid detail-grid"}

       ;; Headers
       ($ :div {:class "grid-row grid-header"}
          ($ :div {:key "old-date" :class "grid-cell"} "Old date")
          ($ :div {:key "old-value" :class "grid-cell"} "Old value, ms")
          ($ :div {:key "new-date" :class "grid-cell"} "New date")
          ($ :div {:key "new-value" :class "grid-cell"} "New value, ms"))

       ;;Rows
       (for [idx (range cnt)
             :let [old-item (nth old idx nil)
                   new-item (nth new idx nil)]]
         ($ :div {:key (str "detail" idx) :class "grid-row"}
            ($ :div {:key "old-date" :class "grid-cell"} (format-date old-item))
            ($ :div {:key "old-value" :class "grid-cell"} (format-number old-item))
            ($ :div {:key "new-date" :class "grid-cell"} (format-date new-item))
            ($ :div {:key "new-value" :class "grid-cell"} (format-number new-item)))))))
