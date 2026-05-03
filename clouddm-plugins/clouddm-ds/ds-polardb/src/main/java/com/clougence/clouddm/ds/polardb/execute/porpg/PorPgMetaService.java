package com.clougence.clouddm.ds.polardb.execute.porpg;

import java.sql.Connection;

import com.clougence.clouddm.ds.polardb.definition.porpg.editor.table.PorPgEditorProvider;
import com.clougence.clouddm.dsfamily.postgres.execute.PgMetaService;
import com.clougence.clouddm.sdk.execute.session.Session;
import com.clougence.clouddm.sdk.execute.session.rdb.DmRdbUmiService;
import com.clougence.schema.editor.provider.SqlBuilder;

import lombok.extern.slf4j.Slf4j;

/**
 * @author mode 2021/1/15 17:11
 */
@Slf4j
public class PorPgMetaService extends PgMetaService {

    public PorPgMetaService(Session rdbSession){
        super(rdbSession);
    }

    @Override
    protected DmRdbUmiService rdbUmiService(Connection con) {
        return new PorPgUmiServiceDm(con);
    }

    @Override
    protected SqlBuilder getSqlBuilder() { return PorPgEditorProvider.INSTANCE; }

}
