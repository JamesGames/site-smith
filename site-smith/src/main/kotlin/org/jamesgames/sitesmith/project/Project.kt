package org.jamesgames.sitesmith.project

import org.jamesgames.sitesmith.htmlfunctions.HtmlFunction
import org.jamesgames.sitesmith.parsers.HtmlFunctionParser
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
    private val htmlFunctionSourceExtension = ".hf"
    private val htmlScriptSourceExtension = ".hs"
    val htmlFunctionDirectory: File
    val htmlScriptDirectory: File
    private val htmlFunctionMap: HtmlFunctionMap = HtmlFunctionMap()
    private val resourceMap: ResourceMap = ResourceMap()

    init {
        if (!projectDirectory.exists()) throw IllegalArgumentException("The project directory specified does not exist")
        if (!projectDirectory.isDirectory) throw IllegalArgumentException("The project directory specified is not a directory")
        htmlFunctionDirectory = Paths.get(projectDirectory.toURI()).resolve(htmlFunctionDirectoryName).toFile();
        if (!htmlFunctionDirectory.exists()) htmlFunctionDirectory.mkdir()
        htmlScriptDirectory = Paths.get(projectDirectory.toURI()).resolve(htmlScriptDirectoryName).toFile();
        if (!htmlScriptDirectory.exists()) htmlScriptDirectory.mkdir()

        clearAndRefillFunctionMap()
    }

    private fun clearAndRefillFunctionMap() {
        htmlFunctionMap.clearMap()
        Files.walk(Paths.get(htmlFunctionDirectory.toURI()))
                .filter { it.endsWith(htmlFunctionSourceExtension) }
                .map { HtmlFunctionParser(it.toFile()) }
                .map { it.getHtmlFunction() }
                .forEach { htmlFunctionMap.addHtmlFunction(it) }
    }

    fun addResource(name: String, resource: Resource) = resourceMap.addResource(name, resource)
    fun getRelativeResourcePath(name: String, relativeTo: Page): String = resourceMap.getRelativeResourcePath(name, relativeTo)
    fun addHtmlFunction(name: String, function: HtmlFunction) = htmlFunctionMap.addHtmlFunction(function)
    fun getHtmlFunction(name: String): HtmlFunction = htmlFunctionMap.getHtmlFunction(name)
}