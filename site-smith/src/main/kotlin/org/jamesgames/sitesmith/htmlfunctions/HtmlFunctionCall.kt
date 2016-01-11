package org.jamesgames.sitesmith.htmlfunctions

import org.jamesgames.sitesmith.project.HtmlFunctionMap
import org.jamesgames.sitesmith.project.ResourceMap
import org.jamesgames.sitesmith.resources.Page
import java.util.*

class HtmlFunctionCall(val name: String) : HtmlProducer {
    private val arguments: ArrayList<HtmlFunctionArgument> = ArrayList()

    fun addArgument(argument: HtmlFunctionArgument) {
        arguments.add(argument);
    }

    override fun appendHtml(page: Page,
                            resourceMap: ResourceMap,
                            htmlFunctionMap: HtmlFunctionMap,
                            stringBuilder: StringBuilder) {
        stringBuilder.append(htmlFunctionMap.getHtmlFunction(name).callFunction(page, arguments, resourceMap));
    }
}