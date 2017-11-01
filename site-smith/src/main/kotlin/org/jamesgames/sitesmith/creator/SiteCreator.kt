package org.jamesgames.sitesmith.creator

import org.jamesgames.sitesmith.builder.SiteComponentDatabase
import java.io.File
import java.io.FileWriter
import java.nio.file.Files
import java.nio.file.Paths
import java.util.stream.Collectors

/**
 * @author James Murphy
 */
class SiteCreator(private val siteLayoutFile: File,
                  private val textFunctionDirectory: File,
                  private val textScriptDirectory: File) {

    fun modifyLayoutFile(newText: String) = overwriteFile(newText, siteLayoutFile)

    fun modifyTextFunction(newText: String, functionName: String) =
            modifyFile(newText, functionName + SiteComponentDatabase.textFunctionSourceExtension, textFunctionDirectory)

    fun modifyTextScript(newText: String, scriptName: String) =
            modifyFile(newText, scriptName + SiteComponentDatabase.textScriptSourceExtension, textScriptDirectory)

    private fun modifyFile(newText: String, fileName: String, rootContainingDirectory: File) =
            overwriteFile(newText, findMatchingFile(fileName, rootContainingDirectory))

    private fun findMatchingFile(fileName: String, directoryToRecurseSearch: File): File {
        val matchingFiles = matchingFiles(directoryToRecurseSearch, fileName)
        verifySingleFileExists(matchingFiles, fileName)
        return matchingFiles.first()
    }

    private fun verifySingleFileExists(matchingFiles: List<File>,
                                       fileNameWithExtension: String) {
        when {
            matchingFiles.isEmpty() -> throw ModificationException("$fileNameWithExtension was not found")
            matchingFiles.size > 1 -> throw ModificationException(
                    "$fileNameWithExtension was found in multiple directories")
        }
    }

    private fun overwriteFile(newText: String, file: File) = FileWriter(file).use { it.write(newText) }

    private fun matchingFiles(directoryToRecurseSearch: File,
                              fileNameWithExtension: String): List<File> = Files.walk(
            Paths.get(directoryToRecurseSearch.toURI()))
            .map { it.toFile() }
            .filter { it != directoryToRecurseSearch }
            .filter { it.isFile }
            .filter { it.name.equals(fileNameWithExtension) }
            .collect(Collectors.toList())

    fun createTextFunction(text: String, functionName: String) {
        createFile(text, functionName + SiteComponentDatabase.textFunctionSourceExtension, textFunctionDirectory)
    }

    fun createTextScript(text: String, scriptName: String) {
        createFile(text, scriptName + SiteComponentDatabase.textScriptSourceExtension, textScriptDirectory)
    }

    private fun createFile(text: String, fileName: String, rootContainingDirectory: File) {
        val file = Paths.get(rootContainingDirectory.absolutePath, fileName).toFile()
        val matchingFiles = matchingFiles(rootContainingDirectory, file.name)
        if (matchingFiles.isNotEmpty()) {
            throw ModificationException("${file.name} already exists")
        }
        file.createNewFile()
        overwriteFile(text, file)
    }

    fun deleteTextFunction(functionName: String) {
        deleteFile(functionName + SiteComponentDatabase.textFunctionSourceExtension, textFunctionDirectory)
    }

    fun deleteTextScript(scriptName: String) {
        deleteFile(scriptName + SiteComponentDatabase.textScriptSourceExtension, textScriptDirectory)
    }

    private fun deleteFile(fileName: String, rootContainingDirectory: File) {
        val file = Paths.get(rootContainingDirectory.absolutePath, fileName).toFile()
        val matchingFiles = matchingFiles(rootContainingDirectory, file.name)
        verifySingleFileExists(matchingFiles, file.name)
        file.delete()
    }
}