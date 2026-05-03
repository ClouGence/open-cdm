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
package com.clougence.drivers.adapter;

import java.util.*;

public class AdapterManager {

    private static final Map<String, String[]>       propertyGroupBy = new HashMap<>();
    private static final Map<String, AdapterFactory> factoryMap      = new HashMap<>();

    public static void register(String adapter, AdapterFactory factory) {
        factoryMap.put(adapter, factory);
        Set<String> propertyNameSet = new HashSet<>(Arrays.asList(factory.getPropertyNames()));
        propertyNameSet.add(JdbcDriver.P_SERVER);
        propertyGroupBy.put(adapter, propertyNameSet.toArray(new String[0]));
    }

    public static AdapterFactory lookup(String adapter, ClassLoader cl) {
        if (factoryMap.containsKey(adapter)) {
            return factoryMap.get(adapter);
        } else {
            synchronized (factoryMap) {
                if (factoryMap.containsKey(adapter)) {
                    return factoryMap.get(adapter);
                }
                ServiceLoader<AdapterFactory> loader = ServiceLoader.load(AdapterFactory.class, cl);
                for (AdapterFactory factory : loader) {
                    String adapterName = factory.getAdapterName();
                    if (!factoryMap.containsKey(adapterName)) {
                        factoryMap.put(adapterName, factory);
                    }
                }
            }
        }

        if (factoryMap.containsKey(adapter)) {
            return factoryMap.get(adapter);
        } else {
            throw new UnsupportedOperationException("not found " + adapter + " driver adapter.");
        }
    }

    static String[] propertyNames(String adapter, Properties parse, ClassLoader cl) {
        String[] names;
        if (!propertyGroupBy.containsKey(adapter)) {
            synchronized (propertyGroupBy) {
                if (!propertyGroupBy.containsKey(adapter)) {
                    AdapterFactory factory = lookup(adapter, cl);
                    Set<String> propertyNameSet = new HashSet<>(Arrays.asList(factory.getPropertyNames()));
                    propertyNameSet.add(JdbcDriver.P_SERVER);
                    names = propertyNameSet.toArray(new String[0]);
                    propertyGroupBy.put(adapter, names);
                } else {
                    names = propertyGroupBy.get(adapter);
                }
            }
        } else {
            names = propertyGroupBy.get(adapter);
        }

        return Arrays.stream(names).filter(parse::containsKey).toArray(String[]::new);
    }
}
