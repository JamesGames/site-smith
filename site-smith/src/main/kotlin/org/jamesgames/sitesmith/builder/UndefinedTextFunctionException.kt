package org.jamesgames.sitesmith.builder

/**
 * @author James Murphy
 */
class UndefinedTextFunctionException : RuntimeException {
    constructor(functionName: String) : super("Text Function $functionName is not defined.")
}