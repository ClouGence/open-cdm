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
package com.clougence.clouddm.faker.config;

import com.clougence.utils.StringUtils;

/**
 * 要执行的操作类型
 * @version : 2022-07-25
 * @author 赵永春 (zyc@hasor.net)
 */
public enum OpsType {

    Insert,
    Update,
    Delete;

    public String sortCode() {
        return String.valueOf(name().charAt(0));
    }

    public static OpsType valueOfCode(String name) {
        for (OpsType politic : OpsType.values()) {
            if (StringUtils.equalsIgnoreCase(politic.name(), name)) {
                return politic;
            }

            char charA = politic.name().charAt(0);
            char charB = name.charAt(0);
            if (charA == charB || charA == (charB - 32)) {
                return politic;
            }
        }
        return null;
    }
}
