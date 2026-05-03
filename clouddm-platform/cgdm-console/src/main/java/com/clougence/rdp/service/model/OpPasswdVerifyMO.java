package com.clougence.rdp.service.model;

import com.clougence.rdp.service.enumeration.OpVerifyErrType;

import lombok.Getter;
import lombok.Setter;

/**
 * @author bucketli 2021/1/11 11:18
 */
@Getter
@Setter
public class OpPasswdVerifyMO {

    boolean         success;

    OpVerifyErrType opVerifyErrType;

    String          errorMsg;

    public OpPasswdVerifyMO(boolean success){
        this.success = success;
    }

    public OpPasswdVerifyMO(boolean success, OpVerifyErrType opVerifyErrType, String errorMsg){
        this.success = success;
        this.opVerifyErrType = opVerifyErrType;
        this.errorMsg = errorMsg;
    }
}
