package com.clougence.rdp.service.impl;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.clougence.clouddm.api.common.boot.UnifiedPostConstruct;
import com.clougence.clouddm.base.metadata.ds.DataSourceType;
import com.clougence.clouddm.platform.plugin.PluginManager;
import com.clougence.clouddm.sdk.model.analysis.resource.AuthBrowseObject;
import com.clougence.clouddm.sdk.model.feature.RdpFeatureIDs;
import com.clougence.clouddm.sdk.security.auth.*;
import com.clougence.rdp.controller.model.fo.security.ModifyAuthForAppend;
import com.clougence.rdp.controller.model.fo.security.ModifyAuthForDelete;
import com.clougence.rdp.controller.model.fo.security.ModifyAuthForUpdate;
import com.clougence.rdp.controller.model.fo.security.ModifyUserAuthFO;
import com.clougence.rdp.controller.model.fo.ticket.RdpAddAuthTicketFO;
import com.clougence.rdp.controller.model.vo.RdpAuthObjectVO;
import com.clougence.rdp.dal.enumeration.AccountType;
import com.clougence.rdp.dal.enumeration.RdpProduct;
import com.clougence.rdp.dal.mapper.RdpDataSourceMapper;
import com.clougence.rdp.dal.mapper.RdpProductClusterMapper;
import com.clougence.rdp.dal.mapper.RdpResAuthMapper;
import com.clougence.rdp.dal.mapper.RdpUserMapper;
import com.clougence.rdp.dal.model.RdpDataSourceDO;
import com.clougence.rdp.dal.model.RdpResAuthDO;
import com.clougence.rdp.dal.model.RdpUserDO;
import com.clougence.rdp.global.config.RdpConsoleConfig;
import com.clougence.rdp.service.RdpAuthServiceForManage;
import com.clougence.rdp.util.NamedThreadFactory;
import com.clougence.rdp.util.RdpConvertUtils;
import com.clougence.utils.CollectionUtils;
import com.clougence.utils.ExceptionUtils;
import com.clougence.utils.StringUtils;

import lombok.extern.slf4j.Slf4j;

/**
 * @author bucketli 2021/1/13 10:50
 */
@Slf4j
@Service
public class RdpAuthServiceForManageImpl implements RdpAuthServiceForManage, UnifiedPostConstruct, AuthBinder {

    private final AtomicBoolean                 running                  = new AtomicBoolean(false);

    private ScheduledExecutorService            cleanExpiredAuthExecutor;

    @Resource
    private RdpConsoleConfig                    rdpConfig;
    @Resource
    private RdpProductClusterMapper             rdpProductClusterMapper;
    @Resource
    private RdpResAuthMapper                    rdpResAuthMapper;
    @Resource
    private RdpDataSourceMapper                 rdpDsMapper;
    @Resource
    private RdpUserMapper                       rdpUserMapper;

    private final Map<String, AuthInfo>         labelMap                 = new ConcurrentHashMap<>();
    private final Map<AuthKind, List<AuthInfo>> allAuthGroupByKind       = new ConcurrentHashMap<>();
    private final Map<String, AuthInfo>         labelMapOfTree           = new ConcurrentHashMap<>();
    private final Map<AuthKind, List<AuthInfo>> allAuthGroupByKindOfTree = new ConcurrentHashMap<>();

    @Override
    public void init() {
        if (running.compareAndSet(false, true)) {
            List<AuthInfoSpi> list = PluginManager.findSpi(AuthInfoSpi.class);
            for (AuthInfoSpi spi : list) {
                log.info("[RdpAuthServiceForManageImpl] SPI AuthRegistrySpi -> " + spi.getClass().getName());
                spi.registryAuthLabel(this);
            }

            cleanExpiredAuthExecutor = Executors.newSingleThreadScheduledExecutor(new NamedThreadFactory("rdp-expired-auth-cleaner", false));
            cleanExpiredAuthExecutor.scheduleAtFixedRate(() -> {
                try {
                    log.info("[RDP] begin to clean expired data auths.");
                    rdpResAuthMapper.deleteByEndTimeExceed(Calendar.getInstance().getTime());
                    log.info("[RDP] clean expired data auths done.");
                } catch (Throwable e) {
                    log.error(this.getClass().getSimpleName() + " error.msg:" + ExceptionUtils.getRootCauseMessage(e), e);
                }
            }, 120, 10, TimeUnit.MINUTES);
        }
    }

