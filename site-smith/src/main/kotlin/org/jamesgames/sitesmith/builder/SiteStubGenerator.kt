package org.jamesgames.sitesmith.builder

import java.io.File
import java.nio.file.Files
import java.nio.file.Paths
import kotlin.collections.forEach

/**
 * @author James Murphy
 */
class SiteStubGenerator(private val siteLayout: SiteLayout, private val outputDirectory: File, private val resourceDirectory: File) {
    fun generateSiteStub() {
        createStubsAndDirectories(siteLayout.root, File.separator)
    }

    private fun createStubsAndDirectories(directory: SiteLayout.DirectoryInfo, directoryPathSoFar: String) {
        createStubPages(directory.pages)
        directory.directories.forEach {
            Files.createDirectory(Paths.get(outputDirectory.toURI())).resolve(Paths.get(it.name))
            createStubPages(it.pages)
            copyResources(it.pages, directoryPathSoFar + File.separator)
            createStubsAndDirectories(it, directoryPathSoFar + File.separator)
        }
    }

    private fun createStubPages(pages: List<SiteLayout.PageInfo>) {
        pages.forEach {
            Files.createFile(Paths.get(outputDirectory.toURI())).resolve(Paths.get(it.fileName))
        }
    }

    private fun copyResources(pages: List<SiteLayout.PageInfo>, directoryPathSoFar: String) {
        pages.forEach {
            val uniqueResourceNameAlsoTheResourceFileNameFromResourceDir = it.uniqueName
            val fileToCopyFrom = Files.walk(Paths.get(resourceDirectory.toURI()))
                    .filter { it.fileName.equals(uniqueResourceNameAlsoTheResourceFileNameFromResourceDir) }
                    .findFirst().get()
            val fileToCopyTo = Paths.get(outputDirectory.toURI()).resolve(directoryPathSoFar).resolve(it.fileName)
            Files.copy(fileToCopyFrom, fileToCopyTo)
        }
    }
}