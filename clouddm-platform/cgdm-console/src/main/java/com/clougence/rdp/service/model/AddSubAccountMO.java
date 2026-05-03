package com.clougence.rdp.service.model;

import lombok.Getter;
import lombok.Setter;

/**
 * @author bucketli 2021/1/11 10:34
 */
@Getter
@Setter
public class AddSubAccountMO {

    private boolean success;

    private String  subUid;

    private String  errorMsg;

    public AddSubAccountMO(boolean success){
        this.success = success;
    }

    public AddSubAccountMO(boolean success, String errorMsg){
        this.success = success;
        this.errorMsg = errorMsg;
    }

    public AddSubAccountMO(boolean success, String errorMsg, String subUid){
        this.success = success;
        this.errorMsg = errorMsg;
        this.subUid = subUid;
    }
}
