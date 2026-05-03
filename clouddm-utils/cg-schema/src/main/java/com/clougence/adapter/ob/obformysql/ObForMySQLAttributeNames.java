package com.clougence.adapter.ob.obformysql;

import com.clougence.adapter.mysql.MyUmiAttributeNames;
import com.clougence.schema.DsType;

/**
 * @author wanshao create time is 2022/02/08
 **/
public class ObForMySQLAttributeNames extends MyUmiAttributeNames {

    private ObForMySQLAttributeNames(String name){
        super(DsType.OceanBase.getShortName(), name);
    }

}
