(ns site.core
  (:require [site.templates.home :refer [tube-page]]
            [site.templates.base :refer [*reloader?*]]
            [compojure.core :refer [defroutes GET PUT POST]]
            [compojure.route :as route]
            [ring.adapter.jetty :as jetty]
            [babashka.process :refer [$]]
            [ring.middleware.params :refer [wrap-params]]
            [clojure.java.io :as io]
            [clojure.java.shell :as shell])
  (:gen-class))

(def dev? (some #(= "clojure.main$repl" (.getClassName %))
                (.getStackTrace (Thread/currentThread))))

(when dev?
  (defonce p ($ websocat -t "ws-l:127.0.0.1:5557" "broadcast:mirror:" -E)))

(defn build! []
  (binding [*reloader?* dev?]
    (defn politely-spit [f contents] (io/make-parents f) (spit f contents))

    (shell/sh "rm" "-rf" "build")
    (politely-spit "build/index.html" (tube-page))
    (shell/sh "rsync" "-r" "resources/public/" "build"))

  (when-not dev? (shutdown-agents))

  (when dev? (shell/sh "websocat" "-t" "ws://127.0.0.1:5557" "literal:reload")))

(build!)

(defroutes app
  (route/files "/" {:root "build/"})
  (route/not-found "<h1>Not found</h1>"))

(defn server []
  (-> #'app
      (wrap-params)
      (jetty/run-jetty {:port 3001 :join? false})))

(defn -main [] (server))

(comment
  (def server-instance (server))
  (.start server)
  (.stop server-instance)

  nil)
