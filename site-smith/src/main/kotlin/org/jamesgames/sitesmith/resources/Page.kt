package org.jamesgames.sitesmith.resources

import org.jamesgames.sitesmith.builder.SiteComponentDatabase
import java.io.File
import java.nio.file.Path
import kotlin.collections.map
import kotlin.text.appendln

/**
 * @author James Murphy
 */
class Page(private val file: File,
           private val uniqueName: String,
           private val pageTitle: String,
           private val htmlScriptNames: List<String>) : Resource {

    companion object constants {
        val siteWideCssFileName = "global-style.css"
    }

    override fun getPath(): Path = file.toPath()
    override fun getUniqueName(): String = uniqueName

    fun writePage(componentDb: SiteComponentDatabase) {
        val pageData = StringBuilder()
        writePageData(componentDb, pageData)
        file.writer().write(pageData.toString())
    }

    private fun writePageData(componentDb: SiteComponentDatabase, pageData: StringBuilder) {
        writeHtmlPageOpen(pageData)
        writeHtmlHeadTag(pageData, componentDb)
        writePageBody(pageData, componentDb)
        writeHtmlPageClose(pageData)
    }

    private fun writeHtmlPageOpen(pageData: StringBuilder) {
        pageData.appendln("<!DOCTYPE html>").appendln("<html>")
    }

    private fun writeHtmlHeadTag(pageData: StringBuilder, componentDb: SiteComponentDatabase) {

        pageData.appendln("  <head>")
        pageData.appendln("    <title>$pageTitle</title>")
        if (componentDb.doesResourceExist(siteWideCssFileName)) {
            val pathToCssFile = componentDb.getRelativeResourcePath(siteWideCssFileName, this)
            pageData.appendln("    <link rel=\"stylesheet\" href=\"$pathToCssFile\">")
        }
        pageData.appendln("  </head>")
    }

    private fun writePageBody(pageData: StringBuilder, componentDb: SiteComponentDatabase) {
        pageData.appendln("  <body>")
        htmlScriptNames.map { componentDb.appendHtmlFromScript(uniqueName, this, pageData) }
        pageData.appendln("  </body>")
    }

    private fun writeHtmlPageClose(pageData: StringBuilder) {
        pageData.appendln("</body>")
    }

}