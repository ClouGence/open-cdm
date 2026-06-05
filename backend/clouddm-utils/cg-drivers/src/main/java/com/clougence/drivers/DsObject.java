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

import java.io.Closeable;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.clougence.utils.function.EConsumer;

/**
 * @author mode 2020/10/31 12:05
 */
public class DsObject<T> implements Closeable {

    private final static Logger            logger    = Logger.getLogger(DsObject.class.getName());
    protected final AtomicBoolean          closed    = new AtomicBoolean(false);
    private final Properties               dsConfig;
    private final DsFactory<T>             dsFactory;
    private final List<CloseEventListener> listeners = new ArrayList<>();
    private T                              target;
    private EConsumer<T, Exception>        closeable;

    public DsObject(Properties dsConfig, T target, DsFactory<T> dsFactory){
        this.dsConfig = dsConfig;
        this.target = target;
        this.dsFactory = dsFactory;
        if (target instanceof AutoCloseable) {
            this.closeable = t -> ((AutoCloseable) t).close();
        } else {
            this.closeable = null;
        }
    }

    public DsObject(Properties dsConfig, T target, DsFactory<T> dsFactory, EConsumer<T, Exception> closeable){
        this.dsConfig = dsConfig;
        this.target = target;
        this.dsFactory = dsFactory;
        this.closeable = closeable;
    }

    public Properties getDsConfig() { return dsConfig; }

    // get DataSource Object when available, (after close,return is null)
    public T getTarget() {
        if (this.closed.get()) {
            throw new RuntimeException("DsObject instance '" + dsConfig.getProperty(DsConfigKeys.ID.getConfigKey()) + "' has been closed");
        } else {
            return this.target;
        }
    }

    // out of resource control
    public DsObject<T> unsafeSimilar() throws Exception {
        if (this.closed.get()) {
            throw new RuntimeException("DsObject instance '" + dsConfig.getProperty(DsConfigKeys.ID.getConfigKey()) + "' has been closed");
        }

        return this.dsFactory.create(this.dsConfig);
    }

    public boolean isClose() { return this.closed.get(); }

    // force DataSource to close
    public void close() {
        if (this.closed.compareAndSet(false, true)) {
            try {
                if (this.closeable != null) {
                    this.closeable.eAccept(this.target);
                    logger.log(Level.SEVERE, "close ignored, the 'close' interface was not set.");
                } else {
                    logger.log(Level.INFO, "close dsObject.");
                }
            } catch (Exception e) {
                logger.log(Level.SEVERE, "close failed " + e.getMessage(), e);
            }

            this.listeners.forEach(listener -> {
                try {
                    listener.onClose();
                } catch (Exception e) {
                    logger.log(Level.SEVERE, "listener.onClose " + e.getMessage(), e);
                }
            });

            this.target = null;
            this.closeable = null;
        }
    }

    public void addCloseListener(CloseEventListener listener) {
        this.listeners.add(listener);
    }
}
