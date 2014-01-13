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

package com.lightydev.dk.gradle.findbugs

import com.android.build.gradle.api.AndroidSourceSet
import com.lightydev.dk.gradle.CodeQualityPlugin
import org.gradle.api.file.FileCollection

/**
 * @author =Troy=
 * @version 1.0
 */
class FindbugsPlugin extends CodeQualityPlugin<FindbugsTask> {

  @Override
  String getTaskName() {
    return "findbugs"
  }

  @Override
  Class<FindbugsTask> getTaskType() {
    return FindbugsTask
  }

  @Override
  FileCollection getTaskClasspath() {
    def classpath = project.configurations.findbugs
    if (classpath.dependencies.empty) {
      project.dependencies { findbugs "com.google.code.findbugs:findbugs-ant:2.0.+" }
    }
    project.dependencies { compile "com.google.code.findbugs:annotations:2.0.+" }
    return classpath
  }

  @Override
  void createConfiguration() {
    super.createConfiguration()
    project.configurations.create("findbugsPlugin").with {
      visible = false
      transitive = true
      exclude group: 'ant', module: 'ant'
      exclude group: 'org.apache.ant', module: 'ant'
      exclude group: 'org.apache.ant', module: 'ant-launcher'
      exclude group: 'org.codehaus.groovy', module: 'groovy'
      exclude group: 'org.codehaus.groovy', module: 'groovy-all'
      exclude group: 'org.slf4j', module: 'slf4j-api'
      exclude group: 'org.slf4j', module: 'jcl-over-slf4j'
      exclude group: 'org.slf4j', module: 'log4j-over-slf4j'
      exclude group: 'commons-logging', module: 'commons-logging'
      exclude group: 'log4j', module: 'log4j'
      exclude group: 'com.android.tools.build', module: 'gradle'
    }
  }

  @Override
  void configureTask(FindbugsTask task, AndroidSourceSet ss) {
    super.configureTask(task, ss)
    task.with {
      pluginClasspath = project.configurations.findbugsPlugin
    }
  }

}
