package com.clougence.adapter.db2;

import com.clougence.schema.DsType;
import com.clougence.schema.umi.special.rdb.RdbAttributeNames;
import com.clougence.schema.umi.struts.UmiAttributeNames;

/**
 * @version : 2021-04-01
 * @author 赵永春 (zyc@hasor.net)
 */
public class Db2AttributeNames extends RdbAttributeNames {

    private Db2AttributeNames(String name){
        super(DsType.Db2.getShortName(), name);
    }

    // catalog
    public static final UmiAttributeNames CAT_STATUS                    = check(new Db2AttributeNames("cats"));
    public static final UmiAttributeNames READ_ONLY                     = check(new Db2AttributeNames("ro"));
    public static final UmiAttributeNames FULLTEXT_ENABLED              = check(new Db2AttributeNames("fe"));
    public static final UmiAttributeNames DEFAULT_LANGUAGE_NAME         = check(new Db2AttributeNames("dln"));
    public static final UmiAttributeNames DEFAULT_FULLTEXT_LANGUAGE     = check(new Db2AttributeNames("dfl"));
    public static final UmiAttributeNames DEFAULT_COLLATION_NAME        = check(new Db2AttributeNames("dcn"));

    // schema
    public static final UmiAttributeNames OWNER_TYPE                    = check(new Db2AttributeNames("ownt"));
    public static final UmiAttributeNames DEFAULT_CHARACTER_SET_CATALOG = check(new Db2AttributeNames("dcsc"));
    public static final UmiAttributeNames DEFAULT_CHARACTER_SET_SCHEMA  = check(new Db2AttributeNames("dcss"));
    public static final UmiAttributeNames DEFAULT_CHARACTER_SET_NAME    = check(new Db2AttributeNames("dcsn"));

    // table
    public static final UmiAttributeNames CREATE_DATE                   = check(new Db2AttributeNames("ctime"));
    public static final UmiAttributeNames UPDATE_DATE                   = check(new Db2AttributeNames("utime"));
    public static final UmiAttributeNames INVALID_DATE                  = check(new Db2AttributeNames("itime"));
    public static final UmiAttributeNames STATS_TIME                    = check(new Db2AttributeNames("stime"));
    public static final UmiAttributeNames COLUMN_COUNT                  = check(new Db2AttributeNames("colc"));
    public static final UmiAttributeNames ROW_COUNT                     = check(new Db2AttributeNames("rowc"));
    public static final UmiAttributeNames NPAGES                        = check(new Db2AttributeNames("npages"));
    public static final UmiAttributeNames MPAGES                        = check(new Db2AttributeNames("mpages"));
    public static final UmiAttributeNames FPAGES                        = check(new Db2AttributeNames("fpages"));
    public static final UmiAttributeNames SYSTEM_TABLE                  = check(new Db2AttributeNames("syst"));
    public static final UmiAttributeNames INSERTABLE                    = check(new Db2AttributeNames("inser"));
    public static final UmiAttributeNames DELETABLE                     = check(new Db2AttributeNames("dele"));
    public static final UmiAttributeNames UPDATABLE                     = check(new Db2AttributeNames("upda"));
    public static final UmiAttributeNames DEFINER                       = check(new Db2AttributeNames("definer"));

    // column
    public static final UmiAttributeNames DATA_TYPE                     = check(new Db2AttributeNames("dt"));
    public static final UmiAttributeNames MAX_LENGTH                    = check(new Db2AttributeNames("mlen"));
    public static final UmiAttributeNames COLLATION_NAME                = check(new Db2AttributeNames("cn"));
    public static final UmiAttributeNames IDENTITY                      = check(new Db2AttributeNames("ide"));

    // PK\UK\FK\INDEX
    public static final UmiAttributeNames INDEX_TYPE                    = check(new Db2AttributeNames("it"));
    public static final UmiAttributeNames CONSTRAINT_TYPE               = check(new Db2AttributeNames("cont"));

    // view
    public static final UmiAttributeNames VIEW_CHECK_OPTION             = check(new Db2AttributeNames("viewco"));
    public static final UmiAttributeNames VIEW_READ_ONLY                = check(new Db2AttributeNames("vro"));

    //trigger
    public static final UmiAttributeNames NEW_ALIAS                     = check(new Db2AttributeNames("newal"));
    public static final UmiAttributeNames OLD_ALIAS                     = check(new Db2AttributeNames("oldal"));
    public static final UmiAttributeNames NEW_TABLE_ALIAS               = check(new Db2AttributeNames("newtabal"));
    public static final UmiAttributeNames OLD_TABLE_ALIAS               = check(new Db2AttributeNames("oldtabal"));
    public static final UmiAttributeNames CONDITION                     = check(new Db2AttributeNames("cond"));
    public static final UmiAttributeNames TRIGGER_GRANULARITY           = check(new Db2AttributeNames("trig"));
    public static final UmiAttributeNames ENCRYPTION                    = check(new Db2AttributeNames("encry"));
    public static final UmiAttributeNames UPDATE_EVENT                  = check(new Db2AttributeNames("upeve"));
    public static final UmiAttributeNames DELETE_EVENT                  = check(new Db2AttributeNames("deeve"));
    public static final UmiAttributeNames INSERT_EVENT                  = check(new Db2AttributeNames("ineve"));
    public static final UmiAttributeNames SECURE                        = check(new Db2AttributeNames("trise"));

    //sequence
    public static final UmiAttributeNames CYCLE                         = check(new Db2AttributeNames("cycle"));
    public static final UmiAttributeNames NEXT_CACHE_VALUE              = check(new Db2AttributeNames("ncv"));
    public static final UmiAttributeNames START_VALUE                   = check(new Db2AttributeNames("starva"));
    public static final UmiAttributeNames CACHE_SIZE                    = check(new Db2AttributeNames("casi"));
    public static final UmiAttributeNames ORDER                         = check(new Db2AttributeNames("order"));

    // routine
    public static final UmiAttributeNames DEBUG_MODE                    = check(new Db2AttributeNames("demo"));
    public static final UmiAttributeNames DETERMINISTIC                 = check(new Db2AttributeNames("deter"));

}
