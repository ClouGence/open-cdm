package com.clougence.rdp.service;

import com.clougence.rdp.controller.model.fo.AddWebViewLogFO;

/**
 * @author bucketli 2023/5/26 17:11:26
 */
public interface RdpWebViewLogService {

    Integer KEY_WORD_CONTENT_LENGTH  = 255;

    Integer SRC_CONTENT_LENGTH       = 127;

    Integer CLIENT_ID_CONTENT_LENGTH = 255;

    Integer VB_ID_CONTENT_LENGTH     = 512;

    void addOneLog(AddWebViewLogFO logFO, String uid);
}
