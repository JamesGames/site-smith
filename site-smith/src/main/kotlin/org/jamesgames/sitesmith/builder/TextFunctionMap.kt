package org.jamesgames.sitesmith.builder

import org.jamesgames.sitesmith.sitecomponents.TextFunction
import org.jamesgames.sitesmith.textfunctions.TextFunctionInterface
import java.util.*

/**
 * @author James Murphy
 */
internal class TextFunctionMap {
    private val nameToFunction: HashMap<String, TextFunction> = HashMap()

    fun addTextFunction(function: TextFunction) {
        nameToFunction.put(function.name, function);
        TextFunctionInterface.defineFunction(
                function.name,
                function.functionText)
    }

    fun clearMap() {
        nameToFunction.clear();
    }
}