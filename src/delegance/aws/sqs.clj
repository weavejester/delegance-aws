(ns delegance.aws.sqs
  (:require [delegance.protocols :refer :all]
            [cemerick.bandalore :as sqs]))

(deftype SQSQueue [client queue]
  Queue
  (push [_ data]
    (sqs/send client queue (pr-str data))
    nil)
  (reserve [_]
    (if-let [message (first (sqs/receive client queue :limit 1))]
      [(:receipt-handle message) (read-string (:body message))]))
  (finish [_ id]
    (sqs/delete client queue id)))

(defn- sqs-client [cred]
  (sqs/create-client (:access-key cred) (:secret-key cred)))

(defn sqs-queue [cred]
  (SQSQueue. (sqs-client cred) (:queue cred)))