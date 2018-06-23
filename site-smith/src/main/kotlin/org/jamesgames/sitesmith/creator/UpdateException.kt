package org.jamesgames.sitesmith.creator

/**
 * @author James Murphy
 */
class UpdateException(reason: String) : RuntimeException("Cannot update file: $reason")