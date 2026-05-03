package com.clougence.clouddm.dsfamily.mysql.definition.ui.ddl;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.clougence.adapter.mysql.MyUmiAttributeNames;
import com.clougence.clouddm.sdk.ui.ddl.ConvertTableDDLSpi;
import com.clougence.clouddm.sdk.ui.ddl.DDLType;
import com.clougence.clouddm.base.metadata.ds.DataSourceType;
import com.clougence.reactor.handlers.attributes.HandlersAttributeNames;
import com.clougence.schema.DsType;
import com.clougence.schema.editor.TableEditor;
import com.clougence.schema.editor.builder.actions.Action;
import com.clougence.schema.editor.domain.ETable;
import com.clougence.schema.editor.provider.SqlBuilder;

/**
 * @Author: Ekko
 * @Date: 2024-01-22 10:10
 */
public class MyConvertTableDDLSpi implements ConvertTableDDLSpi {

    private static final List<DataSourceType> targetList     = new ArrayList<>();
    private static final List<DDLType>        ddlTypeList    = new ArrayList<>();
    private static final Set<DsType>          needSchemaList = new HashSet<>();

    static {
        // target ds
        targetList.add(DataSourceType.MySQL);
        targetList.add(DataSourceType.ClickHouse);
        targetList.add(DataSourceType.Dameng);
        targetList.add(DataSourceType.Db2);
        targetList.add(DataSourceType.Doris);
        targetList.add(DataSourceType.OceanBase);
        targetList.add(DataSourceType.Oracle);
        targetList.add(DataSourceType.PolarDbX);
        targetList.add(DataSourceType.PolarDbMySQL);
        targetList.add(DataSourceType.PolarDBPg);
        targetList.add(DataSourceType.PostgreSQL);
        targetList.add(DataSourceType.SQLServer);
        targetList.add(DataSourceType.StarRocks);
        targetList.add(DataSourceType.TiDB);
        targetList.add(DataSourceType.Hana);
        targetList.add(DataSourceType.Greenplum);

        // ddlType
        ddlTypeList.add(DDLType.Request);
        ddlTypeList.add(DDLType.Convert);

        // needSchemaList
        needSchemaList.add(DsType.Db2);
    }

    @Override
    public List<String> convertDDL(TableEditor sourceEditor, SqlBuilder targetSqlBuilder) {
        DsType dsType = targetSqlBuilder.getDataSourceType();
        ETable sETable = sourceEditor.getSource();
        Map<String, String> attribute = sETable.getAttribute();

        // clear engine
        attribute.put(MyUmiAttributeNames.ENGINE.getCodeKey(), null);

        if (!needSchemaList.contains(dsType)) {
            sETable.setCatalog(null);
            sETable.setSchema(null);
        }

        sourceEditor.addAttrToContext(HandlersAttributeNames.DR_KEEP_INDEX.getCodeKey(), Boolean.TRUE.toString());
        sourceEditor.addAttrToContext(HandlersAttributeNames.SR_KEEP_INDEX.getCodeKey(), Boolean.TRUE.toString());

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
