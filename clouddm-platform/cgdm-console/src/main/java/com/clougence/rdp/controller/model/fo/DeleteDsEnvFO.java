package com.clougence.rdp.controller.model.fo;

import javax.validation.constraints.Min;

import lombok.Data;

/**
 * @author wanshao create time is 2021/1/18
 **/
@Data
public class DeleteDsEnvFO {

    @Min(value = 1, message = "{min.dsenvid}")
    private Long dsEnvId;
}
