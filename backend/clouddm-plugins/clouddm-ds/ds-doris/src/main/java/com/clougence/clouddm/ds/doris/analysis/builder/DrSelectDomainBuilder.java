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
package com.clougence.clouddm.ds.doris.analysis.builder;

import java.util.Stack;

import com.clougence.clouddm.ds.doris.analysis.enums.DrAttribute;
import com.clougence.clouddm.ds.doris.analysis.secrules.DrSelectDomain;
import com.clougence.clouddm.dsfamily.analysis.secrules.builder.SelectDomainBuilder;
import com.clougence.clouddm.dsfamily.analysis.secrules.builder.enums.Attribute;
import com.clougence.clouddm.dsfamily.analysis.secrules.builder.enums.CommonAttribute;

public class DrSelectDomainBuilder extends SelectDomainBuilder<DrSelectDomain> {

    public DrSelectDomainBuilder(Stack selectStack){
        super(selectStack);
    }

    @Override
    protected DrSelectDomain getSelectDomain() { return new DrSelectDomain(); }

    @Override
    public void addAttr(Attribute attr, Object value) {
        if (attr == CommonAttribute.LIMIT) {
            selectDomain.setHasLimit(true);
        } else if (attr == DrAttribute.VALUES_SELECT) {
            selectDomain.setValuesSelect(true);
        } else {
            super.addAttr(attr, value);
        }
    }
}
