package org.jamesgames.sitesmith.parsers

import org.jamesgames.sitesmith.htmlfunctions.HtmlFunctionArgument
import org.jamesgames.sitesmith.htmlfunctions.HtmlFunctionCall
import org.jamesgames.sitesmith.htmlfunctions.HtmlFunctionParseException
import java.io.File
import java.nio.file.Files
import java.nio.file.Paths
import java.util.*
import kotlin.collections.drop
import kotlin.collections.forEach
import kotlin.collections.map
import kotlin.text.split
import kotlin.text.trim

/**
 * @author James Murphy
 */
class HtmlScriptParser(private val htmlScriptSourceFile: File) {
    private val argumentSeparator = ","

    fun getHtmlScript() {
        val name = com.google.common.io.Files.getNameWithoutExtension(htmlScriptSourceFile.name)


    }

    private fun toHtmlFunctionCall(line: String): HtmlFunctionCall {
        var htmlFunctionArguments = ArrayList<HtmlFunctionArgument>();
        val tokens: List<String> = line.split(argumentSeparator)
        tokens
                .drop(0)
                .map { it.trim() }
                .map { ::HtmlFunctionArgument }
                .forEach { it -> htmlFunctionArguments.add(it.call()) }
        return HtmlFunctionCall(tokens[0], htmlFunctionArguments);
    }
}