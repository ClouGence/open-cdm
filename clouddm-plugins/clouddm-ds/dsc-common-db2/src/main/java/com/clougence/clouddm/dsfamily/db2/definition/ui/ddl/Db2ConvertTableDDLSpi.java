package com.clougence.clouddm.dsfamily.db2.definition.ui.ddl;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.clougence.clouddm.sdk.ui.ddl.ConvertTableDDLSpi;
import com.clougence.clouddm.sdk.ui.ddl.DDLType;
import com.clougence.clouddm.base.metadata.ds.DataSourceType;
import com.clougence.schema.editor.TableEditor;
import com.clougence.schema.editor.builder.actions.Action;
import com.clougence.schema.editor.provider.SqlBuilder;

/**
 * @Author: Ekko
 * @Date: 2024-01-22 10:10
 */
public class Db2ConvertTableDDLSpi implements ConvertTableDDLSpi {

    private static final List<DataSourceType> targetList  = new ArrayList<>();
    private static final List<DDLType>        ddlTypeList = new ArrayList<>();

    static {
        // target dsType
        targetList.add(DataSourceType.Db2);
        targetList.add(DataSourceType.MySQL);
        targetList.add(DataSourceType.Dameng);
        targetList.add(DataSourceType.StarRocks);
        targetList.add(DataSourceType.TiDB);

        // ddlType
        ddlTypeList.add(DDLType.Convert);
    }

    @Override
    public List<String> convertDDL(TableEditor sourceEditor, SqlBuilder targetSqlBuilder) {
        sourceEditor.getSource().setSchema(null);
        sourceEditor.getSource().setCatalog(null);
        List<Action> actions = sourceEditor.buildCreate(targetSqlBuilder, null);
        List<String> actionScripts = actions.stream().flatMap((Function<Action, Stream<String>>) action -> {
            return action.getSqlString().stream();
        }).collect(Collectors.toList());

        return actionScripts.isEmpty() ? new ArrayList<>() : actionScripts;
    }

    @Override
    public List<DataSourceType> convertDDLTargetList() {
        return targetList;
    }

    @Override
    public List<DDLType> ddlTypeList() {
        return ddlTypeList;
    }
}
