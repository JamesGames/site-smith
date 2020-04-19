package org.jamesgames.sitesmith.main

import org.apache.commons.cli.*
import org.jamesgames.sitesmith.project.Project
import java.io.File

/**
 * @author James Murphy
 */
class Main {
    companion object {
        private const val siteLayoutOption = "sl"
        private const val projectDirOption = "pd"
        private const val useGuiOption = "gui"
        private val cliHeaderMessage = "Welcome to the site-smith website generator tool${System.lineSeparator()}" +
                "Project page: github.com/jamesgames/site-smith${System.lineSeparator()}${System.lineSeparator()}" +
                "Options:"

        @JvmStatic
        fun main(args: Array<String>) {
            val options = Options()
            options.addOption(Option.builder(projectDirOption)
                    .longOpt("project-directory")
                    .required()
                    .hasArg()
                    .desc("The directory containing the project's file such as the resources, text scripts and functions directories (auto-created if needed)")
                    .build())
            options.addOption(Option.builder(siteLayoutOption)
                    .longOpt("site-layout")
                    .required()
                    .hasArg()
                    .desc("The file depicting the layout of the files and directories for the generated website")
                    .build())
            options.addOption(Option.builder(useGuiOption)
                    .desc("Serves a web page to help create SiteSmith projects")
                    .build())

            val parser = DefaultParser()
            try {
                val line = parser.parse(options, args)
                var project = Project(File(line.getOptionValue(projectDirOption)),
                        File(line.getOptionValue(siteLayoutOption)))
                val cmdLineResults = try {
                    project.buildSite()
                    project.results
                } catch (exceptionFromProject: Exception) {
                    "Issue during site creation: " + exceptionFromProject.toString()
                    throw exceptionFromProject
                }
                    System.out.println(cmdLineResults)
            } catch (exp: ParseException) {
                System.out.println("Command line argument parsing failed. Reason: ${exp.message}")
                printHelp(options)
            }
        }

        private fun printHelp(options: Options) =
                HelpFormatter().printHelp("java -jar [NAME-OF-JAR-FILE] [OPTIONS]", cliHeaderMessage, options, "")
    }
}