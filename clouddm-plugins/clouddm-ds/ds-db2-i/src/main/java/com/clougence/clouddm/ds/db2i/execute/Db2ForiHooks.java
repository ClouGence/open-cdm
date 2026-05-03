package com.clougence.clouddm.ds.db2i.execute;

import com.clougence.clouddm.dsfamily.db2.execute.Db2Hooks;
import com.clougence.clouddm.dsfamily.db2.execute.Db2MetaService;
import com.clougence.clouddm.sdk.execute.session.Session;

/**
 * https://www.ibm.com/docs/zh/i/7.5?topic=statements-set-transaction
 * @author mode create time is 2021/1/12
 **/
public class Db2ForiHooks extends Db2Hooks {

    @Override
    public Db2MetaService createMetaService(Session session) {
        return new Db2ForiMetaService(session);
    }
}
