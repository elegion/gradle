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

package com.lightydev.dk.gradle.pmd

import com.lightydev.dk.gradle.CodeQualityPlugin
import org.gradle.api.file.FileCollection
/**
 * @author =Troy=
 * @version 1.0
 */
class PmdPlugin extends CodeQualityPlugin<PmdTask> {

  @Override
  String getTaskName() {
    return "pmd"
  }

  @Override
  Class<PmdTask> getTaskType() {
    return PmdTask
  }

  @Override
  FileCollection getTaskClasspath() {
    def classpath = project.configurations.pmd
    if (classpath.dependencies.empty) {
      project.dependencies { pmd "net.sourceforge.pmd:pmd:5.0.+" }
    }
    return classpath
  }

}
