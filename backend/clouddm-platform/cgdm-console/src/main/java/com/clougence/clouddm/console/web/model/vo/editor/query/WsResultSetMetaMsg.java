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
package com.clougence.clouddm.console.web.model.vo.editor.query;

import java.util.List;

import com.clougence.clouddm.sdk.execute.resultset.echo.ReceiveMode;

import lombok.Getter;
import lombok.Setter;

/**
 * @author bucketli 2022/3/23 18:42:25
 */
@Getter
@Setter
public class WsResultSetMetaMsg extends WsQueryResult {

    private String       resultId;
    private String       resource;
    private List<String> columnList;
    private List<String> columnType;
    private ReceiveMode  receiveMode;

    private String       receiveCost;
    private String       cacheFile;
    private String       querySql;
    private String       original;
    private List<String> rewriteTags;
}
