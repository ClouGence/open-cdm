package com.clougence.rdp.controller.model.fo;

import javax.validation.constraints.NotBlank;

import lombok.Getter;
import lombok.Setter;

/**
 * @author mode 2021/1/11 10:34
 */
@Getter
@Setter
public class CheckSubAccountBindInfoFO extends CheckSubAccountFO {

    @NotBlank(message = "{notblank.primaryuid}")
    private String primaryUid;
}
