package org.jamesgames.sitesmith.sitecomponents

import org.jamesgames.sitesmith.builder.SiteBuilder
import org.jamesgames.sitesmith.resources.Page

/**
 * @author James Murphy
 */
interface HtmlProducer {
    fun appendHtml(page: Page, siteBuilder: SiteBuilder, stringBuilder: StringBuilder);
}

