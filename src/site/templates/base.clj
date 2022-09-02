(ns site.templates.base
  (:require [hiccup.core :refer [html]]
            [hiccup.page :refer [doctype include-css include-js]]
            [hiccup.util]))

(def ^:dynamic *reloader?* nil)

(defn metadata [page-description]
  (list
   [:meta {"charset" hiccup.util/*encoding*}]
   [:meta {:name    "viewport"
           :content "width=device-width, initial-scale=1"}]
   [:meta {:name    "description"
           :content page-description}]
   [:meta {:name    "author"
           :content "Felipe Cortez"}]
   [:meta {:name    "format-detection"
           :content "telephone=no"}]))

(defn analytics []
  [:script "(function(i,s,o,g,r,a,m){i['GoogleAnalyticsObject']=r;i[r]=i[r]||function(){(i[r].q=i[r].q||[]).push(arguments)},i[r].l=1*new Date();a=s.createElement(o),m=s.getElementsByTagName(o)[0];a.async=1;a.src=g;m.parentNode.insertBefore(a,m)})(window,document,'script','//www.google-analytics.com/analytics.js','ga');ga('create', 'UA-49103022-1', 'auto');ga('send', 'pageview');"])

(defn head [page-title page-description js css & {:keys [etc]}]
  (let [css-base ["/reset.css" "/base.css"]
        css (concat css-base css)]
    (list
     [:head
      [:title page-title]
      [:link {:href "https://fonts.googleapis.com" :rel "preconnect"}]
      [:link {:crossorigin "crossorigin"
              :href "https://fonts.gstatic.com"
              :rel "preconnect"}]
      (when *reloader?* [:script {:src "/autoreload/reloader.js"}])
      [:link {:rel "stylesheet"
              :href "https://fonts.googleapis.com/css2?family=Cabin&display=swap"}]

      (metadata page-description)
      etc
      (apply include-js js)
      (apply include-css css)
      (analytics)])))

(defn body [content]
  [:body content])

(defn document [{:keys [title css js description more-head]} content]
  (str (:html5 doctype)
       (html [:html {:lang "en"}
              (head title description js css :etc more-head)
              (body content)])))
