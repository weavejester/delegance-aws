# Delegance for AWS

This library allows [Delegance][1] to work with Amazon's [SQS][2],
[SimpleDB][3] and [S3][4] services.

[1]: https://github.com/weavejester/delegance
[2]: http://aws.amazon.com/sqs/
[3]: http://aws.amazon.com/simpledb/
[4]: http://aws.amazon.com/s3/

## Installation

Add the following dependency to your `project.clj` file:

    [delegance/amazon-web-services "0.1.0"]

## Usage

Create a configuration map using `s3-store` and `sqs-queue`:

```clojure
(def cred
  {:access-key ...
   :secret-key ...})

(def config
  {:queue (sqs-queue (assoc cred :queue "delegance"))
   :store (s3-store (assoc cred :bucket "delegance"))})
```

Create the SQS queue and S3 bucket if necessary, then use the config
with delegance:

```clojure
(def worker
  (run-worker config))

(def result
  (delegate config `(+ 1 1))
```

## License

Copyright © 2013 James Reeves

Distributed under the Eclipse Public License, the same as Clojure.
