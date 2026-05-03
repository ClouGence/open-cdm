package com.clougence.clouddm.ds.db2zos.execute;

import java.sql.Connection;

import com.clougence.clouddm.dsfamily.db2.execute.Db2MetaService;
import com.clougence.clouddm.sdk.execute.session.Session;
import com.clougence.clouddm.sdk.execute.session.rdb.DmRdbUmiService;

import lombok.extern.slf4j.Slf4j;

/**
 * @author mode 2021/1/15 17:11
 */
@Slf4j
public class Db2ForZosMetaService extends Db2MetaService {

    public Db2ForZosMetaService(Session rdbSession){
        super(rdbSession);
    }

    @Override
    protected DmRdbUmiService rdbUmiService(Connection con) {
        return new Db2ForZosUmiServiceDM(con);
    }
}
