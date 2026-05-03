package com.clougence.clouddm.ds.oceanbase.execute.obformysql;

import java.sql.Connection;

import com.clougence.clouddm.ds.oceanbase.definition.obformysql.ui.editor.table.ObEditorProvider;
import com.clougence.clouddm.dsfamily.mysql.execute.MyMetaService;
import com.clougence.clouddm.sdk.execute.session.Session;
import com.clougence.clouddm.sdk.execute.session.rdb.DmRdbUmiService;
import com.clougence.schema.editor.provider.SqlBuilder;

/**
 * @Author: Ekko
 * @Date: 2023-09-18 14:33
 */
public class ObMetaService extends MyMetaService {

    protected ObMetaService(Session rdbSession){
        super(rdbSession);
    }

    @Override
    protected DmRdbUmiService rdbUmiService(Connection con) {
        return new ObForMySQLUmiServiceDm(con);
    }

    @Override
    protected SqlBuilder getSqlBuilder() { return ObEditorProvider.INSTANCE; }
}
