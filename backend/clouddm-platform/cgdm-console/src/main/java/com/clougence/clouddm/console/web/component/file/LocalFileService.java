/*
 * Copyright 2026 杭州开云集致科技有限公司
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 */
package com.clougence.clouddm.console.web.component.file;

import java.nio.file.Path;

import com.clougence.clouddm.platform.dal.model.system.SysAttachmentType;
import com.clougence.utils.function.EFunction;

public interface LocalFileService {

    <T> T consumeLocked(long fileId, Path cacheFile, EFunction<Path, T, Exception> visitor);

    long addAsLocked(String userUid, Path sourceFile, String fileName, SysAttachmentType attachmentType, long approvalId);

    //

    <T> T consumeEditing(String userUid, long fileId, EFunction<Path, T, Exception> visitor);

    void renewEditing(String userUid, long fileId);

    void lockEditing(String userUid, long fileId, long approvalId);

    long addAsEditing(String userUid, Path temporaryFile, String fileName, SysAttachmentType attachmentType);

    //

    boolean exists(long fileId);

    void deleteRecord(long fileId);

    void invalidateCache(Path cacheFile);
}
