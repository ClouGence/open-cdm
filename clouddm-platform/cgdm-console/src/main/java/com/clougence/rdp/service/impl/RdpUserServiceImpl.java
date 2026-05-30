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
package com.clougence.rdp.service.impl;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.clougence.clouddm.api.common.crypt.CryptService;
import com.clougence.clouddm.api.common.crypt.PasswordInfo;
import com.clougence.clouddm.api.common.exception.ErrorMessageException;
import com.clougence.clouddm.api.common.rpc.ResWebData;
import com.clougence.clouddm.api.common.rpc.ResWebDataUtils;
import com.clougence.clouddm.base.metadata.rdp.enumeration.GlobalDeploySite;
import com.clougence.clouddm.console.web.component.auth.DmAuthLabelService;
import com.clougence.clouddm.console.web.component.auth.DmUserService;
import com.clougence.clouddm.console.web.constants.CheckSubAccountType;
import com.clougence.clouddm.console.web.global.i18n.DmI18nUtils;
import com.clougence.clouddm.console.web.global.i18n.I18nRdpMsgKeys;
import com.clougence.clouddm.console.web.model.fo.*;
import com.clougence.clouddm.console.web.model.fo.role.UpdateUserRoleFO;
import com.clougence.clouddm.console.web.model.fo.user.*;
import com.clougence.clouddm.console.web.model.lo.UpdateUserInfoLO;
import com.clougence.clouddm.console.web.model.lo.UpdateUserRoleLO;
import com.clougence.clouddm.console.web.model.vo.ListUserVO;
import com.clougence.clouddm.console.web.model.vo.PwdValidateExprVO;
import com.clougence.clouddm.console.web.model.vo.RdpUserAkSkVO;
import com.clougence.clouddm.console.web.service.auth.RdpRoleService;
import com.clougence.clouddm.console.web.service.auth.RdpUserConfigService;
import com.clougence.clouddm.console.web.service.auth.RdpUserService;
import com.clougence.clouddm.console.web.util.RdpAuthUtils;
import com.clougence.clouddm.console.web.util.RdpConvertUtils;
import com.clougence.clouddm.platform.dal.access.AuthDal;
import com.clougence.clouddm.platform.dal.model.auth.*;
import com.clougence.clouddm.platform.dal.model.system.DmSysUserConfDO;
import com.clougence.clouddm.platform.plugin.PluginManager;
import com.clougence.clouddm.sdk.model.feature.RdpFeatureIDs;
import com.clougence.clouddm.sdk.security.auth.AuthInfo;
import com.clougence.clouddm.sdk.security.auth.AuthInfoType;
import com.clougence.clouddm.sdk.security.auth.def.SecSysRole;
import com.clougence.clouddm.sdk.security.login.LoginProvider;
import com.clougence.clouddm.sdk.security.login.LoginProviderSpi;
import com.clougence.clouddm.sdk.security.login.LoginRequest;
import com.clougence.clouddm.sdk.security.login.LoginResponse;
import com.clougence.rdp.global.config.user.UserDefinedConfig;
import com.clougence.rdp.service.RdpNamingService;
import com.clougence.rdp.service.RdpNotifyService;
import com.clougence.rdp.service.RdpVerifyService;
import com.clougence.rdp.service.enumeration.OpVerifyErrType;
import com.clougence.rdp.service.enumeration.UserOperationType;
import com.clougence.rdp.service.model.*;
import com.clougence.utils.CollectionUtils;
import com.clougence.utils.StringUtils;

import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;

/**
 * @author pudding
 * @date 2020-01-17 15:29
 */
@Slf4j
@Service
public class RdpUserServiceImpl implements RdpUserService, DmUserService {
    @Resource
    private AuthDal                authDal;
    @Resource
    private RdpRoleService         rdpRoleService;
    @Resource
    private RdpVerifyService       rdpVerifyService;
    @Resource
    private RdpNamingService       rdpNamingService;
    @Resource
    private RdpUserConfigService   rdpUserConfigService;
    @Resource
    private DmAuthLabelService     authLabelService;
    @Resource
    private List<RdpNotifyService> notifyServices;

    @Override
    public List<AuthInfo> allAuthLabelByUser(String puid, String uid) {
        if (StringUtils.equals(puid, uid)) {
            return this.authLabelService.getRoleAuthLabel();
        } else {
            DmAuthUserDO userDO = this.authDal.userMapper().queryByUid(uid);

            Long roleId = userDO.getRoleId();
            DmAuthRoleDO dmRoleDO = this.authDal.roleMapper().selectById(roleId);
            Set<String> effectiveAuthLabels = new HashSet<>(this.authLabelService.normalizeRoleAuthLabels(dmRoleDO.getRoleAuthLabels()));

            return this.authLabelService.getRoleAuthLabel().stream().filter(authInfo -> {
                return authInfo.getAuthType() == AuthInfoType.Auth && effectiveAuthLabels.contains(authInfo.getKey());
            }).collect(Collectors.toList());
        }
    }

    @Override
    public Collection<AuthInfo> allAuthMenuCategoryByUser(String puid, String uid) {
        List<AuthInfo> tmpDef = this.authLabelService.getAllCategory();
        List<String> support = new ArrayList<>();
        support.add(RdpFeatureIDs.PRODUCT_CLOUD_RDP);
        support.add(RdpFeatureIDs.PRODUCT_CLOUD_DM);

        List<AuthInfo> catTreeDef = tmpDef.stream().filter(a -> {
            return CollectionUtils.containsAny(a.getForProduct(), support);
        }).collect(Collectors.toList());

        if (StringUtils.equals(puid, uid)) {
            return catTreeDef;
        } else {

            // 1. cat List convert to Map
            Map<String, AuthInfo> catMap = new HashMap<>();
            for (AuthInfo info : catTreeDef) {
                catMap.put(info.getKey(), info);
            }

            // filter cat List
            Map<String, AuthInfo> useCatMap = new HashMap<>();
            List<AuthInfo> authInfos = this.allAuthLabelByUser(puid, uid);
            for (AuthInfo label : authInfos) {
                AuthInfo catInfo = catMap.get(label.getCategory());
                if (catInfo == null) {
                    continue;// category not exist
                }

                useCatMap.put(catInfo.getKey(), catInfo);
                while (StringUtils.isNotBlank(catInfo.getParent())) {
                    catInfo = catMap.get(catInfo.getParent());
                    if (catInfo == null) {
                        break;
                    }

                    useCatMap.put(catInfo.getKey(), catInfo);
                }
            }

            return useCatMap.values();
        }
    }

