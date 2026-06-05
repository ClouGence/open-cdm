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
package com.clougence.clouddm.sdk.service.execute;

import java.util.List;
import java.util.stream.Collectors;

import com.clougence.clouddm.sdk.analysis.column.RealColumn;
import com.clougence.clouddm.sdk.analysis.column.SelectItem;

public final class MetaColConvert {

    private MetaColConvert(){
    }

    public static List<SelectItem> toSelectItems(List<MetaCol> metaCols, int tableId) {
        return metaCols.stream().map(metaCol -> toSelectItem(metaCol, tableId)).collect(Collectors.toList());
    }

    public static SelectItem toSelectItem(MetaCol metaCol, int tableId) {
        SelectItem selectItem = new SelectItem();

        RealColumn realColumn = new RealColumn();
        realColumn.setCatalog(metaCol.getCatalog());
        realColumn.setSchema(metaCol.getSchema());
        realColumn.setTable(metaCol.getTable());
        realColumn.setColumn(metaCol.getColumn());
        realColumn.setTableId(tableId);

        selectItem.addRealColumn(realColumn);
        selectItem.setItemAlias(metaCol.getColumn());
        return selectItem;
    }
}
