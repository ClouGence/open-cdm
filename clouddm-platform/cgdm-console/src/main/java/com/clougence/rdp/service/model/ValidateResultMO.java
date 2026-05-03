package com.clougence.rdp.service.model;

import lombok.Getter;
import lombok.Setter;

/**
 * @author bucketli 2021/1/11 10:08
 */
@Getter
@Setter
public class ValidateResultMO {

    boolean success;

    String  errorMsg;

    public ValidateResultMO(boolean success, String errorMsg){
        this.success = success;
        this.errorMsg = errorMsg;
    }
}
