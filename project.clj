(defproject kalance "0.1.0-SNAPSHOT"
  :min-lein-version "2.0.0"
  :description "clog - A simple, efficient blog in clojure."
  :url ""
  :plugins [[lein-ring "0.8.6"]]
  :ring {:handler clog.core/ring-handler
         :port 5000
         :auto-relaod? true
         :reload-paths ["src" "resources/templates"]}
  :dependencies [[org.clojure/clojure "1.5.1"]
                 [ring "1.2.0"]
                 [org.clojure/tools.namespace "0.2.4"] ; https://github.com/clojure/tools.namespace
                 [postgresql "9.1-901.jdbc4"]
                 [ring.middleware.logger "0.4.0"]  ; https://github.com/pjlegato/ring.middleware.logger
                 [korma "0.3.0-RC5"]
                 [compojure "1.1.5"]
                 [c3p0/c3p0 "0.9.1.2"]
                 [clj-http "0.7.5"]
                 [robert/hooke "1.3.0"]
                 [sandbar/sandbar "0.4.0-SNAPSHOT"]
                 [hiccup "1.0.2"]
                 [fleet "0.10.1"]
                 [clj-time "0.6.0"]
                 [clj-json "0.5.3"]]
  :main clog.core)

