package com.clougence.adapter.postgre;

import com.clougence.schema.DsType;
import com.clougence.schema.umi.special.rdb.RdbAttributeNames;
import com.clougence.schema.umi.struts.UmiAttributeNames;

/**
 * @author 赵永春 (zyc@hasor.net)
 * @version : 2021-04-01
 */
public class PostgreAttributeNames extends RdbAttributeNames {

    private PostgreAttributeNames(String name){
        super(DsType.PostgreSQL.getShortName(), name);
    }

    protected PostgreAttributeNames(String space, String name){
        super(space, name);
    }

    //table
    public static final UmiAttributeNames INHERITED_FROM                = check(new PostgreAttributeNames("info"));
    public static final UmiAttributeNames TABLE_TYPE                    = check(new PostgreAttributeNames("tt"));
    public static final UmiAttributeNames TABLESPACE                    = check(new PostgreAttributeNames("ts"));
    public static final UmiAttributeNames FILL_FACTOR                   = check(new PostgreAttributeNames("ff"));
    public static final UmiAttributeNames PARTITION_TYPE                = check(new PostgreAttributeNames("pt"));
    public static final UmiAttributeNames WITH_OIDS                     = check(new PostgreAttributeNames("wo"));

    // column
    public static final UmiAttributeNames DATA_TYPE                     = check(new PostgreAttributeNames("dt"));
    public static final UmiAttributeNames COLUMN_TYPE                   = check(new PostgreAttributeNames("ct"));
    public static final UmiAttributeNames NUMERIC_PRECISION_RADIX       = check(new PostgreAttributeNames("npr"));
    public static final UmiAttributeNames TYPE_OID                      = check(new PostgreAttributeNames("to"));
    public static final UmiAttributeNames ELEMENT_TYPE                  = check(new PostgreAttributeNames("et"));
    public static final UmiAttributeNames CHARACTERS_MAX_LENGTH         = check(new PostgreAttributeNames("clen"));
    public static final UmiAttributeNames ARRAY_DIMENSION               = check(new PostgreAttributeNames("ad"));
    public static final UmiAttributeNames BYTES_MAX_LENGTH              = check(new PostgreAttributeNames("blen"));
    //    public static final UmiAttributeNames AUTO_INCREMENT           = check(new PostgreAttributeNames("auto"));
    public static final UmiAttributeNames COLUMN_DEFAULT_OPTION         = check(new PostgreAttributeNames("cdo"));
    public static final UmiAttributeNames GIS_SRID                      = check(new PostgreAttributeNames("gsrid"));
    public static final UmiAttributeNames GIS_TYPE                      = check(new PostgreAttributeNames("gtype"));
    public static final UmiAttributeNames VIRTUAL_TYPE                  = check(new PostgreAttributeNames("vt"));
    public static final UmiAttributeNames VIRTUAL_TYPE_INCREMENTAL      = check(new PostgreAttributeNames("vti"));
    public static final UmiAttributeNames VIRTUAL_TYPE_MIN              = check(new PostgreAttributeNames("min"));
    public static final UmiAttributeNames VIRTUAL_TYPE_MAX              = check(new PostgreAttributeNames("max"));
    public static final UmiAttributeNames VIRTUAL_TYPE_START            = check(new PostgreAttributeNames("vts"));
    public static final UmiAttributeNames VIRTUAL_TYPE_CACHE            = check(new PostgreAttributeNames("vtc"));
    public static final UmiAttributeNames VIRTUAL_TYPE_EXPRESSION       = check(new PostgreAttributeNames("vte"));
    public static final UmiAttributeNames VIRTUAL_TYPE_LOOP             = check(new PostgreAttributeNames("vtl"));
    public static final UmiAttributeNames COLUMN_SORT_RULES             = check(new PostgreAttributeNames("csr"));

