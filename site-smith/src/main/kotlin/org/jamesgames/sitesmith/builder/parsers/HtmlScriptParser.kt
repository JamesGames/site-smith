package org.jamesgames.sitesmith.builder.parsers

import org.jamesgames.sitesmith.sitecomponents.HtmlScript
import org.jamesgames.sitesmith.sitecomponents.HtmlScriptParseException
import org.jamesgames.sitesmith.textfunctions.TextScript
import java.io.File
import java.nio.file.Paths

/**
 * @author James Murphy
 */
internal class HtmlScriptParser(private val htmlScriptSourceFile: File) {
    companion object {
        private val formatAndExample =
                "Format: (clojure-function-calls*)" + System.lineSeparator() +
                        "Example:" + System.lineSeparator() +
                        "((someHtmlFunctionName \"stringArg\" (+ 2 2) resolvableSymbol)" + System.lineSeparator() +
                        " (str \"words\"))"
    }

    fun getHtmlScript(): HtmlScript {
        val name = com.google.common.io.Files.getNameWithoutExtension(htmlScriptSourceFile.name)
        val text = com.google.common.io.Files.toString(
                Paths.get(htmlScriptSourceFile.toURI()).toFile(),
                Charsets.UTF_8)
        if (TextScript.isScriptInValidFormat(text)) {
            throw HtmlScriptParseException(name, "Script is in an invalid format. Format should be:" +
                    System.lineSeparator() + HtmlScriptParser.formatAndExample);
        }
        return HtmlScript(name, text);
    }

}