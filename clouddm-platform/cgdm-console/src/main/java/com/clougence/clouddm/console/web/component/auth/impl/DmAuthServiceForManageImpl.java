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
package com.clougence.clouddm.console.web.component.auth.impl;

import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.clougence.clouddm.api.common.boot.UnifiedPostConstruct;
import com.clougence.clouddm.base.metadata.ds.DataSourceType;
import com.clougence.clouddm.console.web.component.auth.DmAuthLabelService;
import com.clougence.clouddm.console.web.component.auth.DmAuthServiceForManage;
import com.clougence.clouddm.console.web.model.fo.security.ModifyAuthForAppend;
import com.clougence.clouddm.console.web.model.fo.security.ModifyAuthForDelete;
import com.clougence.clouddm.console.web.model.fo.security.ModifyAuthForUpdate;
import com.clougence.clouddm.console.web.model.fo.security.ModifyUserAuthFO;
import com.clougence.clouddm.console.web.model.fo.ticket.RdpAddAuthTicketFO;
import com.clougence.clouddm.console.web.model.vo.RdpAuthObjectVO;
import com.clougence.clouddm.console.web.util.NamedThreadFactory;
import com.clougence.clouddm.console.web.util.RdpConvertUtils;
import com.clougence.clouddm.platform.dal.access.AuthDal;
import com.clougence.clouddm.platform.dal.access.DataSourceDal;
import com.clougence.clouddm.platform.dal.model.auth.AccountType;
import com.clougence.clouddm.platform.dal.model.auth.DmAuthResDO;
import com.clougence.clouddm.platform.dal.model.auth.DmAuthUserDO;
import com.clougence.clouddm.platform.dal.model.datasource.DmDsConfigDO;
import com.clougence.clouddm.platform.dal.model.datasource.DmDsDO;
import com.clougence.clouddm.sdk.model.analysis.resource.AuthBrowseObject;
import com.clougence.clouddm.sdk.security.auth.AuthElementType;
import com.clougence.clouddm.sdk.security.auth.AuthInfo;
import com.clougence.clouddm.sdk.security.auth.AuthInfoType;
import com.clougence.clouddm.sdk.security.auth.AuthKind;
import com.clougence.utils.CollectionUtils;
import com.clougence.utils.ExceptionUtils;
import com.clougence.utils.StringUtils;

import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;

/**
 * @author bucketli 2021/1/13 10:50
 */
@Service
@Slf4j
public class DmAuthServiceForManageImpl implements DmAuthServiceForManage, UnifiedPostConstruct {

    @Resource
    private DataSourceDal            dsDal;
    @Resource
    private AuthDal                  authDal;
    @Resource
    private DmAuthLabelService       authLabelService;
    private ScheduledExecutorService cleanExpiredAuthExecutor;

    private final AtomicBoolean      running = new AtomicBoolean(false);

    public void init() {
        if (running.compareAndSet(false, true)) {
            cleanExpiredAuthExecutor = Executors.newSingleThreadScheduledExecutor(new NamedThreadFactory("rdp-expired-auth-cleaner", false));
            cleanExpiredAuthExecutor.scheduleAtFixedRate(() -> {
                try {
                    log.info("[RDP] begin to clean expired data auths.");
                    authDal.resMapper().deleteByEndTimeExceed(Calendar.getInstance().getTime());
                    log.info("[RDP] clean expired data auths done.");
                } catch (Throwable e) {
                    log.error(this.getClass().getSimpleName() + " error.msg:" + ExceptionUtils.getRootCauseMessage(e), e);
                }
            }, 120, 10, TimeUnit.MINUTES);
        }
    }

    public void stop() {

    }

    public List<AuthInfo> getAllCategory() { return this.authLabelService.getAllCategory(); }

    public List<AuthInfo> getCascadeAuthByLabel(String authLabel) {
        return this.authLabelService.getCascadeAuthByLabel(authLabel);
    }

    public List<String> normalizeRoleAuthLabels(List<String> authLabels) {
        return this.authLabelService.normalizeRoleAuthLabels(authLabels);
    }

    private List<String> getCascadeAuthByLabel(List<String> authLabels) {
        Set<String> result = new TreeSet<>();
        for (String label : authLabels) {
            result.addAll(getCascadeAuthByLabel(label).stream().map(AuthInfo::getKey).toList());
        }
        return new ArrayList<>(result);
    }

