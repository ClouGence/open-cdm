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
package com.clougence.clouddm.ds.maxcompute.execute;

import static com.clougence.adapter.mc.McAttributeNames.*;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

import com.aliyun.maxcompute20220104.models.ListProjectUsersResponseBody;
import com.aliyun.maxcompute20220104.models.ListProjectsResponseBody;
import com.aliyun.maxcompute20220104.models.ListRolesResponseBody;
import com.aliyun.odps.*;
import com.aliyun.odps.type.TypeInfo;
import com.clougence.adapter.mc.McAttributeNames;
import com.clougence.adapter.mc.McSqlTypes;
import com.clougence.schema.metadata.FieldType;
import com.clougence.schema.umi.special.rdb.*;
import com.clougence.schema.umi.struts.UmiTypes;
import com.clougence.schema.umi.struts.Value;
import com.clougence.utils.CollectionUtils;
import com.clougence.utils.StringUtils;
import com.clougence.utils.format.WellKnowFormat;

import lombok.extern.slf4j.Slf4j;

/**
 * @author 赵永春 (zyc@hasor.net)
 * @version : 2021-04-29
 */
@Slf4j
public class McMetaProviderUtils {

    public static List<Value> convertCatalog(List<ListProjectsResponseBody.ListProjectsResponseBodyDataProjects> rs) {
        List<Value> result = new ArrayList<>();
        for (ListProjectsResponseBody.ListProjectsResponseBodyDataProjects p : rs) {
            RdbValue schema = new RdbValue();
            schema.setValue(p.getName());
            schema.setUmiType(UmiTypes.Catalog);
            result.add(schema);
        }
        return result;
    }

    public static List<Value> convertSchema(ResultSet rs) throws SQLException {
        List<Value> result = new ArrayList<>();
        if (rs.next()) {
            String info = rs.getString("Info");
            for (String item : info.split("\n")) {
                RdbValue schema = new RdbValue();
                schema.setValue(item);
                schema.setUmiType(UmiTypes.Schema);
                result.add(schema);
            }
        }
        return result;
    }

    public static List<Value> convertTableName(Iterable<Table> tables) {
        Iterator<Table> iterator = tables.iterator();
        List<Value> result = new ArrayList<>();
        while (iterator.hasNext()) {
            Table table = iterator.next();

            RdbValue def = new RdbValue();
            def.setValue(table.getName());

            switch (table.getType()) {
                case MANAGED_TABLE:
                    def.setUmiType(UmiTypes.Table);
                    break;
                case EXTERNAL_TABLE:
                    def.setUmiType(UmiTypes.ExternalTable);
                    break;
                case VIRTUAL_VIEW:
                    def.setUmiType(UmiTypes.View);
                    break;
                case MATERIALIZED_VIEW:
                    def.setUmiType(UmiTypes.Materialized);
                    break;
                default:
                    continue;
            }

            def.setAttribute(COMMENT, table.getComment());
            result.add(def);
        }
        return result;
    }

    public static List<Value> convertFuncName(Iterable<Function> funcIt) {
        Iterator<Function> iterator = funcIt.iterator();
        List<Value> result = new ArrayList<>();
        while (iterator.hasNext()) {
            Function func = iterator.next();

            RdbValue def = new RdbValue();
            def.setValue(func.getName());
            def.setUmiType(UmiTypes.Function);
            result.add(def);
        }
        return result;
    }

    public static List<Value> convertUsers(List<ListProjectUsersResponseBody.ListProjectUsersResponseBodyDataUsers> users) {
        List<Value> result = new ArrayList<>();
        for (ListProjectUsersResponseBody.ListProjectUsersResponseBodyDataUsers user : users) {
            RdbValue def = new RdbValue();
            def.setValue(user.getName());
            def.setUmiType(UmiTypes.USER);
            result.add(def);
        }
        return result;
    }

