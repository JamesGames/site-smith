package org.jamesgames.sitesmith.creator

/**
 * @author James Murphy
 */
class ModificationException(reason: String) : RuntimeException("Cannot modify file: $reason")