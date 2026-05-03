package com.clougence.schema.editor.triggers;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import com.clougence.schema.editor.domain.ETable;

/**
 * @author mode 2021/5/21 19:56
 */
public interface TableTriggers extends java.util.EventListener {

    default boolean supportTableRename() {
        return true;
    }

    default List<String> tableRename(TriggerContext buildContext, String catalog, String schema, String table, String newName) {
        return Collections.emptyList();
    }

    default List<String> tableComment(TriggerContext buildContext, String catalog, String schema, String table, String comment) {
        return Collections.emptyList();
    }

    default List<String> tableAlterBeFore(TriggerContext buildContext, String catalog, String schema, String table, ETable eTable) {
        return Collections.emptyList();
    }

    default List<String> tableAlter(TriggerContext buildContext, String catalog, String schema, String table, ETable eTable, Map<String, String> sourceAttr) {
        return Collections.emptyList();
    }

    default List<String> tableCreate(TriggerContext buildContext, String catalog, String schema, String table, ETable eTable) {
        return Collections.emptyList();
    }

    default List<String> tableDrop(TriggerContext buildContext, String catalog, String schema, String table, ETable eTable) {
        return Collections.emptyList();
    }

    default List<String> tableTruncate(TriggerContext buildContext, String catalog, String schema, String table, ETable eTable) {
        return Collections.emptyList();
    }
}
