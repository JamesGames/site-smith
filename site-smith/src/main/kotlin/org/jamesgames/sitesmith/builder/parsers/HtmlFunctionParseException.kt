package org.jamesgames.sitesmith.sitecomponents

/**
 * @author James Murphy
 */
internal class HtmlFunctionParseException : RuntimeException {
    constructor(functionName: String, errorMsg: String) :
    super("Failed to parse Html function $functionName. $errorMsg")
}