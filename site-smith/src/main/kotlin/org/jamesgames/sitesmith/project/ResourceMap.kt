package org.jamesgames.sitesmith.project

import org.jamesgames.sitesmith.resources.Page

/**
 * @author James Murphy
 */
interface ResourceMap {
    fun getResourcePath(nameOfResource: String, relativeTo: Page): String
}