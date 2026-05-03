package com.clougence.rdp.service.impl;

import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.clougence.clouddm.api.common.boot.UnifiedPostConstruct;
import com.clougence.clouddm.api.common.boot.UnifiedPostConstructOrder;
import com.clougence.clouddm.api.common.rpc.ResWebData;
import com.clougence.clouddm.api.common.rpc.ResWebDataUtils;
import com.clougence.clouddm.platform.plugin.PluginManager;
import com.clougence.clouddm.sdk.security.auth.AuthInfo;
import com.clougence.clouddm.sdk.security.auth.AuthInfoSpi;
import com.clougence.clouddm.sdk.security.auth.AuthInfoType;
import com.clougence.clouddm.sdk.security.auth.def.SecSysRole;
import com.clougence.rdp.constant.I18nRdpMsgKeys;
import com.clougence.rdp.controller.model.fo.role.CreateRoleFO;
import com.clougence.rdp.controller.model.fo.role.DeleteRoleFO;
import com.clougence.rdp.controller.model.fo.role.UpdateRoleFO;
import com.clougence.rdp.dal.enumeration.AccountType;
import com.clougence.rdp.dal.mapper.RdpRoleMapper;
import com.clougence.rdp.dal.mapper.RdpUserMapper;
import com.clougence.rdp.dal.model.RdpRoleDO;
import com.clougence.rdp.dal.model.RdpUserDO;
import com.clougence.rdp.global.config.RdpConsoleConfig;
import com.clougence.rdp.service.RdpAuthServiceForManage;
import com.clougence.rdp.service.RdpRoleService;
import com.clougence.rdp.service.model.AddRoleMO;
import com.clougence.rdp.util.RdpI18nUtils;
import com.clougence.utils.CollectionUtils;
import com.clougence.utils.JsonUtils;
import com.clougence.utils.StringUtils;

import lombok.extern.slf4j.Slf4j;

/**
 * @author bucketli 2021/1/9 12:22
 */
@Slf4j
@Service
@UnifiedPostConstructOrder(1)
public class RdpRoleServiceImpl implements RdpRoleService, UnifiedPostConstruct {

    @Resource
    private RdpConsoleConfig               rdpConfig;
    @Resource
    private RdpRoleMapper                  rdpRoleMapper;
    @Resource
    private RdpUserMapper                  rdpUserMapper;
    @Resource
    private RdpAuthServiceForManage        rdpDsAuthManagerService;

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
            this.rdpRoleMapper.updateInnerRoleByName(roleName, labelJson);
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
    public List<RdpRoleDO> listRoleByUID(String puid) {
        List<RdpRoleDO> roles = this.rdpRoleMapper.queryByOwnerUid(puid);
        roles.forEach(roleDO -> {
            if (roleDO.isInnerTag()) {
                roleDO.setAliasName(RdpI18nUtils.getMessage(roleDO.getRoleName()));
            }
        });
        return roles;
    }

    @Override
    public List<RdpRoleDO> listRoleExcludeByName(String puid, List<String> name) {
        List<RdpRoleDO> roles = this.rdpRoleMapper.queryByOwnerUid(puid);

        roles = roles.stream().filter(p -> !name.contains(p.getRoleName())).collect(Collectors.toList());

        roles.forEach(roleDO -> {
            if (roleDO.isInnerTag()) {
                roleDO.setAliasName(RdpI18nUtils.getMessage(roleDO.getRoleName()));
            }
        });
        return roles;
    }

    @Override
    public RdpRoleDO fetchRoleById(long roleId) {
        RdpRoleDO roleDO = this.rdpRoleMapper.selectById(roleId);
        if (roleDO == null) {
            return null;
        }

        if (roleDO.isInnerTag()) {
            roleDO.setAliasName(RdpI18nUtils.getMessage(roleDO.getRoleName()));
        }
        return roleDO;
    }

