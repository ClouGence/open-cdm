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

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import java.util.stream.Collectors;

import com.aliyun.maxcompute20220104.Client;
import com.aliyun.maxcompute20220104.models.*;
import com.aliyun.odps.*;
import com.aliyun.odps.jdbc.OdpsConnection;
import com.aliyun.odps.jdbc.OdpsDatabaseMetaData;
import com.aliyun.tea.TeaException;
import com.clougence.clouddm.ds.maxcompute.dsconf.McConfig;
import com.clougence.clouddm.dsfamily.execute.AbstractMetadataProvider;
import com.clougence.schema.metadata.MetaDataService;
import com.clougence.schema.umi.special.rdb.*;
import com.clougence.schema.umi.struts.UmiConstraint;
import com.clougence.schema.umi.struts.UmiTypes;
import com.clougence.schema.umi.struts.Value;
import com.clougence.utils.CollectionUtils;
import com.clougence.utils.StringUtils;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

/**
 * MaxCompute：
 * <li>https://help.aliyun.com/zh/maxcompute/user-guide/schema-related-operations</li>
 * <li>https://help.aliyun.com/zh/maxcompute/user-guide/show-1</li>
 *
 * @author 赵永春 (zyc@hasor.net)
 * @version : 2021-04-29
 */
@Slf4j
public class McMetaProviderDm extends AbstractMetadataProvider implements MetaDataService {

    private final Client   sdkClient;
    private final McConfig dsConfig;

    @SneakyThrows
    public McMetaProviderDm(Connection connection, McConfig dsConfig){
        super(connection);
        if (StringUtils.isBlank(dsConfig.getSdkEndpoint())) {
            throw new SQLException("SDK Endpoint is not config.");
        }

        this.dsConfig = dsConfig;
        this.sdkClient = createClient(connection, dsConfig);
    }

    private Client createClient(Connection info, McConfig dsConfig) throws Exception {
        com.aliyun.credentials.Client credential = new com.aliyun.credentials.Client();
        com.aliyun.teaopenapi.models.Config config = new com.aliyun.teaopenapi.models.Config().setCredential(credential);
        config.endpoint = dsConfig.getSdkEndpoint();
        config.accessKeyId = info.getClientInfo("access_id");
        config.accessKeySecret = info.getClientInfo("access_key");
        return new Client(config);
    }

    @Override
    protected int defaultGroupSize() {
        return 1;
    }

    @Override
    public String getVersion() throws SQLException {
        try (Connection conn = this.connectSupplier.eGet()) {
            OdpsDatabaseMetaData dbMeta = (OdpsDatabaseMetaData) conn.getMetaData();
            int majorVersion = dbMeta.getDatabaseMajorVersion();
            int minorVersion = dbMeta.getDatabaseMinorVersion();
            return majorVersion + "." + minorVersion;
        }
    }

    public boolean defensiveCheck(String catalog) throws SQLException {
        try (Connection conn = this.connectSupplier.eGet()) {
            String currentCatalog = conn.unwrap(OdpsConnection.class).getOdps().getDefaultProject();
            return StringUtils.equalsIgnoreCase(catalog, currentCatalog);
        }
    }

    public List<Value> selectCatalogs() throws SQLException {
        try {
            com.aliyun.maxcompute20220104.models.ListProjectsRequest listProjectsRequest = new com.aliyun.maxcompute20220104.models.ListProjectsRequest();
            com.aliyun.teautil.models.RuntimeOptions runtime = new com.aliyun.teautil.models.RuntimeOptions();
            java.util.Map<String, String> headers = new java.util.HashMap<>();
            ListProjectsResponse projects = this.sdkClient.listProjectsWithOptions(listProjectsRequest, headers, runtime);

            if (projects.getBody() != null) {
                return McMetaProviderUtils.convertCatalog(projects.body.data.projects);
            } else {
                return Collections.emptyList();
            }
        } catch (TeaException error) {
            throw new SQLException(error.message);
        } catch (Exception _error) {
            throw new SQLException(_error.getMessage());
        }
    }

