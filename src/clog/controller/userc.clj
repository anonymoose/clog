(ns clog.controller.userc
  (:require
   [sandbar.stateful-session :as session]
   [clog.lib.template :only [page-out]]
   [clojure.tools.logging :as log]
   [clog.lib.template :as tpl]
   [clojure.string :as str]
   [clog.model.user :as user]
   [clog.model.blog :as blog]
   [ring.util.response :as ring]
   [clog.lib.util :as util]))


(defn sign-in
  ([deny] (tpl/page-out "sign-in" {:deny deny}))
  ([email password]
     (let [usr (user/find-by-email email)]
       (if (and (not (nil? usr))
                (user/authenticate usr password))
         (do
           (session/session-put! :user-id (usr :id))
           (ring/redirect "/dashboard"))
         (ring/redirect (str "/sign-in/deny"))
         ))))


(defn sign-out
  ([]
     (session/destroy-session!)
     (ring/redirect "/")))


(defn dashboard
  []
  (let [posts (blog/latest-n 10)]
    (log/error (str "posts " posts))
    (tpl/page-out "dashboard" {:posts posts})))
