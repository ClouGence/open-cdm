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
package com.clougence.drivers.adapter.lob;

/**
 * Objects that want to be notified of lifecycle events on a JdbcWatchableOutputStream should implement this interface,
 * and register themselves with setWatcher() on the JdbcWatchableOutputStream instance.
 */
public interface JdbcOutputStreamWatcher {

    /**
     * Called when the OutputStream being watched has .close() called
     * @param out {@link JdbcWatchableStream}
     */
    void streamClosed(JdbcWatchableStream out);
}