    public Value selectCatalog(String catalog) throws SQLException {
        if (this.defensiveCheck(catalog)) {
            RdbValue def = new RdbValue();
            def.setValue(catalog);
            def.setUmiType(UmiTypes.Catalog);
            return def;
        } else {
            List<Value> values = selectCatalogs();
            if (CollectionUtils.isEmpty(values)) {
                return null;
            } else {
                return values.stream().filter(v -> StringUtils.equals(v.asValue(), catalog)).findAny().orElse(null);
            }
        }
    }

    public List<Value> selectSchemas(String catalog) throws SQLException {
        if (!this.defensiveCheck(catalog)) {
            return Collections.emptyList();
        }

        if (!Boolean.TRUE.equals(this.dsConfig.getSchemaStyle())) {
            RdbValue def = new RdbValue();
            def.setUmiType(UmiTypes.Schema);
            def.setValue("default");
            return Collections.singletonList(def);
        } else {
            String sql = "show schemas";
            try (Connection conn = this.connectSupplier.eGet(); PreparedStatement ps = conn.prepareStatement(sql)) {
                try (ResultSet rs = ps.executeQuery()) {
                    return McMetaProviderUtils.convertSchema(rs);
                }
            } catch (SQLException e) {
                if (e.getMessage().contains("Invalid database operations on two-tier model")) {
                    RdbValue def = new RdbValue();
                    def.setUmiType(UmiTypes.Schema);
                    def.setValue("default");
                    return Collections.singletonList(def);
                } else {
                    throw new RuntimeException(e);
                }
            }
        }
    }

    public Value selectSchema(String catalog, String schema) throws SQLException {
        if (!this.defensiveCheck(catalog)) {
            return null;
        }

        if (StringUtils.equals(schema, "default")) {
            RdbValue def = new RdbValue();
            def.setUmiType(UmiTypes.Schema);
            def.setValue("default");
            return def;
        }

        String sql = "show schemas";
        try (Connection conn = this.connectSupplier.eGet(); PreparedStatement ps = conn.prepareStatement(sql)) {
            try (ResultSet rs = ps.executeQuery()) {
                List<Value> values = McMetaProviderUtils.convertSchema(rs);
                return values.stream().filter(v -> StringUtils.equals(v.asValue(), schema)).findAny().orElse(null);
            }
        } catch (SQLException e) {
            log.error(e.getMessage(), e);
            return null;
        }
    }

    public List<Value> selectTables(String catalog, String schema) throws SQLException {
        return this.selectTableOrView(catalog, schema, Table.TableType.MANAGED_TABLE);
    }

    public List<Value> selectExternalTable(String catalog, String schema) throws SQLException {
        return this.selectTableOrView(catalog, schema, Table.TableType.EXTERNAL_TABLE);
    }

    public List<Value> selectViews(String catalog, String schema) throws SQLException {
        return this.selectTableOrView(catalog, schema, Table.TableType.VIRTUAL_VIEW);
    }

    public List<Value> selectMaterializedView(String catalog, String schema) throws SQLException {
        return this.selectTableOrView(catalog, schema, Table.TableType.MATERIALIZED_VIEW);
    }

    private List<Value> selectTableOrView(String catalog, String schema, Table.TableType tableType) throws SQLException {
        if (!this.defensiveCheck(catalog)) {
            return Collections.emptyList();
        }

        try (Connection conn = this.connectSupplier.eGet()) {
            Tables tables = conn.unwrap(OdpsConnection.class).getOdps().tables();

            TableFilter filter = new TableFilter();
            filter.setType(tableType);
            Iterable<Table> tabIt = tables.iterable(catalog, schema, filter, true);
            return McMetaProviderUtils.convertTableName(tabIt);
        }
    }

    public List<Value> selectFunctions(String catalog, String schema) throws SQLException {
        if (!this.defensiveCheck(catalog)) {
            return Collections.emptyList();
        }

        try (Connection conn = this.connectSupplier.eGet()) {
            Functions functions = conn.unwrap(OdpsConnection.class).getOdps().functions();
            Iterable<Function> funcIt = functions.iterable(catalog, schema);
            return McMetaProviderUtils.convertFuncName(funcIt);
        }
    }

