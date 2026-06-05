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
 */package com.clougence.clouddm.worker.component.result;

import com.clougence.clouddm.sdk.execute.resultset.file.ResultType;

public interface EntityType {

    /** see: StorageCode  */
    byte Code       = ResultType.Code;      // (byte) 0x00;
    byte Boolean    = ResultType.Boolean;   // (byte) 0x01;
    byte Byte       = ResultType.Byte;      // (byte) 0x02;
    byte Short      = ResultType.Short;     // (byte) 0x03;
    byte Integer    = ResultType.Integer;   // (byte) 0x04;
    byte Long       = ResultType.Long;      // (byte) 0x05;
    byte BigInteger = ResultType.BigInteger;// (byte) 0x06;
    byte Float      = ResultType.Float;     // (byte) 0x07;
    byte Double     = ResultType.Double;    // (byte) 0x08;
    byte String     = ResultType.String;    // (byte) 0x09;
    byte Bytes      = ResultType.Bytes;     // (byte) 0x0A;
    byte Date       = ResultType.Date;      // (byte) 0x0B;
    byte Time       = ResultType.Time;      // (byte) 0x0C;
    byte TimeZ      = ResultType.TimeZ;     // (byte) 0x0D;
    byte DateTime   = ResultType.DateTime;  // (byte) 0x0E;
    byte DateTimeZ  = ResultType.DateTimeZ; // (byte) 0x0F;
    byte BigDecimal = ResultType.BigDecimal;// (byte) 0x10;

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
