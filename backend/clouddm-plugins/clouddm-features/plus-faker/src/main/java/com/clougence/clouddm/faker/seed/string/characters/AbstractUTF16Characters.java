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
package com.clougence.clouddm.faker.seed.string.characters;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;

import com.clougence.clouddm.faker.seed.string.Characters;
import com.clougence.utils.HexadecimalUtils;

/**
 * code range  see https://codepoints.net/
 *   - https://codepoints.net/U+0041
 *   -  char 'A' utf16 code is '00 41' in range 0x00 -> 0xFF
 */
public abstract class AbstractUTF16Characters implements Characters {

    public static class Range {

        private final int start;
        private final int end;

        public Range(int start, int end){
            this.start = start;
            this.end = end;
        }

        public int getSize() { return end - start + 1; }

        public int getStart() { return start; }

        public int getEnd() { return end; }
    }

    protected abstract Range[] getRanges();

    @Override
    public int getSize() { return Arrays.stream(getRanges()).mapToInt(Range::getSize).sum(); }

    @Override
    public char getChar(int index) {
        while (true) {
            for (Range range : getRanges()) {
                int rangeSize = range.getSize();
                if (index < rangeSize) {
                    int charCode = range.getStart() + index;
                    byte[] charBytes = HexadecimalUtils.hex2bytes(Integer.toHexString(charCode));
                    if (charBytes.length == 1) {
                        charBytes = new byte[] { 0x00, charBytes[0] };
                    }
                    return new String(charBytes, StandardCharsets.UTF_16).charAt(0);
                } else {
                    index = index - rangeSize;
                }
            }
        }
    }
}
