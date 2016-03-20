([links] (let [link-func
               #(str "<a class=\"footerlink\" href=\""
                     (:pageName %) "\">"
                     (:pageTitle %) "</a>")]
              (str "<div id=\"footer\">"
                   (clojure.string/join " | " (map link-func links))
                   "</div>")))