    @Override
    public DmAuthUserDO getUserByUid(String uid) {
        if (StringUtils.isBlank(uid)) {
            return null;
        } else {
            return authDal.userMapper().queryByUid(uid);
        }
    }

    @Override
    public DmAuthUserDO getUserById(long id) {
        if (id <= 0) {
            return null;
        } else {
            return this.authDal.userMapper().queryById(id);
        }
    }

    @Override
    public boolean isPrimaryUid(String uid) {
        DmAuthUserDO userDO = authDal.userMapper().queryByUid(uid);
        if (userDO == null) {
            return false;
        } else {
            return userDO.getParentId() == null || userDO.getParentId() <= 0;
        }
    }

    @Override
    public boolean isMaintainer(String uid) {
        DmAuthUserDO userDO = authDal.userMapper().queryByUid(uid);
        if (userDO == null) {
            return false;
        } else {
            return userDO.isMaintainer();
        }
    }

    @Override
    public PwdValidateExprVO getPwdValidateExprWithoutEscape(String puid) {
        //unlogin state
        if (StringUtils.isBlank(puid)) {
            return getDefaultValidateExprVO();
        }

        DmSysUserConfDO configDO = rdpUserConfigService.getSpecifiedConfig(puid, UserDefinedConfig.Fields.subAccountPwdVerifyExpr);
        if (configDO != null && StringUtils.isNotBlank(configDO.getConfigValue())) {
            DmSysUserConfDO tipsConf = rdpUserConfigService.getSpecifiedConfig(puid, UserDefinedConfig.Fields.subAccountPwdVerifyTips);
            if (tipsConf != null && StringUtils.isNotBlank(configDO.getConfigValue())) {
                PwdValidateExprVO vo = new PwdValidateExprVO();
                vo.setExpr(configDO.getConfigValue());
                vo.setTips(tipsConf.getConfigValue());
                return vo;
            } else {
                return getDefaultValidateExprVO();
            }
        } else {
            return getDefaultValidateExprVO();
        }
    }

    protected PwdValidateExprVO getDefaultValidateExprVO() {
        PwdValidateExprVO vo = new PwdValidateExprVO();
        String defaultPwdRegexForFront = DEFAULT_PWD_REGEX.replace("\\\\", "\\");
        vo.setExpr(defaultPwdRegexForFront);
        vo.setTips(DmI18nUtils.getMessage(I18nRdpMsgKeys.SUB_ACCOUNT_PWD_VALIDATE_TIPS.name()));
        return vo;
    }

    @Override
    public ValidateResultMO validatePrimaryAccountPwd(String pwd) {
        PwdValidateExprVO exprVO = getPwdValidateExprWithoutEscape(null);
        return validateByExpr(exprVO.getExpr(), exprVO.getTips(), pwd);
    }

    @Override
    public ValidateResultMO validateSubAccountPwd(String puid, String pwd) {
        PwdValidateExprVO exprVO = getPwdValidateExprWithoutEscape(puid);
        return validateByExpr(exprVO.getExpr(), exprVO.getTips(), pwd);
    }

    @Override
    public ValidateResultMO validateByExpr(String expr, String errorMsg, String content) {
        Pattern pattern = Pattern.compile(expr);
        Matcher matcher = pattern.matcher(content);

        if (!matcher.matches()) {
            return new ValidateResultMO(false, errorMsg);
        } else {
            return new ValidateResultMO(true, null);
        }
    }

    //
    // -- for Current User Manager
    //

    @Override
    public UpdateUserInfoMO resetOpPasswd(ResetOpPasswdFO fo, String uid) {
        DmAuthUserDO userDO = authDal.userMapper().queryByUid(uid);

        if (userDO == null) {
            return new UpdateUserInfoMO(false, DmI18nUtils.getMessage(I18nRdpMsgKeys.USER_NOT_EXIST_ERROR.name()));
        }

        CheckVerifyMO mo = new CheckVerifyMO();
        mo.setUid(uid);
        mo.setPhoneNumber(userDO.getPhone());
        mo.setVerifyCode(fo.getVerifyCode());
        mo.setVerifyType(VerifyType.SMS_VERIFY_CODE);
        mo.setVerifyCodeType(VerifyCodeType.RESET_OP_PASSWORD);
        rdpVerifyService.checkVerifyCode(mo);

        String opPassword = CryptService.INSTANCE.encryptForOneWay(fo.getOpPassword()).getEncryptPassword();
        authDal.userMapper().updateOpPasswdById(userDO.getId(), opPassword);
        return new UpdateUserInfoMO(true, null);
    }

    @Override
    public OpPasswdVerifyMO opPasswdVerify(String opPassword, String uid) {
        DmAuthUserDO dmUserDO = authDal.userMapper().queryByUid(uid);
        if (dmUserDO.getOpPassword() == null) {
            return new OpPasswdVerifyMO(false, OpVerifyErrType.OP_PASSWD_NOT_SET, DmI18nUtils.getMessage(I18nRdpMsgKeys.VERIFY_OP_PASSWORD_NOT_SET_ERROR.name()));
        }

        PasswordInfo passInfo = new PasswordInfo();
        passInfo.setEncryptPassword(dmUserDO.getOpPassword());
        passInfo.setPlainPassword(opPassword);
        boolean match = CryptService.INSTANCE.isMatchForOneWay(passInfo);
        if (match) {
            return new OpPasswdVerifyMO(true);
        } else {
            return new OpPasswdVerifyMO(false, OpVerifyErrType.OP_PASSWD_ERROR, DmI18nUtils.getMessage(I18nRdpMsgKeys.VERIFY_OP_PASSWORD_ERROR.name()));
        }
    }

