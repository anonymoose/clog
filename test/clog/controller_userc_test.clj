(ns clog.controller_userc_test
  (:use [clog.controller.userc :as userc]
        [clog.lib.db :only [connect-db]]
        [clog.lib.template :only [compile-templates]]
        [clog.common_testing]
        )
  (:require [clojure.test :refer :all]
            [clog.model.user :as user]
            ))


(deftest test-sign-in
  (testing "Show signin form"
    (connect-db)
    (compile-templates)
    (is (> (.indexOf (userc/sign-in true) "Email or password incorrect") 0))
    (is (< (.indexOf (userc/sign-in false) "Email or password incorrect") 0))
    (is (= "/sign-in/deny" (get ((userc/sign-in "bogusid" "boguspwd") :headers) "Location")))
    (let [usr (create-test-user)
          resp (sessionize #(userc/sign-in "test@email.com" "password"))]
      (is (= (str "/contractor-home/" (:id usr))
             (get (:headers resp) "Location")))
      (is (= (:id usr) (:user-id (:_sandbar_session (:session resp)))))
      (delete-test-user)
      )
    ))




