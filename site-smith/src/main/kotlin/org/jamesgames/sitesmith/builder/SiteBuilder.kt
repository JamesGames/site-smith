package org.jamesgames.sitesmith.builder

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import org.jamesgames.sitesmith.builder.buildersteps.BuildHelper
import org.jamesgames.sitesmith.builder.buildersteps.ResourceDirectoryValidator
import org.jamesgames.sitesmith.builder.buildersteps.SiteLayoutValidator
import org.jamesgames.sitesmith.builder.buildersteps.SiteStubGenerator
import org.jamesgames.sitesmith.resources.Page
import java.io.File
import java.nio.file.Files
import java.nio.file.Paths
import java.util.*
import kotlin.collections.firstOrNull
import kotlin.collections.forEach
import kotlin.collections.joinToString
import kotlin.collections.map

/**
 * @author James Murphy
 */
class SiteBuilder(private val siteLayoutFile: File,
                  private val htmlFunctionDirectory: File,
                  private val htmlScriptDirectory: File,
                  private val resourceDirectory: File,
                  private val outputDirectory: File) {

    private val successString: String = "Project generated successfully in: " +
            System.lineSeparator() + outputDirectory.absoluteFile
    private val failureString: String = "Project generation failed"
    private val componentDatabase: SiteComponentDatabase =
            SiteComponentDatabase(htmlFunctionDirectory, htmlScriptDirectory)


    fun buildSite(): String {
        clearOutputDirectory()
        val siteLayout = readSiteLayout()
        componentDatabase.populateDatabase()

        val buildHelpers: MutableList<BuildHelper> = ArrayList()
        buildHelpers.add(ResourceDirectoryValidator(resourceDirectory, Page.siteWideCssFileName))
        buildHelpers.add(SiteLayoutValidator(siteLayout))
        buildHelpers.add(SiteStubGenerator(siteLayout, componentDatabase, outputDirectory, resourceDirectory))
        buildHelpers.forEach {
            it.applyBuildAction()
        }
        val failedBuildHelper = buildHelpers.firstOrNull { !it.buildHelperPassed() }
        if (failedBuildHelper != null) {
            return arrayOf(failedBuildHelper.getErrorMessages(),
                    failedBuildHelper.getWarningMessages(),
                    failureString).joinToString { System.lineSeparator() }
        }

        componentDatabase.writePages()
        return joinBuilderHelperWarnings(buildHelpers) + System.lineSeparator() + successString
    }

    private fun joinBuilderHelperWarnings(buildHelpers: List<BuildHelper>): String =
            buildHelpers.map { it.getErrorMessages() }.joinToString { System.lineSeparator() }

    private fun clearOutputDirectory() {
        // Have to remove all files from directories first
        Files.walk(Paths.get(outputDirectory.toURI()))
                .filter { it.toFile() != outputDirectory }
                .filter { !it.toFile().isDirectory }
                .forEach {
                    Files.deleteIfExists(it)
                }
        // Now will be able to delete all empty directories
        // (Is there a way to delete a directory with the JCL that has files?)
        Files.walk(Paths.get(outputDirectory.toURI()))
                .filter { it.toFile() != outputDirectory }
                .forEach {
                    Files.deleteIfExists(it)
                }
    }

    private fun readSiteLayout(): SiteLayout =
            jacksonObjectMapper().readValue(siteLayoutFile.readText())
}