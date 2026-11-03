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
package com.clougence.clouddm.console.web.component.approval;

import java.nio.file.Path;

import com.clougence.utils.function.EFunction;

public interface ApprovalService {

    void checkSqlFile(long attachmentId, String ownerUid);

    <T> T consumeSqlFile(long approvalId, EFunction<Path, T, Exception> visitor);

    void confirmSqlFile(long approvalId, long attachmentId, String userUID);
}
