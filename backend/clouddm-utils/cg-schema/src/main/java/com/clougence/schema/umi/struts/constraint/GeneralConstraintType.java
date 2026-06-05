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
package com.clougence.schema.umi.struts.constraint;

import com.clougence.schema.umi.struts.UmiConstraintType;

/**
 * 约束类型
 * @version : 2021-03-30
 * @author 赵永春 (zyc@hasor.net)
 */
public enum GeneralConstraintType implements UmiConstraintType {

    /** 非空的 */
    NonNull("NonNull"),
    /** 唯一的 */
    Unique("Unique"),
    /** 主要的 */
    Primary("Primary"),
    Check("Check"),
    Foreign("Foreign"),;

    private final String typeName;

    GeneralConstraintType(String typeName){
        this.typeName = typeName;
    }

    @Override
    public String getCode() { return this.typeName; }

    public static GeneralConstraintType valueOfCode(String code) {
        for (GeneralConstraintType constraintType : GeneralConstraintType.values()) {
            if (constraintType.typeName.equalsIgnoreCase(code)) {
                return constraintType;
            }
        }
        return null;
    }
}
