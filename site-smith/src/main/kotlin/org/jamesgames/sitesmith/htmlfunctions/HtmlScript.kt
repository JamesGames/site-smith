package org.jamesgames.sitesmith.htmlfunctions

import org.jamesgames.sitesmith.project.Project
import org.jamesgames.sitesmith.resources.Page
import kotlin.collections.forEach

/**
 * @author James Murphy
 */
class HtmlScript(val name: String, private val htmlFunctionCalls: List<HtmlFunctionCall>) : HtmlProducer {

    override fun appendHtml(page: Page,
                            project: Project,
                            stringBuilder: StringBuilder) {
        htmlFunctionCalls.forEach {
            stringBuilder.append(it.appendHtml(page, project, stringBuilder))
        }
    }
}

