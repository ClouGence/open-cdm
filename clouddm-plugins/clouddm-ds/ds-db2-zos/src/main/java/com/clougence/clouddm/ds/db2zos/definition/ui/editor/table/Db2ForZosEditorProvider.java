package com.clougence.clouddm.ds.db2zos.definition.ui.editor.table;

import java.util.Collections;
import java.util.List;

import com.clougence.clouddm.dsfamily.db2.definition.ui.editor.table.Db2EditorProvider;
import com.clougence.schema.editor.provider.SqlBuilder;
import com.clougence.schema.editor.triggers.TriggerContext;

/**
 * @Author: mode
 * @Date: 2023-10-07 14:46
 */
public class Db2ForZosEditorProvider extends Db2EditorProvider {

    public static final SqlBuilder INSTANCE = new Db2ForZosEditorProvider();

    @Override
    public List<String> tableRename(TriggerContext buildContext, String catalog, String schema, String table, String newName) {
        StringBuilder sqlBuild = new StringBuilder();
        sqlBuild.append("rename table ");
        sqlBuild.append(getDialect().fmtTableName(true, null, schema, table));
        sqlBuild.append(" to ");
        sqlBuild.append(getDialect().fmtName(true, newName));
        sqlBuild.append(";");
        return Collections.singletonList(sqlBuild.toString());
    }
}
