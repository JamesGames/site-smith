package org.jamesgames.sitesmith.sitecomponents

import org.jamesgames.sitesmith.builder.SiteComponentDatabase
import org.jamesgames.sitesmith.resources.Page
import kotlin.collections.forEach

/**
 * @author James Murphy
 */
class HtmlScript(val name: String, private val htmlFunctionCalls: List<HtmlFunctionCall>) : HtmlProducer {

    override fun appendHtml(page: Page,
                            componentDb: SiteComponentDatabase,
                            stringBuilder: StringBuilder) {
        htmlFunctionCalls.forEach {
            it.appendHtml(page, componentDb, stringBuilder)
        }
    }
}

