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
