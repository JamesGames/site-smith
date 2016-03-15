package org.jamesgames.sitesmith.sitecomponents

/**
 * @author James Murphy
 */
class TextFunctionParseException : RuntimeException {
    constructor(functionName: String, errorMsg: String) :
    super("Failed to parse Text Function: $functionName. $errorMsg")
}