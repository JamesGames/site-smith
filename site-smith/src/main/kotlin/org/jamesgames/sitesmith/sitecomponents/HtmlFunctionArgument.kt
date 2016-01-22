package org.jamesgames.sitesmith.sitecomponents

import org.jamesgames.sitesmith.builder.SiteBuilder
import org.jamesgames.sitesmith.resources.Page
import java.nio.file.Paths
import kotlin.text.startsWith
import kotlin.text.substring

/**
 * @author James Murphy
 */
class HtmlFunctionArgument(val argument: String) {
    private val startOfResourceReference: String = "resource:";

    fun evaluate(pageFunctionCalledOn: Page, siteBuilder: SiteBuilder): String {
        return if (argument.startsWith(startOfResourceReference))
            pageFunctionCalledOn.getPath()
                    .relativize(Paths.get(siteBuilder.getRelativeResourcePath(
                            argument.substring(startOfResourceReference.length),
                            pageFunctionCalledOn))).toString()
        else
            argument;
    }
}