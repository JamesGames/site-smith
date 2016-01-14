package org.jamesgames.sitesmith.htmlfunctions

/**
 * @author James Murphy
 */
class HtmlScriptParseException : RuntimeException {
    constructor(scriptName: String, errorMsg: String) :
    super("Failed to parse Html script $scriptName. $errorMsg")
}