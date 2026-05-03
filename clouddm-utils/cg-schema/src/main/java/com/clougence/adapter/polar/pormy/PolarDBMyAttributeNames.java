package com.clougence.adapter.polar.pormy;

import com.clougence.adapter.mysql.MyUmiAttributeNames;
import com.clougence.schema.DsType;

/**
 * @version : 2021-04-01
 * @author 赵永春 (zyc@hasor.net)
 */
public class PolarDBMyAttributeNames extends MyUmiAttributeNames {

    private PolarDBMyAttributeNames(String name){
        super(DsType.PolarDBMySQL.getShortName(), name);
    }

    protected PolarDBMyAttributeNames(String space, String name){
        super(space, name);
    }
}
