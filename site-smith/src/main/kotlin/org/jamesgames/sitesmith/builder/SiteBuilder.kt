package org.jamesgames.sitesmith.builder

import org.jamesgames.sitesmith.parsers.HtmlFunctionParser
import org.jamesgames.sitesmith.parsers.HtmlScriptParser
import org.jamesgames.sitesmith.resources.Page
import org.jamesgames.sitesmith.resources.Resource
import org.jamesgames.sitesmith.sitecomponents.HtmlFunctionArgument
import org.jamesgames.sitesmith.sitecomponents.SiteLayout
import java.io.File
import java.nio.file.Files
import java.nio.file.Paths

/**
 * @author James Murphy
 */
class SiteBuilder(private val siteLayoutFile: File,
                  private val htmlFunctionDirectory: File,
                  private val htmlScriptDirectory: File,
                  private val resourceDirectory: File,
                  private val outputDirectory: File) {

    private val htmlFunctionSourceExtension = ".hf"
    private val htmlScriptSourceExtension = ".hs"
    private val htmlFunctionMap: HtmlFunctionMap = HtmlFunctionMap()
    private val htmlScriptMap: HtmlScriptMap = HtmlScriptMap()
    private val resourceMap: ResourceMap = ResourceMap()

    fun recordResource(resource: Resource) =
            resourceMap.addResource(resource.getName(), resource)

    fun getRelativeResourcePath(name: String, relativeTo: Page): String =
            resourceMap.getRelativeResourcePath(name, relativeTo)

    fun callFunction(name: String, page: Page, arguments: List<HtmlFunctionArgument>, siteBuilder: SiteBuilder): String =
            htmlFunctionMap.getHtmlFunction(name).callFunction(page, arguments, siteBuilder).toString()


    fun buildSite() {
        clearOutputDirectory()
        fillFunctionMap()
        fillScriptMap()
        val siteLayout = readSiteLayout()
        if (validateSiteLayout(siteLayout)) {
            generateDirectoryAndPageStubs(siteLayout)
            moveSiteResources(siteLayout)
            fillPages(siteLayout)
        }
    }

    private fun clearOutputDirectory() {
        Files.walk(Paths.get(outputDirectory.toURI()))
                .forEach { Files.deleteIfExists(it) }
    }

    private fun fillFunctionMap() {
        htmlScriptMap.clearMap()
        Files.walk(Paths.get(htmlFunctionDirectory.toURI()))
                .filter { it.endsWith(htmlFunctionSourceExtension) }
                .map { HtmlFunctionParser(it.toFile()) }
                .map { it.getHtmlFunction() }
                .forEach { htmlFunctionMap.addHtmlFunction(it) }
    }

    private fun fillScriptMap() {
        Files.walk(Paths.get(htmlScriptDirectory.toURI()))
                .filter { it.endsWith(htmlScriptSourceExtension) }
                .map { HtmlScriptParser(it.toFile()) }
                .map { it.getHtmlScript() }
                .forEach { htmlScriptMap.addHtmlScript(it) }
    }

    private fun readSiteLayout(): SiteLayout {
        // TODO
        return SiteLayout()
    }

    private fun validateSiteLayout(siteLayout: SiteLayout): Boolean {
        // TODO
        return false
    }


    private fun generateDirectoryAndPageStubs(siteLayout: SiteLayout) {
        // TODO
    }

    private fun moveSiteResources(siteLayout: SiteLayout) {
        // TODO
    }

    private fun fillPages(siteLayout: SiteLayout) {
        // TODO
    }
}