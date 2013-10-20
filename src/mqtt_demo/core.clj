(ns mqtt-demo.core
  (:gen-class)
  (:require [clojurewerkz.machine-head.client :as mh]))

(def id (mh/generate-id))
(def conn (mh/connect "tcp://127.0.0.1:1883" id))

(defn subscribes []
  (mh/subscribe conn ["hello"] (fn [topic meta payload]
                                 (println (String. payload "UTF-8"))))
  (mh/publish conn "hello" "hello world"))

(subscribes)

(defn over-pollution)

(def ^:const topic "nba/scores")

(defn start-consumer
  [conn ^String username]
  (mh/subscribe conn
                [topic]
                (fn [^String topic _ ^bytes payload]
                  (println (format "[consumer] %s received %s" username (String. payload "UTF-8"))))))

(defn -main
  [& args]
  (let [id    (mh/generate-id)
        conn  (mh/connect "tcp://127.0.0.1:1883" id)
        users ["joe" "aaron" "bob"]]
    (doseq [u users]
      (let [c (mh/connect "tcp://127.0.0.1:1883" (format "consumer.%s" u))]
        (start-consumer c u)))
    (mh/publish conn topic "BOS 101, NYK 89")
    (mh/publish conn topic "ORL 85, ALT 88")
    (Thread/sleep 100)
    (mh/disconnect conn)
    ))
