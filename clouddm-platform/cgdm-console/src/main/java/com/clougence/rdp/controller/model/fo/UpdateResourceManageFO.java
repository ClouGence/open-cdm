package com.clougence.rdp.controller.model.fo;

import javax.validation.constraints.NotNull;

import lombok.Data;

/**
 * @author <a href="https://gitee.com/LongLiS">cloudconceal</a> 2024/10/18 11:41
 */
@Data
public class UpdateResourceManageFO {

    @NotNull(message = "")
    private String  targetUid;

    @NotNull(message = "")
    private boolean resourceManage;

}
