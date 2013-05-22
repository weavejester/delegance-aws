(ns delegance.aws.simpledb
  "Implementation of the KeyValueStore for SimpleDB."
  (:require [delegance.protocols :refer :all]
            [cemerick.rummage :as sdb]
            [cemerick.rummage.encoding :as encoding]))

(deftype SimpleDBStore [config domain]
  KeyValueStore
  (get! [_ key]
    (read-string (:value (sdb/get-attrs config domain key))))
  (put [_ key value]
    (sdb/put-attrs config domain {::sdb/id key :value (pr-str value)}))
  (modify* [store key func]
    (put store key (func (get! store key)))))

(defn- sdb-client [cred]
  (let [client (sdb/create-client (cred :access-key) (cred :secret-key))]
    (when-let [endpoint (:endpoint cred)]
      (.setEndpoint client endpoint))
    client))

(defn- sdb-config [cred]
  (assoc encoding/keyword-strings :client (sdb-client cred)))

(defn simpledb-store [cred]
  (SimpleDBStore. (sdb-config cred) (:domain cred)))