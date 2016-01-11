package org.jamesgames.sitesmith.resources

/**
 * @author James Murphy
 */
interface Page : Resource, ResourceHolder {
    fun getHtml(): String;
}