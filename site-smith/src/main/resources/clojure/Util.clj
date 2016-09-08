(ns org.jamesgames.sitesmith.text.Util
  (:import (java.util.function Function)))

(declare ^:dynamic *unique-name-of-page*)
(declare ^:dynamic ^Function *name-to-path-func*)
(declare ^:dynamic *all-resource-names*)

(defn get-file-names
  ([] *all-resource-names*)
  ([resource-names]
   (map #(.apply *name-to-path-func* %) resource-names)))

(defn get-resource-file-names
  ([filter-on-resource-names]
   (get-resource-file-names filter-on-resource-names (constantly true)))
  ([filter-on-resource-names filter-on-file-names]
   (let [filtered-file-names (get-file-names (filter filter-on-resource-names *all-resource-names*))
         filtered-resource-names (filter filter-on-file-names filtered-file-names)]
     filtered-resource-names)))