package com.clougence.adapter.tidb;

import com.clougence.adapter.mysql.MyUmiAttributeNames;
import com.clougence.schema.DsType;
import com.clougence.schema.umi.struts.UmiAttributeNames;

/**
 * @version : 2021-04-01
 * @author 赵永春 (zyc@hasor.net)
 */
public class TiDBAttributeNames extends MyUmiAttributeNames {

    protected TiDBAttributeNames(String name){
        super(DsType.TiDB.getShortName(), name);
    }

    // table
    public static final UmiAttributeNames SHARD_ROW_ID_BITS = check(new TiDBAttributeNames("shabits"));
    public static final UmiAttributeNames PRE_SPLIT_REGIONS = check(new TiDBAttributeNames("psplr"));
    public static final UmiAttributeNames TIDB_PK_TYPE      = check(new TiDBAttributeNames("tipktype"));

    // column

    // PK\UK\FK\INDEX

}
