(ns site.templates.home
  (:require [clojure.string :as str]
            [site.templates.base :refer [document]]))

(defn tube-page []
  (document
   {:title "RotO Tube"
    :js ["/js/tube.js"]}
   [:div
    [:h1 "Tube Map"]]))
