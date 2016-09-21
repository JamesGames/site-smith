(str util/*all-resource-names*)
(str "resource names containing 'Resource' count: "
     (count (util/filtered-file-paths #(.contains % "Resource"))))
(str "resource names containing 'Resource' on a path containing 'directoryA' count: "
     (count (util/filtered-file-paths #(.contains % "Resource") #(.contains % "directoryA"))))
(str "with util call, resource names beginning with 'abc' count: "
     (count (util/file-paths-starting-with "abc")))
(str "with util call, resource names ending with 'def' count: "
     (count (util/file-paths-ending-with "def")))
(str "with string shortcut, resource names beginning with 'abc' count: "
     (count "resource*:abc*"))
(str "with string shortcut, resource names ending with 'def' count: "
     (count "resource*:*def"))