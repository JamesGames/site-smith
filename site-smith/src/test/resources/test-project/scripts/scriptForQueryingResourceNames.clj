(
  (str *all-resource-names*)
  (str "resource names containing 'Resource' count: "
       (reduce
         #(if (.contains %2 "Resource")
           (+ %1 1)
           %1)
         0
         *all-resource-names*))
  )