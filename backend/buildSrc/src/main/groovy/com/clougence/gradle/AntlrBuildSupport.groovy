/*
 * Copyright 2026 杭州开云集致科技有限公司
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.clougence.gradle

import org.gradle.api.Project
import org.gradle.api.plugins.antlr.AntlrTask
import org.gradle.api.tasks.TaskProvider

class AntlrBuildSupport {
    private static final List<String> GENERATED_OUTPUT_PATTERNS = ['**/*.java', '**/*.tokens', '**/*.interp']

    static void configureGrammarTask(Project project, AntlrTask task, Map config) {
        String outputPath = requiredString(config, 'outputPath')
        String packageName = requiredString(config, 'packageName')
        File outputDirectory = project.file(outputPath)
        outputDirectory.mkdirs()

        task.enabled = config.containsKey('enabled') ? (config.enabled as boolean) : true
        task.maxHeapSize = config.maxHeapSize?.toString() ?: '1024m'
        task.outputDirectory = outputDirectory

        if (config.containsKey('source')) {
            task.source = config.source
        }

        List<String> arguments = ['-visitor', '-package', packageName]
        if (config.libDir) {
            arguments.addAll(['-lib', config.libDir.toString()])
        }
        if (config.extraArguments) {
            arguments.addAll((config.extraArguments as Iterable).collect { it.toString() })
        }
        task.arguments = arguments

        task.doFirst {
            def generatedOutputs = project.fileTree(outputDirectory)
            GENERATED_OUTPUT_PATTERNS.each { generatedOutputs.include(it) }
            project.delete(generatedOutputs)
            outputDirectory.mkdirs()
        }
    }

    static void wireGrammarTasks(Project project, Iterable<?> taskRefs) {
        List<Object> dependencies = taskRefs.collect { normalizeTaskRef(it) }

        project.tasks.named('compileJava').configure {
            dependsOn(dependencies)
        }
        project.tasks.matching { it.name == 'sourcesJar' }.configureEach {
            dependsOn(dependencies)
        }
    }

    private static Object normalizeTaskRef(Object taskRef) {
        if (taskRef instanceof TaskProvider) {
            return taskRef
        }
        return taskRef.toString()
    }

    private static String requiredString(Map config, String key) {
        Object value = config.get(key)
        if (!(value instanceof CharSequence) || value.toString().trim().isEmpty()) {
            throw new IllegalArgumentException("Missing required ANTLR config: ${key}")
        }
        return value.toString()
    }
}