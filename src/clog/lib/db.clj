;;
;; Connection pooling.
;; http://clojure.github.io/java.jdbc/doc/clojure/java/jdbc/ConnectionPooling.html
;;
(ns clog.lib.db
  (:use
   [clojure.tools.logging :only (info error)]
   [ring.middleware.session.store]
   [clog.lib.util :as util])
  (:require [clojure.java.jdbc :as sql]
            [korma.db :as db]
            [robert.hooke :refer [add-hook]]
            [clj-time.core :as time]
            [clojure.string :as string])
  (:import (java.net URI)))


(defn sql-log [sql millis]
  (info (str sql) "| timing:" millis "ms"))


(defn korma-sql-hook
  "Hook into korma's SQL exec method and log what comes out."
  [f & args]
  (let [start (time/now)
        result (apply f args)
        end (time/now)
        time-taken (time/in-millis (time/interval start end))]
    (sql-log (:sql-str (first args)) time-taken)
    result))


(defn korma-add-log-hook
  []
  (add-hook #'db/exec-sql #'korma-sql-hook))


(defn- connect-db-impl
  "split up DATABASE_URL into constituent parts so that korma likes it."
  [url]
  (if (not (nil? url))
    (let
        [db-uri-str url
         db-uri (java.net.URI. db-uri-str)]
      (->> (string/split (.getUserInfo db-uri) #":")
           (#(identity {:db (last (string/split db-uri-str #"\/"))
                        :host (.getHost db-uri)
                        :port (.getPort db-uri)
                        :user (% 0)
                                        ;:make-pool? true
                        :password (% 1)}))
           (db/postgres)
           (db/defdb pgdb)
           )
      (if (util/debug-mode?)
        (korma-add-log-hook))
      )
    nil
    ))


(defn connect-db
  "Connect to the DB based on the environment var passed to use either by
    Heroku, or via exporting it in the shell locally. "
  ([]
     (connect-db-impl (System/getenv "HEROKU_POSTGRESQL_CYAN_URL")))
  ([url] (connect-db-impl url)))


(defn wrap-transaction
  "Middleware that ensures that everything is executed in a transaciton.
    If anyone throws an exception, then roll it back and propagate the transaction up the stack."
  [handler]
  (fn [request]
    (db/transaction
     (handler request))))


