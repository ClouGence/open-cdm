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
