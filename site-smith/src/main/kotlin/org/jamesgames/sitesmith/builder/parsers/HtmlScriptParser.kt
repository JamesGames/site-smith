package org.jamesgames.sitesmith.builder.parsers

import org.jamesgames.sitesmith.sitecomponents.HtmlFunctionArgument
import org.jamesgames.sitesmith.sitecomponents.HtmlFunctionCall
import org.jamesgames.sitesmith.sitecomponents.HtmlScript
import org.jamesgames.sitesmith.sitecomponents.HtmlScriptParseException
import java.io.File
import java.nio.file.Files
import java.nio.file.Paths
import kotlin.collections.drop
import kotlin.collections.filterNot
import kotlin.collections.map
import kotlin.collections.toList
import kotlin.text.isEmpty
import kotlin.text.split
import kotlin.text.trim

/**
 * @author James Murphy
 */
internal class HtmlScriptParser(private val htmlScriptSourceFile: File) {
    companion object {
        private val argumentSeparator = ","
    }

    fun getHtmlScript(): HtmlScript {
        val name = com.google.common.io.Files.getNameWithoutExtension(htmlScriptSourceFile.name)
        val lines = try {
            Files.readAllLines(Paths.get(htmlScriptSourceFile.toURI()))
        } catch (e: Exception) {
            throw HtmlScriptParseException(name, e.message!!)
        }
        if (lines.isEmpty()) throw HtmlScriptParseException(name,
                "Script is empty, expected at least one line for the script");
        return HtmlScript(name, lines.map { toHtmlFunctionCall(it) }.toList())
    }

    private fun toHtmlFunctionCall(line: String): HtmlFunctionCall {
        val tokens: List<String> = line.split(argumentSeparator).filterNot { it.isEmpty() }
        return HtmlFunctionCall(tokens[0], tokens.drop(0)
                .map { it.trim() }
                .map { HtmlFunctionArgument(it) }.toList());
    }
}