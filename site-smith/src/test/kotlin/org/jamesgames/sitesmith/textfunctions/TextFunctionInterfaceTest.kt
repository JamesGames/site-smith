package org.jamesgames.sitesmith.textfunctions


import clojure.lang.RT
import org.junit.Test
import kotlin.test.assertEquals

/**
 * @author James Murphy
 */
class TextFunctionInterfaceTest {

    // Valid
    val completelyEmptyFunction = "[]()"
    val completelyEmptyFunctionWithEmptyOptions = "[]()[]"
    val emptyParamsButWithBody = "[](str \"test\")"
    val emptyParamsButWithBodyAndEmptyOptions = "[](str \"test\")[]"
    val emptyParamsButWithBodyAndMarkdownOption = "[](str \"test\")[markdown]"
    val emptyParamsButWithBodyAndMarkdownOptionAfterExpression = "[](str \"test\")[markdown]"
    val paramsWithEmptyBody = "[x]()"
    val paramsWithEmptyBodyAndEmptyOptions = "[x]()[]"
    val fullValidExample = "[x](str \"Hello {{x}}\")[markdown]"
    val multipleLargeHelloNamesFunction = "[names](str " +
            "\"{{#names}}" +
            "# Hello {{.}}" +
            "{{/names}}\"" +
            ")[markdown]"
    val emptyParamsButWithBodyAndSymbolAsOption = "[](str \"test\")[someOptionToBeResolved]"
    // Invalid
    val invalidFormatParamsNotVector = "(x)(str \"Hello {{x}}\")[markdown]"
    val invalidFormatOptionsNotVector = "[x](str \"Hello {{x}}\")(markdown)"
    val paramsCantBeCollections = "[(x)](str \"test\")"
    val paramsCantBeStrings = "[\"x\"](str \"test\")"

    @Test
    fun testValidFunctionFormat() {
        assertEquals(true, TextFunctionInterface.isFunctionTextInValidFormat(completelyEmptyFunction))
        assertEquals(true, TextFunctionInterface.isFunctionTextInValidFormat(completelyEmptyFunctionWithEmptyOptions))
        assertEquals(true, TextFunctionInterface.isFunctionTextInValidFormat(emptyParamsButWithBody))
        assertEquals(true, TextFunctionInterface.isFunctionTextInValidFormat(emptyParamsButWithBodyAndEmptyOptions))
        assertEquals(true, TextFunctionInterface.isFunctionTextInValidFormat(emptyParamsButWithBodyAndMarkdownOption))
        assertEquals(true, TextFunctionInterface.isFunctionTextInValidFormat(emptyParamsButWithBodyAndMarkdownOptionAfterExpression))
        assertEquals(true, TextFunctionInterface.isFunctionTextInValidFormat(paramsWithEmptyBody))
        assertEquals(true, TextFunctionInterface.isFunctionTextInValidFormat(paramsWithEmptyBodyAndEmptyOptions))
        assertEquals(true, TextFunctionInterface.isFunctionTextInValidFormat(fullValidExample))
        assertEquals(true, TextFunctionInterface.isFunctionTextInValidFormat(multipleLargeHelloNamesFunction))
        assertEquals(true, TextFunctionInterface.isFunctionTextInValidFormat(emptyParamsButWithBodyAndSymbolAsOption))

        assertEquals(false, TextFunctionInterface.isFunctionTextInValidFormat(paramsCantBeStrings))
        assertEquals(false, TextFunctionInterface.isFunctionTextInValidFormat(invalidFormatParamsNotVector))
        assertEquals(false, TextFunctionInterface.isFunctionTextInValidFormat(invalidFormatOptionsNotVector))
        assertEquals(false, TextFunctionInterface.isFunctionTextInValidFormat(paramsCantBeCollections))
    }

    @Test
    fun testAppendFunctionTextToMap() {
        // Test creating functions, including reusing the same name more than once, and more than
        // once in a row
        TextFunctionInterface.defineFunction("firstUniqueName", fullValidExample)
        TextFunctionInterface.defineFunction("firstUniqueName", fullValidExample)
        TextFunctionInterface.defineFunction("secondUniqueName", fullValidExample)
        TextFunctionInterface.defineFunction("firstUniqueName", fullValidExample)
    }

    @Test
    fun testNamespaceForFunction() {
        TextFunctionInterface.defineFunction("outputNamespace", "[](str \"called-correctly\")[]")
        // If we can call it via first param here that specifies the name space, then the function
        // was definitely given the correct namespace
        val functionResults = RT.`var`("func", "outputNamespace").invoke(null)
        assertEquals("called-correctly", functionResults)
    }
}