    @Override
    @Transactional(rollbackFor = Throwable.class, propagation = Propagation.REQUIRED)
    public UpdateUserInfoMO updateUserPhone(String uid, UpdateUserPhoneFO fo) {
        DmAuthUserDO consoleUserDO = authDal.userMapper().queryByUid(uid);
        String oldPhone = consoleUserDO.getPhone();
        String newPhone = fo.getPhone();
        if (!oldPhone.equals(newPhone)) {
            DmAuthUserDO phoneUser;
            if (consoleUserDO.getAccountType() == AccountType.PRIMARY_ACCOUNT) {
                phoneUser = authDal.userMapper().queryPrimaryByPhone(newPhone);
            } else if (consoleUserDO.getAccountType() == AccountType.SUB_ACCOUNT) {
                phoneUser = authDal.userMapper().queryByPhoneAndParentId(newPhone, consoleUserDO.getParentId());
            } else {
                throw new IllegalArgumentException("Unsupported accountType:" + consoleUserDO.getAccountType());
            }

            if (phoneUser != null) {
                return new UpdateUserInfoMO(false, DmI18nUtils.getMessage(I18nRdpMsgKeys.USER_PHONE_EXIST_ERROR.name(), newPhone));
            }
        }

        CheckVerifyMO verifyData = new CheckVerifyMO();
        verifyData.setVerifyType(VerifyType.SMS_VERIFY_CODE);
        verifyData.setPhoneNumber(newPhone);
        verifyData.setVerifyCodeType(VerifyCodeType.UPDATE_USER_PHONE);
        verifyData.setVerifyCode(fo.getVerifyCode());
        rdpVerifyService.checkVerifyCode(verifyData);
        // phone console_user,alert_config_detail,system
        authDal.userMapper().updateUserContactInfo(uid, newPhone, null);
        rdpVerifyService.updateEmailOrPhoneByUid(uid, newPhone, null);

        UpdateUserInfoMO mo = new UpdateUserInfoMO(true, null);
        UpdateUserInfoLO lo = new UpdateUserInfoLO();
        lo.setTargetUid(uid);
        lo.setNewPhone(newPhone);
        lo.setOldPhone(oldPhone);
        mo.setConfigLO(lo);
        return mo;
    }

    @Override
    @Transactional(rollbackFor = Throwable.class, propagation = Propagation.REQUIRED)
    public UpdateUserInfoMO updateUserEmail(String uid, UpdateUserEmailFO fo) {
        DmAuthUserDO consoleUserDO = authDal.userMapper().queryByUid(uid);

        if (consoleUserDO == null) {
            throw new IllegalArgumentException("User(" + uid + ") is not exist.");
        }

        String newEmail = fo.getEmail();
        String oldEmail = consoleUserDO.getEmail();
        if (!oldEmail.equals(newEmail)) {
            DmAuthUserDO emailUser = authDal.userMapper().queryPrimaryByEmail(newEmail);
            if (emailUser != null) {
                return new UpdateUserInfoMO(false, DmI18nUtils.getMessage(I18nRdpMsgKeys.USER_EMAIL_EXIST_ERROR.name(), newEmail));
            }
        }

        CheckVerifyMO verifyData = new CheckVerifyMO();
        switch (fo.getVerifyType()) {
            case SMS_VERIFY_CODE:
                verifyData.setVerifyType(VerifyType.SMS_VERIFY_CODE);
                verifyData.setPhoneNumber(consoleUserDO.getPhone());
                verifyData.setVerifyCode(fo.getVerifyCode());
                verifyData.setVerifyCodeType(VerifyCodeType.UPDATE_USER_EMAIL);
                break;
            case EMAIL_VERIFY_CODE:
                verifyData.setVerifyType(VerifyType.EMAIL_VERIFY_CODE);
                verifyData.setEmail(oldEmail);
                verifyData.setVerifyCode(fo.getVerifyCode());
                verifyData.setVerifyCodeType(VerifyCodeType.UPDATE_USER_EMAIL);
                break;
            default:
                return new UpdateUserInfoMO(false, DmI18nUtils.getMessage(I18nRdpMsgKeys.VERIFY_UNSUPPORTED_TYPE_ERROR.name()));
        }

        rdpVerifyService.checkVerifyCode(verifyData);
        // phone console_user,alert_config_detail,system
        authDal.userMapper().updateUserContactInfo(uid, null, newEmail);
        rdpVerifyService.updateEmailOrPhoneByUid(uid, null, newEmail);

        UpdateUserInfoMO mo = new UpdateUserInfoMO(true, null);
        UpdateUserInfoLO lo = new UpdateUserInfoLO();
        lo.setTargetUid(uid);
        lo.setNewEmail(newEmail);
        lo.setOldEmail(oldEmail);
        mo.setConfigLO(lo);
        return mo;
    }

    @Override
    @Transactional(rollbackFor = Throwable.class, propagation = Propagation.REQUIRED)
    public UpdateUserInfoMO updateUserPhoneWithPwd(String uid, UpdateUserPhoneWithPwdFO fo) {
        DmAuthUserDO userDO = this.authDal.userMapper().queryByUid(uid);

        boolean re = checkPassword(userDO, fo.getPassword());

        UpdateUserInfoMO mo = new UpdateUserInfoMO();
        if (re) {
            mo.setSuccess(false);
            mo.setErrorMsg(DmI18nUtils.getMessage(I18nRdpMsgKeys.USER_ORIGIN_PASSWORD_ERROR.name()));
            return mo;
        }

        String oldPhone = userDO.getPhone();
        String newPhone = fo.getPhone();
        if (!oldPhone.equals(newPhone)) {
            DmAuthUserDO phoneUser;
            if (userDO.getAccountType() == AccountType.PRIMARY_ACCOUNT) {
                phoneUser = authDal.userMapper().queryPrimaryByPhone(newPhone);
            } else if (userDO.getAccountType() == AccountType.SUB_ACCOUNT) {
                phoneUser = authDal.userMapper().queryByPhoneAndParentId(newPhone, userDO.getParentId());
            } else {
                throw new IllegalArgumentException("Unsupported accountType:" + userDO.getAccountType());
            }

            if (phoneUser != null) {
                return new UpdateUserInfoMO(false, DmI18nUtils.getMessage(I18nRdpMsgKeys.USER_PHONE_EXIST_ERROR.name(), newPhone));
            }
        }

        authDal.userMapper().updateUserContactInfo(uid, newPhone, null);
        rdpVerifyService.updateEmailOrPhoneByUid(uid, newPhone, null);

        UpdateUserInfoLO lo = new UpdateUserInfoLO();
        lo.setTargetUid(uid);
        lo.setNewPhone(newPhone);
        lo.setOldPhone(oldPhone);
        mo.setConfigLO(lo);
        mo.setSuccess(true);
        mo.setErrorMsg(null);
        return mo;
    }

