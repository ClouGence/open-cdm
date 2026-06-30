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
package com.clougence.drivers.adapter;

import java.util.*;

public class AdapterManager {

    private static final Map<String, String[]>       propertyGroupBy = new HashMap<>();
    private static final Map<String, AdapterFactory> factoryMap      = new HashMap<>();

    public static void register(ClassLoader cl) {
        if (cl == null) {
            return;
        }
        ServiceLoader<AdapterFactory> loader = ServiceLoader.load(AdapterFactory.class, cl);
        for (AdapterFactory factory : loader) {
            register(factory.getAdapterName(), factory);
        }
    }

    public static void register(String adapter, AdapterFactory factory) {
        if (adapter == null || adapter.trim().isEmpty() || factory == null) {
            return;
        }

        synchronized (factoryMap) {
            factoryMap.put(adapter, factory);
            Set<String> propertyNameSet = new HashSet<>(Arrays.asList(factory.getPropertyNames()));
            propertyNameSet.add(JdbcDriver.P_SERVER);
            propertyGroupBy.put(adapter, propertyNameSet.toArray(new String[0]));
        }
    }

    public static AdapterFactory lookup(String adapter) {
        AdapterFactory factory = registeredFactory(adapter);
        if (factory != null) {
            return factory;
        }

        throw new UnsupportedOperationException("not found " + adapter + " driver adapter.");
    }

    private static AdapterFactory registeredFactory(String adapter) {
        if (factoryMap.containsKey(adapter)) {
            return factoryMap.get(adapter);
        }
        return null;
    }

    public static String[] propertyNames(String adapter, Properties parse) {
        String[] names;
        if (!propertyGroupBy.containsKey(adapter)) {
            synchronized (propertyGroupBy) {
                if (!propertyGroupBy.containsKey(adapter)) {
                    AdapterFactory factory = lookup(adapter);
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
