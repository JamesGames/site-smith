package org.jamesgames.sitesmith.resources

/**
 * @author James Murphy
 */
interface Page : Resource {
    fun getHtml(): String;
}