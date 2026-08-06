/*
 * Copyright 2026 杭州开云集致科技有限公司
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 */
package com.clougence.clouddm.console.web.component.approval;

import java.nio.file.Path;
import com.clougence.utils.function.EFunction;

public interface ApprovalService {

    void checkSqlFile(long attachmentId, String ownerUid);

    <T> T consumeSqlFile(long approvalId, EFunction<Path, T, Exception> visitor);

    void confirmSqlFile(long approvalId, long attachmentId, String userUID);
}
