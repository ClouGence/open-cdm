package com.clougence.adapter.polar.porpg;

import com.clougence.adapter.postgre.PostgreAttributeNames;
import com.clougence.schema.DsType;

/**
 * @version : 2021-04-01
 * @author 赵永春 (zyc@hasor.net)
 */
public class PolarDBPgAttributeNames extends PostgreAttributeNames {

    private PolarDBPgAttributeNames(String name){
        super(DsType.PolarDBPostgre.getShortName(), name);
    }

    protected PolarDBPgAttributeNames(String space, String name){
        super(space, name);
    }
}
