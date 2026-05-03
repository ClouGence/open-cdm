package com.clougence.clouddm.ds.polardb.execute.pormy;

import java.sql.Connection;

import com.clougence.clouddm.ds.polardb.definition.pormy.editor.table.PorMyEditorProvider;
import com.clougence.clouddm.dsfamily.mysql.execute.MyMetaService;
import com.clougence.clouddm.sdk.execute.session.Session;
import com.clougence.clouddm.sdk.execute.session.rdb.DmRdbUmiService;
import com.clougence.schema.editor.provider.SqlBuilder;

import lombok.extern.slf4j.Slf4j;

/**
 * @author mode 2021/1/15 17:11
 */
@Slf4j
public class PorMyMetaService extends MyMetaService {

    public PorMyMetaService(Session rdbSession){
        super(rdbSession);
    }

    @Override
    protected DmRdbUmiService rdbUmiService(Connection con) {
        return new PorMyUmiServiceDm(con);
    }

    @Override
    protected SqlBuilder getSqlBuilder() { return PorMyEditorProvider.INSTANCE; }

}
