package com.clougence.rdp.controller.model.lo;

import lombok.Getter;
import lombok.Setter;

/**
 * @author chunlin create time is 2024/8/2
 */
@Getter
@Setter
public class UpsertUserConfigLO {

    private String  configName;
    private String  oldConfigValue;
    private String  configValue;
    private Boolean needCreate;
}
