package com.clougence.rdp.controller.model.vo;

import lombok.Getter;
import lombok.Setter;

/**
 * @author bucketli 2021/1/8 21:42
 */
@Getter
@Setter
public class RoleInfoVO {

    private long    roleId;

    private String  roleName;

    private String  aliasName;

    private boolean innerTag;

}
