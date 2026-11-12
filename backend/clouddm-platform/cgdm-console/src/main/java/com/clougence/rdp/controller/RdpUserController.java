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
import static com.clougence.clouddm.console.web.global.jwtsession.RequestAuth.AuthStrategy.RefAnyOnes;
import static com.clougence.clouddm.platform.dal.model.monitor.SecurityLevel.HIGH;
import static com.clougence.clouddm.sdk.security.auth.def.SecRoleAuthLabel.*;

import java.util.*;
import java.util.stream.Collectors;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.clougence.clouddm.console.web.global.session.WebSessionManager;
import com.clougence.clouddm.api.common.rpc.ResWebData;
import com.clougence.clouddm.api.common.rpc.ResWebDataUtils;
import com.clougence.clouddm.console.web.component.auth.DmAuthServiceForBiz;
import com.clougence.clouddm.console.web.component.config.ConsoleConfig;
import com.clougence.clouddm.console.web.constants.CheckSubAccountType;
import com.clougence.clouddm.console.web.constants.DmControllerUrlPrefix;
import com.clougence.clouddm.console.web.global.i18n.DmI18nUtils;
import com.clougence.clouddm.console.web.global.i18n.I18nRdpMsgKeys;
import com.clougence.clouddm.console.web.global.jwtsession.RequestAuth;
import com.clougence.clouddm.console.web.global.session.SessionCookieService;
import com.clougence.clouddm.console.web.global.session.SessionAccountPolicy;
import com.clougence.clouddm.console.web.global.session.SessionPrincipal;
import com.clougence.clouddm.console.web.model.fo.*;
import com.clougence.clouddm.console.web.model.fo.user.CheckSubAccountFO;
import com.clougence.clouddm.console.web.model.fo.user.ResetSubAccountPwdFO;
import com.clougence.clouddm.console.web.model.vo.LoginUserVO;
import com.clougence.clouddm.console.web.model.vo.PwdPolicyVO;
import com.clougence.clouddm.console.web.model.vo.RdpUserAkSkVO;
import com.clougence.clouddm.console.web.model.vo.ResourceSummaryVO;
import com.clougence.clouddm.console.web.service.auth.RdpRoleService;
import com.clougence.clouddm.console.web.service.auth.RdpUserService;
import com.clougence.clouddm.console.web.service.login.LoginMFAService;
import com.clougence.clouddm.console.web.util.RdpConvertUtils;
import com.clougence.clouddm.console.web.util.Sm2Utils;
import com.clougence.clouddm.platform.dal.access.AuthDal;
import com.clougence.clouddm.platform.dal.model.ResourceType;
import com.clougence.clouddm.platform.dal.model.auth.AccountType;
import com.clougence.clouddm.platform.dal.model.auth.DmAuthMFADO;
import com.clougence.clouddm.platform.dal.model.auth.DmAuthRoleDO;
import com.clougence.clouddm.platform.dal.model.auth.DmAuthUserDO;
import com.clougence.clouddm.platform.dal.model.monitor.AuditType;
import com.clougence.clouddm.platform.dal.model.monitor.SecurityLevel;
import com.clougence.clouddm.sdk.security.auth.AuthInfo;
import com.clougence.rdp.service.RdpOpAuditService;
import com.clougence.rdp.service.model.UpdateUserInfoMO;
import com.clougence.rdp.service.model.ValidateResultMO;
import com.clougence.utils.StringUtils;
import com.clougence.utils.format.DateFormatType;

import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

/**
 * @author wanshao create time is 2021/1/5
 **/
@RestController
@RequestMapping(value = DmControllerUrlPrefix.CONSOLE_PREFIX + "/user")
@Slf4j
public class RdpUserController {

    @Resource
    private AuthDal              authDal;
    @Resource
    private RdpUserService       rdpUserService;
    @Resource
    private RdpRoleService       rdpRoleService;
    @Resource
    private SessionCookieService sessionCookieService;
    @Resource
    private SessionAccountPolicy accountPolicy;
    @Resource
    private DmAuthServiceForBiz  rdpAuthService;
    @Resource
    private RdpOpAuditService    rdpOpAuditService;
    @Resource
    private ConsoleConfig        consoleConfig;
    @Resource
    private LoginMFAService      loginMFAService;

