package org.jamesgames.sitesmith.resources

import org.jamesgames.sitesmith.builder.SiteComponentDatabase
import java.io.File
import java.nio.file.Path

/**
 * @author James Murphy
 */
class Page(private val file: File,
           private val uniqueName: String,
           private val pageTitle: String,
           private val additionalCssFiles: List<String>,
           private val htmlScriptNames: List<String>) : Resource {

    companion object {
        private val docType = "<!DOCTYPE html>"
        private val htmlOpen = "<html>"
        private val headOpen = "<head>"
        private val titleOpen = "<title>"
        private val titleClose = "</title>"
        private val cssLinkStart = "<link rel=\"stylesheet\" href=\""
        private val cssLinkEnd = "\">"
        private val headClose = "</head>"
        private val bodyOpen = "<body>"
        private val bodyClose = "</body>"
        private val htmlClose = "</html>"
    }

    override fun getPath(): Path = file.toPath()
    override fun getUniqueName(): String = uniqueName

    fun writePage(componentDb: SiteComponentDatabase) {
        val pageData = StringBuilder()
        writePageData(componentDb, pageData)
        file.writer().use { it.write(pageData.toString()) }
    }

    private fun writePageData(componentDb: SiteComponentDatabase, pageData: StringBuilder) {
        writeHtmlPageOpen(pageData)
        writeHtmlHeadTag(pageData, componentDb)
        writePageBody(pageData, componentDb)
        writeHtmlPageClose(pageData)
    }

    private fun writeHtmlPageOpen(pageData: StringBuilder) {
        pageData.appendln(docType).appendln(htmlOpen)
    }

    private fun writeHtmlHeadTag(pageData: StringBuilder, componentDb: SiteComponentDatabase) {

        pageData.appendln(headOpen)
        pageData.appendln("$titleOpen$pageTitle$titleClose")
        if (componentDb.doesResourceExist(componentDb.globalCssFileName)) {
            val pathToCssFile = componentDb.getRelativeResourcePath(componentDb.globalCssFileName, this)
            pageData.appendln("$cssLinkStart$pathToCssFile$cssLinkEnd")
        }
        additionalCssFiles.map { "$cssLinkStart$it$cssLinkEnd" }.forEach { pageData.appendln(it) }
        pageData.appendln(headClose)
    }

    private fun writePageBody(pageData: StringBuilder, componentDb: SiteComponentDatabase) {
        pageData.appendln(bodyOpen)
        htmlScriptNames.map { componentDb.appendHtmlFromScript(uniqueName, this, pageData) }.forEach { pageData.appendln(it) }
        pageData.appendln(bodyClose)
    }

    private fun writeHtmlPageClose(pageData: StringBuilder) {
        pageData.appendln(htmlClose)
    }

}