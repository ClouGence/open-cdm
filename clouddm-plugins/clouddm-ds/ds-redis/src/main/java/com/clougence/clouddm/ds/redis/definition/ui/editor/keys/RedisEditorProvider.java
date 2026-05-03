package com.clougence.clouddm.ds.redis.definition.ui.editor.keys;

import com.clougence.clouddm.ds.redis.dialect.RedisDialect;
import com.clougence.schema.DsType;
import com.clougence.schema.dialect.Dialect;
import com.clougence.clouddm.dsfamily.schema.sqlbuilder.AbstractSqlBuilder;
import com.clougence.schema.editor.provider.SqlBuilder;

import lombok.extern.slf4j.Slf4j;

/**
 * @author Ekko
 * @date 2023/8/18 10:07
*/
@Slf4j
public class RedisEditorProvider extends AbstractSqlBuilder implements SqlBuilder {

    public static final SqlBuilder INSTANCE = new RedisEditorProvider();

    @Override
    public DsType getDataSourceType() { return DsType.Redis; }

    public Dialect getDialect() { return RedisDialect.INSTANCE; }

}