    @Override
    public void stop() {

    }

    private List<String> products() {
        List<String> strings = new ArrayList<>();
        strings.add(this.rdpConfig.getDefaultProduct().name());
        strings.addAll(this.rdpProductClusterMapper.supportProductType());

        List<String> filterProduct = new ArrayList<>();
        if (strings.contains(RdpProduct.CloudCanal.name())) {
            filterProduct.add(RdpFeatureIDs.PRODUCT_CLOUD_CANAL);
        }
        if (strings.contains(RdpProduct.CloudDM.name())) {
            filterProduct.add(RdpFeatureIDs.PRODUCT_CLOUD_DM);
        }

        return filterProduct;
    }

    @Override
    public AuthBinder addAuthInfo(AuthInfo authInfo) {
        this.labelMap.put(authInfo.getKey(), authInfo);
        this.labelMapOfTree.put(authInfo.getDefField(), authInfo);
        this.allAuthGroupByKind.clear();
        return this;
    }

    @Override
    public List<AuthInfo> getAllCategory() {
        return this.labelMap.values().stream().filter(a -> {
            return a.getAuthType() == AuthInfoType.Category;
        }).collect(Collectors.toList());
    }

    @Override
    public List<AuthInfo> getCascadeAuthByLabel(String authLabel) {
        List<AuthInfo> result = Collections.emptyList();
        if (this.labelMap.containsKey(authLabel)) {
            AuthInfo info = this.labelMap.get(authLabel);

            result = new ArrayList<>();
            result.add(info);
            if (info.getAuthType() == AuthInfoType.Auth && info.getInclude() != null) {
                for (String include : info.getInclude()) {
                    result.addAll(this.getCascadeAuthByLabel(include));
                }
            }
        }

        // duplicate removal.
        Map<String, AuthInfo> tempData = new TreeMap<>();
        for (AuthInfo info : result) {
            tempData.put(info.getKey(), info);
        }

        return new ArrayList<>(tempData.values());
    }

    @Override
    public List<String> normalizeRoleAuthLabels(List<String> authLabels) {
        if (CollectionUtils.isEmpty(authLabels)) {
            return Collections.emptyList();
        }

        Map<String, String> categoryParentMap = this.getAllCategory().stream().collect(Collectors.toMap(AuthInfo::getKey, AuthInfo::getParent, (left, right) -> left));
        List<AuthInfo> roleAuthLabels = this.getRoleAuthLabel();
        Set<String> result = new TreeSet<>();

        for (String authLabel : authLabels) {
            AuthInfo info = this.labelMap.get(authLabel);
            if (info == null) {
                continue;
            }

            if (info.getAuthType() == AuthInfoType.Auth) {
                result.addAll(this.getCascadeAuthByLabel(authLabel).stream().filter(a -> a.getAuthType() == AuthInfoType.Auth).map(AuthInfo::getKey).collect(Collectors.toSet()));
                continue;
            }

            for (AuthInfo roleAuth : roleAuthLabels) {
                if (roleAuth.getAuthType() != AuthInfoType.Auth) {
                    continue;
                }

                if (!belongsToCategory(roleAuth.getCategory(), authLabel, categoryParentMap)) {
                    continue;
                }

                result.addAll(this.getCascadeAuthByLabel(roleAuth.getKey())
                    .stream()
                    .filter(a -> a.getAuthType() == AuthInfoType.Auth)
                    .map(AuthInfo::getKey)
                    .collect(Collectors.toSet()));
            }
        }

        return new ArrayList<>(result);
    }

    private boolean belongsToCategory(String categoryKey, String targetCategoryKey, Map<String, String> categoryParentMap) {
        String currentCategoryKey = categoryKey;
        while (StringUtils.isNotBlank(currentCategoryKey)) {
            if (StringUtils.equals(currentCategoryKey, targetCategoryKey)) {
                return true;
            }

            currentCategoryKey = categoryParentMap.get(currentCategoryKey);
        }

        return false;
    }

