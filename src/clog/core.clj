(ns clog.core
  (:use [compojure.core]
        [ring.middleware.params         :only [wrap-params]]
        [ring.middleware.cookies        :only [wrap-cookies]]
        [ring.middleware.keyword-params :only [wrap-keyword-params]]
        [ring.middleware.session        :only [wrap-session]]
        [ring.middleware.reload         :only [wrap-reload]]
        [ring.middleware.logger         :only [wrap-with-logger]]
        [clojure.tools.logging          :only [info error]]
        [clog.controller.routes      :only [app-routes]]
        [clog.lib.db                 :only [connect-db wrap-transaction]]
        [clog.lib.session            :only [db-session-store]]
        [clog.lib.template           :only [wrap-recompile-templates compile-templates]]
        [sandbar.stateful-session]
        )
  (:require
   [compojure.handler :as handler]
   [ring.adapter.jetty :as jetty]
   [ring.util.response]
   [clojure.tools.logging :as log]))


(defn app []
  (-> app-routes
      (wrap-with-logger)
      (wrap-recompile-templates)
      (wrap-cookies)
      (wrap-stateful-session {:cookie-name "clogsession" :store (db-session-store) })
      (wrap-keyword-params)
      (wrap-params)
      (wrap-transaction)))


(def ring-handler
  (handler/site
   (do (connect-db)
       (compile-templates)
       (app))))


(defn svr-start []
  (connect-db)
  (let [port (Integer/parseInt (get (System/getenv) "PORT" "5000"))]
    (jetty/run-jetty (app) {:port port :join? false})))


(defn -main [] (svr-start))








