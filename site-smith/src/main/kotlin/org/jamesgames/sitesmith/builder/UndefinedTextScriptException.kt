package org.jamesgames.sitesmith.builder

/**
 * @author James Murphy
 */
class UndefinedTextScriptException : RuntimeException {
    constructor(scriptName: String) : super("TextScript $scriptName is not defined.")
}