    // --------------------------------
    //      for User Info
    // --------------------------------

    @RequestAuth(strategy = RefAnyOnes)
    @RequestMapping(value = "/queryLoginUser", method = RequestMethod.POST)
    public ResWebData<?> queryLoginUser(HttpServletRequest request, HttpServletResponse response) {
        String uid = (String) request.getAttribute(RdpUserService.UID);
        if (uid == null) {
            return ResWebDataUtils.buildSuccess(null);
        }

        DmAuthUserDO userDO = this.rdpUserService.getUserByUid(uid);
        DmAuthUserDO pUser;

        String puid = (String) request.getAttribute(RdpUserService.PUID);
        if (puid == null || !puid.equals(uid)) {
            pUser = this.rdpUserService.getPrimaryUser(uid);
        } else {
            pUser = userDO;
        }

        LoginUserVO userVO = RdpConvertUtils.convertToLoginUserVO(userDO, pUser);
        DmAuthMFADO mfaDO = this.loginMFAService.queryMFA(uid);
        if (mfaDO != null) {
            userVO.setMfaStatus(mfaDO.getMfaStatus());
            userVO.setMfaAccountType(mfaDO.getMfaAccountType());
        }
        SessionPrincipal principal = (SessionPrincipal) request.getAttribute(WebSessionManager.SESSION_PRINCIPAL);
        userVO.setLoginType(principal == null ? null : principal.getLoginType());
        userVO.setSubAccountPwdValidDays(this.accountPolicy.passwordValidDays(userDO));

        //if value is sub-zero,need logout
        if (userVO.getSubAccountPwdValidDays() != null && userVO.getSubAccountPwdValidDays() < 0) {
            logout(response);
        }

        return ResWebDataUtils.buildSuccess(userVO);
    }

    @RequestAuth(strategy = RefAnyOnes)
    @RequestMapping(value = "/listMyAuth", method = RequestMethod.POST)
    public ResWebData<?> listMyAuth(HttpServletRequest request) {
        String puid = (String) request.getAttribute(RdpUserService.PUID);
        String uid = (String) request.getAttribute(RdpUserService.UID);

        Collection<AuthInfo> myList = this.rdpUserService.allAuthLabelByUser(puid, uid);
        List<String> labelVOS = myList.stream().map(AuthInfo::getKey).collect(Collectors.toList());
        return ResWebDataUtils.buildSuccess(labelVOS);
    }

    @RequestAuth(strategy = Ignore)
    @RequestMapping(value = "/getPrimaryAccountPwdPolicy", method = RequestMethod.POST)
    public ResWebData<?> getPrimaryAccountPwdPolicy(HttpServletRequest request) {
        PwdPolicyVO vo = this.rdpUserService.getPwdPolicy(null);
        return ResWebDataUtils.buildSuccess(vo);
    }

    @RequestAuth(strategy = Ignore)
    @RequestMapping(value = "/getSubAccountPwdPolicy", method = RequestMethod.POST)
    public ResWebData<?> getSubAccountPwdPolicy(HttpServletRequest request) {
        String puid = (String) request.getAttribute(RdpUserService.PUID);

        PwdPolicyVO vo = this.rdpUserService.getPwdPolicy(puid);
        return ResWebDataUtils.buildSuccess(vo);
    }

    @RequestAuth(strategy = Ignore)
    @RequestMapping(value = "/listMyAuthCategoryForMenu", method = RequestMethod.POST)
    public ResWebData<?> listMyAuthCategoryForMenu(HttpServletRequest request) {
        String puid = (String) request.getAttribute(RdpUserService.PUID);
        String uid = (String) request.getAttribute(RdpUserService.UID);

        Collection<AuthInfo> categoryList = this.rdpUserService.allAuthMenuCategoryByUser(puid, uid);
        List<String> vos = categoryList.stream().map(AuthInfo::getKey).collect(Collectors.toList());
        return ResWebDataUtils.buildSuccess(vos);
    }

