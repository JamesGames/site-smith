(ns org.jamesgames.sitesmith.text.TextScript
  (:use [clojure.string :only [starts-with?]])
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
  (reduce
    #(identity (if (starts-with? %2 startOfResourceReference)
                 (conj %1 (.apply name-to-path-func (subs %2 (count startOfResourceReference))))
                 (conj %1 %2)))
    [] arguments))

(defn- invoke-html-function
  [^Function name-to-path-func [function-name & function-args]]
  (let [function-to-call (resolve (symbol (str 'project-functions "/" function-name)))
        function-args-evaluated (map eval function-args)
        converted-arguments (resource-name-args-to-path name-to-path-func function-args-evaluated)]
    (function-to-call converted-arguments)))

(defn- is-html-function?
  [function-name]
  (resolve (symbol (str 'project-functions "/" function-name))))

(defn- invoke-function
  [^Function name-to-path-func function-expression]
  (if (is-html-function? (script-function-name function-expression))
    (invoke-html-function name-to-path-func function-expression)
    (eval function-expression)))

(defn- execute-script
  "Takes in a list of html-function calls to execute"
  [^Function name-to-path-func script-text]
  (reduce str (map (partial invoke-function name-to-path-func)
                   (script-text-to-clojure-structure script-text))))

