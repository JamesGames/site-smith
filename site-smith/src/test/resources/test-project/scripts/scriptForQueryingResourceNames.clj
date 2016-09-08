(
  (str util/*all-resource-names*)
  (str "resource names containing 'Resource' count: "
       (count (util/get-resource-file-names #(.contains % "Resource"))))
  )