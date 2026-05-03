package com.clougence.clouddm.sdk.model.onlineddl;

/**
 * @author bucketli 2022/6/1 16:30:16
 */
public enum GhostCutOverType {

    DEFAULT("default"),
    ATOMIC("atomic");

    private final String val;

    GhostCutOverType(String val){
        this.val = val;
    }

    public String getVal() { return val; }
}
