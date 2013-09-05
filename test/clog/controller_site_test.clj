(ns kalance.controller_site_test
  (:use [kalance.controller.site :as site]
        [kalance.lib.db :only [connect-db]]
        [kalance.lib.template :only [compile-templates]]
        [kalance.common_testing])
  (:require [clojure.test :refer :all]))


(deftest test-index
  (testing "Index page"
    (connect-db)
    (compile-templates)
    (is (> (.indexOf (site/index) "<html") 0))))





