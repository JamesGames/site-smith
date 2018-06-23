package org.jamesgames.sitesmith.resources

import java.nio.file.Path

/**
 * @author James Murphy
 */
interface Resource {
    companion object {
        val startOfExternalFile = "external:"
    }

    fun getUniqueName(): String
    fun getPath(): Path
}