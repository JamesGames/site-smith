package org.jamesgames.sitesmith.builder

/**
 * @author James Murphy
 */
class UndefinedHtmlScriptException : RuntimeException {
    constructor(scriptName: String) : super("HtmlScript $scriptName is not defined.")
}