/*
 * Copyright 2026 杭州开云集致科技有限公司
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 */
package com.clougence.clouddm.console.web.service.upload;

import org.springframework.web.multipart.MultipartFile;

import com.clougence.clouddm.console.web.service.upload.model.SqlFilePreviewVO;
import com.clougence.clouddm.console.web.service.upload.model.SqlFileUploadVO;

public interface UploadService4SqlFile {

    SqlFileUploadVO upload(String uid, MultipartFile file);

    SqlFilePreviewVO preview(String uid, long attachmentId, int startLine, int lineCount);
}
