package org.jamesgames.sitesmith.project

import org.jamesgames.sitesmith.htmlfunctions.HtmlFunction
import org.jamesgames.sitesmith.project.UndefinedHtmlFunctionException
import java.util.*

/**
 * @author James Murphy
 */
class HtmlFunctionMap {
    private val nameToFunction: HashMap<String, HtmlFunction> = HashMap()

    fun addHtmlFunction(name: String, function: HtmlFunction) {
        nameToFunction.put(name, function);
    }

    fun getHtmlFunction(name: String): HtmlFunction = nameToFunction[name] ?: throw UndefinedHtmlFunctionException(name)
}