package com.clougence.clouddm.sdk;

public interface LifeSpi extends Spi {

    LifeSpiResponse start(String ownerUid, LifeSpiRequest requestDTO) throws Exception;

    LifeSpiResponse stop(String ownerUid, LifeSpiRequest requestDTO) throws Exception;

    LifeSpiResponse status(String ownerUid, LifeSpiRequest requestDTO);
}
