package com.clougence.adapter.hana;

import com.clougence.schema.DsType;
import com.clougence.schema.umi.special.rdb.RdbAttributeNames;
import com.clougence.schema.umi.struts.UmiAttributeNames;

/**
 * @version : 2021-04-01
 * @author 赵永春 (zyc@hasor.net)
 */
public class HanaAttributeNames extends RdbAttributeNames {

    private HanaAttributeNames(String name){
        super(DsType.Hana.getShortName(), name);
    }

    // table
    public static final UmiAttributeNames CREATE_TIME           = new HanaAttributeNames("ctime");
    public static final UmiAttributeNames UPDATE_TIME           = new HanaAttributeNames("utime");
    public static final UmiAttributeNames TABLE_TYPE            = new HanaAttributeNames("tableType");
    public static final UmiAttributeNames TABLE_AUTO_MERGE_ON   = new HanaAttributeNames("tamo");
    public static final UmiAttributeNames TABLE_UNLOAD_PRIORITY = new HanaAttributeNames("tup");

    // column
    public static final UmiAttributeNames DATA_TYPE             = new HanaAttributeNames("dt");
    public static final UmiAttributeNames COLUMN_TYPE           = new HanaAttributeNames("ct");
    public static final UmiAttributeNames CHARACTERS_MAX_LENGTH = new HanaAttributeNames("clen");
    public static final UmiAttributeNames BYTES_MAX_LENGTH      = new HanaAttributeNames("blen");
    public static final UmiAttributeNames CONSTRAINT_TYPE       = new HanaAttributeNames("cont");
    public static final UmiAttributeNames GENERATION_TYPE       = new HanaAttributeNames("gent");
    public static final UmiAttributeNames GENERATION_ALWAYS_AS  = new HanaAttributeNames("genaa");
    public static final UmiAttributeNames ORDER_TYPE            = new HanaAttributeNames("ot");
    public static final UmiAttributeNames GEO_TYPE              = new HanaAttributeNames("geot");
    public static final UmiAttributeNames INDEX_WAY             = new HanaAttributeNames("inw");

    //trigger
    public static final UmiAttributeNames NEW_ALIAS             = check(new HanaAttributeNames("newal"));
    public static final UmiAttributeNames OLD_ALIAS             = check(new HanaAttributeNames("oldal"));
    public static final UmiAttributeNames CONDITION             = check(new HanaAttributeNames("cond"));
    public static final UmiAttributeNames TRIGGER_GRANULARITY   = check(new HanaAttributeNames("trig"));

}
