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

package com.clougence.clouddm.console.web.service.sdk;

import static com.clougence.clouddm.sdk.openapi.OpenApiErrorCode.*;
import static com.clougence.clouddm.sdk.security.auth.def.SecDataAuthLabel.RDP_DAUTH_DS_MANAGER;
import static com.clougence.clouddm.sdk.security.auth.def.SecDataAuthLabel.RDP_DAUTH_DS_READ;
import static com.clougence.clouddm.sdk.security.auth.def.SecRoleAuthLabel.*;

import java.net.URI;
import java.util.*;
import java.util.function.Supplier;

import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.clougence.clouddm.api.common.exception.DmErrorCode;
import com.clougence.clouddm.api.common.exception.ErrorMessageException;
import com.clougence.clouddm.api.common.rpc.ResWebData;
import com.clougence.clouddm.api.sidecar.session.drivers.DriverUtils;
import com.clougence.clouddm.base.metadata.ds.DataSourceConfig.Fields;
import com.clougence.clouddm.base.metadata.ds.DataSourceType;
import com.clougence.clouddm.base.metadata.ds.SecurityType;
import com.clougence.clouddm.console.web.component.auth.DmAuthServiceForBiz;
import com.clougence.clouddm.console.web.component.auth.DmAuthServiceForManage;
import com.clougence.clouddm.console.web.component.dsconfig.DmDriverService;
import com.clougence.clouddm.console.web.component.dsconfig.DmDsConfigService;
import com.clougence.clouddm.console.web.component.dsconfig.DmDsService;
import com.clougence.clouddm.console.web.component.dsconfig.impl.DmDsConfigUiDataFactory;
import com.clougence.clouddm.console.web.model.fo.datasource.DsConfigSubmitFO;
import com.clougence.clouddm.console.web.model.vo.cluster.ClusterVO;
import com.clougence.clouddm.console.web.service.cicd.DmChangeFlowService;
import com.clougence.clouddm.console.web.service.cluster.ClusterService;
import com.clougence.clouddm.console.web.service.datasource.DmDsWebService;
import com.clougence.clouddm.console.web.util.RdpAuthUtils;
import com.clougence.clouddm.platform.dal.access.ApprovalDal;
import com.clougence.clouddm.platform.dal.access.AuthDal;
import com.clougence.clouddm.platform.dal.access.DataSourceDal;
import com.clougence.clouddm.platform.dal.access.SystemDal;
import com.clougence.clouddm.platform.dal.model.LifeCycleState;
import com.clougence.clouddm.platform.dal.model.ResourceType;
import com.clougence.clouddm.platform.dal.model.approval.ApprovalStatus;
import com.clougence.clouddm.platform.dal.model.auth.DmAuthResDO;
import com.clougence.clouddm.platform.dal.model.datasource.ArgDsQueryParamObj;
import com.clougence.clouddm.platform.dal.model.datasource.DmDsDO;
import com.clougence.clouddm.platform.dal.model.monitor.AuditType;
import com.clougence.clouddm.platform.dal.model.monitor.SecurityLevel;
import com.clougence.clouddm.platform.dal.model.system.DmSysClusterDO;
import com.clougence.clouddm.platform.dal.model.system.DmSysEnvDO;
import com.clougence.clouddm.platform.plugin.PluginManager;
import com.clougence.clouddm.sdk.execute.dsconf.DsConfigSpi;
import com.clougence.clouddm.sdk.openapi.OpenApiCaller;
import com.clougence.clouddm.sdk.openapi.OpenApiException;
import com.clougence.clouddm.sdk.openapi.datasource.*;
import com.clougence.clouddm.sdk.security.auth.AuthKind;
import com.clougence.clouddm.sdk.ui.browser.DsBrowseSpi;
import com.clougence.rdp.service.RdpDsEnvService;
import com.clougence.rdp.service.RdpOpAuditService;
import com.clougence.schema.umi.struts.UmiTypes;
import com.clougence.utils.HostAndPort;
import com.clougence.utils.StringUtils;

