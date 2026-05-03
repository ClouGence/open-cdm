package com.clougence.clouddm.sdk.execute.dsconf;

import java.util.Map;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DsConfigMap {

    private Map<String, String> defaultConfig;
    private Map<String, Object> rdpDsBean;
    private Map<String, Object> rdpExtraBean;
}
