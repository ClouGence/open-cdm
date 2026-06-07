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
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.clougence.clouddm.api.common.crypt.CryptService;
import com.clougence.clouddm.api.common.crypt.PasswordInfo;
import com.clougence.clouddm.api.common.exception.ErrorMessageException;
import com.clougence.clouddm.api.common.rpc.ResWebData;
import com.clougence.clouddm.api.common.rpc.ResWebDataUtils;
import com.clougence.clouddm.console.web.component.auth.DmAuthLabelService;
import com.clougence.clouddm.console.web.component.auth.DmUserService;
import com.clougence.clouddm.console.web.component.config.UserConfigService;
import com.clougence.clouddm.console.web.constants.CheckSubAccountType;
import com.clougence.clouddm.console.web.global.i18n.DmI18nUtils;
import com.clougence.clouddm.console.web.global.i18n.I18nRdpMsgKeys;
import com.clougence.clouddm.console.web.model.fo.*;
import com.clougence.clouddm.console.web.model.fo.role.UpdateUserRoleFO;
import com.clougence.clouddm.console.web.model.fo.user.*;
import com.clougence.clouddm.console.web.model.lo.UpdateUserInfoLO;
import com.clougence.clouddm.console.web.model.lo.UpdateUserRoleLO;
import com.clougence.clouddm.console.web.model.vo.ListUserVO;
import com.clougence.clouddm.console.web.model.vo.PwdPolicyVO;
import com.clougence.clouddm.console.web.model.vo.RdpUserAkSkVO;
import com.clougence.clouddm.console.web.service.auth.RdpRoleService;
import com.clougence.clouddm.console.web.service.auth.RdpUserService;
import com.clougence.clouddm.console.web.util.RdpAuthUtils;
import com.clougence.clouddm.console.web.util.RdpConvertUtils;
import com.clougence.clouddm.platform.dal.access.AuthDal;
import com.clougence.clouddm.platform.dal.access.NamingDao;
import com.clougence.clouddm.platform.dal.access.SystemDal;
import com.clougence.clouddm.platform.dal.model.auth.*;
import com.clougence.clouddm.platform.plugin.PluginManager;
import com.clougence.clouddm.sdk.security.auth.AuthInfo;
import com.clougence.clouddm.sdk.security.auth.AuthInfoType;
import com.clougence.clouddm.sdk.security.login.LoginProvider;
import com.clougence.clouddm.sdk.security.login.LoginProviderSpi;
import com.clougence.clouddm.sdk.security.login.LoginRequest;
import com.clougence.clouddm.sdk.security.login.LoginResponse;
import com.clougence.rdp.global.config.user.UserDefinedConfig;
import com.clougence.rdp.service.RdpNotifyService;
import com.clougence.rdp.service.RdpVerifyService;
import com.clougence.rdp.service.enumeration.OpVerifyErrType;
import com.clougence.rdp.service.enumeration.UserOperationType;
import com.clougence.rdp.service.model.*;
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
    private NamingDao              namingDao;
    @Resource
    private SystemDal              systemDal;
    @Resource
    private UserConfigService      userConfigService;
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
        List<AuthInfo> catTreeDef = tmpDef;

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
    public PwdPolicyVO getPwdPolicy(String puid) {
        PwdPolicyVO vo = new PwdPolicyVO();
        boolean strongPolicy = false;
        int minLength = DEFAULT_PWD_MIN_LENGTH;
        Boolean strongPolicyConfig = this.systemDal.fetchSystemConf(UserDefinedConfig.Fields.accountPwdStrongPolicy, Boolean.class);
        if (strongPolicyConfig != null) {
            strongPolicy = strongPolicyConfig;
        }
        Integer minLengthConfig = this.systemDal.fetchSystemConf(UserDefinedConfig.Fields.accountPwdMinLength, Integer.class);
        if (minLengthConfig != null) {
            minLength = Math.max(1, minLengthConfig);
        }
        vo.setStrongPolicy(strongPolicy);
        vo.setMinLength(minLength);
        vo.setTips(buildPasswordPolicyTips(strongPolicy, minLength));
        return vo;
    }

    private String buildPasswordPolicyTips(boolean strongPolicy, int minLength) {
        if (strongPolicy) {
            return DmI18nUtils.getMessage(I18nRdpMsgKeys.SUB_ACCOUNT_PWD_STRONG_VALIDATE_TIPS.name(), minLength);
        }
        return DmI18nUtils.getMessage(I18nRdpMsgKeys.SUB_ACCOUNT_PWD_MIN_LENGTH_TIPS.name(), minLength);
    }

    @Override
    public ValidateResultMO validatePrimaryAccountPwd(String pwd) {
        PwdPolicyVO policy = getPwdPolicy(null);
        return validateByPolicy(policy, pwd);
    }

    @Override
    public ValidateResultMO validateSubAccountPwd(String puid, String pwd) {
        PwdPolicyVO policy = getPwdPolicy(puid);
        return validateByPolicy(policy, pwd);
    }

    @Override
    public ValidateResultMO validateByPolicy(PwdPolicyVO policy, String content) {
        if (content == null || content.length() < policy.getMinLength()) {
            return new ValidateResultMO(false, policy.getTips());
        }

        if (Boolean.TRUE.equals(policy.getStrongPolicy())) {
            boolean hasUpper = content.chars().anyMatch(Character::isUpperCase);
            boolean hasLower = content.chars().anyMatch(Character::isLowerCase);
            boolean hasDigit = content.chars().anyMatch(Character::isDigit);
            if (!hasUpper || !hasLower || !hasDigit) {
                return new ValidateResultMO(false, policy.getTips());
            }
        }
        return new ValidateResultMO(true, null);
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
        if (!StringUtils.equals(oldPhone, newPhone)) {
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

        // phone console_user,alert_config_detail,system
        authDal.userMapper().updateUserContactInfo(uid, newPhone, null);

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
        if (!StringUtils.equals(oldEmail, newEmail)) {
            DmAuthUserDO emailUser = authDal.userMapper().queryPrimaryByEmail(newEmail);
            if (emailUser != null) {
                return new UpdateUserInfoMO(false, DmI18nUtils.getMessage(I18nRdpMsgKeys.USER_EMAIL_EXIST_ERROR.name(), newEmail));
            }
        }

        // phone console_user,alert_config_detail,system
        authDal.userMapper().updateUserContactInfo(uid, null, newEmail);

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
    public UpdateUserInfoMO updateUserName(String uid, UpdateUserNameFO fo) {
        DmAuthUserDO user = authDal.userMapper().queryByUid(uid);
        if (user == null) {
            throw new IllegalArgumentException("User(" + uid + ") is not exist.");
        }

        String oldUserName = user.getUsername();
        String newUserName = fo.getUserName();
        authDal.userMapper().updateUserName(uid, newUserName);

        UpdateUserInfoMO mo = new UpdateUserInfoMO(true, null);
        UpdateUserInfoLO lo = new UpdateUserInfoLO();
        lo.setTargetUid(uid);
        lo.setOldUserName(oldUserName);
        lo.setNewUserName(newUserName);
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
        if (!StringUtils.equals(oldPhone, newPhone)) {
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

        DmAuthUserDO primaryUser = this.authDal.userMapper().queryById(userDO.getParentId());
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
        if (!StringUtils.equals(oldEmail, newEmail)) {
            DmAuthUserDO emailUser = authDal.userMapper().queryPrimaryByEmail(newEmail);
            if (emailUser != null) {
                return new UpdateUserInfoMO(false, DmI18nUtils.getMessage(I18nRdpMsgKeys.USER_EMAIL_EXIST_ERROR.name(), newEmail));
            }
        }

        authDal.userMapper().updateUserContactInfo(uid, null, newEmail);

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
    public ResWebData<RdpUserAkSkVO> queryAkSk(String puid, QueryUserAkSkFO fo) {
        //use parent user
        DmAuthUserDO parentUserDO = this.authDal.userMapper().queryByUid(puid);
        RdpUserAkSkVO akSkVO = new RdpUserAkSkVO();
        akSkVO.setAccessKey(parentUserDO.getAccessKey());
        akSkVO.setSecretKey(CryptService.INSTANCE.decryptUseDefaultKeyAndSalt(parentUserDO.getSecretKey()));
        return ResWebDataUtils.buildSuccess(akSkVO);
    }

    @Override
    public ResWebData<String> resetAkSk(String puid, ResetUserAkSkFO fo) {
        //use parent user
        String newAccessKey = this.namingDao.genAccessKey();
        String newSecretKey = CryptService.INSTANCE.encryptUseDefaultKeyAndSalt(this.namingDao.genSecretKey());
        this.authDal.userMapper().updateUserAkSk(puid, newAccessKey, newSecretKey);
        return ResWebDataUtils.buildSuccess("OK");
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
            if (StringUtils.isBlank(fo.getAccount())) {
                return new UpdateUserInfoMO(false, DmI18nUtils.getMessage(I18nRdpMsgKeys.USER_ACCOUNT_EMPTY_ERROR.name()));
            }

            userDO = this.authDal.userMapper().queryBySubAccount(fo.getAccount());
        }

        if (userDO == null) {
            return new UpdateUserInfoMO(false, DmI18nUtils.getMessage(I18nRdpMsgKeys.USER_NOT_EXIST_ERROR.name()));
        }

        if (userDO.isDisable()) {
            return new UpdateUserInfoMO(false, DmI18nUtils.getMessage(I18nRdpMsgKeys.USER_IS_DISABLED_ERROR.name()));
        }

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

        String duplicateError = checkSubAccountUniqueOnSave(accountFO.getAccount(), accountFO.getPhone(), accountFO.getEmail(), primaryUser, null, skipMailCheck, skipPhoneCheck);
        if (duplicateError != null) {
            throw new ErrorMessageException(duplicateError);
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

    private String checkSubAccountUniqueOnSave(String account, String phone, String email, DmAuthUserDO primaryUser, String targetUid, boolean skipMailCheck,
                                               boolean skipPhoneCheck) {
        if (StringUtils.isNotBlank(account)) {
            DmAuthUserDO userWithSameAccount = StringUtils.isBlank(targetUid) ? //
                this.authDal.userMapper().queryBySubAccount(account) : //
                this.authDal.userMapper().queryBySubAccountExcludeUid(account, targetUid);
            if (isDuplicateUser(userWithSameAccount, targetUid)) {
                return DmI18nUtils.getMessage(I18nRdpMsgKeys.REGISTER_ACCOUNT_EXIST_ERROR.name(), account);
            }
        }
        if (!skipPhoneCheck && StringUtils.isNotBlank(phone)) {
            if (StringUtils.equals(phone, primaryUser.getPhone()) && isDuplicateUser(primaryUser, targetUid)) {
                return DmI18nUtils.getMessage(I18nRdpMsgKeys.REGISTER_PHONE_EXIST_ERROR.name(), phone);
            }
            DmAuthUserDO userWithSamePhone = StringUtils.isBlank(targetUid) ? //
                this.authDal.userMapper().queryByPhoneAndParentId(phone, primaryUser.getId()) ://
                this.authDal.userMapper().queryByPhoneAndParentIdExcludeUid(phone, primaryUser.getId(), targetUid);
            if (isDuplicateUser(userWithSamePhone, targetUid)) {
                return DmI18nUtils.getMessage(I18nRdpMsgKeys.REGISTER_PHONE_EXIST_ERROR.name(), phone);
            }
        }
        if (!skipMailCheck && StringUtils.isNotBlank(email)) {
            if (StringUtils.equals(email, primaryUser.getEmail()) && isDuplicateUser(primaryUser, targetUid)) {
                return DmI18nUtils.getMessage(I18nRdpMsgKeys.REGISTER_EMAIL_EXIST_ERROR.name(), email);
            }
            DmAuthUserDO userWithSameEmail = StringUtils.isBlank(targetUid) ? //
                this.authDal.userMapper().queryByEmailAndParentId(email, primaryUser.getId()) ://
                this.authDal.userMapper().queryByEmailAndParentIdExcludeUid(email, primaryUser.getId(), targetUid);
            if (isDuplicateUser(userWithSameEmail, targetUid)) {
                return DmI18nUtils.getMessage(I18nRdpMsgKeys.REGISTER_EMAIL_EXIST_ERROR.name(), email);
            }
        }
        return null;
    }

    @Transactional(rollbackFor = Throwable.class, propagation = Propagation.REQUIRED)
    @Override
    public AddSubAccountMO addSubAccountForBind(String puid, AccountBindType bindType, DmAuthUserDO bindUser) {
        String generatePwd = Long.toHexString(System.currentTimeMillis()) + "!@#";
        DmAuthUserDO primaryUser = this.authDal.userMapper().queryByUid(puid);

        try {
            AddSubAccountFO fo = new AddSubAccountFO();
            fo.setUserName(bindUser.getUsername());
            fo.setAccount(null);
            fo.setRoleId(bindUser.getRoleId());
            fo.setPassword(generatePwd);
            fo.setEmail(bindUser.getEmail());
            fo.setPhone(bindUser.getPhone());
            this.addSubAccountCheck(fo, primaryUser, false, false);
        } catch (ErrorMessageException e) {
            return new AddSubAccountMO(false, e.getErrorMessage());
        }

        DmAuthUserDO userDO = new DmAuthUserDO();
        userDO.setUid(this.namingDao.genUID());
        userDO.setUsername(bindUser.getUsername());
        userDO.setAccount(bindUser.getAccount());
        userDO.setEmail(bindUser.getEmail());
        userDO.setPhone(bindUser.getPhone());
        userDO.setPassword(CryptService.INSTANCE.encryptForOneWay(generatePwd).getEncryptPassword());
        userDO.setAccountType(AccountType.SUB_ACCOUNT);
        userDO.setBindType(bindType);
        userDO.setBindAccount(bindUser.getBindAccount());
        userDO.setAllowLocal(false);
        userDO.setParentId(primaryUser.getId());
        userDO.setRoleId(bindUser.getRoleId());
        userDO.setAccessKey(this.namingDao.genAccessKey());
        userDO.setSecretKey(CryptService.INSTANCE.encryptUseDefaultKeyAndSalt(this.namingDao.genSecretKey()));
        this.authDal.userMapper().insert(userDO);
        //        verifyService.initUserVerify(userDO);

        this.userConfigService.initSubAccountConfigs(userDO.getUid());

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
        userDO.setUid(this.namingDao.genUID());
        userDO.setUsername(fo.getUserName());
        userDO.setAccount(fo.getAccount());
        userDO.setEmail(fo.getEmail());
        userDO.setPhone(fo.getPhone());
        userDO.setPassword(CryptService.INSTANCE.encryptForOneWay(fo.getPassword()).getEncryptPassword());
        userDO.setAccountType(AccountType.SUB_ACCOUNT);
        userDO.setBindType(AccountBindType.INTERNAL);
        userDO.setBindAccount("");
        userDO.setAllowLocal(true);
        userDO.setParentId(primaryUser.getId());
        userDO.setRoleId(fo.getRoleId());
        userDO.setDisable(Boolean.TRUE.equals(fo.getDisable()));
        boolean loginLocked = Boolean.TRUE.equals(fo.getLoginLocked());
        userDO.setLoginLocked(loginLocked);
        userDO.setLoginFailCount(loginLocked ? 1 : 0);
        userDO.setLastTryLoginTime(loginLocked ? new Date() : new Date(0));
        userDO.setAccessKey(this.namingDao.genAccessKey());
        userDO.setSecretKey(CryptService.INSTANCE.encryptUseDefaultKeyAndSalt(this.namingDao.genSecretKey()));
        this.authDal.userMapper().insert(userDO);
        //        verifyService.initUserVerify(userDO);

        this.userConfigService.initSubAccountConfigs(userDO.getUid());

        this.notifyServices.forEach(s -> s.notifyUser(puid, userDO.getUid(), UserOperationType.ADD));
        return new AddSubAccountMO(true, null, userDO.getUid());
    }

    @Override
    public UpdateUserInfoMO updateSubAccount(UpdateSubAccountFO fo, String puid) {
        UpdateUserInfoMO mo = new UpdateUserInfoMO();

        DmAuthUserDO oldUser = this.authDal.userMapper().queryByUid(fo.getTargetUid());
        if (oldUser == null) {
            mo.setSuccess(false);
            mo.setErrorMsg(DmI18nUtils.getMessage(I18nRdpMsgKeys.USER_NOT_EXIST_ERROR.name()));
            return mo;
        }
        DmAuthUserDO primaryUser = this.authDal.userMapper().queryByUid(puid);
        if (fo.getRoleId() != null) {
            DmAuthRoleDO roleDO = this.rdpRoleService.fetchRoleById(fo.getRoleId());
            if (roleDO == null) {
                mo.setSuccess(false);
                mo.setErrorMsg(DmI18nUtils.getMessage(I18nRdpMsgKeys.USER_ROLE_NOT_EXIST_ERROR.name()));
                return mo;
            }
            if (!roleDO.getOwnerUid().equals(primaryUser.getUid())) {
                mo.setSuccess(false);
                mo.setErrorMsg(DmI18nUtils.getMessage(I18nRdpMsgKeys.USER_ROLE_IS_NOT_BELONG_YOU_ERROR.name()));
                return mo;
            }
        }
        boolean internalAccount = oldUser.getBindType() == AccountBindType.INTERNAL;
        boolean allowLocal = internalAccount || (fo.getAllowLocal() == null ? oldUser.isAllowLocal() : Boolean.TRUE.equals(fo.getAllowLocal()));
        String localAccount = fo.getAccount() == null ? oldUser.getAccount() : fo.getAccount();
        if (internalAccount || allowLocal) {
            if (StringUtils.isBlank(localAccount)) {
                mo.setSuccess(false);
                mo.setErrorMsg(DmI18nUtils.getMessage(I18nRdpMsgKeys.USER_INFO_EMPTY_ERROR.name()));
                return mo;
            }
            if (fo.getPassword() != null) {
                if (StringUtils.isBlank(fo.getPassword())) {
                    mo.setSuccess(false);
                    mo.setErrorMsg(DmI18nUtils.getMessage(I18nRdpMsgKeys.USER_INFO_EMPTY_ERROR.name()));
                    return mo;
                }
                ValidateResultMO pwdMO = validateSubAccountPwd(puid, fo.getPassword());
                if (pwdMO != null && !pwdMO.isSuccess()) {
                    mo.setSuccess(false);
                    mo.setErrorMsg(pwdMO.getErrorMsg());
                    return mo;
                }
            }
            if (!internalAccount && !oldUser.isAllowLocal() && StringUtils.isBlank(fo.getPassword())) {
                mo.setSuccess(false);
                mo.setErrorMsg(DmI18nUtils.getMessage(I18nRdpMsgKeys.USER_INFO_EMPTY_ERROR.name()));
                return mo;
            }
        } else {
            fo.setAccount(null);
            fo.setPassword(null);
            localAccount = null;
        }
        String duplicateError = checkSubAccountUniqueOnSave(localAccount, fo.getPhone(), fo.getEmail(), primaryUser, fo.getTargetUid(), false, false);
        if (duplicateError != null) {
            mo.setSuccess(false);
            mo.setErrorMsg(duplicateError);
            return mo;
        }

        UpdateUserInfoLO lo = new UpdateUserInfoLO();
        lo.setTargetUid(fo.getTargetUid());
        lo.setOldSubAccount(oldUser.getAccount());
        lo.setOldUserName(oldUser.getUsername());
        lo.setNewSubAccount(fo.getAccount() == null ? oldUser.getAccount() : fo.getAccount());
        lo.setNewUserName(fo.getUserName() == null ? oldUser.getUsername() : fo.getUserName());

        if (fo.getAccount() != null) {
            oldUser.setAccount(fo.getAccount());
        }
        if (fo.getUserName() != null) {
            oldUser.setUsername(fo.getUserName());
        }
        if ((internalAccount || allowLocal) && fo.getPassword() != null) {
            oldUser.setPassword(CryptService.INSTANCE.encryptForOneWay(fo.getPassword()).getEncryptPassword());
        }
        oldUser.setAllowLocal(allowLocal);
        if (fo.getRoleId() != null) {
            oldUser.setRoleId(fo.getRoleId());
        }
        if (fo.getPhone() != null) {
            oldUser.setPhone(fo.getPhone());
        }
        if (fo.getEmail() != null) {
            oldUser.setEmail(fo.getEmail());
        }
        if (fo.getDisable() != null) {
            oldUser.setDisable(Boolean.TRUE.equals(fo.getDisable()));
        }
        if (fo.getLoginLocked() != null) {
            boolean loginLocked = Boolean.TRUE.equals(fo.getLoginLocked());
            oldUser.setLoginLocked(loginLocked);
            oldUser.setLoginFailCount(loginLocked ? Math.max(oldUser.getLoginFailCount(), 1) : 0);
            oldUser.setLastTryLoginTime(loginLocked ? new Date() : new Date(0));
        }

        this.authDal.userMapper().updateById(oldUser);

        mo.setConfigLO(lo);
        mo.setSuccess(true);
        mo.setErrorMsg(null);
        return mo;
    }

    @Override
    public CheckSubAccountMO checkSubAccount(String puid, CheckSubAccountFO fo) {
        String content = fo.getCheckContent();
        if (fo.getCheckType() == CheckSubAccountType.SUB_ACCOUNT) {
            DmAuthUserDO user = StringUtils.isBlank(fo.getTargetUid()) ? this.authDal.userMapper().queryBySubAccount(content) : this.authDal.userMapper()
                .queryBySubAccountExcludeUid(content, fo.getTargetUid());
            if (isDuplicateUser(user, fo.getTargetUid())) {
                return new CheckSubAccountMO(false, DmI18nUtils.getMessage(I18nRdpMsgKeys.REGISTER_ACCOUNT_EXIST_ERROR.name(), content));
            }
        } else if (fo.getCheckType() == CheckSubAccountType.PHONE) {
            DmAuthUserDO parentUser = this.authDal.userMapper().queryByUid(puid);
            if (StringUtils.equals(content, parentUser.getPhone()) && isDuplicateUser(parentUser, fo.getTargetUid())) {
                return new CheckSubAccountMO(false, DmI18nUtils.getMessage(I18nRdpMsgKeys.REGISTER_PHONE_EXIST_ERROR.name(), content));
            }
            DmAuthUserDO phoneUser = StringUtils.isBlank(fo.getTargetUid()) ? this.authDal.userMapper()
                .queryByPhoneAndParentId(content, parentUser.getId()) : this.authDal.userMapper().queryByPhoneAndParentIdExcludeUid(content, parentUser.getId(), fo.getTargetUid());
            if (isDuplicateUser(phoneUser, fo.getTargetUid())) {
                return new CheckSubAccountMO(false, DmI18nUtils.getMessage(I18nRdpMsgKeys.REGISTER_PHONE_EXIST_ERROR.name(), content));
            }
        } else if (fo.getCheckType() == CheckSubAccountType.EMAIL) {
            DmAuthUserDO parentUser = this.authDal.userMapper().queryByUid(puid);
            if (StringUtils.equals(content, parentUser.getEmail()) && isDuplicateUser(parentUser, fo.getTargetUid())) {
                return new CheckSubAccountMO(false, DmI18nUtils.getMessage(I18nRdpMsgKeys.REGISTER_EMAIL_EXIST_ERROR.name(), content));
            }
            DmAuthUserDO emailUser = StringUtils.isBlank(fo.getTargetUid()) ? this.authDal.userMapper()
                .queryByEmailAndParentId(content, parentUser.getId()) : this.authDal.userMapper().queryByEmailAndParentIdExcludeUid(content, parentUser.getId(), fo.getTargetUid());
            if (isDuplicateUser(emailUser, fo.getTargetUid())) {
                return new CheckSubAccountMO(false, DmI18nUtils.getMessage(I18nRdpMsgKeys.REGISTER_EMAIL_EXIST_ERROR.name(), content));
            }
        }

        return new CheckSubAccountMO(true, null);
    }

    private boolean isDuplicateUser(DmAuthUserDO user, String targetUid) {
        return user != null && (StringUtils.isBlank(targetUid) || !StringUtils.equals(user.getUid(), targetUid));
    }

    @Transactional(rollbackFor = Throwable.class, propagation = Propagation.REQUIRED)
    @Override
    public ResWebData<Boolean> deleteSubAccount(String puid, DeleteSubAccountFO fo) {
        DmAuthUserDO userDO = this.authDal.userMapper().queryBySubAccount(fo.getAccount());
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

}
