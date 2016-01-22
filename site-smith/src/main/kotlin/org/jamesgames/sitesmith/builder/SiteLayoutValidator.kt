package org.jamesgames.sitesmith.builder

import java.io.File
import java.util.*
import kotlin.collections.forEach
import kotlin.collections.joinToString
import kotlin.collections.map
import kotlin.text.appendln
import kotlin.text.isEmpty

/**
 * @author James Murphy
 */
class SiteLayoutValidator(private val siteLayout: SiteLayout) {

    private val resourceIdentifierNamesWithinSameProject: MutableSet<String> = HashSet()
    private val pageIdentifierNamesWithinSameProject: MutableSet<String> = HashSet()

    private val listOfDuplicateResourcesNamesWithinEntireProject: MutableList<Pair<SiteLayout.ResourceInfo, String>> = ArrayList()
    private val listOfDuplicateDirectoriesWithinSameDirectory: MutableList<Pair<SiteLayout.DirectoryInfo, String>> = ArrayList()
    private val listOfDuplicatePageIdentifierWithinEntireProject: MutableList<Pair<SiteLayout.PageInfo, String>> = ArrayList()
    private val listOfDuplicateFileNamesWithinSameDirectory: MutableList<Pair<String, String>> = ArrayList()
    private val listOfEmptyDirectoryNames: MutableList<Pair<SiteLayout.DirectoryInfo, String>> = ArrayList()
    private val listOfEmptyFileNames: MutableList<Pair<String, String>> = ArrayList()

    fun getErrorMessages(): String = StringBuilder().appendln(listOfDuplicateResourcesNamesWithinEntireProject
            .map { "Duplicate resource id in project: ${it.first.uniqueName}, duplicate found in: ${it.second}" }
            .joinToString { System.lineSeparator() }).
            appendln(listOfDuplicateDirectoriesWithinSameDirectory
                    .map { "Duplicate directory name in directory: ${it.first.name}, duplicate found in: ${it.second}" }
                    .joinToString { System.lineSeparator() }).
            appendln(listOfDuplicatePageIdentifierWithinEntireProject
                    .map { "Duplicate page id in project: ${it.first.uniqueName}, duplicate found in: ${it.second}" }
                    .joinToString { System.lineSeparator() }).
            appendln(listOfDuplicateFileNamesWithinSameDirectory
                    .map { "Duplicate file name in directory: ${it.first}, duplicate found in: ${it.second}" }
                    .joinToString { System.lineSeparator() }).
            appendln(listOfEmptyDirectoryNames
                    .map { "Empty directory name, found in: ${it.second}" }
                    .joinToString { System.lineSeparator() }).
            appendln(listOfEmptyFileNames
                    .map { "Empty file name, found in: ${it.second}" }
                    .joinToString { System.lineSeparator() }).toString()

    fun validateSiteLayout(): Boolean {
        clearErrorLists()
        validateDirectories(siteLayout.root, File.separator);
        return listOfDuplicateResourcesNamesWithinEntireProject.isEmpty() &&
                listOfDuplicateDirectoriesWithinSameDirectory.isEmpty() &&
                listOfDuplicatePageIdentifierWithinEntireProject.isEmpty() &&
                listOfDuplicateFileNamesWithinSameDirectory.isEmpty()
    }

    private fun clearErrorLists() {
        listOfDuplicateResourcesNamesWithinEntireProject.clear()
        listOfDuplicateDirectoriesWithinSameDirectory.clear()
        listOfDuplicatePageIdentifierWithinEntireProject.clear()
        listOfDuplicateFileNamesWithinSameDirectory.clear()
    }

    private fun validateDirectories(directory: SiteLayout.DirectoryInfo, directoryPathSoFar: String) {
        validateResourcesAndPages(directory.pages, directory.resources, directoryPathSoFar)
        checkForDuplicateDirectoryIssues(directory, directoryPathSoFar)
        directory.directories.forEach { validateDirectories(it, directoryPathSoFar + File.separator) }
    }

    private fun checkForDuplicateDirectoryIssues(directory: SiteLayout.DirectoryInfo, directoryPathSoFar: String) {
        val directoryNamesInDirectory: MutableSet<String> = HashSet()
        directory.directories.forEach {
            if (directoryNamesInDirectory.add(it.name))
                listOfDuplicateDirectoriesWithinSameDirectory.add(Pair(it, directoryPathSoFar))
            if (it.name.isEmpty())
                listOfEmptyDirectoryNames.add(Pair(it, directoryPathSoFar))
        }
    }

    private fun validateResourcesAndPages(pages: List<SiteLayout.PageInfo>,
                                          resources: List<SiteLayout.ResourceInfo>,
                                          directoryPathSoFar: String) {
        val fileNamesInDirectory: MutableSet<String> = HashSet()
        pages.forEach {
            if (fileNamesInDirectory.add(it.fileName))
                listOfDuplicateFileNamesWithinSameDirectory.add(Pair(it.fileName, directoryPathSoFar))
            if (it.fileName.isEmpty())
                listOfEmptyFileNames.add(Pair(it.fileName, directoryPathSoFar))
            if (pageIdentifierNamesWithinSameProject.add(it.uniqueName))
                listOfDuplicatePageIdentifierWithinEntireProject.add(Pair(it, directoryPathSoFar))
        }
        resources.forEach {
            if (fileNamesInDirectory.add(it.uniqueName))
                listOfDuplicateFileNamesWithinSameDirectory.add(Pair(it.fileName, directoryPathSoFar))
            if (it.fileName.isEmpty())
                listOfEmptyFileNames.add(Pair(it.fileName, directoryPathSoFar))
            if (resourceIdentifierNamesWithinSameProject.add(it.uniqueName))
                listOfDuplicateResourcesNamesWithinEntireProject.add(Pair(it, directoryPathSoFar))
        }
    }
}