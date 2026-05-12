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

import static com.clougence.clouddm.console.web.global.jwtsession.SecurityLevel.HIGH;
import static com.clougence.clouddm.sdk.security.auth.def.SecRoleAuthLabel.*;

import java.util.List;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.clougence.clouddm.api.common.rpc.ResWebData;
import com.clougence.clouddm.api.common.rpc.ResWebDataUtils;
import com.clougence.clouddm.base.metadata.rdp.enumeration.ResourceType;
import com.clougence.clouddm.console.web.dal.enumeration.AccountType;
import com.clougence.clouddm.console.web.global.config.DmConsoleConfig;
import com.clougence.clouddm.console.web.global.jwtsession.RequestAuth;
import com.clougence.clouddm.console.web.global.jwtsession.SecurityLevel;
import com.clougence.clouddm.console.web.model.fo.ResetPasswdFO;
import com.clougence.clouddm.console.web.model.fo.UpdateResourceManageFO;
import com.clougence.clouddm.console.web.model.fo.role.UpdateUserRoleFO;
import com.clougence.clouddm.console.web.model.fo.user.*;
import com.clougence.clouddm.console.web.model.lo.UpdateUserRoleLO;
import com.clougence.clouddm.console.web.model.vo.ListUserVO;
import com.clougence.clouddm.console.web.util.DmI18nUtils;
import com.clougence.clouddm.console.web.util.Sm2Utils;
import com.clougence.rdp.constant.I18nRdpMsgKeys;
import com.clougence.rdp.constant.RdpControllerUrlPrefix;
import com.clougence.rdp.constant.RdpErrorCode;
import com.clougence.rdp.constant.operation.AuditType;
import com.clougence.clouddm.console.web.dal.mapper.RdpUserMapper;
import com.clougence.clouddm.console.web.dal.model.RdpUserDO;
import com.clougence.clouddm.console.web.dal.model.RdpUserKvBaseConfigDO;
import com.clougence.rdp.global.config.user.UserDefinedConfig;
import com.clougence.rdp.global.exception.ErrorMessageException;
import com.clougence.rdp.service.RdpAuthServiceForBiz;
import com.clougence.rdp.service.RdpOpAuditService;
import com.clougence.rdp.service.RdpUserConfigService;
import com.clougence.rdp.service.RdpUserService;
import com.clougence.rdp.service.model.AddSubAccountMO;
import com.clougence.rdp.service.model.CheckSubAccountMO;
import com.clougence.rdp.service.model.UpdateUserInfoMO;
import com.clougence.rdp.service.model.ValidateResultMO;
import com.clougence.utils.StringUtils;

import jakarta.annotation.Resource;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

/**
 * @author wanshao create time is 2020/3/11
 **/
@RestController
@RequestMapping(value = RdpControllerUrlPrefix.CONSOLE_PREFIX + "/user/manager")
@Slf4j
public class RdpUserManagerController {

    @Resource
    private RdpUserService       rdpUserService;

    @Resource
    private RdpAuthServiceForBiz rdpAuthServiceForBiz;

    @Resource
    private RdpUserMapper        rdpUserMapper;

    @Resource
    private RdpUserConfigService rdpUserConfigService;

    @Resource
    private DmConsoleConfig      rdpConfig;

    @Resource
    private RdpOpAuditService    rdpOpAuditService;

