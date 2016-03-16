(ns org.jamesgames.sitesmith.text.TextFunction
  (:use clostache.parser)
  (:import (org.pegdown PegDownProcessor)))

(defn- function-string-to-clojure-structure
  [function]
  (read-string function))

(defn- function-params
  [function]
  (first function))

(defn- function-func-body
  [function]
  (second function))

(defn- function-options
  [function]
  (second (next function)))

(defn- is-function-text-in-valid-format?
  "Determines if a function is in a valid format, which is a list of two or three elements,
  a vector of parameter names, a list (that should be a string expression but is not checked),
  and then an optional vector that would contain names of options"
  [function]
  (let [function (function-string-to-clojure-structure function)
        element-count (count function)
        params (function-params function)
        func-body (function-func-body function)
        options (function-options function)]
    (and
      (list? function)
      (or (= 2 element-count) (= 3 element-count))
      ;; 1st element is a vector of symbols
      (and (vector? params) (every? (complement coll?) params) (every? (complement string?) params))
      (list? func-body)
      ;; 3rd element if exists is vector of strings
      (if (not (nil? options))
        ;; options is a vector of strings
        (vector? options)
        ;; no options is okay
        true))))


(def markdown-option "markdown")
(def optionToAction {(keyword markdown-option) #(.markdownToHtml (PegDownProcessor.) %)})
(defn- applyOptions
  [function-result function]
  (reduce
    #(if (contains? optionToAction (keyword %2))
      ((optionToAction (keyword %2)) %1)
      %1)
    function-result
    (map eval (function-options function))))

(defn- make-let
  [params arguments body]
  (list 'let (into [] (apply concat (map vector params arguments))) body))

(defn- render-text
  "Returns a string after applying the arguments to the template from the text expression"
  [function arguments]
  (let [params (function-params function)
        function-body-evaled (eval (make-let params arguments (function-func-body function)))
        argument-map-for-template-engine (zipmap (map keyword params) arguments)
        ^String rendered-text (render function-body-evaled argument-map-for-template-engine)]
    (str (applyOptions rendered-text function) (System/lineSeparator))))

(defn- define-text-function
  "Defines a function based off of the text supplied that can be called by the user text scripts.
  Assumed format of a list with a vector of parameter names and an evaluable string expression and
  an optional vector of options"
  [function-name function-text]
  (let [function (function-string-to-clojure-structure function-text)]
    (intern (create-ns 'project-functions)
            (symbol function-name)
            (fn [arguments] (render-text function arguments)))))