    private boolean checkPassword(DmAuthUserDO userDO, String password) {
        if (userDO.getAccountType() == AccountType.PRIMARY_ACCOUNT) {
            return RdpAuthUtils.isErrorPasswd(userDO.getPassword(), password);
        } else if (userDO.getBindType() == AccountBindType.INTERNAL) {
            return RdpAuthUtils.isErrorPasswd(userDO.getPassword(), password);
        }

        // other
        LoginProvider providerType = userDO.getBindType().getProvider();
        LoginProviderSpi loginProviderSpi = PluginManager.findSpi(LoginProviderSpi.class, providerType.name());
        if (loginProviderSpi == null) {
            throw new ErrorMessageException(DmI18nUtils.getMessage(I18nRdpMsgKeys.LOGIN_FAIL_UNSUPPORTED_SUBACCOUNT_LOGIN_TYPE.name()));
        }

        DmAuthUserDO primaryUser = this.authDal.userMapper().queryPrimaryByDomain(userDO.getUserDomain());
        LoginRequest request = new LoginRequest();
        request.setLoginAccount(userDO.getUsername());
        request.setLoginPassword(password);
        request.setLoginVerifyCode(null);
        LoginResponse authUserDTO = loginProviderSpi.authLogin(primaryUser.getUid(), request);
        return !authUserDTO.isSuccess();
    }

    @Override
    @Transactional(rollbackFor = Throwable.class, propagation = Propagation.REQUIRED)
    public UpdateUserInfoMO updateUserEmailWithPwd(String uid, UpdateUserEmailWithPwdFO fo) {
        DmAuthUserDO userDO = this.authDal.userMapper().queryByUid(uid);
        boolean re = checkPassword(userDO, fo.getPassword());

        UpdateUserInfoMO mo = new UpdateUserInfoMO();
        if (re) {
            mo.setSuccess(false);
            mo.setErrorMsg(DmI18nUtils.getMessage(I18nRdpMsgKeys.USER_ORIGIN_PASSWORD_ERROR.name()));
            return mo;
        }

        String newEmail = fo.getEmail();
        String oldEmail = userDO.getEmail();
        if (!oldEmail.equals(newEmail)) {
            DmAuthUserDO emailUser = authDal.userMapper().queryPrimaryByEmail(newEmail);
            if (emailUser != null) {
                return new UpdateUserInfoMO(false, DmI18nUtils.getMessage(I18nRdpMsgKeys.USER_EMAIL_EXIST_ERROR.name(), newEmail));
            }
        }

        authDal.userMapper().updateUserContactInfo(uid, null, newEmail);
        rdpVerifyService.updateEmailOrPhoneByUid(uid, null, newEmail);

        UpdateUserInfoLO lo = new UpdateUserInfoLO();
        lo.setTargetUid(uid);
        lo.setNewEmail(newEmail);
        lo.setOldEmail(oldEmail);
        mo.setConfigLO(lo);
        mo.setSuccess(true);
        mo.setErrorMsg(null);
        return mo;
    }

    @Override
    public void updateAliyunAkSk(String puid, String ak, String sk) {
        String encryptAliyunSk = CryptService.INSTANCE.encryptUseDefaultKeyAndSalt(sk);
        this.authDal.userMapper().updateUserAliyunAkSk(puid, ak, encryptAliyunSk);
    }

    @Override
    public void cleanAliyunAkSk(String puid) {
        this.authDal.userMapper().updateUserAliyunAkSk(puid, null, null);
    }

    @Override
    public ResWebData<RdpUserAkSkVO> queryAkSk(String puid, QueryUserAkSkFO fo) {
        DmAuthUserDO userDO = this.authDal.userMapper().queryByUid(puid);
        CheckVerifyMO verifyData = new CheckVerifyMO();
        switch (fo.getVerifyType()) {
            case SMS_VERIFY_CODE:
                verifyData.setVerifyType(VerifyType.SMS_VERIFY_CODE);
                verifyData.setPhoneNumber(userDO.getPhone());
                verifyData.setVerifyCodeType(VerifyCodeType.FETCH_USER_AK_SK);
                verifyData.setVerifyCode(fo.getVerifyCode());
                break;
            case EMAIL_VERIFY_CODE:
                verifyData.setVerifyType(VerifyType.EMAIL_VERIFY_CODE);
                verifyData.setEmail(userDO.getEmail());
                verifyData.setVerifyCodeType(VerifyCodeType.FETCH_USER_AK_SK);
                verifyData.setVerifyCode(fo.getVerifyCode());
                break;
            default:
                return ResWebDataUtils.buildError(DmI18nUtils.getMessage(I18nRdpMsgKeys.VERIFY_UNSUPPORTED_TYPE_ERROR.name()));
        }

        this.rdpVerifyService.checkVerifyCode(verifyData);

        //use parent user
        DmAuthUserDO parentUserDO = this.authDal.userMapper().queryByUid(puid);
        RdpUserAkSkVO akSkVO = new RdpUserAkSkVO();
        akSkVO.setAccessKey(parentUserDO.getAccessKey());
        akSkVO.setSecretKey(CryptService.INSTANCE.decryptUseDefaultKeyAndSalt(parentUserDO.getSecretKey()));
        return ResWebDataUtils.buildSuccess(akSkVO);
    }

    @Override
    public ResWebData<String> resetAkSk(String puid, ResetUserAkSkFO fo) {
        DmAuthUserDO userDO = this.authDal.userMapper().queryByUid(puid);
        CheckVerifyMO verifyData = new CheckVerifyMO();
        switch (fo.getVerifyType()) {
            case SMS_VERIFY_CODE:
                verifyData.setVerifyType(VerifyType.SMS_VERIFY_CODE);
                verifyData.setPhoneNumber(userDO.getPhone());
                verifyData.setVerifyCodeType(VerifyCodeType.RESET_USER_AK_SK);
                verifyData.setVerifyCode(fo.getVerifyCode());
                break;
            case EMAIL_VERIFY_CODE:
                verifyData.setVerifyType(VerifyType.EMAIL_VERIFY_CODE);
                verifyData.setEmail(userDO.getEmail());
                verifyData.setVerifyCodeType(VerifyCodeType.RESET_USER_AK_SK);
                verifyData.setVerifyCode(fo.getVerifyCode());
                break;
            default:
                return ResWebDataUtils.buildError(DmI18nUtils.getMessage(I18nRdpMsgKeys.VERIFY_UNSUPPORTED_TYPE_ERROR.name()));
        }

        this.rdpVerifyService.checkVerifyCode(verifyData);

        //use parent user
        String newAccessKey = this.rdpNamingService.genAccessKey();
        String newSecretKey = CryptService.INSTANCE.encryptUseDefaultKeyAndSalt(this.rdpNamingService.genSecretKey());
        this.authDal.userMapper().updateUserAkSk(puid, newAccessKey, newSecretKey);
        return ResWebDataUtils.buildSuccess("OK");
    }

