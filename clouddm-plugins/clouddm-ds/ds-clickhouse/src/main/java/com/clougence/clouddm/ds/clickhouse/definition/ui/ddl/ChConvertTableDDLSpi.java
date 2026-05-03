package com.clougence.clouddm.ds.clickhouse.definition.ui.ddl;

import java.util.ArrayList;
import java.util.List;

import com.clougence.clouddm.sdk.ui.ddl.ConvertTableDDLSpi;
import com.clougence.clouddm.sdk.ui.ddl.DDLType;
import com.clougence.clouddm.base.metadata.ds.DataSourceType;
import com.clougence.schema.editor.TableEditor;
import com.clougence.schema.editor.provider.SqlBuilder;

/**
 * @Author: Ekko
 * @Date: 2024-01-22 10:10
 */
public class ChConvertTableDDLSpi implements ConvertTableDDLSpi {

    private static final List<DataSourceType> targetList  = new ArrayList<>();
    private static final List<DDLType>        ddlTypeList = new ArrayList<>();

    static {
        targetList.add(DataSourceType.ClickHouse);

        // ddlType
        ddlTypeList.add(DDLType.Request);
    }

    @Override
    public List<String> convertDDL(TableEditor sourceEditor, SqlBuilder targetSqlBuilder) {
        throw new UnsupportedOperationException("Unsupported ClickHouse struct migration");
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
