(ns app.core
  (:require [app.data :as data]
            [app.analysis :as an]
            [app.ui.cockpit :refer [cockpit]]
            [app.ui.grid :refer [grid]]
            [uix.core :as uix :refer [defui $]]
            [uix.dom]))

(declare start)
(defn ^:export init [] (start))

(def cockpit-default {:regression 5.0,
                      :coeff-variation 40.0,
                      :group-simple? true
                      :ignore-missing? true})

(def benchmarks (an/join
                 (an/aggregate data/new :bench_simple)
                 (an/aggregate data/old :bench_simple)))

(defui app []
  ($ :div {:class "app"}
     ($ :div {:class "top-row"}
        ($ cockpit cockpit-default)
        ($ :div {:class "panel panel-right"}))
     ($ :div {:class "panel panel-bottom"}
        ($ grid benchmarks))
     ($ :div {:class "panel panel-bottom"})))


(defonce root (uix.dom/create-root (js/document.getElementById "root")))

(defn ^:export start []
  (uix.dom/render-root ($ app) root))


(comment
  (an/join
   (an/aggregate data/new :bench_simple)
   (an/aggregate data/old :bench_simple)))