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
package com.clougence.clouddm.console.web.component.config.impl;

import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.clougence.clouddm.api.common.boot.UnifiedPostConstruct;
import com.clougence.clouddm.api.common.boot.UnifiedPostConstructOrder;
import com.clougence.clouddm.api.common.rpc.ResWebData;
import com.clougence.clouddm.api.common.rpc.ResWebDataUtils;
import com.clougence.clouddm.console.web.component.auth.DmAuthLabelService;
import com.clougence.clouddm.console.web.global.config.DmConsoleConfig;
import com.clougence.clouddm.console.web.global.i18n.DmI18nUtils;
import com.clougence.clouddm.console.web.global.i18n.I18nRdpMsgKeys;
import com.clougence.clouddm.console.web.model.fo.role.CreateRoleFO;
import com.clougence.clouddm.console.web.model.fo.role.DeleteRoleFO;
import com.clougence.clouddm.console.web.model.fo.role.UpdateRoleFO;
import com.clougence.clouddm.console.web.service.auth.RdpRoleService;
import com.clougence.clouddm.platform.dal.access.AuthDal;
import com.clougence.clouddm.platform.dal.model.auth.AccountType;
import com.clougence.clouddm.platform.dal.model.auth.DmAuthRoleDO;
import com.clougence.clouddm.platform.dal.model.auth.DmAuthUserDO;
import com.clougence.clouddm.platform.plugin.PluginManager;
import com.clougence.clouddm.sdk.security.auth.AuthInfo;
import com.clougence.clouddm.sdk.security.auth.AuthInfoSpi;
import com.clougence.clouddm.sdk.security.auth.AuthInfoType;
import com.clougence.clouddm.sdk.security.auth.def.SecSysRole;
import com.clougence.rdp.service.model.AddRoleMO;
import com.clougence.utils.CollectionUtils;
import com.clougence.utils.JsonUtils;
import com.clougence.utils.StringUtils;

import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;

/**
 * @author bucketli 2021/1/9 12:22
 */
@Slf4j
@Service
@UnifiedPostConstructOrder(1)
public class RdpRoleServiceImpl implements RdpRoleService, UnifiedPostConstruct {

    @Resource
    private AuthDal                        authDal;
    @Resource
    private DmConsoleConfig                rdpConfig;
    @Resource
    private DmAuthLabelService             authLabelService;

    private final AtomicBoolean            init             = new AtomicBoolean(false);
    private final Map<String, Set<String>> innerRoleInfoDef = new TreeMap<>();

    @Transactional(rollbackFor = Throwable.class, propagation = Propagation.REQUIRED)
    @Override
    public void init() {
        if (!init.compareAndSet(false, true)) {
            return;
        }

        // 1. load inner role info.
        List<AuthInfoSpi> list = PluginManager.findSpi(AuthInfoSpi.class);

        Set<String> innerRole = new TreeSet<>();
        for (AuthInfoSpi spi : list) {
            log.info("[Rdp Role Service] SPI init innerRole -> " + spi.getClass().getName());
            innerRole.addAll(spi.innerRoleDef());
        }

        for (String roleName : innerRole) {
            if (!this.innerRoleInfoDef.containsKey(roleName)) {
                this.innerRoleInfoDef.put(roleName, new TreeSet<>());
            }

            Set<String> labels = this.innerRoleInfoDef.get(roleName);
            for (AuthInfoSpi spi : list) {
                labels.addAll(spi.innerRoleAuthLabels(roleName));
            }
        }

        Map<String, String> innerRoleLog = new TreeMap<>();
        this.innerRoleInfoDef.forEach((s, strings) -> innerRoleLog.put(s, "size:" + strings.size()));
        log.info("[Rdp Role Service] Inner Role Info:" + JsonUtils.toJson(innerRoleLog));

        // 2. update inner role label
        for (String roleName : innerRole) {
            Set<String> authLabels = this.innerRoleInfoDef.get(roleName);
            String labelJson = JsonUtils.toJson(authLabels);
            this.authDal.roleMapper().updateInnerRoleByName(roleName, labelJson);
        }
    }

    @Override
    public void stop() {

    }

    @Override
    public Set<String> getInnerRoleLabel(String roleName) {
        return this.innerRoleInfoDef.getOrDefault(roleName, Collections.emptySet());
    }

    @Override
    public List<DmAuthRoleDO> listRoleByUID(String puid) {
        List<DmAuthRoleDO> roles = this.authDal.roleMapper().queryByOwnerUid(puid);
        roles.forEach(roleDO -> {
            if (roleDO.isInnerTag()) {
                roleDO.setAliasName(DmI18nUtils.getMessage(roleDO.getRoleName()));
            }
        });
        return roles;
    }