import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;

@Service
public class ConsoleOpenApiDataSourceService implements OpenApiDataSourceService {

    private static final List<String> TERMINAL_TICKET_STATUSES = Arrays.stream(ApprovalStatus.values()).filter(ApprovalStatus::isEndStatus).map(Enum::name).toList();

    @Resource
    private DmAuthServiceForBiz       authorization;
    @Resource
    private DmAuthServiceForManage    resourceAuthorization;
    @Resource
    private AuthDal                   authDal;
    @Resource
    private SystemDal                 systemDal;
    @Resource
    private DataSourceDal             dataSourceDal;
    @Resource
    private ApprovalDal               approvalDal;
    @Resource
    private RdpDsEnvService           envService;
    @Resource
    private ClusterService            clusterService;
    @Resource
    private DmDsConfigService         configService;
    @Resource
    private DmDsConfigUiDataFactory   uiDataFactory;
    @Resource
    private DmDriverService           driverService;
    @Resource
    private DmDsWebService            dataSourceService;
    @Resource
    private DmDsService               connectionService;
    @Resource
    private DmChangeFlowService       changeFlowService;
    @Resource
    private RdpOpAuditService         auditService;

    @Override
    public List<OpenApiEnvironment> listEnvironments(OpenApiCaller caller) {
        this.requireBindingRole(caller);
        return this.envService.listDsEnv(caller.puid(), caller.uid(), null)
            .stream()
            .sorted(Comparator.comparing(DmSysEnvDO::getId))
            .map(env -> new OpenApiEnvironment(env.getId(), env.getEnvName()))
            .toList();
    }

    @Override
    public List<OpenApiCluster> listClusters(OpenApiCaller caller) {
        this.requireBindingRole(caller);
        return this.clusterService.listByOwnerUid(caller.puid())
            .stream()
            .sorted(Comparator.comparing(ClusterVO::getId))
            .map(cluster -> new OpenApiCluster(cluster.getId(), cluster.getClusterName(), cluster.getClusterDesc(), cluster.getRunningCount()))
            .toList();
    }

    @Override
    public List<OpenApiDataSourceType> listDataSourceTypes(OpenApiCaller caller) {
        if (!this.hasRole(caller, DM_DS_READ) && !this.hasRole(caller, RDP_DS_MANAGE)) {
            throw new OpenApiException(FORBIDDEN);
        }
        return Arrays.stream(DataSourceType.values())
            .filter(type -> this.configService.dsConstantSettings(type) != null)
            .sorted(Comparator.comparing(DataSourceType::getTypeName))
            .map(this::describeType)
            .toList();
    }

    private OpenApiDataSourceType describeType(DataSourceType type) {
        List<String> operations = new ArrayList<>(List.of("getDataSourceConfigSpec", "listDataSources", "getDataSource", "updateDataSource", "deleteDataSource"));
        if (supportsRegistration(type)) {
            operations.add("createDataSource");
        }
        if (PluginManager.findSessionSpi(type) != null) {
            operations.add("testConnection");
        }
        DsBrowseSpi browse = PluginManager.findDsBrowseSpi(type);
        List<String> levels = List.of();
        if (browse != null) {
            levels = browse.getLevels().stream().map(UmiTypes::getTypeName).toList();
        }
        return new OpenApiDataSourceType(type.getTypeName(), operations, levels);
    }

