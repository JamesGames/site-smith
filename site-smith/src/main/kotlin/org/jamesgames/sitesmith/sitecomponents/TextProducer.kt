package org.jamesgames.sitesmith.sitecomponents

import org.jamesgames.sitesmith.builder.SiteComponentDatabase
import org.jamesgames.sitesmith.resources.Page

/**
 * @author James Murphy
 */
interface TextProducer {
    fun appendText(page: Page, componentDb: SiteComponentDatabase, stringBuilder: StringBuilder);
}

