/*
 * Copyright 2012-2014 Daniel Serdyukov
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.elegion.gradle.findbugs

import com.elegion.gradle.CodeQualityTask
import org.gradle.api.GradleException
import org.gradle.api.file.FileCollection
import org.gradle.api.internal.project.IsolatedAntBuilder
import org.gradle.api.tasks.TaskAction
/**
 * @author =Troy=
 * @version 1.0
 */
class FindbugsTask extends CodeQualityTask {

  FileCollection pluginClasspath

  String jvmArgs = "-Xmx768M"

  @TaskAction
  public void run() {
    def antBuilder = services.get(IsolatedAntBuilder)
    def String errorProp = 'org.gradle.findbugs.error'
    def String failureProperty = 'org.gradle.findbugs.warn'
    def gradleProject = project
    antBuilder.withClasspath(classpath).execute {
      ant.taskdef(name: 'findbugs', classname: 'edu.umd.cs.findbugs.anttask.FindBugsTask')
      ant.findbugs(
          errorProperty: errorProp,
          warningsProperty: failureProperty,
          output: 'xml:withMessages',
          outputFile: reportFile,
          excludeFilter: configFile,
          jvmargs: getJvmArgs()
      ) {
        classpath.addToAntBuilder(ant, 'classpath')
        pluginClasspath.addToAntBuilder(ant, 'pluginList')
        auxClasspath(path: gradleProject.configurations.compile.asPath)
        sourcePath(path: javaSources.getFiles())
        "class"(location: "${gradleProject.buildDir}/classes")
      }
      createHtmlReport()
      if (ant.project.properties[errorProp]) {
        throw new GradleException("FindBugs encountered an error. Run with --debug to get more information.")
      }
      if (ant.project.properties[failureProperty]) {
        def message = "${name}: Violations were found."
        if (reportFile != null && reportFile.exists()) {
          message += "\n${name}: See the report at: ${reportFile.absolutePath}"
        }
        if (failOnError) {
          throw new GradleException(message)
        } else {
          logger.error(message)
        }
      }
    }
  }

}