    @Override
    public OpenApiDataSourceConfigSpec getDataSourceConfigSpec(OpenApiCaller caller, DataSourceType type, Long clusterId) {
        this.requireBindingRole(caller);
        this.requireBindings(caller, null, clusterId);
        DsConfigSpi config = this.requireInstalledType(type);
        OpenApiDataSourceDriver driver = null;
        String driverSpec = this.uiDataFactory.defaultDriverSpec(type);
        if (driverSpec != null) {
            var ref = DriverUtils.parseDriverRef(driverSpec);
            Boolean available = null;
            if (clusterId != null) {
                available = this.driverService.checkDriverStatus(clusterId, ref.getDriverFamily(), ref.getDriverVersion()).isAvailable();
            }
            driver = new OpenApiDataSourceDriver(ref.getDriverFamily(), ref.getDriverVersion(), available);
        }
        if (!supportsRegistration(type)) {
            return new OpenApiDataSourceConfigSpec(type.getTypeName(), false, List.of(), List.of(), null, null, List.of(), Map.of(), driver);
        }
        return new OpenApiDataSourceConfigSpec(type.getTypeName(),
            true,
            List.of("host", "port", "securityType", "userName", "password"),
            List.of("host"),
            Integer.valueOf(config.defaultPort()),
            SecurityType.USER_PASSWD,
            config.securityTypes(),
            Map.of(SecurityType.ONLY_USER, List.of("userName"), SecurityType.USER_PASSWD, List.of("userName", "password")),
            driver);
    }

    @Override
    public List<OpenApiDataSourceInfo> listDataSources(OpenApiCaller caller, Long envId, DataSourceType type) {
        this.requireRole(caller, DM_DS_READ);
        List<Long> dataSourceIds = null;
        if (!this.resourceAuthorization.hasGlobalAuth(caller.uid(), AuthKind.DataSource, RDP_DAUTH_DS_READ)) {
            dataSourceIds = this.authDal.resMapper()
                .listByKind(caller.uid(), AuthKind.DataSource)
                .stream()
                .filter(grant -> "/".equals(grant.getResPath()) && grant.isEffective() && grant.getAuthLabels() != null && grant.getAuthLabels().contains(RDP_DAUTH_DS_READ))
                .map(DmAuthResDO::getResId)
                .distinct()
                .toList();
            if (dataSourceIds.isEmpty()) {
                return List.of();
            }
        }
        ArgDsQueryParamObj query = ArgDsQueryParamObj.builder().ownerUid(caller.puid()).envId(envId).dataSourceType(type).dataSourceIds(dataSourceIds).build();
        return this.dataSourceService.fetchByCondition(query).stream().sorted(Comparator.comparing(DmDsDO::getId)).map(ds -> toInfo(ds, null)).toList();
    }

    @Override
    public OpenApiDataSourceInfo getDataSource(OpenApiCaller caller, long dataSourceId) {
        this.requireRole(caller, DM_DS_READ);
        DmDsDO ds = this.requireLiveDataSource(caller, dataSourceId);
        this.requireResource(caller, dataSourceId, RDP_DAUTH_DS_READ);
        String address = null;
        if (this.hasResource(caller, dataSourceId, RDP_DAUTH_DS_MANAGER)) {
            try {
                address = endpoint(ds.getHost()).toString();
            } catch (IllegalArgumentException ignored) {
                // The summary exposes standalone addresses. Connection strings can contain credentials.
            }
        }
        return toInfo(ds, address);
    }

    @Override
    public OpenApiConnectionTest testConnection(OpenApiCaller caller, long dataSourceId) {
        this.requireBindingRole(caller);
        DmDsDO ds = this.requireLiveDataSource(caller, dataSourceId);
        this.requireResource(caller, dataSourceId, RDP_DAUTH_DS_MANAGER);
        this.requireWritable(ds);
        try {
            return new OpenApiConnectionTest(true, this.connectionService.testConnect(dataSourceId), null);
        } catch (ErrorMessageException e) {
            String reason = "CONNECTION_FAILED";
            if (DmErrorCode.CLUSTER_HAVE_NO_WORKS_ERROR.code().equals(e.getErrorCode())) {
                reason = "CLUSTER_UNAVAILABLE";
            }
            return new OpenApiConnectionTest(false, null, reason);
        }
    }

