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
package com.clougence.utils.io.input;

import java.io.IOException;
import java.io.Reader;

/**
 * Closed reader. This reader returns -1 to all attempts to read something from the reader.
 * <p>
 * Typically uses of this class include testing for corner cases in methods
 * that accept input streams and acting as a sentinel value instead of a
 * {@code null} reader.
 * @author 赵永春 (zyc@hasor.net)
 */
public class ClosedReader extends Reader {

    /**
     * A singleton.
     */
    public static final ClosedReader CLOSED_READER_STREAM = new ClosedReader();

    /**
     * Returns -1 to indicate that the stream is closed.
     * @return always -1
     */
    @Override
    public int read() {
        return -1;
    }

    @Override
    public int read(char[] cbuf, int off, int len) throws IOException {
        return -1;
    }

    @Override
    public void close() throws IOException {

    }
}
