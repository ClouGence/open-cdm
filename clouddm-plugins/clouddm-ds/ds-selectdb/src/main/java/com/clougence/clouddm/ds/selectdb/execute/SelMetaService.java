package com.clougence.clouddm.ds.selectdb.execute;

import java.sql.Connection;

import com.clougence.clouddm.ds.doris.execute.DrMetaService;
import com.clougence.clouddm.sdk.execute.session.Session;
import com.clougence.clouddm.sdk.execute.session.rdb.DmRdbUmiService;

import lombok.extern.slf4j.Slf4j;

/**
* @author Ekko
* @date 2023/3/28 14:13
*/
@Slf4j
public class SelMetaService extends DrMetaService {

    public SelMetaService(Session rdbSession){
        super(rdbSession);
    }

    @Override
    protected DmRdbUmiService rdbUmiService(Connection con) {
        return new SelUmiServiceDm(con);
    }

}