    public AuthInfo getAuthLabel(String authLabelKey) {
        return this.authLabelService.getAuthLabel(authLabelKey);
    }

    public List<AuthInfo> getRoleAuthLabel() { return this.authLabelService.getRoleAuthLabel(); }

    public List<AuthInfo> getDataAuthLabel() { return this.authLabelService.getDataAuthLabel(); }

    public List<AuthInfo> getAllAuthLabel(AuthKind selectKind) {
        return this.authLabelService.getAllAuthLabel(selectKind);
    }

    public List<AuthInfo> getAllAuthLabelForAuthTreeDef(AuthKind kindType, AuthElementType elementType, DataSourceType dsType) {
        return this.authLabelService.getAllAuthLabelForAuthTreeDef(kindType, elementType, dsType);
    }

    public List<RdpAuthObjectVO> listElements(String puid, List<String> levels, AuthKind authKind) {
        List<AuthBrowseObject> objs;
        if (authKind == AuthKind.DataSource) {
            objs = listDsEles(puid);
        } else {
            throw new IllegalArgumentException("Unsupported auth kind:" + authKind);
        }

        if (objs == null) {
            return Collections.emptyList();
        } else {
            return objs.stream().map(RdpConvertUtils::convertToRdpAuthObjectVO).collect(Collectors.toList());
        }
    }

    public List<RdpAuthObjectVO> listElements(String puid, String envId, AuthKind authKind) {
        List<AuthBrowseObject> objs;
        if (authKind == AuthKind.DataSource) {
            objs = listDsEles(puid, envId);
        } else {
            throw new IllegalArgumentException("Unsupported auth kind:" + authKind);
        }

        return objs.stream().map(RdpConvertUtils::convertToRdpAuthObjectVO).collect(Collectors.toList());
    }

    private List<AuthBrowseObject> listDsEles(String puid, String envId) {
        List<DmDsDO> dsDOs = this.dsDal.dsMapper().listByDsEnvId(Long.parseLong(envId));
        if (dsDOs == null || dsDOs.isEmpty()) {
            return Collections.emptyList();
        }
        List<Long> dsIds = dsDOs.stream().map(DmDsDO::getId).collect(Collectors.toList());

        List<DmDsConfigDO> confList = this.dsDal.configMapper().queryByIds(puid, dsIds);
        List<Long> enableQueryDsIds = confList.stream().map(DmDsConfigDO::getDataSourceId).collect(Collectors.toList());

        List<AuthBrowseObject> objs = new ArrayList<>();
        for (DmDsDO dsDO : dsDOs) {
            boolean enable = enableQueryDsIds.contains(dsDO.getId());
            if (!enable) {
                continue;
            }

            AuthBrowseObject obj = new AuthBrowseObject();
            obj.setObjId(dsDO.getId());
            obj.setObjName(dsDO.getInstanceId());
            obj.setObjDesc(dsDO.getInstanceDesc());
            obj.setObjType(AuthElementType.Instance);
            obj.setObjAttr(new HashMap<>());
            obj.getObjAttr().put("dsDeployType", dsDO.getDeployType().name());
            obj.getObjAttr().put("dsType", dsDO.getDataSourceType().name());
            obj.getObjAttr().put("enableQuery", enable);
            obj.setLeaf(true);
            objs.add(obj);
        }

        return objs;
    }

    protected List<AuthBrowseObject> listDsEles(String puid) {
        List<DmDsDO> dsDOs = this.dsDal.dsMapper().listByUserWithGmtOrder(puid);

        if (dsDOs == null || dsDOs.isEmpty()) {
            return Collections.emptyList();
        }

        List<AuthBrowseObject> objs = new ArrayList<>();

        for (DmDsDO dsDO : dsDOs) {
            AuthBrowseObject obj = new AuthBrowseObject();

            obj.setObjId(dsDO.getId());
            obj.setObjName(dsDO.getInstanceId());
            obj.setObjDesc(dsDO.getInstanceDesc());
            obj.setObjType(AuthElementType.Instance);
            obj.setObjAttr(new HashMap<>());
            obj.getObjAttr().put("dsDeployType", dsDO.getDeployType().name());
            obj.getObjAttr().put("dsType", dsDO.getDataSourceType().name());
            obj.setLeaf(true);
            objs.add(obj);
        }

        return objs;
    }

