/* Copyright 2026 杭州开云集致科技有限公司 */
package com.clougence.clouddm.console.web.service.editor.script;

import java.util.Set;

public interface SqlScriptStorage {

    String put(String content);

    String read(String fileUri);

    void touch(String fileUri);

    void delete(String fileUri);

    void cleanupOrphans(Set<String> referencedFileUris);
}
