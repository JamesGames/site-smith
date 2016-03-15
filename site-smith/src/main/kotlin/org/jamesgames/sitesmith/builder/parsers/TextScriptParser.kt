package org.jamesgames.sitesmith.builder.parsers

import org.jamesgames.sitesmith.sitecomponents.TextScript
import org.jamesgames.sitesmith.sitecomponents.TextScriptParseException
import org.jamesgames.sitesmith.textfunctions.TextScriptInterface
import java.io.File
import java.nio.file.Paths

/**
 * @author James Murphy
 */
internal class TextScriptParser(private val textScriptSourceFile: File) {
    companion object {
        private val formatAndExample =
                "Format: (clojure-function-calls*)" + System.lineSeparator() +
                        "Example:" + System.lineSeparator() +
                        "((someTextFunctionName \"stringArg\" (+ 2 2) resolvableSymbol)" + System.lineSeparator() +
                        " (str \"words\"))"
    }

    fun getTextScript(): TextScript {
        val name = com.google.common.io.Files.getNameWithoutExtension(textScriptSourceFile.name)
        val text = com.google.common.io.Files.toString(
                Paths.get(textScriptSourceFile.toURI()).toFile(),
                Charsets.UTF_8)
        try {
            if (!TextScriptInterface.isScriptInValidFormat(text))
                throw TextScriptParseException(name, "Script is in an invalid format. Format should be:" +
                        System.lineSeparator() + TextScriptParser.formatAndExample);
        } catch (e: Exception) {
            throw TextScriptParseException(name, "Function error:${System.lineSeparator()}${e.message}");
        }
        return TextScript(name, text);
    }

}