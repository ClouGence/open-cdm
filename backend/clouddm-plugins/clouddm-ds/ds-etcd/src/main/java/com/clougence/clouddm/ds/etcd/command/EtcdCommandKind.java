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
package com.clougence.clouddm.ds.etcd.command;

import com.clougence.clouddm.sdk.sql.analysis.behavior.BehaviorAction;
import com.clougence.clouddm.sdk.sql.parser.SplitQueryType;

public enum EtcdCommandKind {

    GET("get", SplitQueryType.SELECT, BehaviorAction.READ),
    PUT("put", SplitQueryType.UPDATE, BehaviorAction.REPLACE),
    DEL("del", SplitQueryType.DELETE, BehaviorAction.DELETE);

    private final String         command;
    private final SplitQueryType queryType;
    private final BehaviorAction action;

    EtcdCommandKind(String command, SplitQueryType queryType, BehaviorAction action){
        this.command = command;
        this.queryType = queryType;
        this.action = action;
    }

    public String command() {
        return this.command;
    }

    public SplitQueryType queryType() {
        return this.queryType;
    }

    public BehaviorAction action() {
        return this.action;
    }

    public static EtcdCommandKind find(String token) {
        if (token == null) {
            return null;
        }
        for (EtcdCommandKind kind : values()) {
            if (kind.command.equalsIgnoreCase(token)) {
                return kind;
            }
        }
        return null;
    }
}
