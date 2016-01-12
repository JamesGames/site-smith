package org.jamesgames.sitesmith.htmlfunctions

import org.jamesgames.sitesmith.project.Project
import org.jamesgames.sitesmith.resources.Page

/**
 * @author James Murphy
 */
interface HtmlProducer {
    fun appendHtml(page: Page, project: Project, stringBuilder: StringBuilder);
}

