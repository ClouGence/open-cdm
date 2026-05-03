package com.clougence.clouddm.ds.mongodb.execute;

import java.sql.Connection;

import com.clougence.clouddm.sdk.execute.session.Session;
import com.clougence.clouddm.sdk.execute.session.rdb.DefaultRdbMetaService;
import com.clougence.clouddm.sdk.execute.session.rdb.DmRdbUmiService;
import com.clougence.schema.editor.provider.SqlBuilder;

import lombok.extern.slf4j.Slf4j;

/**
 * @author mode 2021/1/15 17:11
 */
@Slf4j
public class MongoMetaService extends DefaultRdbMetaService {

    protected MongoMetaService(Session rdbSession){
        super(rdbSession);
    }

    @Override
    protected DmRdbUmiService rdbUmiService(Connection con) {
        return new MongoUmiServiceDm(con);
    }

    @Override
    protected SqlBuilder getSqlBuilder() { return null; }

    @Override
    public String getCurrentCatalog() { return ""; }

    @Override
    public String getCurrentSchema() { return ""; }

    @Override
    public void testConnect() {

    }

}
