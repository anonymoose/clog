;;
;; Define functions here that are direcly callable from templates
;; if "getit" is defined here, you can call it from your fleet template like so:
;; <(getit)>
;;

(ns clog.lib.helpers
  (:use
   [clog.lib.util :as util]
   [markdown.core]
   ;; [endophile.core :only [mp to-clj]]
   ;; [endophile.hiccup :only [to-hiccup]]
   ;; [hiccup.core :only [html]]
   )
  (:require
   [clojure.string :as str]))


(defn ghosted-input
  [id data obj-key field-key default & options]
  "Optionally 'ghost' the input field, but only if the field from the database is null.  Insert a default if required
  <(ghosted-input 'fname' data :user :fname 'First Name')>
  "
  (let [options (first options)
        dbvalue (get (data obj-key) field-key)
        value (if (nil? dbvalue) default dbvalue)
        ghost-class (if (nil? dbvalue) "ghost")]
    (str "<input type=\"text\" name=\"" id "\" id=\"" id "\" class=\"" (get options :class "input-small-kl") " " ghost-class "\" value=\"" value "\"/>"))
  )


(defn format-title-for-link [title]
  (str/replace (.toLowerCase title) " " "-"))



;; (defn markdown-to-html [markdown]
;;   (let [parsed (mp markdown)]
;;     (html (to-hiccup parsed))))
