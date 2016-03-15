package org.jamesgames.sitesmith.builder

import org.jamesgames.sitesmith.sitecomponents.TextScript
import java.util.*

/**
 * @author James Murphy
 */
internal class TextScriptMap {
    private val nameToScript: HashMap<String, TextScript> = HashMap()

    fun addTextScript(script: TextScript) {
        nameToScript.put(script.name, script);
    }

    fun getTextScript(name: String): TextScript = nameToScript[name] ?: throw UndefinedTextScriptException(name)

    fun clearMap() {
        nameToScript.clear();
    }
}

