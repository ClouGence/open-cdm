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

import java.io.CharArrayWriter;

/**
 * A java.io.Writer used to write unicode data into Blobs and Clobs
 */
public class JdbcWatchableWriter extends CharArrayWriter {

    private JdbcWriterWatcher watcher;

    /** @see java.io.Writer#close() */
    @Override
    public void close() {
        super.close();

        // Send data to watcher
        if (this.watcher != null) {
            this.watcher.writerClosed(this);
        }
    }

    /** @param watcher {@link JdbcWriterWatcher} */
    public void setWatcher(JdbcWriterWatcher watcher) { this.watcher = watcher; }
}
