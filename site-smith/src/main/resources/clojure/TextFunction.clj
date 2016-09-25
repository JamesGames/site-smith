(ns org.jamesgames.sitesmith.text.TextFunction
  (:use clostache.parser)
  (:import (org.pegdown PegDownProcessor Extensions)))

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

(defn- is-vector-of-symbols
  [x]
  (and (vector? x)
       (every? (complement coll?) x)
       (every? (complement string?) x)))

(defn- is-function-text-in-valid-format?
  "Determines if a function is in a valid format, which is a list of two or three elements,
  a vector of parameter names, a list (that should be a string expression but is not checked),
  and then an optional vector that would contain names of options"
  [function-text]
  (let [function-text-in-list (str "(" function-text ")")
        function (function-string-to-clojure-structure function-text-in-list)
        element-count (count function)
        params (function-params function)
        func-body (function-func-body function)
        options (function-options function)
        is-list? (list? function)
        size-of-two-or-three-for-options? (or (= 2 element-count) (= 3 element-count))
        params-is-a-vector-of-symbols? (is-vector-of-symbols params)
        func-body-a-list? (list? func-body)
        options-vector-if-exists? (or (nil? options) (is-vector-of-symbols options))]
    (and is-list? size-of-two-or-three-for-options? params-is-a-vector-of-symbols?
         func-body-a-list? options-vector-if-exists?)))


(def markdown-extensions (int (bit-or Extensions/ABBREVIATIONS
                                      Extensions/AUTOLINKS
                                      Extensions/TABLES
                                      Extensions/DEFINITIONS
                                      Extensions/FENCED_CODE_BLOCKS
                                      Extensions/STRIKETHROUGH
                                      Extensions/ANCHORLINKS)))
(defn- markdown [x] (.markdownToHtml (PegDownProcessor. markdown-extensions) x))

(def option-to-func {:markdown markdown})

(defn- unresolvable-symbols
  [x]
  (filter #(not (contains? option-to-func (keyword %))) x))

;; check if resolvable to correct option type maybe move this to valid function check
(defn- symbols-resolvable?
  [options]
  (let [options-not-resolvable (unresolvable-symbols options)]
    [(empty? options-not-resolvable) options-not-resolvable]))

(defn- applyOptions
  [function-result function name]
  (let [options (function-options function)
        [options-valid? unresolved] (symbols-resolvable? options)]
    (if (not options-valid?)
      (throw (ex-info "Function options not resolvable"
                      {:function   name
                       :unresolved unresolved}))
      ((apply comp (map option-to-func (map keyword options))) function-result))))

(defn- make-let
  [params arguments body]
  (list 'let (into [] (apply concat (map vector params arguments))) body))

(defn- render-text
  "Returns a string after applying the arguments to the template from the text expression"
  [function arguments name]
  (let [params (function-params function)
        function-body-evaled (eval (make-let params arguments (function-func-body function)))
        argument-map-for-template-engine (zipmap (map keyword params) arguments)
        ^String rendered-text (render function-body-evaled argument-map-for-template-engine)
        options-applied-text (applyOptions rendered-text function name)]
    (str options-applied-text (System/lineSeparator))))

(defn- define-text-function
  "Defines a function based off of the text supplied that can be called by the user text scripts.
  Assumed format of a list with a vector of parameter names and an evaluable string expression and
  an optional vector of options"
  [function-name function-text]
  (let [function-text-in-list (str "(" function-text ")")
        function (function-string-to-clojure-structure function-text-in-list)]
    (intern (create-ns 'func)
            (symbol function-name)
            (fn [& arguments] (render-text function arguments function-name)))))







