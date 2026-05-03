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
package com.clougence.utils;

import java.util.*;
import java.util.function.Function;

import com.clougence.utils.ref.LinkedCaseInsensitiveMap;

/**
 *
 * @version : 2013-4-12
 * @author 赵永春 (zyc@hasor.net)
 */
public class CollectionUtils {

    // Empty utilities
    //--------------------------------------------------------------------------
    public static boolean isNotEmpty(Collection<?> tables) {
        return tables != null && !tables.isEmpty();
    }

    public static boolean isEmpty(Collection<?> tables) {
        return tables == null || tables.isEmpty();
    }

    public static boolean isNotEmpty(Map<?, ?> maps) {
        return maps != null && !maps.isEmpty();
    }

    public static boolean isEmpty(Map<?, ?> maps) {
        return maps == null || maps.isEmpty();
    }
    // split utilities
    //--------------------------------------------------------------------------

    /**
     * 切分list
     * @param sourceList
     * @param groupSize 每组定长
     */
    public static <T> List<List<T>> splitList(List<T> sourceList, int groupSize) {
        groupSize = Math.max(1, groupSize);
        int length = sourceList.size();
        // 计算可以分成多少组
        long num = (length + (long) groupSize - 1) / groupSize;
        List<List<T>> newList = null;

        if (groupSize > 8192) {
            newList = new LinkedList<>();
        } else {
            newList = new ArrayList<>((int) (groupSize * 0.25));
        }

        for (int i = 0; i < num; i++) {
            // 开始位置
            int fromIndex = i * groupSize;
            // 结束位置
            int toIndex = Math.min((i + 1) * groupSize, length);
            newList.add(sourceList.subList(fromIndex, toIndex));
        }
        return newList;
    }
    // Iterator/Enumeration utilities
    //--------------------------------------------------------------------------

    /** 迭代器类型转换 */
    public static <T, O> Iterator<O> convertIterator(final Iterator<T> oriIterator, final Function<T, O> converter) {
        return new Iterator<O>() {

            @Override
            public void remove() {
                oriIterator.remove();
            }

            @Override
            public O next() {
                return converter.apply(oriIterator.next());
            }

            @Override
            public boolean hasNext() {
                return oriIterator.hasNext();
            }
        };
    }

    /** 转换为 Enumeration */
    public static <T> Enumeration<T> asEnumeration(final Iterator<T> iterator) {
        return new Enumeration<T>() {

            @Override
            public boolean hasMoreElements() {
                return iterator.hasNext();
            }

            @Override
            public T nextElement() {
                return iterator.next();
            }
        };
    }

    public static <T> List<T> asList(Enumeration<T> enumeration) {
        if (enumeration == null) {
            return Collections.emptyList();
        }
        ArrayList<T> arrayList = new ArrayList<>();
        while (enumeration.hasMoreElements()) {
            arrayList.add(enumeration.nextElement());
        }
        return arrayList;
    }

    public static <T> List<T> asList(T... arrays) {
        if (arrays == null || arrays.length == 0) {
            return new ArrayList<>();
        }
        ArrayList<T> arrayList = new ArrayList<>();
        Collections.addAll(arrayList, arrays);
        return arrayList;
    }

    public static <T> List<T> asList(Iterator<T> iterator) {
        if (iterator == null) {
            return Collections.emptyList();
        }
        ArrayList<T> arrayList = new ArrayList<>();
        while (iterator.hasNext()) {
            arrayList.add(iterator.next());
        }
        return arrayList;
    }

    public static <K, V> Map<K, V> asMap(K key1, V val1) {
        return asMap(key1, val1, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null);
    }

    public static <K, V> Map<K, V> asMap(K key1, V val1, K key2, V val2) {
        return asMap(key1, val1, key2, val2, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null);
    }

    public static <K, V> Map<K, V> asMap(K key1, V val1, K key2, V val2, K key3, V val3) {
        return asMap(key1, val1, key2, val2, key3, val3, null, null, null, null, null, null, null, null, null, null, null, null, null, null);
    }

    public static <K, V> Map<K, V> asMap(K key1, V val1, K key2, V val2, K key3, V val3, K key4, V val4) {
        return asMap(key1, val1, key2, val2, key3, val3, key4, val4, null, null, null, null, null, null, null, null, null, null, null, null);
    }

    public static <K, V> Map<K, V> asMap(K key1, V val1, K key2, V val2, K key3, V val3, K key4, V val4, K key5, V val5) {
        return asMap(key1, val1, key2, val2, key3, val3, key4, val4, key5, val5, null, null, null, null, null, null, null, null, null, null);
    }

    public static <K, V> Map<K, V> asMap(K key1, V val1, K key2, V val2, K key3, V val3, K key4, V val4, K key5, V val5, K key6, V val6) {
        return asMap(key1, val1, key2, val2, key3, val3, key4, val4, key5, val5, key6, val6, null, null, null, null, null, null, null, null);
    }

    public static <K, V> Map<K, V> asMap(K key1, V val1, K key2, V val2, K key3, V val3, K key4, V val4, K key5, V val5, K key6, V val6, K key7, V val7) {
        return asMap(key1, val1, key2, val2, key3, val3, key4, val4, key5, val5, key6, val6, key7, val7, null, null, null, null, null, null);
    }

