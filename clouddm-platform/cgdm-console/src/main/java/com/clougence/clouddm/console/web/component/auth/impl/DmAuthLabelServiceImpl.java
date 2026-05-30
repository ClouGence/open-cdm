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
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.clougence.clouddm.api.common.boot.UnifiedPostConstruct;
import com.clougence.clouddm.base.metadata.ds.DataSourceType;
import com.clougence.clouddm.console.web.component.auth.DmAuthLabelService;
import com.clougence.clouddm.platform.plugin.PluginManager;
import com.clougence.clouddm.sdk.model.feature.RdpFeatureIDs;
import com.clougence.clouddm.sdk.security.auth.*;
import com.clougence.utils.CollectionUtils;
import com.clougence.utils.StringUtils;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class DmAuthLabelServiceImpl implements DmAuthLabelService, UnifiedPostConstruct, AuthBinder {

    private final AtomicBoolean                 running                  = new AtomicBoolean(false);
    private final Map<String, AuthInfo>         labelMap                 = new ConcurrentHashMap<>();
    private final Map<AuthKind, List<AuthInfo>> allAuthGroupByKind       = new ConcurrentHashMap<>();
    private final Map<String, AuthInfo>         labelMapOfTree           = new ConcurrentHashMap<>();
    private final Map<AuthKind, List<AuthInfo>> allAuthGroupByKindOfTree = new ConcurrentHashMap<>();

    public void init() {
        if (!running.compareAndSet(false, true)) {
            return;
        }

        List<AuthInfoSpi> list = PluginManager.findSpi(AuthInfoSpi.class);
        for (AuthInfoSpi spi : list) {
            log.info("[DmAuthLabelServiceImpl] SPI AuthRegistrySpi -> " + spi.getClass().getName());
            spi.registryAuthLabel(this);
        }
    }

    public void stop() {

    }

    private List<String> products() {
        List<String> strings = new ArrayList<>();
        strings.add(RdpFeatureIDs.PRODUCT_CLOUD_DM);
        return strings;
    }

    public AuthBinder addAuthInfo(AuthInfo authInfo) {
        this.labelMap.put(authInfo.getKey(), authInfo);
        this.labelMapOfTree.put(authInfo.getDefField(), authInfo);
        this.allAuthGroupByKind.clear();
        this.allAuthGroupByKindOfTree.clear();
        return this;
    }

    public List<AuthInfo> getAllCategory() { return this.labelMap.values().stream().filter(a -> a.getAuthType() == AuthInfoType.Category).collect(Collectors.toList()); }

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

        Map<String, AuthInfo> tempData = new TreeMap<>();
        for (AuthInfo info : result) {
            tempData.put(info.getKey(), info);
        }

        return new ArrayList<>(tempData.values());
    }

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

    public AuthInfo getAuthLabel(String authLabelKey) {
        return this.labelMap.get(authLabelKey);
    }

    public List<AuthInfo> getRoleAuthLabel() {
        List<String> products = products();
        return this.labelMap.values().stream().filter(info -> info.isUsedOfRole() && CollectionUtils.containsAny(info.getForProduct(), products)).collect(Collectors.toList());
    }

    public List<AuthInfo> getDataAuthLabel() {
        List<String> products = products();
        return this.labelMap.values().stream().filter(info -> !info.isUsedOfRole() && CollectionUtils.containsAny(info.getForProduct(), products)).collect(Collectors.toList());
    }

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
}
