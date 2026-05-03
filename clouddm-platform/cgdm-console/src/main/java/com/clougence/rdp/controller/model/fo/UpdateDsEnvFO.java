package com.clougence.rdp.controller.model.fo;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import lombok.Data;

/**
 * @author wanshao create time is 2021/1/18
 **/
@Data
public class UpdateDsEnvFO {

    @Min(value = 1, message = "{min.dsenvid}")
    private Long   dsEnvId;

    @NotBlank(message = "{notblank.envname}")
    private String envName;

    @Size(max = 500, message = "{max.description}")
    private String description;

}
