[] (str "
Site Smith
==========

Site Smith is a static website generator programmed in Kotlin and Clojure (it generated this website).
I decided to make Site Smith to automate my own personal website in some capacity,
and to find a hobby project to learn Kotlin and Clojure.

My overall goal was to create a simple scripting environment (in Clojure), that can generate static websites.
My approach for this was to have 4 simple inputs into Site Smith:

1. A site layout file. A site map like file in json, which depicts the site's hierarchy of files, and
what scripts are used to generate a page, and various other options.

    ```json
    \"pages\": [
      {
        \"uniqueName\": \"home-page\",
        \"fileName\": \"index.html\",
        \"pageTitle\": \"Home Page\",
        \"textScriptsForPage\": [\"header\", \"homePageScript\", \"footer\"]
      }
    ],
    \"directories\": [
      {
        \"name\": \"how_to_use\",
        \"pages\": [
          {
            \"uniqueName\": \"howToPage\",
            \"fileName\": \"index.html\",
            \"pageTitle\": \"How to use Site Smith\",
            \"textScriptsForPage\": [\"header\", \"howToPageScript\", \"footer\"]
          }
    ```

2. A scripts directory. User written Clojure code in the form of Site Smith's \"text scripts\", where one file is one
script, which can be referenced from a page description in the layout file.

    ```clojure
    ;; header.clj
    (
      (image-link \"resource:homePage\" \"resource:site-smith-logo.png\" \"Site Smith header image\")
      (navigation-bar [{:pageName \"resource:homePage\" :pageTitle \"Home\"}
                     {:pageName \"resource:howToPage\" :pageTitle \"How to use\"}
                     {:pageName \"resource:sourceCodePage\" :pageTitle \"Source Code\"}
                     {:pageName \"resource:aboutPage\" :pageTitle \"About\"}])
      (str \"<p>Site generated on: \" (.toString (java.util.Date.)) \"</p>\")
    )
    ```

3. A functions directory. More user written Clojure code, but in form of Site Smith's \"text functions\", where one file
is one function, that can be called from a text script. A couple defining features of text functions are that string
arguments starting with \"resource:\" can auto evaluate to a relative path to the resource file or page that the rest of
string argument names, and that parameter names with the text function are passed to a templating text engine
(currently [Clostache](https://github.com/fhd/clostache)) for the final function result.
{{!changing template format so that I can print it out in an example here}
{{=<% %>=}}

    ```clojure
    ;; link.clj
    (
      ([class linkNameOrUrl text]
      (str \"<a class=\\\"{{class}}\\\" href=\\\"{{linkNameOrUrl}}\\\">{{text}}</a>\"))
    )
    ;; a call from a script with (link \"footer-link\" \"resource:howToPage\" \"how to use\")
    ;; from a page that is one directory below the target link would produce
    ;; <a class=\"footerlink\" href=\"how_to_use/\">how to use</a>
    ```

4. A resource file directory, whose contents are copied directly into the generated website directory structure as is,
or can be specified file by file through the site layout file on how and where they will appear in the site's structure.\n

The output is a directory containing the generated website.





Credits
-------
- James Murphy - JamesGames.Org(at)gmail(dot)com
")
[markdown]