    public static <K, V> Map<K, V> asMap(K key1, V val1, K key2, V val2, K key3, V val3, K key4, V val4, K key5, V val5, K key6, V val6, K key7, V val7, K key8, V val8) {
        return asMap(key1, val1, key2, val2, key3, val3, key4, val4, key5, val5, key6, val6, key7, val7, key8, val8, null, null, null, null);
    }

    public static <K, V> Map<K, V> asMap(K key1, V val1, K key2, V val2, K key3, V val3, K key4, V val4, K key5, V val5, K key6, V val6, K key7, V val7, K key8, V val8, K key9,
                                         V val9) {
        return asMap(key1, val1, key2, val2, key3, val3, key4, val4, key5, val5, key6, val6, key7, val7, key8, val8, key9, val9, null, null);
    }

    public static <K, V> Map<K, V> asMap(K key1, V val1, K key2, V val2, K key3, V val3, K key4, V val4, K key5, V val5, K key6, V val6, K key7, V val7, K key8, V val8, K key9,
                                         V val9, K key0, V val0) {
        Map<K, V> map = new HashMap<>();
        if (key1 != null) {
            map.put(key1, val1);
        }
        if (key2 != null) {
            map.put(key2, val2);
        }
        if (key3 != null) {
            map.put(key3, val3);
        }
        if (key4 != null) {
            map.put(key4, val4);
        }
        if (key5 != null) {
            map.put(key5, val5);
        }
        if (key6 != null) {
            map.put(key6, val6);
        }
        if (key7 != null) {
            map.put(key7, val7);
        }
        if (key8 != null) {
            map.put(key8, val8);
        }
        if (key9 != null) {
            map.put(key9, val9);
        }
        if (key0 != null) {
            map.put(key0, val0);
        }
        return map;
    }

    /** 合并两个迭代器 */
    public static <T> Enumeration<T> mergeEnumeration(final Enumeration<T> enum1, final Enumeration<T> enum2) {
        final Enumeration<T> i1 = enum1 != null ? enum1 : CollectionUtils.asEnumeration(Collections.emptyIterator());
        final Enumeration<T> i2 = enum2 != null ? enum2 : CollectionUtils.asEnumeration(Collections.emptyIterator());
        return new Enumeration<T>() {

            private Enumeration<T> it = i1;

            @Override
            public boolean hasMoreElements() {
                return i1.hasMoreElements() || i2.hasMoreElements();
            }

            @Override
            public T nextElement() {
                if (!this.it.hasMoreElements()) {
                    this.it = i2;
                }
                return this.it.nextElement();
            }
        };
    }

    /** 合并两个迭代器 */
    public static <T> Iterator<T> mergeIterator(final Iterator<T> iterator1, final Iterator<T> iterator2) {
        final Iterator<T> i1 = iterator1 != null ? iterator1 : Collections.emptyIterator();
        final Iterator<T> i2 = iterator2 != null ? iterator2 : Collections.emptyIterator();
        return new Iterator<T>() {

            private Iterator<T> it = i1;

            @Override
            public boolean hasNext() {
                return i1.hasNext() || i2.hasNext();
            }

            @Override
            public T next() {
                if (!this.it.hasNext()) {
                    this.it = i2;
                }
                return this.it.next();
            }

            @Override
            public void remove() {
                this.it.remove();
            }
        };
    }

    /**
     * Returns <code>true</code> iff at least one element is in both collections.
     * <p>
     * In other words, this method returns <code>true</code> iff the
     * intersection of <i>coll1</i> and <i>coll2</i> is not empty.
     *
     * @param coll1  the first collection, must not be null
     * @param coll2  the first collection, must not be null
     * @return <code>true</code> iff the intersection of the collections is non-empty
     * @since 2.1
     */
    public static boolean containsAny(final Collection<?> coll1, final Collection<?> coll2) {
        if (coll1.size() < coll2.size()) {
            for (Object o : coll1) {
                if (coll2.contains(o)) {
                    return true;
                }
            }
        } else {
            for (Object o : coll2) {
                if (coll1.contains(o)) {
                    return true;
                }
            }
        }
        return false;
    }

    public static Map<String, ?> decorateCaseSensitive(Map<String, ?> listMap) {
        Map<String, Object> inSensitiveMap = new LinkedCaseInsensitiveMap<>();
        HashSet<String> set = new HashSet<>(listMap.size());
        for (Map.Entry<String, ?> entry : listMap.entrySet()) {
            set.add(entry.getKey().toUpperCase());
            inSensitiveMap.put(entry.getKey(), entry.getValue());
        }
        if (set.size() != listMap.size()) {
            return listMap;
        }
        return inSensitiveMap;
    }

    public static <T, G> Map<G, List<T>> groupBy(List<T> list, Function<T, G> groupBy) {
        return groupBy(list, groupBy, t -> t);
    }

    public static <T, G, V> Map<G, List<V>> groupBy(List<T> list, Function<T, G> groupBy, Function<T, V> convert) {
        if (list == null) {
            return Collections.emptyMap();
        }
        Map<G, List<V>> groupMap = new HashMap<>();
        for (T t : list) {
            groupMap.computeIfAbsent(groupBy.apply(t), k -> new ArrayList<>()).add(convert.apply(t));
        }
        return groupMap;
    }

}
