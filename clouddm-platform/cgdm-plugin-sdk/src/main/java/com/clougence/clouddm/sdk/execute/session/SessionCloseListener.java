package com.clougence.clouddm.sdk.execute.session;

@FunctionalInterface
public interface SessionCloseListener {

    void onClose(String sessionId);
}
