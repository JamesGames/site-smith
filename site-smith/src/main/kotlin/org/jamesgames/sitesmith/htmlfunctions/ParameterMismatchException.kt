package org.jamesgames.sitesmith.htmlfunctions

/**
 * @author James Murphy
 */
class ParameterMismatchException : RuntimeException {
    constructor(functionName: String, expectedArgs: Int, actualArgs: Int) :
    super("Failed to call Html function $functionName, expected $expectedArgs arguments, but received $actualArgs.")
}