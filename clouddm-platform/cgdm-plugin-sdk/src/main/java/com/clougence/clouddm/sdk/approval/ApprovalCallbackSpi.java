package com.clougence.clouddm.sdk.approval;

import java.util.Map;

import com.clougence.clouddm.sdk.Spi;

public interface ApprovalCallbackSpi extends Spi {

    Object handle(String ownerUid, Map<String, String> params);
}
