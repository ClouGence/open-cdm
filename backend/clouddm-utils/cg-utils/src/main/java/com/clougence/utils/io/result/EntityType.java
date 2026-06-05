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
package com.clougence.utils.io.result;

public interface EntityType {

    /** see: StorageCode  */
    byte Code       = (byte) 0x00;
    byte Boolean    = (byte) 0x01;
    byte Byte       = (byte) 0x02;
    byte Short      = (byte) 0x03;
    byte Integer    = (byte) 0x04;
    byte Long       = (byte) 0x05;
    byte BigInteger = (byte) 0x06;
    byte Float      = (byte) 0x07;
    byte Double     = (byte) 0x08;
    byte String     = (byte) 0x09;
    byte Bytes      = (byte) 0x0A;
    byte Date       = (byte) 0x0B;
    byte Time       = (byte) 0x0C;
    byte TimeZ      = (byte) 0x0D;
    byte DateTime   = (byte) 0x0E;
    byte DateTimeZ  = (byte) 0x0F;
    byte BigDecimal = (byte) 0x10;

    static boolean isTypeOf(byte header, byte typeCode) {
        return header == typeCode;
    }

    static boolean isType(byte header) {
        return header != Code;
    }

    static boolean isCode(byte header) {
        return header == Code;
    }

    static int lengthFieldBytes(byte header) {
        if (isType(header)) {
            return (byte) (header & 0x0F);
        }

        throw new IllegalArgumentException("header is not Type header.");
    }
}
