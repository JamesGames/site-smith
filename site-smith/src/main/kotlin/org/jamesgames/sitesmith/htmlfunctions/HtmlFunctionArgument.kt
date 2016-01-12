package org.jamesgames.sitesmith.htmlfunctions

import org.jamesgames.sitesmith.project.Project
import org.jamesgames.sitesmith.resources.Page
import java.nio.file.Paths
import kotlin.text.startsWith
import kotlin.text.substring

/**
 * @author James Murphy
 */
class HtmlFunctionArgument(val argument: String) {
    private val startOfResourceReference: String = "resource:";

    fun evaluate(pageFunctionCalledOn: Page, project: Project): String {
        return if (argument.startsWith(startOfResourceReference))
            Paths.get(pageFunctionCalledOn.getPath())
                    .relativize(Paths.get(project.getRelativeResourcePath(
                            argument.substring(startOfResourceReference.length),
                            pageFunctionCalledOn))).toString()
        else
            argument;
    }
}