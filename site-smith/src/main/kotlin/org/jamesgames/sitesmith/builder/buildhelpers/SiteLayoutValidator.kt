package org.jamesgames.sitesmith.builder.buildhelpers

import org.jamesgames.sitesmith.builder.SiteLayout
import java.io.File
import java.util.*

/**
 * @author James Murphy
 */
internal class SiteLayoutValidator(private val siteLayout: SiteLayout) : BuildHelper {

    companion object {
        const private val resourcesSpecifiedByDirectoryLayoutText = "Layout description has specified where " +
                "to locate resource files by both the by-directory root option and by specifying details of the " +
                "resource files individually throughout the layout. Choose one or the other."
    }

    private val resourceIdentifierNamesWithinSameProject: MutableSet<String> = HashSet()
    private val pageIdentifierNamesWithinSameProject: MutableSet<String> = HashSet()
    private val cssFilesFound: MutableList<Triple<String, SiteLayout.PageInfo, String>> = ArrayList()
    // For use when specifying resource files by resource directory layout
    private val resourceFileNamesWithinSameProject: MutableSet<String> = HashSet()

    private val listOfDuplicateResourcesNamesWithinEntireProject: MutableList<Pair<SiteLayout.ResourceInfo, String>> = ArrayList()
    private val listOfDuplicateDirectoriesWithinSameDirectory: MutableList<Pair<SiteLayout.DirectoryInfo, String>> = ArrayList()
    private val listOfDuplicatePageIdentifierWithinEntireProject: MutableList<Pair<SiteLayout.PageInfo, String>> = ArrayList()
    private val listOfDuplicateFileNamesWithinSameDirectory: MutableList<Pair<String, String>> = ArrayList()
    private val listOfEmptyDirectoryNames: MutableList<Pair<SiteLayout.DirectoryInfo, String>> = ArrayList()
    private val listOfEmptyFileNames: MutableList<Pair<String, String>> = ArrayList()
    private var listOfSpecifiedCssFilesThatDoNotExist: MutableList<Triple<String, SiteLayout.PageInfo, String>> = ArrayList()
    private var resourcesSpecifiedByBothDirectoryLayoutAndLayoutFile: Boolean = false;
    // For use when specifying resource files by resource directory layout
    private val listOfDuplicateResourcesFileNamesWithinEntireProject: MutableList<Pair<SiteLayout.ResourceInfo, String>> = ArrayList()

    override fun getErrorMessages(): String {
        val errors: MutableList<List<String>> = arrayListOf(
                listOfDuplicateResourcesNamesWithinEntireProject
                        .map { "Duplicate resource id in project: ${it.first.uniqueName}, duplicate found in: ${it.second}${System.lineSeparator()}" },
                listOfDuplicateDirectoriesWithinSameDirectory
                        .map { "Duplicate directory name in directory: ${it.first.name}, duplicate found in: ${it.second}${System.lineSeparator()}" },
                listOfDuplicatePageIdentifierWithinEntireProject
                        .map { "Duplicate page id in project: ${it.first.uniqueName}, duplicate found in: ${it.second}${System.lineSeparator()}" },
                listOfDuplicateFileNamesWithinSameDirectory
                        .map { "Duplicate file name in directory: ${it.first}, duplicate found in: ${it.second}${System.lineSeparator()}" },
                listOfEmptyDirectoryNames
                        .map { "Empty directory name, found in: ${it.second}${System.lineSeparator()}" },
                listOfEmptyFileNames
                        .map { "Empty file name, found in: ${it.second}${System.lineSeparator()}" },
                listOfSpecifiedCssFilesThatDoNotExist
                        .map { "Css file with the unique resource id of ${it.first} not found, page that used file: ${it.third}${it.second.fileName}${System.lineSeparator()}" })

        if (resourcesSpecifiedByBothDirectoryLayoutAndLayoutFile) {
            errors.add(arrayListOf(resourcesSpecifiedByDirectoryLayoutText + System.lineSeparator()))
            errors.add(listOfDuplicateResourcesFileNamesWithinEntireProject
                    .map { "Duplicate resource file name in project: ${it.first.fileName}, duplicate found in: ${it.second}${System.lineSeparator()}" })
        }

        return errors.flatten().joinToString("")
    }

