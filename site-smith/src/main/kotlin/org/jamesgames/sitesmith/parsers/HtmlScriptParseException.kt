package org.jamesgames.sitesmith.sitecomponents

/**
 * @author James Murphy
 */
class HtmlScriptParseException : RuntimeException {
    constructor(scriptName: String, errorMsg: String) :
    super("Failed to parse Html script $scriptName. $errorMsg")
}