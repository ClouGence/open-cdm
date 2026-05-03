package com.clougence.adapter.mysql;

import com.clougence.schema.DsType;
import com.clougence.schema.umi.special.rdb.RdbAttributeNames;
import com.clougence.schema.umi.struts.UmiAttributeNames;

/**
 * @version : 2021-04-01
 * @author 赵永春 (zyc@hasor.net)
 */
public class MyUmiAttributeNames extends RdbAttributeNames {

    private MyUmiAttributeNames(String name){
        super(DsType.MySQL.getShortName(), name);
    }

    protected MyUmiAttributeNames(String space, String name){
        super(space, name);
    }

    // common
    public static final UmiAttributeNames COLLATION                      = check(new MyUmiAttributeNames("coll"));
    public static final UmiAttributeNames CHARACTER_SET                  = check(new MyUmiAttributeNames("cset"));
    public static final UmiAttributeNames KEY_BLOCK_SIZE                 = check(new MyUmiAttributeNames("kbs"));
    public static final UmiAttributeNames DEFAULT_COLLATION_NAME         = check(new MyUmiAttributeNames("dcn"));
    public static final UmiAttributeNames DEFAULT_CHARACTER_SET_NAME     = check(new MyUmiAttributeNames("dcsn"));
    public static final UmiAttributeNames DEFINER                        = check(new MyUmiAttributeNames("de"));
    public static final UmiAttributeNames SECURITY_TYPE                  = check(new MyUmiAttributeNames("st"));

    // table
    public static final UmiAttributeNames CREATE_TIME                    = check(new MyUmiAttributeNames("ctime"));
    public static final UmiAttributeNames UPDATE_TIME                    = check(new MyUmiAttributeNames("utime"));
    public static final UmiAttributeNames TEMPORARY                      = check(new MyUmiAttributeNames("tem"));
    public static final UmiAttributeNames ROW_FORMAT                     = check(new MyUmiAttributeNames("rfmt"));
    public static final UmiAttributeNames COMPRESSION                    = check(new MyUmiAttributeNames("com"));
    public static final UmiAttributeNames ENGINE                         = check(new MyUmiAttributeNames("eng"));
    public static final UmiAttributeNames AUTO_INCREMENT_STAR            = check(new MyUmiAttributeNames("inc"));
    public static final UmiAttributeNames AVG_ROW_LENGTH                 = check(new MyUmiAttributeNames("arl"));
    public static final UmiAttributeNames MIN_ROWS                       = check(new MyUmiAttributeNames("minr"));
    public static final UmiAttributeNames MAX_ROWS                       = check(new MyUmiAttributeNames("maxr"));
    public static final UmiAttributeNames CHECK_TIME                     = check(new MyUmiAttributeNames("checkt"));
    public static final UmiAttributeNames INDEX_LENGTH                   = check(new MyUmiAttributeNames("ilen"));
    public static final UmiAttributeNames DATA_LENGTH                    = check(new MyUmiAttributeNames("dlen"));
    public static final UmiAttributeNames DATA_ROW                       = check(new MyUmiAttributeNames("drow"));
    public static final UmiAttributeNames CREATE_OPTIONS                 = check(new MyUmiAttributeNames("copt"));
    public static final UmiAttributeNames DATA_FREE                      = check(new MyUmiAttributeNames("dafr"));

    // column
    public static final UmiAttributeNames DATA_TYPE                      = check(new MyUmiAttributeNames("dt"));
    public static final UmiAttributeNames COLUMN_TYPE                    = check(new MyUmiAttributeNames("ct"));
    public static final UmiAttributeNames ZEROFILL                       = check(new MyUmiAttributeNames("zf"));
    public static final UmiAttributeNames UNSIGNED                       = check(new MyUmiAttributeNames("ug"));
    public static final UmiAttributeNames CHARACTERS_MAX_LENGTH          = check(new MyUmiAttributeNames("clen"));
    public static final UmiAttributeNames BYTES_MAX_LENGTH               = check(new MyUmiAttributeNames("blen"));
    public static final UmiAttributeNames CURRENT_UPDATE_TYPE            = check(new MyUmiAttributeNames("cut"));
    public static final UmiAttributeNames GENERATED                      = check(new MyUmiAttributeNames("gen"));
    public static final UmiAttributeNames DEFAULT_ENUMSET_COLLATION      = check(new MyUmiAttributeNames("esc"));

    // PK\UK\FK\INDEX
    public static final UmiAttributeNames CONSTRAINT_TYPE                = check(new MyUmiAttributeNames("cont"));
    public static final UmiAttributeNames INDEX_WAY                      = check(new MyUmiAttributeNames("inw"));
    public static final UmiAttributeNames STORAGE_TYPE                   = check(new MyUmiAttributeNames("st"));
    public static final UmiAttributeNames SUB_PART                       = check(new MyUmiAttributeNames("sp"));
    public static final UmiAttributeNames SUB_ORDER                      = check(new MyUmiAttributeNames("so"));
    public static final UmiAttributeNames FK_TABLE                       = check(new MyUmiAttributeNames("fktb"));

    // PROCEDURE/FUNCTION
    public static final UmiAttributeNames DETERMINISTIC                  = check(new MyUmiAttributeNames("deterministic"));
    public static final UmiAttributeNames SQL_DATA_ACCESS                = check(new MyUmiAttributeNames("sqlda"));

    // TRIGGER
    public static final UmiAttributeNames EVENT_MANIPULATION             = check(new MyUmiAttributeNames("evpu"));
    public static final UmiAttributeNames ACTION_TIMING                  = check(new MyUmiAttributeNames("acti"));
    //VIEW
    public static final UmiAttributeNames VIEW_CHECK_OPTION              = check(new MyUmiAttributeNames("viewco"));
    public static final UmiAttributeNames VIEW_UPDATABLE                 = check(new MyUmiAttributeNames("viewup"));
    //PARTITION
    // partition
    public static final UmiAttributeNames PARTITION_METHOD               = check(new MyUmiAttributeNames("pmt"));
    public static final UmiAttributeNames PARTITION_EXPRESSION           = check(new MyUmiAttributeNames("pes"));
    public static final UmiAttributeNames PARTITION_COUNT                = check(new MyUmiAttributeNames("pct"));
    public static final UmiAttributeNames SUB_PARTITION_METHOD           = check(new MyUmiAttributeNames("spmt"));
    public static final UmiAttributeNames SUB_PARTITION_EXPRESSION       = check(new MyUmiAttributeNames("spes"));
    public static final UmiAttributeNames SUB_PARTITION_COUNT            = check(new MyUmiAttributeNames("spct"));

    public static final UmiAttributeNames PARTITION_ITEM_DATA_DIRECTORY  = check(new MyUmiAttributeNames("pidd"));
    public static final UmiAttributeNames PARTITION_ITEM_INDEX_DIRECTORY = check(new MyUmiAttributeNames("piid"));
    public static final UmiAttributeNames PARTITION_ITEM_MAX_ROW         = check(new MyUmiAttributeNames("pimaxr"));
    public static final UmiAttributeNames PARTITION_ITEM_MIN_ROW         = check(new MyUmiAttributeNames("piminr"));
    public static final UmiAttributeNames PARTITION_ITEM_COMMENT         = check(new MyUmiAttributeNames("pic"));

}
