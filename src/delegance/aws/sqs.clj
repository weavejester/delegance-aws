(ns delegance.aws.sqs
  "Implementation of the Queue protocol for SQS."
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

(defn sqs-queue
  "Create a queue interface for Delegance, based around Amazon SQS, which
  Delegance will use to distribute jobs to workers. Takes a map that accepts
  the following keys:
    :access-key - your AWS access key
    :secret-key - your AWS secret key
    :queue      - the name of the queue to push messages to"
  [cred]
  (SQSQueue. (sqs-client cred) (:queue cred)))
