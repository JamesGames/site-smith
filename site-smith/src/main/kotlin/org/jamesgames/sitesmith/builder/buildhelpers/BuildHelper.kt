package org.jamesgames.sitesmith.builder.buildhelpers

/**
 * @author James Murphy
 */
internal interface BuildHelper {
    fun getErrorMessages(): String = ""
    fun getWarningMessages(): String = ""
    fun applyBuildAction()
    fun buildHelperPassed(): Boolean = getErrorMessages().isEmpty()
}