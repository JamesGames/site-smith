package org.jamesgames.sitesmith.builder

import org.jamesgames.sitesmith.builder.parsers.HtmlFunctionParser
import org.jamesgames.sitesmith.builder.parsers.HtmlScriptParser
import org.jamesgames.sitesmith.resources.Page
import org.jamesgames.sitesmith.resources.Resource
import org.jamesgames.sitesmith.sitecomponents.HtmlFunctionArgument
import java.io.File
import java.nio.file.Files
import java.nio.file.Paths
import kotlin.collections.forEach

/**
 * @author James Murphy
 */
class SiteComponentDatabase(private val htmlFunctionDirectory: File,
                            private val htmlScriptDirectory: File) {

    private val htmlFunctionSourceExtension = ".hf"
    private val htmlScriptSourceExtension = ".hs"
    private val htmlFunctionMap: HtmlFunctionMap = HtmlFunctionMap()
    private val htmlScriptMap: HtmlScriptMap = HtmlScriptMap()
    private val resourceMap: ResourceMap = ResourceMap()

    fun populateDatabase() {
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

    fun callFunction(name: String, page: Page, arguments: List<HtmlFunctionArgument>, stringBuilder: StringBuilder) =
            htmlFunctionMap.callFunction(name, page, arguments, this, stringBuilder)

    fun appendHtmlFromScript(scriptName: String, page: Page, stringBuilder: StringBuilder) {
        htmlScriptMap.executeHtmlScript(scriptName, page, this, stringBuilder)
    }

    private fun fillFunctionMap() {
        htmlScriptMap.clearMap()
        Files.walk(Paths.get(htmlFunctionDirectory.toURI()))
                .filter { it.endsWith(htmlFunctionSourceExtension) }
                .map { HtmlFunctionParser(it.toFile()) }
                .map { it.getHtmlFunction() }
                .forEach { htmlFunctionMap.addHtmlFunction(it) }
    }

    private fun fillScriptMap() =
            Files.walk(Paths.get(htmlScriptDirectory.toURI()))
                    .filter { it.endsWith(htmlScriptSourceExtension) }
                    .map { HtmlScriptParser(it.toFile()) }
                    .map { it.getHtmlScript() }
                    .forEach { htmlScriptMap.addHtmlScript(it) }
}