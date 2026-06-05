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
package com.clougence.clouddm.console.web.model.fo.editor;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public abstract class WsRequestFO {

    @JsonIgnore
    private String       channelKey;
    private String       primaryUserId;
    private String       currentUserId;
    private long         requestTime;
    private String       clientIp;

    private List<String> levels;
    private int          basicCodeLine;
    private int          basicCodeColumn;

    @JsonIgnore
    public String resultOriginal() {
        return "";
    }

    @JsonIgnore
    public String resultSessionId() {
        return "";
    }

    @JsonIgnore
    public String resultRequestId() {
        return null;
    }
}
