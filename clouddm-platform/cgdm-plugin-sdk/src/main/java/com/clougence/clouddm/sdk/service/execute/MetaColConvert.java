package com.clougence.clouddm.sdk.service.execute;

import java.util.List;
import java.util.stream.Collectors;

import com.clougence.clouddm.sdk.analysis.column.RealColumn;
import com.clougence.clouddm.sdk.analysis.column.SelectItem;

public final class MetaColConvert {

    private MetaColConvert() {
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