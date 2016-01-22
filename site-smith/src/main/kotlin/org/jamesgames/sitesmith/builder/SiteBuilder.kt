package org.jamesgames.sitesmith.builder

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
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

    private val successString: String = "Project generated successfully in: " +
            System.lineSeparator() + outputDirectory.absoluteFile
    private val componentDatabase: SiteComponentDatabase =
            SiteComponentDatabase(htmlFunctionDirectory, htmlScriptDirectory)


    fun buildSite(): String {
        validateResourceDirectory()
        clearOutputDirectory()
        componentDatabase.populateDatabase()
        val siteLayout = readSiteLayout()
        val siteValidator = SiteLayoutValidator(siteLayout)
        if (!siteValidator.validateSiteLayout())
            return siteValidator.getErrorMessages()
        generateSiteStubAndRecordResources(siteLayout)
        fillPages()
        return successString
    }

    private fun validateResourceDirectory() {
        // TODO
    }

    private fun clearOutputDirectory() =
            Files.walk(Paths.get(outputDirectory.toURI())).forEach { Files.deleteIfExists(it) }


    private fun readSiteLayout(): SiteLayout =
            jacksonObjectMapper().readValue(siteLayoutFile.readText())


    private fun generateSiteStubAndRecordResources(siteLayout: SiteLayout) =
            SiteStubGenerator(siteLayout, componentDatabase, outputDirectory, resourceDirectory).generateSiteStub()

    private fun fillPages() = componentDatabase.writePages()

}




