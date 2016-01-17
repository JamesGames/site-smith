package org.jamesgames.sitesmith.builder

import org.jamesgames.sitesmith.sitecomponents.HtmlFunction
import java.util.*

/**
 * @author James Murphy
 */
internal class HtmlFunctionMap {
    private val nameToFunction: HashMap<String, HtmlFunction> = HashMap()

    fun addHtmlFunction(function: HtmlFunction) {
        nameToFunction.put(function.name, function);
    }

    fun getHtmlFunction(name: String): HtmlFunction = nameToFunction[name] ?: throw UndefinedHtmlFunctionException(name)

    fun clearMap() {
        nameToFunction.clear();
    }
}