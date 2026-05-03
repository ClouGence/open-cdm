package com.clougence.clouddm.console.web.model.fo.checkrules;

import javax.validation.constraints.NotBlank;

import lombok.Getter;
import lombok.Setter;

/**
 * @author mode 2021/1/8 15:01
 */
@Getter
@Setter
public class SpecUpdateInfoFO {

    private long   specId;

    @NotBlank(message = "{checkrules.specname.notblank}")
    private String newName;

    private String newDesc;
}
