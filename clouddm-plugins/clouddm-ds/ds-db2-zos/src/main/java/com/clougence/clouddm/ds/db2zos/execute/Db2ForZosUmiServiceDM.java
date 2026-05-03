package com.clougence.clouddm.ds.db2zos.execute;

import java.sql.Connection;

import com.clougence.clouddm.dsfamily.db2.execute.Db2UmiServiceDm;

public class Db2ForZosUmiServiceDM extends Db2UmiServiceDm {

    public Db2ForZosUmiServiceDM(Connection con){
        super(() -> new Db2ForZosMetaProvider(con));
    }
}
