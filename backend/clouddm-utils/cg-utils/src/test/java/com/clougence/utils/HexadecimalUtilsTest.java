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
package com.clougence.utils;

import static com.clougence.utils.HexadecimalUtils.*;

import org.junit.jupiter.api.Test;

public class HexadecimalUtilsTest {

    @Test
    public void bit2bytes_01() {
        assert bytes2hex(bit2bytes("1")).equals("01");
        assert bytes2hex(bit2bytes("10")).equals("02");
        assert bytes2hex(bit2bytes("100")).equals("04");
        assert bytes2hex(bit2bytes("1000")).equals("08");
        assert bytes2hex(bit2bytes("10001")).equals("11");
        assert bytes2hex(bit2bytes("100010")).equals("22");
        assert bytes2hex(bit2bytes("1000100")).equals("44");
        assert bytes2hex(bit2bytes("10001000")).equals("88");
    }

    @Test
    public void bytes2bit_01() {
        assert bytes2bit(null) == null;
        assert bytes2bit(new byte[] {}).equals("");
        assert bytes2bit(new byte[] { 0x00000 }).equals("0");
        assert bytes2bit(new byte[] { 0x000 }).equals("0");
        assert bytes2bit(new byte[] { 0x0001 }).equals("1");
        assert bytes2bit(new byte[] { 0x001 }).equals("1");
        assert bytes2bit(new byte[] { 0x01 }).equals("1");
        assert bytes2bit(new byte[] { 0x02 }).equals("10");
        assert bytes2bit(new byte[] { 0x04 }).equals("100");
        assert bytes2bit(new byte[] { 0x08 }).equals("1000");
        assert bytes2bit(new byte[] { 0x11 }).equals("10001");
        assert bytes2bit(new byte[] { 0x22 }).equals("100010");
        assert bytes2bit(new byte[] { 0x44 }).equals("1000100");
        assert bytes2bit(new byte[] { (byte) 0x88 }).equals("10001000");
    }
    //
}
