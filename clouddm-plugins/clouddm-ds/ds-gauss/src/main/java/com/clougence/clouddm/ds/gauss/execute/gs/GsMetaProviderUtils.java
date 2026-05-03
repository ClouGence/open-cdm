package com.clougence.clouddm.ds.gauss.execute.gs;

import static com.clougence.adapter.gauss.GaussDBAttributeNames.*;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.clougence.clouddm.dsfamily.postgres.execute.PgMetaProviderUtils;
import com.clougence.schema.metadata.MainVersion;
import com.clougence.schema.umi.special.rdb.RdbIndex;
import com.clougence.schema.umi.special.rdb.RdbTable;
import com.clougence.schema.umi.struts.UmiTypes;
import com.clougence.utils.StringUtils;

/**
 * @Author: mode
 * @Date: 2023-12-20 15:16
 */
public class GsMetaProviderUtils extends PgMetaProviderUtils {

    @Override
    public List<RdbTable> convertTable(ResultSet rs, MainVersion mainVersion) throws SQLException {
        List<RdbTable> rdbTables = new ArrayList<>();
        while (rs.next()) {
            RdbTable tab = new RdbTable();
            tab.setSchema(rs.getString("nspname"));
            tab.setName(rs.getString("relname"));
            String comment = rs.getString("table_comment");
            tab.setComment(comment == null ? "" : comment);

            String tableType = rs.getString("relkind");
            tab.setTableType(tableType);
            if (StringUtils.equalsIgnoreCase(tableType, "v")) {
                tab.setUmiType(UmiTypes.View);
            } else if (StringUtils.equalsIgnoreCase(tableType, "r") || StringUtils.equalsIgnoreCase(tableType, "p")) {
                tab.setUmiType(UmiTypes.Table);
            }

            tab.setAttribute(INHERITED_FROM, rs.getString("parent_table"));
            String createOptions = rs.getString("reloptions");
            tab.setAttribute(TABLESPACE, rs.getString("spcname"));
            if (StringUtils.isNotBlank(createOptions)) {
                //containAndSetOpt("fillfactor", createOptions, FILL_FACTOR, tab);
                //containAndSetOpt("appendonly", createOptions, APPEND_OPTIMIZED, tab);
                //containAndSetOpt("blocksize", createOptions, BLOCK_SIZE, tab);
                containAndSetOpt("orientation", createOptions, ORIENTATION, tab);
                containAndSetOpt("storage_type", createOptions, STORAGE_TYPE, tab);
                //containAndSetOpt("checksum", createOptions, CHECK_SUM, tab);
                //containAndSetOpt("compresstype", createOptions, COMPRESS_TYPE, tab);
                //containAndSetOpt("compresslevel", createOptions, COMPRESS_LEVEL, tab);
            } else {
                //tab.setAttribute(FILL_FACTOR, null);
                //tab.setAttribute(APPEND_OPTIMIZED, null);
                //tab.setAttribute(BLOCK_SIZE, null);
                tab.setAttribute(ORIENTATION, null);
                //tab.setAttribute(CHECK_SUM, null);
                tab.setAttribute(COMPRESS_TYPE, null);
                //tab.setAttribute(COMPRESS_LEVEL, null);
            }
            //tab.setAttribute(WITH_OIDS, String.valueOf(rs.getBoolean("relhasoids")));
            //GP special attribute
            //tab.setAttribute(DISTRIBUTED_TYPE, null);
            rdbTables.add(tab);
        }
        return rdbTables;
    }

    @Override
    protected List<RdbIndex> convertIndex(ResultSet rs) throws SQLException {
        return super.convertIndex(rs);
    }
}
