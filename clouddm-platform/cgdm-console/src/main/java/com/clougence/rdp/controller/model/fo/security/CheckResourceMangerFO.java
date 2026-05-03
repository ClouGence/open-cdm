package com.clougence.rdp.controller.model.fo.security;

import javax.validation.constraints.NotNull;

import lombok.Getter;
import lombok.Setter;

/**
 * @author <a href="https://gitee.com/LongLiS">cloudconceal</a> 2024/11/19 11:22
 */
@Getter
@Setter
public class CheckResourceMangerFO {

    @NotNull(message = "targetUid can not be null.")
    private String targetUid;
}
