package com.clougence.drivers.factory;

import java.io.File;
import java.util.Properties;

import com.clougence.drivers.DriverBinding;
import com.clougence.drivers.def.VerDef;
import com.clougence.drivers.factory.prepare.ClassResourcePreparer;
import com.clougence.drivers.factory.prepare.FileResourcePreparer;

/**
 * Load external datasource service jar for clouddm
 *
 * @author mode 2021/01/11
 */
public class DefaultDriverLoader extends AbstractDriverLoader {

    public DefaultDriverLoader(File localDir, Properties config){
        super(localDir, config);

        registerPreparer("class", ClassResourcePreparer::new);
        registerPreparer("file", FileResourcePreparer::new);
    }

    @Override
    public DriverBinding createBinding(ClassLoader parent, String driverFamily, String driverVersion) {
        String logMessage = ", driverFamily=" + driverFamily + ", driverVersion=" + driverVersion;
        VerDef matchedVersion = findDriver(driverFamily, driverVersion);
        if (matchedVersion == null) {
            throw new IllegalArgumentException("driver definition not found" + logMessage);
        }

        return new DriverBindingImpl(parent, matchedVersion);
    }
}
