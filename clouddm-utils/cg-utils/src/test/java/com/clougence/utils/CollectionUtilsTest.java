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
package com.clougence.utils;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;

/***
 * @version : 2014-1-13
 * @author 赵永春 (zyc@hasor.net)
 */
public class CollectionUtilsTest {

    @Test
    public void splitList_01() {
        List<String> data = Arrays.asList("1", "2", "3", "4", "5", "6");
        assertEquals(CollectionUtils.splitList(data, Integer.MAX_VALUE).size(), 1);
        assertEquals(CollectionUtils.splitList(data, Integer.MIN_VALUE).size(), data.size());
        assertEquals(CollectionUtils.splitList(data, 1).size(), data.size());
        assertEquals(CollectionUtils.splitList(data, 2).size(), 3);
        assertEquals(CollectionUtils.splitList(data, 3).size(), 2);
        assertEquals(CollectionUtils.splitList(data, 4).size(), 2);
        assertEquals(CollectionUtils.splitList(data, 5).size(), 2);
        assertEquals(CollectionUtils.splitList(data, 6).size(), 1);
    }
}
