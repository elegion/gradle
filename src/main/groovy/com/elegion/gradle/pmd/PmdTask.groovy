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

package com.elegion.gradle.pmd

import com.elegion.gradle.CodeQualityTask
import org.gradle.api.GradleException
import org.gradle.api.file.FileCollection
import org.gradle.api.internal.project.IsolatedAntBuilder
import org.gradle.api.tasks.TaskAction

/**
 * @author =Troy=
 * @version 1.0
 */
class PmdTask extends CodeQualityTask {

  int level = 5

  @TaskAction
  public void run() {
    def antBuilder = services.get(IsolatedAntBuilder)
    def failureProperty = "org.gradle.pmd.violations"
    antBuilder.withClasspath(getClasspath()).execute {
      ant.taskdef(name: 'pmd', classname: 'net.sourceforge.pmd.ant.PMDTask')
      ant.pmd(
          shortFilenames: 'true',
          minimumPriority: level,
          failOnRuleViolation: false,
          rulesetfiles: configFile.toURI().toString(),
          failuresPropertyName: failureProperty
      ) {
        getJavaSources().addToAntBuilder(ant, 'fileset', FileCollection.AntType.FileSet)
        if (showViolations) {
          formatter(type: 'plain', toConsole: true)
        }
        if (reportFile) {
          formatter(type: 'xml', toFile: reportFile)
        }
      }
      createHtmlReport()
      def failureCount = ant.project.properties[failureProperty];
      if (failureCount) {
        def message = "${name}: ${failureCount} violations were found."
        if (reportFile != null && reportFile.exists()) {
          message += "\n${name}: See the report at: ${reportFile.absolutePath}"
        }
        if (failOnError || (maxErrors > 0 && failureCount > maxErrors)) {
          throw new GradleException(message)
        } else {
          logger.error(message)
        }
      }
    }
  }

}
