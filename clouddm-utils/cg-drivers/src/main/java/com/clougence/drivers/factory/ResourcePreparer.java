package com.clougence.drivers.factory;

import com.clougence.drivers.DriverPrepareProgress;
import com.clougence.drivers.DriverVersion;
import com.clougence.drivers.def.ResDef;

public interface ResourcePreparer {

    DriverPrepareProgress NONE = new DriverPrepareProgress() {
    };

    void analysis(DriverVersion driverVersion, ResDef resDef, ClassLoader classLoader, DriverPrepareProgress progress) throws Exception;

    void refresh(DriverVersion driverVersion, ResDef resDef, ClassLoader classLoader, DriverPrepareProgress progress) throws Exception;

    void resolve(DriverVersion driverVersion, ResDef resDef, ClassLoader classLoader, DriverPrepareProgress progress) throws Exception;
}
