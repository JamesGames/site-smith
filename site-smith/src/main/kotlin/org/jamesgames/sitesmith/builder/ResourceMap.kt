package org.jamesgames.sitesmith.builder

import org.jamesgames.sitesmith.resources.Page
import org.jamesgames.sitesmith.resources.Resource
import java.util.*
import kotlin.collections.filter
import kotlin.collections.map
import kotlin.collections.toList

/**
 * @author James Murphy
 */
internal class ResourceMap {

    private val nameToResource: HashMap<String, Resource> = HashMap()

    fun addResource(name: String, resource: Resource) {
        nameToResource.put(name, resource);
    }

    fun getRelativeResourcePath(name: String, relativeTo: Page): String {
        val resource = nameToResource[name] ?: throw UndefinedResourceException(name)
        return relativeTo.getPath().relativize(resource.getPath()).normalize().toString()
    }

    fun getPages(): List<Page> {
        return nameToResource.filter { it.value is Page }.map { it.value as Page }.toList()
    }

    fun doesResourceExist(name: String): Boolean = nameToResource.containsKey(name)
}