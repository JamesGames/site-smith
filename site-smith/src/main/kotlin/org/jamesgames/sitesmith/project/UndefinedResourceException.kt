package org.jamesgames.sitesmith.project

/**
 * @author James Murphy
 */
class UndefinedResourceException : RuntimeException {
    constructor(resourceName: String) : super("Resource $resourceName is not defined.")
}