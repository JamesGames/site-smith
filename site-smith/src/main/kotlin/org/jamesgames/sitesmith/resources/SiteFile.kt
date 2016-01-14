package org.jamesgames.sitesmith.resources

import java.io.File

/**
 * @author James Murphy
 */
class SiteFile(private val file: File) : Resource {
    override fun getPath(): String = file.absolutePath
    override fun getName(): String = file.name
}