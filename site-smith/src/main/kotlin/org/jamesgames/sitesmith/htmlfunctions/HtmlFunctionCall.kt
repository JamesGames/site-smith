package org.jamesgames.sitesmith.htmlfunctions

import org.jamesgames.sitesmith.project.Project
import org.jamesgames.sitesmith.resources.Page
import java.util.*

class HtmlFunctionCall(val name: String) : HtmlProducer {
    private val arguments: ArrayList<HtmlFunctionArgument> = ArrayList()

    fun addArgument(argument: HtmlFunctionArgument) {
        arguments.add(argument);
    }

    override fun appendHtml(page: Page,
                            project: Project,
                            stringBuilder: StringBuilder) {
        stringBuilder.append(project.getHtmlFunction(name).callFunction(page, arguments, project));
    }
}