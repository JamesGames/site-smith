package org.jamesgames.sitesmith.resources

import java.nio.file.Path

/**
 * @author James Murphy
 */
interface ResourceHolder {
    fun getPath(): Path
    fun addResource(resource: Resource)
}