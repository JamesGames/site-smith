package org.jamesgames.sitesmith.sitecomponents

import org.jamesgames.sitesmith.builder.SiteComponentDatabase
import org.jamesgames.sitesmith.resources.Page

/**
 * @author James Murphy
 */
interface HtmlProducer {
    fun appendHtml(page: Page, componentDb: SiteComponentDatabase, stringBuilder: StringBuilder);
}

