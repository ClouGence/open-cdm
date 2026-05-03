package com.clougence.clouddm.console.web.global.notify;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.clougence.clouddm.base.metadata.ds.ConfigKeys;
import com.clougence.clouddm.base.metadata.ds.DataSourceConfig;
import com.clougence.clouddm.base.metadata.ds.DataSourceType;
import com.clougence.clouddm.base.metadata.rdp.enumeration.GlobalDeployMode;
import com.clougence.clouddm.console.web.component.auth.BizResOwnerCacheService;
import com.clougence.clouddm.console.web.component.auth.model.UserCacheEntry;
import com.clougence.clouddm.console.web.constants.DmMode;
import com.clougence.clouddm.console.web.dal.mapper.DmDsConfigMapper;
import com.clougence.clouddm.console.web.dal.mapper.DmDsKvBaseConfigMapper;
import com.clougence.clouddm.console.web.dal.model.DmDsConfigDO;
import com.clougence.clouddm.console.web.dal.model.DmDsKvBaseConfigDO;
import com.clougence.clouddm.console.web.global.config.DmConsoleConfig;
import com.clougence.clouddm.console.web.util.DmConvertUtils;
import com.clougence.rdp.controller.model.fo.security.ModifyAuthForAppend;
import com.clougence.rdp.controller.model.fo.security.ModifyUserAuthFO;
import com.clougence.rdp.dal.enumeration.AccountBindType;
import com.clougence.rdp.dal.enumeration.AccountType;
import com.clougence.rdp.dal.enumeration.HostType;
import com.clougence.rdp.dal.mapper.RdpDataSourceMapper;
import com.clougence.rdp.dal.mapper.RdpDsKvBaseConfigMapper;
import com.clougence.rdp.dal.model.RdpDataSourceDO;
import com.clougence.rdp.dal.model.RdpDsKvBaseConfigDO;
import com.clougence.rdp.dal.model.RdpUserDO;
import com.clougence.clouddm.sdk.security.auth.AuthKind;
import com.clougence.clouddm.sdk.security.auth.AuthInfo;
import com.clougence.rdp.service.RdpAuthServiceForManage;
import com.clougence.rdp.service.RdpNotifyService;
import com.clougence.rdp.service.RdpUserService;

import lombok.extern.slf4j.Slf4j;

/**
 * @author mode 2020/11/7 17:11
 */
@Slf4j
@Service
public class DmDataSourceNotify implements RdpNotifyService {

    @Resource
    private RdpAuthServiceForManage authServiceForManage;

    @Resource
    private DmConsoleConfig         dmConfig;

    @Resource
    private RdpDataSourceMapper     rdpDsMapper;

    @Resource
    private DmDsKvBaseConfigMapper  dmDsKvConfMapper;

    @Resource
    private BizResOwnerCacheService ownerCacheService;

    @Resource
    private DmDsConfigMapper        dmDsConfigMapper;

    @Resource
    private RdpDsKvBaseConfigMapper rdpDsKvBaseConfigMapper;

    @Resource
    private RdpUserService          rdpUserService;

    @Override
    public void onDsAdd(String operatorUid, long dsId) {
        if (this.dmConfig.getDmMode() == DmMode.desktop && this.dmConfig.getPersonalConfig() != null) {
            return;
        }

        this.syncConf(dsId, true);
        this.addAuth(operatorUid, dsId);
    }

    @Override
    public void onDsUpdate(long dsId) {
        DmDsKvBaseConfigDO dsKvConf = this.dmDsKvConfMapper.queryByDsIdAndConfigName(dsId, DataSourceConfig.DM_DS_KEY_CONFIG_VERSION);
        if (dsKvConf != null) {
            long nextVersion = Long.parseLong(dsKvConf.getConfigValue()) + 1;
            this.dmDsKvConfMapper.updateDsConfig(dsId, DataSourceConfig.DM_DS_KEY_CONFIG_VERSION, Long.toString(nextVersion));
        }

        if (this.dmConfig.getDmMode() == DmMode.desktop && this.dmConfig.getPersonalConfig() != null) {
            return;
        }

        this.syncConf(dsId, false);
    }

