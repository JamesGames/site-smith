([links] (let [link-func
               #(str "<a class=\"navbutton\" href=\"" (:pageName %) "\">" (:pageTitle %) "</a>")]
              (str "<div id=\"navigation\">" (apply str (map link-func links)) "</div>")))