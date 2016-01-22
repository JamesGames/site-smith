package org.jamesgames.sitesmith.builder

import org.jamesgames.sitesmith.resources.Page
import org.jamesgames.sitesmith.sitecomponents.HtmlFunction
import org.jamesgames.sitesmith.sitecomponents.HtmlFunctionArgument
import java.util.*
import kotlin.text.appendln

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

    fun callFunction(name: String, page: Page, arguments: List<HtmlFunctionArgument>, componentDb: SiteComponentDatabase, stringBuilder: StringBuilder) {
        stringBuilder.appendln(getHtmlFunction(name).callFunction(page, arguments, componentDb))
    }
}