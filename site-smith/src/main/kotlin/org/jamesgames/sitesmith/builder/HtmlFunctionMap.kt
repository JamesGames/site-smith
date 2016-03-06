package org.jamesgames.sitesmith.builder

import org.jamesgames.sitesmith.sitecomponents.HtmlFunction
import org.jamesgames.sitesmith.textfunctions.TextFunction
import java.util.*

/**
 * @author James Murphy
 */
internal class HtmlFunctionMap {
    private val nameToFunction: HashMap<String, HtmlFunction> = HashMap()

    fun addHtmlFunction(function: HtmlFunction) {
        nameToFunction.put(function.name, function);
        TextFunction.defineFunction(
                function.name,
                function.functionText)
    }

    fun clearMap() {
        nameToFunction.clear();
    }
}