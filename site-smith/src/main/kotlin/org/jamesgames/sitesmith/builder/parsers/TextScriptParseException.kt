package org.jamesgames.sitesmith.sitecomponents

/**
 * @author James Murphy
 */
class TextScriptParseException : RuntimeException {
    constructor(scriptName: String, errorMsg: String) :
    super("Failed to parse Text Script: $scriptName. $errorMsg")
}