package com.clougence.adapter.redis;

import com.clougence.schema.DsType;
import com.clougence.schema.umi.special.rdb.RdbAttributeNames;
import com.clougence.schema.umi.struts.UmiAttributeNames;

/**
 * @author 赵永春 (zyc@hasor.net)
 * @version : 2021-04-01
 */
public class RedisAttributeNames extends RdbAttributeNames {

    private RedisAttributeNames(String name){
        super(DsType.Redis.getShortName(), name);
    }

    protected RedisAttributeNames(String space, String name){
        super(space, name);
    }

    // key
    public static final UmiAttributeNames KEY_TYPE = check(new RedisAttributeNames("type"));
}
