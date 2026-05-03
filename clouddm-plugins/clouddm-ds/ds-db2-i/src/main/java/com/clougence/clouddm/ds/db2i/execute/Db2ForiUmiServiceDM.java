package com.clougence.clouddm.ds.db2i.execute;

import java.sql.Connection;

import com.clougence.clouddm.dsfamily.db2.execute.Db2UmiServiceDm;

public class Db2ForiUmiServiceDM extends Db2UmiServiceDm {

    public Db2ForiUmiServiceDM(Connection con){
        super(() -> new Db2MetaProviderForAs400(con));
    }
}
