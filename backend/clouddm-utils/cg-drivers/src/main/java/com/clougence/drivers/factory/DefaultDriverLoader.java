/*
 * Copyright 2026 杭州开云集致科技有限公司
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
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
