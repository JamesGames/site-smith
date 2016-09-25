package org.jamesgames.sitesmith.builder

import clojure.lang.ExceptionInfo
import org.jamesgames.sitesmith.builder.parsers.TextFunctionParser
import org.jamesgames.sitesmith.builder.parsers.TextScriptParser
import org.jamesgames.sitesmith.resources.Page
import org.jamesgames.sitesmith.resources.Resource
import org.jamesgames.sitesmith.textfunctions.TextScriptInterface
import java.io.File
import java.nio.file.Files
import java.nio.file.Paths
import java.util.*

/**
 * @author James Murphy
 */
class SiteComponentDatabase(private val textFunctionDirectory: File,
                            private val textScriptDirectory: File,
                            val globalCssFileNames: List<String>) {

    companion object {
        const private val textFunctionSourceExtension = ".clj"
        const private val textScriptSourceExtension = ".clj"
    }

    private val textFunctionMap: TextFunctionMap = TextFunctionMap()
    private val textScriptMap: TextScriptMap = TextScriptMap()
    private val resourceMap: ResourceMap = ResourceMap()

    init {
        fillFunctionMap()
        fillScriptMap()
    }

    fun writePages() = resourceMap.getPages().forEach { it.writePage(this) }

    fun recordResource(resource: Resource) =
            resourceMap.addResource(resource.getUniqueName(), resource)

    fun getRelativeResourcePath(name: String, relativeTo: Page): String =
            resourceMap.getRelativeResourcePath(name, relativeTo)

    fun doesResourceExist(name: String): Boolean =
            resourceMap.doesResourceExist(name)

    fun appendTextFromScript(scriptName: String, page: Page, stringBuilder: StringBuilder) =
            try {
                stringBuilder.append(TextScriptInterface.executeScript(
                        page.getUniqueName(),
                        getResourceNameToPathFunction(page),
                        textScriptMap.getTextScript(scriptName).scriptText,
                        resourceMap.getAllResourceNames()))
            }catch (e: ExceptionInfo) {
                throw ScriptExecutionException(page.getUniqueName(), scriptName, e.toString(), e)
            }
            catch (e: Exception) {
                throw ScriptExecutionException(page.getUniqueName(), scriptName, e.message + "", e)
            }

    fun getResourceNameToPathFunction(page: Page) = { s: String -> resourceMap.getRelativeResourcePath(s, page) }


    private fun fillFunctionMap() {
        textFunctionMap.clearMap()
        Files.walk(Paths.get(textFunctionDirectory.toURI()))
                .map { it.toFile() }
                .filter { it != textFunctionDirectory }
                .filter { it.isFile }
                .filter { it.name.endsWith(textFunctionSourceExtension) }
                .map { TextFunctionParser(it) }
                .map { it.getTextFunction() }
                .forEach { textFunctionMap.addTextFunction(it) }
    }

    private fun fillScriptMap() {
        textScriptMap.clearMap()
        Files.walk(Paths.get(textScriptDirectory.toURI()))
                .map { it.toFile() }
                .filter { it != textScriptDirectory }
                .filter { it.isFile }
                .filter { it.name.endsWith(textScriptSourceExtension) }
                .map { TextScriptParser(it) }
                .map { it.getTextScript() }
                .forEach { textScriptMap.addTextScript(it) }
    }
}