package com.clougence.rdp.service;

/**
 * @author bucketli 2021/1/7 10:35
 */
public interface RdpMailService {

    void sendEmail(String mail, String subject, String content);
}
