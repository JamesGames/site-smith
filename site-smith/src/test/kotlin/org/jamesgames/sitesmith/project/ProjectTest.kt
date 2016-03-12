package org.jamesgames.sitesmith.project

import org.jamesgames.sitesmith.builder.InvalidSiteLayoutException
import org.junit.Test
import java.io.File
import kotlin.test.assertEquals
import kotlin.test.assertTrue

/**
 * @author James Murphy
 */
class ProjectTest {
    val projectDir = this.javaClass.getResource("/test-project").file
    val projectResourceFilesCopiedDirectly = this.javaClass.getResource("/test-project-copy-resource-dir").file

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
        assertTrue(fileNamesWithinDirMatch(
                listOf("index.html", "homePageResourceFileA.txt", "homePageResourceFileB.txt"),
                files))
    }

    private fun fileNamesWithinDirMatch(expectedFileNames: List<String>, filesInDir: Array<File>): Boolean {
        return filesInDir.size == expectedFileNames.size &&
                filesInDir
                        .map { expectedFileNames.contains(it.name) }
                        .reduce { a: Boolean, b: Boolean -> a && b }
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
        assertTrue(fileNamesWithinDirMatch(
                listOf("index.html", "homeResource2.txt", "homeResource1.txt", "css", "subDirectory"),
                files))
        val cssDirFiles = files.filter { it.isDirectory && it.name.equals("css") }.first().listFiles()
        assertTrue(fileNamesWithinDirMatch(
                listOf("customGlobal.css", "additionalCssFile.css"),
                cssDirFiles))
        val subDirFiles = files.filter { it.isDirectory && it.name.equals("subDirectory") }.first().listFiles()
        assertTrue(fileNamesWithinDirMatch(
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
        assertTrue(fileNamesWithinDirMatch(
                listOf("index.html", "resource1.txt", "directoryA", "directoryB"),
                files))
        val dirA = files.filter { it.isDirectory && it.name.equals("directoryA") }.first().listFiles()
        assertTrue(fileNamesWithinDirMatch(
                listOf("resource3.txt", "resource4.txt", "index.html"),
                dirA))
        val dirB = files.filter { it.isDirectory && it.name.equals("directoryB") }.first().listFiles()
        assertTrue(fileNamesWithinDirMatch(
                listOf("resource2.txt"),
                dirB))
    }

}