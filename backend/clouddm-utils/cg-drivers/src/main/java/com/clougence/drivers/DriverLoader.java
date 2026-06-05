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
