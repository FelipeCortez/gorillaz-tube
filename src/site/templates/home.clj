(ns site.templates.home
  (:require [clojure.string :as str]
            [clojure.java.io :as io]
            [site.templates.base :refer [document]])
  (:import [org.jsoup Jsoup]))

(defn good-svg []
  (->> "public/tube.svg"
       io/resource
       slurp))

(defn svg-with-font []
  (let [doc (Jsoup/parseBodyFragment (good-svg) "UTF-8")
        svg (.selectFirst doc "svg")]
    (-> doc .outputSettings (.prettyPrint false))
    (-> svg
        (.prepend "<style>@import url(\"https://fonts.googleapis.com/css2?family=Cabin\");</style>")
        str)))

(defn tube-page []
  (document
   {:title "Gorillaz musical influences tube map from Rise of the Ogre"
    :js ["/js/tube.js"]}
   [:div (svg-with-font)]))
