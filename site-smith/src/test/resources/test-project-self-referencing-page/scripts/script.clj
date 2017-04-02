(util/str-ln "<a href=\"" (util/file-path util/*unique-name-of-page*) "\">to this page</a>")
(let [all-page-names util/*all-resource-names*
      all-page-names (sort all-page-names)]
  (clojure.string/join (map #(util/str-ln (func/link (util/file-path %) %))
                            all-page-names)))