    @RequestAuth(strategy = Ignore)
    @RequestMapping(value = "/resourceSummary", method = { RequestMethod.POST })
    public ResWebData<?> resourceSummary(HttpServletRequest request) {
        String puid = (String) request.getAttribute(RdpUserService.PUID);
        String uid = (String) request.getAttribute(RdpUserService.UID);

        ResourceSummaryVO summaryVO = new ResourceSummaryVO();
        if (StringUtils.equalsIgnoreCase(puid, uid)) {
            DmAuthUserDO userDO = this.rdpUserService.getUserByUid(puid);
            long subAccounts = this.authDal.userMapper().queryCountByParentId(userDO.getId());
            summaryVO.setSubAccountCounts(subAccounts);
            summaryVO.setDsAuthCounts(0);
        } else {
            summaryVO.setSubAccountCounts(0);
            summaryVO.setDsAuthCounts(this.authDal.resMapper().queryAuthCountByUser(uid));
        }

        return ResWebDataUtils.buildSuccess(summaryVO);
    }

    @RequestAuth(strategy = Ignore)
    @RequestMapping(value = "/waterMark", method = RequestMethod.POST)
    public ResWebData<?> watermark(HttpServletRequest request) {
        String uid = (String) request.getAttribute(RdpUserService.UID);
        Map<String, String> data = new LinkedHashMap<>();

        DmAuthUserDO userDO = this.rdpUserService.getUserByUid(uid);
        if (userDO == null) {
            data.put("user_id", "Unknown");
            data.put("user_name", "Unknown");
            data.put("user_phone", "Unknown");
            data.put("time", DateFormatType.s_yyyyMMdd_HHmmss.format(new Date(System.currentTimeMillis())));
        } else {
            data.put("user_id", userDO.getUid());
            data.put("user_name", userDO.getUsername());
            data.put("user_phone", userDO.getPhone());
            data.put("time", DateFormatType.s_yyyyMMdd_HHmmss.format(new Date(System.currentTimeMillis())));
        }

        return ResWebDataUtils.buildSuccess(data);
    }

    // --------------------------------
    //      for User Info Update
    // --------------------------------
    @RequestAuth(level = HIGH, value = RDP_USER_READ)
    @RequestMapping(value = "/listRules", method = { RequestMethod.POST })
    public ResWebData<?> listRole(HttpServletRequest request) {
        String puid = (String) request.getAttribute(RdpUserService.PUID);

        List<DmAuthRoleDO> roles = this.rdpRoleService.listRoleByUID(puid);
        return ResWebDataUtils.buildSuccess(roles.stream().map(RdpConvertUtils::convertToRoleInfoVO).collect(Collectors.toList()));
    }

    @RequestAuth(strategy = Ignore)
    @RequestMapping(value = "/resetPwdWithOriginPwd", method = { RequestMethod.POST })
    public ResWebData<?> resetPwdWithOriginPwd(HttpServletRequest request, HttpServletResponse response, @RequestBody @Valid ResetPwdWithOriginPwdFO resetPwdFO) {
        String puid = (String) request.getAttribute(RdpUserService.PUID);
        String uid = (String) request.getAttribute(RdpUserService.UID);
        //decrypt
        resetPwdFO.setOriginPassword(Sm2Utils.decrypt(consoleConfig.getPrivateKey(), resetPwdFO.getOriginPassword()));
        resetPwdFO.setNewPassword(Sm2Utils.decrypt(consoleConfig.getPrivateKey(), resetPwdFO.getNewPassword()));

        UpdateUserInfoMO mo = rdpUserService.resetPwdWithOriginPwd(resetPwdFO, uid, puid);
        if (mo.isSuccess()) {
            logout(response);

            this.rdpOpAuditService.logAndAddOperationAudit(puid, uid, request.getRequestURI(), request
                .getRemoteAddr(), uid, "", SecurityLevel.HIGH, AuditType.UPDATE_ACCOUNT_PWD, ResourceType.ACCOUNT);

            return ResWebDataUtils.buildSuccess();
        } else {
            return ResWebDataUtils.buildError(mo.getErrorMsg());
        }
    }

