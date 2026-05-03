package com.clougence.rdp.service.model;

import lombok.Getter;
import lombok.Setter;

/**
 * @author bucketli 2021/1/11 10:34
 */
@Getter
@Setter
public class CheckSubAccountMO {

    boolean success;

    String  errorMsg;

    public CheckSubAccountMO(boolean success, String errorMsg){
        this.success = success;
        this.errorMsg = errorMsg;
    }
}
