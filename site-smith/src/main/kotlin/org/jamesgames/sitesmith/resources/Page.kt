package org.jamesgames.sitesmith.resources

import org.jamesgames.sitesmith.builder.SiteComponentDatabase
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStreamWriter
import java.nio.charset.StandardCharsets
import java.nio.file.Path

/**
 * @author James Murphy
 */
class Page(private val file: File,
           private val uniqueName: String,
           private val pageTitle: String,
           private val additionalCssFiles: List<String>,
           private val textScriptNames: List<String>,
           private val extraAttributes: Map<String, String>) : Resource {

    companion object {
        val faviconKey = "favicon"

        private val docType = "<!DOCTYPE html>"
        private val htmlOpen = "<html>"
        private val headOpen = "<head><meta charset=\"UTF-8\">"
        private val titleOpen = "<title>"
        private val titleClose = "</title>"
        private val cssLinkStart = "<link rel=\"stylesheet\" href=\""
        private val faviconStart = "<link rel=\"icon\" type=\"image/png\" href=\""
        private val tagClose = "\">"
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
        OutputStreamWriter(FileOutputStream(file), StandardCharsets.UTF_8).use { it.write(pageData.toString()); it.close() }
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
        pageData.append(arrayListOf(componentDb.globalCssFileNames, additionalCssFiles).flatten()
                .filter { componentDb.doesResourceExist(it) }
                .map {
                    if (it.startsWith(Resource.startOfExternalFile))
                        it.substring(Resource.startOfExternalFile.length)
                    else
                        cssLinkStart + componentDb.getRelativeResourcePath(it, this) + tagClose + System.lineSeparator()
                }.joinToString(""))
        if (extraAttributes.containsKey(faviconKey)) {
            pageData.appendln(faviconStart + componentDb.getRelativeResourcePath(extraAttributes[faviconKey]!!, this) + tagClose + System.lineSeparator())
        }
        pageData.appendln(headClose)
    }

    private fun writePageBody(pageData: StringBuilder, componentDb: SiteComponentDatabase) {
        pageData.appendln(bodyOpen)
        textScriptNames.forEach { componentDb.appendTextFromScript(it, this, pageData) }
        pageData.appendln(bodyClose)
    }

    private fun writeHtmlPageClose(pageData: StringBuilder) {
        pageData.appendln(htmlClose)
    }

}