package org.jamesgames.sitesmith.project

import org.jamesgames.sitesmith.builder.InvalidSiteLayoutException
import org.junit.Test
import java.io.File
import java.nio.file.Files
import kotlin.test.assertEquals
import kotlin.test.assertTrue

/**
 * @author James Murphy
 */
class ProjectTest {
    val projectDir = this.javaClass.getResource("/test-project").file!!
    val projectResourceFilesCopiedDirectly = this.javaClass.getResource("/test-project-copy-resource-dir").file!!
    val projectSelfReferencingPageDir = this.javaClass.getResource("/test-project-self-referencing-page").file!!

    val exampleScriptExpectedOutput = "<h1><a href=\"#hello-bob\" name=\"hello-bob\">Hello bob</a></h1>${System.lineSeparator()}" +
            "<h1><a href=\"#hello-bill\" name=\"hello-bill\">Hello bill</a></h1>${System.lineSeparator()}" +
            "<h1><a href=\"#hello-ben\" name=\"hello-ben\">Hello ben</a></h1>" +
            "<p>$$\$FunctionArgument$$$</p>" +
            "<a href=\"subDir/\">Relative link to test page 2</a>"

    fun makeAllNewLinesEqual(input: String) =
            input.replace(System.lineSeparator(), "\n")

    @Test
    fun testBuildingRootOnlyLayout() {
        val layoutFile = this.javaClass.getResource("/test-project/onlyRootLayout.json").file
        val project = Project(File(projectDir), File(layoutFile))
        assertTrue(project.buildSite())
        val outputDir = project.outputDirectory
        assertTrue(outputDir.exists())
        assertTrue(outputDir.isDirectory)
        assertTrue(outputDir.listFiles().isEmpty())
    }

    @Test(expected = InvalidSiteLayoutException::class)
    fun testBuildingEmptyLayout() {
        val layoutFile = this.javaClass.getResource("/test-project/emptyLayout.json").file
        val project = Project(File(projectDir), File(layoutFile))
        project.buildSite()
    }

    @Test
    fun testBuildingJustAPageLayout() {
        val layoutFile = this.javaClass.getResource("/test-project/justAPageLayout.json").file
        val project = Project(File(projectDir), File(layoutFile))
        assertTrue(project.buildSite())
        val outputDir = project.outputDirectory
        assertTrue(outputDir.exists())
        assertTrue(outputDir.isDirectory)
        val files = outputDir.listFiles()
        assertEquals(1, files.size)
        assertTrue(files[0].isFile)
        assertEquals("index.html", files[0].name)
    }

    @Test
    fun testBuildingPageAndResourceLayout() {
        val layoutFile = this.javaClass.getResource("/test-project/pageWithResourcesFileLayout.json").file
        val project = Project(File(projectDir), File(layoutFile))
        assertTrue(project.buildSite())
        val outputDir = project.outputDirectory
        assertTrue(outputDir.exists())
        assertTrue(outputDir.isDirectory)
        val files = outputDir.listFiles()
        // 3 files, a page, and two resource files
        assertEquals(3, files.size)
        assertTrue(files[0].isFile)
        assertTrue(files[1].isFile)
        assertTrue(files[2].isFile)
        assertTrue(Companion.fileNamesWithinDirMatch(
                listOf("index.html", "homePageResourceFileA.txt", "homePageResourceFileB.txt"),
                files))
    }

    @Test
    fun testMultipleDirectoriesLayout() {
        val layoutFile = this.javaClass.getResource("/test-project/multipleDirectoriesLayout.json").file
        val project = Project(File(projectDir), File(layoutFile))
        assertTrue(project.buildSite())
        val outputDir = project.outputDirectory
        assertTrue(outputDir.exists())
        assertTrue(outputDir.isDirectory)
        val files = outputDir.listFiles()
        assertTrue(Companion.fileNamesWithinDirMatch(
                listOf("index.html", "homeResource2.txt", "homeResource1.txt", "css", "subDirectory"),
                files))
        val cssDirFiles = files.filter { it.isDirectory && it.name.equals("css") }.first().listFiles()
        assertTrue(Companion.fileNamesWithinDirMatch(
                listOf("customGlobal.css", "additionalCssFile.css"),
                cssDirFiles))
        val subDirFiles = files.filter { it.isDirectory && it.name.equals("subDirectory") }.first().listFiles()
        assertTrue(Companion.fileNamesWithinDirMatch(
                listOf("a_page_in_a_sub_directory.html", "index.html", "resource2.txt"),
                subDirFiles))
    }

