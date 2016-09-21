package org.jamesgames.sitesmith.textfunctions

/**
 * @author James Murphy
 */

import org.junit.Test
import java.util.*
import kotlin.test.assertEquals

/**
 * @author James Murphy
 */
class TextScriptInterfaceTest {

    val largeHelloFunction = "[x](str \"# Hello {{x}}\")[(str \"markdown\")]"
    val multipleLargeHelloNamesFunction = "[names](str " +
            "\"{{#names}}" +
            "# Hello {{.}}" + System.lineSeparator() + System.lineSeparator() +
            "{{/names}}\"" +
            ")[\"markdown\"]"
    val helloFunctionWhereArgIsString = "[x](str \"Hello {{x}}\")"
    val helloNoArgsFunction = "[](str \"Hello, look no arguments\")"
    val functionWithFunctionCallForArg = "[aFunction](str " +
            "\"{{#aFunction}}FunctionArgument{{/aFunction}}\"" +
            ")"

    // valid
    val emptyTestScript = ""
    val emptyTestExpectedOutput = ""

    val helloNoArgsFuncName = "hello-no-args"
    val helloNoArgsCall = "(func/$helloNoArgsFuncName)"
    val testScriptWithFunctionWithNoArgs = "$helloNoArgsCall"
    val helloNoArgsExpectedOutput = "Hello, look no arguments${System.lineSeparator()}"

    val helloSomeStringArgFuncName = "hello"
    val helloWorldArgsCall = "(func/$helloSomeStringArgFuncName \"world\")"
    val testScriptWithFunctionWithArgs = "$helloWorldArgsCall"
    val helloWorldFuncExpectedOutput = "Hello world${System.lineSeparator()}"

    val helloLargeWorldFuncName = "hello-large"
    val helloLargeWorldCall = "(func/$helloLargeWorldFuncName \"large world\")"
    val testScriptWithHelloWorldLargeCall = "$helloLargeWorldCall"
    val helloLargeWorldExpectedOutput = "<h1><a href=\"#hello-large-world\" name=\"hello-large-world\">Hello large world</a></h1>${System.lineSeparator()}"

    val nonTextFunctionCall = "(str \"not an text function but still okay\" (System/lineSeparator))"
    val testScriptWithNonTextFunction = "$nonTextFunctionCall"
    val nonTextFunctionCallExpectedOutput = "not an text function but still okay${System.lineSeparator()}"

    val helloVariousLargeNamesFuncName = "hello-large-names"
    val helloVariousLargeNamesCall = "(func/$helloVariousLargeNamesFuncName [\"bob\" \"bill\" \"ben\"])"
    val testScriptWithVariousLargeNames = "$helloVariousLargeNamesCall"
    // markdown converter Site Smith uses outputs just newline character, not the system newline string
    val helloVariousLargeNamesExpectedOutput = "<h1><a href=\"#hello-bob\" name=\"hello-bob\">Hello bob</a></h1>${System.lineSeparator()}" +
            "<h1><a href=\"#hello-bill\" name=\"hello-bill\">Hello bill</a></h1>${System.lineSeparator()}" +
            "<h1><a href=\"#hello-ben\" name=\"hello-ben\">Hello ben</a></h1>${System.lineSeparator()}"
    val helloWorldNonLiteralArg = "(func/$helloSomeStringArgFuncName (str \"wor\" \"ld\" \"2\"))"
    val testScriptWithFuncWithNonLiteralArg = "$helloWorldNonLiteralArg"
    val helloWorldNonLiteralArgExpectedOutput = "Hello world2${System.lineSeparator()}"

    val textFunctionPassingFuncName = "function-that-accepts-a-function"
    val textFuncPassingFuncAsArg = "(func/$textFunctionPassingFuncName #(str \"$$$\" % \"$$$\"))"
    val testScriptWithFuncCallPassingFunc = "$textFuncPassingFuncAsArg"
    val funcCallPassingFuncExpectedOutput = "$$\$FunctionArgument$$$${System.lineSeparator()}"

    val textFunctionPassesClojureValue = "[x y](str x \"{{y}}\")"
    val textFunctionPassesClojureValueName = "accepts-clojure-value-function"
    val textFunctionPassesClojureValueCall = "(func/$textFunctionPassesClojureValueName 42 (str \"test\" 42))"
    val testScriptForFuncPassesClojureValue = "$textFunctionPassesClojureValueCall"
    val testScriptPassesClojureValueExpectedOutput = "42test42${System.lineSeparator()}"

