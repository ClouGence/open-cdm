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
package com.clougence.utils.loader;

import java.io.IOException;

/**
 * 资源加载器、资源扫描器
 * @version : 2021-09-29
 * @author 赵永春 (zyc@hasor.net)
 */
@FunctionalInterface
public interface ClassMatcher {

    boolean matcher(ClassMatcherContext context) throws IOException;

    /** 类信息结构 */
    class ClassInfo {

        public String   className  = null;
        public String   superName  = null;
        public String[] interFaces = new String[0];
        public String[] castType   = new String[0];
        public String[] annos      = new String[0];
    }

    abstract class ClassMatcherContext {

        private final ClassInfo classInfo;

        public ClassMatcherContext(ClassInfo classInfo){
            this.classInfo = classInfo;
        }

        public ClassInfo getClassInfo() { return this.classInfo; }

        public abstract ClassInfo loadClass(String className) throws IOException;
    }
}
