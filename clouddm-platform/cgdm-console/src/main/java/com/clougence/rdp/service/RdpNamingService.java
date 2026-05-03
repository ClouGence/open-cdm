package com.clougence.rdp.service;

/**
 * @author bucketli 2020/7/10 21:14
 */
public interface RdpNamingService {

    String genAccessKey();

    String genSecretKey();

    String genUid();

    /**
     * @return random password for inner user
     */
    String genInnerUserPwd();

    String genProductClusterName();

    String genProductClusterCode();

    String genConsoleJobToken();
}
