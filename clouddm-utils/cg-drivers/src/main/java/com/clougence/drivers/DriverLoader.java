package com.clougence.drivers;

import java.io.File;
import java.io.InputStream;
import java.util.List;
import java.util.function.Predicate;

import com.clougence.drivers.def.ResDef;
import com.clougence.drivers.factory.ResourcePreparerFactory;

/**
 * Load external datasource service jar for clouddm
 *
 * @author mode 2021/01/11
 */
public interface DriverLoader {

    List<String> familyNames();

    DriverFamily findDriver(String driverFamily);

    DriverVersion findDriver(String driverFamily, String driverVersion);

    DriverFamily addDriver(String driverFamily);

    File getDriverHome();

    //

    void refreshDriverVersion(DriverVersion driverVersion);

    void prepareDriverVersion(DriverVersion driverVersion, Predicate<ResDef> skip, DriverPrepareProgress progress);

    //

    void loadDriverXml(InputStream xmlInputStream);

    void loadDsFactory(ClassLoader classLoader) throws Exception;

    void registerPreparer(String resourceType, ResourcePreparerFactory preparerFactory);

    void unregisterPreparer(String resourceType);

    DriverBinding createBinding(ClassLoader parent, String driverFamily, String driverVersion);
}
