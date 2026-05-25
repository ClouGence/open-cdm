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

import java.util.Properties;
import java.util.concurrent.atomic.AtomicBoolean;

import com.clougence.drivers.DsFactory;
import com.clougence.drivers.DsObject;

public final class LeasedDsFactory<T> implements DsFactory<T>, AutoCloseable {

    private final DsDriverBindingHolder holder;
    private final DsFactory<T>          delegate;
    private final AtomicBoolean         closed = new AtomicBoolean();

    LeasedDsFactory(DsDriverBindingHolder holder, DsFactory<T> delegate){
        this.holder = holder;
        this.delegate = delegate;
        this.holder.retain();
    }

    @Override
    public DsObject<T> create(Properties dsConfig) throws Exception {
        this.holder.retain();
        boolean success = false;
        try {
            DsObject<T> dsObject = this.delegate.create(dsConfig);
            dsObject.addCloseListener(this.holder::release);
            success = true;
            return dsObject;
        } finally {
            if (!success) {
                this.holder.release();
            }
        }
    }

    @Override
    public void close() {
        if (this.closed.compareAndSet(false, true)) {
            this.holder.release();
        }
    }
}
