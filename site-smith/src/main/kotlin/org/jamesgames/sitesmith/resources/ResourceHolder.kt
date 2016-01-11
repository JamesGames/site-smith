package org.jamesgames.sitesmith.resources

/**
 * @author James Murphy
 */
interface ResourceHolder {
    fun getPath(nameOfResource: String, relativeTo: Page): String
}