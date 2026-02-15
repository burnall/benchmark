(ns app.core
  (:require [app.data :as data]
            [app.analysis :as an]
            [app.ui.cockpit :refer [cockpit]]
            [app.ui.chart :refer [pie-chart]]
            [app.ui.grid :refer [grid]]
            [uix.core :as uix :refer [defui $]]
            [uix.dom]))

(declare start)
(defn ^:export init [] (start))

(def cockpit-default-options {:regression 5.0,
                              :coeff-variation 40.0,
                              :group-simple? true
                              :ignore-missing? true})

(def benchmarks-joined-agg
  {:simple  (an/join
             (an/aggregate data/old :bench_simple)
             (an/aggregate data/new :bench_simple))
   :full  (an/join
           (an/aggregate data/old :bench_full)
           (an/aggregate data/new :bench_full))})

(defn- normalise-options [options]
  (-> options
      (update :regression #(/ % 100))
      (update :coeff-variation #(/ % 100))))

(defn get-benchmarks [options]
  ;; (prn (js->clj options))
  (let [key (if (:group-simple? options) :simple :full)
        benchmarks (key benchmarks-joined-agg)]
    (an/filter-by-options benchmarks (normalise-options options))))

(defn get-benchmark-count [options]
  (let [key (if (:group-simple? options) :simple :full)]
    (count (key benchmarks-joined-agg))))

(defui app []
  (let [[options set-options!] (uix.core/use-state cockpit-default-options)
        benchmarks (get-benchmarks options)
        failed-count (count benchmarks)
        total-count (get-benchmark-count options)
        aa (js/console.log "counts" failed-count total-count)]
    ($ :div {:class "app"}
       ($ :div {:class "top-row"}
          ($ cockpit {:options options, :on-apply set-options!})
          ($ :div {:class "panel panel-right"}
             ($ pie-chart {:total total-count :failed failed-count})))
       ($ :div {:class "panel panel-bottom"}
          ($ grid benchmarks))
       ($ :div {:class "panel panel-bottom"}))))

(defonce root (uix.dom/create-root (js/document.getElementById "root")))

(defn ^:export start []
  (uix.dom/render-root ($ app) root))


(comment
  (an/join
   (an/aggregate data/new :bench_simple)
   (an/aggregate data/old :bench_simple))
  (normalise-options cockpit-default-options))