    @RequestAuth(level = HIGH, value = RDP_USER_MANAGE)
    @RequestMapping(value = "/resetpasswd", method = { RequestMethod.POST })
    public ResWebData<?> resetPasswd(@Valid @RequestBody ResetPasswdFO fo, HttpServletRequest request) {
        String uid = (String) request.getAttribute(RdpUserService.UID);

        //decrypt
        fo.setPassword(Sm2Utils.decrypt(rdpConfig.getPrivateKey(), fo.getPassword()));

        RdpUserDO userDO = null;
        ValidateResultMO validatePwdMO = null;
        if (fo.getAccountType() == AccountType.PRIMARY_ACCOUNT) {
            userDO = this.rdpUserMapper.queryPrimaryByPhone(fo.getPhone());
            validatePwdMO = this.rdpUserService.validatePrimaryAccountPwd(fo.getPassword());
        } else if (fo.getAccountType() == AccountType.SUB_ACCOUNT) {
            if (StringUtils.isBlank(fo.getSubAccount())) {
                return ResWebDataUtils.buildError(DmI18nUtils.getMessage(I18nRdpMsgKeys.USER_ACCOUNT_EMPTY_ERROR.name()));
            }

            userDO = this.rdpUserMapper.queryBySubAccount(fo.getSubAccount());

            String puid = (String) request.getAttribute(RdpUserService.PUID);
            validatePwdMO = this.rdpUserService.validateSubAccountPwd(puid, fo.getPassword());
        }

        if (userDO == null) {
            return ResWebDataUtils.buildError(DmI18nUtils.getMessage(I18nRdpMsgKeys.USER_NOT_EXIST_ERROR.name()));
        }

        if (validatePwdMO != null && !validatePwdMO.isSuccess()) {
            return ResWebDataUtils.buildError(validatePwdMO.getErrorMsg());
        }

        checkOperateUserAuth(uid, userDO.getUid());

        UpdateUserInfoMO resetPasswdMO = this.rdpUserService.resetPassword(fo);
        if (resetPasswdMO.isSuccess()) {
            return ResWebDataUtils.buildSuccess();
        } else {
            return ResWebDataUtils.buildError(resetPasswdMO.getErrorMsg());
        }
    }

    @RequestAuth(level = HIGH, value = RDP_USER_READ)
    @RequestMapping(value = "/listsubaccounts", method = { RequestMethod.POST })
    public ResWebData<?> listSubAccounts(@Valid @RequestBody ListSubAccountsFO fo, HttpServletRequest request) {
        String puid = (String) request.getAttribute(RdpUserService.PUID);
        List<ListUserVO> users = this.rdpUserService.listSubAccounts(puid, fo);
        return ResWebDataUtils.buildSuccess(users);
    }

    @RequestAuth(level = HIGH, value = RDP_USER_MANAGE)
    @RequestMapping(value = "/ctrl_addsubaccount", method = RequestMethod.POST)
    public ResWebData<?> ctrlAddSubAccount() {
        return ResWebDataUtils.buildSuccess();
    }

    @RequestAuth(level = HIGH, value = RDP_USER_MANAGE)
    @RequestMapping(value = "/addsubaccount", method = { RequestMethod.POST })
    public ResWebData<?> addSubAccount(@Valid @RequestBody AddSubAccountFO fo, HttpServletRequest request) {
        String puid = (String) request.getAttribute(RdpUserService.PUID);
        String uid = (String) request.getAttribute(RdpUserService.UID);

        //decrypt
        fo.setPassword(Sm2Utils.decrypt(rdpConfig.getPrivateKey(), fo.getPassword()));

        AddSubAccountMO accountMO = this.rdpUserService.addSubAccountForInternal(puid, fo);
        if (accountMO.isSuccess()) {
            rdpOpAuditService.logAndAddOperationAudit(puid, uid, request.getRequestURI(), request.getRemoteAddr(), accountMO
                .getSubUid(), fo, SecurityLevel.HIGH, AuditType.ADD_SUB_ACCOUNT, ResourceType.ACCOUNT);
            return ResWebDataUtils.buildSuccess();
        } else {
            return ResWebDataUtils.buildError(accountMO.getErrorMsg());
        }
    }

    @RequestAuth(level = HIGH, value = RDP_USER_MANAGE)
    @RequestMapping(value = "/updatesubaccount", method = { RequestMethod.POST })
    public ResWebData<?> updateSubAccount(@Valid @RequestBody UpdateSubAccountFO fo, HttpServletRequest request) {
        String puid = (String) request.getAttribute(RdpUserService.PUID);
        String uid = (String) request.getAttribute(RdpUserService.UID);

        checkOperateUserAuth(uid, fo.getTargetUid());

        UpdateUserInfoMO accountMO = this.rdpUserService.updateSubAccount(fo, puid);
        if (accountMO.isSuccess()) {
            rdpOpAuditService.logAndAddOperationAudit(puid, uid, request.getRequestURI(), request.getRemoteAddr(), fo.getTargetUid(), accountMO
                .getConfigLO(), SecurityLevel.HIGH, AuditType.UPDATE_SUB_ACCOUNT, ResourceType.ACCOUNT);
            return ResWebDataUtils.buildSuccess();
        } else {
            return ResWebDataUtils.buildError(accountMO.getErrorMsg());
        }
    }

