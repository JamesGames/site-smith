package org.jamesgames.sitesmith.textfunctions

/**
 * @author James Murphy
 */

import org.junit.Test
import kotlin.test.assertEquals

/**
 * @author James Murphy
 */
class TextScriptInterfaceTest {

    val largeHelloFunction = "([x](str \"# Hello {{x}}\")[(str \"markdown\")])"
    val multipleLargeHelloNamesFunction = "([names](str " +
            "\"{{#names}}" +
            "# Hello {{.}}" + System.lineSeparator() + System.lineSeparator() +
            "{{/names}}\"" +
            ")[\"markdown\"])"
    val helloFunctionWhereArgIsString = "([x](str \"Hello {{x}}\"))"
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

    val nonTextFunctionCall = "(str \"not an text function but still okay\" (System/lineSeparator))"
    val testScriptWithNonTextFunction = "($nonTextFunctionCall)"
    val nonTextFunctionCallExpectedOutput = "not an text function but still okay${System.lineSeparator()}"

    val helloVariousLargeNamesFuncName = "hello-large-names"
    val helloVariousLargeNamesCall = "($helloVariousLargeNamesFuncName [\"bob\" \"bill\" \"ben\"])"
    val testScriptWithVariousLargeNames = "($helloVariousLargeNamesCall)"
    // markdown converter Site Smith uses outputs just newline character, not the system newline string
    val helloVariousLargeNamesExpectedOutput = "<h1>Hello bob</h1>\n<h1>Hello bill</h1>\n<h1>Hello ben</h1>${System.lineSeparator()}"

    val helloWorldNonLiteralArg = "($helloSomeStringArgFuncName (str \"wor\" \"ld\" \"2\"))"
    val testScriptWithFuncWithNonLiteralArg = "($helloWorldNonLiteralArg)"
    val helloWorldNonLiteralArgExpectedOutput = "Hello world2${System.lineSeparator()}"

    val textFunctionPassingFuncName = "function-that-accepts-a-function"
    val textFuncPassingFuncAsArg = "($textFunctionPassingFuncName #(str \"$$$\" % \"$$$\"))"
    val testScriptWithFuncCallPassingFunc = "($textFuncPassingFuncAsArg)"
    val funcCallPassingFuncExpectedOutput = "$$\$FunctionArgument$$$${System.lineSeparator()}"

    val textFunctionPassesClojureValue = "([x y](str x \"{{y}}\"))"
    val textFunctionPassesClojureValueName = "accepts-clojure-value-function"
    val textFunctionPassesClojureValueCall = "($textFunctionPassesClojureValueName 42 (str \"test\" 42))"
    val testScriptForFuncPassesClojureValue = "($textFunctionPassesClojureValueCall)"
    val testScriptPassesClojureValueExpectedOutput = "42test42${System.lineSeparator()}"

    val testScriptWithMultipleFunctions = "(" + helloNoArgsCall +
            helloWorldArgsCall +
            helloLargeWorldCall +
            nonTextFunctionCall +
            helloVariousLargeNamesCall + ")"
    val multipleFuncsExpectedOutput = helloNoArgsExpectedOutput +
            helloWorldFuncExpectedOutput +
            helloLargeWorldExpectedOutput +
            nonTextFunctionCallExpectedOutput +
            helloVariousLargeNamesExpectedOutput


    // invalid
    val scriptWhereFunctionNameIsAString = "((\"func1\" \"arg1\"))"
    val scriptWithVectorNotLists = "([func1 \"arg1\"])"


    init {
        TextFunctionInterface.defineFunction(helloLargeWorldFuncName, largeHelloFunction);
        TextFunctionInterface.defineFunction(helloSomeStringArgFuncName, helloFunctionWhereArgIsString);
        TextFunctionInterface.defineFunction(helloNoArgsFuncName, helloNoArgsFunction);
        TextFunctionInterface.defineFunction(helloVariousLargeNamesFuncName, multipleLargeHelloNamesFunction);
        TextFunctionInterface.defineFunction(textFunctionPassingFuncName, functionWithFunctionCallForArg);
        TextFunctionInterface.defineFunction(textFunctionPassesClojureValueName, textFunctionPassesClojureValue);
    }


    @Test
    fun testIsScriptInValidFormat() {
        assertEquals(true, TextScriptInterface.isScriptInValidFormat(emptyTestScript))
        assertEquals(true, TextScriptInterface.isScriptInValidFormat(testScriptWithFunctionWithNoArgs))
        assertEquals(true, TextScriptInterface.isScriptInValidFormat(testScriptWithFunctionWithArgs))
        assertEquals(true, TextScriptInterface.isScriptInValidFormat(testScriptWithHelloWorldLargeCall))
        assertEquals(true, TextScriptInterface.isScriptInValidFormat(testScriptWithNonTextFunction))
        assertEquals(true, TextScriptInterface.isScriptInValidFormat(testScriptWithVariousLargeNames))
        assertEquals(true, TextScriptInterface.isScriptInValidFormat(testScriptWithFuncWithNonLiteralArg))
        assertEquals(true, TextScriptInterface.isScriptInValidFormat(testScriptWithFuncCallPassingFunc))
        assertEquals(true, TextScriptInterface.isScriptInValidFormat(testScriptWithMultipleFunctions))
        assertEquals(true, TextScriptInterface.isScriptInValidFormat(testScriptForFuncPassesClojureValue))

        assertEquals(false, TextScriptInterface.isScriptInValidFormat(scriptWhereFunctionNameIsAString))
        assertEquals(false, TextScriptInterface.isScriptInValidFormat(scriptWithVectorNotLists))
    }

    @Test
    fun testInvokeTextFunctions() {
        val resourceConverter = { s: String -> s }

        assertEquals(emptyTestExpectedOutput,
                TextScriptInterface.executeScript(resourceConverter, emptyTestScript));
        assertEquals(helloNoArgsExpectedOutput,
                TextScriptInterface.executeScript(resourceConverter, testScriptWithFunctionWithNoArgs));
        assertEquals(helloWorldFuncExpectedOutput,
                TextScriptInterface.executeScript(resourceConverter, testScriptWithFunctionWithArgs));
        assertEquals(helloLargeWorldExpectedOutput,
                TextScriptInterface.executeScript(resourceConverter, testScriptWithHelloWorldLargeCall));
        assertEquals(nonTextFunctionCallExpectedOutput,
                TextScriptInterface.executeScript(resourceConverter, testScriptWithNonTextFunction));
        assertEquals(helloVariousLargeNamesExpectedOutput,
                TextScriptInterface.executeScript(resourceConverter, testScriptWithVariousLargeNames));
        assertEquals(helloWorldNonLiteralArgExpectedOutput,
                TextScriptInterface.executeScript(resourceConverter, testScriptWithFuncWithNonLiteralArg));
        assertEquals(funcCallPassingFuncExpectedOutput,
                TextScriptInterface.executeScript(resourceConverter, testScriptWithFuncCallPassingFunc));
        assertEquals(multipleFuncsExpectedOutput,
                TextScriptInterface.executeScript(resourceConverter, testScriptWithMultipleFunctions))
        assertEquals(testScriptPassesClojureValueExpectedOutput,
                TextScriptInterface.executeScript(resourceConverter, testScriptForFuncPassesClojureValue))
    }
}
