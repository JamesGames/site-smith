package org.jamesgames.sitesmith.project

import org.jamesgames.sitesmith.builder.InvalidSiteLayoutException
import org.junit.Test
import java.io.File
import kotlin.collections.isEmpty
import kotlin.collections.listOf
import kotlin.collections.map
import kotlin.collections.reduce
import kotlin.test.assertEquals

/**
 * @author James Murphy
 */
class ProjectTest {
    val projectDir = this.javaClass.getResource("/test-project").file

    @Test
    fun testBuildingRootOnlyLayout() {
        val layoutFile = this.javaClass.getResource("/test-project/onlyRootLayout.json").file
        val project = Project(File(projectDir), File(layoutFile))
        project.buildSite()
        val outputDir = project.outputDirectory
        assert(outputDir.exists())
        assert(outputDir.isDirectory)
        assert(outputDir.listFiles().isEmpty())
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
        project.buildSite()
        val outputDir = project.outputDirectory
        assert(outputDir.exists())
        assert(outputDir.isDirectory)
        val files = outputDir.listFiles()
        assertEquals(1, files.size)
        assert(files[0].isFile)
        assertEquals("index.html", files[0].name)
    }

    @Test
    fun testBuildingPageAndResourceLayout() {
        val layoutFile = this.javaClass.getResource("/test-project/pageWithResourcesFileLayout.json").file
        val project = Project(File(projectDir), File(layoutFile))
        project.buildSite()
        val outputDir = project.outputDirectory
        assert(outputDir.exists())
        assert(outputDir.isDirectory)
        val files = outputDir.listFiles()
        // 3 files, a page, and two resource files
        assertEquals(3, files.size)
        assert(files[0].isFile)
        assert(files[1].isFile)
        assert(files[2].isFile)
        assert(fileNamesWithinDirMatch(
                listOf("index.html", "homePageResourceFileA.txt", "homePageResourceFileB.txt"),
                files))
    }

    private fun fileNamesWithinDirMatch(expectedFileNames: List<String>, filesInDir: Array<File>): Boolean {
        return filesInDir.size == expectedFileNames.size &&
                filesInDir
                        .map { expectedFileNames.contains(it.name) }
                        .reduce { a: Boolean, b: Boolean -> a && b }
    }
}