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

import com.clougence.utils.io.IOUtils;

/**
 * 具有自动关闭的输入流
 * @author 赵永春 (zyc@hasor.net)
 * @version 2009-5-13
 */
public class AutoCloseReader extends ProxyReader {

    /**
     * Creates an automatically closing proxy for the given input stream.
     * @param in underlying input stream
     */
    public AutoCloseReader(final Reader in){
        super(in);
    }

    /**
     * Closes the underlying input stream and replaces the reference to it
     * with a {@link ClosedReader} instance.
     * <p>
     * This method is automatically called by the read methods when the end
     * of input has been reached.
     * <p>
     * Note that it is safe to call this method any number of times. The original
     * underlying input stream is closed and discarded only once when this
     * method is first called.
     * @throws IOException if the underlying input stream can not be closed
     */
    @Override
    public void close() throws IOException {
        in.close();
        in = ClosedReader.CLOSED_READER_STREAM;
    }

    /**
     * Automatically closes the stream if the end of stream was reached.
     * @param n number of bytes read, or -1 if no more bytes are available
     * @throws IOException if the stream could not be closed
     * @since 2.0
     */
    @Override
    protected void afterRead(final int n) throws IOException {
        if (n == IOUtils.EOF) {
            close();
        }
    }

    /**
     * Ensures that the stream is closed before it gets garbage-collected.
     * As mentioned in {@link #close()}, this is a no-op if the stream has
     * already been closed.
     * @throws Throwable if an error occurs
     */
    @Override
    @SuppressWarnings("removal")
    protected void finalize() throws Throwable {
        close();
        super.finalize();
    }
}
