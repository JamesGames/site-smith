package org.jamesgames.sitesmith.resources

import java.io.File
import java.nio.file.Path

/**
 * @author James Murphy
 */
class SiteFile(private val file: File, private val uniqueName: String) : Resource {
    override fun getPath(): Path = file.toPath()
    override fun getName(): String = uniqueName
}