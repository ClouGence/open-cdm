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
package com.clougence.clouddm.faker.config.dsl.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Collection type
 * @author 赵永春 (zyc@hasor.net)
 * @version : 2023-02-14
 */
public class ListModel implements DataModel {

    private final List<DataModel> dataModel = new ArrayList<>();

    @Override
    public List<Object> recover(Map<String, Object> context) {
        List<Object> unwrap = new ArrayList<>(this.dataModel.size());
        for (DataModel model : this.dataModel) {
            unwrap.add(model.recover(context));
        }
        return unwrap;
    }

    /** Add an element to the end of the collection */
    public void add(DataModel object) {
        this.dataModel.add(object == null ? ValueModel.NULL : object);
    }

    /** Get an element */
    public DataModel get(int index) {
        return this.dataModel.get(index);
    }

    /** Pool Size */
    public int size() {
        return this.dataModel.size();
    }
}
