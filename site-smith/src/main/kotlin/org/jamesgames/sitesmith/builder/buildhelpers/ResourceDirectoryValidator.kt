package org.jamesgames.sitesmith.builder.buildhelpers

import java.io.File
import java.nio.file.Files
import java.util.*


/**
 * @author James Murphy
 */
internal class ResourceDirectoryValidator(private val resourceDirectory: File,
                                          private val cssStyleFileNames: Set<String>) : BuildHelper {

    private val resourceFilesWithDuplicateUniqueFileNames: MutableList<File> = ArrayList()
    private val cssStyleFileNamesNotFound: HashSet<String> = HashSet()

    override fun applyBuildAction() {
        cssStyleFileNamesNotFound.clear()
        cssStyleFileNamesNotFound.addAll(cssStyleFileNames)
        resourceFilesWithDuplicateUniqueFileNames.clear()

        val uniqueFileNames: MutableSet<String> = HashSet()
        Files.walk(resourceDirectory.toPath())
                .map { it.toFile() }
                .filter { it.isFile }
                .forEach {
                    if (!uniqueFileNames.add(it.name))
                        resourceFilesWithDuplicateUniqueFileNames.add(it)
                    cssStyleFileNamesNotFound.remove(it.name)
                }
    }

    override fun getErrorMessages(): String {
        val errors: List<List<String>> = arrayListOf(
                resourceFilesWithDuplicateUniqueFileNames.map {
                    "Resource file with duplicate unique name in resource directory: ${it.absolutePath}${System.lineSeparator()}"
                },
                cssStyleFileNamesNotFound.map {
                    "Css file not found: $it${System.lineSeparator()}"
                }
        )
        val flattened = errors.flatten()
        return errors.flatten().joinToString(System.lineSeparator())
    }

}