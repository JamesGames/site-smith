package org.jamesgames.sitesmith.htmlfunctions

import org.jamesgames.sitesmith.builder.SiteBuilder
import org.jamesgames.sitesmith.resources.Page
import kotlin.collections.forEach

/**
 * @author James Murphy
 */
class HtmlScript(val name: String, private val htmlFunctionCalls: List<HtmlFunctionCall>) : HtmlProducer {

    override fun appendHtml(page: Page,
                            siteBuilder: SiteBuilder,
                            stringBuilder: StringBuilder) {
        htmlFunctionCalls.forEach {
            stringBuilder.append(it.appendHtml(page, siteBuilder, stringBuilder))
        }
    }
}

