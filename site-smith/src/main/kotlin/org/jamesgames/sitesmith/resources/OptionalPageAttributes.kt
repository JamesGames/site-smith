package org.jamesgames.sitesmith.resources

/**
 * @author James Murphy
 */
data class OptionalPageAttributes(val favicon: String?, val clientScripts: List<String>?,
                                  val additionalCssFiles: List<String>?)