    @RequestAuth(level = HIGH, value = RDP_USER_MANAGE)
    @RequestMapping(value = "/resetSubAccountPwd", method = { RequestMethod.POST })
    public ResWebData<?> resetSubAccountPwd(HttpServletRequest request, @RequestBody @Valid ResetSubAccountPwdFO resetPwdFO) {
        String puid = (String) request.getAttribute(RdpUserService.PUID);
        String uid = (String) request.getAttribute(RdpUserService.UID);
        //decrypt
        resetPwdFO.setOperatorPwd(Sm2Utils.decrypt(consoleConfig.getPrivateKey(), resetPwdFO.getOperatorPwd()));
        resetPwdFO.setNewPassword(Sm2Utils.decrypt(consoleConfig.getPrivateKey(), resetPwdFO.getNewPassword()));

        ValidateResultMO pwdMO = rdpUserService.validateSubAccountPwd(puid, resetPwdFO.getNewPassword());
        if (pwdMO != null && !pwdMO.isSuccess()) {
            return ResWebDataUtils.buildError(pwdMO.getErrorMsg());
        }

        rdpAuthService.checkOperateOtherUserAuth(uid, resetPwdFO.getSubAccountUid());
        UpdateUserInfoMO mo = rdpUserService.resetSubAccountPwd(resetPwdFO, uid);

        if (mo.isSuccess()) {
            rdpOpAuditService.logAndAddOperationAudit(puid, uid, request.getRequestURI(), request.getRemoteAddr(), resetPwdFO
                .getSubAccountUid(), "", SecurityLevel.HIGH, AuditType.UPDATE_SUB_ACCOUNT_PWD, ResourceType.ACCOUNT);
            return ResWebDataUtils.buildSuccess();
        } else {
            return ResWebDataUtils.buildError(mo.getErrorMsg());
        }
    }

    @RequestAuth(strategy = Ignore)
    @RequestMapping(value = "/updateUserPhone", method = RequestMethod.POST)
    public ResWebData<?> updateUserPhone(@RequestBody @Valid UpdateUserPhoneFO fo, HttpServletRequest request) {
        String puid = (String) request.getAttribute(RdpUserService.PUID);
        String uid = (String) request.getAttribute(RdpUserService.UID);

        UpdateUserInfoMO res = this.rdpUserService.updateUserPhone(uid, fo);
        if (!res.isSuccess()) {
            return ResWebDataUtils.buildError(res.getErrorMsg());
        }

        //if is primary user
        //        if (uid.equals(puid)) {
        //            this.saasService.updateUserPhone(uid, fo.getPhone());
        //        }

        this.rdpOpAuditService.logAndAddOperationAudit(puid, uid, request.getRequestURI(), request.getRemoteAddr(), uid, res
            .getConfigLO(), SecurityLevel.HIGH, AuditType.UPDATE_ACCOUNT_PHONE, ResourceType.ACCOUNT);

        return ResWebDataUtils.buildSuccess();
    }

    @RequestAuth(strategy = Ignore)
    @RequestMapping(value = "/updateUserEmail", method = RequestMethod.POST)
    public ResWebData<?> updateUserEmail(@RequestBody @Valid UpdateUserEmailFO fo, HttpServletRequest request) {
        String puid = (String) request.getAttribute(RdpUserService.PUID);
        String uid = (String) request.getAttribute(RdpUserService.UID);

        if (StringUtils.isBlank(uid)) {
            throw new IllegalArgumentException("The login user is empty,can not change email.");
        }

        UpdateUserInfoMO res = this.rdpUserService.updateUserEmail(uid, fo);
        if (!res.isSuccess()) {
            return ResWebDataUtils.buildError(res.getErrorMsg());
        }

        //if is primary user
        //        if (uid.equals(puid)) {
        //            this.saasService.updateUserEmail(uid, fo.getEmail());
        //        }

        this.rdpOpAuditService.logAndAddOperationAudit(puid, uid, request.getRequestURI(), request.getRemoteAddr(), uid, res
            .getConfigLO(), SecurityLevel.HIGH, AuditType.UPDATE_ACCOUNT_EMAIL, ResourceType.ACCOUNT);

        return ResWebDataUtils.buildSuccess();
    }

