package org.jamesgames.sitesmith.creator

import org.jamesgames.sitesmith.builder.SiteComponentDatabase
import java.io.File
import java.io.FileWriter
import java.nio.file.Files
import java.nio.file.Paths
import java.util.stream.Collectors
import java.util.stream.Stream

/**
 * @author James Murphy
 */
class SiteCreator(private val siteLayoutFile: File,
                  private val textFunctionDirectory: File,
                  private val textScriptDirectory: File) {

    fun updateLayoutFile(newText: String) = overwriteFile(newText, siteLayoutFile)

    fun updateTextFunction(newText: String, functionName: String) =
            updateFile(newText, functionName + SiteComponentDatabase.textFunctionSourceExtension, textFunctionDirectory)

    fun updateTextScript(newText: String, scriptName: String) =
            updateFile(newText, scriptName + SiteComponentDatabase.textScriptSourceExtension, textScriptDirectory)

    private fun updateFile(newText: String, fileName: String, rootContainingDirectory: File) =
            overwriteFile(newText, findMatchingFile(fileName, rootContainingDirectory))

    private fun findMatchingFile(fileName: String, directoryToRecurseSearch: File): File {
        val matchingFiles = matchingFiles(directoryToRecurseSearch, fileName)
        verifySingleFileExists(matchingFiles, fileName)
        return matchingFiles.first()
    }

    private fun verifySingleFileExists(matchingFiles: List<File>,
                                       fileNameWithExtension: String) {
        when {
            matchingFiles.isEmpty() -> throw UpdateException("$fileNameWithExtension was not found")
            matchingFiles.size > 1 -> throw UpdateException(
                    "$fileNameWithExtension was found in multiple directories")
        }
    }

    private fun verifyNoFileExists(matchingFiles: List<File>, fileNameWithExtension: String) {
        when {
            matchingFiles.isNotEmpty() -> throw UpdateException("$fileNameWithExtension already exists")
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
        verifyNoFileExists(matchingFiles, file.name)
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

    fun renameTextFunction(functionName: String, newName: String) {
        renameFile(functionName + SiteComponentDatabase.textFunctionSourceExtension,
                newName + SiteComponentDatabase.textFunctionSourceExtension,
                textFunctionDirectory)
    }

    fun renameTextScript(scriptName: String, newName: String) {
        renameFile(scriptName + SiteComponentDatabase.textScriptSourceExtension,
                newName + SiteComponentDatabase.textScriptSourceExtension,
                textScriptDirectory)
    }

    private fun renameFile(oldFileName: String, newFileName: String, rootContainingDirectory: File) {
        val oldFile = Paths.get(rootContainingDirectory.absolutePath, oldFileName).toFile()
        val matchingOldFiles = matchingFiles(rootContainingDirectory, oldFile.name)
        verifySingleFileExists(matchingOldFiles, oldFile.name)

        val newFile = Paths.get(rootContainingDirectory.absolutePath, newFileName).toFile()
        val matchingNewFiles = matchingFiles(rootContainingDirectory, newFile.name)
        verifyNoFileExists(matchingNewFiles, newFile.name)

        oldFile.renameTo(newFile)
    }

    fun readTextFunction(functionName: String): String {
        return readFile(functionName + SiteComponentDatabase.textFunctionSourceExtension, textFunctionDirectory)
    }

    fun readTextScript(scriptName: String): String {
        return readFile(scriptName + SiteComponentDatabase.textScriptSourceExtension, textScriptDirectory)
    }

    fun readLayout(): String {
        return com.google.common.io.Files.toString(siteLayoutFile, Charsets.UTF_8)
    }

    private fun readFile(fileName: String, rootContainingDirectory: File): String {
        val file = Paths.get(rootContainingDirectory.absolutePath, fileName).toFile()
        val matchingFiles = matchingFiles(rootContainingDirectory, file.name)
        verifySingleFileExists(matchingFiles, file.name)
        return com.google.common.io.Files.toString(file, Charsets.UTF_8)
    }

    fun getTextFunctionNames(): List<String> {
        return allFiles(textFunctionDirectory, SiteComponentDatabase.textFunctionSourceExtension)
                .map{ it.nameWithoutExtension }
                .sorted()
                .collect(Collectors.toList())
    }

    fun getTextScriptNames(): List<String> {
        return allFiles(textScriptDirectory, SiteComponentDatabase.textScriptSourceExtension)
                .map { it.nameWithoutExtension }
                .sorted()
                .collect(Collectors.toList())
    }

    private fun allFiles(directoryToRecurseSearch: File,
                              extensionFilter: String): Stream<File> = Files.walk(
            Paths.get(directoryToRecurseSearch.toURI()))
            .map { it.toFile() }
            .filter { it != directoryToRecurseSearch }
            .filter { it.isFile }
            .filter { it.name.endsWith(extensionFilter) }
}