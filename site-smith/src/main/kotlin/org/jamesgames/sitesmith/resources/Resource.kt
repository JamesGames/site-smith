package org.jamesgames.sitesmith.resources

import java.nio.file.Path

/**
 * @author James Murphy
 */
interface Resource {
    fun getName(): String
    fun getPath(): Path
}