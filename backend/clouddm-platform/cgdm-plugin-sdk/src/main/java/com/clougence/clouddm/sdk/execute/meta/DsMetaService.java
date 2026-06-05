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
package com.clougence.clouddm.sdk.execute.meta;

import java.util.List;
import java.util.Map;

import com.clougence.schema.umi.special.rdb.RdbColumn;
import com.clougence.schema.umi.struts.UmiTypes;
import com.clougence.schema.umi.struts.Value;

public interface DsMetaService {

    void testConnect();

    String getVersion();

    /** If not supported, null should be returned */
    String getCurrentCatalog();

    /** If not supported, null should be returned */
    String getCurrentSchema();

    List<DsElement> listLevels(List<UmiTypes> levels, Map<UmiTypes, Object> levelsParam);

    DsElement detailLevel(List<UmiTypes> levels, Map<UmiTypes, Object> levelsParam);

    List<DsElement> listLeaf(Map<UmiTypes, Object> levelsParam, UmiTypes leafType, String pattern);

    Value detailLeaf(Map<UmiTypes, Object> levelsParam, UmiTypes leafType, String leafName);

    default Value fetchSelectObject(Map<UmiTypes, Object> levelsParam, String leafName) {
        throw new UnsupportedOperationException();
    }

    Map<String, List<RdbColumn>> batchColumns(Map<UmiTypes, Object> levelsParam, UmiTypes leafType, List<String> leafNames);

    String loadTableEditor(Map<UmiTypes, Object> levelsParam, String table);

    List<String> requestObjectScript(Map<UmiTypes, Object> levelsParam, UmiTypes leafType, String leafName);
}
