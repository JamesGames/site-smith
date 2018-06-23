package org.jamesgames.sitesmith.builder

/**
 * @author James Murphy
 */
class ScriptExecutionException : RuntimeException {
    constructor(pageUniqueName: String, scriptName: String, errorMsg: String, cause: Exception) :
            super("Execution exception for page: $pageUniqueName with script: $scriptName. Message:${System.lineSeparator()}$errorMsg", cause)
}