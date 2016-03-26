package org.jamesgames.sitesmith.builder.buildhelpers

import org.jamesgames.sitesmith.resources.Resource
import java.io.File
import java.nio.file.Files
import java.util.*


/**
 * @author James Murphy
 */
internal class ResourceDirectoryValidator(private val resourceDirectory: File) : BuildHelper {

    private val resourceFilesWithDuplicateUniqueFileNames: MutableList<File> = ArrayList()

    override fun applyBuildAction() {
        resourceFilesWithDuplicateUniqueFileNames.clear()

        val uniqueFileNames: MutableSet<String> = HashSet()
        Files.walk(resourceDirectory.toPath())
                .map { it.toFile() }
                .filter { it.isFile }
                .forEach {
                    if (!uniqueFileNames.add(it.name))
                        resourceFilesWithDuplicateUniqueFileNames.add(it)
                }
    }

    override fun getErrorMessages(): String {
        val errors: List<List<String>> = arrayListOf(
                resourceFilesWithDuplicateUniqueFileNames.map {
                    "Resource file with duplicate unique name in resource directory: ${it.absolutePath}${System.lineSeparator()}"
                }
        )
        return errors.flatten().joinToString(System.lineSeparator())
    }

}