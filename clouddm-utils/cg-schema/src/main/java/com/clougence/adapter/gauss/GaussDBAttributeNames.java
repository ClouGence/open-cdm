package com.clougence.adapter.gauss;

import com.clougence.schema.DsType;
import com.clougence.schema.umi.special.rdb.RdbAttributeNames;
import com.clougence.schema.umi.struts.UmiAttributeNames;

/**
 * @author 赵永春 (zyc@hasor.net)
 * @version : 2021-04-01
 */
public class GaussDBAttributeNames extends RdbAttributeNames {

    private GaussDBAttributeNames(String name){
        super(DsType.PostgreSQL.getShortName(), name);
    }

    protected GaussDBAttributeNames(String space, String name){
        super(space, name);
    }

    //table
    public static final UmiAttributeNames INHERITED_FROM           = check(new GaussDBAttributeNames("info"));
    public static final UmiAttributeNames TABLE_TYPE               = check(new GaussDBAttributeNames("tt"));
    public static final UmiAttributeNames TABLESPACE               = check(new GaussDBAttributeNames("ts"));
    public static final UmiAttributeNames FILL_FACTOR              = check(new GaussDBAttributeNames("ff"));
    public static final UmiAttributeNames PARTITION_TYPE           = check(new GaussDBAttributeNames("pt"));
    public static final UmiAttributeNames WITH_OIDS                = check(new GaussDBAttributeNames("wo"));
    public static final UmiAttributeNames ORIENTATION              = check(new GaussDBAttributeNames("ori"));
    public static final UmiAttributeNames STORAGE_TYPE             = check(new GaussDBAttributeNames("st"));
    public static final UmiAttributeNames COMPRESS_TYPE            = check(new GaussDBAttributeNames("compress_t"));
    public static final UmiAttributeNames COMPRESS_LEVEL           = check(new GaussDBAttributeNames("compress_l"));

    // column
    public static final UmiAttributeNames DATA_TYPE                = check(new GaussDBAttributeNames("dt"));
    public static final UmiAttributeNames COLUMN_TYPE              = check(new GaussDBAttributeNames("ct"));
    public static final UmiAttributeNames NUMERIC_PRECISION_RADIX  = check(new GaussDBAttributeNames("npr"));
    public static final UmiAttributeNames TYPE_OID                 = check(new GaussDBAttributeNames("to"));
    public static final UmiAttributeNames ELEMENT_TYPE             = check(new GaussDBAttributeNames("et"));
    public static final UmiAttributeNames CHARACTERS_MAX_LENGTH    = check(new GaussDBAttributeNames("clen"));
    public static final UmiAttributeNames ARRAY_DIMENSION          = check(new GaussDBAttributeNames("ad"));
    public static final UmiAttributeNames BYTES_MAX_LENGTH         = check(new GaussDBAttributeNames("blen"));
    public static final UmiAttributeNames COLUMN_DEFAULT_OPTION    = check(new GaussDBAttributeNames("cdo"));
    public static final UmiAttributeNames GIS_SRID                 = check(new GaussDBAttributeNames("gsrid"));
    public static final UmiAttributeNames GIS_TYPE                 = check(new GaussDBAttributeNames("gtype"));
    public static final UmiAttributeNames VIRTUAL_TYPE             = check(new GaussDBAttributeNames("vt"));
    public static final UmiAttributeNames VIRTUAL_TYPE_INCREMENTAL = check(new GaussDBAttributeNames("vti"));
    public static final UmiAttributeNames VIRTUAL_TYPE_MIN         = check(new GaussDBAttributeNames("min"));
    public static final UmiAttributeNames VIRTUAL_TYPE_MAX         = check(new GaussDBAttributeNames("max"));
    public static final UmiAttributeNames VIRTUAL_TYPE_START       = check(new GaussDBAttributeNames("vts"));
    public static final UmiAttributeNames VIRTUAL_TYPE_CACHE       = check(new GaussDBAttributeNames("vtc"));
    public static final UmiAttributeNames VIRTUAL_TYPE_EXPRESSION  = check(new GaussDBAttributeNames("vte"));
    public static final UmiAttributeNames VIRTUAL_TYPE_LOOP        = check(new GaussDBAttributeNames("vtl"));
    public static final UmiAttributeNames COLUMN_SORT_RULES        = check(new GaussDBAttributeNames("csr"));

