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
package com.clougence.clouddm.sdk.ui.editor.property;

import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PropertyEditorUiData {

    private static final long   serialVersionUID = -1810374665700501556L;

    @JsonInclude()
    private Map<String, String> baseInfo         = new HashMap<>();

    //    @JsonInclude()
    //    private List<Map<String, Object>> columns          = new ArrayList<>();
    //
    //    @JsonInclude()
    //    private Map<String, Object>       keys             = null;
    //
    //    @JsonInclude()
    //    private List<Map<String, Object>> indexes          = new ArrayList<>();
    //
    //    @JsonInclude()
    //    private Map<String, Object>       partitions       = null;
    //
    //    @JsonInclude()
    //    private List<Map<String, Object>> constraints      = new ArrayList<>();
    //    @JsonInclude()
    //    private List<Map<String, Object>> foreignKeys      = new ArrayList<>();

}
