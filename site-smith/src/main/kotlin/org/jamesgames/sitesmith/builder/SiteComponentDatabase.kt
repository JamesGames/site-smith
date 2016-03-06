package org.jamesgames.sitesmith.builder

import org.jamesgames.sitesmith.builder.parsers.HtmlFunctionParser
import org.jamesgames.sitesmith.builder.parsers.HtmlScriptParser
import org.jamesgames.sitesmith.resources.Page
import org.jamesgames.sitesmith.resources.Resource
import org.jamesgames.sitesmith.textfunctions.TextScript
import java.io.File
import java.nio.file.Files
import java.nio.file.Paths

/**
 * @author James Murphy
 */
class SiteComponentDatabase(private val htmlFunctionDirectory: File,
                            private val htmlScriptDirectory: File,
                            val globalCssFileName: String) {

    companion object {
        const private val htmlFunctionSourceExtension = ".hf"
        const private val htmlScriptSourceExtension = ".hs"
    }

    private val htmlFunctionMap: HtmlFunctionMap = HtmlFunctionMap()
    private val htmlScriptMap: HtmlScriptMap = HtmlScriptMap()
    private val resourceMap: ResourceMap = ResourceMap()

    init {
        fillFunctionMap()
        fillScriptMap()
    }

    fun writePages() = resourceMap.getPages().forEach { it.writePage(this) }

    fun recordResource(resource: Resource) =
            resourceMap.addResource(resource.getUniqueName(), resource)

    fun getRelativeResourcePath(name: String, relativeTo: Page): String =
            resourceMap.getRelativeResourcePath(name, relativeTo)

    fun doesResourceExist(name: String): Boolean =
            resourceMap.doesResourceExist(name)

    fun appendHtmlFromScript(scriptName: String, page: Page, stringBuilder: StringBuilder) =
            stringBuilder.append (TextScript.executeScript(
                    { s: String ->
                        page.getPath().relativize(Paths.get(getRelativeResourcePath(
                                s, page))).toString()
                    },
                    htmlScriptMap.getHtmlScript(scriptName).scriptText));


    private fun fillFunctionMap() {
        htmlFunctionMap.clearMap()
        Files.walk(Paths.get(htmlFunctionDirectory.toURI()))
                .filter { it.endsWith(htmlFunctionSourceExtension) }
                .map { HtmlFunctionParser(it.toFile()) }
                .map { it.getHtmlFunction() }
                .forEach { htmlFunctionMap.addHtmlFunction(it) }
    }

    private fun fillScriptMap() {
        htmlScriptMap.clearMap()
        Files.walk(Paths.get(htmlScriptDirectory.toURI()))
                .filter { it.endsWith(htmlScriptSourceExtension) }
                .map { HtmlScriptParser(it.toFile()) }
                .map { it.getHtmlScript() }
                .forEach { htmlScriptMap.addHtmlScript(it) }
    }
}