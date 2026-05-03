package com.clougence.clouddm.ds.maxcompute.definition.ui.editor.table;

import com.clougence.clouddm.ds.maxcompute.dialect.McDialect;
import com.clougence.schema.DsType;
import com.clougence.schema.dialect.Dialect;
import com.clougence.clouddm.dsfamily.schema.sqlbuilder.AbstractSqlBuilder;
import com.clougence.schema.editor.provider.SqlBuilder;

import lombok.extern.slf4j.Slf4j;

/**
 * @author  mode
 * @date 2025-9-22 15:57
 */
@Slf4j
public class McEditorProvider extends AbstractSqlBuilder implements SqlBuilder {

    public static final SqlBuilder INSTANCE = new McEditorProvider();

    public McEditorProvider(){
    }

    @Override
    public DsType getDataSourceType() { return DsType.MaxCompute; }

    @Override
    public Dialect getDialect() { return McDialect.INSTANCE; }
}
