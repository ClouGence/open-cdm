package com.clougence.clouddm.ds.oceanbase.execute.obforora;

import java.sql.Connection;

import com.clougence.clouddm.ds.oceanbase.definition.obformysql.ui.editor.table.ObEditorProvider;
import com.clougence.clouddm.ds.oracle.execute.OraMetaService;
import com.clougence.clouddm.sdk.execute.session.Session;
import com.clougence.clouddm.sdk.execute.session.rdb.DmRdbUmiService;
import com.clougence.schema.editor.provider.SqlBuilder;

import lombok.extern.slf4j.Slf4j;

/**
 * @Author: Ekko
 * @Date: 2023-09-18 14:33
 */
@Slf4j
public class ObForOraMetaService extends OraMetaService {

    protected ObForOraMetaService(Session rdbSession){
        super(rdbSession);
    }

    @Override
    protected DmRdbUmiService rdbUmiService(Connection con) {
        return new ObForOracleUmiServiceDm(con);
    }

    @Override
    protected SqlBuilder getSqlBuilder() { return ObEditorProvider.INSTANCE; }

    @Override
    public String getCurrentCatalog() { return null; }
}
