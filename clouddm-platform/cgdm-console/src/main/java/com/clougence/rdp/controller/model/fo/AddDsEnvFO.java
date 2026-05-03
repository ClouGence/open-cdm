package com.clougence.rdp.controller.model.fo;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import lombok.Data;

/**
 * @author wanshao create time is 2021/1/18
 **/
@Data
public class AddDsEnvFO {

    @NotBlank(message = "{notblank.envname}")
    private String envName;

    @Size(max = 500, message = "description length need less than 500")
    private String description;

    @Min(value = 1, message = "{min.querylimit}")
    private Long   queryLimit;
}
