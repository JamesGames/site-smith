package org.jamesgames.sitesmith.builder

import org.jamesgames.sitesmith.resources.Page
import org.jamesgames.sitesmith.resources.Resource
import java.util.*

/**
 * @author James Murphy
 */
internal class ResourceMap {

    private val nameToFunction: HashMap<String, Resource> = HashMap()

    fun addResource(name: String, resource: Resource) {
        nameToFunction.put(name, resource);
    }

    fun getRelativeResourcePath(name: String, relativeTo: Page): String {
        val resource = nameToFunction[name] ?: throw UndefinedResourceException(name)
        return relativeTo.getPath().relativize(resource.getPath()).normalize().toString()
    }
}