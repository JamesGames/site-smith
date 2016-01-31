package org.jamesgames.sitesmith.builder

import java.io.File

/**
 * @author James Murphy
 */

class InvalidSiteLayoutException : RuntimeException {
    constructor(siteLayoutFile: File, errorMessage: String) : super("Invalid site layout in file: ${siteLayoutFile.absolutePath}" +
            "${System.lineSeparator()}$errorMessage")
}