package org.jamesgames.sitesmith.builder

/**
 * @author James Murphy
 */
internal class UndefinedHtmlScriptException : RuntimeException {
    constructor(scriptName: String) : super("HtmlScript $scriptName is not defined.")
}