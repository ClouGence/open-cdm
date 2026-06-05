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
package com.clougence.clouddm.api.console.sqlaudit;

import java.util.Date;
import java.util.List;

import com.clougence.clouddm.sdk.service.secrules.Requester;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SqlExecNotifyDTO {

    private Type         type;

    private SqlStatus    sqlStatus;
    private String       sql;
    private String       uid;
    private String       clientIp;
    private String       sessionId;
    private String       queryId;
    private Requester    requester;
    private long         line;
    private Long         dsId;
    private List<String> levels;

    private String       message;
    private Date         time;

    private boolean      isExplain;

    private boolean      rewrite;
    private List<String> rewriteTag;
    private String       originalSql;
}