    protected void addAuth(String operatorUid, long dsId) {
        RdpUserDO userDO = rdpUserService.getUserByUid(operatorUid);

        if (userDO.getAccountType() == AccountType.PRIMARY_ACCOUNT) {
            // primary user no need to add auth.
            return;
        }

        ModifyAuthForAppend item = new ModifyAuthForAppend();
        item.setResId(dsId);
        item.setResPaths(Collections.emptyList());

        if (GlobalDeployMode.inCloud()) {
            if (userDO.getBindType() != null && userDO.getBindType() == AccountBindType.MANAGED) {
                // cloud managed user should to be added all auth who added the datasource.
                List<AuthInfo> allDmAuth = this.authServiceForManage.getAllAuthLabel(AuthKind.DataSource);
                List<AuthInfo> allDataAuth = allDmAuth.stream().filter(a -> !a.isUsedOfRole()).collect(Collectors.toList());
                item.setAuthLabels(allDataAuth.stream().map(AuthInfo::getKey).collect(Collectors.toList()));
            }
        }

        ModifyUserAuthFO authFO = new ModifyUserAuthFO();
        authFO.setAuthKind(AuthKind.DataSource);
        authFO.setTargetUid(operatorUid);
        authFO.setAppends(Collections.singletonList(item));
        authFO.setUpdates(Collections.emptyList());
        authFO.setDeletes(Collections.emptyList());

        UserCacheEntry userCache = this.ownerCacheService.queryByUid(operatorUid);
        this.authServiceForManage.modifyUserAuth(userCache.getParentUid(), authFO);
    }

    public void onDsDelete(long dsId) {
        this.authServiceForManage.clearAuthOfRes(dsId, AuthKind.DataSource);
    }

    protected void syncConf(long dsId, boolean init) {
        RdpDataSourceDO dsDO = this.rdpDsMapper.selectById(dsId);
        this.dmDsKvConfMapper.updateDsConfig(dsId, ConfigKeys.DM_DS_KEY_SEC_TYPE, dsDO.getSecurityType().name());
        this.dmDsKvConfMapper.updateDsConfig(dsId, ConfigKeys.DM_DS_KEY_USERNAME, dsDO.getAccount());
        this.dmDsKvConfMapper.updateDsConfig(dsId, ConfigKeys.DM_DS_KEY_PASSWORD, dsDO.getPassword());

        DmDsConfigDO dmDsConfigDO = dmDsConfigMapper.queryByDataSourceId(dsId);
        if (dmDsConfigDO != null && dmDsConfigDO.getHostType() == HostType.PRIVATE) {
            this.dmDsKvConfMapper.updateDsConfig(dsId, ConfigKeys.DM_DS_KEY_HOST, dsDO.getPrivateHost());
        } else {
            this.dmDsKvConfMapper.updateDsConfig(dsId, ConfigKeys.DM_DS_KEY_HOST, dsDO.getPublicHost());
        }

        dataSourceConfig(dsId, init, dsDO);
    }

    private void dataSourceConfig(long dsId, boolean init, RdpDataSourceDO dsDO) {
        if (dsDO.getDataSourceType() == DataSourceType.MaxCompute) {
            RdpDsKvBaseConfigDO style = rdpDsKvBaseConfigMapper.queryByDsIdAndConfigName(dsDO.getId(), ConfigKeys.RDP_EXTRA_MC_SCHEMA_STYLE);
            RdpDsKvBaseConfigDO endPoint = rdpDsKvBaseConfigMapper.queryByDsIdAndConfigName(dsDO.getId(), ConfigKeys.RDP_EXTRA_MC_SDK_ENDPOINT);
            if (init) {
                if (style != null) {
                    DmDsKvBaseConfigDO config1 = DmConvertUtils.convertToDmDsKvBaseConfigDOForInsert(style);
                    dmDsKvConfMapper.insert(config1);
                }
                if (endPoint != null) {
                    DmDsKvBaseConfigDO config2 = DmConvertUtils.convertToDmDsKvBaseConfigDOForInsert(endPoint);
                    dmDsKvConfMapper.insert(config2);
                }
            } else {
                if (style != null) {
                    this.dmDsKvConfMapper.updateDsConfig(dsId, ConfigKeys.RDP_EXTRA_MC_SCHEMA_STYLE, style.getConfigValue());
                }
                if (endPoint != null) {
                    this.dmDsKvConfMapper.updateDsConfig(dsId, ConfigKeys.RDP_EXTRA_MC_SDK_ENDPOINT, endPoint.getConfigValue());
                }
            }
        }
    }
}