    public Long addSubAccountForSaasManagedBind(String managedUid, AccountBindType bindType, DmAuthUserDO primaryUser) {
        String generatePwd = Long.toHexString(System.currentTimeMillis()) + "!@#";
        DmAuthUserDO managedUser = this.authDal.userMapper().queryByUid(managedUid);
        DmAuthRoleDO devRoleOfManager = findDevRoleForSaasManagedUser(managedUid);

        String managedUserName = "m_" + primaryUser.getUsername();
        String managedSubAccount = primaryUser.getUid() + "@" + managedUser.getUserDomain();
        String managedSubEmail = "m_" + primaryUser.getEmail();
        String managedSubPhone = "m_" + primaryUser.getPhone();

        AddSubAccountFO fo = new AddSubAccountFO();
        fo.setUserName(managedUserName);
        fo.setSubAccount(managedSubAccount);
        fo.setRoleId(devRoleOfManager.getId());
        fo.setPassword(generatePwd);
        fo.setEmail(managedSubEmail);
        fo.setPhone(managedSubPhone);

        if (GlobalDeploySite.outChina()) {
            this.addSubAccountCheck(fo, managedUser, false, true);
        } else {
            this.addSubAccountCheck(fo, managedUser, true, false);
        }

        DmAuthUserDO userDO = new DmAuthUserDO();
        userDO.setUid(this.rdpNamingService.genUid());
        userDO.setCompany(primaryUser.getCompany());
        userDO.setUsername(managedUserName);
        userDO.setSubAccount(managedSubAccount);
        userDO.setEmail(managedSubEmail);
        userDO.setPhone(managedSubPhone);
        userDO.setPassword(CryptService.INSTANCE.encryptForOneWay(generatePwd).getEncryptPassword());
        userDO.setAccountType(AccountType.SUB_ACCOUNT);
        userDO.setBindType(bindType);
        userDO.setBindAccount(primaryUser.getUid());
        userDO.setParentId(managedUser.getId());
        userDO.setRoleId(devRoleOfManager.getId());
        userDO.setUserDomain(primaryUser.getUserDomain());
        userDO.setAccessKey(this.rdpNamingService.genAccessKey());
        userDO.setSecretKey(CryptService.INSTANCE.encryptUseDefaultKeyAndSalt(this.rdpNamingService.genSecretKey()));
        this.authDal.userMapper().insert(userDO);
        this.rdpUserConfigService.initSubAccountConfigs(userDO.getUid());
        this.notifyServices.forEach(s -> s.notifyUser(managedUid, userDO.getUid(), UserOperationType.ADD));

        return userDO.getId();
    }

    public DmAuthRoleDO findDevRoleForSaasManagedUser(String managedUid) {
        String roleName = SecSysRole.CC_SAAS_DEV_NAME;
        List<DmAuthRoleDO> roles = this.authDal.roleMapper().queryByRoleName(managedUid, roleName);
        DmAuthRoleDO role = CollectionUtils.isEmpty(roles) ? null : roles.get(0);
        if (role == null) {
            String msg = "User(" + managedUid + ") have no " + roleName + " role.";
            log.info(msg);
            throw new IllegalArgumentException(msg);
        }

        return role;
    }

    //
    // -- for Other Manager
    //

    @Override
    public UpdateUserInfoMO resetPassword(ResetPasswdFO fo) {
        DmAuthUserDO userDO = null;
        if (fo.getAccountType() == AccountType.PRIMARY_ACCOUNT) {
            if (fo.getVerifyType() == VerifyType.SMS_VERIFY_CODE) {
                userDO = this.authDal.userMapper().queryPrimaryByPhone(fo.getPhone());
            } else if (fo.getVerifyType() == VerifyType.EMAIL_VERIFY_CODE) {
                userDO = this.authDal.userMapper().queryPrimaryByEmail(fo.getEmail());
            } else {
                throw new IllegalArgumentException("Unsupported verify type:" + fo.getVerifyType());
            }
        } else if (fo.getAccountType() == AccountType.SUB_ACCOUNT) {
            if (StringUtils.isBlank(fo.getSubAccount())) {
                return new UpdateUserInfoMO(false, DmI18nUtils.getMessage(I18nRdpMsgKeys.USER_ACCOUNT_EMPTY_ERROR.name()));
            }

            userDO = this.authDal.userMapper().queryBySubAccount(fo.getSubAccount());
        }

        if (userDO == null) {
            return new UpdateUserInfoMO(false, DmI18nUtils.getMessage(I18nRdpMsgKeys.USER_NOT_EXIST_ERROR.name()));
        }

        if (userDO.isDisable()) {
            return new UpdateUserInfoMO(false, DmI18nUtils.getMessage(I18nRdpMsgKeys.USER_IS_DISABLED_ERROR.name()));
        }

        CheckVerifyMO verifyMO = new CheckVerifyMO();
        verifyMO.setSubAccount(fo.getAccountType() == AccountType.SUB_ACCOUNT);
        verifyMO.setSubAccountName(fo.getSubAccount());
        verifyMO.setPhoneNumber(fo.getPhone());
        verifyMO.setVerifyCode(fo.getVerifyCode());
        verifyMO.setVerifyType(fo.getVerifyType());
        verifyMO.setEmail(fo.getEmail());
        verifyMO.setVerifyCodeType(VerifyCodeType.RESET_PASSWORD);

        rdpVerifyService.checkVerifyCode(verifyMO);

        String password = CryptService.INSTANCE.encryptForOneWay(fo.getPassword()).getEncryptPassword();
        authDal.userMapper().updatePasswdById(userDO.getId(), password);

        return new UpdateUserInfoMO(true, null);
    }