    @Test
    fun testResourceFilesSpecifiedByDirectoryLayout() {
        val layoutFile = this.javaClass.getResource("/test-project-copy-resource-dir/layout.json").file
        val project = Project(File(projectResourceFilesCopiedDirectly), File(layoutFile))
        assertTrue(project.buildSite())
        val outputDir = project.outputDirectory
        assertTrue(outputDir.exists())
        assertTrue(outputDir.isDirectory)
        val files = outputDir.listFiles()
        assertTrue(Companion.fileNamesWithinDirMatch(
                listOf("index.html", "resource1.txt", "directoryA", "directoryB"),
                files))
        val dirA = files.filter { it.isDirectory && it.name.equals("directoryA") }.first().listFiles()
        assertTrue(Companion.fileNamesWithinDirMatch(
                listOf("resource3.txt", "resource4.txt", "index.html"),
                dirA))
        val dirB = files.filter { it.isDirectory && it.name.equals("directoryB") }.first().listFiles()
        assertTrue(Companion.fileNamesWithinDirMatch(
                listOf("resource2.txt"),
                dirB))
    }

    @Test
    fun testPageContentLayout() {
        val layoutFile = this.javaClass.getResource("/test-project/pageContentLayout.json").file
        val project = Project(File(projectDir), File(layoutFile))
        assertTrue(project.buildSite())
        val outputDir = project.outputDirectory
        assertTrue(outputDir.exists())
        assertTrue(outputDir.isDirectory)

        var files = outputDir.listFiles()
        assertTrue(Companion.fileNamesWithinDirMatch(
                listOf("index.html", "subDir"),
                files))
        val testPage1File = files.filter { it.name.equals("index.html") }.first()
        val testPage1pageContent = String(Files.readAllBytes(testPage1File.toPath()))
        assertTrue(makeAllNewLinesEqual(testPage1pageContent).contains(
                (makeAllNewLinesEqual(exampleScriptExpectedOutput))))

        val subDir = files.filter { it.isDirectory && it.name.equals("subDir") }.first().listFiles()
        assertTrue(Companion.fileNamesWithinDirMatch(
                listOf("index.html"),
                subDir))
        val testPage2File = subDir.filter { it.name.equals("index.html") }.first()
        val testPage2PageContent = String(Files.readAllBytes(testPage2File.toPath()))
        assertTrue(testPage2PageContent.contains("<a href=\"../\">Relative link to test page 1</a>"))
    }

    @Test
    fun testPageNameOutputtedByScript() {
        val layoutFile = this.javaClass.getResource("/test-project/pageNameOutputtedByScript.json").file
        val project = Project(File(projectDir), File(layoutFile))
        assertTrue(project.buildSite())
        val outputDir = project.outputDirectory
        assertTrue(outputDir.exists())
        assertTrue(outputDir.isDirectory)

        var files = outputDir.listFiles()
        assertTrue(Companion.fileNamesWithinDirMatch(
                listOf("index.html", "subDir"),
                files))
        val testPage1File = files.filter { it.name.equals("index.html") }.first()
        val testPage1pageContent = String(Files.readAllBytes(testPage1File.toPath()))
        assertTrue(testPage1pageContent.contains("testPage"))
        assertTrue(!testPage1pageContent.contains("testPage2"))

        val subDir = files.filter { it.isDirectory && it.name.equals("subDir") }.first().listFiles()
        assertTrue(Companion.fileNamesWithinDirMatch(
                listOf("index.html"),
                subDir))
        val testPage2File = subDir.filter { it.name.equals("index.html") }.first()
        val testPage2PageContent = String(Files.readAllBytes(testPage2File.toPath()))
        assertTrue(testPage2PageContent.contains("testPage2"))

    }

    @Test
    fun testResourceNameQuerying() {
        val layoutFile = this.javaClass.getResource("/test-project/resourceNameQueryingTestLayout.json").file
        val project = Project(File(projectDir), File(layoutFile))
        assertTrue(project.buildSite())
        val outputDir = project.outputDirectory
        assertTrue(outputDir.exists())
        assertTrue(outputDir.isDirectory)

        var files = outputDir.listFiles()
        assertTrue(Companion.fileNamesWithinDirMatch(
                listOf("index.html", "homePageResourceFileA.txt", "homePageResourceFileB.txt", "dirForFilteredNames"),
                files))
        val subDir = files.filter { it.isDirectory && it.name.equals("dirForFilteredNames") }.first().listFiles()
        assertTrue(Companion.fileNamesWithinDirMatch(
                listOf("abc_1_def.txt", "abc_2.txt", "3_def.txt"),
                subDir))

        val testPage1File = files.filter { it.name.equals("index.html") }.first()
        val testPage1pageContent = String(Files.readAllBytes(testPage1File.toPath()))
        // Note for this test not all Resource files are added to the project layout!
        assertTrue(testPage1pageContent.contains("resource names containing 'Resource' count: 2"))
        assertTrue(testPage1pageContent.contains("with util call, resource names beginning with 'abc' count: 2"))
        assertTrue(testPage1pageContent.contains("with util call, resource names ending with 'def' count: 2"))
        assertTrue(testPage1pageContent.contains("with string shortcut, resource names beginning with 'abc' count: 2"))
        assertTrue(testPage1pageContent.contains("with string shortcut, resource names ending with 'def' count: 2"))
        assertTrue(testPage1pageContent.contains("someResource1"))
        assertTrue(testPage1pageContent.contains("someResource2"))
        assertTrue(testPage1pageContent.contains("testPage"))
    }

