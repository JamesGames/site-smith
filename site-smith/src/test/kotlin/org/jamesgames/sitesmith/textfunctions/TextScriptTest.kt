package org.jamesgames.sitesmith.textfunctions

/**
 * @author James Murphy
 */

import org.junit.Test
import kotlin.test.assertEquals

/**
 * @author James Murphy
 */
class TextScriptTest {

    val largeHelloFunction = "([x](str \"# Hello {{x}}\")[(str \"markdown\")])"
    val multipleLargeHelloNamesFunction = "([names](str " +
            "\"{{#names}}" +
            "# Hello {{.}}" + System.lineSeparator() + System.lineSeparator() +
            "{{/names}}\"" +
            ")[\"markdown\"])"
    val helloFunctionWhereArgIsString = "([\"x\"](str \"Hello {{x}}\"))"
    val helloNoArgsFunction = "([](str \"Hello, look no arguments\"))"
    val functionWithFunctionCallForArg = "([aFunction](str " +
            "\"{{#aFunction}}FunctionArgument{{/aFunction}}\"" +
            "))";

    // valid
    val emptyTestScript = "()"
    val emptyTestExpectedOutput = ""

    val helloNoArgsFuncName = "hello-no-args"
    val helloNoArgsCall = "($helloNoArgsFuncName)"
    val testScriptWithFunctionWithNoArgs = "($helloNoArgsCall)"
    val helloNoArgsExpectedOutput = "Hello, look no arguments${System.lineSeparator()}"

    val helloSomeStringArgFuncName = "hello"
    val helloWorldArgsCall = "($helloSomeStringArgFuncName \"world\")"
    val testScriptWithFunctionWithArgs = "($helloWorldArgsCall)"
    val helloWorldFuncExpectedOutput = "Hello world${System.lineSeparator()}"

    val helloLargeWorldFuncName = "hello-large"
    val helloLargeWorldCall = "($helloLargeWorldFuncName \"large world\")"
    val testScriptWithHelloWorldLargeCall = "($helloLargeWorldCall)"
    val helloLargeWorldExpectedOutput = "<h1>Hello large world</h1>${System.lineSeparator()}"

    val nonHtmlFunctionCall = "(str \"not an html function but still okay\" (System/lineSeparator))"
    val testScriptWithNonHtmlFunction = "($nonHtmlFunctionCall)"
    val nonHtmlFunctionCallExpectedOutput = "not an html function but still okay${System.lineSeparator()}"

    val helloVariousLargeNamesFuncName = "hello-large-names"
    val helloVariousLargeNamesCall = "($helloVariousLargeNamesFuncName [\"bob\" \"bill\" \"ben\"])"
    val testScriptWithVariousLargeNames = "($helloVariousLargeNamesCall)"
    // markdown converter Site Smith uses outputs just newline character, not the system newline string
    val helloVariousLargeNamesExpectedOutput = "<h1>Hello bob</h1>\n<h1>Hello bill</h1>\n<h1>Hello ben</h1>${System.lineSeparator()}"

    val helloWorldNonLiteralArg = "($helloSomeStringArgFuncName (str \"wor\" \"ld\" \"2\"))"
    val testScriptWithFuncWithNonLiteralArg = "($helloWorldNonLiteralArg)"
    val helloWorldNonLiteralArgExpectedOutput = "Hello world2${System.lineSeparator()}"

    val htmlFunctionPassingFuncName = "function-that-accepts-a-function"
    val htmlFuncPassingFuncAsArg = "($htmlFunctionPassingFuncName #(str \"$$$\" % \"$$$\"))"
    val testScriptWithFuncCallPassingFunc = "($htmlFuncPassingFuncAsArg)"
    val funcCallPassingFuncExpectedOutput = "$$\$FunctionArgument$$$${System.lineSeparator()}"

    val testScriptWithMultipleFunctions = "(" + helloNoArgsCall +
            helloWorldArgsCall +
            helloLargeWorldCall +
            nonHtmlFunctionCall +
            helloVariousLargeNamesCall + ")"
    val multipleFuncsExpectedOutput = helloNoArgsExpectedOutput +
            helloWorldFuncExpectedOutput +
            helloLargeWorldExpectedOutput +
            nonHtmlFunctionCallExpectedOutput +
            helloVariousLargeNamesExpectedOutput


    // invalid
    val scriptWhereFunctionNameIsAString = "((\"func1\" \"arg1\"))"
    val scriptWithVectorNotLists = "([func1 \"arg1\"])"


    init {
        TextFunction.defineFunction(helloLargeWorldFuncName, largeHelloFunction);
        TextFunction.defineFunction(helloSomeStringArgFuncName, helloFunctionWhereArgIsString);
        TextFunction.defineFunction(helloNoArgsFuncName, helloNoArgsFunction);
        TextFunction.defineFunction(helloVariousLargeNamesFuncName, multipleLargeHelloNamesFunction);
        TextFunction.defineFunction(htmlFunctionPassingFuncName, functionWithFunctionCallForArg);
    }


    @Test
    fun testIsScriptInValidFormat() {
        assertEquals(true, TextScript.isScriptInValidFormat(emptyTestScript))
        assertEquals(true, TextScript.isScriptInValidFormat(testScriptWithFunctionWithNoArgs))
        assertEquals(true, TextScript.isScriptInValidFormat(testScriptWithFunctionWithArgs))
        assertEquals(true, TextScript.isScriptInValidFormat(testScriptWithHelloWorldLargeCall))
        assertEquals(true, TextScript.isScriptInValidFormat(testScriptWithNonHtmlFunction))
        assertEquals(true, TextScript.isScriptInValidFormat(testScriptWithVariousLargeNames))
        assertEquals(true, TextScript.isScriptInValidFormat(testScriptWithFuncWithNonLiteralArg))
        assertEquals(true, TextScript.isScriptInValidFormat(testScriptWithFuncCallPassingFunc))
        assertEquals(true, TextScript.isScriptInValidFormat(testScriptWithMultipleFunctions))
        assertEquals(false, TextScript.isScriptInValidFormat(scriptWhereFunctionNameIsAString))
        assertEquals(false, TextScript.isScriptInValidFormat(scriptWithVectorNotLists))
    }

    @Test
    fun testInvokeTextFunctions() {
        val resourceConverter = { s: String -> s }

        assertEquals(emptyTestExpectedOutput,
                TextScript.executeScript(resourceConverter, emptyTestScript));
        assertEquals(helloNoArgsExpectedOutput,
                TextScript.executeScript(resourceConverter, testScriptWithFunctionWithNoArgs));
        assertEquals(helloWorldFuncExpectedOutput,
                TextScript.executeScript(resourceConverter, testScriptWithFunctionWithArgs));
        assertEquals(helloLargeWorldExpectedOutput,
                TextScript.executeScript(resourceConverter, testScriptWithHelloWorldLargeCall));
        assertEquals(nonHtmlFunctionCallExpectedOutput,
                TextScript.executeScript(resourceConverter, testScriptWithNonHtmlFunction));
        assertEquals(helloVariousLargeNamesExpectedOutput,
                TextScript.executeScript(resourceConverter, testScriptWithVariousLargeNames));
        assertEquals(helloWorldNonLiteralArgExpectedOutput,
                TextScript.executeScript(resourceConverter, testScriptWithFuncWithNonLiteralArg));
        assertEquals(funcCallPassingFuncExpectedOutput,
                TextScript.executeScript(resourceConverter, testScriptWithFuncCallPassingFunc));
        assertEquals(multipleFuncsExpectedOutput,
                TextScript.executeScript(resourceConverter, testScriptWithMultipleFunctions))
    }
}
