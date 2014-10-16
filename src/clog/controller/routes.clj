;;
;; Compojure Routing
;;

(ns clog.controller.routes
  (:use
   [compojure.core]
   [clojure.tools.logging :only (info error)]
   [clog.lib.web]
   [clog.lib.util :as util]
   )
  (:require
   [compojure.route :as route]
   [clog.controller.site]
   [clog.controller.userc]
   ))


(defroutes app-routes


  ; dynamic, external routes.  Maybe forms.  Maybe A/B tested, etc.
  (GET "/" [] (clog.controller.site/index))

  (GET "/adam" [] (clog.controller.site/adam))

  (GET "/sign-in/deny" [] (clog.controller.userc/sign-in true))
  (GET "/sign-in" [] (clog.controller.userc/sign-in false))
  (POST "/sign-in" [email password] (clog.controller.userc/sign-in email password))

  (GET "/sign-out" [] (clog.controller.userc/sign-out))

  (POST "/save"
        {params :form-params}
        (logged-in? #(if (empty? (params "id"))
                       (clog.controller.site/save-blog (util/keyword-keyify params))
                       (clog.controller.site/save-blog (params "id") (util/keyword-keyify params))
                       )))

  (GET "/publish/:id" [id]
       (logged-in? #(clog.controller.site/publish-blog id)))
  (GET "/unpublish/:id" [id]
       (logged-in? #(clog.controller.site/unpublish-blog id)))
  (GET "/delete/:id" [id]
       (logged-in? #(clog.controller.site/delete-blog id)))


  (GET "/dashboard" [] (logged-in? #(clog.controller.userc/dashboard)))
  (GET "/new" [] (logged-in? #(clog.controller.site/edit)))
  (GET "/post/:id" [id] (clog.controller.site/post id))
  (GET "/post/:id/:title" [id title] (clog.controller.site/post id))
  (GET "/edit/:id" [id] (logged-in? #(clog.controller.site/edit id)))



  ; housekeeping routes
  (route/resources "/")
  (route/not-found "404 Not Found"))