    public static List<Value> convertRoles(List<ListRolesResponseBody.ListRolesResponseBodyDataRoles> roles) {
        List<Value> result = new ArrayList<>();
        for (ListRolesResponseBody.ListRolesResponseBodyDataRoles role : roles) {
            RdbValue def = new RdbValue();
            def.setValue(role.getName());
            def.setUmiType(UmiTypes.ROLE);

            String type = role.getType();
            String policy = role.getPolicy();
            def.setAttribute(OBJ_UI_TIPS, "Type(" + type + ")");
            result.add(def);
        }
        return result;
    }

    public static RdbTable convertTableInfo(String catalog, String schema, Table table) {
        RdbTable def;
        switch (table.getType()) {
            case MANAGED_TABLE:
                def = new RdbTable();
                def.setUmiType(UmiTypes.Table);
                break;
            case EXTERNAL_TABLE:
                def = new RdbTable();
                def.setUmiType(UmiTypes.ExternalTable);
                break;
            case VIRTUAL_VIEW:
                def = new RdbView();
                def.setUmiType(UmiTypes.View);
                ((RdbView) def).setSql(table.getViewText());
                break;
            case MATERIALIZED_VIEW:
                def = new RdbView();
                def.setUmiType(UmiTypes.Materialized);
                ((RdbView) def).setSql(table.getViewText());
                break;
            default:
                return null;
        }

        // for tableInfo
        def.setCatalog(catalog);
        def.setSchema(schema);
        def.setName(table.getName());
        def.setComment(table.getComment());
        def.setAttribute(TABLE_ID, table.getTableID());
        def.setAttribute(TABLE_CRYPTO_ALGO_NAME, table.getCryptoAlgoName());
        def.setAttribute(TABLE_MAX_LABEL, table.getMaxLabel());
        def.setAttribute(LAST_META_MODIFIED, WellKnowFormat.WKF_DATE_TIME24.format(table.getLastMetaModifiedTime()));
        def.setAttribute(LAST_DATA_MODIFIED, WellKnowFormat.WKF_DATE_TIME24.format(table.getLastDataModifiedTime()));
        def.setAttribute(LAST_ACCESS_MODIFIED, WellKnowFormat.WKF_DATE_TIME24.format(table.getLastDataAccessTime()));
        def.setAttribute(DATA_BYTES, StringUtils.toString(table.getSize()));
        def.setAttribute(McAttributeNames.PHYSICAL_BYTES, StringUtils.toString(table.getPhysicalSize()));
        def.setAttribute(FILE_NUMBER, StringUtils.toString(table.getFileNum()));
        def.setAttribute(RECORD_NUM, StringUtils.toString(table.getRecordNum()));
        def.setAttribute(LIFE_DAY, StringUtils.toString(table.getLife()));
        def.setAttribute(ARCHIVED, StringUtils.toString(table.isArchived()));
        def.setAttribute(TABLE_LABEL, StringUtils.toString(table.getTableLabel()));
        def.setAttribute(OWNER, StringUtils.toString(table.getOwner()));

        // for partition
        List<Column> ptCols = table.getSchema().getPartitionColumns();
        if (CollectionUtils.isNotEmpty(ptCols)) {
            RdbPartition rdbPartition = new RdbPartition();

            // for partition defs
            rdbPartition.setPtDefs(new ArrayList<>());
            for (Column ptCol : ptCols) {
                RdbPartitionDef ptDef = new RdbPartitionDef();
                ptDef.setType(ptCol.getTypeInfo().getTypeName());
                ptDef.setName(ptCol.getName());
                rdbPartition.getPtDefs().add(ptDef);
            }

            // for partition items
            List<Partition> ptItems = table.getPartitions();
            rdbPartition.setPtItems(new ArrayList<>());
            if (ptItems != null) {
                for (Partition ptItem : ptItems) {
                    RdbPartitionItem item = new RdbPartitionItem();
                    item.setValueMap(new LinkedHashMap<>());

                    PartitionSpec spec = ptItem.getPartitionSpec();
                    item.setDescription(spec.toString());
                    for (String key : spec.keys()) {
                        item.getValueMap().put(key, spec.get(key));
                    }

                    item.setAttribute(McAttributeNames.CREATED_TIME, WellKnowFormat.WKF_DATE_TIME24.format(ptItem.getCreatedTime()));
                    item.setAttribute(McAttributeNames.LAST_META_MODIFIED, WellKnowFormat.WKF_DATE_TIME24.format(ptItem.getLastMetaModifiedTime()));
                    item.setAttribute(McAttributeNames.LAST_DATA_MODIFIED, WellKnowFormat.WKF_DATE_TIME24.format(ptItem.getLastDataModifiedTime()));
                    item.setAttribute(McAttributeNames.LAST_ACCESS_MODIFIED, WellKnowFormat.WKF_DATE_TIME24.format(ptItem.getLastDataAccessTime()));
                    item.setAttribute(McAttributeNames.DATA_BYTES, StringUtils.toString(ptItem.getSize()));
                    item.setAttribute(PHYSICAL_BYTES, StringUtils.toString(ptItem.getPhysicalSize()));
                    item.setAttribute(McAttributeNames.FILE_NUMBER, StringUtils.toString(ptItem.getFileNum()));
                    item.setAttribute(McAttributeNames.RECORD_NUM, StringUtils.toString(ptItem.getRecordNum()));
                    item.setAttribute(McAttributeNames.LIFE_DAY, StringUtils.toString(ptItem.getLifeCycle()));
                    item.setAttribute(McAttributeNames.ARCHIVED, StringUtils.toString(ptItem.isArchived()));
                }
            }
        }

        // for columns
        def.setColumns(new LinkedHashMap<>());
        List<Column> columns = table.getSchema().getColumns();
        if (columns != null) {
            int i = 0;
            for (Column c : columns) {
                RdbColumn column = new RdbColumn();
                column.setCatalog(catalog);
                column.setSchema(schema);
                column.setTable(table.getName());
                column.setName(c.getName());
                column.setIndex(i++);

                column.setDefaultValue(c.getDefaultValue());
                column.setDefaultValueIsFunc(false);
                column.setComment(c.getComment());

                column.setAttribute(COLUMN_TYPE, c.getTypeInfo().getTypeName());
                column.setSqlType(safeToMcTypes(c.getTypeInfo()));

                def.getColumns().put(column.getName(), column);
            }
        }

        return def;
    }