    @Override
    public List<DmAuthRoleDO> listRoleExcludeByName(String puid, List<String> name) {
        List<DmAuthRoleDO> roles = this.authDal.roleMapper().queryByOwnerUid(puid);

        roles = roles.stream().filter(p -> !name.contains(p.getRoleName())).collect(Collectors.toList());

        roles.forEach(roleDO -> {
            if (roleDO.isInnerTag()) {
                roleDO.setAliasName(DmI18nUtils.getMessage(roleDO.getRoleName()));
            }
        });
        return roles;
    }

    @Override
    public DmAuthRoleDO fetchRoleById(long roleId) {
        DmAuthRoleDO roleDO = this.authDal.roleMapper().selectById(roleId);
        if (roleDO == null) {
            return null;
        }

        if (roleDO.isInnerTag()) {
            roleDO.setAliasName(DmI18nUtils.getMessage(roleDO.getRoleName()));
        }
        return roleDO;
    }

    @Override
    public AddRoleMO createRole(String puid, CreateRoleFO fo) {
        if (StringUtils.isBlank(fo.getRoleName())) {
            return new AddRoleMO(false, DmI18nUtils.getMessage(I18nRdpMsgKeys.ROLE_CRATE_NAME_IS_EMPTY_ERROR.name()));
        }

        if (this.innerRoleInfoDef.containsKey(fo.getRoleName())) {
            String avoid = StringUtils.join(this.innerRoleInfoDef.keySet().toArray(), ", ");
            return new AddRoleMO(false, DmI18nUtils.getMessage(I18nRdpMsgKeys.ROLE_CRATE_NAME_IS_INNER_ERROR.name(), fo.getRoleName(), avoid));
        }

        List<DmAuthRoleDO> sameRoles = this.authDal.roleMapper().queryByRoleName(puid, fo.getRoleName());
        if (sameRoles != null && !sameRoles.isEmpty()) {
            return new AddRoleMO(false, DmI18nUtils.getMessage(I18nRdpMsgKeys.ROLE_CRATE_NAME_IS_EXIST_ERROR.name(), fo.getRoleName()));
        }

        DmAuthRoleDO roleDO = new DmAuthRoleDO();
        roleDO.setOwnerUid(puid);
        roleDO.setRoleName(fo.getRoleName());
        roleDO.setAliasName(fo.getRoleName());
        roleDO.setInnerTag(false);
        roleDO.setRoleAuthLabels(this.authLabelService.normalizeRoleAuthLabels(fo.getAuthLabelList()));

        int insert = this.authDal.roleMapper().insert(roleDO);
        return new AddRoleMO(insert != 0, roleDO.getId());
    }

    @Override
    public ResWebData<Boolean> deleteRole(String puid, DeleteRoleFO fo) {
        DmAuthRoleDO roleDO = this.authDal.roleMapper().selectById(fo.getRoleId());
        if (roleDO == null) {
            return ResWebDataUtils.buildSuccess(true);
        }

        if (!roleDO.getOwnerUid().equals(puid)) {
            return ResWebDataUtils.buildError(DmI18nUtils.getMessage(I18nRdpMsgKeys.ROLE_DELETE_IS_NOT_BELONG_YOU_ERROR.name()));
        }

        if (roleDO.isInnerTag()) {
            return ResWebDataUtils.buildError(DmI18nUtils.getMessage(I18nRdpMsgKeys.ROLE_DELETE_IS_INNER_ERROR.name(), roleDO.getAliasName()));
        }

        List<DmAuthUserDO> userDOs = this.authDal.userMapper().listByRoleId(fo.getRoleId());
        if (userDOs != null && !userDOs.isEmpty()) {
            return ResWebDataUtils.buildError(DmI18nUtils.getMessage(I18nRdpMsgKeys.ROLE_DELETE_HAVE_USING.name(), roleDO.getAliasName()));
        }

        int delete = this.authDal.roleMapper().deleteById(fo.getRoleId());
        return ResWebDataUtils.buildSuccess(delete != 0);
    }

