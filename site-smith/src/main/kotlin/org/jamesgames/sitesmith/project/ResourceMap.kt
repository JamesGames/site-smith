package org.jamesgames.sitesmith.project

import org.jamesgames.sitesmith.resources.Page
import org.jamesgames.sitesmith.resources.Resource
import java.nio.file.Paths
import java.util.*

/**
 * @author James Murphy
 */
class ResourceMap {

    private val nameToFunction: HashMap<String, Resource> = HashMap()

    fun addResource(name: String, resource: Resource) {
        nameToFunction.put(name, resource);
    }

    fun getRelativeResourcePath(name: String, relativeTo: Page): String {
        val resource = nameToFunction[name] ?: throw UndefinedResourceException(name)
        return Paths.get(relativeTo.getPath()).relativize(Paths.get(resource.getPath())).normalize().toString()
    }
}