    @RequestAuth(level = HIGH, value = RDP_USER_MANAGE)
    @RequestMapping(value = "/checksubaccountduplicate", method = { RequestMethod.POST })
    public ResWebData<?> checkSubAccountDuplicate(@Valid @RequestBody CheckSubAccountFO fo, HttpServletRequest request) {
        String puid = (String) request.getAttribute(RdpUserService.PUID);

        CheckSubAccountMO re = this.rdpUserService.checkSubAccount(puid, fo);
        if (re.isSuccess()) {
            return ResWebDataUtils.buildSuccess();
        } else {
            return ResWebDataUtils.buildError(re.getErrorMsg());
        }
    }

    @RequestAuth(level = HIGH, value = RDP_USER_MANAGE)
    @RequestMapping(value = "/deletesubaccount", method = { RequestMethod.POST })
    public ResWebData<?> deleteSubAccount(@Valid @RequestBody DeleteSubAccountFO fo, HttpServletRequest request) {
        String puid = (String) request.getAttribute(RdpUserService.PUID);
        String uid = (String) request.getAttribute(RdpUserService.UID);

        if (StringUtils.isBlank(fo.getSubAccount())) {
            return ResWebDataUtils.buildError(DmI18nUtils.getMessage(I18nRdpMsgKeys.USER_ACCOUNT_EMPTY_ERROR.name()));
        }

        RdpUserDO userDO = this.rdpUserMapper.queryBySubAccount(fo.getSubAccount());

        if (userDO == null) {
            return ResWebDataUtils.buildError(DmI18nUtils.getMessage(I18nRdpMsgKeys.USER_NOT_EXIST_ERROR.name()));
        }

        if (userDO.getUid().equals(uid)) {
            return ResWebDataUtils.buildError(DmI18nUtils.getMessage(I18nRdpMsgKeys.CAN_NOT_DELETE_SUB_ACCOUNT_SELF.name()));
        }

        rdpAuthServiceForBiz.checkOperateOtherUserAuth(uid, userDO.getUid());

        RdpUserKvBaseConfigDO configDO = rdpUserConfigService.getSpecifiedConfig(puid, UserDefinedConfig.Fields.forbidDelSubAccount);
        if (configDO != null) {
            boolean forbid = Boolean.parseBoolean(configDO.getConfigValue());
            if (forbid) {
                return ResWebDataUtils.buildError(DmI18nUtils.getMessage(I18nRdpMsgKeys.NOT_ALLOW_DELETE_SUB_ACCOUNT.name()));
            }
        }
        RdpUserDO rdpUserDO = rdpUserMapper.queryBySubAccount(fo.getSubAccount());
        ResWebData<Boolean> resWebData = this.rdpUserService.deleteSubAccount(puid, fo);

        if (resWebData.isSuccess()) {
            rdpOpAuditService.logAndAddOperationAudit(puid, uid, request.getRequestURI(), request.getRemoteAddr(), userDO
                .getUid(), fo, SecurityLevel.HIGH, AuditType.DELETE_SUB_ACCOUNT, ResourceType.ACCOUNT, rdpUserDO.getUsername());
        }

        return resWebData;
    }

