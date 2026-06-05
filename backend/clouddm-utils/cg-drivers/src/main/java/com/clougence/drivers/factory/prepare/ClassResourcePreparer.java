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
package com.clougence.drivers.factory.prepare;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
import java.util.Properties;

import com.clougence.drivers.DriverPrepareProgress;
import com.clougence.drivers.DriverVersion;
import com.clougence.drivers.def.ResDef;
import com.clougence.utils.StringUtils;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ClassResourcePreparer extends AbstractResourcePreparer {

    public ClassResourcePreparer(File localDir, Properties config){
        super(localDir, config);
    }

    @Override
    public void analysis(DriverVersion driverVersion, ResDef driverResource,//
                         ClassLoader classLoader, DriverPrepareProgress progress) {
        driverResource.setFileDefList(Collections.emptyList());
        driverResource.setPrepared(false);
    }

    @Override
    public void refresh(DriverVersion driverVersion, ResDef driverResource, ClassLoader classLoader, DriverPrepareProgress progress) {
        String classResource = driverResource.getCoordinate().replace(".", "//") + ".class";
        try (InputStream inputStream = classLoader.getResourceAsStream(classResource)) {
            driverResource.setPrepared(inputStream != null);
        } catch (IOException e) {
            log.error("Failed to load resource {}", classResource, e);
            driverResource.setPrepared(false);
        }
    }

    @Override
    public void resolve(DriverVersion driverVersion, ResDef driverResource,//
                        ClassLoader classLoader, DriverPrepareProgress progress) throws ClassNotFoundException {
        String coordinate = driverResource.getCoordinate().trim();
        if (StringUtils.isBlank(coordinate)) {
            throw new ClassNotFoundException("class coordinate is blank.");
        }

        classLoader.loadClass(coordinate);
    }
}