    public List<DmAuthResDO> listUserAuthWithoutLabels(String targetUid, AuthKind authKind) {
        if (authDal.userMapper().isResourceManger(targetUid) && authKind == AuthKind.DataSource) {
            // fetch datasource from parent
            Long pid = authDal.userMapper().queryByUid(targetUid).getParentId();
            if (pid != null) {
                String pUid = authDal.userMapper().queryById(pid).getUid();
                return this.dsDal.dsMapper() //
                    .listByUser(pUid)
                    .stream()
                    .map(ds -> RdpConvertUtils.convertToAuthDOByDataSource(ds, null))
                    .collect(Collectors.toList());
            }
        }
        return this.authDal.resMapper().listWithoutLabels(targetUid, authKind);
    }

    @Transactional(rollbackFor = Throwable.class, propagation = Propagation.REQUIRED)
    public void appendUserAuth(String uid, RdpAddAuthTicketFO fo) {
        List<DmAuthResDO> applyInfo = fo.getApplyAuths().stream().map(applyAuth -> {
            return RdpConvertUtils.convertToAuthDOFromApply(uid, applyAuth, fo.getAuthKind());
        }).collect(Collectors.toList());

        for (DmAuthResDO resAuthDO : applyInfo) {
            this.authDal.resMapper().insert(resAuthDO);
        }
    }

    @Transactional(rollbackFor = Throwable.class, propagation = Propagation.REQUIRED)
    public void modifyUserAuth(String puid, ModifyUserAuthFO modifyData) {
        //now only support DataSource
        if (modifyData.getAuthKind() != AuthKind.DataSource) {
            throw new IllegalArgumentException("Unsupported auth kind:" + modifyData.getAuthKind());
        }

        checkResOwner(puid, modifyData);

        Map<Long, String> resInstIdMap = new HashMap<>();
        Map<Long, String> resDescMap = new HashMap<>();
        fillExtraInfo(resInstIdMap, resDescMap, modifyData.getAppends(), modifyData.getUpdates(), modifyData.getAuthKind());
        String targetUid = modifyData.getTargetUid();

        // for delete
        List<DmAuthResDO> delAuth = modifyData.getDeletes().stream().map(d -> {
            return RdpConvertUtils.convertToAuthDOFromDelete(targetUid, d, modifyData.getAuthKind());
        }).collect(Collectors.toList());
        if (CollectionUtils.isNotEmpty(delAuth)) {
            this.deleteDataAuth(targetUid, modifyData.getAuthKind(), delAuth);
        }

        // for append
        List<DmAuthResDO> addAuth = new ArrayList<>();
        for (ModifyAuthForAppend append : modifyData.getAppends()) {
            DmAuthResDO authDO = RdpConvertUtils
                .convertToAuthDOFromInsert(targetUid, append, resInstIdMap.get(append.getResId()), resDescMap.get(append.getResId()), modifyData.getAuthKind());
            addAuth.add(authDO);
        }
        if (CollectionUtils.isNotEmpty(addAuth)) {
            this.appendDataAuth(targetUid, modifyData.getAuthKind(), addAuth);
        }

        // for update
        List<DmAuthResDO> updateAuth = modifyData.getUpdates()
            .stream()
            .map(u -> RdpConvertUtils.convertToAuthDOFromUpdate(targetUid, u, resInstIdMap.get(u.getResId()), resDescMap.get(u.getResId()), modifyData.getAuthKind()))
            .collect(Collectors.toList());
        this.appendDataAuth(targetUid, modifyData.getAuthKind(), updateAuth);
    }

