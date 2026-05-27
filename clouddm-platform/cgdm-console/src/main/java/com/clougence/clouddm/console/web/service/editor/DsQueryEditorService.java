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
package com.clougence.clouddm.console.web.service.editor;

import java.util.List;
import java.util.Map;

import com.clougence.clouddm.console.web.component.dsconfig.mode.DsLevels;
import com.clougence.clouddm.platform.dal.model.execution.DmExecFileDO;
import com.clougence.clouddm.console.web.model.vo.editor.query.SessionVO;
import com.clougence.clouddm.console.web.service.browse.model.rdb.BrowseColumnMO;
import com.clougence.clouddm.console.web.service.editor.model.DataResultDataVO;
import com.clougence.clouddm.console.web.service.editor.model.DataResultPageVO;
import com.clougence.clouddm.console.web.service.editor.model.DsAvailableDTO;
import com.clougence.clouddm.console.web.service.editor.model.FileSaveAsDTO;
import com.clougence.clouddm.sdk.execute.session.rdb.RdbIsolation;
import com.clougence.schema.umi.struts.UmiTypes;

public interface DsQueryEditorService {

    boolean hasMoreSessionQuota(String userId);

    int getMaxTxSessionUserQuota();

    /** for service API '/query/listsession' */
    List<SessionVO> getSessionList(String puid, String uid);

    /** for service API '/query/createsession' */
    String createSession(String curUid, List<String> levels, boolean autoCommit, RdbIsolation initIsolation);

    /** for service API '/query/closesession' */
    void closeSession(String puid, String uid, List<String> sessionIds);

    /** for service API '/query/rdbColumns' */
    Map<String, List<BrowseColumnMO>> rdbBatchColumns(String puid, String uid, DsLevels levels, UmiTypes leafType, List<String> leafNames);

    DsAvailableDTO availableDataSource(String puid, String uid, long dsId);

    DmExecFileDO queryUserFileByUniqueId(String puid, String uid, String resultId);

    /** for service API '/query/exportResult' */
    FileSaveAsDTO resultSetFileSaveAs(String puid, String uid, String resultId, String dstFileName, String dstFormatName, boolean autoName, String option);

    /** for service API '/query/closeResultWindow' */
    void removeResultSetCacheFile(String puid, String uid, String fileUniqueId);

    /** for service API '/query/exportResult' */
    long fetchFileSizeByUri(String puid, String uid, String fileUriStr);

    /** for service API '/query/exportResult' */
    long fetchFileSizeByUniqueId(String puid, String uid, String fileUniqueId);

    /** for service API '/query/exportResult' */
    byte[] fetchFileData(String puid, String uid, String fileUriStr, long offset, int length);

    /** for service API '/query/fetchResultPage' */
    DataResultPageVO fetchResultPage(String puid, String uid, String resultId, long offsetRow, int pageSize);

    /** for service API '/query/fetchResultData' */
    DataResultDataVO fetchResultData(String puid, String uid, String resultId, long rowNumber, long colNumber, long offset, int length);

}
