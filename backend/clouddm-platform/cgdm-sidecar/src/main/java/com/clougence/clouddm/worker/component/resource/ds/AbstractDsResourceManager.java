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
package com.clougence.clouddm.worker.component.resource.ds;

import java.io.IOException;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

import jakarta.annotation.Resource;

import com.clougence.clouddm.api.sidecar.session.drivers.DriverRef;
import com.clougence.clouddm.api.sidecar.session.drivers.DriverUtils;
import com.clougence.clouddm.base.metadata.ds.DataSourceConfig;
import com.clougence.clouddm.base.metadata.rdp.enumeration.SecurityFileType;
import com.clougence.clouddm.platform.plugin.DsPluginInfo;
import com.clougence.clouddm.platform.plugin.PluginManager;
import com.clougence.clouddm.platform.plugin.info.LeasedDsFactory;
import com.clougence.clouddm.sdk.execute.resource.DsResourceManager;
import com.clougence.clouddm.worker.component.resource.file.FileResourceManagerImpl;
import com.clougence.drivers.DriverLoader;
import com.clougence.drivers.DsConfigKeys;
import com.clougence.drivers.DsFactory;
import com.clougence.drivers.DsObject;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public abstract class AbstractDsResourceManager implements DsResourceManager {

    @Resource
    private FileResourceManagerImpl          resourceManager;
    private final Map<String, AtomicInteger> resourceCounter = new ConcurrentHashMap<>();
    private final Map<String, Integer>       resourceLimited = new ConcurrentHashMap<>();

    @Override
    public <C extends DataSourceConfig, T extends AutoCloseable> DsObject<T> requestResource(C dsConfig) throws Exception {
        DriverLoader driverLoader = PluginManager.driverLoader();
        DriverRef driverRef = DriverUtils.parseDriverRef(dsConfig.getDriverVersion());
        if (driverLoader.findDriver(driverRef.getDriverFamily(), driverRef.getDriverVersion()) == null) {
            String msg = "dsType '" + dsConfig.getDataSourceType() + "', driverVersion '" + dsConfig.getDriverVersion() + "'";
            throw new RuntimeException(msg + ", no matching Driver exists.");
        }

        final String dsId = dsConfig.getInstanceId();
        Integer limit = this.resourceLimited.get(dsId);
        if (limit == null) {
            synchronized (this) {
                limit = this.resourceLimited.get(dsId);
                if (limit == null) {
                    limit = getMaxConnections(dsConfig);
                    log.info("requestResource '" + dsId + "' limit is " + limit);
                    this.resourceLimited.put(dsId, limit);
                    this.resourceCounter.put(dsId, new AtomicInteger(0));
                }
            }
        }

        final AtomicInteger counter = this.resourceCounter.get(dsId);
        synchronized (this) {
            if (limit > 0 && counter.get() >= limit) {
                throw new IndexOutOfBoundsException("the ds '" + dsId + "' usage reached " + counter.get() + "/" + limit);
            } else {
                counter.incrementAndGet();//++
            }
        }

        DsPluginInfo pluginInfo = PluginManager.findDsPlugin(dsConfig.getDataSourceType());
        if (pluginInfo == null) {
            throw new UnsupportedOperationException("no plugin found for dsType '" + dsConfig.getDataSourceType() + "'.");
        }

        try (LeasedDsFactory<?> dsFactory = pluginInfo.createDriver(driverRef.getDriverFamily(), driverRef.getDriverVersion())) {
            Properties properties = dsConfig.asDriverProperties();
            properties.setProperty(DsConfigKeys.CLIENT_NAME.getConfigKey(), "CloudDM Client");
            DsObject<T> apply = (DsObject<T>) dsFactory.create(properties);
            apply.addCloseListener(() -> doClose(dsId, counter));//--
            return apply;
        } catch (Exception e) {
            counter.decrementAndGet();//--
            throw e;
        }
    }

    private void doClose(String dsId, AtomicInteger counter) {
        counter.decrementAndGet();

        if (counter.get() <= 0) {
            this.resourceLimited.remove(dsId);
            this.resourceCounter.remove(dsId);
        }
    }

    protected int getMaxConnections(DataSourceConfig dsConfig) {
        return this.isTask() ? dsConfig.getExportMaxConnections() : dsConfig.getOnlineMaxConnections();
    }

    @Override
    public String getSecurityFilePath(DataSourceConfig config, String fileName, SecurityFileType fileType) throws IOException {
        return this.resourceManager.getFilePath(config, fileName, fileType);
    }
}
