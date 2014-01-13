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

package com.lightydev.dk.gradle

import org.gradle.api.DefaultTask
import org.gradle.api.file.FileCollection
import org.gradle.api.file.FileTree
import org.gradle.api.tasks.InputFile
import org.gradle.api.tasks.InputFiles
import org.gradle.api.tasks.Optional
import org.gradle.api.tasks.OutputFile
/**
 * @author =Troy=
 * @version 1.0
 */
class CodeQualityTask extends DefaultTask {

  @InputFiles
  FileCollection classpath

  @InputFiles
  FileTree javaSources

  @InputFile
  File configFile

  @Optional
  @OutputFile
  File reportFile

  @Optional
  @InputFile
  File reportXslFile

  boolean failOnError

  boolean showViolations

  int maxErrors

  void createHtmlReport() {
    if (reportFile.exists() && reportXslFile != null && reportXslFile.exists()) {
      ant.xslt(
          in: reportFile,
          style: reportXslFile,
          out: reportFile.absolutePath.replaceFirst(~/\.[^\.]+$/, ".html")
      )
    }
  }

}