    public List<Value> selectUsers(String catalog, String schema) throws SQLException {
        if (!this.defensiveCheck(catalog)) {
            return Collections.emptyList();
        }

        try {
            ListProjectUsersResponse userList = this.sdkClient.listProjectUsers(catalog);

            if (userList.getBody() != null) {
                return McMetaProviderUtils.convertUsers(userList.body.data.users);
            } else {
                return Collections.emptyList();
            }
        } catch (TeaException error) {
            throw new SQLException(error.message);
        } catch (Exception _error) {
            throw new SQLException(_error.getMessage());
        }
    }

    public List<Value> selectRoles(String catalog, String schema) throws SQLException {
        if (!this.defensiveCheck(catalog)) {
            return Collections.emptyList();
        }

        try {
            ListRolesResponse roleList = this.sdkClient.listRoles(catalog);

            if (roleList.getBody() != null) {
                return McMetaProviderUtils.convertRoles(roleList.body.data.roles);
            } else {
                return Collections.emptyList();
            }
        } catch (TeaException error) {
            throw new SQLException(error.message);
        } catch (Exception _error) {
            throw new SQLException(_error.getMessage());
        }
    }

    @Override
    public List<RdbTable> loadTables(String catalog, String schema, List<String> tables) throws SQLException {
        if (tables.size() > 1) {
            throw new IndexOutOfBoundsException("fetch tables size need 1");
        }
        if (CollectionUtils.isEmpty(tables)) {
            return Collections.emptyList();
        }

        return this.loadTableOrView(catalog, schema, tables.get(0), Table.TableType.MANAGED_TABLE);
    }

    @Override
    public List<RdbTable> loadViews(String catalog, String schema, List<String> views) throws SQLException {
        if (views.size() > 1) {
            throw new IndexOutOfBoundsException("fetch views size need 1");
        }
        if (CollectionUtils.isEmpty(views)) {
            return Collections.emptyList();
        }

        return this.loadTableOrView(catalog, schema, views.get(0), Table.TableType.VIRTUAL_VIEW);
    }

    @Override
    public List<RdbTable> loadMaterialized(String catalog, String schema, List<String> views) throws SQLException {
        if (views.size() > 1) {
            throw new IndexOutOfBoundsException("fetch materialized views size need 1");
        }
        if (CollectionUtils.isEmpty(views)) {
            return Collections.emptyList();
        }

        return this.loadTableOrView(catalog, schema, views.get(0), Table.TableType.MATERIALIZED_VIEW);
    }

    public List<RdbTable> loadExternalTable(String catalog, String schema, List<String> extTable) throws SQLException {
        if (extTable.size() > 1) {
            throw new IndexOutOfBoundsException("fetch externalTable size need 1");
        }
        if (CollectionUtils.isEmpty(extTable)) {
            return Collections.emptyList();
        }

        return this.loadTableOrView(catalog, schema, extTable.get(0), Table.TableType.EXTERNAL_TABLE);
    }

    private List<RdbTable> loadTableOrView(String catalog, String schema, String table, Table.TableType tableType) throws SQLException {
        if (!this.defensiveCheck(catalog)) {
            return Collections.emptyList();
        }

        try (Connection conn = this.connectSupplier.eGet()) {
            Tables odpsTables = conn.unwrap(OdpsConnection.class).getOdps().tables();
            TableFilter filter = new TableFilter();
            filter.setName(table);
            filter.setType(tableType);
            Iterable<Table> iterable = odpsTables.iterable(catalog, schema, filter, true);
            for (Table next : iterable) {
                if (StringUtils.equals(next.getName(), table)) {
                    next.reload();
                    RdbTable rdbTab = McMetaProviderUtils.convertTableInfo(catalog, schema, next);
                    return Collections.singletonList(rdbTab);
                }
            }
            return Collections.emptyList();
        } catch (TeaException error) {
            throw new SQLException(error.message);
        } catch (Exception _error) {
            throw new SQLException(_error.getMessage());
        }
    }