    @Override
    public UpdateUserInfoMO resetPwdWithOriginPwd(ResetPwdWithOriginPwdFO fo, String targetUid, String puid) {
        DmAuthUserDO userDO = this.authDal.userMapper().queryByUid(targetUid);

        UpdateUserInfoMO mo = new UpdateUserInfoMO();
        boolean notSame = RdpAuthUtils.isErrorPasswd(userDO.getPassword(), fo.getNewPassword());
        if (!notSame) {
            //can not reset with same password
            mo.setSuccess(false);
            mo.setErrorMsg(DmI18nUtils.getMessage(I18nRdpMsgKeys.RESET_WITH_SAME_PASSWORD_ERROR.name()));
            return mo;
        }

        ValidateResultMO validatePwdMO = null;
        if (userDO.getAccountType() == AccountType.PRIMARY_ACCOUNT) {
            validatePwdMO = validatePrimaryAccountPwd(fo.getNewPassword());
        } else if (userDO.getAccountType() == AccountType.SUB_ACCOUNT) {
            validatePwdMO = validateSubAccountPwd(puid, fo.getNewPassword());
        }

        if (validatePwdMO != null && !validatePwdMO.isSuccess()) {
            mo.setSuccess(false);
            mo.setErrorMsg(validatePwdMO.getErrorMsg());
            return mo;
        }

        boolean re = RdpAuthUtils.isErrorPasswd(userDO.getPassword(), fo.getOriginPassword());
        if (re) {
            mo.setSuccess(false);
            mo.setErrorMsg(DmI18nUtils.getMessage(I18nRdpMsgKeys.USER_ORIGIN_PASSWORD_ERROR.name()));
            return mo;
        }

        String encryptPwd = CryptService.INSTANCE.encryptForOneWay(fo.getNewPassword()).getEncryptPassword();
        authDal.userMapper().updatePasswdById(userDO.getId(), encryptPwd);

        mo.setSuccess(true);
        return mo;
    }

    @Override
    public UpdateUserInfoMO resetSubAccountPwd(ResetSubAccountPwdFO fo, String operatorUid) {
        DmAuthUserDO opUserDO = this.authDal.userMapper().queryByUid(operatorUid);

        boolean re = RdpAuthUtils.isErrorPasswd(opUserDO.getPassword(), fo.getOperatorPwd());
        UpdateUserInfoMO mo = new UpdateUserInfoMO();
        if (re) {
            mo.setSuccess(false);
            mo.setErrorMsg(DmI18nUtils.getMessage(I18nRdpMsgKeys.OPERATOR_PASSWORD_ERROR.name()));
            return mo;
        }

        DmAuthUserDO userDO = this.authDal.userMapper().queryByUid(fo.getSubAccountUid());

        String encryptPwd = CryptService.INSTANCE.encryptForOneWay(fo.getNewPassword()).getEncryptPassword();
        authDal.userMapper().updatePasswdById(userDO.getId(), encryptPwd);

        mo.setSuccess(true);
        return mo;
    }

    @Override
    public List<DmAuthUserDO> listSubAccounts(String puid) {
        DmAuthUserDO parentUser = this.authDal.userMapper().queryByUid(puid);
        return this.authDal.userMapper().listByParentId(parentUser.getId());
    }

    @Override
    public List<ListUserVO> listSubAccounts(String puid, ListSubAccountsFO fo) {
        DmAuthUserDO parentUser = this.authDal.userMapper().queryByUid(puid);

        String prefix = StringUtils.isBlank(fo.getUserNameOrSubAccountPrefix()) ? null : fo.getUserNameOrSubAccountPrefix();
        List<DmAuthUserDO> subAccounts = this.authDal.userMapper().listByCondition(parentUser.getId(), fo.getRoleId(), prefix);
        List<DmAuthRoleDO> roles = this.rdpRoleService.listRoleByUID(puid);
        Map<Long, DmAuthRoleDO> roleMap = new HashMap<>();
        for (DmAuthRoleDO role : roles) {
            roleMap.put(role.getId(), role);
        }

        return subAccounts.stream().map(u -> RdpConvertUtils.convertToListUserVO(u, roleMap)).collect(Collectors.toList());
    }

    private void addSubAccountCheck(AddSubAccountFO accountFO, DmAuthUserDO primaryUser, boolean skipMailCheck, boolean skipPhoneCheck) {
        //this.rdpLicenseCheckService.checkSubAccountCount();

        DmAuthUserDO user = this.authDal.userMapper().queryBySubAccount(accountFO.getSubAccount());
        if (user != null) {
            throw new ErrorMessageException(DmI18nUtils.getMessage(I18nRdpMsgKeys.USER_ADD_EXIST_ERROR.name(), accountFO.getSubAccount()));
        }

        if (!skipMailCheck) {
            DmAuthUserDO emailUser = this.authDal.userMapper().queryByEmailAndParentId(accountFO.getEmail(), primaryUser.getId());
            if (emailUser != null) {
                throw new ErrorMessageException(DmI18nUtils.getMessage(I18nRdpMsgKeys.USER_ADD_EXIST_ERROR.name(), accountFO.getEmail()));
            }
        }

        if (!skipPhoneCheck) {
            DmAuthUserDO phoneUser = this.authDal.userMapper().queryByPhoneAndParentId(accountFO.getPhone(), primaryUser.getId());
            if (phoneUser != null) {
                throw new ErrorMessageException(DmI18nUtils.getMessage(I18nRdpMsgKeys.USER_ADD_EXIST_ERROR.name(), accountFO.getPhone()));
            }
        }

        DmAuthRoleDO roleDO = this.rdpRoleService.fetchRoleById(accountFO.getRoleId());
        if (roleDO == null) {
            throw new ErrorMessageException(DmI18nUtils.getMessage(I18nRdpMsgKeys.USER_ROLE_NOT_EXIST_ERROR.name()));
        }

        if (!roleDO.getOwnerUid().equals(primaryUser.getUid())) {
            throw new ErrorMessageException(DmI18nUtils.getMessage(I18nRdpMsgKeys.USER_ROLE_IS_NOT_BELONG_YOU_ERROR.name()));
        }

        ValidateResultMO pwdMO = validateSubAccountPwd(primaryUser.getUid(), accountFO.getPassword());
        if (pwdMO != null && !pwdMO.isSuccess()) {
            throw new ErrorMessageException(pwdMO.getErrorMsg());
        }
    }

