package org.jamesgames.sitesmith.sitecomponents

import org.jamesgames.sitesmith.builder.SiteBuilder
import org.jamesgames.sitesmith.resources.Page

class HtmlFunctionCall(val name: String, private val arguments: List<HtmlFunctionArgument>) : HtmlProducer {
    override fun appendHtml(page: Page,
                            siteBuilder: SiteBuilder,
                            stringBuilder: StringBuilder) {
        stringBuilder.append(siteBuilder.callFunction(name, page, arguments));
    }
}