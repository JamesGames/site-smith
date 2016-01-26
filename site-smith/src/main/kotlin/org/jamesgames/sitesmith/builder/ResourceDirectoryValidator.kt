package org.jamesgames.sitesmith.builder

import java.io.File
import java.nio.file.Files
import java.util.*
import kotlin.collections.joinToString
import kotlin.collections.map


/**
 * @author James Murphy
 */
class ResourceDirectoryValidator(private val resourceDirectory: File, private val cssStyleFileName: String) {
    private val resourceFilesWithDuplicateUniqueFileNames: MutableList<File> = ArrayList()
    private var cssStyleFileFound: Boolean = false;

    fun validateDirectory(): Boolean {
        resourceFilesWithDuplicateUniqueFileNames.clear()
        val uniqueFileNames: MutableSet<String> = HashSet()
        Files.walk(resourceDirectory.toPath()).map { it.toFile() }.forEach {
            if (!uniqueFileNames.add(it.name))
                resourceFilesWithDuplicateUniqueFileNames.add(it)
            if (!cssStyleFileFound)
                cssStyleFileFound = it.name.equals(cssStyleFileName)
        }
        return resourceFilesWithDuplicateUniqueFileNames.isEmpty()
    }

    fun getErrorMessages(): String =
            resourceFilesWithDuplicateUniqueFileNames.map {
                "Resource file with duplicate unique name in resource directory: ${it.absolutePath}"
            }.joinToString { System.lineSeparator() }

    fun getWarningMessages(): String {
        return if (!cssStyleFileFound) "No site wide css style file found under the name: $cssStyleFileName" else ""
    }

}