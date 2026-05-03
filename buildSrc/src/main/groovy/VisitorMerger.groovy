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

        frontFile.withWriter('UTF-8') { writer ->
            writer.write(mergedContent)
        }
    }
}