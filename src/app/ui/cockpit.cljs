(ns app.ui.cockpit
  (:require [uix.core :as uix :refer [defui $]]
            [uix.dom]))

; [regression coeff-variation group-simple? ignore-missing?]
(defui cockpit [options]
  (let [[state set-state!] (uix.core/use-state options)]

    (letfn [(update! [k v]
              (set-state! #(assoc % k v)))]

      ($ :div {:class "panel cockpit"}

         ;; header
         ($ :div {:class "panel-header"}
            ($ :h2 "Cockpit"))

         ;; group by
         ($ :div {:class "panel-row"}
            ($ :label {:class "field"}
               ($ :input {:type "checkbox"
                          :checked (:group-simple? state)
                          :on-change #(update! :group-simple?
                                               (.. % -target -checked))})
               ($ :span "Group by \"bench simple\"")))

         ;; REGRESSION
         ($ :div {:class "panel-row"}
            ($ :div {:class "field"}
               ($ :label {:class "field-label"} "Regression, %")
               ($ :div {:class "field-inputs"}
                  ($ :input {:type "number"
                             :class "input"
                             :value (:regression state)
                             :on-change #(update! :regression
                                                  (js/parseFloat (.. % -target -value)))}))))

         ;; coeff of variation
         ($ :div {:class "panel-row"}
            ($ :div {:class "field"}
               ($ :label {:class "field-label"} "Coefficient of variation, %")
               ($ :div {:class "field-inputs"}
                  ($ :input {:type "number"
                             :class "input"
                             :value (:coeff-variation state)
                             :on-change #(update! :coeff-variation
                                                  (js/parseFloat (.. % -target -value)))}))))

         ;; ignore missing
         ($ :div {:class "panel-row"}
            ($ :label {:class "field"}
               ($ :input {:type "checkbox"
                          :checked (:ignore-missing? state)
                          :on-change #(update! :ignore-missing?
                                               (.. % -target -checked))})
               ($ :span "Ignore missing benchmark")))

         ;; apply button
         ($ :div {:class "panel-footer"}
            ($ :button {:class "apply-btn"
                        :on-click #(js/console.log "APPLY" (clj->js state))}
               "Apply"))))))
