(ns clog.controller.site
  (:use
   [clog.lib.template :only [page-out page-edit]]
   [clojure.tools.logging :only (info error)]
   [sandbar.stateful-session])
  (:require
   [clojure.string :as str]
   [ring.util.response :as ring]
   [clog.model.blog :as blog]
   [clog.model.user :as user]
   [clog.lib.util :as util]))


(defn index []
  (let [posts (blog/latest-n-published 10)]
    (page-out 'views/index {:posts posts})))


(defn save-blog
  "Save this event and redirect to wherever the form indicates in the 'redir' param."
  ([id params]
     (let [blog (blog/load-one id)
           usr (user/load-one (:users_id params))]
       (error (str params))
       (blog/save id params)
       (ring/redirect (str "/edit/" id))))
  ([params]
     (let [usr (user/load-one (:users_id params))
           blog (blog/save params)]
       (ring/redirect (str "/edit/" (:id blog))))))


(defn post [id]
  (page-out 'views/post {:blog (blog/load-one id)}))


(defn edit
  ([]
     (page-edit 'views/edit {:blog {}
                             :usr (user/load-one (session-get :user-id))}))
  ([id]
     (page-edit 'views/edit {:blog (blog/load-one id)
                             :usr (user/load-one (session-get :user-id))})))


(defn delete-blog [id]
  (blog/del id)
  (ring/redirect "/dashboard"))


(defn publish-blog [id]
  (blog/publish id)
  (ring/redirect (str "/edit/" id)))


(defn unpublish-blog [id]
  (blog/unpublish id)
  (ring/redirect (str "/edit/" id)))


(defn about []
  (page-out 'views/about {}))


(defn contact []
  (page-out 'views/contact {}))


(defn adam []
  (page-out 'views/adam {}))
