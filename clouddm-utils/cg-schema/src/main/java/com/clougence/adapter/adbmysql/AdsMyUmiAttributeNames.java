package com.clougence.adapter.adbmysql;

import com.clougence.adapter.mysql.MyUmiAttributeNames;
import com.clougence.schema.DsType;
import com.clougence.schema.umi.struts.UmiAttributeNames;

/**
 * @version : 2021-04-01
 * @author 赵永春 (zyc@hasor.net)
 */
public class AdsMyUmiAttributeNames extends MyUmiAttributeNames {

    private AdsMyUmiAttributeNames(String name){
        super(DsType.AdbForMySQL.getShortName(), name);
    }

    // table
    public static final UmiAttributeNames STORAGE_POLICY            = check(new AdsMyUmiAttributeNames("sp"));
    public static final UmiAttributeNames STORAGE_POLICY_HOT_PT_CNT = check(new AdsMyUmiAttributeNames("ptcnt"));
    public static final UmiAttributeNames BLOCK_SIZE                = check(new AdsMyUmiAttributeNames("bsize"));
    public static final UmiAttributeNames RT_ENGINE                 = check(new AdsMyUmiAttributeNames("reng"));
    public static final UmiAttributeNames TAB_PROPERTIES            = check(new AdsMyUmiAttributeNames("tabp"));

    // column
    public static final UmiAttributeNames TYPE_STRUCTURE            = check(new AdsMyUmiAttributeNames("st"));

    // PK\UK\FK\INDEX
    public static final UmiAttributeNames ANN_ALGORITHM             = check(new AdsMyUmiAttributeNames("anna"));
    public static final UmiAttributeNames ANN_DISFUNCTION           = check(new AdsMyUmiAttributeNames("annd"));

}
