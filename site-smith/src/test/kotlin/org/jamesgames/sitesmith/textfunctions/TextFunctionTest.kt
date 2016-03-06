package org.jamesgames.sitesmith.textfunctions;


import org.junit.Test
import kotlin.test.assertEquals

/**
 * @author James Murphy
 */
class TextFunctionTest {

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
        assertEquals(true, TextFunction.isFunctionTextInValidFormat(completelyEmptyFunction));
        assertEquals(true, TextFunction.isFunctionTextInValidFormat(completelyEmptyFunctionWithEmptyOptions));
        assertEquals(true, TextFunction.isFunctionTextInValidFormat(emptyParamsButWithBody));
        assertEquals(true, TextFunction.isFunctionTextInValidFormat(emptyParamsButWithBodyAndEmptyOptions));
        assertEquals(true, TextFunction.isFunctionTextInValidFormat(emptyParamsButWithBodyAndMarkdownOption));
        assertEquals(true, TextFunction.isFunctionTextInValidFormat(emptyParamsButWithBodyAndMarkdownOptionAfterExpression));
        assertEquals(true, TextFunction.isFunctionTextInValidFormat(paramsWithEmptyBody));
        assertEquals(true, TextFunction.isFunctionTextInValidFormat(paramsWithEmptyBodyAndEmptyOptions));
        assertEquals(true, TextFunction.isFunctionTextInValidFormat(fullValidExample));
        assertEquals(true, TextFunction.isFunctionTextInValidFormat(paramsCanBeStrings));
        assertEquals(true, TextFunction.isFunctionTextInValidFormat(multipleLargeHelloNamesFunction));
        assertEquals(true, TextFunction.isFunctionTextInValidFormat(emptyParamsButWithBodyAndSymbolAsOption));

        assertEquals(false, TextFunction.isFunctionTextInValidFormat(invalidFormatNoEnclosingList));
        assertEquals(false, TextFunction.isFunctionTextInValidFormat(invalidFormatParamsNotVector));
        assertEquals(false, TextFunction.isFunctionTextInValidFormat(invalidFormatOptionsNotVector));
        assertEquals(false, TextFunction.isFunctionTextInValidFormat(paramsCantBeCollections));
    }


    @Test
    fun testAppendFunctionTextToMap() {
        // Test creating functions, including reusing the same name more than once, and more than
        // once in a row
        TextFunction.defineFunction("firstUniqueName", fullValidExample);
        TextFunction.defineFunction("firstUniqueName", fullValidExample);
        TextFunction.defineFunction("secondUniqueName", fullValidExample);
        TextFunction.defineFunction("firstUniqueName", fullValidExample);
    }
}