    protected void checkResOwner(String puid, ModifyUserAuthFO modifyData) {
        Set<Long> resIds = new HashSet<>();

        if (modifyData.getAppends() != null && !modifyData.getAppends().isEmpty()) {
            Set<Long> iResIds = modifyData.getAppends().stream().map(ModifyAuthForAppend::getResId).collect(Collectors.toSet());
            resIds.addAll(iResIds);
        }

        if (modifyData.getUpdates() != null && !modifyData.getUpdates().isEmpty()) {
            Set<Long> uResIds = modifyData.getUpdates().stream().map(ModifyAuthForUpdate::getResId).collect(Collectors.toSet());
            resIds.addAll(uResIds);
        }

        if (modifyData.getDeletes() != null && !modifyData.getDeletes().isEmpty()) {
            List<Long> delAuthIds = modifyData.getDeletes().stream().map(ModifyAuthForDelete::getAuthId).collect(Collectors.toList());
            List<DmAuthResDO> auths = this.authDal.resMapper().selectBatchIds(delAuthIds);
            Set<Long> dResIds = auths.stream().map(DmAuthResDO::getResId).collect(Collectors.toSet());
            resIds.addAll(dResIds);
        }

        List<DmDsDO> dss = this.dsDal.dsMapper().listByUser(puid);
        Set<Long> dsIds = dss.stream().map(DmDsDO::getId).collect(Collectors.toSet());
        if (!dsIds.containsAll(resIds)) {
            throw new IllegalArgumentException("Resource not belong the primary user.");
        }
    }

    protected void fillExtraInfo(Map<Long, String> resInstIdMap, Map<Long, String> resDescMap, List<ModifyAuthForAppend> appends, List<ModifyAuthForUpdate> updates,
                                 AuthKind authKind) {
        if (authKind == AuthKind.DataSource) {
            Set<Long> dsIds = appends.stream().map(ModifyAuthForAppend::getResId).collect(Collectors.toSet());
            dsIds.addAll(updates.stream().map(ModifyAuthForUpdate::getResId).collect(Collectors.toSet()));
            if (!dsIds.isEmpty()) {
                List<DmDsDO> dss = dsDal.dsMapper().listByIds(new ArrayList<>(dsIds));
                for (DmDsDO ds : dss) {
                    resInstIdMap.put(ds.getId(), ds.getInstanceId());

                    if (StringUtils.isBlank(ds.getInstanceDesc())) {
                        resDescMap.put(ds.getId(), ds.getInstanceId());
                    } else {
                        resDescMap.put(ds.getId(), ds.getInstanceDesc());
                    }
                }
            }
        } else {
            throw new IllegalArgumentException("Unsupported authKind:" + authKind);
        }
    }

    private void deleteDataAuth(String targetUid, AuthKind kindType, List<DmAuthResDO> delAuth) {
        for (DmAuthResDO authDO : delAuth) {
            List<DmAuthResDO> list = this.authDal.resMapper().queryByPath(authDO.getResId(), targetUid, kindType, authDO.getResPath());
            if (CollectionUtils.isEmpty(list)) {
                continue;
            }
            this.authDal.resMapper().deleteByPath(authDO.getResId(), targetUid, kindType, authDO.getResPath());

            // keep unknown
            keepUnknownLabels(list);
        }
    }

    private void appendDataAuth(String targetUid, AuthKind kindType, List<DmAuthResDO> append) {
        List<DmAuthResDO> authDOs = append.stream().filter(a -> CollectionUtils.isNotEmpty(a.getAuthLabels())).collect(Collectors.toList());

        Map<String, List<DmAuthResDO>> oldAuthMap = new HashMap<>();
        List<DmAuthResDO> authList = this.authDal.resMapper().listByKind(targetUid, kindType);

        authList.forEach(authDO -> {
            String key = targetUid + "-" + authDO.getResId() + "-" + authDO.getKindType() + "-" + authDO.getResPath();
            oldAuthMap.computeIfAbsent(key, k -> new ArrayList<>()).add(authDO);
        });

        for (DmAuthResDO authDO : authDOs) {
            String key = targetUid + "-" + authDO.getResId() + "-" + authDO.getKindType() + "-" + authDO.getResPath();
            this.authDal.resMapper().deleteByPath(authDO.getResId(), targetUid, kindType, authDO.getResPath());
            if (oldAuthMap.containsKey(key)) {
                keepUnknownLabels(oldAuthMap.get(key));
            }
            List<String> cascadeAuthLabel = this.getCascadeAuthByLabel(authDO.getAuthLabels());
            authDO.setAuthLabels(cascadeAuthLabel);
            this.authDal.resMapper().insert(authDO);
        }
    }

