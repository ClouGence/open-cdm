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
