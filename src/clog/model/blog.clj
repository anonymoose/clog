
(ns clog.model.blog
  (:use
   [korma.db]
   [korma.core])
  (:require
   [clog.lib.util :as util]
   [clojure.string :as str]
   ))


;;
;; Blog entity
;;
(defentity blog)


(defn latest-n [n]
  (select blog (where
                {:delete_dt nil
                 })
          (order :create_dt)
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


