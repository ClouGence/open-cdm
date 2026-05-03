package com.clougence.adapter.dameng;

import com.clougence.schema.DsType;
import com.clougence.schema.umi.special.rdb.RdbAttributeNames;
import com.clougence.schema.umi.struts.UmiAttributeNames;

/**
 * @version : 2021-04-01
 * @author 赵永春 (zyc@hasor.net)
 */
public class DmAttributeNames extends RdbAttributeNames {

    private DmAttributeNames(String name){
        super(DsType.Dameng.getShortName(), name);
    }

    // schema
    public static final UmiAttributeNames CREATED                                       = check(new DmAttributeNames("cd"));

    // table
    public static final UmiAttributeNames TABLESPACE                                    = check(new DmAttributeNames("ts"));
    public static final UmiAttributeNames READ_ONLY                                     = check(new DmAttributeNames("ro"));
    public static final UmiAttributeNames LOG_TABLE                                     = check(new DmAttributeNames("logt"));
    public static final UmiAttributeNames LOG_ROWIDS                                    = check(new DmAttributeNames("logr"));
    public static final UmiAttributeNames LOG_PK                                        = check(new DmAttributeNames("logpk"));
    public static final UmiAttributeNames LOG_SEQ                                       = check(new DmAttributeNames("logseq"));
    public static final UmiAttributeNames TABLE_TYPE                                    = check(new DmAttributeNames("tt"));

    // column
    public static final UmiAttributeNames COLUMN_TYPE                                   = check(new DmAttributeNames("ct"));
    public static final UmiAttributeNames COLUMN_TYPE_OWNER                             = check(new DmAttributeNames("cto"));
    public static final UmiAttributeNames CHARACTER_SET_NAME                            = check(new DmAttributeNames("csn"));
    public static final UmiAttributeNames DATA_CHAR_LENGTH                              = check(new DmAttributeNames("clen"));
    public static final UmiAttributeNames DATA_BYTES_LENGTH                             = check(new DmAttributeNames("dlen"));
    public static final UmiAttributeNames HIDDEN                                        = check(new DmAttributeNames("hid"));
    public static final UmiAttributeNames DEFAULT_OPTION                                = check(new DmAttributeNames("do"));
    public static final UmiAttributeNames VIRTUAL                                       = check(new DmAttributeNames("vir"));
    public static final UmiAttributeNames VIRTUAL_EXPR                                  = check(new DmAttributeNames("vire"));
    public static final UmiAttributeNames IDENTITY                                      = check(new DmAttributeNames("ide"));
    public static final UmiAttributeNames IDENTITY_SEED                                 = check(new DmAttributeNames("ides"));
    public static final UmiAttributeNames IDENTITY_INCREMENT                            = check(new DmAttributeNames("idei"));

    // public static final UmiAttributeNames SENSITIVE = check(new OracleAttributeNames("sen"));

    // PK\UK\FK\INDEX
    public static final UmiAttributeNames ENABLED                                       = check(new DmAttributeNames("enb"));
    public static final UmiAttributeNames VALIDATED                                     = check(new DmAttributeNames("val"));
    public static final UmiAttributeNames CONSTRAINT_TYPE                               = check(new DmAttributeNames("cont"));
    public static final UmiAttributeNames STORAGE_TYPE                                  = check(new DmAttributeNames("st"));
    public static final UmiAttributeNames INDEX_WAY                                     = check(new DmAttributeNames("iw"));

    // partition
    public static final UmiAttributeNames PARTITION_ITEM_NAME                           = check(new DmAttributeNames("pin"));
    public static final UmiAttributeNames PARTITION_ITEM_DESCRIPTION                    = check(new DmAttributeNames("pid"));
    public static final UmiAttributeNames PARTITION_ITEM_BOUNDARY_VALUE                 = check(new DmAttributeNames("pibv"));
    public static final UmiAttributeNames PARTITION_ITEM_BOUNDARY_TYPE                  = check(new DmAttributeNames("pibt"));
    public static final UmiAttributeNames PARTITION_ITEM_TABLESPACE                     = check(new DmAttributeNames("pits"));
    public static final UmiAttributeNames PARTITION_ITEM_INITIAL_ALLOCATION_OF_CLUSTERS = check(new DmAttributeNames("piiaoc"));
    public static final UmiAttributeNames PARTITION_ITEM_NEXT_ALLOCATION_OF_CLUSTERS    = check(new DmAttributeNames("pinaoc"));
    public static final UmiAttributeNames PARTITION_ITEM_MIN_ALLOCATION_OF_CLUSTERS     = check(new DmAttributeNames("pimaoc"));
    public static final UmiAttributeNames PARTITION_ITEM_FILL_RATIO                     = check(new DmAttributeNames("pifr"));
    public static final UmiAttributeNames PARTITION_ITEM_RANGE_INTERVAL                 = check(new DmAttributeNames("piri"));

    //trigger
    public static final UmiAttributeNames NEW_ALIAS                                     = check(new DmAttributeNames("newal"));
    public static final UmiAttributeNames OLD_ALIAS                                     = check(new DmAttributeNames("oldal"));
    public static final UmiAttributeNames CONDITION                                     = check(new DmAttributeNames("cond"));
    public static final UmiAttributeNames TRIGGER_GRANULARITY                           = check(new DmAttributeNames("trig"));
    public static final UmiAttributeNames ENCRYPTION                                    = check(new DmAttributeNames("encry"));

}
