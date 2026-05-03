package com.clougence.schema.editor.triggers;

import java.util.Collections;
import java.util.List;

import com.clougence.schema.editor.domain.EColumn;
import com.clougence.schema.editor.domain.ETable;

/**
 * @author mode 2021/5/21 19:56
 */
public interface ColumnTriggers extends java.util.EventListener {

    default List<String> addColumn(TriggerContext buildContext, String catalog, String schema, String table, //
                                   EColumn columnInfo, ETable eTable) {
        return Collections.emptyList();
    }

    default List<String> dropColumn(TriggerContext buildContext, String catalog, String schema, String table, //
                                    EColumn columnInfo) {
        return Collections.emptyList();
    }

    default boolean supportColumnRename() {
        return true;
    }

    /**
     * colum rename
     * @param columnInfo old column
     * @param newColumnName new column name
     * @return
     */
    default List<String> columnRename(TriggerContext buildContext, String catalog, String schema, String table, //
                                      EColumn columnInfo, String newColumnName) {
        return Collections.emptyList();
    }

    default List<String> columnChange(TriggerContext buildContext, String catalog, String schema, String table, //
                                      EColumn columnInfo, EColumn newInfo, List<String> diffChange, ETable eTable) {
        return Collections.emptyList();
    }

    default List<String> columnComment(TriggerContext buildContext, String catalog, String schema, String table, //
                                       EColumn columnInfo, String comment, ETable eTable) {
        return Collections.emptyList();
    }
}
