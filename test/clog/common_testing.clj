(ns clog.common_testing
  (:use [clog.model.user :as user]
        [sandbar.stateful-session]))


(def test-user-params
  {
   :fname "fname" 
   :lname "lname" 
   :email "test@email.com" 
   :password "password" 
   })


(def test-user-params-as-string-keys
  (zipmap (map name (keys test-user-params)) (vals test-user-params)))


(defn create-test-user []
  (user/save test-user-params))


(defn delete-test-user []
  (user/full-delete (:id (user/find-by-email "test@email.com"))))


; run a controller type function in the context of a session.
(defn sessionize [f]
  ((wrap-stateful-session* (fn [r] (f))) {}))


(defmacro responsify
  "wrap fn call in a response to make stateful session happy during testing." 
  [func]
  `{:status 200, :headers {"Location" "/"}, :body ~func })


(defmacro with-log-in
  "execute a function as a logged in user."
  [usr func]
  `(sessionize #(do
                  (userc/sign-in (:email 'usr) (:password 'usr))
                  ~func
                  )))