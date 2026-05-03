package com.clougence.rdp.service.model;

import lombok.Getter;
import lombok.Setter;

/**
 * @author chunlin create time is 2024/7/18
 */
@Getter
@Setter
public class AddRoleMO {

    private boolean success;

    private Long    roleUid;

    private String  errorMsg;

    public AddRoleMO(boolean success){
        this.success = success;
    }

    public AddRoleMO(boolean success, String errorMsg){
        this.success = success;
        this.errorMsg = errorMsg;
    }

    public AddRoleMO(boolean success, Long roleUid){
        this.success = success;
        this.roleUid = roleUid;
    }
}
