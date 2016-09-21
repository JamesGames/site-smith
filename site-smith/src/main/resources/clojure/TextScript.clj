(ns org.jamesgames.sitesmith.text.TextScript
  (:use [clojure.string :only [starts-with? ends-with?]]
        [clojure.walk :only [postwalk]])
  (:require [org.jamesgames.sitesmith.text.Util :as util])
  (:import (java.util.function Function)))


(defn- script-text-to-clojure-structure
  [script-text]
  (read-string script-text))

(defn- script-function-name
  [script]
  (first script))

(defn- is-script-in-valid-format?
  [script-text]
  (let [script-text-in-list (str "(" script-text ")")
        script (script-text-to-clojure-structure script-text-in-list)]
    (let [is-list (list? script)
          is-series-of-func-calls (every? list? script)
          funcs-are-not-empty-lists (every? #(< 0 (count %)) script)
          func-names-are-symbols (or (every? symbol? (map script-function-name script)))]
      (and is-list is-series-of-func-calls funcs-are-not-empty-lists func-names-are-symbols))))

(def startOfResourceReference "resource:")
(def startOfResourceListReference "resource*:")

(defn- resolve-resource-reference
  [resource-reference-str]
  (let [resource-name (subs resource-reference-str (count startOfResourceReference))]
    (util/file-path resource-name)))

(defn- resolve-resource-list-reference
  [resource-list-reference-str]
  (let [resource-name (subs resource-list-reference-str (count startOfResourceListReference))]
    (cond (starts-with? resource-name "*")
          (apply conj [] (util/file-paths-ending-with (subs resource-name 1)))
          (ends-with? resource-name "*")
          (apply conj [] (util/file-paths-starting-with (apply str (drop-last resource-name))))
          :else
          (throw (Exception.
                   (str "Invalid resource list reference: " resource-name
                        ". Requires an asterisk at either the start "
                        "or end of the partial resource name."))))))

(defn- resolve-str-shortcuts
  "Resolves string shortcuts. For example the string literal 'resource:SomeName' would be replaced with
  the relative file path of the named resource"
  [code]
  (postwalk #(let [is-resource-reference (starts-with? % startOfResourceReference)
                   is-resource-list-reference (starts-with? % startOfResourceListReference)
                   replacement (cond is-resource-reference (resolve-resource-reference %)
                                     is-resource-list-reference (resolve-resource-list-reference %)
                                     :else %)]
              replacement)
            code))

(defn- execute-script
  "Takes in a list of function calls to execute, these can be Site Smith text functions, or any function."
  [calling-page-name ^Function name-to-path-func script-text list-of-resource-names]
  (binding [*ns* (find-ns 'org.jamesgames.sitesmith.text.TextScript)
            ;; Following binds enable the util functions to work
            ;; The util functions are used by site smith scripts and functions
            util/*unique-name-of-page* calling-page-name
            util/*name-to-path-func* name-to-path-func
            util/*all-resource-names* (into #{} list-of-resource-names)]
    (let [script-text-in-list (str "(" script-text ")")
          script-structure (script-text-to-clojure-structure script-text-in-list)
          script-structure-with-str-resolves (resolve-str-shortcuts script-structure)]
      (reduce str (map (comp util/str-ln eval) script-structure-with-str-resolves)))))