    @RequestAuth(strategy = Ignore)
    @RequestMapping(value = "/updateUserName", method = RequestMethod.POST)
    public ResWebData<?> updateUserName(@RequestBody @Valid UpdateUserNameFO fo, HttpServletRequest request) {
        String puid = (String) request.getAttribute(RdpUserService.PUID);
        String uid = (String) request.getAttribute(RdpUserService.UID);

        UpdateUserInfoMO res = this.rdpUserService.updateUserName(uid, fo);
        if (!res.isSuccess()) {
            return ResWebDataUtils.buildError(res.getErrorMsg());
        }

        this.rdpOpAuditService.logAndAddOperationAudit(puid, uid, request.getRequestURI(), request.getRemoteAddr(), uid, res
            .getConfigLO(), SecurityLevel.NORMAL, AuditType.UPDATE_ACCOUNT_USER_NAME, ResourceType.ACCOUNT);

        return ResWebDataUtils.buildSuccess();
    }

    @RequestAuth(strategy = Ignore)
    @RequestMapping(value = "/checkProfileDuplicate", method = RequestMethod.POST)
    public ResWebData<?> checkProfileDuplicate(@RequestBody @Valid CheckSubAccountFO fo, HttpServletRequest request) {
        String uid = (String) request.getAttribute(RdpUserService.UID);
        DmAuthUserDO userDO = this.rdpUserService.getUserByUid(uid);
        if (userDO == null) {
            return ResWebDataUtils.buildError(DmI18nUtils.getMessage(I18nRdpMsgKeys.USER_NOT_EXIST_ERROR.name()));
        }

        DmAuthUserDO existUserDO = null;
        if (fo.getCheckType() == CheckSubAccountType.PHONE) {
            if (userDO.getAccountType() == AccountType.PRIMARY_ACCOUNT) {
                existUserDO = this.authDal.userMapper().queryPrimaryByPhone(fo.getCheckContent());
            } else {
                existUserDO = this.authDal.userMapper().queryByPhoneAndParentIdExcludeUid(fo.getCheckContent(), userDO.getParentId(), uid);
            }
            if (existUserDO != null && !StringUtils.equals(existUserDO.getUid(), uid)) {
                return ResWebDataUtils.buildError(DmI18nUtils.getMessage(I18nRdpMsgKeys.USER_PHONE_EXIST_ERROR.name(), fo.getCheckContent()));
            }
        } else if (fo.getCheckType() == CheckSubAccountType.EMAIL) {
            if (userDO.getAccountType() == AccountType.PRIMARY_ACCOUNT) {
                existUserDO = this.authDal.userMapper().queryPrimaryByEmail(fo.getCheckContent());
            } else {
                existUserDO = this.authDal.userMapper().queryByEmailAndParentIdExcludeUid(fo.getCheckContent(), userDO.getParentId(), uid);
            }
            if (existUserDO != null && !StringUtils.equals(existUserDO.getUid(), uid)) {
                return ResWebDataUtils.buildError(DmI18nUtils.getMessage(I18nRdpMsgKeys.USER_EMAIL_EXIST_ERROR.name(), fo.getCheckContent()));
            }
        }

        return ResWebDataUtils.buildSuccess();
    }

    protected void logout(HttpServletResponse response) {
        this.sessionCookieService.clear(response);
        this.sessionCookieService.clearLegacy(response);
    }

