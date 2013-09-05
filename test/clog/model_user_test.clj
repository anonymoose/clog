(ns clog.model_user_test
  (:use [clog.model.user :as user]
        [clog.lib.template :only [compile-templates]]
        [clog.lib.db :only [connect-db]]
        [clog.common_testing]        )
  (:require [clojure.test :refer :all]))


(deftest test-save-create
  (testing "Create user"
    (connect-db)
    (compile-templates)
    (let [id (:id (create-test-user))]
      (is (= id (:id (load-one id))))
      (is (= id (:id (find-by-email "test@email.com"))))
      (is (= id (:id (full-delete id))))
      (is (= nil (:id (load-one id))))
      )))


(deftest test-save-update
  (testing "Update user"
    (connect-db)
    (compile-templates)
    (let [id (:id (create-test-user))]
      (is (= id (:id (user/save id {
                                    :fname "x" 
                                    :lname "y" 
                                    }))))
      (is (= "x" (:fname (load-one id))))
      (is (= "y" (:lname (load-one id))))
      (is (= id (:id (full-delete id)))))))


(deftest test-authenticate
  (testing "Authenticate user"
    (connect-db)
    (compile-templates)
    (let [usr (create-test-user)
          id (:id usr)]
      (is (user/authenticate usr "password"))
      (is (not (user/authenticate usr "bogus")))
      (is (not (user/authenticate nil "bogus")))
      (is (= id (:id (full-delete id)))))))



