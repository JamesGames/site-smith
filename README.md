![](example/site-smith-how-to-project/resources/logo/site-smith-logo.png)

Site Smith
==========

Site Smith is a static website generator programmed in Kotlin and Clojure.
I decided to make Site Smith to automate my own personal website in some capacity,
and to find a hobby project to learn Kotlin and Clojure.

My overall goal was to create a simple scripting environment (in Clojure), that can generate static websites.
My approach for this was to have 4 simple inputs into Site Smith:

1. A site layout file. A site map like file in json, which depicts the site's hierarchy of files, and
what scripts are used to generate a page, and various other options.

    ```json
    "pages": [
      {
        "uniqueName": "home-page",
        "fileName": "index.html",
        "pageTitle": "Home Page",
        "textScriptsForPage": ["header", "homePageScript", "footer"]
      }
    ],
    "directories": [
      {
        "name": "how_to_use",
        "pages": [
          {
            "uniqueName": "howToPage",
            "fileName": "index.html",
            "pageTitle": "How to use Site Smith",
            "textScriptsForPage": ["header", "howToPageScript", "footer"]
          }
    ```

2. A scripts directory. User written Clojure code in the form of Site Smith's "text scripts", where one file is one
script, which can be referenced from a page description in the layout file.

    ```clojure
    ;; header.clj
    (
      (image-link "resource:homePage" "resource:site-smith-logo.png" "Site Smith header image")
      (navigation-bar [{:pageName "resource:homePage" :pageTitle "Home"}
                     {:pageName "resource:howToPage" :pageTitle "How to use"}
                     {:pageName "resource:sourceCodePage" :pageTitle "Source Code"}
                     {:pageName "resource:aboutPage" :pageTitle "About"}])
      (str "<p>Site generated on: " (.toString (java.util.Date.)) "</p>")
    )
    ```

3. A functions directory. More user written Clojure code, but in form of Site Smith's "text functions", where one file
is one function, that can be called from a text script. A couple defining features of text functions are that string
arguments starting with "resource:" can auto evaluate to a relative path to the resource file or page that the rest of
string argument names, and that parameter names with the text function are passed to a templating text engine
(currently [Clostache](https://github.com/fhd/clostache)) for the final function result.

    ```clojure
    ;; link.clj
    (
      ([class linkNameOrUrl text]
      (str "<a class=\"{{class}}\" href=\"{{linkNameOrUrl}}\">{{text}}</a>"))
    )
    ;; a call from a script with (link "footer-link" "resource:howToPage" "how to use")
    ;; from a page that is one directory below the target link would produce
    ;; <a class="footerlink" href="how_to_use/">how to use</a>
    ```
    
4. A resource file directory, whose contents are copied directly into the generated website directory structure as is,
or can be specified file by file through the site layout file on how and where they will appear in the site's structure.

The output is a directory containing the generated website.


How to Use
==========

A good place to learn Site Smith is from the example project that the code base comes with, as well as the
unit tests for Site Smith. I won't make a tutorial here on how to use Site Smith, as how it works is a moving target since
it is a hobby project, but I'll go over some useful features and characteristics of the tool that I find useful to me.

Unique Names
------------
Throughout a project's layout file, you'll see references to the idea of a *unique name*. For example:
```json
"uniqueName": "home-page",
"fileName": "index.html",
"pageTitle": "Home Page",
```
This unique name, is used to refer to a particular file in the project.
When found in a text function string argument prefixed with `resource:`
(so `resource:home-page` for this example), will be changed to a string containing the relative path between the
page executing the script and the file represented by the unique name. This is useful as all links will work offline
when locally viewing the generated website, and don't need to be hardcoded to reference any particular domain names or
directories that may change.

Template Engine
---------------
All function text results are applied to a template engine, allowing you to insert values, call functions, repeat
data in a list, using special syntax. The syntax and features of the template engine are currently
those found in the [Clostache](https://github.com/fhd/clostache) project.
```clojure
([paragraphText](str "<p>{{paragraphText}}</p>"))
;; instead of (but still possible if you wish)
([paragraphText](str "<p>" paragraphText "</p>"))
```


Post Processing Options
-----------------------
A function text can contain a vector of options that detail any post processing to be done to the result of the function
*before* the template engine is applied. Currently one option exists, which is "markdown" as a string. This will apply
a markdown engine (currently [pegdown](https://github.com/sirthias/pegdown)) to the result.
```clojure
([linkName linkUrl]
  (str "[{{linkName}}]({{linkUrl}})")
  ["markdown"])
```

Current Site Layout File Structure
----------------------------------
Here's a copy of the internal data structure of the layout file:
```kotlin
internal class SiteLayout(val root: SiteLayout.DirectoryInfo,
                          val globalCssFileName: String?,
                          val specifyResourcesByDirectory: Boolean) {

    data class PageInfo(val fileName: String,
                        val uniqueName: String? = fileName,
                        val pageTitle: String,
                        val additionalCssFiles: List<String>? = ArrayList(),
                        val textScriptsForPage: List<String>? = ArrayList())

    data class ResourceInfo(val fileName: String,
                            val uniqueName: String? = fileName,
                            val fileNameInResourceDir: String? = fileName)

    data class DirectoryInfo(val name: String,
                             val pages: List<PageInfo>? = ArrayList(),
                             val resources: List<ResourceInfo>? = ArrayList(),
                             val directories: List<DirectoryInfo>? = ArrayList())
}
```

Command Line Arguments
======================
    usage: java -jar [NAME-OF-JAR-FILE] [OPTIONS]
    
    Options:
     -pd,--project-directory <arg>   The directory containing the project's
                                     file such as the resources, text scripts
                                     and functions directories (auto-created
                                     if needed)
     -sl,--site-layout <arg>         The file depicting the layout of the
                                     files and directories for the generated
                                     website

How to Build
============
Site Smith uses [Maven](https://maven.apache.org/) to build. Run maven in the root project directory.


Credits
-------
- James Murphy - JamesGames.Org(at)gmail(dot)com

![](example/site-smith-how-to-project/resources/logo/site-smith-badge.png)