/*
 * Copyright 2008-2009 the original author or authors.
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
import java.io.InputStream;
import java.io.Reader;

public class HexInputStreamReader extends Reader {

    private final InputStream   inputStream;
    private final StringBuilder hexStringBuilder = new StringBuilder();
    private int                 currentPos       = 0;

    public HexInputStreamReader(InputStream inputStream) throws IOException{
        this.inputStream = inputStream;
        readAndConvertToHex();
    }

    private void readAndConvertToHex() throws IOException {
        byte[] buffer = new byte[1024];
        int bytesRead;
        while ((bytesRead = this.inputStream.read(buffer)) != -1) {
            for (int i = 0; i < bytesRead; i++) {
                String hexString = Integer.toHexString(buffer[i] & 0xFF).toUpperCase();
                if (hexString.length() == 1) {
                    this.hexStringBuilder.append('0');
                }
                this.hexStringBuilder.append(hexString);
            }
        }
    }

    @Override
    public int read(char[] cbuf, int off, int len) throws IOException {
        if (this.currentPos >= this.hexStringBuilder.length()) {
            return -1;
        }
        int available = this.hexStringBuilder.length() - currentPos;
        int toRead = Math.min(available, len);
        this.hexStringBuilder.getChars(this.currentPos, this.currentPos + toRead, cbuf, off);
        this.currentPos += toRead;
        return toRead;
    }

    @Override
    public void close() throws IOException {
        this.inputStream.close();
    }
}
