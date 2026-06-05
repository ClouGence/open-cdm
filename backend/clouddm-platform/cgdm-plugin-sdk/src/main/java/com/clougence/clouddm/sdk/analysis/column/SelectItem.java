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
package com.clougence.clouddm.sdk.analysis.column;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

/**
 * The final return fields
 * such as
 * select id from a union select id from b
 * SelectColumn : {
 *     columnAlias: id,
 *     columns[ a.id, b.id]
 * }
 */
@Getter
@Setter
public class SelectItem {

    private List<RealColumn> columns = new ArrayList<>();

    // for find true column
    private String           itemAlias;
    private String           tableAlias;

    public void addRealColumn(RealColumn column) {
        this.columns.add(column);
    }

    public void addAllRealColumns(List<RealColumn> columns) {
        this.columns.addAll(columns);
    }
}
