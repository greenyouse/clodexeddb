(ns clodexeddb.core
  (:require [ydn.db :as db]
            [ydn.db.KeyRange :as kr])
  (:import [ydn.db Storage]))


;; ydn.db.Request problem with async callbacks
;; see /ydn/db/base/request.js

;; get-query and value-query don't work yet. They return a ydn.db.Request which
;; invokes an async callback. When compiled, the queries can be passed a .done()
;; or .fail() callback but this doesn't work with gclosure format. Not sure
;; what to do here.

(comment (enable-console-print!))

(defn setup
  "Defines a new database with a given schema. An optional ydn-db schema
  may be given, otherwise a default one will be provided. See here for more
  information: http://dev.yathit.com/ydn-db/doc/setup/schema.html"
  ([name]
   (let [schema {:stores [{:name "database"
                           :keyPath "name"
                           :indexes [{:keyPath "value"
                                      :type "TEXT"}]}]}
         s (clj->js schema)]
     (Storage. name s)))
  ([name schema]
   (let [s (clj->js schema)]
     (Storage. name s))))

;; TODO: find a better way to deal with db, shouldn't have to pass it around
;;   like this
(comment (def test-db
            (setup "test")))

(defn rm-db
  "Removes a database"
  [db-name]
  (db/deleteDatabase db-name))

(comment (rm-db "test"))

(defn add
  "A funciton for adding an object to an ObjectStore.
  db -- the database defined with setup
  store -- the name of an ObjectStore
  value -- the value to add (as a clojure map, follows your schema)"
  [db store value]
  (let [obj (clj->js value)]
    (.add db store obj)))

(comment (add test-db "database" {:name "stuff"
                                   :value "this is stuff"}))

(defn clear
  "Removes an item from an ObjectStore
  db -- the database defined with setup
  store -- the name of an ObjectStore
  value -- the value to remove (as a string, primary key of your schema)"
  [db store name]
  (.clear db store (kr/only name)))

;; see how "stuff" is the primary key for the ObjectStore?
(comment (clear test-db "database" "stuff"))

(defmulti key-range (fn [call & args] call))

;; All keys ?x or (if true) All keys < x
(defmethod key-range :upper-bound [call val ceil]
  (kr/upperBound val (or ceil false)))

;; All keys ?x or (if true) All keys > x
(defmethod key-range :lower-bound [call val floor]
  (kr/lowerBound val (or floor false)))

;; All keys ?x && ?y  -- false, false
;; All keys > x && < y -- true, true
;; All > x && ?y -- true, false
;; All ?x && < y -- false, true
(defmethod key-range :bound [call val floor ceil]
  (kr/bound val (or floor false) (or ceil false)))

;; All keys = x, most efficient
(defmethod key-range :only [call val]
  (kr/only val))

;; String or Array keys starting with x
(defmethod key-range :starts [call val]
  (kr/starts val))



;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;; Queries

(defn value-query
  "Gets a value using an optional key range query. This works on indexes of an
  ObjectStore.
  db -- the database defined with setup
  store -- the name of an ObjectStore
  field -- an index of an ObjectStore
  k-range -- a key-range of the field to search in
  callback -- returns a JS callback with the results
  limit -- limit for results, defaults to 100
  offset -- offset for results, defaults to 0"
  ([db store callback]
   (-> (.values db store)
       (.done (comp callback #(js->clj %)))))
  ([db store field k-range callback]
   (-> (.values db store field k-range)
       (.done (comp callback #(js->clj %)))))
  ([db store field k-range callback limit]
   (-> (.values db store field k-range limit)
       (.done (comp callback #(js->clj %)))))
  ([db store field k-range callback limit offset]
   (-> (.values db store field k-range limit offset)
       (.done (comp callback #(js->clj %))))))


(comment (let [k (key-range :only "this is stuff")]
            (value-query test-db "database" "value" k
              (fn [x] (println x))))

         ;; won't work on primary keys, try this:
         (value-query test-db "database" "name" "stuff"
           (fn [x] (println x))))

(defn get-query
  "Retrieves an item from an ObjectStore.
  db -- the database defined with setup
  store -- the name of an ObjectStore
  val -- the primary key of an item in an ObjectStore
  callback -- returns a JS callback with results"
  [db store val callback]
  (-> (.get db store val)
      (.done (comp callback #(js->clj %)))))

(comment (get-query test-db "database" "stuff"
           (fn [x] (println (get x "value")))))
