(ns clog.model.blog
  (:use
   [korma.db]
   [korma.core]
   )
  (:require
   [clojure.tools.logging :as log]
   [clog.lib.db :as db]
   [clog.lib.util :as util]
   [clojure.string :as str]
   [clj-json.core :as json]
   [clojure.tools.logging :only [info error]]
   [digest]
   ))

;;
;; Blog entity
;;
(defentity blog)


(defn latest-n-published [n]
  (select blog (where
                {:delete_dt nil
                 :publish_dt [not= nil]
                 })
          (order :publish_dt :DESC)
          (limit n)))


(defn latest-n [n]
  (select blog (where
                {:delete_dt nil})
          (order :publish_dt :DESC)
          (limit n)))


(defn load-one [id]
  (first (select blog
                 (where {:id id
                         :delete_dt nil}))))


(defn full-delete [id]
  (delete blog (where {:id id})))


(defn save
  ([id params]
     (update blog
             (set-fields (merge params {:id id
                                        :content (str/trim (:content params))}))
             (where {:id id})))
  ([params]
     (let [id (util/uuid)]
       (insert blog
               (values (merge params {:id id
                                      :content (str/trim (:content params))}))))))


(defn publish [id]
  (update blog
          (set-fields {:publish_dt (util/ts-now)})
          (where {:id id})))


(defn unpublish [id]
  (update blog
          (set-fields {:publish_dt nil})
          (where {:id id})))


(defn del [id]
  (update blog
          (set-fields {:delete_dt (util/ts-now)})
          (where {:id id})))
