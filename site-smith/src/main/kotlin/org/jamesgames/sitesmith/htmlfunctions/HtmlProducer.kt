package org.jamesgames.sitesmith.htmlfunctions

import org.jamesgames.sitesmith.builder.SiteBuilder
import org.jamesgames.sitesmith.resources.Page

/**
 * @author James Murphy
 */
interface HtmlProducer {
    fun appendHtml(page: Page, siteBuilder: SiteBuilder, stringBuilder: StringBuilder);
}

