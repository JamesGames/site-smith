package org.jamesgames.sitesmith.project

import org.jamesgames.sitesmith.htmlfunctions.HtmlScript
import java.util.*

/**
 * @author James Murphy
 */
class HtmlScriptMap {
    private val nameToScript: HashMap<String, HtmlScript> = HashMap()

    fun addHtmlScript(script: HtmlScript) {
        nameToScript.put(script.name, script);
    }

    fun getHtmlScript(name: String): HtmlScript = nameToScript[name] ?: throw UndefinedHtmlScriptException(name)

    fun clearMap() {
        nameToScript.clear();
    }
}