    private List<String> getCascadeAuthByLabel(List<String> authLabels) {
        Set<String> result = new TreeSet<>();
        for (String label : authLabels) {
            result.addAll(getCascadeAuthByLabel(label).stream().map(AuthInfo::getKey).collect(Collectors.toList()));
        }
        return new ArrayList<>(result);
    }

    @Override
    public AuthInfo getAuthLabel(String authLabelKey) {
        return this.labelMap.get(authLabelKey);
    }

    @Override
    public List<AuthInfo> getRoleAuthLabel() {
        List<String> products = products();
        return this.labelMap.values().stream().filter(info -> {
            return info.isUsedOfRole() && CollectionUtils.containsAny(info.getForProduct(), products);
        }).collect(Collectors.toList());
    }

    @Override
    public List<AuthInfo> getDataAuthLabel() {
        List<String> products = products();
        return this.labelMap.values().stream().filter(info -> {
            return !info.isUsedOfRole() && CollectionUtils.containsAny(info.getForProduct(), products);
        }).collect(Collectors.toList());
    }

    @Override
    public List<AuthInfo> getAllAuthLabel(AuthKind selectKind) {
        List<String> products = products();
        if (this.allAuthGroupByKind.isEmpty()) {
            Map<AuthKind, List<AuthInfo>> groupByKind = new HashMap<>();

            Collection<AuthInfo> allAuth = this.labelMap.values();
            for (AuthKind kind : AuthKind.values()) {
                List<AuthInfo> result = allAuth.stream().filter(i -> {
                    if (i.getAuthType() != AuthInfoType.Auth) {
                        return false;
                    } else {
                        return i.getKinds().contains(kind) && CollectionUtils.containsAny(i.getForProduct(), products);
                    }
                }).collect(Collectors.toList());
                groupByKind.put(kind, result);
            }

            this.allAuthGroupByKind.putAll(groupByKind);
        }

        return this.allAuthGroupByKind.getOrDefault(selectKind, Collections.emptyList());
    }

    private List<AuthInfo> getAllAuthLabelForTree(AuthKind selectKind) {
        List<String> products = products();
        if (this.allAuthGroupByKindOfTree.isEmpty()) {
            Map<AuthKind, List<AuthInfo>> groupByKind = new HashMap<>();

            Collection<AuthInfo> allAuth = this.labelMapOfTree.values();
            for (AuthKind kind : AuthKind.values()) {
                List<AuthInfo> result = allAuth.stream().filter(i -> {
                    if (i.getAuthType() != AuthInfoType.Auth) {
                        return false;
                    } else {
                        return i.getKinds().contains(kind) && CollectionUtils.containsAny(i.getForProduct(), products);
                    }
                }).collect(Collectors.toList());
                groupByKind.put(kind, result);
            }

            this.allAuthGroupByKindOfTree.putAll(groupByKind);
        }

        return this.allAuthGroupByKindOfTree.getOrDefault(selectKind, Collections.emptyList());
    }

    @Override
    public List<AuthInfo> getAllAuthLabelForAuthTreeDef(AuthKind kindType, AuthElementType elementType, DataSourceType dsType) {
        List<AuthInfo> infos = this.getAllAuthLabelForTree(kindType);

        Predicate<AuthInfo> hasAnyDsScope = a -> a.getScope() == AuthInfoScope.DataSource && a.getScopeDs() == dsType;
        Predicate<AuthInfo> dataFilter;
        if (dsType != null && infos.stream().anyMatch(hasAnyDsScope)) {
            dataFilter = a -> {
                boolean test1 = a.getScope() == AuthInfoScope.Public;
                boolean test2 = a.getScope() == AuthInfoScope.DataSource && a.getScopeDs() == dsType;
                return test1 || test2;
            };
        } else {
            dataFilter = a -> {
                boolean test1 = a.getScope() == AuthInfoScope.Public;
                boolean test2 = a.getScope() == AuthInfoScope.Default;
                return test1 || test2;
            };
        }

        return infos.stream().filter(dataFilter).filter(info -> {
            Map<AuthKind, List<AuthElementType>> condition = info.getCondition();
            return condition.get(kindType).contains(elementType);
        }).collect(Collectors.toList());
    }