    val testScriptWithMultipleFunctions = helloNoArgsCall +
            helloWorldArgsCall +
            helloLargeWorldCall +
            nonTextFunctionCall +
            helloVariousLargeNamesCall
    val multipleFuncsExpectedOutput = helloNoArgsExpectedOutput + System.lineSeparator() +
            helloWorldFuncExpectedOutput + System.lineSeparator() +
            helloLargeWorldExpectedOutput + System.lineSeparator() +
            nonTextFunctionCallExpectedOutput + System.lineSeparator() +
            helloVariousLargeNamesExpectedOutput


    // invalid
    val scriptWhereFunctionNameIsAString = "(\"func1\" \"arg1\")"
    val scriptWithVectorNotLists = "[func1 \"arg1\"]"

    // page name
    val pageNameNotRelevantForTest = "N/A"


    init {
        TextFunctionInterface.defineFunction(helloLargeWorldFuncName, largeHelloFunction)
        TextFunctionInterface.defineFunction(helloSomeStringArgFuncName, helloFunctionWhereArgIsString)
        TextFunctionInterface.defineFunction(helloNoArgsFuncName, helloNoArgsFunction)
        TextFunctionInterface.defineFunction(helloVariousLargeNamesFuncName, multipleLargeHelloNamesFunction)
        TextFunctionInterface.defineFunction(textFunctionPassingFuncName, functionWithFunctionCallForArg)
        TextFunctionInterface.defineFunction(textFunctionPassesClojureValueName, textFunctionPassesClojureValue)
    }

    fun makeAllNewLinesEqual(input: String) =
            input.replace(System.lineSeparator(), "\n").trimEnd()


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
        val resourceNameToPathConverter = { s: String -> s }
        val namesList = HashSet<String>()

        assertEquals(makeAllNewLinesEqual(emptyTestExpectedOutput),
                makeAllNewLinesEqual(TextScriptInterface.executeScript(pageNameNotRelevantForTest,
                        resourceNameToPathConverter, emptyTestScript, namesList)))
        assertEquals(makeAllNewLinesEqual(helloNoArgsExpectedOutput),
                makeAllNewLinesEqual(TextScriptInterface.executeScript(pageNameNotRelevantForTest,
                        resourceNameToPathConverter, testScriptWithFunctionWithNoArgs, namesList)))
        assertEquals(makeAllNewLinesEqual(helloWorldFuncExpectedOutput),
                makeAllNewLinesEqual(TextScriptInterface.executeScript(pageNameNotRelevantForTest,
                        resourceNameToPathConverter, testScriptWithFunctionWithArgs, namesList)))
        assertEquals(makeAllNewLinesEqual(helloLargeWorldExpectedOutput),
                makeAllNewLinesEqual(TextScriptInterface.executeScript(pageNameNotRelevantForTest,
                        resourceNameToPathConverter, testScriptWithHelloWorldLargeCall, namesList)))
        assertEquals(makeAllNewLinesEqual(nonTextFunctionCallExpectedOutput),
                makeAllNewLinesEqual(TextScriptInterface.executeScript(pageNameNotRelevantForTest,
                        resourceNameToPathConverter, testScriptWithNonTextFunction, namesList)))
        assertEquals(makeAllNewLinesEqual(helloVariousLargeNamesExpectedOutput),
                makeAllNewLinesEqual(TextScriptInterface.executeScript(pageNameNotRelevantForTest,
                        resourceNameToPathConverter, testScriptWithVariousLargeNames, namesList)))
        assertEquals(makeAllNewLinesEqual(helloWorldNonLiteralArgExpectedOutput),
                makeAllNewLinesEqual(TextScriptInterface.executeScript(pageNameNotRelevantForTest,
                        resourceNameToPathConverter, testScriptWithFuncWithNonLiteralArg, namesList)))
        assertEquals(makeAllNewLinesEqual(funcCallPassingFuncExpectedOutput),
                makeAllNewLinesEqual(TextScriptInterface.executeScript(pageNameNotRelevantForTest,
                        resourceNameToPathConverter, testScriptWithFuncCallPassingFunc, namesList)))
        assertEquals(makeAllNewLinesEqual(multipleFuncsExpectedOutput),
                makeAllNewLinesEqual(TextScriptInterface.executeScript(pageNameNotRelevantForTest,
                        resourceNameToPathConverter, testScriptWithMultipleFunctions, namesList)))
        assertEquals(makeAllNewLinesEqual(testScriptPassesClojureValueExpectedOutput),
                makeAllNewLinesEqual(TextScriptInterface.executeScript(pageNameNotRelevantForTest,
                        resourceNameToPathConverter, testScriptForFuncPassesClojureValue, namesList)))
    }
}
