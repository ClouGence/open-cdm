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
package com.clougence.schema.umi.struts;

import java.util.HashSet;
import java.util.Set;

/**
 * Keys
 * @version : 2020-01-22
 * @author 赵永春 (zyc@hasor.net)
 */
public abstract class AbstractAttributeNames implements UmiAttributeNames {

    private final String space;
    private final String name;

    private AbstractAttributeNames(String name){
        this("", name);
    }

    protected AbstractAttributeNames(String space, String name){
        this.space = space;
        this.name = name;
    }

    public String getCodeKey() { return this.space + "_" + this.name; }

    private static final Set<String> checkNames = new HashSet<>();

    protected static synchronized <T extends UmiAttributeNames> T check(T attr) {
        if (checkNames.contains(attr.getCodeKey())) {
            //throw new IllegalStateException("attr " + attr.getCodeKey() + " duplicate.");
        }
        checkNames.add(attr.getCodeKey());
        return attr;
    }

}
