;;
;; Define this namespace as the context for the Fleet template execution environment.
;;
(ns views
  (:use
   [clog.lib.util :as util]
   [clog.lib.helpers]))

;;
;; Fleet Template Rendering
;;
(ns clog.lib.template
  (:use
   [fleet])
  (:require
   [clog.lib.util :as util]
   [clojure.java.io]))

;; http://www.michaelnygard.com/blog/2011/01/glue_fleet_and_compojure_toget.html
;; (extend-protocol Renderable
;;   fleet.util.CljString
;;   (render [this _] (response (.toString this))))

(defn- common-vars [vars]
  (merge vars
         {:today util/current-date-str}))


(defn- render [header page footer vars]
  "Render everything with header, footer and common variables.
   this is for 'outside' pages (hence the -out suffix)

   In this example views/footer is a symbol passed in and eval'ed here.  Magic.
   Fleet tutorial:  https://github.com/Flamefork/fleet"
  (let [v (common-vars vars)]
    (str ((eval header) v)
         ((if (= (type page) java.lang.String)
            (eval (read-string page))
            (eval page)) v)
         ((eval footer) v))))

(defn page-out
  "(page-out 'views/footer {:abc 123})"
  [page vars]
  (render 'views/header-out page 'views/footer-out vars))


(defn page-edit
  "(page-edit 'views/footer {:abc 123})"
  [page vars]
  (render 'views/header-edit page 'views/footer-edit vars))


(defn page-render
  [header page footer vars]
  (render header page footer vars))


(defn compile-templates []
  (fleet-ns views "resources/templates"))


(defn wrap-recompile-templates
  [handler]
  "Middleware that forces recompile of fleet templates in the default location.  This allows
    the system to pick up all of the changes without doing anything explicit.
    Only works when the 'CLOG_DEBUG' variable is set to 'TRUE'.  This is handled in bin/serve
    "
  (fn [request]
    (if (util/debug-mode?)
      (compile-templates))
    (handler request)))