    @Override
    public AddRoleMO createRole(String puid, CreateRoleFO fo) {
        if (StringUtils.isBlank(fo.getRoleName())) {
            return new AddRoleMO(false, RdpI18nUtils.getMessage(I18nRdpMsgKeys.ROLE_CRATE_NAME_IS_EMPTY_ERROR.name()));
        }

        if (this.innerRoleInfoDef.containsKey(fo.getRoleName())) {
            String avoid = StringUtils.join(this.innerRoleInfoDef.keySet().toArray(), ", ");
            return new AddRoleMO(false, RdpI18nUtils.getMessage(I18nRdpMsgKeys.ROLE_CRATE_NAME_IS_INNER_ERROR.name(), fo.getRoleName(), avoid));
        }

        List<RdpRoleDO> sameRoles = this.rdpRoleMapper.queryByRoleName(puid, fo.getRoleName());
        if (sameRoles != null && !sameRoles.isEmpty()) {
            return new AddRoleMO(false, RdpI18nUtils.getMessage(I18nRdpMsgKeys.ROLE_CRATE_NAME_IS_EXIST_ERROR.name(), fo.getRoleName()));
        }

        RdpRoleDO roleDO = new RdpRoleDO();
        roleDO.setOwnerUid(puid);
        roleDO.setRoleName(fo.getRoleName());
        roleDO.setAliasName(fo.getRoleName());
        roleDO.setInnerTag(false);
        roleDO.setRoleAuthLabels(this.rdpDsAuthManagerService.normalizeRoleAuthLabels(fo.getAuthLabelList()));

        int insert = this.rdpRoleMapper.insert(roleDO);
        return new AddRoleMO(insert != 0, roleDO.getId());
    }

    @Override
    public ResWebData<Boolean> deleteRole(String puid, DeleteRoleFO fo) {
        RdpRoleDO roleDO = this.rdpRoleMapper.selectById(fo.getRoleId());
        if (roleDO == null) {
            return ResWebDataUtils.buildSuccess(true);
        }

        if (!roleDO.getOwnerUid().equals(puid)) {
            return ResWebDataUtils.buildError(RdpI18nUtils.getMessage(I18nRdpMsgKeys.ROLE_DELETE_IS_NOT_BELONG_YOU_ERROR.name()));
        }

        if (roleDO.isInnerTag()) {
            return ResWebDataUtils.buildError(RdpI18nUtils.getMessage(I18nRdpMsgKeys.ROLE_DELETE_IS_INNER_ERROR.name(), roleDO.getAliasName()));
        }

        List<RdpUserDO> userDOs = this.rdpUserMapper.listByRoleId(fo.getRoleId());
        if (userDOs != null && !userDOs.isEmpty()) {
            return ResWebDataUtils.buildError(RdpI18nUtils.getMessage(I18nRdpMsgKeys.ROLE_DELETE_HAVE_USING.name(), roleDO.getAliasName()));
        }

        int delete = this.rdpRoleMapper.deleteById(fo.getRoleId());
        return ResWebDataUtils.buildSuccess(delete != 0);
    }