    @Override
    public Supplier<OpenApiDataSourceInfo> prepareCreateDataSource(OpenApiCaller caller, DataSourceType type, OpenApiDataSourceConfig input) {
        this.requireRole(caller, RDP_DS_MANAGE);
        this.requireBindings(caller, input.getEnvId(), input.getClusterId());
        return () -> {
            if (!supportsRegistration(type)) {
                throw new OpenApiException(INVALID_ARGUMENT);
            }
            DsConfigSpi config = this.requireInstalledType(type);
            var driverSpec = this.uiDataFactory.defaultDriverSpec(type);
            if (driverSpec == null) {
                throw new OpenApiException(STATE_CONFLICT);
            }
            var driver = DriverUtils.parseDriverRef(driverSpec);
            Map<String, String> settings = new LinkedHashMap<>();
            settings.put("driverFamily", driver.getDriverFamily());
            settings.put(Fields.driverVersion, driver.getDriverVersion());
            applyConnection(settings, input, null, config);

            DsConfigSubmitFO form = new DsConfigSubmitFO();
            form.setDsType(type);
            form.setClusterId(input.getClusterId());
            form.setEnvId(input.getEnvId());
            form.setInstanceDesc(input.getInstanceDesc());
            form.setConfigMap(settings);
            long id = requireSuccess(this.dataSourceService.addDs(caller.puid(), caller.uid(), form)).getData();
            DmDsDO created = this.dataSourceService.queryById(id);
            this.audit(caller, created, AuditType.ADD_DATA_SOURCE);
            return toInfo(created, null);
        };
    }

    @Override
    public Runnable prepareUpdateDataSource(OpenApiCaller caller, long dataSourceId, OpenApiDataSourceConfig input) {
        this.requireRole(caller, RDP_DS_MANAGE);
        DmDsDO ds = this.lockForManagement(caller, dataSourceId);
        this.requireBindings(caller, input.getEnvId(), input.getClusterId());
        return () -> {
            this.requireWritable(ds);
            boolean connectionChanged = input.getHost() != null || input.getPort() != null || input.getSecurityType() != null || input.getUserName() != null
                                        || input.getPassword() != null;
            if (!connectionChanged && input.getEnvId() == null && input.getClusterId() == null) {
                this.dataSourceService.updateDataSourceDesc(caller.puid(), dataSourceId, input.getInstanceDesc());
                this.audit(caller, ds, AuditType.UPDATE_DATA_SOURCE_DESC);
                return;
            }

            Map<String, String> settings = new LinkedHashMap<>();
            settings.put(Fields.instanceId, ds.getInstanceId());
            if (connectionChanged) {
                if (!supportsRegistration(ds.getDataSourceType())) {
                    throw new OpenApiException(INVALID_ARGUMENT);
                }
                applyConnection(settings, input, ds, this.requireInstalledType(ds.getDataSourceType()));
            }
            DsConfigSubmitFO form = new DsConfigSubmitFO();
            form.setDsId(dataSourceId);
            form.setDsType(ds.getDataSourceType());
            form.setClusterId(input.getClusterId() == null ? ds.getBindClusterId() : input.getClusterId());
            form.setEnvId(input.getEnvId() == null ? ds.getDsEnvId() : input.getEnvId());
            form.setInstanceDesc(input.getInstanceDesc());
            form.setConfigMap(settings);
            this.requireInstalledType(ds.getDataSourceType());
            requireSuccess(this.dataSourceService.updateDs(caller.uid(), form));
            this.audit(caller, ds, AuditType.UPDATE_DATA_SOURCE_CONFIG);
        };
    }

