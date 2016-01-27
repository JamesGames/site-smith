package org.jamesgames.sitesmith.builder

/**
 * @author James Murphy
 */
class UndefinedHtmlFunctionException : RuntimeException {
    constructor(functionName: String) : super("HtmlFunction $functionName is not defined.")
}