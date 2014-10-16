(ns clog.controller.site
  (:require
   [clojure.tools.logging :as log]
   [sandbar.stateful-session :as session]
   [clog.lib.template :as tpl]
   [clojure.string :as str]
   [ring.util.response :as ring]
   [clog.model.blog :as blog]
   [clog.model.user :as user]
   [clog.lib.util :as util]))


(defn index []
  (let [posts (blog/latest-n-published 10)]
    (tpl/page-out "index" {:posts posts})))


(defn save-blog
  "Save this event and redirect to wherever the form indicates in the 'redir' param."
  ([id params]
     (let [blog (blog/load-one id)
           usr (user/load-one (:users_id params))]
       (log/error (str params))
       (blog/save id params)
       (ring/redirect (str "/edit/" id))))
  ([params]
     (let [usr (user/load-one (:users_id params))
           blog (blog/save params)]
       (ring/redirect (str "/edit/" (:id blog))))))


(defn post [id]
  (tpl/page-out "post" {:blog (util/markdown-to-html (:content (blog/load-one id)))}))


(defn edit
  ([]
     (tpl/page-app "edit" {:blog {}
                           :usr (user/load-one (session/session-get :user-id))}))
  ([id]
     (tpl/page-app "edit" {:blog (blog/load-one id)
                           :usr (user/load-one (session/session-get :user-id))})))


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
  (tpl/page-out "about" {}))


(defn contact []
  (tpl/page-out "contact" {}))


(defn adam []
  (tpl/page-out "adam" {}))
