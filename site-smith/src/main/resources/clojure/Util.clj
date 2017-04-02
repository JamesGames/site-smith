(ns org.jamesgames.sitesmith.text.Util
  (:use [clojure.string :only [starts-with? ends-with?]])
  (:import (java.util.function Function)))

(declare ^:dynamic *unique-name-of-page*)
(declare ^:dynamic ^Function *name-to-path-func*)
(declare ^:dynamic *all-resource-names*)

(defn all-file-paths
  ([] *all-resource-names*)
  ([resource-names]
   (map #(.apply *name-to-path-func* %) resource-names)))

(defn filtered-file-paths
  ([filter-on-resource-names]
   (filtered-file-paths filter-on-resource-names (constantly true)))
  ([filter-on-resource-names filter-on-file-names]
   (let [filtered-file-names (all-file-paths (filter filter-on-resource-names *all-resource-names*))
         filtered-resource-names (filter filter-on-file-names filtered-file-names)]
     filtered-resource-names)))

(defn file-paths-starting-with
  [starting-with]
  (filtered-file-paths #(starts-with? % starting-with)))

(defn file-paths-ending-with
  [starting-with]
  (filtered-file-paths #(ends-with? % starting-with)))

(defn file-path
  [resource-name]
  (.apply *name-to-path-func* resource-name))

(defn str-ln
  [& text]
   (str (apply str text) (System/lineSeparator)))