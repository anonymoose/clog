(ns clog.lib.web
  (:use
   [clojure.tools.logging :only (info error)]
   [ring.util.response :as ring]    
   [sandbar.stateful-session]
   [clog.lib.util :as util]
   [clj-json.core :as json]
   ))


(defn logged-in?
  "If there is a userid in the session, then execute the function.
   If an ID is provided, make sure it matches the one in session.
   Otherwise, kill the session and barf out somewhere."
  ([id func]
     (let [session-user-id (session-get :user-id)]
       (if (and (not (nil? session-user-id))
                (= session-user-id id))
         (func)
         (do
           (destroy-session!)
           (ring/redirect (str "/sign-in/deny"))))
       ))
  ([func]
     (let [session-user-id (session-get :user-id)]
       (if (not (nil? session-user-id))
         (func)
         (do
           (destroy-session!)
           (ring/redirect (str "/sign-in/deny"))))
       )))


(defn json-api
  "set the mime type and convert whatever objects/arrays into json string"
  [data]
  (error (type data))
  (let [newdata (if (or (vector? data) (seq? data))
                  (for [o (seq data)]
                    (util/undateify-params o))
                  (util/undateify-params data))]
    {:status 200
     :headers {"Content-Type" "application/json"}
     :body (json/generate-string newdata)})
  )

