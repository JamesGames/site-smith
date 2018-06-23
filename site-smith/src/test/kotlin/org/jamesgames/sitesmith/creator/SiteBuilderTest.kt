package org.jamesgames.sitesmith.creator

import org.apache.commons.io.FileUtils
import org.jamesgames.sitesmith.project.Project
import org.junit.After
import org.junit.Before
import org.junit.Test
import java.io.File
import kotlin.test.assertEquals
import kotlin.test.assertNull
import kotlin.test.assertTrue

/**
 * @author James Murphy
 */
class SiteBuilderTest {
    private var onlyRootLayoutProject: Project? = null
    private var justAPageLayout: File? = null
    private var siteCreator: SiteCreator? = null
    private var newTempProjectLocationFile: File? = null

    @Before
    fun setUp() {
        val projectDirToCopyFrom = this.javaClass.getResource("/test-project").file!!
        val newTempProjectLocation = "/test-project-temp"
        val projectDirToCopyFromFile = File(projectDirToCopyFrom)
        newTempProjectLocationFile = File(projectDirToCopyFromFile.parent + newTempProjectLocation)

        if (!newTempProjectLocationFile!!.exists()) {
            newTempProjectLocationFile!!.mkdir()
        }
        FileUtils.cleanDirectory(newTempProjectLocationFile)
        FileUtils.copyDirectory(projectDirToCopyFromFile, newTempProjectLocationFile)

        justAPageLayout = newTempProjectLocationFile!!.listFiles().first { it.name == "justAPageLayout.json" }
        onlyRootLayoutProject = Project(newTempProjectLocationFile!!, justAPageLayout!!)

        val outputDir = onlyRootLayoutProject!!.outputDirectory
        // Verify copy of test project was copied
        assertTrue(onlyRootLayoutProject!!.buildSite())
        assertTrue(outputDir.exists())
        assertTrue(justAPageLayout!!.exists())
        assertTrue(fileAsString(justAPageLayout).isNotEmpty())

        siteCreator = SiteCreator(onlyRootLayoutProject!!.siteLayout, onlyRootLayoutProject!!.textFunctionDirectory,
                onlyRootLayoutProject!!.textScriptDirectory)
    }

    @After
    fun tearDown() {
        FileUtils.cleanDirectory(newTempProjectLocationFile)
    }

    private fun getFunctionFile(fileName: String) = onlyRootLayoutProject!!.textFunctionDirectory.listFiles()
            .first { it.name == fileName }

    private fun getScriptFile(fileName: String) = onlyRootLayoutProject!!.textScriptDirectory.listFiles()
            .first { it.name == fileName }

    private fun assertFunctionFileDoesNotExist(fileName: String) = assertNull(onlyRootLayoutProject!!.textFunctionDirectory.listFiles()
            .firstOrNull { it.name == fileName })

    private fun assertScriptFileDoesNotExist(fileName: String) = assertNull(onlyRootLayoutProject!!.textScriptDirectory.listFiles()
            .firstOrNull { it.name == fileName })

    @Test
    fun updateLayoutFile() {
        val newTextToUse = "new text for layout"

        var existingLayoutFileText = fileAsString(justAPageLayout)
        assertTrue(!existingLayoutFileText.equals(newTextToUse))

        siteCreator!!.updateLayoutFile(newTextToUse)

        var newLayoutFileText = fileAsString(justAPageLayout)
        assertTrue(newLayoutFileText.equals(newLayoutFileText))
    }

    @Test
    fun updateTextFunction() {
        var existingTextFunction = getFunctionFile("link.clj")
        val newTextToUse = "new text for function"

        var existingTextFunctionText = fileAsString(existingTextFunction)
        assertTrue(!existingTextFunctionText.equals(newTextToUse))

        siteCreator!!.updateTextFunction(newTextToUse, "link")

        var newTextFunctionText = fileAsString(existingTextFunction)
        assertTrue(newTextFunctionText.equals(newTextToUse))
    }

    @Test
    fun updateTextScript() {
        var existingTextScript = getScriptFile("exampleScript.clj")
        val newTextToUse = "new text for script"

        var existingTextScriptText = fileAsString(existingTextScript)
        assertTrue(!existingTextScriptText.equals(newTextToUse))

        siteCreator!!.updateTextScript(newTextToUse, "exampleScript")

        var newTextScriptText = fileAsString(existingTextScript)
        assertTrue(newTextScriptText.equals(newTextToUse))
    }

