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
package com.clougence.clouddm.comm.util;

import java.io.ByteArrayOutputStream;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.zip.DataFormatException;
import java.util.zip.Deflater;
import java.util.zip.Inflater;

import com.clougence.utils.JsonUtils;
import com.fasterxml.jackson.core.StreamReadConstraints;

/**
 * @author wanshao create time is 2021/1/9
 **/
public class DeflateCompressorUtil {

    private static final int COMPRESSION_LEVEL = 9;

    static {
        // fix String length (20030999) exceeds the maximum length (20000000)
        StreamReadConstraints.overrideDefaultStreamReadConstraints(StreamReadConstraints.builder() //
            .maxNestingDepth(Integer.MAX_VALUE)
            .maxNumberLength(Integer.MAX_VALUE)
            .maxStringLength(Integer.MAX_VALUE)
            .build());
    }

    public static String compress(Object object) {
        String dtoJson = JsonUtils.toJson(object);
        byte[] uncompressedBytes = dtoJson.getBytes(StandardCharsets.UTF_8);
        byte[] compressedBytes = compress(uncompressedBytes);
        return Base64.getEncoder().encodeToString(compressedBytes);
    }

    public static Object decompress(String base64String, Class<?> methodParameterClassName) throws DataFormatException {
        byte[] compressedBytes = Base64.getDecoder().decode(base64String);
        byte[] uncomressedBytes = uncompress(compressedBytes);

        String dtoJson = new String(uncomressedBytes, StandardCharsets.UTF_8);
        return JsonUtils.toObj(dtoJson, methodParameterClassName);
    }

    public static byte[] compress(byte input[]) {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        Deflater compressor = new Deflater(COMPRESSION_LEVEL);
        try {
            compressor.setInput(input);
            compressor.finish();
            final byte[] buf = new byte[2048];
            while (!compressor.finished()) {
                int count = compressor.deflate(buf);
                bos.write(buf, 0, count);
            }
        } finally {
            compressor.end();
        }
        return bos.toByteArray();
    }

    public static byte[] uncompress(byte[] input) throws DataFormatException {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        Inflater decompressor = new Inflater();
        try {
            decompressor.setInput(input);
            final byte[] buf = new byte[2048];
            while (!decompressor.finished()) {
                int count = decompressor.inflate(buf);
                bos.write(buf, 0, count);
            }
        } finally {
            decompressor.end();
        }
        return bos.toByteArray();
    }
}
