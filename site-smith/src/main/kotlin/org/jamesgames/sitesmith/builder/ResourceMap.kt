package org.jamesgames.sitesmith.builder

import org.jamesgames.sitesmith.resources.Page
import org.jamesgames.sitesmith.resources.Resource
import java.util.*

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
        // https://github.com/fhd/clostache/issues/43 replacing slash type as work around
        return relativeTo.getPath().toFile().parentFile.toPath().relativize(resource.getPath()).normalize().toString().replace('\\', '/')
    }

    fun getPages(): List<Page> {
        return nameToResource.filter { it.value is Page }.map { it.value as Page }.toList()
    }

    fun doesResourceExist(name: String): Boolean = nameToResource.containsKey(name)
}