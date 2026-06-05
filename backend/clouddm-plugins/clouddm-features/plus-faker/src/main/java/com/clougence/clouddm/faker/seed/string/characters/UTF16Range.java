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

/**
 * 基于 UTF-16 的 Bit 字符集，只含有 0，1 两个字符
 * @version : 2022-07-25
 * @author 赵永春 (zyc@hasor.net)
 */
public class UTF16Range extends AbstractUTF16Characters {

    private final Range[] ranges;

    public UTF16Range(Range... ranges){
        this.ranges = ranges;
    }

    @Override
    protected Range[] getRanges() { return ranges; }

    public static UTF16Range ofRanges(Range... ranges) {
        return new UTF16Range(ranges);
    }
}