    // PK\UK\FK\INDEX
    public static final UmiAttributeNames MATCH_OPTION             = check(new GaussDBAttributeNames("mo"));
    public static final UmiAttributeNames INDEX_TYPE_NAME          = check(new GaussDBAttributeNames("itn"));
    public static final UmiAttributeNames CONSTRAINT_TYPE          = check(new GaussDBAttributeNames("cont"));
    public static final UmiAttributeNames INDEX_WAY                = check(new GaussDBAttributeNames("inw"));
    public static final UmiAttributeNames PK_PREFIX_LENGTH         = check(new GaussDBAttributeNames("ppl"));
    public static final UmiAttributeNames PK_SORT_ORDER            = check(new GaussDBAttributeNames("pso"));
    public static final UmiAttributeNames INDEX_SORT_ORDER         = check(new GaussDBAttributeNames("iso"));
    public static final UmiAttributeNames INDEX_SORT_RULES         = check(new GaussDBAttributeNames("isr"));
    public static final UmiAttributeNames INDEX_PREFIX_LENGTH      = check(new GaussDBAttributeNames("ipl"));
    public static final UmiAttributeNames INDEX_OPERATOR_TYPE      = check(new GaussDBAttributeNames("iot"));
    public static final UmiAttributeNames INDEX_NULLS_SORT         = check(new GaussDBAttributeNames("ino"));
    public static final UmiAttributeNames PK_CONSTRAINT_DELAY      = check(new GaussDBAttributeNames("pcd"));
    public static final UmiAttributeNames PK_CONSTRAINT_INITIAL    = check(new GaussDBAttributeNames("pci"));
    public static final UmiAttributeNames INDEX_CONSTRAINT_DELAY   = check(new GaussDBAttributeNames("icd"));
    public static final UmiAttributeNames INDEX_CONSTRAINT_INITIAL = check(new GaussDBAttributeNames("ici"));
    public static final UmiAttributeNames INDEX_CONCURRENTLY       = check(new GaussDBAttributeNames("ic"));
    public static final UmiAttributeNames INDEX_WITH_FILLFACTOR    = check(new GaussDBAttributeNames("iwf"));
    public static final UmiAttributeNames INDEX_WITH_BUFFERING     = check(new GaussDBAttributeNames("iwb"));
    public static final UmiAttributeNames INDEX_WITH_FASTUPDATE    = check(new GaussDBAttributeNames("iwfa"));
    public static final UmiAttributeNames INDEX_TABLESPACE         = check(new GaussDBAttributeNames("it"));
    public static final UmiAttributeNames INDEX_WHERE              = check(new GaussDBAttributeNames("iw"));

    //    //trigger
    //    public static final UmiAttributeNames TRIGGER_CONSTRAINT            = check(new GaussDBAttributeNames("trict"));
    //    public static final UmiAttributeNames TRIGGER_REFERENCED_TABLE_NAME = check(new GaussDBAttributeNames("trirtn"));
    //    public static final UmiAttributeNames TRIGGER_TIMING                = check(new GaussDBAttributeNames("trit"));
    //    public static final UmiAttributeNames NEW_ALIAS                     = check(new GaussDBAttributeNames("newal"));
    //    public static final UmiAttributeNames OLD_ALIAS                     = check(new GaussDBAttributeNames("oldal"));
    //    public static final UmiAttributeNames TRIGGER_GRANULARITY           = check(new GaussDBAttributeNames("trig"));
    //    public static final UmiAttributeNames TRIGGER_CONDITION             = check(new GaussDBAttributeNames("tricn"));

    //view
    public static final UmiAttributeNames VIEW_TEMP                = check(new GaussDBAttributeNames("viewt"));
    public static final UmiAttributeNames VIEW_SECURITY_BARRIER    = check(new GaussDBAttributeNames("viewsb"));
    public static final UmiAttributeNames VIEW_CHECK_OPTION        = check(new GaussDBAttributeNames("viewco"));

    //llm
    public static final UmiAttributeNames VECTOR_OP_CLASS          = check(new GaussDBAttributeNames("voc"));
}