    // PK\UK\FK\INDEX
    public static final UmiAttributeNames MATCH_OPTION                  = check(new PostgreAttributeNames("mo"));
    public static final UmiAttributeNames STORAGE_TYPE                  = check(new PostgreAttributeNames("st"));
    public static final UmiAttributeNames INDEX_TYPE_NAME               = check(new PostgreAttributeNames("itn"));
    public static final UmiAttributeNames CONSTRAINT_TYPE               = check(new PostgreAttributeNames("cont"));
    public static final UmiAttributeNames INDEX_WAY                     = check(new PostgreAttributeNames("inw"));
    public static final UmiAttributeNames PK_PREFIX_LENGTH              = check(new PostgreAttributeNames("ppl"));
    public static final UmiAttributeNames PK_SORT_ORDER                 = check(new PostgreAttributeNames("pso"));
    public static final UmiAttributeNames INDEX_SORT_ORDER              = check(new PostgreAttributeNames("iso"));
    public static final UmiAttributeNames INDEX_SORT_RULES              = check(new PostgreAttributeNames("isr"));
    public static final UmiAttributeNames INDEX_PREFIX_LENGTH           = check(new PostgreAttributeNames("ipl"));
    public static final UmiAttributeNames INDEX_OPERATOR_TYPE           = check(new PostgreAttributeNames("iot"));
    public static final UmiAttributeNames INDEX_NULLS_SORT              = check(new PostgreAttributeNames("ino"));
    public static final UmiAttributeNames PK_CONSTRAINT_DELAY           = check(new PostgreAttributeNames("pcd"));
    public static final UmiAttributeNames PK_CONSTRAINT_INITIAL         = check(new PostgreAttributeNames("pci"));
    public static final UmiAttributeNames INDEX_CONSTRAINT_DELAY        = check(new PostgreAttributeNames("icd"));
    public static final UmiAttributeNames INDEX_CONSTRAINT_INITIAL      = check(new PostgreAttributeNames("ici"));
    public static final UmiAttributeNames INDEX_CONCURRENTLY            = check(new PostgreAttributeNames("ic"));
    public static final UmiAttributeNames INDEX_WITH_FILLFACTOR         = check(new PostgreAttributeNames("iwf"));
    public static final UmiAttributeNames INDEX_WITH_BUFFERING          = check(new PostgreAttributeNames("iwb"));
    public static final UmiAttributeNames INDEX_WITH_FASTUPDATE         = check(new PostgreAttributeNames("iwfa"));
    public static final UmiAttributeNames INDEX_TABLESPACE              = check(new PostgreAttributeNames("it"));
    public static final UmiAttributeNames INDEX_WHERE                   = check(new PostgreAttributeNames("iw"));

    //trigger
    public static final UmiAttributeNames TRIGGER_CONSTRAINT            = check(new PostgreAttributeNames("trict"));
    public static final UmiAttributeNames TRIGGER_REFERENCED_TABLE_NAME = check(new PostgreAttributeNames("trirtn"));
    public static final UmiAttributeNames TRIGGER_TIMING                = check(new PostgreAttributeNames("trit"));
    public static final UmiAttributeNames NEW_ALIAS                     = check(new PostgreAttributeNames("newal"));
    public static final UmiAttributeNames OLD_ALIAS                     = check(new PostgreAttributeNames("oldal"));
    public static final UmiAttributeNames TRIGGER_GRANULARITY           = check(new PostgreAttributeNames("trig"));
    public static final UmiAttributeNames TRIGGER_CONDITION             = check(new PostgreAttributeNames("tricn"));

    //view
    public static final UmiAttributeNames VIEW_TEMP                     = check(new PostgreAttributeNames("viewt"));
    public static final UmiAttributeNames VIEW_SECURITY_BARRIER         = check(new PostgreAttributeNames("viewsb"));
    public static final UmiAttributeNames VIEW_CHECK_OPTION             = check(new PostgreAttributeNames("viewco"));

    //llm
    public static final UmiAttributeNames VECTOR_OP_CLASS               = check(new PostgreAttributeNames("voc"));
}
