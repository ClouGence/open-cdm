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

import static com.clougence.clouddm.console.web.global.jwtsession.JwtService.jwtTokenName;
import static com.clougence.clouddm.console.web.global.jwtsession.RequestAuth.AuthStrategy.Ignore;
import static com.clougence.clouddm.console.web.global.jwtsession.RequestAuth.AuthStrategy.RefAnyOnes;
import static com.clougence.clouddm.platform.dal.model.monitor.SecurityLevel.HIGH;
import static com.clougence.clouddm.sdk.security.auth.def.SecRoleAuthLabel.*;

import java.util.*;
import java.util.stream.Collectors;

import org.jetbrains.annotations.NotNull;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.clougence.clouddm.api.common.rpc.ResWebData;
import com.clougence.clouddm.api.common.rpc.ResWebDataUtils;
import com.clougence.clouddm.base.metadata.rdp.enumeration.ResourceType;
import com.clougence.clouddm.console.web.component.auth.DmAuthServiceForBiz;
import com.clougence.clouddm.console.web.global.config.DmConsoleConfig;
import com.clougence.clouddm.console.web.global.i18n.DmI18nUtils;
import com.clougence.clouddm.console.web.global.i18n.I18nRdpMsgKeys;
import com.clougence.clouddm.console.web.global.jwtsession.JwtService;
import com.clougence.clouddm.console.web.global.jwtsession.RequestAuth;
import com.clougence.clouddm.console.web.model.fo.*;
import com.clougence.clouddm.console.web.model.fo.user.ResetSubAccountPwdFO;
import com.clougence.clouddm.console.web.model.vo.LoginUserVO;
import com.clougence.clouddm.console.web.model.vo.PwdValidateExprVO;
import com.clougence.clouddm.console.web.model.vo.RdpUserAkSkVO;
import com.clougence.clouddm.console.web.model.vo.ResourceSummaryVO;
import com.clougence.clouddm.console.web.service.auth.RdpRoleService;
import com.clougence.clouddm.console.web.service.auth.RdpUserLoginRegService;
import com.clougence.clouddm.console.web.service.auth.RdpUserService;
import com.clougence.clouddm.console.web.util.RdpConvertUtils;
import com.clougence.clouddm.console.web.util.Sm2Utils;
import com.clougence.clouddm.platform.dal.access.AuthDal;
import com.clougence.clouddm.platform.dal.model.auth.DmAuthRoleDO;
import com.clougence.clouddm.platform.dal.model.auth.DmAuthUserDO;
import com.clougence.clouddm.platform.dal.model.auth.VerifyCodeType;
import com.clougence.clouddm.platform.dal.model.monitor.AuditType;
import com.clougence.clouddm.platform.dal.model.monitor.SecurityLevel;
import com.clougence.clouddm.sdk.security.auth.AuthInfo;
import com.clougence.rdp.constant.RdpControllerUrlPrefix;
import com.clougence.rdp.service.RdpOpAuditService;
import com.clougence.rdp.service.RdpVerifyService;
import com.clougence.rdp.service.enumeration.OpVerifyErrType;
import com.clougence.rdp.service.model.CheckVerifyMO;
import com.clougence.rdp.service.model.OpPasswdVerifyMO;
import com.clougence.rdp.service.model.UpdateUserInfoMO;
import com.clougence.rdp.service.model.ValidateResultMO;
import com.clougence.utils.StringUtils;
import com.clougence.utils.format.DateFormatType;

import jakarta.annotation.Resource;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

/**
 * @author wanshao create time is 2021/1/5
 **/
@RestController
@RequestMapping(value = RdpControllerUrlPrefix.CONSOLE_PREFIX + "/user")
@Slf4j
public class RdpUserController {

    @Resource
    private AuthDal                authDal;

    @Resource
    private RdpUserService         rdpUserService;
    @Resource
    private RdpRoleService         rdpRoleService;

    @Resource
    private JwtService             jwtService;

    @Resource
    private RdpVerifyService       rdpVerifyService;

    @Resource
    private DmAuthServiceForBiz    rdpAuthService;

    @Resource
    private RdpUserLoginRegService rdpUserLoginRegService;

