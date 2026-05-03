package com.clougence.rdp.controller;

import static com.clougence.clouddm.sdk.security.auth.def.SecRoleAuthLabel.RDP_ROLE_MANAGE;
import static com.clougence.clouddm.sdk.security.auth.def.SecRoleAuthLabel.RDP_ROLE_READ;
import static com.clougence.rdp.constant.auth.RequestAuth.AuthStrategy.Ignore;

import java.util.List;
import java.util.stream.Collectors;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.clougence.clouddm.api.common.rpc.ResWebData;
import com.clougence.clouddm.api.common.rpc.ResWebDataUtils;
import com.clougence.clouddm.base.metadata.rdp.enumeration.ResourceType;
import com.clougence.rdp.constant.I18nRdpMsgKeys;
import com.clougence.rdp.constant.auth.RequestAuth;
import com.clougence.rdp.constant.auth.SecurityLevel;
import com.clougence.rdp.constant.operation.AuditType;
import com.clougence.rdp.controller.model.fo.role.CreateRoleFO;
import com.clougence.rdp.controller.model.fo.role.DeleteRoleFO;
import com.clougence.rdp.controller.model.fo.role.FetchRoleFO;
import com.clougence.rdp.controller.model.fo.role.UpdateRoleFO;
import com.clougence.rdp.controller.model.http.RdpControllerUrlPrefix;
import com.clougence.rdp.controller.model.vo.RoleAuthTreeVO;
import com.clougence.rdp.controller.model.vo.RoleVO;
import com.clougence.rdp.dal.model.RdpRoleDO;
import com.clougence.clouddm.sdk.security.auth.AuthInfo;
import com.clougence.rdp.service.RdpAuthServiceForManage;
import com.clougence.rdp.service.RdpOpAuditService;
import com.clougence.rdp.service.RdpRoleService;
import com.clougence.rdp.service.RdpUserService;
import com.clougence.rdp.service.model.AddRoleMO;
import com.clougence.rdp.util.RdpConvertUtils;
import com.clougence.rdp.util.RdpI18nUtils;
import com.clougence.utils.StringUtils;

import lombok.extern.slf4j.Slf4j;

/**
 * @author wanshao create time is 2021/1/5
 **/
@RestController
@RequestMapping(value = RdpControllerUrlPrefix.CONSOLE_PREFIX + "/role")
@Slf4j
public class RdpRoleController {

    @Resource
    private RdpAuthServiceForManage rdpDsAuthManagerService;
    @Resource
    private RdpRoleService          rdpRoleService;
    @Resource
    private RdpOpAuditService       rdpOpAuditService;

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

        List<RdpRoleDO> roles = this.rdpRoleService.listRoleByUID(puid);
        List<RoleVO> vos = roles.stream().map(roleDO -> {
            return RdpConvertUtils.convertToRoleVO(roleDO, labels);
        }).collect(Collectors.toList());
        return ResWebDataUtils.buildSuccess(vos);
    }

    @RequestAuth(RDP_ROLE_READ)
    @RequestMapping(value = "/fetchRole", method = { RequestMethod.POST })
    public ResWebData<?> fetchRole(@Valid @RequestBody FetchRoleFO fo, HttpServletRequest request) {
        String puid = (String) request.getAttribute(RdpUserService.PUID);

        RdpRoleDO role = this.rdpRoleService.fetchRoleById(fo.getRoleId());
        if (StringUtils.equals(role.getOwnerUid(), puid)) {
            List<AuthInfo> allLabel = this.rdpDsAuthManagerService.getRoleAuthLabel();
            List<String> labels = allLabel.stream().map(AuthInfo::getKey).collect(Collectors.toList());
            return ResWebDataUtils.buildSuccess(RdpConvertUtils.convertToRoleVO(role, labels));
        } else {
            return ResWebDataUtils.buildError(RdpI18nUtils.getMessage(I18nRdpMsgKeys.USER_ROLE_NOT_EXIST_ERROR.name()));
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
        RdpRoleDO rdpRoleDO = rdpRoleService.fetchRoleById(fo.getRoleId());
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
