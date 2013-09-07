
(ns clog.model.user
  (:use
   [korma.db]
   [korma.core])
  (:require
   [clog.lib.util :as util]
   ))


;;
;; User entity
;;
(defentity user
  (table :users))


(defn find-by-email [email]
  (first 
   (select user
           (where {:email email
                   :delete_dt nil}))))
    

(defn load-one [id]
  (first (select user
                 (where {:id id
                         :delete_dt nil}))))


(defn full-delete [id]
  (delete user (where {:id id})))


(defn authenticate [usr password]
  (and (not (nil? usr))
       (= (util/hash-md5 password) (usr :password))))


(defn save
  ([id params]
     (update user
             (set-fields (merge {:id id} params))
             (where {:id id}))) 

  ([params]
     (let [id (util/uuid)]
       (insert user
               (values (merge params {:id id}))))))


