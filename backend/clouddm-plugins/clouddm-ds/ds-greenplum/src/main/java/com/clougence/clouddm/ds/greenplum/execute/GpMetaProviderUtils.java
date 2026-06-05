/*
 * Copyright 2026 杭州开云集致科技有限公司
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.clougence.clouddm.ds.greenplum.execute;

import static com.clougence.adapter.greenplum.GreenplumAttributeNames.*;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.clougence.clouddm.dsfamily.postgres.execute.PgMetaProviderUtils;
import com.clougence.schema.metadata.MainVersion;
import com.clougence.schema.umi.special.rdb.RdbIndex;
import com.clougence.schema.umi.special.rdb.RdbIndexType;
import com.clougence.schema.umi.special.rdb.RdbTable;
import com.clougence.schema.umi.struts.UmiTypes;
import com.clougence.utils.JsonUtils;
import com.clougence.utils.StringUtils;

/**
 * @Author: mode
 * @Date: 2023-12-20 15:16
 */
public class GpMetaProviderUtils extends PgMetaProviderUtils {

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
                containAndSetOpt("fillfactor", createOptions, FILL_FACTOR, tab);
                //containAndSetOpt("appendonly", createOptions, APPEND_OPTIMIZED, tab);
                //containAndSetOpt("blocksize", createOptions, BLOCK_SIZE, tab);
                containAndSetOpt("orientation", createOptions, ORIENTATION, tab);
                //containAndSetOpt("checksum", createOptions, CHECK_SUM, tab);
                containAndSetOpt("compresstype", createOptions, COMPRESS_TYPE, tab);
                //containAndSetOpt("compresslevel", createOptions, COMPRESS_LEVEL, tab);
            } else {
                tab.setAttribute(FILL_FACTOR, null);
                //tab.setAttribute(APPEND_OPTIMIZED, null);
                //tab.setAttribute(BLOCK_SIZE, null);
                tab.setAttribute(ORIENTATION, null);
                //tab.setAttribute(CHECK_SUM, null);
                tab.setAttribute(COMPRESS_TYPE, null);
                //tab.setAttribute(COMPRESS_LEVEL, null);
            }
            tab.setAttribute(WITH_OIDS, String.valueOf(rs.getBoolean("relhasoids")));
            //GP special attribute
            tab.setAttribute(DISTRIBUTED_TYPE, null);
            //            String relType = rs.getString("relpersistence");
            //            String policytype = rs.getString("policytype");
            //            String dkCols = rs.getString("distkey_columns");
            //            if ("r".equals(policytype)) {
            //                tab.setAttribute(DISTRIBUTED_TYPE, "RANDOMLY");
            //            } else {
            //                if (StringUtils.isBlank(dkCols)) {
            //                    tab.setAttribute(DISTRIBUTED_TYPE, "REPLICATED");
            //                } else {
            //                    tab.setAttribute(DISTRIBUTED_TYPE, policytype);
            //                    //tab.setAttribute(DISTRIBUTED_COLUMN, distkeyColumns);
            //                    //                    rs.getString("distclass_opcname"); //don't know how to display it at the moment
            //                }
            //            }
            rdbTables.add(tab);
        }
        return rdbTables;
    }

    @Override
    protected List<RdbIndex> convertIndex(ResultSet rs) throws SQLException {
        List<RdbIndex> indexList = new ArrayList<>();
        while (rs.next()) {
            RdbIndex idx = new RdbIndex();
            idx.setSchema(rs.getString("table_schema"));
            idx.setTable(rs.getString("TABLE_NAME"));
            idx.setName(rs.getString("index_name"));

            String indexType = rs.getString("am_name");
            if (rs.getBoolean("isunique")) {
                idx.setAttribute(INDEX_WAY, "Unique");
                idx.setType(RdbIndexType.Unique);
            } else {
                idx.setAttribute(INDEX_WAY, "Normal");
                idx.setType(RdbIndexType.Normal);
            }
            idx.setAttribute(INDEX_TYPE, indexType);
            idx.setComment(rs.getString("description"));

            //column attribute
            String columnName = rs.getString("column_name");
            HashMap<String, String> subPartMap = new HashMap<>();
            if (columnName.contains("substring")) {
                String subPart = null;
                String[] split = columnName.split(",");
                if (split.length == 3) {
                    subPart = split[2].replace(")", "");
                }
                int index = columnName.lastIndexOf("(");
                int index2 = columnName.indexOf(")");
                if (index < index2) {
                    columnName = columnName.substring(index + 1, index2);
                }
                subPartMap.put(columnName, subPart);
            } else {
                subPartMap.put(columnName, null);
            }
            idx.addColumn(columnName);
            String newSubPartJson = JsonUtils.toJson(subPartMap);
            idx.setAttribute(INDEX_PREFIX_LENGTH, newSubPartJson);

            //order
            HashMap<String, String> orderMap = new HashMap<>();
            String ascOrDesc = rs.getString("asc_or_desc");
            if (StringUtils.isBlank(ascOrDesc)) {
                ascOrDesc = "";
            }
            orderMap.put(columnName, ascOrDesc);
            String newOrderMapJson = JsonUtils.toJson(orderMap);
            idx.setAttribute(INDEX_SORT_ORDER, newOrderMapJson);

            //sort rules
            String collationName = rs.getString("COLLATION_NAME");
            String collationSchemaName = rs.getString("collation_schema_name");
            HashMap<String, String> sortRulesMap = new HashMap<>();
            if (collationName != null && collationSchemaName != null) {
                sortRulesMap.put(columnName, collationSchemaName + "." + collationName);
            } else {
                sortRulesMap.put(columnName, null);
            }
            String newSortRulesJson = JsonUtils.toJson(sortRulesMap);
            idx.setAttribute(INDEX_SORT_RULES, newSortRulesJson);

            //Operator Category
            String opcname = rs.getString("opcname");
            String opcnameSchemaName = rs.getString("opcname_schema_name");
            HashMap<String, String> opcnameMap = new HashMap<>();
            if (opcname != null && opcnameSchemaName != null) {
                opcnameMap.put(columnName, opcnameSchemaName + "." + opcname);
            } else {
                opcnameMap.put(columnName, null);
            }
            String newOpcNameJson = JsonUtils.toJson(opcnameMap);
            idx.setAttribute(INDEX_OPERATOR_TYPE, newOpcNameJson);

            //nulls sort
            HashMap<String, String> nullsFirstMap = new HashMap<>();
            nullsFirstMap.put(columnName, rs.getString("nullsfirst"));
            String newNullsFirstJson = JsonUtils.toJson(nullsFirstMap);
            idx.setAttribute(INDEX_NULLS_SORT, newNullsFirstJson);

            //with option
            String reloptions = rs.getString("reloptions");
            if (StringUtils.isNotBlank(reloptions)) {
                containAndSetOpt("fillfactor", reloptions, INDEX_WITH_FILLFACTOR, idx);
                containAndSetOpt("buffering", reloptions, INDEX_WITH_BUFFERING, idx);
                containAndSetOpt("fastupdate", reloptions, INDEX_WITH_FASTUPDATE, idx);
            } else {
                idx.setAttribute(INDEX_WITH_FILLFACTOR, null);
                idx.setAttribute(INDEX_WITH_BUFFERING, null);
                idx.setAttribute(INDEX_WITH_FASTUPDATE, null);
            }
            //tablespace
            idx.setAttribute(TABLESPACE, rs.getString("tablespace"));
            //where predicate
            idx.setAttribute(INDEX_WHERE, rs.getString("predicate"));
            indexList.add(idx);
        }
        return indexList;
    }
}
