package org.jamesgames.sitesmith.builder.buildersteps

import org.jamesgames.sitesmith.builder.SiteComponentDatabase
import org.jamesgames.sitesmith.builder.SiteLayout
import org.jamesgames.sitesmith.resources.Page
import org.jamesgames.sitesmith.resources.SiteFile
import java.io.File
import java.nio.file.Files
import java.nio.file.Paths
import kotlin.collections.forEach

/**
 * @author James Murphy
 */
internal class SiteStubGenerator(private val siteLayout: SiteLayout, private val componentDb: SiteComponentDatabase,
                                 private val outputDirectory: File, private val resourceDirectory: File) {
    fun generateSiteStub() {
        createStubsAndDirectories(siteLayout.root, File.separator)
    }

    private fun createStubsAndDirectories(directory: SiteLayout.DirectoryInfo, directoryPathSoFar: String) {
        createStubPages(directory.pages)
        directory.directories.forEach {
            Files.createDirectory(Paths.get(outputDirectory.toURI())).resolve(Paths.get(it.name))
            createStubPages(it.pages)
            copyResources(it.resources, directoryPathSoFar + File.separator)
            createStubsAndDirectories(it, directoryPathSoFar + File.separator)
        }
    }

    private fun createStubPages(pages: List<SiteLayout.PageInfo>) {
        pages.forEach {
            val path = Files.createFile(Paths.get(outputDirectory.toURI())).resolve(Paths.get(it.fileName))
            componentDb.recordResource(Page(path.toFile(), it.uniqueName, it.pageTitle,
                    it.additionalCssFiles, it.templateNamesForPage))
        }
    }

    private fun copyResources(resources: List<SiteLayout.ResourceInfo>, directoryPathSoFar: String) {
        resources.forEach {
            val uniqueResourceNameAlsoTheResourceFileNameFromResourceDir = it.uniqueName
            val fileToCopyFrom = Files.walk(Paths.get(resourceDirectory.toURI()))
                    .filter { it.fileName.equals(uniqueResourceNameAlsoTheResourceFileNameFromResourceDir) }
                    .findFirst().get()
            val fileToCopyTo = Paths.get(outputDirectory.toURI()).resolve(directoryPathSoFar).resolve(it.fileName)
            Files.copy(fileToCopyFrom, fileToCopyTo)
            componentDb.recordResource(SiteFile(fileToCopyTo.toFile(), it.uniqueName))
        }
    }
}