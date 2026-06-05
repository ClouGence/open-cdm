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

import org.junit.jupiter.api.Test;

public class IoTest {

    @Test
    public void headerTest() {
        assert EntityType.isCode(EntityType.Code);

        assert !EntityType.isType(EntityType.Code);
        assert EntityType.isType(EntityType.Boolean);
        assert EntityType.isType(EntityType.Byte);
        assert EntityType.isType(EntityType.Short);
        assert EntityType.isType(EntityType.Integer);
        assert EntityType.isType(EntityType.Long);
        assert EntityType.isType(EntityType.BigInteger);
        assert EntityType.isType(EntityType.Float);
        assert EntityType.isType(EntityType.Double);
        assert EntityType.isType(EntityType.String);
        assert EntityType.isType(EntityType.Bytes);
        assert EntityType.isType(EntityType.Date);
        assert EntityType.isType(EntityType.Time);
        assert EntityType.isType(EntityType.TimeZ);
        assert EntityType.isType(EntityType.DateTime);
        assert EntityType.isType(EntityType.DateTimeZ);
    }

    @Test
    public void lengthFieldBytesTest() {
        assert EntityType.lengthFieldBytes((byte) (EntityType.Boolean | 0x01)) == 1;
        assert EntityType.lengthFieldBytes((byte) (EntityType.Byte | 0x02)) == 2;
        assert EntityType.lengthFieldBytes((byte) (EntityType.Short | 0x03)) == 3;
        assert EntityType.lengthFieldBytes((byte) (EntityType.Integer | 0x04)) == 4;
        assert EntityType.lengthFieldBytes((byte) (EntityType.Long | 0x05)) == 5;
        assert EntityType.lengthFieldBytes((byte) (EntityType.BigInteger | 0x06)) == 6;
        assert EntityType.lengthFieldBytes((byte) (EntityType.Float | 0x07)) == 7;
        assert EntityType.lengthFieldBytes((byte) (EntityType.Double | 0x08)) == 8;
        assert EntityType.lengthFieldBytes((byte) (EntityType.String | 0x09)) == 9;
        assert EntityType.lengthFieldBytes((byte) (EntityType.Bytes | 0x0A)) == 10;
        assert EntityType.lengthFieldBytes((byte) (EntityType.Date | 0x0B)) == 11;
        assert EntityType.lengthFieldBytes((byte) (EntityType.Time | 0x0C)) == 12;
        assert EntityType.lengthFieldBytes((byte) (EntityType.TimeZ | 0x0D)) == 13;
        assert EntityType.lengthFieldBytes((byte) (EntityType.DateTime | 0x0E)) == 14;
        assert EntityType.lengthFieldBytes((byte) (EntityType.DateTimeZ | 0x0F)) == 15;
    }
}
