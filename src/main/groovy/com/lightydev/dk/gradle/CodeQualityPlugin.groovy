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

import com.android.build.gradle.BasePlugin
import com.android.build.gradle.api.AndroidSourceSet
import org.gradle.api.Plugin
import org.gradle.api.file.FileCollection
import org.gradle.api.internal.project.ProjectInternal

/**
 * @author =Troy=
 * @version 1.0
 */
abstract class CodeQualityPlugin<T extends CodeQualityTask> implements Plugin<ProjectInternal> {

  private static final String TOOL_NAME = "Android Code Quality"

  ProjectInternal project;

  static String getToolName() {
    return TOOL_NAME
  }

  @Override
  void apply(ProjectInternal p) {
    project = p
    createConfiguration()
    project.plugins.withType(BasePlugin) {
      if (project.android.sourceSets.hasProperty("main")) {
        AndroidSourceSet sourceSet = project.android.sourceSets.main
        configureTask(project.tasks.create(taskName, taskType), sourceSet)
      }
    }
  }

  void createConfiguration() {
    project.configurations.create(taskName).with {
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

  void configureTask(T task, AndroidSourceSet ss) {
    task.with {
      group = toolName
      description = "Run ${taskName.capitalize()} analysis of project"
      classpath = taskClasspath
      javaSources = ss.allJava
      configFile = project.file("src/config/${taskName}/${taskName}.xml")
      reportFile = new File(project.buildDir, "reports/${taskName}/${taskName}.xml")
      reportXslFile = project.file("src/config/${taskName}/${taskName}.xsl")
    }
  }

  abstract String getTaskName()

  abstract Class<T> getTaskType()

  abstract FileCollection getTaskClasspath()

}