    @Transactional(rollbackFor = Throwable.class, propagation = Propagation.REQUIRED)
    @Override
    public AddSubAccountMO addSubAccountForBind(String puid, AccountBindType bindType, DmAuthUserDO bindUser) {
        String generatePwd = Long.toHexString(System.currentTimeMillis()) + "!@#";
        DmAuthUserDO primaryUser = this.authDal.userMapper().queryByUid(puid);

        try {
            AddSubAccountFO fo = new AddSubAccountFO();
            fo.setUserName(bindUser.getUsername());
            fo.setSubAccount(bindUser.getSubAccount());
            fo.setRoleId(bindUser.getRoleId());
            fo.setPassword(generatePwd);
            fo.setEmail(bindUser.getEmail());
            fo.setPhone(bindUser.getPhone());
            this.addSubAccountCheck(fo, primaryUser, false, false);
        } catch (ErrorMessageException e) {
            return new AddSubAccountMO(false, e.getErrorMessage());
        }

        DmAuthUserDO userDO = new DmAuthUserDO();
        userDO.setUid(this.rdpNamingService.genUid());
        userDO.setCompany(primaryUser.getCompany());
        userDO.setUsername(bindUser.getUsername());
        userDO.setSubAccount(bindUser.getSubAccount());
        userDO.setEmail(bindUser.getEmail());
        userDO.setPhone(bindUser.getPhone());
        userDO.setPassword(CryptService.INSTANCE.encryptForOneWay(generatePwd).getEncryptPassword());
        userDO.setAccountType(AccountType.SUB_ACCOUNT);
        userDO.setBindType(bindType);
        userDO.setBindAccount(bindUser.getBindAccount());
        userDO.setParentId(primaryUser.getId());
        userDO.setRoleId(bindUser.getRoleId());
        userDO.setUserDomain(primaryUser.getUserDomain());
        userDO.setAccessKey(this.rdpNamingService.genAccessKey());
        userDO.setSecretKey(CryptService.INSTANCE.encryptUseDefaultKeyAndSalt(this.rdpNamingService.genSecretKey()));
        this.authDal.userMapper().insert(userDO);
        //        verifyService.initUserVerify(userDO);

        this.rdpUserConfigService.initSubAccountConfigs(userDO.getUid());

        this.notifyServices.forEach(s -> s.notifyUser(puid, userDO.getUid(), UserOperationType.ADD));
        return new AddSubAccountMO(true, null, userDO.getUid());
    }

    @Transactional(rollbackFor = Throwable.class, propagation = Propagation.REQUIRED)
    @Override
    public AddSubAccountMO addSubAccountForInternal(String puid, AddSubAccountFO fo) {
        DmAuthUserDO primaryUser = this.authDal.userMapper().queryByUid(puid);

        try {
            this.addSubAccountCheck(fo, primaryUser, false, false);
        } catch (ErrorMessageException e) {
            return new AddSubAccountMO(false, e.getErrorMessage());
        }

        DmAuthUserDO userDO = new DmAuthUserDO();
        userDO.setUid(this.rdpNamingService.genUid());
        userDO.setCompany(primaryUser.getCompany());
        userDO.setUsername(fo.getUserName());
        userDO.setSubAccount(fo.getSubAccount());
        userDO.setEmail(fo.getEmail());
        userDO.setPhone(fo.getPhone());
        userDO.setPassword(CryptService.INSTANCE.encryptForOneWay(fo.getPassword()).getEncryptPassword());
        userDO.setAccountType(AccountType.SUB_ACCOUNT);
        userDO.setBindType(AccountBindType.INTERNAL);
        userDO.setBindAccount("");
        userDO.setParentId(primaryUser.getId());
        userDO.setRoleId(fo.getRoleId());
        userDO.setUserDomain(primaryUser.getUserDomain());
        userDO.setAccessKey(this.rdpNamingService.genAccessKey());
        userDO.setSecretKey(CryptService.INSTANCE.encryptUseDefaultKeyAndSalt(this.rdpNamingService.genSecretKey()));
        this.authDal.userMapper().insert(userDO);
        //        verifyService.initUserVerify(userDO);

        this.rdpUserConfigService.initSubAccountConfigs(userDO.getUid());

        this.notifyServices.forEach(s -> s.notifyUser(puid, userDO.getUid(), UserOperationType.ADD));
        return new AddSubAccountMO(true, null, userDO.getUid());
    }

    @Override
    public UpdateUserInfoMO updateSubAccount(UpdateSubAccountFO fo, String puid) {
        UpdateUserInfoMO mo = new UpdateUserInfoMO();

        if (StringUtils.isBlank(fo.getSubAccount()) && StringUtils.isBlank(fo.getUserName())) {
            mo.setSuccess(false);
            mo.setErrorMsg(DmI18nUtils.getMessage(I18nRdpMsgKeys.USER_INFO_EMPTY_ERROR.name()));
            return mo;
        }

        if (StringUtils.isNotBlank(fo.getSubAccount())) {
            DmAuthUserDO userWithNewSubAccount = this.authDal.userMapper().queryBySubAccount(fo.getSubAccount());
            if (userWithNewSubAccount != null && !userWithNewSubAccount.getUid().equals(fo.getTargetUid())) {
                mo.setSuccess(false);
                mo.setErrorMsg(DmI18nUtils.getMessage(I18nRdpMsgKeys.REGISTER_ACCOUNT_EXIST_ERROR.name(), fo.getSubAccount()));
                return mo;
            }
        }

        DmAuthUserDO oldUser = this.authDal.userMapper().queryByUid(fo.getTargetUid());
        if (oldUser == null) {
            mo.setSuccess(false);
            mo.setErrorMsg(DmI18nUtils.getMessage(I18nRdpMsgKeys.USER_NOT_EXIST_ERROR.name()));
            return mo;
        }

        UpdateUserInfoLO lo = new UpdateUserInfoLO();
        lo.setTargetUid(fo.getTargetUid());
        lo.setOldSubAccount(oldUser.getSubAccount());
        lo.setOldUserName(oldUser.getUsername());
        lo.setNewSubAccount(fo.getSubAccount());
        lo.setNewUserName(fo.getUserName());

        this.authDal.userMapper().updateSubAccountAndName(fo.getTargetUid(), fo.getSubAccount(), fo.getUserName());

        mo.setConfigLO(lo);
        mo.setSuccess(true);
        mo.setErrorMsg(null);
        return mo;
    }

