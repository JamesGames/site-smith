(ns org.jamesgames.sitesmith.text.TextScript
  (:use [clojure.string :only [starts-with?]]
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
  (let [script (script-text-to-clojure-structure script-text)]
    (and (list? script)
         (every? list? script)
         ;; No empty calls
         (every? #(< 0 (count %)) script)
         ;; function names must be symbols
         (or (every? symbol? (map script-function-name script))))))

(def startOfResourceReference "resource:")
(defn- resolve-str-shortcuts
  "Resolves string shortcuts. For example the string literal 'resource:SomeName' would be replaced with
  the relative file path of the named resource"
  [^Function name-to-path-func code]
  (postwalk #(if (starts-with? % startOfResourceReference)
              (conj (.apply name-to-path-func (subs % (count startOfResourceReference))))
              (conj %)) code))

(defn- execute-script
  "Takes in a list of function calls to execute, these can be Site Smith text functions, or any function."
  [calling-page-name ^Function name-to-path-func script-text list-of-resource-names]
  (binding [*ns* (find-ns 'org.jamesgames.sitesmith.text.TextScript)
            ;; Following binds enable the util functions to work
            ;; The util functions are used by site smith scripts and functions
            util/*unique-name-of-page* calling-page-name
            util/*name-to-path-func* name-to-path-func
            util/*all-resource-names* (into #{} list-of-resource-names)]
    (let [script-structure (script-text-to-clojure-structure script-text)
          script-structure-with-str-resolves (resolve-str-shortcuts name-to-path-func script-structure)]
      (reduce str (map (partial eval) script-structure-with-str-resolves)))))



