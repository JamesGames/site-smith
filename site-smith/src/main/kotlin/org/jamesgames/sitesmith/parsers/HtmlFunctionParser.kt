package org.jamesgames.sitesmith.parsers

import org.jamesgames.sitesmith.htmlfunctions.HtmlFunction
import org.jamesgames.sitesmith.htmlfunctions.HtmlFunctionParseException
import java.io.File
import java.nio.file.Files
import java.nio.file.Paths
import kotlin.collections.drop
import kotlin.collections.forEach
import kotlin.collections.map
import kotlin.collections.toList
import kotlin.text.*

/**
 * @author James Murphy
 */
class HtmlFunctionParser(private val htmlFunctionSourceFile: File) {
    private val parameterSeparator = ","
    private val markdownNotice = "(markdown)"

    fun getHtmlFunction(): HtmlFunction {
        val name = com.google.common.io.Files.getNameWithoutExtension(htmlFunctionSourceFile.name)

        val lines = try {
            Files.readAllLines(Paths.get(htmlFunctionSourceFile.toURI()))
        } catch (e: Exception) {
            throw HtmlFunctionParseException(name, e.message!!)
        }
        if (lines.isEmpty()) throw HtmlFunctionParseException(name,
                "Function is empty, expected at least one line of params (body not needed)");
        val paramLine = lines[0].trim()
        val isMarkDown = paramLine.startsWith(markdownNotice)
        val restOfParamLine = if (isMarkDown) paramLine.substring(markdownNotice.length) else paramLine
        val params = restOfParamLine.split(parameterSeparator).map { it.trim() }.toList()
        val body = StringBuilder()
        lines.drop(1).forEach { body.appendln(it) }
        return HtmlFunction(name, body.toString(), isMarkDown, params)
    }
}