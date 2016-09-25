[links]
(let [link-func #(func/link "navbutton" (:pageName %) (:pageTitle %))]
     (str "<div id=\"navigation\">" (apply str (map link-func links)) "</div>"))