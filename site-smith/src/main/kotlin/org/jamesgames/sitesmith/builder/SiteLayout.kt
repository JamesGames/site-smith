package org.jamesgames.sitesmith.builder

import java.util.*


/**
 * @author James Murphy
 */
internal class SiteLayout(val root: SiteLayout.DirectoryInfo,
                          val globalCssFileName: String?,
                          val specifyResourcesByDirectory: Boolean) {

    data class PageInfo(val fileName: String,
                        val uniqueName: String? = fileName,
                        val pageTitle: String,
                        val additionalCssFiles: List<String>? = ArrayList(),
                        val templateNamesForPage: List<String>? = ArrayList())

    data class ResourceInfo(val fileName: String,
                            val uniqueName: String? = fileName,
                            val fileNameInResourceDir: String? = fileName)

    data class DirectoryInfo(val name: String,
                             val pages: List<PageInfo>? = ArrayList(),
                             val resources: List<ResourceInfo>? = ArrayList(),
                             val directories: List<DirectoryInfo>? = ArrayList())
}