    @Test
    fun createNewTextFunction() {
        var functionNameToCreate = "someNewFunc"
        assertFunctionFileDoesNotExist(functionNameToCreate)

        var functionTextToUse = "some function text"
        siteCreator!!.createTextFunction(functionTextToUse, functionNameToCreate)

        var newlyCreatedFunction = onlyRootLayoutProject!!.textFunctionDirectory.listFiles()
                .first { it.name == functionNameToCreate + ".clj" }
        assertTrue(newlyCreatedFunction.exists())
        var functionTextActuallyUsed = fileAsString(newlyCreatedFunction)
        assertTrue(functionTextToUse.equals(functionTextActuallyUsed))
    }

    @Test
    fun createNewTextScript() {
        var scriptNameToCreate = "someNewScript"
        assertScriptFileDoesNotExist(scriptNameToCreate)

        var scriptTextToUse = "some script text"
        siteCreator!!.createTextScript(scriptTextToUse, scriptNameToCreate)

        var newlyCreatedScript = getScriptFile(scriptNameToCreate + ".clj")
        assertTrue(newlyCreatedScript.exists())
        var scriptTextActuallyUsed = fileAsString(newlyCreatedScript)
        assertTrue(scriptTextToUse.equals(scriptTextActuallyUsed))
    }

    @Test
    fun deleteTextFunction() {
        var existingFunction = getFunctionFile("link.clj")
        assertTrue(existingFunction.exists())

        siteCreator!!.deleteTextFunction("link")

        assertFunctionFileDoesNotExist("link.clj")
    }

    @Test
    fun deleteTextScript() {
        var existingScript = getScriptFile("exampleScript.clj")
        assertTrue(existingScript.exists())

        siteCreator!!.deleteTextScript("exampleScript")

        assertScriptFileDoesNotExist("exampleScript.clj")
    }

    @Test
    fun renameTextFunction() {
        var existingFunction = getFunctionFile("link.clj")
        assertTrue(existingFunction.exists())
        var originalFunctionText = fileAsString(existingFunction)

        siteCreator!!.renameTextFunction("link", "linkRenamed")

        assertFunctionFileDoesNotExist("link.clj")

        var newFunction = getFunctionFile("linkRenamed.clj")
        assertTrue(newFunction.exists())

        var newFunctionText = fileAsString(newFunction)
        assertEquals(originalFunctionText, newFunctionText)
    }

    @Test
    fun renameTextScript() {
        var existingScript = getScriptFile("exampleScript.clj")
        assertTrue(existingScript.exists())
        var originalScriptText = fileAsString(existingScript)

        siteCreator!!.renameTextScript("exampleScript", "exampleScriptRenamed")

        assertScriptFileDoesNotExist("exampleScript.clj")

        var newScript = getScriptFile("exampleScriptRenamed.clj")
        assertTrue(newScript.exists())

        var newScriptText = fileAsString(newScript)
        assertEquals(originalScriptText, newScriptText)
    }

    @Test(expected = UpdateException::class)
    fun renameTextFunction_ExceptionOnRenamingToExistingFile() {
        var existingFunction = getFunctionFile("link.clj")
        assertTrue(existingFunction.exists())

        siteCreator!!.createTextFunction("", "linkRenamed")
        siteCreator!!.renameTextFunction("link", "linkRenamed")
    }

    @Test(expected = UpdateException::class)
    fun renameTextScript_ExceptionOnRenamingToExistingFile() {
        var existingScript = getScriptFile("exampleScript.clj")
        assertTrue(existingScript.exists())

        siteCreator!!.createTextScript("", "exampleScriptRenamed")
        siteCreator!!.renameTextScript("exampleScript", "exampleScriptRenamed")
    }

    @Test
    fun readTextFunction() {
        var existingFunction = getFunctionFile("link.clj")
        assertTrue(existingFunction.exists())
        var expectedFunctionText = fileAsString(existingFunction)
        var actualFunctionText = siteCreator!!.readTextFunction("link")
        assertEquals(expectedFunctionText, actualFunctionText)
    }

    @Test
    fun readTextScript() {
        var existingScript = getScriptFile("exampleScript.clj")
        assertTrue(existingScript.exists())
        var expectedScriptText = fileAsString(existingScript)
        var actualScriptText = siteCreator!!.readTextScript("exampleScript")
        assertEquals(expectedScriptText, actualScriptText)
    }

    @Test
    fun readLayout() {
        var expectedLayoutText = fileAsString(justAPageLayout)
        var actualLayoutText = siteCreator!!.readLayout()
        assertEquals(expectedLayoutText, actualLayoutText)
    }

    companion object {
        private fun fileAsString(file: File?): String = com.google.common.io.Files.toString(file, Charsets.UTF_8)
    }
}