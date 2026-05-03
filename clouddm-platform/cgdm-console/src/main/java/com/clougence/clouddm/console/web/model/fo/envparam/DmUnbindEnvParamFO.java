package com.clougence.clouddm.console.web.model.fo.envparam;

import javax.validation.constraints.NotBlank;

import lombok.Getter;
import lombok.Setter;

/**
 * @Author: Ekko
 * @Date: 2024-05-31 10:08
 */
@Getter
@Setter
public class DmUnbindEnvParamFO {

    private long   envId;

    @NotBlank(message = "paramKey can not be blank.")
    private String paramKey;
}