    private void keepUnknownLabels(List<DmAuthResDO> resAuthDOList) {
        for (DmAuthResDO resAuthDO : resAuthDOList) {
            List<String> labels = this.unknownLabels(resAuthDO.getAuthLabels());
            if (!labels.isEmpty() && resAuthDO.isNotExpired()) {
                resAuthDO.setId(null);
                resAuthDO.setAuthLabels(labels);
                this.authDal.resMapper().insert(resAuthDO);
            }
        }
    }

    private List<String> unknownLabels(List<String> labels) {

        // find unknownLabel to keep
        List<String> allLabel = this.getDataAuthLabel().stream().filter(a -> a.getAuthType() == AuthInfoType.Auth).map(AuthInfo::getKey).collect(Collectors.toList());
        List<String> unknownLabel = new ArrayList<>(labels);
        unknownLabel.removeAll(allLabel);

        // merge keepLabel and unknownLabel, keep cascade
        Set<String> finalLabel = new HashSet<>();
        for (String label : unknownLabel) {
            List<AuthInfo> labelSet = this.getCascadeAuthByLabel(label);
            finalLabel.addAll(labelSet.stream().map(AuthInfo::getKey).collect(Collectors.toList()));
            finalLabel.add(label);
        }
        return new ArrayList<>(finalLabel);
    }

    private Collection<String> evalLabels(List<String> beforeLabels, List<String> afterLabels) {
        // find all keepLabel to add.
        Set<String> keepLabel = new HashSet<>(afterLabels);

        // find unknownLabel to keep
        List<String> allLabel = this.getDataAuthLabel().stream().filter(a -> a.getAuthType() == AuthInfoType.Auth).map(AuthInfo::getKey).collect(Collectors.toList());
        List<String> unknownLabel = new ArrayList<>(beforeLabels);
        unknownLabel.removeAll(allLabel);

        // merge keepLabel and unknownLabel, keep cascade
        Set<String> finalLabel = new HashSet<>();
        for (String label : keepLabel) {
            List<AuthInfo> labelSet = this.getCascadeAuthByLabel(label);
            finalLabel.addAll(labelSet.stream().map(AuthInfo::getKey).collect(Collectors.toList()));
            finalLabel.add(label);
        }
        for (String label : unknownLabel) {
            List<AuthInfo> labelSet = this.getCascadeAuthByLabel(label);
            finalLabel.addAll(labelSet.stream().map(AuthInfo::getKey).collect(Collectors.toList()));
            finalLabel.add(label);
        }
        return finalLabel;
    }

    @Transactional(rollbackFor = Throwable.class, propagation = Propagation.REQUIRED)
    public void clearAuthOfRes(long resId, AuthKind authKind) {
        this.authDal.resMapper().deleteByRes(resId, authKind);
    }

    public void clearAuthOfUser(String uid) {
        this.authDal.resMapper().deleteByUser(uid);
    }

    public boolean isResourceMangerEnable(String targetUid) {
        Boolean isResourceManger = authDal.userMapper().isResourceManger(targetUid);
        if (isResourceManger == null) {
            return false;
        }
        return isResourceManger;
    }

    public List<DmAuthResDO> listUserAuthByRes(String targetUid, long resId, List<String> authPrefixList, AuthKind authKind) {
        if (authKind == AuthKind.DataSource) {
            DmAuthUserDO rdpUserDO = this.authDal.userMapper().queryByUid(targetUid);
            if (rdpUserDO.isResourceManageEnable() || rdpUserDO.getAccountType() == AccountType.PRIMARY_ACCOUNT) {
                DmDsDO ds = this.dsDal.dsMapper().selectById(resId);
                List<String> labels = this.getAllAuthLabel(authKind).stream().map(AuthInfo::getKey).collect(Collectors.toList());
                return Collections.singletonList(RdpConvertUtils.convertToAuthDOByDataSource(ds, labels));
            }

            List<DmAuthResDO> resAuthDO = this.authDal.resMapper().queryByPathLike(resId, targetUid, authKind, authPrefixList);
            return resAuthDO.stream().//
                filter(DmAuthResDO::isEffective).//
                collect(Collectors.toList());
        } else {
            throw new IllegalArgumentException("Unsupported auth kind:" + authKind);
        }
    }
}
