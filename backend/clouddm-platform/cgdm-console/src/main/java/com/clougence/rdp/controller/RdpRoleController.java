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
package com.clougence.rdp.controller;

import static com.clougence.clouddm.console.web.global.jwtsession.RequestAuth.AuthStrategy.Ignore;
import static com.clougence.clouddm.sdk.security.auth.def.SecRoleAuthLabel.RDP_ROLE_MANAGE;
import static com.clougence.clouddm.sdk.security.auth.def.SecRoleAuthLabel.RDP_ROLE_READ;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.clougence.clouddm.api.common.rpc.ResWebData;
import com.clougence.clouddm.api.common.rpc.ResWebDataUtils;
import com.clougence.clouddm.base.metadata.rdp.enumeration.ResourceType;
import com.clougence.clouddm.console.web.component.auth.DmAuthServiceForManage;
import com.clougence.clouddm.console.web.global.i18n.DmI18nUtils;
import com.clougence.clouddm.console.web.global.i18n.I18nRdpMsgKeys;
import com.clougence.clouddm.console.web.global.jwtsession.RequestAuth;
import com.clougence.clouddm.console.web.model.fo.role.CreateRoleFO;
import com.clougence.clouddm.console.web.model.fo.role.DeleteRoleFO;
import com.clougence.clouddm.console.web.model.fo.role.FetchRoleFO;
import com.clougence.clouddm.console.web.model.fo.role.UpdateRoleFO;
import com.clougence.clouddm.console.web.model.vo.role.RoleAuthTreeVO;
import com.clougence.clouddm.console.web.model.vo.role.RoleVO;
import com.clougence.clouddm.console.web.service.auth.RdpRoleService;
import com.clougence.clouddm.console.web.service.auth.RdpUserService;
import com.clougence.clouddm.console.web.util.RdpConvertUtils;
import com.clougence.clouddm.platform.dal.model.auth.DmAuthRoleDO;
import com.clougence.clouddm.platform.dal.model.monitor.AuditType;
import com.clougence.clouddm.platform.dal.model.monitor.SecurityLevel;
import com.clougence.clouddm.sdk.security.auth.AuthInfo;
import com.clougence.rdp.constant.RdpControllerUrlPrefix;
import com.clougence.rdp.service.RdpOpAuditService;
import com.clougence.rdp.service.model.AddRoleMO;
import com.clougence.utils.StringUtils;

import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

/**
 * @author wanshao create time is 2021/1/5
 **/
@RestController
@RequestMapping(value = RdpControllerUrlPrefix.CONSOLE_PREFIX + "/role")
@Slf4j
public class RdpRoleController {

    @Resource
    private DmAuthServiceForManage rdpDsAuthManagerService;
    @Resource
    private RdpRoleService         rdpRoleService;
    @Resource
    private RdpOpAuditService      rdpOpAuditService;

    @RequestAuth(strategy = Ignore)
    @RequestMapping(value = "/listRoleAuthLabelTree", method = { RequestMethod.POST })
    public ResWebData<?> listRoleAuthLabelTree() {
        List<AuthInfo> allCategory = this.rdpDsAuthManagerService.getAllCategory();
        List<AuthInfo> allLabel = this.rdpDsAuthManagerService.getRoleAuthLabel();

        List<RoleAuthTreeVO> treeList = RdpConvertUtils.convertToCtrlAuthTree(allCategory, allLabel);
        return ResWebDataUtils.buildSuccess(treeList);
    }

    @RequestAuth(RDP_ROLE_READ)
    @RequestMapping(value = "/listRole", method = { RequestMethod.POST })
    public ResWebData<?> listRole(HttpServletRequest request) {
        String puid = (String) request.getAttribute(RdpUserService.PUID);

        List<AuthInfo> allLabel = this.rdpDsAuthManagerService.getRoleAuthLabel();
        List<String> labels = allLabel.stream().map(AuthInfo::getKey).collect(Collectors.toList());

        List<DmAuthRoleDO> roles = this.rdpRoleService.listRoleByUID(puid);
        List<RoleVO> vos = roles.stream().map(roleDO -> {
            return RdpConvertUtils.convertToRoleVO(roleDO, labels);
        }).collect(Collectors.toList());
        return ResWebDataUtils.buildSuccess(vos);
    }