    @Override
    public Supplier<String> prepareDeleteDataSource(OpenApiCaller caller, long dataSourceId) {
        this.requireRole(caller, DM_DS_MANAGE);
        DmDsDO ds = this.lockForManagement(caller, dataSourceId);
        return () -> {
            this.requireWritable(ds);
            if (this.approvalDal.approvalMapper().existsActiveByDataSource(caller.puid(), dataSourceId, TERMINAL_TICKET_STATUSES)
                || !this.changeFlowService.queryEnableDevopsByDsId(caller.puid(), dataSourceId).isEmpty()) {
                throw new OpenApiException(STATE_CONFLICT);
            }
            requireSuccess(this.dataSourceService.delDataSource(caller.puid(), dataSourceId));
            this.audit(caller, ds, AuditType.DELETE_DATA_SOURCE);
            return LifeCycleState.DELETED.name();
        };
    }

    private void applyConnection(Map<String, String> settings, OpenApiDataSourceConfig input, DmDsDO existing, DsConfigSpi config) {
        if (existing == null || input.getHost() != null || input.getPort() != null) {
            applyAddress(settings, input, existing, config);
        }
        if (existing != null && input.getSecurityType() == null && input.getUserName() == null && input.getPassword() == null) {
            return;
        }
        SecurityType security = input.getSecurityType();
        if (security == null) {
            security = existing == null ? SecurityType.USER_PASSWD : existing.getSecurityType();
        }
        if (!config.securityTypes().contains(security)) {
            throw new OpenApiException(INVALID_ARGUMENT);
        }
        boolean usesUser = security == SecurityType.ONLY_USER || security == SecurityType.USER_PASSWD;
        boolean usesPassword = security == SecurityType.USER_PASSWD;
        String userName = input.getUserName();
        if (userName == null && existing != null) {
            userName = existing.getAccessKey();
        }
        boolean savedPassword = existing != null && existing.getSecurityType() == SecurityType.USER_PASSWD && StringUtils.isNotBlank(existing.getSecretKey());
        if ((usesUser && StringUtils.isBlank(userName)) || (!usesUser && input.getUserName() != null)
            || (usesPassword && StringUtils.isBlank(input.getPassword()) && !savedPassword) || (!usesPassword && input.getPassword() != null)) {
            throw new OpenApiException(INVALID_ARGUMENT);
        }
        settings.put(Fields.securityType, security.name());
        if (input.getUserName() != null) {
            settings.put(Fields.userName, input.getUserName());
        }
        if (input.getPassword() != null) {
            settings.put(Fields.password, input.getPassword());
        }
    }

    private static void applyAddress(Map<String, String> settings, OpenApiDataSourceConfig input, DmDsDO existing, DsConfigSpi config) {
        try {
            String host = input.getHost();
            if (host == null && existing != null) {
                host = existing.getHost();
            }
            HostAndPort address = endpoint(host);
            int port = address.getPort();
            if (input.getPort() != null) {
                if (input.getHost() != null && port > 0) {
                    throw new IllegalArgumentException("host already contains a port");
                }
                port = input.getPort();
            }
            if (port < 0 && existing != null && existing.getHost() != null) {
                port = endpoint(existing.getHost()).getPort();
            }
            if (port < 0) {
                port = Integer.parseInt(config.defaultPort());
            }
            String formatted = HostAndPort.fromParts(address.formatHost(), port).toString();
            if (formatted.length() > 512) {
                throw new IllegalArgumentException("host is too long");
            }
            settings.put(DsConfigSpi.ADDRESS_FIELD, address.formatHost());
            settings.put(DsConfigSpi.PORT_FIELD, Integer.toString(port));
        } catch (IllegalArgumentException e) {
            throw new OpenApiException(INVALID_ARGUMENT);
        }
    }

    private static HostAndPort endpoint(String value) {
        HostAndPort address = HostAndPort.parse(value);
        URI uri = URI.create("tcp://" + address.formatHost());
        if (uri.getHost() == null || !address.formatHost().equals(uri.getRawAuthority())) {
            throw new IllegalArgumentException("invalid standalone host");
        }
        return address;
    }

    private static boolean supportsRegistration(DataSourceType type) {
        return type == DataSourceType.MySQL || type == DataSourceType.PostgreSQL;
    }