    @Resource
    private RdpOpAuditService      rdpOpAuditService;

    @Resource
    private DmConsoleConfig        rdpConfig;

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
        rdpUserLoginRegService.fillSubAccountPwdValidDays(userVO, userDO.getLastDateUpdatePwd(), pUser.getUid());

        //if value is sub-zero,need logout
        if (userVO.getSubAccountPwdValidDays() != null && userVO.getSubAccountPwdValidDays() < 0) {
            logout(response);
        }

        //        // refresh saas user status
        //        if (GlobalDeployMode.inCloud()) {
        //            String fuid = rdpUserService.findFinanceOwnerUid(uid, puid);
        //            if (fuid != null && !fuid.equals(uid)) {
        //                userVO.setSaasResMode(SaasResMode.MANAGED);
        //                saasService.refreshUserStatus(fuid);
        //            } else {
        //                userVO.setSaasResMode(SaasResMode.BYOC);
        //                saasService.refreshUserStatus(uid);
        //            }
        //        }

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
    @RequestMapping(value = "/getPrimaryAccountPwdValidateExpr", method = RequestMethod.POST)
    public ResWebData<?> getPrimaryAccountPwdValidateExpr(HttpServletRequest request) {
        PwdValidateExprVO vo = this.rdpUserService.getPwdValidateExprWithoutEscape(null);
        return ResWebDataUtils.buildSuccess(vo);
    }

