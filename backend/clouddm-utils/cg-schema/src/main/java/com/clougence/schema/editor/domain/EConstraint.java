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

import com.clougence.utils.StringUtils;
import lombok.Getter;
import lombok.Setter;

import java.util.*;

@Getter
@Setter
public class EConstraint extends EChange<EConstraint> {

    private String              name;
    private Map<String, String> attribute  = new HashMap<>();
    private EConstraintType     type;
    private String              expression;
    private List<String>        columnList = new ArrayList<>();

    //
    private Boolean             enabled;

    @Override
    public EConstraint clone() {
        EConstraint eConstraint = new EConstraint();
        eConstraint.name = this.name;
        eConstraint.columnList.addAll(this.columnList);

        eConstraint.type = this.type;
        eConstraint.enabled = this.enabled;
        eConstraint.expression = this.expression;

        eConstraint.attribute.putAll(this.attribute);
        return eConstraint;
    }

    public boolean testChanged(EConstraint initData) {
        if (initData == null) {
            return true;
        }
        if (!StringUtils.equals(this.name, initData.name)) {
            return true;
        }
        if (!Objects.deepEquals(this.columnList, initData.columnList)) {
            return true;
        }

        if (!Objects.equals(this.enabled, initData.enabled)) {
            return true;
        }
        if (!Objects.equals(this.expression, initData.expression)) {
            return true;
        }

        if (this.type != initData.type) {
            return true;
        }

        if (EditorUtils.testAttribute(this.attribute, initData.attribute)) {
            return true;
        }
        return false;
    }
}
