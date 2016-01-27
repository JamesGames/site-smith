package org.jamesgames.sitesmith.builder

/**
 * @author James Murphy
 */
class UndefinedResourceException : RuntimeException {
    constructor(resourceName: String) : super("Resource $resourceName is not defined.")
}