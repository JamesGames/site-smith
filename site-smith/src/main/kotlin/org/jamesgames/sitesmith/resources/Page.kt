package org.jamesgames.sitesmith.resources

import org.jamesgames.sitesmith.builder.SiteComponentDatabase
import java.io.File
import java.nio.file.Path
import kotlin.collections.map

/**
 * @author James Murphy
 */
class Page(private val file: File,
           private val uniqueName: String,
           private val htmlScriptNames: List<String>) : Resource {

    override fun getPath(): Path = file.toPath()
    override fun getUniqueName(): String = uniqueName

    fun writePage(componentDb: SiteComponentDatabase) {
        val pageData = StringBuilder()
        htmlScriptNames.map { componentDb.appendHtmlFromScript(uniqueName, this, pageData) }
        file.writer().write(pageData.toString())
    }

}