    @Override
    public CheckSubAccountMO checkSubAccount(String puid, CheckSubAccountFO fo) {
        String content = fo.getCheckContent();
        if (fo.getCheckType() == CheckSubAccountType.SUB_ACCOUNT) {
            DmAuthUserDO user = this.authDal.userMapper().queryBySubAccount(content);
            if (user != null) {
                return new CheckSubAccountMO(false, DmI18nUtils.getMessage(I18nRdpMsgKeys.REGISTER_ACCOUNT_EXIST_ERROR.name(), content));
            }
        } else if (fo.getCheckType() == CheckSubAccountType.PHONE) {
            DmAuthUserDO parentUser = this.authDal.userMapper().queryByUid(puid);
            DmAuthUserDO phoneUser = this.authDal.userMapper().queryByPhoneAndParentId(content, parentUser.getId());
            if (phoneUser != null) {
                return new CheckSubAccountMO(false, DmI18nUtils.getMessage(I18nRdpMsgKeys.REGISTER_PHONE_EXIST_ERROR.name(), content));
            }
        } else if (fo.getCheckType() == CheckSubAccountType.EMAIL) {
            DmAuthUserDO parentUser = this.authDal.userMapper().queryByUid(puid);
            DmAuthUserDO emailUser = this.authDal.userMapper().queryByEmailAndParentId(content, parentUser.getId());
            if (emailUser != null) {
                return new CheckSubAccountMO(false, DmI18nUtils.getMessage(I18nRdpMsgKeys.REGISTER_EMAIL_EXIST_ERROR.name(), content));
            }
        }

        return new CheckSubAccountMO(true, null);
    }

    @Transactional(rollbackFor = Throwable.class, propagation = Propagation.REQUIRED)
    @Override
    public ResWebData<Boolean> deleteSubAccount(String puid, DeleteSubAccountFO fo) {
        DmAuthUserDO userDO = this.authDal.userMapper().queryBySubAccount(fo.getSubAccount());
        rdpVerifyService.dropUserVerify(userDO.getUid());
        authDal.userMapper().deleteById(userDO.getId());
        authDal.resMapper().deleteByUser(userDO.getUid());
        authDal.mfaMapper().deleteByUid(userDO.getUid());

        this.notifyServices.forEach(s -> s.notifyUser(puid, userDO.getUid(), UserOperationType.DELETE));
        return ResWebDataUtils.buildSuccess();
    }

    @Override
    public UpdateUserRoleLO updateUserRole(UpdateUserRoleFO fo) {
        DmAuthUserDO theUser = this.authDal.userMapper().queryByUid(fo.getSubAccountUid());
        if (theUser == null) {
            throw new ErrorMessageException(DmI18nUtils.getMessage(I18nRdpMsgKeys.USER_NOT_EXIST_ERROR.name()));
        }

        if (theUser.getRoleId() == fo.getRoleId()) {
            return convertToUpdateUserRoleLO(theUser, fo.getRoleId());
        }

        DmAuthRoleDO theRole = this.rdpRoleService.fetchRoleById(fo.getRoleId());
        if (theRole == null) {
            throw new ErrorMessageException(DmI18nUtils.getMessage(I18nRdpMsgKeys.USER_ROLE_NOT_EXIST_ERROR.name()));
        }

        this.authDal.userMapper().updateRoleById(theUser.getId(), fo.getRoleId());
        return convertToUpdateUserRoleLO(theUser, fo.getRoleId());
    }

    private UpdateUserRoleLO convertToUpdateUserRoleLO(DmAuthUserDO rdpUserDO, long newRoleId) {
        DmAuthRoleDO oldRole = this.authDal.roleMapper().selectById(rdpUserDO.getRoleId());
        DmAuthRoleDO newRole = this.authDal.roleMapper().selectById(newRoleId);
        UpdateUserRoleLO lo = new UpdateUserRoleLO();
        lo.setSubAccountUid(rdpUserDO.getUid());
        lo.setOldRoleId(oldRole.getId());
        lo.setOldRoleName(oldRole.getRoleName());
        lo.setNewRoleName(newRole.getRoleName());
        lo.setNewRoleId(newRole.getId());
        return lo;
    }

    @Transactional(rollbackFor = Throwable.class, propagation = Propagation.REQUIRED)
    @Override
    public ResWebData<Boolean> updateAccountAbility(String puid, AccountAbilityFO fo) {
        if (!fo.getDisable()) {
            //rdpLicenseCheckService.checkSubAccountCount();
        }
        this.authDal.userMapper().updateAbilityByUid(fo.getUid(), fo.getDisable());
        if (fo.getDisable()) {
            this.notifyServices.forEach(s -> s.notifyUser(puid, fo.getUid(), UserOperationType.DISABLE));
        } else {
            this.notifyServices.forEach(s -> s.notifyUser(puid, fo.getUid(), UserOperationType.ENABLE));
        }
        return ResWebDataUtils.buildSuccess();
    }

    //
    //
    //
    //
    //
    //

    @Override
    public DmAuthUserDO getUserByAk(String ak) {
        if (StringUtils.isBlank(ak)) {
            return null;
        } else {
            return authDal.userMapper().queryByAccessKey(ak);
        }
    }

    @Override
    public String getPrimaryUid(String uid) {
        DmAuthUserDO userDO = authDal.userMapper().queryByUid(uid);
        if (userDO == null) {
            throw new IllegalArgumentException("uid not exist.");
        }

        if (userDO.getParentId() == null || userDO.getParentId() <= 0) {
            return uid;
        } else {
            DmAuthUserDO parentUserDO = authDal.userMapper().queryById(userDO.getParentId());
            return parentUserDO.getUid();
        }
    }

    @Override
    public DmAuthUserDO getPrimaryUser(String uid) {
        DmAuthUserDO userDO = authDal.userMapper().queryByUid(uid);
        if (userDO == null) {
            throw new IllegalArgumentException("uid not exist.");
        }

        if (userDO.getParentId() == null || userDO.getParentId() <= 0) {
            return userDO;
        } else {
            return authDal.userMapper().queryById(userDO.getParentId());
        }
    }

    @Override
    public List<DmAuthUserDO> listPrimaryUser() {
        return this.authDal.userMapper().listPrimaryAccount();
    }

    @Override
    public UpdateUserInfoMO updateResourceManage(UpdateResourceManageFO fo, String puid) {
        DmAuthUserDO user = this.authDal.userMapper().queryByUid(fo.getTargetUid());
        if (user == null) {
            return new UpdateUserInfoMO(false, DmI18nUtils.getMessage(I18nRdpMsgKeys.USER_NOT_EXIST_ERROR.name(), fo.getTargetUid()));
        }

        this.authDal.userMapper().updateResourceMangeEnable(fo.getTargetUid(), fo.isResourceManage());

        return new UpdateUserInfoMO(true, null);
    }
}
