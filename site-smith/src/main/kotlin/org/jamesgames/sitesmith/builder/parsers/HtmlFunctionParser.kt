package org.jamesgames.sitesmith.builder.parsers

import org.jamesgames.sitesmith.sitecomponents.HtmlFunction
import org.jamesgames.sitesmith.sitecomponents.HtmlFunctionParseException
import org.jamesgames.sitesmith.textfunctions.TextFunction
import java.io.File
import java.nio.file.Paths

/**
 * @author James Murphy
 */
internal class HtmlFunctionParser(private val htmlFunctionSourceFile: File) {

    companion object {
        private val formatAndExample =
                "Format: ([vector-of-parameter-names][clojure-string-expression][optional-vector-of-options])" +
                        System.lineSeparator() + "Example: ([x y](str x y)[\"some-option\" some-resolvable-option])"
    }

    fun getHtmlFunction(): HtmlFunction {
        val name = com.google.common.io.Files.getNameWithoutExtension(htmlFunctionSourceFile.name)
        val text = com.google.common.io.Files.toString(
                Paths.get(htmlFunctionSourceFile.toURI()).toFile(),
                Charsets.UTF_8)
        try {
            if (!TextFunction.isFunctionTextInValidFormat(text))
                throw HtmlFunctionParseException(name, "Function is in an invalid format. Format should be:" +
                        System.lineSeparator() + formatAndExample);
        } catch (e: Exception) {
            throw HtmlFunctionParseException(name, "Function error:${System.lineSeparator()}${e.message}");
        }
        return HtmlFunction(name, text)
    }
}