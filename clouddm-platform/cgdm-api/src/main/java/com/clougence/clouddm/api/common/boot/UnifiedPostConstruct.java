package com.clougence.clouddm.api.common.boot;

public interface UnifiedPostConstruct {

    void init() throws Exception;

    void stop();
}