    @RequestAuth(strategy = Ignore)
    @RequestMapping(value = "/updateUserPhoneWithPwd", method = RequestMethod.POST)
    public ResWebData<?> updateUserPhoneWithPwd(@RequestBody @Valid UpdateUserPhoneWithPwdFO fo, HttpServletRequest request) {
        String puid = (String) request.getAttribute(RdpUserService.PUID);
        String uid = (String) request.getAttribute(RdpUserService.UID);

        //decrypt
        fo.setPassword(Sm2Utils.decrypt(consoleConfig.getPrivateKey(), fo.getPassword()));

        UpdateUserInfoMO res = this.rdpUserService.updateUserPhoneWithPwd(uid, fo);
        if (!res.isSuccess()) {
            return ResWebDataUtils.buildError(res.getErrorMsg());
        }

        //if is primary user
        //        if (uid.equals(puid)) {
        //            this.saasService.updateUserPhone(uid, fo.getPhone());
        //        }

        this.rdpOpAuditService.logAndAddOperationAudit(puid, uid, request.getRequestURI(), request.getRemoteAddr(), uid, res
            .getConfigLO(), SecurityLevel.HIGH, AuditType.UPDATE_ACCOUNT_PHONE, ResourceType.ACCOUNT);

        return ResWebDataUtils.buildSuccess();
    }

    @RequestAuth(strategy = Ignore)
    @RequestMapping(value = "/updateUserEmailWithPwd", method = RequestMethod.POST)
    public ResWebData<?> updateUserEmailWithPwd(@RequestBody @Valid UpdateUserEmailWithPwdFO fo, HttpServletRequest request) {
        String puid = (String) request.getAttribute(RdpUserService.PUID);
        String uid = (String) request.getAttribute(RdpUserService.UID);

        //decrypt
        fo.setPassword(Sm2Utils.decrypt(consoleConfig.getPrivateKey(), fo.getPassword()));

        UpdateUserInfoMO res = this.rdpUserService.updateUserEmailWithPwd(uid, fo);
        if (!res.isSuccess()) {
            return ResWebDataUtils.buildError(res.getErrorMsg());
        }

        //if is primary user
        //        if (uid.equals(puid)) {
        //            this.saasService.updateUserEmail(uid, fo.getEmail());
        //        }

        this.rdpOpAuditService.logAndAddOperationAudit(puid, uid, request.getRequestURI(), request.getRemoteAddr(), uid, res
            .getConfigLO(), SecurityLevel.HIGH, AuditType.UPDATE_ACCOUNT_EMAIL, ResourceType.ACCOUNT);

        return ResWebDataUtils.buildSuccess();
    }

    @RequestAuth(level = HIGH, value = RDP_PRI_USER_AK_SK_R)
    @RequestMapping(value = "/queryUserAkSk", method = RequestMethod.POST)
    public ResWebData<?> queryUserAkSk(@RequestBody @Valid QueryUserAkSkFO fo, HttpServletRequest request) {
        String puid = (String) request.getAttribute(RdpUserService.PUID);
        String uid = (String) request.getAttribute(RdpUserService.UID);

        ResWebData<RdpUserAkSkVO> resWebData = this.rdpUserService.queryAkSk(puid, fo);

        if (resWebData.isSuccess()) {
            rdpOpAuditService.logAndAddOperationAudit(puid, uid, request.getRequestURI(), request
                .getRemoteAddr(), uid, fo, SecurityLevel.HIGH, AuditType.QUERY_ACCOUNT_AK_SK, ResourceType.ACCOUNT);
        }

        return resWebData;
    }

    @RequestAuth(level = HIGH, value = RDP_PRI_USER_AK_SK_W)
    @RequestMapping(value = "/resetUserAkSk", method = RequestMethod.POST)
    public ResWebData<?> resetUserAkSk(@RequestBody @Valid ResetUserAkSkFO fo, HttpServletRequest request) {
        String puid = (String) request.getAttribute(RdpUserService.PUID);
        String uid = (String) request.getAttribute(RdpUserService.UID);

        ResWebData<String> resWebData = this.rdpUserService.resetAkSk(puid, fo);

        if (resWebData.isSuccess()) {
            rdpOpAuditService.logAndAddOperationAudit(puid, uid, request.getRequestURI(), request
                .getRemoteAddr(), uid, fo, SecurityLevel.HIGH, AuditType.RESET_ACCOUNT_AK_SK, ResourceType.ACCOUNT);
        }

        return resWebData;
    }

}
