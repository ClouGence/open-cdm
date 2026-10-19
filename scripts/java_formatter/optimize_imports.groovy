import com.intellij.openapi.application.ApplicationManager
import com.intellij.formatting.service.FormattingServiceUtil
import com.intellij.openapi.fileEditor.FileDocumentManager
import com.intellij.openapi.project.DumbService
import com.intellij.openapi.project.ProjectManager
import com.intellij.openapi.roots.ProjectFileIndex
import com.intellij.openapi.vfs.LocalFileSystem
import com.intellij.psi.PsiDocumentManager
import com.intellij.psi.PsiJavaFile
import com.intellij.psi.PsiManager
import groovy.json.JsonOutput
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.StandardCopyOption

// Executed through IDEA's built-in ideScript entry point, never as a Gradle script.
def response
try {
    def repoRoot = Path.of(request.repoRoot)
    def projects = ProjectManager.getInstance().openProjects.findAll { project ->
        !project.isDisposed() && project.basePath != null && repoRoot.startsWith(Path.of(project.basePath))
    }.sort { project -> -project.basePath.length() }
    if (projects.isEmpty()) {
        throw new IllegalStateException("Open the IDEA project containing ${repoRoot} first.")
    }
    def project = projects.first()
    def application = ApplicationManager.getApplication()
    application.invokeAndWait {
        application.runWriteAction {
            if (DumbService.isDumb(project)) {
                throw new IllegalStateException('IDEA is indexing; retry when indexing finishes.')
            }
            def documents = FileDocumentManager.getInstance()
            def psiDocuments = PsiDocumentManager.getInstance(project)
            def psiManager = PsiManager.getInstance(project)
            def index = ProjectFileIndex.getInstance(project)
            def optimizerNames = new LinkedHashSet<String>()
            def files = request.files.collect { entry ->
                def path = repoRoot.resolve(entry.path)
                def virtualFile = LocalFileSystem.getInstance().refreshAndFindFileByNioFile(path)
                if (virtualFile == null || !index.isInSourceContent(virtualFile)) {
                    throw new IllegalStateException("Not an IDEA project source file: ${entry.path}")
                }
                def document = documents.getDocument(virtualFile)
                if (document == null || documents.isDocumentUnsaved(document)) {
                    throw new IllegalStateException("Save the editor buffer first: ${entry.path}")
                }
                if (document.text != entry.originalText.replace('\r\n', '\n')) {
                    throw new IllegalStateException("IDEA has different file contents; synchronize and retry: ${entry.path}")
                }
                psiDocuments.commitDocument(document)
                def original = psiManager.findFile(virtualFile)
                if (!(original instanceof PsiJavaFile)) {
                    throw new IllegalStateException("Not a Java PSI file: ${entry.path}")
                }
                // Copies retain their original file, so native code style and resolution apply.
                // Optimize Imports only changes the nonphysical copy, including in --check mode.
                def copy = original.copy() as PsiJavaFile
                // Follow OptimizeImportsProcessor's service dispatch, including plugin overrides.
                def service = FormattingServiceUtil.findImportsOptimizingService(original)
                def operations = []
                service.getImportOptimizers(original).each { optimizer ->
                    def countBefore = operations.size()
                    copy.viewProvider.allFiles.each { root ->
                        if (optimizer.supports(root)) {
                            optimizerNames.add(optimizer.class.name)
                            operations.add(optimizer.processFile(root))
                        }
                    }
                    if (operations.size() == countBefore && original.viewProvider.allFiles.any { optimizer.supports(it) }) {
                        throw new IllegalStateException("IDEA optimizer does not support preview copies: ${optimizer.class.name}")
                    }
                }
                operations.each { it.run() }
                def optimized = copy.text
                if (entry.originalText.contains('\r\n')) {
                    optimized = optimized.replace('\n', '\r\n')
                }
                [path: entry.path, optimizedText: optimized]
            }
            response = [ok: true, projectPath: project.basePath, optimizers: optimizerNames.toList(), files: files]
        }
    }
} catch (Throwable error) {
    response = [ok: false, error: "${error.class.simpleName}: ${error.message}"]
}

// The launcher may return before execution finishes; publish a complete result atomically.
def resultPath = Path.of(request.resultPath)
def pendingPath = resultPath.resolveSibling(resultPath.fileName.toString() + '.pending')
Files.writeString(pendingPath, JsonOutput.toJson(response))
Files.move(pendingPath, resultPath, StandardCopyOption.ATOMIC_MOVE)
