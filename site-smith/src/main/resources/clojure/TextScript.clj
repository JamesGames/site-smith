(ns org.jamesgames.sitesmith.text.TextScript
  (:use [clojure.string :only [starts-with?]]
        [clojure.walk])
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
(defn- resource-name-args-to-path
  [^Function name-to-path-func arguments]
  (postwalk #(if (starts-with? % startOfResourceReference)
              (conj (.apply name-to-path-func (subs % (count startOfResourceReference))))
              (conj %)) arguments))

(defn- invoke-text-function
  [^Function name-to-path-func [function-name & function-args]]
  (let [function-to-call (resolve (symbol (str 'project-functions "/" function-name)))
        function-args-evaluated (map eval function-args)
        converted-arguments (resource-name-args-to-path name-to-path-func function-args-evaluated)]
    (function-to-call converted-arguments)))

(defn- is-text-function?
  [function-name]
  (resolve (symbol (str 'project-functions "/" function-name))))

(declare ^:dynamic *unique-name-of-page*)

(defn- invoke-function
  [calling-page-name ^Function name-to-path-func function-expression]
  (binding [*ns* (find-ns 'org.jamesgames.sitesmith.text.TextScript)
            *unique-name-of-page* calling-page-name]
    (if (is-text-function? (script-function-name function-expression))
      (invoke-text-function name-to-path-func function-expression)
      (eval function-expression))))

(defn- execute-script
  "Takes in a list of html-function calls to execute"
  [calling-page-name ^Function name-to-path-func script-text]
  (reduce str (map (partial invoke-function calling-page-name name-to-path-func)
                   (script-text-to-clojure-structure script-text))))

