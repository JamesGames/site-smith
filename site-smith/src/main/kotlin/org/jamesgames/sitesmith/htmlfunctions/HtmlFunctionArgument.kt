package org.jamesgames.sitesmith.htmlfunctions

import org.jamesgames.sitesmith.project.ResourceMap
import org.jamesgames.sitesmith.resources.Page
import java.nio.file.Paths
import kotlin.text.startsWith
import kotlin.text.substring

/**
 * @author James Murphy
 */
class HtmlFunctionArgument(val argument: String) {
    private val startOfResourceReference: String = "resource:";

    fun evaluate(pageFunctionCalledOn: Page, resourceMap: ResourceMap): String {
        if (argument.startsWith(startOfResourceReference)) {
            val restOfArgument = argument.substring(startOfResourceReference.length)
            return Paths.get(pageFunctionCalledOn.getPath())
                    .relativize(Paths.get(resourceMap.getResourcePath(restOfArgument, pageFunctionCalledOn))).toString()
        } else {
            return argument;
        }
    }
}