package com.clougence.drivers;

import com.clougence.drivers.def.ResDef;

public interface DriverPrepareProgress {

    default void onStart(DriverVersion driverVersion, ResDef resDef, int index, int totalCount) {
    }

    default void onProgress(DriverVersion driverVersion, ResDef resDef, String fileName, long current, long total) {
    }

    default void onComplete(DriverVersion driverVersion, ResDef resource, int index, int totalCount) {
    }

    default void onError(DriverVersion driverVersion, ResDef resource, Exception exception) {
    }
}