    @RequestAuth(RDP_ROLE_READ)
    @RequestMapping(value = "/fetchRole", method = { RequestMethod.POST })
    public ResWebData<?> fetchRole(@Valid @RequestBody FetchRoleFO fo, HttpServletRequest request) {
        String puid = (String) request.getAttribute(RdpUserService.PUID);

        DmAuthRoleDO role = this.rdpRoleService.fetchRoleById(fo.getRoleId());
        if (StringUtils.equals(role.getOwnerUid(), puid)) {
            List<AuthInfo> allLabel = this.rdpDsAuthManagerService.getRoleAuthLabel();
            List<String> labels = allLabel.stream().map(AuthInfo::getKey).collect(Collectors.toList());
            return ResWebDataUtils.buildSuccess(RdpConvertUtils.convertToRoleVO(role, labels));
        } else {
            return ResWebDataUtils.buildError(DmI18nUtils.getMessage(I18nRdpMsgKeys.USER_ROLE_NOT_EXIST_ERROR.name()));
        }
    }

    @RequestAuth(level = SecurityLevel.HIGH, value = RDP_ROLE_MANAGE)
    @RequestMapping(value = "/createRole", method = { RequestMethod.POST })
    public ResWebData<?> createRole(@Valid @RequestBody CreateRoleFO fo, HttpServletRequest request) {
        String puid = (String) request.getAttribute(RdpUserService.PUID);
        String uid = (String) request.getAttribute(RdpUserService.UID);
        AddRoleMO addRoleMO = this.rdpRoleService.createRole(puid, fo);
        if (addRoleMO.isSuccess()) {
            rdpOpAuditService.logAndAddOperationAudit(puid, uid, request.getRequestURI(), request.getRemoteAddr(), addRoleMO
                .getRoleUid(), fo, SecurityLevel.HIGH, AuditType.CREATE_ROLE, ResourceType.ROLE);
            return ResWebDataUtils.buildSuccess();
        }
        return ResWebDataUtils.buildError(addRoleMO.getErrorMsg());
    }

    @RequestAuth(level = SecurityLevel.HIGH, value = RDP_ROLE_MANAGE)
    @RequestMapping(value = "/deleteRole", method = { RequestMethod.POST })
    public ResWebData<?> deleteRole(@Valid @RequestBody DeleteRoleFO fo, HttpServletRequest request) {
        String puid = (String) request.getAttribute(RdpUserService.PUID);
        String uid = (String) request.getAttribute(RdpUserService.UID);
        DmAuthRoleDO rdpRoleDO = rdpRoleService.fetchRoleById(fo.getRoleId());
        ResWebData<Boolean> resWebData = this.rdpRoleService.deleteRole(puid, fo);
        if (resWebData.isSuccess()) {
            rdpOpAuditService.logAndAddOperationAudit(puid, uid, request.getRequestURI(), request.getRemoteAddr(), fo
                .getRoleId(), fo, SecurityLevel.HIGH, AuditType.DELETE_ROLE, ResourceType.ROLE, rdpRoleDO.getRoleName());
        }
        return resWebData;
    }

    @RequestAuth(level = SecurityLevel.HIGH, value = RDP_ROLE_MANAGE)
    @RequestMapping(value = "/updateRole", method = { RequestMethod.POST })
    public ResWebData<?> updateRole(@Valid @RequestBody UpdateRoleFO fo, HttpServletRequest request) {
        String puid = (String) request.getAttribute(RdpUserService.PUID);
        String uid = (String) request.getAttribute(RdpUserService.UID);
        ResWebData<Boolean> resWebData = this.rdpRoleService.updateRole(puid, fo);
        if (resWebData.isSuccess()) {
            rdpOpAuditService.logAndAddOperationAudit(puid, uid, request.getRequestURI(), request.getRemoteAddr(), fo
                .getRoleId(), fo, SecurityLevel.HIGH, AuditType.UPDATE_ROLE, ResourceType.ROLE);
        }
        return resWebData;
    }
}
