package org.jamesgames.sitesmith.project

import org.jamesgames.sitesmith.builder.SiteBuilder
import java.io.File
import java.nio.file.Paths

/**
 * @author James Murphy
 */
class Project(val projectDirectory: File, val siteLayout: File) {
    companion object {
        const private val textFunctionDirectoryName = "functions"
        const private val textScriptDirectoryName = "scripts"
        const private val resourceDirectoryName = "resources"
        const private val outputDirectoryName = "output"
    }

    val textFunctionDirectory: File
    val textScriptDirectory: File
    val resourceDirectory: File
    val outputDirectory: File
    val siteBuilder: SiteBuilder
    val results: String
        get() = siteBuilder.results

    init {
        if (!siteLayout.exists()) throw IllegalArgumentException("The site layout file, ${siteLayout.absolutePath}, does not exist")
        if (!siteLayout.isFile) throw IllegalArgumentException("The site layout file, ${siteLayout.absolutePath}, is not a file")
        if (!projectDirectory.exists()) throw IllegalArgumentException("The project directory specified, ${projectDirectory.absolutePath}, does not exist")
        if (!projectDirectory.isDirectory) throw IllegalArgumentException("The project directory specified, ${projectDirectory.absolutePath}, is not a directory")
        textFunctionDirectory = Paths.get(projectDirectory.toURI()).resolve(textFunctionDirectoryName).toFile()
        if (!textFunctionDirectory.exists()) textFunctionDirectory.mkdir()
        textScriptDirectory = Paths.get(projectDirectory.toURI()).resolve(textScriptDirectoryName).toFile()
        if (!textScriptDirectory.exists()) textScriptDirectory.mkdir()
        resourceDirectory = Paths.get(projectDirectory.toURI()).resolve(resourceDirectoryName).toFile()
        if (!resourceDirectory.exists()) resourceDirectory.mkdir()
        outputDirectory = Paths.get(projectDirectory.toURI()).resolve(outputDirectoryName).toFile()
        if (!outputDirectory.exists()) outputDirectory.mkdir()
        siteBuilder = SiteBuilder(siteLayout, textFunctionDirectory,
                textScriptDirectory, resourceDirectory, outputDirectory)
    }

    fun buildSite(): Boolean = siteBuilder.buildSite()


}