    private DsConfigSpi requireInstalledType(DataSourceType type) {
        DsConfigSpi config = PluginManager.findDsConfigSpi(type);
        if (config == null || this.configService.dsConstantSettings(type) == null) {
            throw new OpenApiException(NOT_FOUND);
        }
        return config;
    }

    private void requireBindings(OpenApiCaller caller, Long envId, Long clusterId) {
        if (envId != null && this.envService.queryByUserAndId(caller.puid(), caller.uid(), envId) == null) {
            throw new OpenApiException(NOT_FOUND);
        }
        if (clusterId != null) {
            DmSysClusterDO cluster = this.systemDal.clusterMapper().selectById(clusterId);
            if (cluster == null || !caller.puid().equals(cluster.getUid())) {
                throw new OpenApiException(NOT_FOUND);
            }
        }
    }

    private DmDsDO requireLiveDataSource(OpenApiCaller caller, long id) {
        DmDsDO ds = this.dataSourceService.queryById(id);
        if (ds == null || !caller.puid().equals(ds.getUid()) || ds.getLifeCycleState() == LifeCycleState.DELETED) {
            throw new OpenApiException(NOT_FOUND);
        }
        return ds;
    }

    private DmDsDO lockForManagement(OpenApiCaller caller, long id) {
        DmDsDO ds = this.dataSourceDal.dsMapper().selectByIdForUpdate(id);
        if (ds == null || !caller.puid().equals(ds.getUid())) {
            throw new OpenApiException(NOT_FOUND);
        }
        this.requireResource(caller, id, RDP_DAUTH_DS_MANAGER);
        return ds;
    }

    private void requireWritable(DmDsDO ds) {
        if (ds.getLifeCycleState() == LifeCycleState.DELETED) {
            throw new OpenApiException(NOT_FOUND);
        }
        if (ds.getLifeCycleState() != LifeCycleState.CREATED) {
            throw new OpenApiException(STATE_CONFLICT);
        }
    }

    private boolean hasRole(OpenApiCaller caller, String label) {
        return this.authorization.checkRoleAuth(caller.puid(), caller.uid(), label);
    }

    private void requireRole(OpenApiCaller caller, String label) {
        if (!this.hasRole(caller, label)) {
            throw new OpenApiException(FORBIDDEN);
        }
    }

    private void requireBindingRole(OpenApiCaller caller) {
        if (!this.hasRole(caller, DM_DS_MANAGE) && !this.hasRole(caller, RDP_DS_MANAGE)) {
            throw new OpenApiException(FORBIDDEN);
        }
    }

    private boolean hasResource(OpenApiCaller caller, long id, String label) {
        return this.authorization.checkResAuthWithoutError(caller.puid(), caller.uid(), id, RdpAuthUtils.genEmptyResPath(), label, AuthKind.DataSource);
    }

    private void requireResource(OpenApiCaller caller, long id, String label) {
        if (!this.hasResource(caller, id, label)) {
            throw new OpenApiException(FORBIDDEN);
        }
    }

    private static ResWebData<Long> requireSuccess(ResWebData<Long> response) {
        if (!response.isSuccess()) {
            throw new OpenApiException(STATE_CONFLICT);
        }
        return response;
    }

    private static OpenApiDataSourceInfo toInfo(DmDsDO ds, String address) {
        return new OpenApiDataSourceInfo(ds.getId(),
            ds.getInstanceId(),
            ds.getInstanceDesc(),
            ds.getDataSourceType().getTypeName(),
            ds.getDsEnvId(),
            ds.getLifeCycleState().name(),
            StringUtils.trimToNull(ds.getVersion()),
            address);
    }

    private void audit(OpenApiCaller caller, DmDsDO ds, AuditType type) {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
        this.auditService.logAndAddOperationAudit(caller.puid(), caller.uid(), request.getRequestURI(), request.getRemoteAddr(), ds
            .getId(), null, SecurityLevel.HIGH, type, ResourceType.DATASOURCE, ds.getInstanceId());
    }
}
