package com.clougence.clouddm.api.sidecar.session.drivers;

import lombok.Getter;

@Getter
public class DriverRef {

    private final String driverFamily;
    private final String driverVersion;

    public DriverRef(String driverFamily, String driverVersion){
        this.driverFamily = driverFamily;
        this.driverVersion = driverVersion;
    }
}
