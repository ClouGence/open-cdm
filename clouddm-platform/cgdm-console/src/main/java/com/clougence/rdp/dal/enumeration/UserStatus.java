package com.clougence.rdp.dal.enumeration;

/**
 * @author bucketli 2021/9/9 15:00
 */
public enum UserStatus {
    NORMAL,

    LOCKED,

    /**
     * user is in heavy change task,e.g.,one user take over another
     */
    UPDATE_DISABLE
}
