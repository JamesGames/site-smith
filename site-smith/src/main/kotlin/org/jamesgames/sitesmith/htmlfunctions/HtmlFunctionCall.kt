package org.jamesgames.sitesmith.htmlfunctions

import org.jamesgames.sitesmith.project.Project
import org.jamesgames.sitesmith.resources.Page

class HtmlFunctionCall(val name: String, private val arguments: List<HtmlFunctionArgument>) : HtmlProducer {
    override fun appendHtml(page: Page,
                            project: Project,
                            stringBuilder: StringBuilder) {
        stringBuilder.append(project.callFunction(name, page, arguments, project));
    }
}