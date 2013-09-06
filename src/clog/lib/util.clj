(ns clog.lib.util
  (:use
   [clojure.tools.logging :only (info error)])
  (:require
   [clojure.string :as str])
  (:import
   (java.security MessageDigest)
   (java.sql Timestamp)))


(defn current-date []
  (new java.util.Date))


(defn parse-sql-date [dt]
  (if-not (str/blank? dt)
    (new java.sql.Date (.getTime (.parse (new java.text.SimpleDateFormat "yyyy-MM-dd") dt)))))


(defn parse-date [dt]
  (if-not (str/blank? dt)
    (.parse (new java.text.SimpleDateFormat "yyyy-MM-dd") dt)))


(defn format-date [dt]
  (if-not (nil? dt)
    (.format (new java.text.SimpleDateFormat "yyyy-MM-dd") dt)
    ""))


(defn format-date-time [dt]
  (if-not (nil? dt)
    (.format (new java.text.SimpleDateFormat "yyyy-MM-dd HH:mm") dt)
    ""))


(defn current-date-str []
  (format-date (current-date)))


(defn uuid []
  "Generate a base 36 string from a UUID
  90a58d7a-d274-4dc4-a80e-37daa76b7fb8 --> 2odnrp7yitnl05rg975gmyeas"
  (.toString (new BigInteger (.replace (str (java.util.UUID/randomUUID)) "-" "") 16) 36))


(defn ^Timestamp ts-now
  ([]
    (ts-now 0))
  ([delta]
    (Timestamp. (+ (System/currentTimeMillis) delta))))


(defn keyword-keyify
  "Turns
    {\"a\" \"aa\" \"b\" \"bb\"}
    into
    {:b \"bb\", :a \"aa\"}
   "
  [hash]
  (zipmap (map keyword (keys hash)) (vals hash)))


(defn string-keyify
  "Turns
    {:b \"bb\", :a \"aa\"}
    into
    {\"a\" \"aa\" \"b\" \"bb\"}
   "
  [hash]
  (zipmap (map name (keys hash)) (vals hash)))


(defn dateify-params
  "if a key ends in _dt and its value is a string, convert that to a sql date."
  [params]
  (zipmap (keys params) 
          (for [kv (seq params)]
            (let [k (name (key kv))
                  v (val kv)]
                                        ; If it ends in _dt and is a string, then do the conversion of the value.
                                        ; Else just return it.
              (if (and (.endsWith k "_dt")
                       (= (type v) java.lang.String))
                (parse-sql-date v)
                v
                )))))

(defn undateify-params
  "if a value's type is a date-ish type, convert the date to a date string"
  [params]
  (zipmap (keys params) 
          (for [kv (seq params)]
            (let [k (name (key kv))
                  v (val kv)]
              (if (or (= (type v) java.sql.Timestamp)
                      (= (type v) java.sql.Date))
                (format-date v)
                v
                )))))


(defn debug-mode? []
  (= (get (System/getenv) "CLOG_DEBUG" "") "TRUE"))


(defn unix-timestamp-to-sql-timestamp [val]
  (if-not (nil? val)
    (java.sql.Timestamp. (* 1000 (Long. val)))))


(defn unix-timestamp-to-sql-date [val]
  (if-not (nil? val)
    (java.sql.Date. (* 1000 (Long. val)))))


(defn rename-key [kvmap old new]
  (assoc (dissoc kvmap old) new (kvmap old)))


(defn md5
  "Generate a md5 checksum for the given string"
  [token]
  (let [hash-bytes
         (doto (java.security.MessageDigest/getInstance "MD5")
               (.reset)
               (.update (.getBytes token)))]
       (.toString
         (new java.math.BigInteger 1 (.digest hash-bytes)) ; Positive and the size of the number
         16))) ; Use base16 i.e. hex


