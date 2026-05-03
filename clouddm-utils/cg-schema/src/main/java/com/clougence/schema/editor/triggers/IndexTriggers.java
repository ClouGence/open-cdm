package com.clougence.schema.editor.triggers;

import java.util.Collections;
import java.util.List;

import com.clougence.schema.editor.domain.EIndex;

/**
 * @author mode 2021/5/21 19:56
 */
public interface IndexTriggers extends java.util.EventListener {

    default List<String> createIndex(TriggerContext buildContext, String catalog, String schema, String table, //
                                     EIndex indexInfo) {
        return Collections.emptyList();
    }

    default List<String> dropIndex(TriggerContext buildContext, String catalog, String schema, String table, //
                                   EIndex indexInfo) {
        return Collections.emptyList();
    }

    default List<String> indexRename(TriggerContext buildContext, String catalog, String schema, String table, //
                                     EIndex indexInfo, String newIndexName) {
        return Collections.emptyList();
    }

    default List<String> indexAddColumn(TriggerContext buildContext, String catalog, String schema, String table, //
                                        EIndex indexInfo, List<String> needAddColumns) {
        return Collections.emptyList();
    }

    default List<String> indexDropColumn(TriggerContext buildContext, String catalog, String schema, String table, //
                                         EIndex indexInfo, List<String> needRemoveColumns) {
        return Collections.emptyList();
    }
}