    public List<RdbFunction> loadFunctions(String catalog, String schema, List<String> functionNames) throws SQLException {
        if (functionNames.size() > 1) {
            throw new IndexOutOfBoundsException("fetch materialized views size need 1");
        }
        if (CollectionUtils.isEmpty(functionNames)) {
            return Collections.emptyList();
        }
        if (!this.defensiveCheck(catalog)) {
            return Collections.emptyList();
        }

        try (Connection conn = this.connectSupplier.eGet()) {
            Functions functions = conn.unwrap(OdpsConnection.class).getOdps().functions();
            Iterable<Function> funcIt = functions.iterable(catalog, schema, functionNames.get(0));
            Iterator<Function> it = funcIt.iterator();
            if (it.hasNext()) {
                Function next = it.next();
                next.reload();

                RdbFunction func = McMetaProviderUtils.convertFunctionInfo(catalog, schema, next);
                return Collections.singletonList(func);
            }

            return Collections.emptyList();
        } catch (TeaException error) {
            throw new SQLException(error.message);
        } catch (Exception _error) {
            throw new SQLException(_error.getMessage());
        }
    }

    public List<RdbUser> loadUsers(String catalog, String schema, List<String> userNames) throws SQLException {
        if (!this.defensiveCheck(catalog)) {
            return Collections.emptyList();
        }

        try {
            ListProjectUsersResponse userList = this.sdkClient.listProjectUsers(catalog);

            if (userList.getBody() != null) {
                List<ListProjectUsersResponseBody.ListProjectUsersResponseBodyDataUsers> users = //
                        userList.body.data.getUsers().stream().filter(u -> userNames.contains(u.getName())).collect(Collectors.toList());
                List<RdbUser> result = new ArrayList<>();
                for (ListProjectUsersResponseBody.ListProjectUsersResponseBodyDataUsers user : users) {
                    result.add(McMetaProviderUtils.convertUserInfo(user));
                }
                return result;
            } else {
                return Collections.emptyList();
            }
        } catch (TeaException error) {
            throw new SQLException(error.message);
        } catch (Exception _error) {
            throw new SQLException(_error.getMessage());
        }
    }

    public List<RdbRole> loadRoles(String catalog, String schema, List<String> roleNames) throws SQLException {
        if (!this.defensiveCheck(catalog)) {
            return Collections.emptyList();
        }

        try {
            ListRolesResponse roleList = this.sdkClient.listRoles(catalog);

            if (roleList.getBody() != null) {
                List<ListRolesResponseBody.ListRolesResponseBodyDataRoles> roles = //
                        roleList.body.data.getRoles().stream().filter(r -> roleNames.contains(r.getName())).collect(Collectors.toList());
                List<RdbRole> result = new ArrayList<>();
                for (ListRolesResponseBody.ListRolesResponseBodyDataRoles role : roles) {
                    result.add(McMetaProviderUtils.convertRoleInfo(role));
                }
                return result;

            } else {
                return Collections.emptyList();
            }
        } catch (TeaException error) {
            throw new SQLException(error.message);
        } catch (Exception _error) {
            throw new SQLException(_error.getMessage());
        }
    }

    // MaxCompute is a special.

    @Override
    protected List<RdbTable> fetchTableByPart(Connection conn, String catalog, String schema, List<String> tabs) {
        return Collections.emptyList();
    }

    @Override
    protected List<RdbTable> fetchViewByPart(Connection conn, String catalog, String schema, List<String> tabs) {
        return Collections.emptyList();
    }

    @Override
    protected List<RdbTable> fetchMaterializedByPart(Connection conn, String catalog, String schema, List<String> tabs) {
        return Collections.emptyList();
    }

    @Override
    protected Map<String, List<RdbIndex>> fetchIndexes(Connection conn, String catalog, String schema, List<String> tabs) {
        return Collections.emptyMap();
    }

    @Override
    protected Map<String, List<RdbForeignKey>> fetchForeignKeys(Connection conn, String catalog, String schema, List<String> tabs) {
        return Collections.emptyMap();
    }

    @Override
    protected Map<String, Map<String, UmiConstraint>> fetchPrimaryUnique(Connection conn, String catalog, String schema, List<String> tabs) {
        return Collections.emptyMap();
    }

    @Override
    protected Map<String, List<RdbColumn>> fetchTableColumns(Connection conn, String catalog, String schema, List<String> tabs) {
        return Collections.emptyMap();
    }
}
