package org.jamesgames.sitesmith.builder

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import org.jamesgames.sitesmith.builder.buildhelpers.BuildHelper
import org.jamesgames.sitesmith.builder.buildhelpers.ResourceDirectoryValidator
import org.jamesgames.sitesmith.builder.buildhelpers.SiteLayoutValidator
import org.jamesgames.sitesmith.builder.buildhelpers.SiteStubGenerator
import java.io.File
import java.nio.file.Files
import java.nio.file.Paths
import java.util.*

/**
 * @author James Murphy
 */
class SiteBuilder(private val siteLayoutFile: File,
                  private val textFunctionDirectory: File,
                  private val textScriptDirectory: File,
                  private val resourceDirectory: File,
                  private val outputDirectory: File) {

    private val successString: String = "Project generated successfully in: " +
            System.lineSeparator() + outputDirectory.absoluteFile
    private val failureString: String = "Project generation failed"
    private val buildNotAttempted: String = "Build not attempted yet"

    var results: String = buildNotAttempted
        private set


    fun buildSite(): Boolean {
        clearOutputDirectory()
        val siteLayout = readSiteLayout()
        val componentDatabase = SiteComponentDatabase(
                textFunctionDirectory,
                textScriptDirectory,
                siteLayout.globalCssFileNames ?: arrayListOf())

        val buildHelpers = executedBuildHelpers(componentDatabase, siteLayout)
        val potentialFailedBuildHelper = potentialFailedBuildHelper(buildHelpers)
        if (potentialFailedBuildHelper != null) {
            recordFailResults(potentialFailedBuildHelper)
            return false;
        }

        componentDatabase.writePages()

        recordSuccessResults(buildHelpers)
        return true;
    }

    private fun executedBuildHelpers(componentDatabase: SiteComponentDatabase, siteLayout: SiteLayout): MutableList<BuildHelper> {
        val buildHelpers: MutableList<BuildHelper> = arrayListOf(
                ResourceDirectoryValidator(resourceDirectory, HashSet(componentDatabase.globalCssFileNames)),
                SiteLayoutValidator(siteLayout),
                SiteStubGenerator(siteLayout, componentDatabase, outputDirectory, resourceDirectory))
        buildHelpers.forEach {
            it.applyBuildAction()
        }
        return buildHelpers
    }

    private fun potentialFailedBuildHelper(buildHelpers: MutableList<BuildHelper>) =
            buildHelpers.firstOrNull { !it.buildHelperPassed() }

    private fun recordFailResults(failedBuildHelper: BuildHelper) {
        results = arrayOf(failedBuildHelper.getErrorMessages(),
                failedBuildHelper.getWarningMessages(),
                failureString).joinToString (System.lineSeparator())
    }

    private fun recordSuccessResults(buildHelpers: MutableList<BuildHelper>) {
        results = joinBuilderHelperWarnings(buildHelpers) + successString
    }

    private fun joinBuilderHelperWarnings(buildHelpers: List<BuildHelper>): String =
            buildHelpers.map { it.getErrorMessages() }
                    .filter { it.length > 0 }
                    .joinToString(System.lineSeparator())
                    .let { if (it.length == 0) "" else it + System.lineSeparator() }

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

    private fun readSiteLayout(): SiteLayout {
        return try {
            jacksonObjectMapper().readValue(siteLayoutFile.readText())
        } catch (e: Exception) {
            throw InvalidSiteLayoutException(siteLayoutFile, e.toString())
        }
    }
}