    @RequestAuth(strategy = Ignore)
    @RequestMapping(value = "/getSubAccountPwdValidateExpr", method = RequestMethod.POST)
    public ResWebData<?> getSubAccountPwdValidateExpr(HttpServletRequest request) {
        String puid = (String) request.getAttribute(RdpUserService.PUID);

        PwdValidateExprVO vo = this.rdpUserService.getPwdValidateExprWithoutEscape(puid);
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
    @RequestMapping(value = "/resetOpPasswd", method = { RequestMethod.POST })
    public ResWebData<?> resetOpPasswd(@Valid @RequestBody ResetOpPasswdFO resetOpPasswdFO, HttpServletRequest request, HttpServletResponse response) {
        String puid = (String) request.getAttribute(RdpUserService.PUID);
        String uid = (String) request.getAttribute(RdpUserService.UID);
        //decrypt
        resetOpPasswdFO.setOpPassword(Sm2Utils.decrypt(rdpConfig.getPrivateKey(), resetOpPasswdFO.getOpPassword()));

        UpdateUserInfoMO resetOpPasswdMO = this.rdpUserService.resetOpPasswd(resetOpPasswdFO, uid);
        if (resetOpPasswdMO.isSuccess()) {
            addOpPwdTokenToCookie(response, uid);

            this.rdpOpAuditService.logAndAddOperationAudit(puid, uid, request.getRequestURI(), request
                .getRemoteAddr(), uid, "", SecurityLevel.HIGH, AuditType.UPDATE_ACCOUNT_OP_PWD, ResourceType.ACCOUNT);

            return ResWebDataUtils.buildSuccess();
        } else {
            return ResWebDataUtils.buildError(resetOpPasswdMO.getErrorMsg());
        }
    }

    @RequestAuth(strategy = Ignore)
    @RequestMapping(value = "/resetPwdWithOriginPwd", method = { RequestMethod.POST })
    public ResWebData<?> resetPwdWithOriginPwd(HttpServletRequest request, HttpServletResponse response, @RequestBody @Valid ResetPwdWithOriginPwdFO resetPwdFO) {
        String puid = (String) request.getAttribute(RdpUserService.PUID);
        String uid = (String) request.getAttribute(RdpUserService.UID);
        //decrypt
        resetPwdFO.setOriginPassword(Sm2Utils.decrypt(rdpConfig.getPrivateKey(), resetPwdFO.getOriginPassword()));
        resetPwdFO.setNewPassword(Sm2Utils.decrypt(rdpConfig.getPrivateKey(), resetPwdFO.getNewPassword()));

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
    public ResWebData<?> resetSubAccountPwd(HttpServletRequest request, HttpServletResponse response, @RequestBody @Valid ResetSubAccountPwdFO resetPwdFO) {
        String puid = (String) request.getAttribute(RdpUserService.PUID);
        String uid = (String) request.getAttribute(RdpUserService.UID);
        //decrypt
        resetPwdFO.setOperatorPwd(Sm2Utils.decrypt(rdpConfig.getPrivateKey(), resetPwdFO.getOperatorPwd()));
        resetPwdFO.setNewPassword(Sm2Utils.decrypt(rdpConfig.getPrivateKey(), resetPwdFO.getNewPassword()));

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
    public ResWebData<?> updateUserPhone(@RequestBody @Valid UpdateUserPhoneFO fo, HttpServletRequest request, HttpServletResponse response) {
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

        logout(response);

        this.rdpOpAuditService.logAndAddOperationAudit(puid, uid, request.getRequestURI(), request.getRemoteAddr(), uid, res
            .getConfigLO(), SecurityLevel.HIGH, AuditType.UPDATE_ACCOUNT_PHONE, ResourceType.ACCOUNT);

        return ResWebDataUtils.buildSuccess();
    }

    @RequestAuth(strategy = Ignore)
    @RequestMapping(value = "/updateUserEmail", method = RequestMethod.POST)
    public ResWebData<?> updateUserEmail(@RequestBody @Valid UpdateUserEmailFO fo, HttpServletRequest request, HttpServletResponse response) {
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

        logout(response);

        this.rdpOpAuditService.logAndAddOperationAudit(puid, uid, request.getRequestURI(), request.getRemoteAddr(), uid, res
            .getConfigLO(), SecurityLevel.HIGH, AuditType.UPDATE_ACCOUNT_EMAIL, ResourceType.ACCOUNT);

        return ResWebDataUtils.buildSuccess();
    }

    protected void logout(HttpServletResponse response) {
        Cookie cookie = new Cookie(jwtTokenName, StringUtils.EMPTY);
        cookie.setHttpOnly(true);
        cookie.setMaxAge(0);
        cookie.setPath("/");

        if (StringUtils.isNotBlank(rdpConfig.getLoginCookieDomain())) {
            cookie.setDomain(rdpConfig.getLoginCookieDomain());
        }

        response.addCookie(cookie);
    }

    @RequestAuth(strategy = Ignore)
    @RequestMapping(value = "/updateUserPhoneWithPwd", method = RequestMethod.POST)
    public ResWebData<?> updateUserPhoneWithPwd(@RequestBody @Valid UpdateUserPhoneWithPwdFO fo, HttpServletRequest request) {
        String puid = (String) request.getAttribute(RdpUserService.PUID);
        String uid = (String) request.getAttribute(RdpUserService.UID);

        //decrypt
        fo.setPassword(Sm2Utils.decrypt(rdpConfig.getPrivateKey(), fo.getPassword()));

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
        fo.setPassword(Sm2Utils.decrypt(rdpConfig.getPrivateKey(), fo.getPassword()));

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
    public ResWebData<?> resetUserAkSk(@RequestBody @Valid ResetUserAkSkFO fo, HttpServletRequest request, HttpServletResponse response) {
        String puid = (String) request.getAttribute(RdpUserService.PUID);
        String uid = (String) request.getAttribute(RdpUserService.UID);

        ResWebData<String> resWebData = this.rdpUserService.resetAkSk(puid, fo);

        if (resWebData.isSuccess()) {
            rdpOpAuditService.logAndAddOperationAudit(puid, uid, request.getRequestURI(), request
                .getRemoteAddr(), uid, fo, SecurityLevel.HIGH, AuditType.RESET_ACCOUNT_AK_SK, ResourceType.ACCOUNT);

            logout(response);
        }

        return resWebData;
    }

    // --------------------------------
    //      for Verify
    // --------------------------------

    @RequestAuth(strategy = Ignore)
    @RequestMapping(value = "/checkVerifyCode", method = RequestMethod.POST)
    public ResWebData<?> checkVerifyCode(@RequestBody @Valid CheckVerifyCodeFO verifyCodeFO, HttpServletRequest request) {
        String puid = (String) request.getAttribute(RdpUserService.PUID);
        String uid = (String) request.getAttribute(RdpUserService.UID);

        DmAuthUserDO userDO = this.rdpUserService.getUserByUid(uid);
        CheckVerifyMO verifyData = new CheckVerifyMO();
        verifyData.setVerifyType(verifyCodeFO.getVerifyType());
        verifyData.setVerifyCode(verifyCodeFO.getVerifyCode());
        verifyData.setVerifyCodeType(VerifyCodeType.VERIFY_OLD_ACCOUNT);
        verifyData.setEmail(userDO.getEmail());
        verifyData.setPhoneNumber(userDO.getPhone());
        this.rdpVerifyService.checkVerifyCode(verifyData);
        return ResWebDataUtils.buildSuccess();
    }

    @RequestAuth(strategy = Ignore)
    @RequestMapping(value = "/opPasswdVerify", method = { RequestMethod.POST })
    public ResWebData<?> opPasswdVerify(@Valid @RequestBody OpPasswdVerifyFO verifyFO, HttpServletRequest request, HttpServletResponse response) {
        String puid = (String) request.getAttribute(RdpUserService.PUID);
        String uid = (String) request.getAttribute(RdpUserService.UID);

        //decrypt
        verifyFO.setOpPassword(Sm2Utils.decrypt(rdpConfig.getPrivateKey(), verifyFO.getOpPassword()));

        OpPasswdVerifyMO verifyMO = this.rdpUserService.opPasswdVerify(verifyFO.getOpPassword(), uid);
        if (verifyMO.isSuccess()) {
            addOpPwdTokenToCookie(response, uid);
            return ResWebDataUtils.buildSuccess();
        } else {
            if (verifyMO.getOpVerifyErrType() == OpVerifyErrType.OP_PASSWD_ERROR) {
                return ResWebDataUtils.buildError(DmI18nUtils.getMessage(I18nRdpMsgKeys.VERIFY_OP_PASSWORD_ERROR.name()));
            } else if (verifyMO.getOpVerifyErrType() == OpVerifyErrType.OP_PASSWD_NOT_SET) {
                return ResWebDataUtils.buildError(DmI18nUtils.getMessage(I18nRdpMsgKeys.VERIFY_OP_PASSWORD_NOT_SET_ERROR.name()));
            } else {
                return ResWebDataUtils.buildError(DmI18nUtils.getMessage(I18nRdpMsgKeys.VERIFY_UNSUPPORTED_TYPE_ERROR.name()));
            }
        }
    }

    @RequestAuth(RDP_PRI_USER_NORMAL_CONF_R)
    @RequestMapping(value = "/verifyMail", method = RequestMethod.POST)
    public ResWebData<?> verifyMail(HttpServletRequest request) {
        String puid = (String) request.getAttribute(RdpUserService.PUID);
        this.rdpVerifyService.verifyMail(puid);
        return ResWebDataUtils.buildSuccess("success");
    }

    @RequestAuth(RDP_PRI_USER_NORMAL_CONF_R)
    @RequestMapping(value = "/verifyIm", method = RequestMethod.POST)
    public ResWebData<?> verifyIm(@NotNull HttpServletRequest request) {
        String uid = (String) request.getAttribute(RdpUserService.UID);
        String puid = (String) request.getAttribute(RdpUserService.PUID);
        return rdpVerifyService.verifyIm(uid, puid);
    }

    protected void addOpPwdTokenToCookie(HttpServletResponse response, String uid) {
        DmAuthUserDO userDO = this.rdpUserService.getUserByUid(uid);
        if (userDO == null) {
            throw new IllegalArgumentException("user not exist.");
        }

        Cookie cookie = new Cookie(JwtService.opPwdToken, this.jwtService.genOpPwdToken(userDO));
        // let fronted to extract jwt token as csrf token
        cookie.setHttpOnly(false);
        cookie.setMaxAge((int) (RdpUserService.OP_PASSWD_TOEKN_EXPIRE_MS / 1000));
        cookie.setPath("/");

        if (StringUtils.isNotBlank(rdpConfig.getLoginCookieDomain())) {
            cookie.setDomain(rdpConfig.getLoginCookieDomain());
        }

        response.addCookie(cookie);
    }
}
