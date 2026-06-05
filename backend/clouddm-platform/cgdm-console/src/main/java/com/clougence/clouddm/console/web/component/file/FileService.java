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
package com.clougence.clouddm.console.web.component.file;

import com.clougence.clouddm.console.web.service.editor.model.DataResultDataVO;
import com.clougence.clouddm.console.web.service.editor.model.DataResultPageVO;
import com.clougence.clouddm.sdk.execute.resultset.file.DmFileType;

/**
 * @author mode 2020-01-20 21:04
 * @since 1.1.3
 */
public interface FileService {

    String submitFileConvert(String puid, String userId, String wsn, String srcFileId, String exportId, DmFileType dmFileType, String srcFile, String dstFile, String formatName,
                             String option);

    String fetchFileExtensionByFormatName(String dstFormatName);

    void deleteFile(String wsn, String filePath);

    void deleteTemp(String wsn, String filePath);

    /** for service API '/query/exportResult' */
    long fetchFileSize(String wsn, String filePath);

    /** for service API '/query/exportResult' */
    byte[] fetchFileData(String wsn, String filePath, long offset, int length);

    /** for service API '/query/fetchResultPage' */
    DataResultPageVO fetchResultPage(String wsn, String path, long offsetRow, int pageSize);

    /** for service API '/query/fetchResultData' */
    DataResultDataVO fetchResultCol(String wsn, String path, long rowNumber, long colNumber, long offset, int length);
}
