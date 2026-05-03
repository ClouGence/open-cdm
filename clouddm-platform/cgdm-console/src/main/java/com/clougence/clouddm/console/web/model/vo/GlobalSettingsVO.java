package com.clougence.clouddm.console.web.model.vo;

import java.util.Map;

import com.clougence.clouddm.console.web.global.config.DmPersonalConfig;

import lombok.Getter;
import lombok.Setter;

/**
 * @author mode create time is 2020/4/13
 **/
@Getter
@Setter
public class GlobalSettingsVO {

    private String              buildVersion;
    private String              buildId;
    private DmPersonalConfig    personal;
    private Map<String, Object> productVersions;
}
