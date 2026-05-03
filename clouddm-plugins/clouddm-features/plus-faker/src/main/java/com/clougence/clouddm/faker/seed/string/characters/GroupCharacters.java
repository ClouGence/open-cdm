/*
 * Copyright 2015-2022 the original author or authors.
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

import java.util.ArrayList;
import java.util.Arrays;

import com.clougence.clouddm.faker.seed.string.Characters;

/**
 * 基于 UTF-16 的 字符集组
 * @version : 2022-07-25
 * @author 赵永春 (zyc@hasor.net)
 */
public class GroupCharacters extends AbstractUTF16Characters {

    private final Range[] ranges;

    public GroupCharacters(Characters... characters){
        ArrayList<Range> list = new ArrayList<>();

        for (Characters character : characters) {
            if (character instanceof AbstractUTF16Characters) {
                list.addAll(Arrays.asList(((AbstractUTF16Characters) character).getRanges()));
            } else {
                throw new UnsupportedOperationException();
            }
        }

        this.ranges = list.toArray(list.toArray(new Range[0]));
    }

    public Range[] getRanges() { return this.ranges; }
}
