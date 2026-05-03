package com.clougence.rdp.controller.model.fo;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import com.clougence.rdp.controller.model.enumeration.CheckSubAccountType;

import lombok.Getter;
import lombok.Setter;

/**
 * @author bucketli 2021/1/11 10:34
 */
@Getter
@Setter
public class CheckSubAccountFO {

    @NotBlank(message = "{notblank.checkcontent}")
    private String              checkContent;

    @NotNull(message = "{notnull.checktype}")
    private CheckSubAccountType checkType;
}
