package com.clougence.clouddm.ds.tidb.execute;

import java.sql.Connection;

import com.clougence.clouddm.ds.tidb.definition.ui.editor.table.TiEditorProvider;
import com.clougence.clouddm.dsfamily.mysql.execute.MyMetaService;
import com.clougence.clouddm.sdk.execute.session.Session;
import com.clougence.clouddm.sdk.execute.session.rdb.DmRdbUmiService;
import com.clougence.schema.editor.provider.SqlBuilder;

import lombok.extern.slf4j.Slf4j;

/**
 * @author mode 2021/1/15 17:11
 */
@Slf4j
public class TiMetaService extends MyMetaService {

    public TiMetaService(Session rdbSession){
        super(rdbSession);
    }

    @Override
    protected DmRdbUmiService rdbUmiService(Connection con) {
        return new TiUmiServiceDm(con);
    }

    @Override
    protected SqlBuilder getSqlBuilder() { return TiEditorProvider.INSTANCE; }
}
