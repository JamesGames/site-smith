package org.jamesgames.sitesmith.builder.parsers

import org.jamesgames.sitesmith.sitecomponents.HtmlFunction
import org.jamesgames.sitesmith.sitecomponents.HtmlFunctionParseException
import java.io.File
import java.nio.file.Files
import java.nio.file.Paths
import kotlin.collections.*
import kotlin.text.*

/**
 * @author James Murphy
 */
internal class HtmlFunctionParser(private val htmlFunctionSourceFile: File) {
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
        val params = restOfParamLine.split(parameterSeparator)
                .filterNot { it.isEmpty() }
                .map { it.trim() }.toList()
        val body = StringBuilder()
        lines.drop(1).forEach { body.appendln(it) }
        return HtmlFunction(name, body.toString(), isMarkDown, params)
    }
}