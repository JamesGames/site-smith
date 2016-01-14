package org.jamesgames.sitesmith.htmlfunctions

/**
 * @author James Murphy
 */
class HtmlFunctionParseException : RuntimeException {
    constructor(functionName: String, errorMsg: String) :
    super("Failed to parse Html function $functionName. $errorMsg")
}