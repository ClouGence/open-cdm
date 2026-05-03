package com.clougence.adapter.mc;

import com.clougence.schema.DsType;
import com.clougence.schema.umi.special.rdb.RdbAttributeNames;
import com.clougence.schema.umi.struts.UmiAttributeNames;

/**
 * @version : 2021-04-01
 * @author 赵永春 (zyc@hasor.net)
 */
public class McAttributeNames extends RdbAttributeNames {

    private McAttributeNames(String name){
        super(DsType.MaxCompute.getShortName(), name);
    }

    // Common
    public static final UmiAttributeNames OWNER                  = check(new McAttributeNames("cowner"));
    public static final UmiAttributeNames LAST_META_MODIFIED     = check(new McAttributeNames("clmm"));
    public static final UmiAttributeNames LAST_DATA_MODIFIED     = check(new McAttributeNames("cldm"));
    public static final UmiAttributeNames LAST_ACCESS_MODIFIED   = check(new McAttributeNames("clam"));
    public static final UmiAttributeNames DATA_BYTES             = check(new McAttributeNames("cdsize"));
    public static final UmiAttributeNames PHYSICAL_BYTES         = check(new McAttributeNames("cpsize"));
    public static final UmiAttributeNames FILE_NUMBER            = check(new McAttributeNames("cfsnum"));
    public static final UmiAttributeNames RECORD_NUM             = check(new McAttributeNames("ccnt"));
    public static final UmiAttributeNames LIFE_DAY               = check(new McAttributeNames("clife"));
    public static final UmiAttributeNames ARCHIVED               = check(new McAttributeNames("carc"));
    public static final UmiAttributeNames CREATED_TIME           = check(new McAttributeNames("ccre"));

    // table
    public static final UmiAttributeNames TABLE_ID               = check(new McAttributeNames("tid"));
    public static final UmiAttributeNames TABLE_CRYPTO_ALGO_NAME = check(new McAttributeNames("tcry"));
    public static final UmiAttributeNames TABLE_MAX_LABEL        = check(new McAttributeNames("tlabel"));
    public static final UmiAttributeNames TABLE_LABEL            = check(new McAttributeNames("tlabel"));

    // partition

    // column
    public static final UmiAttributeNames COLUMN_TYPE            = check(new McAttributeNames("ct"));

    // func
    public static final UmiAttributeNames CLASS_PATH             = check(new McAttributeNames("fclass"));

    // role
    public static final UmiAttributeNames ROLE_POLICY            = check(new McAttributeNames("rpol"));
    public static final UmiAttributeNames ROLE_TYPE              = check(new McAttributeNames("rtype"));
    public static final UmiAttributeNames ROLE_ACL               = check(new McAttributeNames("racl"));

}
