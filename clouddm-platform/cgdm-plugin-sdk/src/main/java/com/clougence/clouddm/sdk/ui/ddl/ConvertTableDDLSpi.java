package com.clougence.clouddm.sdk.ui.ddl;

import java.util.List;

import com.clougence.clouddm.base.metadata.ds.DataSourceType;
import com.clougence.clouddm.sdk.Spi;
import com.clougence.schema.editor.TableEditor;
import com.clougence.schema.editor.provider.SqlBuilder;

/**
 * @Author: Ekko
 * @Date: 2024-01-22 11:22
 */
public interface ConvertTableDDLSpi extends Spi {

    List<String> convertDDL(TableEditor sourceEditor, SqlBuilder targetSqlBuilder);

    List<DataSourceType> convertDDLTargetList();

    List<DDLType> ddlTypeList();
}
