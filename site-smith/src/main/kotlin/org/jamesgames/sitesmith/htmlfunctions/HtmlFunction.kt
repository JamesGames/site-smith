package org.jamesgames.sitesmith.htmlfunctions

import org.jamesgames.sitesmith.builder.SiteBuilder
import org.jamesgames.sitesmith.resources.Page
import org.pegdown.PegDownProcessor
import java.util.*
import java.util.stream.IntStream

/**
 * @author James Murphy
 */
class HtmlFunction(val name: String, private val functionBody: String, private val isMarkdown: Boolean) {
    private val parameters: ArrayList<String> = ArrayList()

    constructor (name: String, functionBody: String,
                 isMarkdown: Boolean, params: List<String>) : this(name, functionBody, isMarkdown) {
        parameters.addAll(params)
    }

    fun callFunction(pageCalledFrom: Page,
                     arguments: List<HtmlFunctionArgument>,
                     siteBuilder: SiteBuilder): String {
        if (arguments.size != parameters.size) {
            throw ParameterMismatchException(name, parameters.size, arguments.size);
        }
        val result = StringBuilder(functionBody)
        IntStream
                .rangeClosed(0, parameters.size - 1)
                .forEach { i ->
                    replaceAll(result, "$" + parameters[i],
                            arguments[i].evaluate(pageCalledFrom, siteBuilder))
                };
        return if (isMarkdown) PegDownProcessor().markdownToHtml(result.toString()) else result.toString();
    }

    private fun replaceAll(stringBuilder: StringBuilder, from: String, to: String) {
        var index = stringBuilder.indexOf(from);
        while (index != -1) {
            stringBuilder.replace(index, index + from.length, to);
            index += to.length;
            index = stringBuilder.indexOf(from, index);
        }
    }
}

