package com.clougence.rdp.controller.model.lo;

import lombok.Getter;
import lombok.Setter;

/**
 * @author chunlin create time is 2024/11/5
 */
@Getter
@Setter
public class UpdateDsConfigLO {

    private String  configName;
    private String  oldConfigValue;
    private String  configValue;
    private Boolean needCreate;
}
