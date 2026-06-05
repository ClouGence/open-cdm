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
package com.clougence.utils.json;

import java.net.URL;
/* ------------------------------------------------------------ */

/** ClassLoader Helper.
 * This helper class allows classes to be loaded either from the
 * Thread's ContextClassLoader, the classloader of the derived class
 * or the system ClassLoader.
 *
 * <B>Usage:</B><PRE>
 * public class MyClass {
 * void myMethod() {
 * ...
 * Class c=Loader.loadClass(this.getClass(),classname);
 * ...
 * }
 * </PRE> */
@Deprecated // use com.clougence.utils.JsonUtils
class Loader {

    /* ------------------------------------------------------------ */
    public static URL getResource(Class<?> loadClass, String name, boolean checkParents) throws ClassNotFoundException {
        URL url = null;
        ClassLoader loader = Thread.currentThread().getContextClassLoader();
        while (url == null && loader != null) {
            url = loader.getResource(name);
            loader = (url == null && checkParents) ? loader.getParent() : null;
        }
        loader = loadClass == null ? null : loadClass.getClassLoader();
        while (url == null && loader != null) {
            url = loader.getResource(name);
            loader = (url == null && checkParents) ? loader.getParent() : null;
        }
        if (url == null) {
            url = ClassLoader.getSystemResource(name);
        }
        return url;
    }

    /* ------------------------------------------------------------ */
    @SuppressWarnings("rawtypes")
    public static Class loadClass(Class loadClass, String name) throws ClassNotFoundException {
        return loadClass(loadClass, name, false);
    }
    /* ------------------------------------------------------------ */

    /** Load a class.
     *
     * @param loadClass
     * @param name
     * @param checkParents If true, try loading directly from parent classloaders.
     * @return Class
     * @throws ClassNotFoundException
     */
    @SuppressWarnings("rawtypes")
    public static Class loadClass(Class loadClass, String name, boolean checkParents) throws ClassNotFoundException {
        ClassNotFoundException ex = null;
        Class<?> c = null;
        ClassLoader loader = (loadClass == null) ? Thread.currentThread().getContextClassLoader() : loadClass.getClassLoader();
        while (c == null && loader != null) {
            try {
                c = loader.loadClass(name);
            } catch (ClassNotFoundException e) {
                if (ex == null) {
                    ex = e;
                }
            }
            loader = (c == null && checkParents) ? loader.getParent() : null;
        }
        if (c != null) {
            return c;
        }
        throw ex;
    }
}
