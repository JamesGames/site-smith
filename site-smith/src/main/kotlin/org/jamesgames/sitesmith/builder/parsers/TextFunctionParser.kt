package org.jamesgames.sitesmith.builder.parsers

import org.jamesgames.sitesmith.sitecomponents.TextFunction
import org.jamesgames.sitesmith.sitecomponents.TextFunctionParseException
import org.jamesgames.sitesmith.textfunctions.TextFunctionInterface
import java.io.File
import java.nio.file.Paths

/**
 * @author James Murphy
 */
internal class TextFunctionParser(private val textFunctionSourceFile: File) {

    companion object {
        private val formatAndExample =
                "Format: ([vector-of-parameter-names][clojure-string-expression][optional-vector-of-options])" +
                        System.lineSeparator() + "Example: ([x y](str x y)[\"some-option\" some-resolvable-option])"
    }

    fun getTextFunction(): TextFunction {
        val name = com.google.common.io.Files.getNameWithoutExtension(textFunctionSourceFile.name)
        val text = com.google.common.io.Files.toString(
                Paths.get(textFunctionSourceFile.toURI()).toFile(),
                Charsets.UTF_8)
        try {
            if (!TextFunctionInterface.isFunctionTextInValidFormat(text))
                throw TextFunctionParseException(name, "Function is in an invalid format. Format should be:" +
                        System.lineSeparator() + formatAndExample)
        } catch (e: Exception) {
            throw TextFunctionParseException(name, "Function error:${System.lineSeparator()}${e.message}")
        }
        return TextFunction(name, text)
    }
}