    override fun applyBuildAction() {
        clearLists()
        cssFilesFound.clear()
        validateDirectories(siteLayout.root, File.separator);
        findSpecifiedCssFilesThatDoNotExist()
    }

    private fun findSpecifiedCssFilesThatDoNotExist() {
        listOfSpecifiedCssFilesThatDoNotExist.addAll(cssFilesFound
                .filterNot { resourceIdentifierNamesWithinSameProject.contains(it.first) })
    }

    private fun clearLists() {
        listOfDuplicateResourcesNamesWithinEntireProject.clear()
        listOfDuplicateDirectoriesWithinSameDirectory.clear()
        listOfDuplicatePageIdentifierWithinEntireProject.clear()
        listOfDuplicateFileNamesWithinSameDirectory.clear()
        listOfEmptyDirectoryNames.clear()
        listOfEmptyFileNames.clear()
        listOfSpecifiedCssFilesThatDoNotExist.clear()
        resourceIdentifierNamesWithinSameProject.clear()
        resourceFileNamesWithinSameProject.clear()
    }

    private fun validateDirectories(directory: SiteLayout.DirectoryInfo, directoryPathSoFar: String) {
        validateResourcesAndPages(directory.pages, directory.resources, directoryPathSoFar)
        checkForDuplicateDirectoryIssues(directory, directoryPathSoFar)
        directory.directories?.forEach { validateDirectories(it, directoryPathSoFar + File.separator) }
    }

    private fun checkForDuplicateDirectoryIssues(directory: SiteLayout.DirectoryInfo, directoryPathSoFar: String) {
        val directoryNamesInDirectory: MutableSet<String> = HashSet()
        directory.directories?.forEach {
            if (!directoryNamesInDirectory.add(it.name))
                listOfDuplicateDirectoriesWithinSameDirectory.add(Pair(it, directoryPathSoFar))
            if (it.name.isEmpty())
                listOfEmptyDirectoryNames.add(Pair(it, directoryPathSoFar))
        }
    }

    private fun validateResourcesAndPages(pages: List<SiteLayout.PageInfo>?,
                                          resources: List<SiteLayout.ResourceInfo>?,
                                          directoryPathSoFar: String) {
        val fileNamesInDirectory: MutableSet<String> = HashSet()
        validatePages(directoryPathSoFar, fileNamesInDirectory, pages)
        validateResources(directoryPathSoFar, fileNamesInDirectory, resources)
    }

    private fun validateResources(directoryPathSoFar: String, fileNamesInDirectory: MutableSet<String>,
                                  resources: List<SiteLayout.ResourceInfo>?) {
        resources?.forEach {
            if (siteLayout.specifyResourcesByDirectory)
                resourcesSpecifiedByBothDirectoryLayoutAndLayoutFile = true;
            if (!fileNamesInDirectory.add(it.uniqueName ?: it.fileName))
                listOfDuplicateFileNamesWithinSameDirectory.add(Pair(it.fileName, directoryPathSoFar))
            if (!resourceFileNamesWithinSameProject.add(it.fileName))
                listOfDuplicateResourcesFileNamesWithinEntireProject.add(Pair(it, directoryPathSoFar))
            if (it.fileName.isEmpty())
                listOfEmptyFileNames.add(Pair(it.fileName, directoryPathSoFar))
            if (!resourceIdentifierNamesWithinSameProject.add(it.uniqueName ?: it.fileName))
                listOfDuplicateResourcesNamesWithinEntireProject.add(Pair(it, directoryPathSoFar))
        }
    }

    private fun validatePages(directoryPathSoFar: String, fileNamesInDirectory: MutableSet<String>,
                              pages: List<SiteLayout.PageInfo>?) {
        pages?.forEach {
            if (!fileNamesInDirectory.add(it.fileName))
                listOfDuplicateFileNamesWithinSameDirectory.add(Pair(it.fileName, directoryPathSoFar))
            if (it.fileName.isEmpty())
                listOfEmptyFileNames.add(Pair(it.fileName, directoryPathSoFar))
            if (!pageIdentifierNamesWithinSameProject.add(it.uniqueName ?: it.fileName))
                listOfDuplicatePageIdentifierWithinEntireProject.add(Pair(it, directoryPathSoFar))
            cssFilesFound.addAll(
                    it.additionalCssFiles?.map { css -> Triple(css, it, directoryPathSoFar) } ?: ArrayList())
        }
    }
}