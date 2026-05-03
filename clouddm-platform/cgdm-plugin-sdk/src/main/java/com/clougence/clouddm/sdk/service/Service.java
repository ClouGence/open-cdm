package com.clougence.clouddm.sdk.service;

public interface Service {

    default String name() {
        return "";
    }
}
