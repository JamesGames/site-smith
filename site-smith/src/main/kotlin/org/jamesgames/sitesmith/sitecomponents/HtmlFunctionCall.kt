package org.jamesgames.sitesmith.sitecomponents

import org.jamesgames.sitesmith.builder.SiteComponentDatabase
import org.jamesgames.sitesmith.resources.Page

/**
 * @author James Murphy
 */
class HtmlFunctionCall(val name: String, private val arguments: List<HtmlFunctionArgument>) : HtmlProducer {
    override fun appendHtml(page: Page,
                            componentDb: SiteComponentDatabase,
                            stringBuilder: StringBuilder) {
        componentDb.callFunction(name, page, arguments, stringBuilder);
    }
}