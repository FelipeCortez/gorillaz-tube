(ns site.templates.home
  (:require [clojure.string :as str]
            [clojure.java.io :as io]
            [hickory.select :as s]
            [hickory.core :as h]
            [hickory.render :as r]
            [hickory.zip :as z]
            [hickory.convert :as convert]
            [clojure.zip :as cz]
            [site.templates.base :refer [document]])
  (:import [org.jsoup Jsoup]))

(defn good-svg []
  (->> "public/tube.svg"
       io/resource
       slurp))

(defn best-svg []
  (let [doc (Jsoup/parseBodyFragment (good-svg) "UTF-8")
        artists (.select doc "#Bands > g")]
    (-> doc .outputSettings (.prettyPrint false))
    (run! (fn [artist-g]
            (.wrap artist-g "<a xlink:href=\"https://google.com\"></a>"))
          artists)
    (str (.selectFirst doc "svg")))
  )

(defn good-svg-hickory []
  (->> (good-svg)
       h/parse-fragment
       first
       h/as-hickory))

(defn ->hickory [hiccup]
  (first (convert/hiccup-fragment-to-hickory [hiccup])))

(defn gooder-svg []
  (->> (good-svg-hickory)
       z/hickory-zip
       (s/select-next-loc (s/child (s/id "Bands") s/any))
       cz/right
       cz/right
       cz/right
       cz/right
       cz/remove
       cz/root
       r/hickory-to-html
       ))

(comment
  (map r/hickory-to-html
       (s/select (s/child (s/id "Bands") s/any)
                 (good-svg-hickory)))

  (->> (good-svg-hickory)
       z/hickory-zip
       (s/select-next-loc (s/child (s/id "Bands") (s/tag :g)))
       (#(cz/edit % (fn [node] (->hickory [:a {:href "google.com"}]))))
       cz/next
       (s/select-next-loc (s/child (s/id "Bands") (s/tag :g)))
       cz/next
       (s/select-next-loc (s/child (s/id "Bands") (s/tag :g)))
       )
  nil)

(defn tube-page []
  (document
   {:title "RotO Tube"
    :js ["/js/tube.js"]}
   [:div
    [:h1 "Tube Map"]
    (best-svg)]))
