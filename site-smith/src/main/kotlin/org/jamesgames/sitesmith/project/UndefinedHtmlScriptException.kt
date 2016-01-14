package org.jamesgames.sitesmith.project

/**
 * @author James Murphy
 */
class UndefinedHtmlScriptException : RuntimeException {
    constructor(scriptName: String) : super("HtmlScript $scriptName is not defined.")
}