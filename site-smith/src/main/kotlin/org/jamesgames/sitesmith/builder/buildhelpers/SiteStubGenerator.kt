package org.jamesgames.sitesmith.builder.buildhelpers

import org.jamesgames.sitesmith.builder.SiteComponentDatabase
import org.jamesgames.sitesmith.builder.SiteLayout
import org.jamesgames.sitesmith.resources.Page
import org.jamesgames.sitesmith.resources.OptionalPageAttributes
import org.jamesgames.sitesmith.resources.SiteFile
import java.io.File
import java.nio.file.Files
import java.nio.file.Paths
import java.util.*

/**
 * @author James Murphy
 */
internal class SiteStubGenerator(private val siteLayout: SiteLayout, private val componentDb: SiteComponentDatabase,
                                 private val outputDirectory: File, private val resourceDirectory: File) : BuildHelper {

    override fun applyBuildAction() {
        createStubsAndDirectories(siteLayout.root, "")
        if (siteLayout.specifyResourcesByDirectory)
            copyResourcesDirectoryAsIsFromResourceDirectory()
    }

    private fun createStubsAndDirectories(directory: SiteLayout.DirectoryInfo, directoryPathSoFar: String) {
        createStubPages(directory.pages, directoryPathSoFar)
        if (!siteLayout.specifyResourcesByDirectory)
            copyResources(directory.resources, directoryPathSoFar)
        directory.directories?.forEach {
            val directoryToMakeIfNeeded = Paths.get(outputDirectory.absolutePath, directoryPathSoFar, it.name)
            if (!directoryToMakeIfNeeded.toFile().exists())
                Files.createDirectory(directoryToMakeIfNeeded)
            createStubsAndDirectories(it, directoryPathSoFar + it.name + File.separator)
        }
    }

    private fun createStubPages(pages: List<SiteLayout.PageInfo>?, directoryPathSoFar: String) {
        pages?.forEach {
            val path = Files.createFile(Paths.get(outputDirectory.absolutePath, directoryPathSoFar, it.fileName))
            val optionalPageAttributes = OptionalPageAttributes(siteLayout.favicon, it.clientScripts, it.additionalCssFiles)
            componentDb.recordResource(Page(path.toFile(), it.uniqueName ?: it.fileName, it.pageTitle,
                    it.textScriptsForPage ?: ArrayList(), optionalPageAttributes))
        }
    }

    private fun copyResources(resources: List<SiteLayout.ResourceInfo>?, directoryPathSoFar: String) {
        resources?.forEach {
            val fileNameInResourceDir: String = it.fileNameInResourceDir ?: it.fileName
            val fileToCopyFrom = Files.walk(Paths.get(resourceDirectory.toURI()))
                    .map { it.toFile() }
                    .filter { it != resourceDirectory }
                    .filter { it.name.equals(fileNameInResourceDir) }
                    .findFirst()
                    .map { it.toPath() }
                    .get()
            val fileToCopyTo = Paths.get(outputDirectory.toURI()).resolve(directoryPathSoFar).resolve(it.fileName)
            Files.copy(fileToCopyFrom, fileToCopyTo)
            componentDb.recordResource(SiteFile(fileToCopyTo.toFile(), it.uniqueName ?: it.fileName))
        }
    }

    private fun copyResourcesDirectoryAsIsFromResourceDirectory() {
        Files.walk(Paths.get(resourceDirectory.toURI()))
                .map { it.toFile() }
                .filter { it != resourceDirectory }
                .forEach {
                    val relativeResourcePath = resourceDirectory.toPath().relativize(it.toPath())
                    val outputPath = outputDirectory.toPath().resolve(relativeResourcePath)
                    val outputPathFile = outputPath.toFile()
                    if (!outputPathFile.exists())
                        Files.copy(it.toPath(), outputPath)
                    if (outputPathFile.isFile)
                        componentDb.recordResource(SiteFile(outputPathFile, outputPathFile.name))
                }
    }


}