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
package com.clougence.clouddm.sdk.ui.editor.data.reload;

import java.util.List;
import java.util.Map;

import com.clougence.clouddm.sdk.execute.resultset.echo.ResultSetRow;
import com.clougence.clouddm.sdk.model.analysis.TargetType;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EditorResultSet {

    private long                          fetchTimeMs;
    private String                        resultId;

    private int                           fetchCount;

    private boolean                       success;
    private String                        message;

    private boolean                       updateCountResult;
    private long                          updateCount;

    private String                        sql;
    private List<Map<TargetType, String>> resource;
    private List<String>                  columnList;
    private List<String>                  columnType;
    private List<ResultSetRow>            rowSet;
    private String                        cacheFile;

    private List<Map<String, String>>     generatedKeys;
    private Map<String, String>           outParams;
}
