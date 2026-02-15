(ns app.ui.cockpit
  (:require [uix.core :as uix :refer [defui $]]
            [uix.dom]))

; [regression coeff-variation group-simple? ignore-missing?]
(defui cockpit [{:keys [options on-apply]}]
  (let [[options set-options!] (uix.core/use-state options)]
    (letfn [(update! [k v]
              (set-options! #(assoc % k v)))]
      ($ :div {:class "panel cockpit"}

         ;; Header
         ($ :div {:class "panel-header"}
            ($ :h2 "Cockpit"))

         ;; Group by
         ($ :div {:class "panel-row"}
            ($ :label {:class "field"}
               ($ :input {:type "checkbox"
                          :checked (:group-simple? options)
                          :on-change #(update! :group-simple?
                                               (.. % -target -checked))})
               ($ :span "Group by \"bench simple\"")))

         ;; Regression
         ($ :div {:class "panel-row"}
            ($ :div {:class "field"}
               ($ :label {:class "field-label"} "Regression, %")
               ($ :div {:class "field-inputs"}
                  ($ :input {:type "number"
                             :class "input"
                             :value (:regression options)
                             :on-change #(update! :regression
                                                  (js/parseFloat (.. % -target -value)))}))))

         ;; Coeff of variation
         ($ :div {:class "panel-row"}
            ($ :div {:class "field"}
               ($ :label {:class "field-label"} "Coefficient of variation, %")
               ($ :div {:class "field-inputs"}
                  ($ :input {:type "number"
                             :class "input"
                             :value (:coeff-variation options)
                             :on-change #(update! :coeff-variation
                                                  (js/parseFloat (.. % -target -value)))}))))

         ;; Ignore missing
         ($ :div {:class "panel-row"}
            ($ :label {:class "field"}
               ($ :input {:type "checkbox"
                          :checked (:ignore-missing? options)
                          :on-change #(update! :ignore-missing?
                                               (.. % -target -checked))})
               ($ :span "Ignore missing benchmark")))

         ;; Apply button
         ($ :div {:class "panel-footer"}
            ($ :button {:class "apply-btn"
                        :on-click #(on-apply options)}
               "Apply"))))))
