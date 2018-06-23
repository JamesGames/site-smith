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
        nameToResource.put(name, resource)
    }

    fun getRelativeResourcePath(name: String, relativeTo: Page): String {
        val resource = nameToResource[name] ?: throw UndefinedResourceException(name)
        val isIndexOrDefaultFile = resource.getPath().toFile().name.let {
            it.equals("index.html") || it.equals("default.html")
        }
        var resourcePath = if (isIndexOrDefaultFile) resource.getPath().toFile().parentFile.toPath() else resource.getPath()
        val relativePathToResource = relativeTo.getPath().toFile().parentFile.toPath().relativize(resourcePath).normalize()
        // https://github.com/fhd/clostache/issues/43 replacing slash type as work around
        relativePathToResource.toString().replace('\\', '/').let {
            // Empty when relative path to the same directory
            if (it.isEmpty())
            // Force to html rel path to same directory format
                return "./"
            // Always force trailing slash. Since using relative paths, ../ and .. always resolves to directoryName/
            // and never just directoryName, so keep using a trailing slash in cases where one isn't made by the
            // relative path. I suppose I could generate an ht access with a rewrite rule to remove
            // trailing slashes for any users that don't want trailing slashes (if that's even possible?)
            return if (isIndexOrDefaultFile && !it.endsWith("/")) it + '/' else it
        }
    }

    fun getPages(): List<Page> = nameToResource.filter { it.value is Page }.map { it.value as Page }.toList()

    fun doesResourceExist(name: String): Boolean = nameToResource.containsKey(name)

    fun getAllResourceNames(): HashSet<String> = nameToResource.keys.toHashSet()
}