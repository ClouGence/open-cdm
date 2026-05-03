/*
 * Copyright 2015-2022 the original author or authors.
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
package com.clougence.drivers.adapter.lob;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * Objects that want to be notified of lifecycle events on a WatchableOutputStream should implement this interface,
 * and register themselves with setWatcher() on the WatchableOutputStream instance.
 */
public class JdbcWatchableOutputStream extends ByteArrayOutputStream implements JdbcWatchableStream {

    private JdbcOutputStreamWatcher watcher;

    /** @see java.io.OutputStream#close() */
    @Override
    public void close() throws IOException {
        super.close();

        if (this.watcher != null) {
            this.watcher.streamClosed(this);
        }
    }

    public void setWatcher(JdbcOutputStreamWatcher watcher) { this.watcher = watcher; }
}
