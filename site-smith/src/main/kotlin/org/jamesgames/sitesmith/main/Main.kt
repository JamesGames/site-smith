package org.jamesgames.sitesmith.main

import org.apache.commons.cli.*
import org.jamesgames.sitesmith.project.Project
import java.io.File

/**
 * @author James Murphy
 */
class Main {
    companion object {
        const private val siteLayoutOption = "sl"
        const private val projectDirOption = "pd"
        const private val helpOption = "h"
        private val welcomeMessage = "Welcome to the site-smith website generator tool${System.lineSeparator()}" +
                "Project page: github.com/jamesgames/site-smith"

        @JvmStatic public fun main(args: Array<String>) {
            val options = Options();
            options.addOption(Option.builder().argName(projectDirOption)
                    .longOpt("project-directory")
                    .required()
                    .hasArg()
                    .desc("The directory containing the project's file such as the resources, html scripts and functions directories")
                    .build())
            options.addOption(Option.builder().argName(siteLayoutOption)
                    .longOpt("site-layout")
                    .required()
                    .hasArg()
                    .desc("The file depicting the layout of the files and directories for the generated website")
                    .build())

            val parser = DefaultParser();
            try {
                val line = parser.parse(options, args);
                if (line.hasOption(helpOption)) {
                    printHelp(options)
                }
                System.out.println(try {
                    Project(File(line.getOptionValue(projectDirOption)), File(line.getOptionValue(siteLayoutOption))).buildSite()
                } catch(exceptionFromProject: Exception) {
                    "Issue during site creation. Issue: " + exceptionFromProject.message
                })

            } catch(exp: ParseException) {
                System.out.println("Command line argument parsing failed. Reason: " + exp.message)
                printHelp(options)
            }
        }

        private fun printHelp(options: Options) {
            HelpFormatter().printHelp("java -jar [NAME-OF-JAR-FILE]", welcomeMessage, options, "");
        }
    }
}