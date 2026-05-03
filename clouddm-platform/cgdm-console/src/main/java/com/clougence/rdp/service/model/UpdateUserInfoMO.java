package com.clougence.rdp.service.model;

import com.clougence.rdp.controller.model.lo.UpdateUserInfoLO;

import lombok.Getter;
import lombok.Setter;

/**
 * @author bucketli 2021/1/11 10:08
 */
@Getter
@Setter
public class UpdateUserInfoMO {

    private boolean          success;

    private String           errorMsg;

    private UpdateUserInfoLO configLO;

    public UpdateUserInfoMO(){
    }

    public UpdateUserInfoMO(boolean success, String errorMsg){
        this.success = success;
        this.errorMsg = errorMsg;
    }
}
