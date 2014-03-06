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

package com.elegion.gradle.checkstyle

import com.elegion.gradle.CodeQualityTask
import org.gradle.api.GradleException
import org.gradle.api.file.FileCollection
import org.gradle.api.internal.project.IsolatedAntBuilder
import org.gradle.api.tasks.TaskAction
/**
 * @author =Troy=
 * @version 1.0
 */
class CheckstyleTask extends CodeQualityTask {

  @TaskAction
  public void run() {
    def antBuilder = services.get(IsolatedAntBuilder)
    def failureProperty = "org.gradle.checkstyle.violations"
    antBuilder.withClasspath(getClasspath()).execute {
      ant.taskdef(name: 'checkstyle', classname: 'com.puppycrawl.tools.checkstyle.CheckStyleTask')
      ant.checkstyle(
          config: getConfigFile(),
          failOnViolation: false,
          failureProperty: failureProperty,
          maxErrors: maxErrors
      ) {
        getJavaSources().addToAntBuilder(ant, 'fileset', FileCollection.AntType.FileSet)
        if (showViolations) {
          formatter(type: 'plain', useFile: false)
        }
        if (reportFile) {
          formatter(type: 'xml', toFile: reportFile)
        }
      }
      createHtmlReport()
      def failureMessage = ant.project.properties[failureProperty]
      if (failureMessage) {
        def message = "${name}: Violations were found. ${failureMessage}"
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
