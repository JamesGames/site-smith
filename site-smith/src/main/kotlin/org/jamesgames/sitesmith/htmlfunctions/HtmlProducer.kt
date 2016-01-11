package org.jamesgames.sitesmith.htmlfunctions

import org.jamesgames.sitesmith.project.HtmlFunctionMap
import org.jamesgames.sitesmith.project.ResourceMap
import org.jamesgames.sitesmith.resources.Page

/**
 * @author James Murphy
 */
interface HtmlProducer {
    fun appendHtml(page: Page, resourceMap: ResourceMap, htmlFunctionMap: HtmlFunctionMap, stringBuilder: StringBuilder);
}

