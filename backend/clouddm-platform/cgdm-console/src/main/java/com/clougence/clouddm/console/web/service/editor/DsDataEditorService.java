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

import com.clougence.clouddm.console.web.component.dsconfig.mode.DsLevels;
import com.clougence.clouddm.console.web.model.fo.editor.data.ExecuteSqlFO;
import com.clougence.clouddm.console.web.model.fo.editor.data.GenerateDataFO;
import com.clougence.clouddm.console.web.model.fo.editor.data.SelectCountFO;
import com.clougence.clouddm.console.web.model.fo.editor.data.SelectDataFO;
import com.clougence.clouddm.console.web.model.vo.editor.data.DataEditorResultVO;
import com.clougence.clouddm.console.web.service.editor.model.DataEditorChangeDTO;
import com.clougence.clouddm.console.web.service.editor.model.DataEditorExecuteResultDTO;

public interface DsDataEditorService {

    /** for service API '/editor/data/fetchData' */
    DataEditorResultVO fetchData(String puid, String uid, String clientIp, DsLevels levels, SelectDataFO selectFO);

    /** for service API '/editor/data/fetchCount' */
    long fetchCount(String puid, String uid, DsLevels levels, SelectCountFO selectFO, String clientIp);

    /** for service API '/editor/data/generateDml' */
    List<DataEditorChangeDTO> generateDml(String puid, String uid, DsLevels levels, GenerateDataFO changeFO);

    /** for service API '/editor/data/saveData' */
    DataEditorExecuteResultDTO saveData(String puid, String uid, DsLevels levels, ExecuteSqlFO execFO, String clientIp);
}
