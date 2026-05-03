package com.clougence.clouddm.ds.mariadb.execute;

import java.sql.Connection;

import com.clougence.clouddm.dsfamily.mysql.definition.ui.editor.table.MyEditorProvider;
import com.clougence.clouddm.dsfamily.mysql.execute.MyMetaService;
import com.clougence.clouddm.sdk.execute.session.Session;
import com.clougence.clouddm.sdk.execute.session.rdb.DmRdbUmiService;
import com.clougence.schema.editor.provider.SqlBuilder;

import lombok.extern.slf4j.Slf4j;

/**
 * @author mode 2021/1/15 17:11
 */
@Slf4j
public class MarMetaService extends MyMetaService {

    public MarMetaService(Session rdbSession){
        super(rdbSession);
    }

    @Override
    protected DmRdbUmiService rdbUmiService(Connection con) {
        return new MarUmiServiceDm(con);
    }

    @Override
    protected SqlBuilder getSqlBuilder() { return MyEditorProvider.INSTANCE; }
}
