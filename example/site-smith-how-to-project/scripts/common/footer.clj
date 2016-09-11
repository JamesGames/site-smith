(
  ;; closes main div, assumes footer comes after header, and always (would be nice to improve this)
  (str "</div>")
  (func/footer-bar [{:pageName "resource:homePage" :pageTitle "Home"}
               {:pageName "resource:howToPage" :pageTitle "How to use"}
               {:pageName "resource:sourceCodePage" :pageTitle "Source Code"}
               {:pageName "resource:contactPage" :pageTitle "Contact"}
               {:pageName "https://github.com/JamesGames/site-smith" :pageTitle "Github"}])
  (str "<div style=\"float: right;\">")
  (func/image-link
    "https://github.com/JamesGames/site-smith"
    "resource:site-smith-badge.png"
    "Site Smith badge icon")
  (str "</div>")
  )