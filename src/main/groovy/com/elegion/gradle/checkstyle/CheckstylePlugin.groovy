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

import com.elegion.gradle.CodeQualityPlugin
import org.gradle.api.file.FileCollection
/**
 * @author =Troy=
 * @version 1.0
 */
class CheckstylePlugin extends CodeQualityPlugin<CheckstyleTask> {

  @Override
  String getTaskName() {
    return "checkstyle"
  }

  @Override
  Class<CheckstyleTask> getTaskType() {
    return CheckstyleTask
  }

  @Override
  FileCollection getTaskClasspath() {
    def classpath = project.configurations.checkstyle
    if (classpath.dependencies.empty) {
      project.dependencies { checkstyle "com.puppycrawl.tools:checkstyle:5.+" }
    }
    return classpath
  }

}
