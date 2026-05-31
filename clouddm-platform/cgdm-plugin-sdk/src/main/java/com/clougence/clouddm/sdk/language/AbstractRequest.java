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
package com.clougence.clouddm.sdk.language;

import java.util.Collections;
import java.util.Map;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public abstract class AbstractRequest {

    private String              requestId;
    private long                requestVersion;
    private Long                dataSourceId;
    private String              dsType;
    private String              catalog;
    private String              schema;
    private String              sqlText;
    private Map<String, Object> options = Collections.emptyMap();
}
