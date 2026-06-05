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
package com.clougence.schema.editor.domain;

import java.util.*;

import lombok.Getter;
import lombok.Setter;

/**
 * @author mode 2021/5/21 19:56
 */
@Getter
@Setter
public class EPrimaryKey extends EChange<EPrimaryKey> {

    private String              primaryKeyName;
    private List<String>        columnList = new ArrayList<>();
    private Map<String, String> attribute  = new HashMap<>();

    @Override
    public EPrimaryKey clone() {
        EPrimaryKey ePrimaryKey = new EPrimaryKey();

        ePrimaryKey.primaryKeyName = primaryKeyName;
        ePrimaryKey.columnList.addAll(this.columnList);
        ePrimaryKey.attribute.putAll(this.attribute);
        return ePrimaryKey;
    }

    @Override
    public boolean testChanged(EPrimaryKey initData) {
        if (initData == null) {
            return true;
        }
        if (!Objects.equals(this.primaryKeyName, initData.primaryKeyName)) {
            return true;
        }
        if (!Objects.deepEquals(this.columnList, initData.columnList)) {
            return true;
        }
        if (EditorUtils.testAttribute(this.attribute, initData.attribute)) {
            return true;
        }
        return false;
    }
}
