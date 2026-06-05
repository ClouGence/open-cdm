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
package com.clougence.clouddm.console.web.model.vo.openapi;

import java.util.List;

import com.clougence.clouddm.platform.dal.model.secrule.WarnLevel;
import com.clougence.clouddm.console.web.model.vo.editor.query.WsInfoResMsg;
import com.clougence.clouddm.console.web.model.vo.editor.query.WsRuleEntity;
import com.clougence.clouddm.sdk.execute.resultset.echo.ReceiveMode;
import com.clougence.clouddm.sdk.execute.resultset.echo.ResultSetRow;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DmApiQueryResultVO {

    // basic info
    private String             script;
    private String             original;
    private List<String>       rewriteTags;

    // rule check info
    private WarnLevel          ruleCheckLevel;
    private List<WsRuleEntity> ruleCheckList;

    // result info
    private String             resultId;
    private List<String>       columnList;
    private List<String>       columnType;
    private ReceiveMode        receiveMode;
    private String             cacheFile;

    // cost and data
    private String             receiveCost;
    private int                fetchCount;
    private List<ResultSetRow> rows;

    // message or error
    private List<WsInfoResMsg> msgList;

}
