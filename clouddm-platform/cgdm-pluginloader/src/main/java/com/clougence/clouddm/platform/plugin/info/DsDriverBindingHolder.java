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
package com.clougence.clouddm.platform.plugin.info;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

import com.clougence.clouddm.sdk.execute.session.SessionFactory;
import com.clougence.drivers.DriverBinding;
import com.clougence.drivers.DsFactory;

final class DsDriverBindingHolder {

    private final String        key;
    private DriverBinding       binding;
    private DsFactory<?>        driverFactory;
    private SessionFactory<?>   sessionFactory;
    private final AtomicInteger refCount = new AtomicInteger();
    private final AtomicBoolean retired  = new AtomicBoolean();

    public DsDriverBindingHolder(String key, DriverBinding binding){
        this.key = key;
        this.binding = binding;
    }

    public synchronized DsFactory<?> getOrCreateDriverFactory(String dsFactoryName) throws Exception {
        if (this.driverFactory != null) {
            return this.driverFactory;
        }

        DriverBinding currentBinding = requireBinding();
        Class<?> factoryType = currentBinding.asClassLoader().loadClass(dsFactoryName);
        if (!DsFactory.class.isAssignableFrom(factoryType)) {
            throw new IllegalStateException(dsFactoryName + " is not a DsFactory, driverVersion='" + this.key + "'.");
        }

        this.driverFactory = (DsFactory<?>) factoryType.getDeclaredConstructor().newInstance();
        return this.driverFactory;
    }

    public synchronized SessionFactory<?> getOrCreateSessionFactory(String sessionFactoryName) throws Exception {
        if (this.sessionFactory != null) {
            return this.sessionFactory;
        }

        DriverBinding currentBinding = requireBinding();
        Class<?> sessionFactoryType = currentBinding.asClassLoader().loadClass(sessionFactoryName);
        if (!SessionFactory.class.isAssignableFrom(sessionFactoryType)) {
            throw new IllegalStateException(sessionFactoryName + " is not a SessionFactory, driverVersion='" + this.key + "'.");
        }

        this.sessionFactory = (SessionFactory<?>) sessionFactoryType.getDeclaredConstructor().newInstance();
        return this.sessionFactory;
    }

    public void retain() {
        this.refCount.incrementAndGet();
    }

    public void retire() {
        this.retired.set(true);
        tryCleanup();
    }

    public boolean isExpired() {
        DriverBinding currentBinding = this.binding;
        return currentBinding == null || currentBinding.isExpired();
    }

    void release() {
        int current = this.refCount.decrementAndGet();
        if (current < 0) {
            this.refCount.incrementAndGet();
            throw new IllegalStateException("driver binding refCount is negative, driverVersion='" + this.key + "'.");
        }
        tryCleanup();
    }

    private synchronized void tryCleanup() {
        if (!this.retired.get() || this.refCount.get() > 0) {
            return;
        }

        this.driverFactory = null;
        this.sessionFactory = null;
        this.binding = null;
    }

    private DriverBinding requireBinding() {
        DriverBinding currentBinding = this.binding;
        if (currentBinding == null) {
            throw new IllegalStateException("driver binding has been cleaned, driverVersion='" + this.key + "'.");
        }
        return currentBinding;
    }

}
