(ns clog.controller.userc
  (:use
   [clog.lib.template :only [page-out]]
   [clojure.tools.logging :only (info error)]
   [sandbar.stateful-session])
  (:require
   [clojure.string :as str]
   [clog.model.user :as user]
   [clog.model.blog :as blog]
   [ring.util.response :as ring]
   [clog.lib.util :as util]))


(defn sign-in 
  ([deny] (page-out 'views/sign-in {:deny deny}))
  ([email password]
     (let [usr (user/find-by-email email)]
       (if (and (not (nil? usr))
                (user/authenticate usr password))
         (do
           (session-put! :user-id (usr :id))
           (ring/redirect "/dashboard"))
         (ring/redirect (str "/sign-in/deny"))
         ))))


(defn sign-out
  ([]
     (destroy-session!)
     (ring/redirect "/")))


(defn dashboard
  []
  (let [posts (blog/latest-n 10)]
    (error (str "posts " posts))
    (page-out 'views/dashboard {:posts posts})))



