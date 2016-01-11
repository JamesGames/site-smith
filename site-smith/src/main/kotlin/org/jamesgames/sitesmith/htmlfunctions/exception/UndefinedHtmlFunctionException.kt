package org.jamesgames.sitesmith.htmlfunctions.exception

/**
 * @author James Murphy
 */
class UndefinedHtmlFunctionException : RuntimeException {
    constructor(functionName: String) : super("HtmlFunction $functionName is not defined.")
}