    @Test
    fun testSelfReferencingPageProject() {
        val layoutFile = this.javaClass.getResource("/test-project-self-referencing-page/layout.json").file
        val project = Project(File(projectSelfReferencingPageDir), File(layoutFile))
        assertTrue(project.buildSite())
        val outputDir = project.outputDirectory
        assertTrue(outputDir.exists())
        assertTrue(outputDir.isDirectory)

        var files = outputDir.listFiles()
        assertTrue(Companion.fileNamesWithinDirMatch(
                listOf("index.html", "page2.html", "page3.html", "subDir"),
                files))

        val testPage1File = files.filter { it.name.equals("index.html") }.first()
        val testPage1pageContent = makeAllNewLinesEqual(String(Files.readAllBytes(testPage1File.toPath())))
        assertTrue(testPage1pageContent.contains(makeAllNewLinesEqual("""<a href="./">to this page</a>
<a href="./">homePage</a>
<a href="page2.html">page2</a>
<a href="page3.html">page3</a>
<a href="subDir/page5.html">page5</a>
<a href="subDir/">subDirHomePage</a>""")))

        val testPage2File = files.filter { it.name.equals("page2.html") }.first()
        val testPage2pageContent = makeAllNewLinesEqual(String(Files.readAllBytes(testPage2File.toPath())))
        assertTrue(testPage2pageContent.contains(makeAllNewLinesEqual("""<a href="page2.html">to this page</a>
<a href="./">homePage</a>
<a href="page2.html">page2</a>
<a href="page3.html">page3</a>
<a href="subDir/page5.html">page5</a>
<a href="subDir/">subDirHomePage</a>""")))

        val testPage3File = files.filter { it.name.equals("page3.html") }.first()
        val testPage3pageContent = makeAllNewLinesEqual(String(Files.readAllBytes(testPage3File.toPath())))
        assertTrue(testPage3pageContent.contains(makeAllNewLinesEqual("""<a href="page3.html">to this page</a>
<a href="./">homePage</a>
<a href="page2.html">page2</a>
<a href="page3.html">page3</a>
<a href="subDir/page5.html">page5</a>
<a href="subDir/">subDirHomePage</a>""")))

        val subDirFiles = files.filter { it.isDirectory && it.name.equals("subDir") }.first().listFiles()
        assertTrue(Companion.fileNamesWithinDirMatch(
                listOf("index.html", "page5.html"),
                subDirFiles))

        val testPage4File = subDirFiles.filter { it.name.equals("index.html") }.first()
        val testPage4pageContent = makeAllNewLinesEqual(String(Files.readAllBytes(testPage4File.toPath())))
        assertTrue(testPage4pageContent.contains(makeAllNewLinesEqual("""<a href="./">to this page</a>
<a href="../">homePage</a>
<a href="../page2.html">page2</a>
<a href="../page3.html">page3</a>
<a href="page5.html">page5</a>
<a href="./">subDirHomePage</a>""")))

        val testPage5File = subDirFiles.filter { it.name.equals("page5.html") }.first()
        val testPage5pageContent = makeAllNewLinesEqual(String(Files.readAllBytes(testPage5File.toPath())))
        assertTrue(testPage5pageContent.contains(makeAllNewLinesEqual("""<a href="page5.html">to this page</a>
<a href="../">homePage</a>
<a href="../page2.html">page2</a>
<a href="../page3.html">page3</a>
<a href="page5.html">page5</a>
<a href="./">subDirHomePage</a>""")))
    }

    companion object {
        fun fileNamesWithinDirMatch(expectedFileNames: List<String>, filesInDir: Array<File>): Boolean {
            return filesInDir.size == expectedFileNames.size &&
                    filesInDir
                            .map { expectedFileNames.contains(it.name) }
                            .reduce { a: Boolean, b: Boolean -> a && b }
        }
    }
}