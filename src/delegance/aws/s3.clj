(ns delegance.aws.s3
  "Implementation of the KeyValueStore protocol for SimpleDB."
  (:require [delegance.protocols :refer :all]
            [aws.sdk.s3 :as s3]))

(deftype S3Store [cred bucket]
  KeyValueStore
  (get! [_ key]
    (read-string (slurp (:content (s3/get-object cred bucket key)))))
  (put [_ key value]
    (s3/put-object cred bucket key (pr-str value)))
  (modify* [store key func]
    (put store key (func (get! store key)))))

(defn s3-store
  "Create a key-value store interface for Delegance, based on an S3 bucket.
  Delegance will use the bucket to store information about jobs. Takes a map
  that accepts the following keys:
    :access-key - your AWS access key
    :secret-key - your AWS secret key
    :bucket     - the S3 bucket (must already exist)"
  [cred]
  (S3Store. cred (:bucket cred)))