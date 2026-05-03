package com.clougence.adapter.polar.porx;

import com.clougence.adapter.mysql.MyUmiAttributeNames;
import com.clougence.schema.DsType;
import com.clougence.schema.umi.struts.UmiAttributeNames;

/**
 * @version : 2021-04-01
 * @author zylicfc
 */
public class PolarDbXAttributeNames extends MyUmiAttributeNames {

    private PolarDbXAttributeNames(String name){
        super(DsType.PolarDbX.getShortName(), name);
    }

    protected PolarDbXAttributeNames(String space, String name){
        super(space, name);
    }

    public static final UmiAttributeNames SHARD_DEF     = check(new PolarDbXAttributeNames("pxcshard"));

    public static final UmiAttributeNames PARTITION_DEF = check(new PolarDbXAttributeNames("pxcpt"));
}