    @Override
    public ResWebData<Boolean> updateRole(String puid, UpdateRoleFO fo) {
        if (StringUtils.isBlank(fo.getRoleName())) {
            return ResWebDataUtils.buildError(RdpI18nUtils.getMessage(I18nRdpMsgKeys.ROLE_UPDATE_NAME_IS_EMPTY_ERROR.name()));
        }

        if (this.innerRoleInfoDef.containsKey(fo.getRoleName())) {
            String avoid = StringUtils.join(this.innerRoleInfoDef.keySet().toArray(), ", ");
            return ResWebDataUtils.buildError(RdpI18nUtils.getMessage(I18nRdpMsgKeys.ROLE_UPDATE_NAME_IS_INNER_ERROR.name(), fo.getRoleName(), avoid));
        }

        RdpRoleDO roleDO = this.rdpRoleMapper.selectById(fo.getRoleId());
        if (roleDO == null) {
            return ResWebDataUtils.buildError(RdpI18nUtils.getMessage(I18nRdpMsgKeys.ROLE_UPDATE_NOT_EXIST.name()));
        }

        if (!roleDO.getOwnerUid().equals(puid)) {
            return ResWebDataUtils.buildError(RdpI18nUtils.getMessage(I18nRdpMsgKeys.ROLE_UPDATE_IS_NOT_BELONG_YOU_ERROR.name()));
        }

        if (roleDO.isInnerTag()) {
            return ResWebDataUtils.buildError(RdpI18nUtils.getMessage(I18nRdpMsgKeys.ROLE_UPDATE_IS_INNER_ERROR.name(), roleDO.getAliasName()));
        }

        Set<String> keepLabel = new HashSet<>(this.rdpDsAuthManagerService.normalizeRoleAuthLabels(fo.getAuthLabelList()));

        // find all need remove
        List<AuthInfo> allLabel = this.rdpDsAuthManagerService.getRoleAuthLabel();
        List<String> removeLabel = allLabel.stream().filter(a -> a.getAuthType() == AuthInfoType.Auth).map(AuthInfo::getKey).collect(Collectors.toList());
        removeLabel.removeAll(keepLabel);

        // just remove need remove, keep unknown.
        Set<String> finalLabel = new HashSet<>(roleDO.getRoleAuthLabels());
        finalLabel.removeAll(removeLabel);
        finalLabel.removeIf(label -> {
            AuthInfo authInfo = this.rdpDsAuthManagerService.getAuthLabel(label);
            return authInfo != null && authInfo.getAuthType() == AuthInfoType.Category;
        });
        finalLabel.addAll(keepLabel);

        String labelJson = JsonUtils.toJson(finalLabel);
        int update = this.rdpRoleMapper.updateRole(roleDO.getId(), fo.getRoleName(), labelJson);
        return ResWebDataUtils.buildSuccess(update != 0);
    }

    @Transactional(rollbackFor = Throwable.class, propagation = Propagation.REQUIRED)
    @Override
    public void repairRoleForUser(String uid) {
        RdpUserDO user = this.rdpUserMapper.queryByUid(uid);
        if (user.getAccountType() != AccountType.PRIMARY_ACCOUNT) {
            return;
        }

        List<RdpRoleDO> roles = this.rdpRoleMapper.queryByOwnerUid(uid);
        Map<String, RdpRoleDO> roleMap = new HashMap<>();

        if (CollectionUtils.isNotEmpty(roles)) {
            for (RdpRoleDO role : roles) {
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
                RdpRoleDO innerRoleDO = roleMap.get(roleName);
                String oldLabelJson = JsonUtils.toJson(innerRoleDO.getRoleAuthLabels());
                String newLabelJson = JsonUtils.toJson(authLabels);
                if (!StringUtils.equals(oldLabelJson, newLabelJson)) {
                    this.rdpRoleMapper.updateRole(innerRoleDO.getId(), innerRoleDO.getRoleName(), newLabelJson);
                }
            } else {
                // do insert
                RdpRoleDO innerRoleDO = new RdpRoleDO();
                innerRoleDO.setOwnerUid(uid);
                innerRoleDO.setRoleName(roleName);
                innerRoleDO.setAliasName(RdpI18nUtils.getMessage(roleName));
                innerRoleDO.setInnerTag(true);
                innerRoleDO.setRoleAuthLabels(new ArrayList<>(authLabels));
                roleMap.put(innerRoleDO.getRoleName(), innerRoleDO);
                this.rdpRoleMapper.insert(innerRoleDO);
            }

        }

        RdpRoleDO adminRole = roleMap.get(SecSysRole.ADMIN_ROLE_NAME);
        if (!Objects.equals(user.getRoleId(), adminRole.getId())) {
            this.rdpUserMapper.updateRoleById(user.getId(), adminRole.getId());
        }
    }
}
