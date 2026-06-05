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
package com.clougence.clouddm.team.provider.feishu.domain.mo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class FeishuWidget {

    @JsonProperty("default_value_type")
    private String defaultValueType;

    @JsonProperty("display_condition")
    private String displayCondition;

    @JsonProperty("enable_default_value")
    private String enableDefaultValue;

    private String id;

    private String name;

    private String printable;

    private String required;

    private String type;

    private String visible;

    @JsonProperty("widget_default_value")
    private String widgetDefaultValue;

    private Object value;

    private List<FeishuWidget> children;

}
