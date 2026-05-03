package com.clougence.clouddm.api.console.notify;

import com.clougence.clouddm.comm.RSocketApiClass;

@RSocketApiClass
public interface NotifyRService {

    void notifyConvertFailed(String puid, String userId, String srcFileId, String exportId, String message);

    void notifyConvertFinish(String puid, String userId, String srcFileId, String exportId, String message, long total);

    void notifyConvertProgress(String puid, String userId, String srcFileId, String exportId, String message, long from, long to, long current);
}
