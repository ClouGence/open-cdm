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
package com.clougence.clouddm.console.web.service.project.domain;

import com.clougence.clouddm.platform.dal.model.project.DmProjectChangeItemDO;
import com.clougence.clouddm.platform.dal.model.project.DmProjectDevopsItemDO;

import lombok.Getter;

@Getter
public class Item {

    private final String                name;
    private final int                   index;
    private final DmProjectDevopsItemDO devopsItem;
    private final DmProjectChangeItemDO changeItem;

    public Item(DmProjectDevopsItemDO i){
        this.name = i.getContentName();
        this.index = i.getContentIndex();
        this.devopsItem = i;
        this.changeItem = null;
    }

    public Item(DmProjectChangeItemDO i){
        this.name = i.getContentName();
        this.index = i.getContentIndex();
        this.devopsItem = null;
        this.changeItem = i;
    }
}
