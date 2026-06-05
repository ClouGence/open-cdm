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
package com.clougence.clouddm.api.sidecar.session.execute;

import com.clougence.clouddm.comm.RSocketApiClass;
import com.clougence.clouddm.comm.model.RSocketSendDTO;
import com.clougence.clouddm.sdk.execute.resultset.file.DmFileType;

@RSocketApiClass
public interface ResultSetRService {

    String convertFile(RSocketSendDTO dto, String puid, String userId, String srcFileId, String exportId, DmFileType dmFileType, String srcFile, String dstFile, String formatName,
                       String option);

    void deleteFile(RSocketSendDTO sendDTO, String filePath, boolean tempFile);

    long fileSize(RSocketSendDTO sendDTO, String filePath);

    ResultFileReadDTO fileRead(RSocketSendDTO sendDTO, String filePath, long offset, int length);

    ResultPageDTO resultPageRead(RSocketSendDTO sendDTO, String filePath, long rowOffset, int pageSize);

    ResultColDTO resultDataRead(RSocketSendDTO sendDTO, String filePath, long rowNumber, long colNumber, long offset, int length);
}
