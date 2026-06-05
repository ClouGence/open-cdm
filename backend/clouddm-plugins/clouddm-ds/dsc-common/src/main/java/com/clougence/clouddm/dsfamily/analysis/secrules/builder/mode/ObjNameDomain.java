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
package com.clougence.clouddm.dsfamily.analysis.secrules.builder.mode;

import java.util.Collections;
import java.util.List;

import com.clougence.clouddm.dsfamily.analysis.secrules.builder.enums.NameType;
import com.clougence.clouddm.sdk.service.secrules.ModeDomain;

import lombok.Getter;

@Getter
public class ObjNameDomain implements ModeDomain {

    private List<String> nameList;

    private NameType     type;

    public ObjNameDomain(List<String> nameList, NameType type){
        this.nameList = nameList;
        this.type = type;
    }

    public ObjNameDomain(String text, NameType type){
        this.nameList = Collections.singletonList(text);
        this.type = type;
    }

    public ObjNameDomain(String text){
        this.nameList = Collections.singletonList(text);
    }

    public String getName() {
        if (nameList.size() != 1) {
            throw new RuntimeException("nameList size is " + nameList.size());
        }
        return nameList.get(0);
    }

    public String getLastName() { return nameList.get(nameList.size() - 1); }

}