    private static FieldType safeToMcTypes(TypeInfo typeInfo) {
        if (typeInfo == null) {
            return null;
        }
        return McSqlTypes.toMcType(typeInfo.getTypeName());
    }

    public static RdbFunction convertFunctionInfo(String catalog, String schema, Function next) {
        RdbFunction rdbFunction = new RdbFunction();
        rdbFunction.setCatalog(catalog);
        rdbFunction.setSchema(schema);
        rdbFunction.setName(next.getName());
        rdbFunction.setSql(next.getSqlDefinitionText());

        rdbFunction.setAttribute(CREATED_TIME, WellKnowFormat.WKF_DATE_TIME24.format(next.getCreatedTime()));
        rdbFunction.setAttribute(CLASS_PATH, next.getClassPath());
        rdbFunction.setAttribute(OWNER, next.getOwner());
        List<String> resourceFullNames = next.getResourceFullNames();

        rdbFunction.setFeatures(Collections.emptyMap());

        return rdbFunction;
    }

    public static RdbUser convertUserInfo(ListProjectUsersResponseBody.ListProjectUsersResponseBodyDataUsers user) {
        RdbUser def = new RdbUser();
        def.setUsername(user.getName());
        return def;
    }

    public static RdbRole convertRoleInfo(ListRolesResponseBody.ListRolesResponseBodyDataRoles role) {
        RdbRole def = new RdbRole();
        def.setRoleName(role.getName());

        def.setAttribute(ROLE_POLICY, role.getPolicy());
        def.setAttribute(ROLE_TYPE, role.getType());
        //def.setAttribute(ROLE_ACL, role.getAcl());

        return def;
    }
}
