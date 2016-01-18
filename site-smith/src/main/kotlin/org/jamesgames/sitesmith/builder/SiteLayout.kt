package org.jamesgames.sitesmith.builder


/**
 * @author James Murphy
 */
class SiteLayout(val root: SiteLayout.DirectoryInfo) {
    data class PageInfo(val uniqueName: String, val fileName: String, val templateNamesForPage: List<String>)

    data class ResourceInfo(val uniqueName: String, val fileName: String)

    data class DirectoryInfo(val name: String,
                             val pages: List<PageInfo>,
                             val resources: List<ResourceInfo>,
                             val directories: List<DirectoryInfo>)
}

