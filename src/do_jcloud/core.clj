(ns do-jcloud.core
  (:import (org.jclouds.ContextBuilder)
           ( org.jclouds.compute ComputeServiceContext ComputeService)))

(def DO_TOKEN "YOUR DO TOKEN HERE")
(def PROVIDER "digitalocean2")
(def GROUP "jcloud")


(defn compute-service-context []
  (.. (org.jclouds.ContextBuilder/newBuilder PROVIDER)
      (credentials "NotUsed" DO_TOKEN)
      (buildView ComputeServiceContext)))

(defn compute-service [cs]
  (.getComputeService cs))

(defn close! [cs]
  (.close cs))

(defn create-nodes [cs members]
  (let [template (.. (compute-service cs)
                     templateBuilder
                     smallest
                     build)]
    (.. (compute-service cs)
        (createNodesInGroup GROUP members template))))


(defn list-nodes [cs]
  (map bean (.listNodes (compute-service cs))))


(defn destroy-node [cs node]
  (.destroyNode (compute-service cs) (:id node)))

(defn destroy-all-nodes [cs]
  (loop [nodes (list-nodes cs)]
    (if (first nodes)
      (do
        (destroy-node cs (first nodes))
        (recur (rest nodes))))))

#_(def CSC (compute-service-context))

#_(create-nodes CSC)

#_(clojure.pprint/pprint (list-nodes CSC))

#_(destroy-all-nodes CSC)

#_(close! CSC)

#_(defn foo
  "I don't do a whole lot."
  [x]
  (println x "Hello, World!"))
