(ns app.core
  (:require [app.data :as data]
            [app.analysis :as an]
            [uix.core :as uix :refer [defui $]]
            [uix.dom]))

(declare start)
(defn ^:export init [] (start))

(defui button [{:keys [on-click children]}]
  ($ :button.btn {:on-click on-click} children))

(defui app []
  (let [[state set-state!] (uix/use-state 20)]
    ($ :<>
       ($ button {:on-click #(set-state! dec)} "-")
       ($ :span state)
       ($ button {:on-click #(set-state! inc)} "+"))))

(defonce root (uix.dom/create-root (js/document.getElementById "root")))

(defn ^:export start []
  (uix.dom/render-root ($ app) root))

(def comparison
  (an/join
   (an/aggregate data/new :bench_simple)
   (an/aggregate data/old :bench_simple)))

(js/console.log "comparison")
(js/console.log comparison)