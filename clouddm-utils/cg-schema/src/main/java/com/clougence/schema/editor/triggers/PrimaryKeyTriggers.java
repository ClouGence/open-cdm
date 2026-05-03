package com.clougence.schema.editor.triggers;

import java.util.Collections;
import java.util.List;

import com.clougence.schema.editor.domain.EPrimaryKey;

/**
 * @author mode 2021/5/21 19:56
 */
public interface PrimaryKeyTriggers extends java.util.EventListener {

    default List<String> createPrimaryKey(TriggerContext buildContext, String catalog, String schema, String table, //
                                          EPrimaryKey primaryInfo) {
        return Collections.emptyList();
    }

    default List<String> dropPrimaryKey(TriggerContext buildContext, String catalog, String schema, String table, //
                                        EPrimaryKey primaryInfo) {
        return Collections.emptyList();
    }

    default List<String> primaryKeyAddColumn(TriggerContext buildContext, String catalog, String schema, String table, //
                                             EPrimaryKey primaryInfo, List<String> needAddColumns) {
        return Collections.emptyList();
    }

    default List<String> primaryKeyDropColumn(TriggerContext buildContext, String catalog, String schema, String table, //
                                              EPrimaryKey primaryInfo, List<String> needRemoveColumns) {
        return Collections.emptyList();
    }
}
