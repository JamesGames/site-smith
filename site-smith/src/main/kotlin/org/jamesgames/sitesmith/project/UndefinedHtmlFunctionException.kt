package org.jamesgames.sitesmith.project

/**
 * @author James Murphy
 */
class UndefinedHtmlFunctionException : RuntimeException {
    constructor(functionName: String) : super("HtmlFunction $functionName is not defined.")
}