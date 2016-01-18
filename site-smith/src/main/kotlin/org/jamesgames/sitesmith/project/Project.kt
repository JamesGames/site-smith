package org.jamesgames.sitesmith.project

import org.jamesgames.sitesmith.builder.SiteBuilder
import java.io.File
import java.nio.file.Paths

/**
 * @author James Murphy
 */
class Project(val projectDirectory: File, val siteLayout: File) {
    private val htmlFunctionDirectoryName = "html-functions"
    private val htmlScriptDirectoryName = "scripts"
    private val resourceDirectoryName = "resources"
    private val outputDirectoryName = "output"

    val htmlFunctionDirectory: File
    val htmlScriptDirectory: File
    val resourceDirectory: File
    val outputDirectory: File

    init {
        if (!siteLayout.exists()) throw IllegalArgumentException("The site layout file does not exist")
        if (!siteLayout.isFile) throw IllegalArgumentException("The site layout file is not a file")
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
    }

    fun buildSite(): String = SiteBuilder(siteLayout, htmlFunctionDirectory,
            htmlScriptDirectory, resourceDirectory, outputDirectory).buildSite()


}