package org.jamesgames.sitesmith.project

import org.jamesgames.sitesmith.htmlfunctions.HtmlFunctionArgument
import org.jamesgames.sitesmith.parsers.HtmlFunctionParser
import org.jamesgames.sitesmith.parsers.HtmlScriptParser
import org.jamesgames.sitesmith.resources.Page
import org.jamesgames.sitesmith.resources.Resource
import java.io.File
import java.nio.file.Files
import java.nio.file.Paths

/**
 * @author James Murphy
 */
class Project(val projectDirectory: File) {
    private val htmlFunctionDirectoryName = "html-functions"
    private val htmlScriptDirectoryName = "scripts"
    private val resourceDirectoryName = "resources"
    private val outputDirectoryName = "output"
    private val htmlFunctionSourceExtension = ".hf"
    private val htmlScriptSourceExtension = ".hs"
    val htmlFunctionDirectory: File
    val htmlScriptDirectory: File
    val resourceDirectory: File
    val outputDirectory: File
    private val htmlFunctionMap: HtmlFunctionMap = HtmlFunctionMap()
    private val htmlScriptMap: HtmlScriptMap = HtmlScriptMap()
    private val resourceMap: ResourceMap = ResourceMap()

    init {
        if (!projectDirectory.exists()) throw IllegalArgumentException("The project directory specified does not exist")
        if (!projectDirectory.isDirectory) throw IllegalArgumentException("The project directory specified is not a directory")
        htmlFunctionDirectory = Paths.get(projectDirectory.toURI()).resolve(htmlFunctionDirectoryName).toFile()
        if (!htmlFunctionDirectory.exists()) htmlFunctionDirectory.mkdir()
        htmlScriptDirectory = Paths.get(projectDirectory.toURI()).resolve(htmlScriptDirectoryName).toFile()
        if (!htmlScriptDirectory.exists()) htmlScriptDirectory.mkdir()
        resourceDirectory = Paths.get(projectDirectory.toURI()).resolve(resourceDirectoryName).toFile()
        if (!resourceDirectory.exists()) resourceDirectory.mkdir()
        outputDirectory = Paths.get(projectDirectory.toURI()).resolve(outputDirectoryName).toFile()
        if (!outputDirectory.exists()) outputDirectory.mkdir()

        clearOutputDirectory()
        clearAndRefillFunctionMap()
        clearAndRefillScriptMap()
    }

    private fun clearOutputDirectory() {
        Files.walk(Paths.get(outputDirectory.toURI()))
                .forEach { Files.deleteIfExists(it) }
    }

    private fun clearAndRefillFunctionMap() {
        htmlScriptMap.clearMap()
        Files.walk(Paths.get(htmlFunctionDirectory.toURI()))
                .filter { it.endsWith(htmlFunctionSourceExtension) }
                .map { HtmlFunctionParser(it.toFile()) }
                .map { it.getHtmlFunction() }
                .forEach { htmlFunctionMap.addHtmlFunction(it) }
    }

    private fun clearAndRefillScriptMap() {
        Files.walk(Paths.get(htmlScriptDirectory.toURI()))
                .filter { it.endsWith(htmlScriptSourceExtension) }
                .map { HtmlScriptParser(it.toFile()) }
                .map { it.getHtmlScript() }
                .forEach { htmlScriptMap.addHtmlScript(it) }
    }

    fun recordResource(resource: Resource) = resourceMap.addResource(resource.getName(), resource)
    fun getRelativeResourcePath(name: String, relativeTo: Page): String = resourceMap.getRelativeResourcePath(name, relativeTo)


    fun callFunction(name: String, page: Page, arguments: List<HtmlFunctionArgument>, project: Project): String {
        return htmlFunctionMap.getHtmlFunction(name).callFunction(page, arguments, project).toString()
    }
}