    @Override
    public ResWebData<Boolean> updateRole(String puid, UpdateRoleFO fo) {
        if (StringUtils.isBlank(fo.getRoleName())) {
            return ResWebDataUtils.buildError(DmI18nUtils.getMessage(I18nRdpMsgKeys.ROLE_UPDATE_NAME_IS_EMPTY_ERROR.name()));
        }

        if (this.innerRoleInfoDef.containsKey(fo.getRoleName())) {
            String avoid = StringUtils.join(this.innerRoleInfoDef.keySet().toArray(), ", ");
            return ResWebDataUtils.buildError(DmI18nUtils.getMessage(I18nRdpMsgKeys.ROLE_UPDATE_NAME_IS_INNER_ERROR.name(), fo.getRoleName(), avoid));
        }

        DmAuthRoleDO roleDO = this.authDal.roleMapper().selectById(fo.getRoleId());
        if (roleDO == null) {
            return ResWebDataUtils.buildError(DmI18nUtils.getMessage(I18nRdpMsgKeys.ROLE_UPDATE_NOT_EXIST.name()));
        }

        if (!roleDO.getOwnerUid().equals(puid)) {
            return ResWebDataUtils.buildError(DmI18nUtils.getMessage(I18nRdpMsgKeys.ROLE_UPDATE_IS_NOT_BELONG_YOU_ERROR.name()));
        }

        if (roleDO.isInnerTag()) {
            return ResWebDataUtils.buildError(DmI18nUtils.getMessage(I18nRdpMsgKeys.ROLE_UPDATE_IS_INNER_ERROR.name(), roleDO.getAliasName()));
        }

        Set<String> keepLabel = new HashSet<>(this.authLabelService.normalizeRoleAuthLabels(fo.getAuthLabelList()));

        // find all need remove
        List<AuthInfo> allLabel = this.authLabelService.getRoleAuthLabel();
        List<String> removeLabel = allLabel.stream().filter(a -> a.getAuthType() == AuthInfoType.Auth).map(AuthInfo::getKey).collect(Collectors.toList());
        removeLabel.removeAll(keepLabel);

        // just remove need remove, keep unknown.
        Set<String> finalLabel = new HashSet<>(roleDO.getRoleAuthLabels());
        finalLabel.removeAll(removeLabel);
        finalLabel.removeIf(label -> {
            AuthInfo authInfo = this.authLabelService.getAuthLabel(label);
            return authInfo != null && authInfo.getAuthType() == AuthInfoType.Category;
        });
        finalLabel.addAll(keepLabel);

        String labelJson = JsonUtils.toJson(finalLabel);
        int update = this.authDal.roleMapper().updateRole(roleDO.getId(), fo.getRoleName(), labelJson);
        return ResWebDataUtils.buildSuccess(update != 0);
    }

    @Transactional(rollbackFor = Throwable.class, propagation = Propagation.REQUIRED)
    @Override
    public void repairRoleForUser(String uid) {
        DmAuthUserDO user = this.authDal.userMapper().queryByUid(uid);
        if (user.getAccountType() != AccountType.PRIMARY_ACCOUNT) {
            return;
        }

        List<DmAuthRoleDO> roles = this.authDal.roleMapper().queryByOwnerUid(uid);
        Map<String, DmAuthRoleDO> roleMap = new HashMap<>();

        if (CollectionUtils.isNotEmpty(roles)) {
            for (DmAuthRoleDO role : roles) {
                if (role.isInnerTag()) {
                    roleMap.put(role.getRoleName(), role);
                }
            }
        }

        for (String roleName : this.innerRoleInfoDef.keySet()) {
            if (CollectionUtils.isEmpty(this.rdpConfig.getInnerRoles()) || !this.rdpConfig.getInnerRoles().contains(roleName)) {
                continue;
            }

            Set<String> authLabels = this.innerRoleInfoDef.get(roleName);

            if (roleMap.containsKey(roleName)) {
                // do update
                DmAuthRoleDO innerRoleDO = roleMap.get(roleName);
                String oldLabelJson = JsonUtils.toJson(innerRoleDO.getRoleAuthLabels());
                String newLabelJson = JsonUtils.toJson(authLabels);
                if (!StringUtils.equals(oldLabelJson, newLabelJson)) {
                    this.authDal.roleMapper().updateRole(innerRoleDO.getId(), innerRoleDO.getRoleName(), newLabelJson);
                }
            } else {
                // do insert
                DmAuthRoleDO innerRoleDO = new DmAuthRoleDO();
                innerRoleDO.setOwnerUid(uid);
                innerRoleDO.setRoleName(roleName);
                innerRoleDO.setAliasName(DmI18nUtils.getMessage(roleName));
                innerRoleDO.setInnerTag(true);
                innerRoleDO.setRoleAuthLabels(new ArrayList<>(authLabels));
                roleMap.put(innerRoleDO.getRoleName(), innerRoleDO);
                this.authDal.roleMapper().insert(innerRoleDO);
            }

        }

        DmAuthRoleDO adminRole = roleMap.get(SecSysRole.ADMIN_ROLE_NAME);
        if (!Objects.equals(user.getRoleId(), adminRole.getId())) {
            this.authDal.userMapper().updateRoleById(user.getId(), adminRole.getId());
        }
    }
}
