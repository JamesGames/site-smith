package org.jamesgames.sitesmith.builder.buildersteps

import kotlin.text.isEmpty

/**
 * @author James Murphy
 */
internal interface BuildHelper {
    fun getErrorMessages(): String = ""
    fun getWarningMessages(): String = ""
    fun applyBuildAction()
    fun buildHelperPassed(): Boolean = getErrorMessages().isEmpty()
}