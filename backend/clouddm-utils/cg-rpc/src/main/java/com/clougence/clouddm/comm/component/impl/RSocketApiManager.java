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
package com.clougence.clouddm.comm.component.impl;

import java.util.*;

import com.clougence.clouddm.comm.RSocketApiClass;
import com.clougence.clouddm.comm.model.RSocketApiWrapper;
import com.clougence.utils.function.EFunction;
import com.clougence.utils.loader.CgResourceScanner;
import com.clougence.utils.loader.providers.ClassPathResourceLoader;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

/**
 * @author noear 2020/11/17
 */
@Slf4j
public class RSocketApiManager {

    /** Key is methodName, value is api wrapper for invoke method */
    private static Map<String, RSocketApiWrapper> workerApiMap = new HashMap<>();
    /** Key is method name, value is related instance object */
    private static Map<String, Object>            methodObjMap = new HashMap<>();

    private static List<ScanMethod> scanAllApiMethod(ClassLoader appClassLoader, String packageScope) throws ClassNotFoundException {
        CgResourceScanner scanner = new CgResourceScanner(new ClassPathResourceLoader(appClassLoader, ""));
        Set<String> apiClass = scanner.getClassNamesSet(new String[] { packageScope }, context -> {
            if (context.getClassInfo() != null) {
                for (String anno : context.getClassInfo().annos) {
                    if (anno.equals(RSocketApiClass.class.getName())) {
                        return true;
                    }
                }
            }
            return false;
        });

        Map<String, ScanMethod> apiMethods = new LinkedHashMap<>();
        for (String className : apiClass) {
            if (!className.startsWith(packageScope)) {
                continue;
            }

            Class<?> clz = appClassLoader.loadClass(className);
            if (!clz.isInterface() && clz.getAnnotation(RSocketApiClass.class) != null) {
                Class<?>[] apiList = clz.getInterfaces();
                for (Class<?> api : apiList) {
                    Arrays.stream(clz.getMethods()).filter(method -> method.getDeclaringClass() != Object.class).forEach(method -> {
                        ScanMethod scanMethod = new ScanMethod();
                        scanMethod.setScanMethodKey(api.getName() + "." + method.getName());
                        scanMethod.setScanMethod(method);
                        scanMethod.setScanTarget(clz);
                        apiMethods.put(scanMethod.getScanMethodKey(), scanMethod);
                    });
                }
            }
        }
        if (apiMethods.isEmpty()) {
            log.warn("Can't scan out any rsocket api from local classloader,please check that your api is annotated with RSocketApi.class");
        }
        return new ArrayList<>(apiMethods.values());
    }

    /** This method will scan all api method that annotated by RSocketApi and register them in to map for invoke */
    @SneakyThrows
    public static void scanAllApiAndRegister(ClassLoader appClassLoader, String packageScope, EFunction<Class<?>, Object, Throwable> beanFactory) {
        for (ScanMethod method : scanAllApiMethod(appClassLoader, packageScope)) {
            String methodName = method.getScanMethodKey();
            if (methodObjMap.get(methodName) == null) {
                Class<?> scanTarget = method.getScanTarget();
                methodObjMap.put(methodName, beanFactory.eApply(scanTarget));
            }

            RSocketApiWrapper apiWrapper = new RSocketApiWrapper();
            apiWrapper.setApiMethodName(method.getScanMethodKey());
            apiWrapper.setApiMethod(method.getScanMethod());
            apiWrapper.setApiType(method.getScanTarget());
            apiWrapper.setApiObj(methodObjMap.get(methodName));

            if (workerApiMap.get(methodName) != null) {
                throw new RuntimeException("Register duplicated name method. Method name is " + methodName + ",please check. Api method name should be globally unique");
            }
            workerApiMap.put(methodName, apiWrapper);
            log.info("register method:" + methodName);
        }
    }

    public static RSocketApiWrapper getWorkerDsApi(String methodName) {
        return workerApiMap.get(methodName);
    }
}