    @RequestAuth(level = HIGH, value = RDP_USER_MANAGE)
    @RequestMapping(value = "/updateuserrole", method = RequestMethod.POST)
    public ResWebData<?> updateUserRole(@Valid @RequestBody UpdateUserRoleFO fo, HttpServletRequest request, HttpServletResponse response) {
        String uid = (String) request.getAttribute(RdpUserService.UID);
        String puid = (String) request.getAttribute(RdpUserService.PUID);

        checkOperateUserAuth(uid, fo.getSubAccountUid());
        UpdateUserRoleLO lo = this.rdpUserService.updateUserRole(fo);
        rdpOpAuditService.logAndAddOperationAudit(puid, uid, request.getRequestURI(), request.getRemoteAddr(), fo
            .getSubAccountUid(), lo, SecurityLevel.HIGH, AuditType.UPDATE_SUB_ACCOUNT_ROLE, ResourceType.ACCOUNT);

        if (StringUtils.equals(uid, fo.getSubAccountUid())) {
            Cookie cookie = new Cookie("jwt_token", StringUtils.EMPTY);
            cookie.setHttpOnly(true);
            cookie.setMaxAge(0);
            cookie.setPath("/");

            if (StringUtils.isNotBlank(rdpConfig.getLoginCookieDomain())) {
                cookie.setDomain(rdpConfig.getLoginCookieDomain());
            }

            response.addCookie(cookie);
            return ResWebDataUtils.buildError(RdpErrorCode.COMM_USER_RELOAD_ERROR, DmI18nUtils.getMessage(I18nRdpMsgKeys.USER_NEED_RELOGIN.name()));
        }

        return ResWebDataUtils.buildSuccess();
    }

    @RequestAuth(level = HIGH, value = RDP_USER_MANAGE)
    @RequestMapping(value = "/updateaccountability", method = RequestMethod.POST)
    public ResWebData<?> updateAccountAbility(@Valid @RequestBody AccountAbilityFO fo, HttpServletRequest request) {
        String uid = (String) request.getAttribute(RdpUserService.UID);
        String puid = (String) request.getAttribute(RdpUserService.PUID);
        if (uid.equals(fo.getUid())) {
            return ResWebDataUtils.buildError(DmI18nUtils.getMessage(I18nRdpMsgKeys.CAN_NOT_DISABLE_SUB_ACCOUNT_SELF.name()));
        }

        checkOperateUserAuth(uid, fo.getUid());
        ResWebData<Boolean> resWebData = this.rdpUserService.updateAccountAbility(puid, fo);
        if (resWebData.isSuccess()) {
            if (fo.getDisable()) {
                rdpOpAuditService.logAndAddOperationAudit(puid, uid, request.getRequestURI(), request.getRemoteAddr(), fo
                    .getUid(), fo, SecurityLevel.HIGH, AuditType.DISABLE_SUB_ACCOUNT, ResourceType.ACCOUNT);
            } else {
                rdpOpAuditService.logAndAddOperationAudit(puid, uid, request.getRequestURI(), request.getRemoteAddr(), fo
                    .getUid(), fo, SecurityLevel.HIGH, AuditType.ENABLE_SUB_ACCOUNT, ResourceType.ACCOUNT);
            }
        }
        return resWebData;
    }

    @RequestAuth(level = HIGH, value = RDP_AUTH_MANAGE)
    @RequestMapping(value = "/updateresourcemanage", method = RequestMethod.POST)
    public ResWebData<?> updateResourceManage(@Valid @RequestBody UpdateResourceManageFO fo, HttpServletRequest request) {
        String uid = (String) request.getAttribute(RdpUserService.UID);
        String puid = (String) request.getAttribute(RdpUserService.PUID);

        UpdateUserInfoMO mo = this.rdpUserService.updateResourceManage(fo, puid);
        if (mo.isSuccess()) {
            rdpOpAuditService.logAndAddOperationAudit(puid, uid, request.getRequestURI(), request.getRemoteAddr(), fo
                .getTargetUid(), fo, HIGH, AuditType.UPDATE_SUB_ACCOUNT_ROLE, ResourceType.ACCOUNT);
            return ResWebDataUtils.buildSuccess();
        } else {
            return ResWebDataUtils.buildError(mo.getErrorMsg());
        }

    }

    private void checkOperateUserAuth(String operateUid, String uid) {
        if (StringUtils.isBlank(uid)) {
            throw new RuntimeException("uid can not be blank.");
        }

        RdpUserDO userDO = this.rdpUserMapper.queryByUid(uid);

        if (userDO == null) {
            throw new ErrorMessageException(DmI18nUtils.getMessage(I18nRdpMsgKeys.USER_NOT_EXIST_ERROR.name()));
        }

        rdpAuthServiceForBiz.checkOperateOtherUserAuth(operateUid, userDO.getUid());
    }
}
