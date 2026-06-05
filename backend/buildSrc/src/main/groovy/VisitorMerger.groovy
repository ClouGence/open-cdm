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
import org.gradle.api.DefaultTask
import org.gradle.api.tasks.InputFile
import org.gradle.api.tasks.TaskAction

class VisitorMerger extends DefaultTask {

    @InputFile
    File backFile
    @InputFile
    File frontFile

    @TaskAction
    void start() {
        def backContent = backFile.text
        def frontContent = frontFile.text

        def backEndIndex = backContent.indexOf('}')
        def frontEndIndex = frontContent.indexOf('}')

        if (backEndIndex == -1 || frontEndIndex == -1) {
            throw new IllegalArgumentException("Missing '}' in one of the template files.")
        }

        def mergedContent = frontContent[0..frontEndIndex] + backContent[(backEndIndex + 1)..-1]

        frontFile.withWriter('UTF-8') { writer -> writer.write(mergedContent)
        }
    }
}