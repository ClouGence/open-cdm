package com.clougence.clouddm.ds.ads.definition.ui.ddl;

import java.util.ArrayList;
import java.util.List;

import com.clougence.clouddm.sdk.ui.ddl.ConvertTableDDLSpi;
import com.clougence.clouddm.sdk.ui.ddl.DDLType;
import com.clougence.clouddm.base.metadata.ds.DataSourceType;
import com.clougence.schema.editor.TableEditor;
import com.clougence.schema.editor.provider.SqlBuilder;

/**
 * @Author: mode
 * @Date: 2024-01-22 10:10
 */
public class AdsMyConvertTableDDLSpi implements ConvertTableDDLSpi {

    private static final List<DataSourceType> targetList  = new ArrayList<>();
    private static final List<DDLType>        ddlTypeList = new ArrayList<>();

    static {
        // target dsType
        targetList.add(DataSourceType.AdbForMySQL);

        // ddlType
        ddlTypeList.add(DDLType.Request);
    }

    @Override
    public List<String> convertDDL(TableEditor sourceEditor, SqlBuilder targetSqlBuilder) {
        throw new UnsupportedOperationException();
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
