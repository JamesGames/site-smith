package org.jamesgames.sitesmith.textfunctions;


import org.junit.Test
import kotlin.test.assertEquals

/**
 * @author James Murphy
 */
class TextFunctionInterfaceTest {

    // Valid
    val completelyEmptyFunction = "([]())";
    val completelyEmptyFunctionWithEmptyOptions = "([]()[])";
    val emptyParamsButWithBody = "([](str \"test\"))";
    val emptyParamsButWithBodyAndEmptyOptions = "([](str \"test\")[])";
    val emptyParamsButWithBodyAndMarkdownOption = "([](str \"test\")[\"markdown\"])";
    val emptyParamsButWithBodyAndMarkdownOptionAfterExpression = "([](str \"test\")[(str \"markdown\")])";
    val paramsWithEmptyBody = "([x]())";
    val paramsWithEmptyBodyAndEmptyOptions = "([x]()[])";
    val fullValidExample = "([x](str \"Hello {{x}}\")[\"markdown\"])";
    val paramsCanBeStrings = "([\"x\"](str \"test\"))";
    val multipleLargeHelloNamesFunction = "([names](str " +
            "\"{{#names}}" +
            "# Hello {{.}}" +
            "{{/names}}\"" +
            ")[\"markdown\"])";
    val emptyParamsButWithBodyAndSymbolAsOption = "([](str \"test\")[someOptionToBeResolved])";
    // Invalid
    val invalidFormatNoEnclosingList = "[x](str \"Hello {{x}}\")[\"markdown\"])";
    val invalidFormatParamsNotVector = "((x)(str \"Hello {{x}}\")[\"markdown\"])";
    val invalidFormatOptionsNotVector = "((x)(str \"Hello {{x}}\")(\"markdown\"))";
    val paramsCantBeCollections = "([(\"x\")](str \"test\"))";

    @Test
    fun testValidFunctionFormat() {
        assertEquals(true, TextFunctionInterface.isFunctionTextInValidFormat(completelyEmptyFunction));
        assertEquals(true, TextFunctionInterface.isFunctionTextInValidFormat(completelyEmptyFunctionWithEmptyOptions));
        assertEquals(true, TextFunctionInterface.isFunctionTextInValidFormat(emptyParamsButWithBody));
        assertEquals(true, TextFunctionInterface.isFunctionTextInValidFormat(emptyParamsButWithBodyAndEmptyOptions));
        assertEquals(true, TextFunctionInterface.isFunctionTextInValidFormat(emptyParamsButWithBodyAndMarkdownOption));
        assertEquals(true, TextFunctionInterface.isFunctionTextInValidFormat(emptyParamsButWithBodyAndMarkdownOptionAfterExpression));
        assertEquals(true, TextFunctionInterface.isFunctionTextInValidFormat(paramsWithEmptyBody));
        assertEquals(true, TextFunctionInterface.isFunctionTextInValidFormat(paramsWithEmptyBodyAndEmptyOptions));
        assertEquals(true, TextFunctionInterface.isFunctionTextInValidFormat(fullValidExample));
        assertEquals(true, TextFunctionInterface.isFunctionTextInValidFormat(paramsCanBeStrings));
        assertEquals(true, TextFunctionInterface.isFunctionTextInValidFormat(multipleLargeHelloNamesFunction));
        assertEquals(true, TextFunctionInterface.isFunctionTextInValidFormat(emptyParamsButWithBodyAndSymbolAsOption));

        assertEquals(false, TextFunctionInterface.isFunctionTextInValidFormat(invalidFormatNoEnclosingList));
        assertEquals(false, TextFunctionInterface.isFunctionTextInValidFormat(invalidFormatParamsNotVector));
        assertEquals(false, TextFunctionInterface.isFunctionTextInValidFormat(invalidFormatOptionsNotVector));
        assertEquals(false, TextFunctionInterface.isFunctionTextInValidFormat(paramsCantBeCollections));
    }


    @Test
    fun testAppendFunctionTextToMap() {
        // Test creating functions, including reusing the same name more than once, and more than
        // once in a row
        TextFunctionInterface.defineFunction("firstUniqueName", fullValidExample);
        TextFunctionInterface.defineFunction("firstUniqueName", fullValidExample);
        TextFunctionInterface.defineFunction("secondUniqueName", fullValidExample);
        TextFunctionInterface.defineFunction("firstUniqueName", fullValidExample);
    }
}