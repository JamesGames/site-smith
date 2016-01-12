package org.jamesgames.sitesmith.htmlfunctions

import org.jamesgames.sitesmith.project.Project
import org.jamesgames.sitesmith.resources.Page
import java.io.File
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import java.util.*
import kotlin.text.isEmpty

/**
 * @author James Murphy
 */
class Template(val name: String) : HtmlProducer {
    private val htmlFunctionCalls: ArrayList<HtmlFunctionCall> = ArrayList();
    private val templateDirectory: Path = Paths.get("/templates");

    init {
        val templateFile: File = File(templateDirectory.resolve(name).toUri())
        if (templateFile.exists() && templateFile.isFile) {
            Files.lines(templateFile.toPath())
                    .filter(String::isEmpty)
                    .map { toHtmlFunctionCall(it) }.forEach { htmlFunctionCalls.add(it) }
        }
    }

    private fun toHtmlFunctionCall(line: String): HtmlFunctionCall {
        val jString: java.lang.String = java.lang.String(line)
        val tokens: Array<String> = jString.split("|")
        val function: HtmlFunctionCall = HtmlFunctionCall(tokens[0])
        Arrays.stream(tokens)
                .skip(0)
                .map(::HtmlFunctionArgument)
                .forEach { function.addArgument(it) }
        return function;
    }

    override fun appendHtml(page: Page,
                            project: Project,
                            stringBuilder: StringBuilder) {
        htmlFunctionCalls.forEach {
            stringBuilder.append(it.appendHtml(page, project, stringBuilder))
        }
    }
}