    @Override
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

    protected List<AuthBrowseObject> listDsEles(String puid) {
        List<RdpDataSourceDO> dsDOs = this.rdpDsMapper.listByUserWithGmtOrder(puid);

        if (dsDOs == null || dsDOs.isEmpty()) {
            return Collections.emptyList();
        }

        List<AuthBrowseObject> objs = new ArrayList<>();

        for (RdpDataSourceDO dsDO : dsDOs) {
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

    @Override
    public List<RdpResAuthDO> listUserAuthWithoutLabels(String targetUid, AuthKind authKind) {
        if (rdpUserMapper.isResourceManger(targetUid) && authKind == AuthKind.DataSource) {
            // fetch datasource from parent
            Long pid = rdpUserMapper.queryByUid(targetUid).getParentId();
            if (pid != null) {
                String pUid = rdpUserMapper.queryById(pid).getUid();
                return rdpDsMapper //
                    .listByUser(pUid)
                    .stream()
                    .map(ds -> RdpConvertUtils.convertToAuthDOByDataSource(ds, null))
                    .collect(Collectors.toList());
            }
        }
        return this.rdpResAuthMapper.listWithoutLabels(targetUid, authKind);
    }

    @Override
    @Transactional(rollbackFor = Throwable.class, propagation = Propagation.REQUIRED)
    public void appendUserAuth(String uid, RdpAddAuthTicketFO fo) {
        List<RdpResAuthDO> applyInfo = fo.getApplyAuths().stream().map(applyAuth -> {
            return RdpConvertUtils.convertToAuthDOFromApply(uid, applyAuth, fo.getAuthKind());
        }).collect(Collectors.toList());

        for (RdpResAuthDO rdpResAuthDO : applyInfo) {
            this.rdpResAuthMapper.insert(rdpResAuthDO);
        }
    }

    @Override
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
        List<RdpResAuthDO> delAuth = modifyData.getDeletes().stream().map(d -> {
            return RdpConvertUtils.convertToAuthDOFromDelete(targetUid, d, modifyData.getAuthKind());
        }).collect(Collectors.toList());
        if (CollectionUtils.isNotEmpty(delAuth)) {
            this.deleteDataAuth(targetUid, modifyData.getAuthKind(), delAuth);
        }

        // for append
        List<RdpResAuthDO> addAuth = new ArrayList<>();
        for (ModifyAuthForAppend append : modifyData.getAppends()) {
            RdpResAuthDO authDO = RdpConvertUtils
                .convertToAuthDOFromInsert(targetUid, append, resInstIdMap.get(append.getResId()), resDescMap.get(append.getResId()), modifyData.getAuthKind());
            addAuth.add(authDO);
        }
        if (CollectionUtils.isNotEmpty(addAuth)) {
            this.appendDataAuth(targetUid, modifyData.getAuthKind(), addAuth);
        }

        // for update
        List<RdpResAuthDO> updateAuth = modifyData.getUpdates()
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
            List<RdpResAuthDO> auths = this.rdpResAuthMapper.selectBatchIds(delAuthIds);
            Set<Long> dResIds = auths.stream().map(RdpResAuthDO::getResId).collect(Collectors.toSet());
            resIds.addAll(dResIds);
        }

        List<RdpDataSourceDO> dss = this.rdpDsMapper.listByUser(puid);
        Set<Long> dsIds = dss.stream().map(RdpDataSourceDO::getId).collect(Collectors.toSet());
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
                List<RdpDataSourceDO> dss = rdpDsMapper.listByIds(new ArrayList<>(dsIds));
                for (RdpDataSourceDO ds : dss) {
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

    private void deleteDataAuth(String targetUid, AuthKind kindType, List<RdpResAuthDO> delAuth) {
        for (RdpResAuthDO authDO : delAuth) {
            List<RdpResAuthDO> list = this.rdpResAuthMapper.queryByPath(authDO.getResId(), targetUid, kindType, authDO.getResPath());
            if (CollectionUtils.isEmpty(list)) {
                continue;
            }
            this.rdpResAuthMapper.deleteByPath(authDO.getResId(), targetUid, kindType, authDO.getResPath());

            // keep unknown
            keepUnknownLabels(list);
        }
    }

    private void appendDataAuth(String targetUid, AuthKind kindType, List<RdpResAuthDO> append) {
        List<RdpResAuthDO> authDOs = append.stream().filter(a -> CollectionUtils.isNotEmpty(a.getAuthLabels())).collect(Collectors.toList());

        Map<String, List<RdpResAuthDO>> oldAuthMap = new HashMap<>();
        List<RdpResAuthDO> authList = this.rdpResAuthMapper.listByKind(targetUid, kindType);

        authList.forEach(authDO -> {
            String key = targetUid + "-" + authDO.getResId() + "-" + authDO.getKindType() + "-" + authDO.getResPath();
            oldAuthMap.computeIfAbsent(key, k -> new ArrayList<>()).add(authDO);
        });

        for (RdpResAuthDO authDO : authDOs) {
            String key = targetUid + "-" + authDO.getResId() + "-" + authDO.getKindType() + "-" + authDO.getResPath();
            this.rdpResAuthMapper.deleteByPath(authDO.getResId(), targetUid, kindType, authDO.getResPath());
            if (oldAuthMap.containsKey(key)) {
                keepUnknownLabels(oldAuthMap.get(key));
            }
            List<String> cascadeAuthLabel = this.getCascadeAuthByLabel(authDO.getAuthLabels());
            authDO.setAuthLabels(cascadeAuthLabel);
            this.rdpResAuthMapper.insert(authDO);
        }
    }

    private void keepUnknownLabels(List<RdpResAuthDO> rdpResAuthDOS) {
        for (RdpResAuthDO rdpResAuthDO : rdpResAuthDOS) {
            List<String> labels = this.unknownLabels(rdpResAuthDO.getAuthLabels());
            if (!labels.isEmpty() && rdpResAuthDO.isNotExpired()) {
                rdpResAuthDO.setId(null);
                rdpResAuthDO.setAuthLabels(labels);
                this.rdpResAuthMapper.insert(rdpResAuthDO);
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
    @Override
    public void clearAuthOfRes(long resId, AuthKind authKind) {
        this.rdpResAuthMapper.deleteByRes(resId, authKind);
    }

    @Override
    public void clearAuthOfUser(String uid) {
        this.rdpResAuthMapper.deleteByUser(uid);
    }

    @Override
    public boolean isResourceMangerEnable(String targetUid) {
        Boolean isResourceManger = rdpUserMapper.isResourceManger(targetUid);
        if (isResourceManger == null) {
            return false;
        }
        return isResourceManger;
    }

    @Override
    public List<RdpResAuthDO> listUserAuthByRes(String targetUid, long resId, List<String> authPrefixList, AuthKind authKind) {
        if (authKind == AuthKind.DataSource) {
            RdpUserDO rdpUserDO = this.rdpUserMapper.queryByUid(targetUid);
            if (rdpUserDO.isResourceManageEnable() || rdpUserDO.getAccountType() == AccountType.PRIMARY_ACCOUNT) {
                RdpDataSourceDO ds = this.rdpDsMapper.selectById(resId);
                List<String> labels = this.getAllAuthLabel(authKind).stream().map(AuthInfo::getKey).collect(Collectors.toList());
                return Collections.singletonList(RdpConvertUtils.convertToAuthDOByDataSource(ds, labels));
            }

            List<RdpResAuthDO> rdpResAuthDOS = this.rdpResAuthMapper.queryByPathLike(resId, targetUid, authKind, authPrefixList);
            return rdpResAuthDOS.stream().//
                filter(RdpResAuthDO::isEffective).//
                collect(Collectors.toList());
        } else {
            throw new IllegalArgumentException("Unsupported auth kind:" + authKind);
        }
    }
}
