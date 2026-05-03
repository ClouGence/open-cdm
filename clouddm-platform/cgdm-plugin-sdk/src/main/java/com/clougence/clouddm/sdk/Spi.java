package com.clougence.clouddm.sdk;

public interface Spi {

    default String name() {
        return "";
    }
}
