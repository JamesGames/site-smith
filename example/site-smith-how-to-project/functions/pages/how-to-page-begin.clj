[] (str "
How to Use
==========

A good place to learn Site Smith is from the example project that the code base comes with, as well as the
unit tests for Site Smith. I won't make a tutorial here on how to use Site Smith, as how it works is a moving target since
it is a hobby project, but I'll go over some useful features and characteristics of the tool that I find useful to me.

Unique Names
------------
Throughout a project's layout file, you'll see references to the idea of a *unique name*. For example:
```json
\"uniqueName\": \"home-page\",
\"fileName\": \"index.html\",
\"pageTitle\": \"Home Page\",
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
([paragraphText](str \"<p>{{paragraphText}}</p>\"))
;; instead of (but still possible if you wish)
([paragraphText](str \"<p>\" paragraphText \"</p>\"))
```


Post Processing Options
-----------------------
A function text can contain a vector of options that detail any post processing to be done to the result of the function
*before* the template engine is applied. Currently one option exists, which is \"markdown\" as a string. This will apply
a markdown engine (currently [pegdown](https://github.com/sirthias/pegdown)) to the result.
{{!changing template format so that I can print it out in an example here}
{{=<% %>=}}
```clojure
([linkName linkUrl]
  (str \"[{{linkName}}]({{linkUrl}})\")
  [\"markdown\"])
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
----------------------
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
------------
Site Smith uses [Maven](https://maven.apache.org/) to build. Run maven in the root project directory.



  ")
[markdown]