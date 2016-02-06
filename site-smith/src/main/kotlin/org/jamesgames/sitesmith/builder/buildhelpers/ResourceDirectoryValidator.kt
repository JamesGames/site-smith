package org.jamesgames.sitesmith.builder.buildhelpers

import java.io.File
import java.nio.file.Files
import java.util.*


/**
 * @author James Murphy
 */
internal class ResourceDirectoryValidator(private val resourceDirectory: File,
                                          private val cssStyleFileName: String) : BuildHelper {

    private val resourceFilesWithDuplicateUniqueFileNames: MutableList<File> = ArrayList()
    private var cssStyleFileFound: Boolean = false;

    override fun applyBuildAction() {
        resourceFilesWithDuplicateUniqueFileNames.clear()
        val uniqueFileNames: MutableSet<String> = HashSet()
        Files.walk(resourceDirectory.toPath()).map { it.toFile() }.forEach {
            if (!uniqueFileNames.add(it.name))
                resourceFilesWithDuplicateUniqueFileNames.add(it)
            if (!cssStyleFileFound)
                cssStyleFileFound = it.name.equals(cssStyleFileName)
        }
    }

    override fun getErrorMessages(): String =
            resourceFilesWithDuplicateUniqueFileNames.map {
                "Resource file with duplicate unique name in resource directory: ${it.absolutePath}"
            }.joinToString { System.lineSeparator() }

    override fun getWarningMessages(): String {
        return if (!cssStyleFileFound) "No site wide css style file found under the name: $cssStyleFileName" else ""
    }

}