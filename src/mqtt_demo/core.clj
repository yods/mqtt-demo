(ns mqtt-demo.core
  (:gen-class)
  (:require [clojurewerkz.machine-head.client :as mh]))

(def conn (mh/connect "tcp://127.0.0.1:1883" (mh/generate-id)))
(defn subscribes []
  (mh/subscribe conn ["hello"] (fn [topic meta payload]
                                 (println (String. payload "UTF-8"))))
  (mh/publish conn "hello" "hello world"))

(subscribes)

(defn over-pollution [limit]
  (mh/subscribe conn ["Pollution"]
                (fn [topic meta payload]
                  (let [pollution (Integer. (String. payload "UTF-8"))]
                   (if (> pollution limit)
                     (println
                      (format "Danger pollution level is currently at %s over the limit of %s" pollution limit)))))))

(defn -main []
  (over-pollution 65)
  (dotimes [n 10]
    (mh/publish conn "Pollution" (str (* n 10)))
    )
  (Thread/sleep 100))

(-main)

(mh/disconnect conn)
