package org.jamesgames.sitesmith.creator

import org.apache.commons.io.FileUtils
import org.jamesgames.sitesmith.project.Project
import org.junit.After
import org.junit.Before
import org.junit.Test
import java.io.File
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

        FileUtils.cleanDirectory(newTempProjectLocationFile)
        FileUtils.copyDirectory(projectDirToCopyFromFile, newTempProjectLocationFile)

        justAPageLayout = newTempProjectLocationFile!!.listFiles().first { it.name == "justAPageLayout.json" }
        onlyRootLayoutProject = Project(newTempProjectLocationFile!!, justAPageLayout!!)

        val outputDir = onlyRootLayoutProject!!.outputDirectory
        // Verify copy of test project was copied
        assertTrue(onlyRootLayoutProject!!.buildSite())
        assertTrue(outputDir.exists())
        assertTrue(justAPageLayout!!.exists())
        assertTrue(Companion.fileAsString(justAPageLayout).isNotEmpty())

        siteCreator = SiteCreator(onlyRootLayoutProject!!.siteLayout, onlyRootLayoutProject!!.textFunctionDirectory,
                onlyRootLayoutProject!!.textScriptDirectory)
    }

    @After
    fun tearDown() {
        FileUtils.cleanDirectory(newTempProjectLocationFile)
    }

    @Test
    fun modifyLayoutFile() {
        val newTextToUse = "new text for layout"

        var existingLayoutFileText = Companion.fileAsString(justAPageLayout)
        assertTrue(!existingLayoutFileText.equals(newTextToUse))

        siteCreator!!.modifyLayoutFile(newTextToUse)

        var newLayoutFileText = Companion.fileAsString(justAPageLayout)
        assertTrue(newLayoutFileText.equals(newLayoutFileText))
    }

    @Test
    fun modifyTextFunction() {
        var existingTextFunction = onlyRootLayoutProject!!.textFunctionDirectory.listFiles()
                .first { it.name == "link.clj" }
        val newTextToUse = "new text for function"

        var existingTextFunctionText = Companion.fileAsString(existingTextFunction)
        assertTrue(!existingTextFunctionText.equals(newTextToUse))

        siteCreator!!.modifyTextFunction(newTextToUse, "link")

        var newTextFunctionText = Companion.fileAsString(existingTextFunction)
        assertTrue(newTextFunctionText.equals(newTextToUse))
    }

    @Test
    fun modifyTextScript() {
        var existingTextScript = onlyRootLayoutProject!!.textScriptDirectory.listFiles()
                .first { it.name == "exampleScript.clj" }
        val newTextToUse = "new text for script"

        var existingTextScriptText = Companion.fileAsString(existingTextScript)
        assertTrue(!existingTextScriptText.equals(newTextToUse))

        siteCreator!!.modifyTextScript(newTextToUse, "exampleScript")

        var newTextScriptText = Companion.fileAsString(existingTextScript)
        assertTrue(newTextScriptText.equals(newTextToUse))
    }

    @Test
    fun createNewTextFunction() {
        var functionNameToCreate = "someNewFunc"
        var functionDoesNotExistYet = null == onlyRootLayoutProject!!.textFunctionDirectory.listFiles()
                .firstOrNull { it.name == functionNameToCreate }
        assertTrue(functionDoesNotExistYet)

        var functionTextToUse = "some function text"
        siteCreator!!.createTextFunction(functionTextToUse, functionNameToCreate)

        var newlyCreatedFunction = onlyRootLayoutProject!!.textFunctionDirectory.listFiles()
                .first { it.name == functionNameToCreate + ".clj" }
        assertTrue(newlyCreatedFunction.exists())
        var functionTextActuallyUsed = Companion.fileAsString(newlyCreatedFunction)
        assertTrue(functionTextToUse.equals(functionTextActuallyUsed))
    }

    @Test
    fun createNewTextScript() {
        var scriptNameToCreate = "someNewScript"
        var scriptDoesNotExistYet = null == onlyRootLayoutProject!!.textScriptDirectory.listFiles()
                .firstOrNull { it.name == scriptNameToCreate }
        assertTrue(scriptDoesNotExistYet)

        var scriptTextToUse = "some script text"
        siteCreator!!.createTextScript(scriptTextToUse, scriptNameToCreate)

        var newlyCreatedScript = onlyRootLayoutProject!!.textScriptDirectory.listFiles()
                .first { it.name == scriptNameToCreate + ".clj" }
        assertTrue(newlyCreatedScript.exists())
        var scriptTextActuallyUsed = Companion.fileAsString(newlyCreatedScript)
        assertTrue(scriptTextToUse.equals(scriptTextActuallyUsed))
    }

    @Test
    fun deleteTextFunction() {
        var existingFunction = onlyRootLayoutProject!!.textFunctionDirectory.listFiles()
                .first { it.name == "link.clj" }
        assertTrue(existingFunction.exists())

        siteCreator!!.deleteTextFunction("link")

        var functionDoesNotExist = null == onlyRootLayoutProject!!.textFunctionDirectory.listFiles()
                .firstOrNull { it.name == "link.clj" }
        assertTrue(functionDoesNotExist)
    }

    @Test
    fun deleteTextScript() {
        var existingScript = onlyRootLayoutProject!!.textScriptDirectory.listFiles()
                .first { it.name == "exampleScript.clj" }
        assertTrue(existingScript.exists())

        siteCreator!!.deleteTextScript("exampleScript")

        var functionDoesNotExist = null == onlyRootLayoutProject!!.textScriptDirectory.listFiles()
                .firstOrNull { it.name == "exampleScript.clj" }
        assertTrue(functionDoesNotExist)
    }

    companion object {
        private fun fileAsString(file: File?): String = com.google.common.io.Files.toString(file, Charsets.UTF_8)
    }
}