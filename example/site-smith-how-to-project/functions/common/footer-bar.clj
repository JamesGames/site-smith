[links]
(let [link-func #(func/link "footerlink" (:pageName %) (:pageTitle %))]
     (str "<div id=\"footer\">"
          (clojure.string/join " | " (map link-func links))
          "</div>"))