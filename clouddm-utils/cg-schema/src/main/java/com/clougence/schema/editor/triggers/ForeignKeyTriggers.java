package com.clougence.schema.editor.triggers;

import java.util.Collections;
import java.util.List;

import com.clougence.schema.editor.domain.EForeignKey;

/**
 * @author mode 2021/5/21 19:56
 */
public interface ForeignKeyTriggers extends java.util.EventListener {

    default List<String> createForeignKey(TriggerContext buildContext, String catalog, String schema, String table, //
                                          EForeignKey foreignKeyInfo) {
        return Collections.emptyList();
    }

    default List<String> dropForeignKey(TriggerContext buildContext, String catalog, String schema, String table, //
                                        EForeignKey foreignKeyInfo) {
        return Collections.emptyList();
    }

    default List<String> foreignKeyRename(TriggerContext buildContext, String catalog, String schema, String table, //
                                          EForeignKey foreignKeyInfo, String newForeignKeyName) {
        return Collections.emptyList();
    }
}
