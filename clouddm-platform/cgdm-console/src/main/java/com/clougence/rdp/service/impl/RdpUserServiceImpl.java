package com.clougence.rdp.service.impl;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import com.clougence.clouddm.sdk.security.auth.def.SecSysRole;
import com.clougence.clouddm.sdk.security.login.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.clougence.clouddm.api.common.crypt.CryptService;
import com.clougence.clouddm.api.common.crypt.PasswordInfo;
import com.clougence.clouddm.api.common.rpc.ResWebData;
import com.clougence.clouddm.api.common.rpc.ResWebDataUtils;
import com.clougence.clouddm.base.metadata.rdp.enumeration.GlobalDeployMode;
import com.clougence.clouddm.base.metadata.rdp.enumeration.GlobalDeploySite;
import com.clougence.clouddm.platform.plugin.PluginManager;
import com.clougence.clouddm.sdk.model.feature.RdpFeatureIDs;
import com.clougence.rdp.constant.I18nRdpMsgKeys;
import com.clougence.rdp.controller.model.enumeration.CheckSubAccountType;
import com.clougence.rdp.controller.model.enumeration.VerifyCodeType;
import com.clougence.rdp.controller.model.enumeration.VerifyType;
import com.clougence.rdp.controller.model.fo.*;
import com.clougence.rdp.controller.model.lo.UpdateUserInfoLO;
import com.clougence.rdp.controller.model.lo.UpdateUserRoleLO;
import com.clougence.rdp.controller.model.vo.ListUserVO;
import com.clougence.rdp.controller.model.vo.PwdValidateExprVO;
import com.clougence.rdp.controller.model.vo.RdpUserAkSkVO;
import com.clougence.rdp.dal.enumeration.AccountBindType;
import com.clougence.rdp.dal.enumeration.AccountType;
import com.clougence.rdp.dal.enumeration.AreaCode;
import com.clougence.rdp.dal.enumeration.RdpProduct;
import com.clougence.rdp.dal.mapper.*;
import com.clougence.rdp.dal.model.RdpRoleDO;
import com.clougence.rdp.dal.model.RdpUserDO;
import com.clougence.rdp.dal.model.RdpUserKvBaseConfigDO;
import com.clougence.rdp.global.config.RdpConsoleConfig;
import com.clougence.rdp.global.config.user.UserDefinedConfig;
import com.clougence.rdp.global.exception.ErrorMessageException;
import com.clougence.clouddm.sdk.security.auth.AuthInfo;
import com.clougence.clouddm.sdk.security.auth.AuthInfoType;
import com.clougence.rdp.service.*;
import com.clougence.rdp.service.enumeration.OpVerifyErrType;
import com.clougence.rdp.service.enumeration.UserOperationType;
import com.clougence.rdp.service.model.*;
import com.clougence.rdp.util.RdpAuthUtils;
import com.clougence.rdp.util.RdpConvertUtils;
import com.clougence.rdp.util.RdpI18nUtils;
import com.clougence.utils.CollectionUtils;
import com.clougence.utils.StringUtils;
import com.clougence.utils.convert.ConverterUtils;

import lombok.extern.slf4j.Slf4j;

/**
 * @author pudding
 * @date 2020-01-17 15:29
 */
@Slf4j
@Service
public class RdpUserServiceImpl implements RdpUserService {

    @Resource
    private RdpUserMapper           rdpUserMapper;

    @Resource
    private RdpRoleMapper           rdpRoleMapper;

    @Resource
    private RdpRoleService          rdpRoleService;

    @Resource
    private RdpVerifyService        rdpVerifyService;

    @Resource
    private RdpNamingService        rdpNamingService;

    @Resource
    private RdpUserConfigService    rdpUserConfigService;

    @Resource
    private RdpAuthServiceForManage rdpDsAuthManagerService;

    @Resource
    private RdpProductClusterMapper rdpProductClusterMapper;

    @Resource
    private RdpResAuthMapper        resAuthMapper;

    @Resource
    private RdpUserMfaMapper        rdpUserMfaMapper;

    @Resource
    private RdpConsoleConfig        rdpConfig;

    @Resource
    private List<RdpNotifyService>  notifyServices;

    @Override
    public List<AuthInfo> allAuthLabelByUser(String puid, String uid) {
        if (StringUtils.equals(puid, uid)) {
            return this.rdpDsAuthManagerService.getRoleAuthLabel();
        } else {
            RdpUserDO userDO = this.rdpUserMapper.queryByUid(uid);

            Long roleId = userDO.getRoleId();
            RdpRoleDO dmRoleDO = this.rdpRoleMapper.selectById(roleId);
            Set<String> effectiveAuthLabels = new HashSet<>(this.rdpDsAuthManagerService.normalizeRoleAuthLabels(dmRoleDO.getRoleAuthLabels()));

            return this.rdpDsAuthManagerService.getRoleAuthLabel().stream().filter(authInfo -> {
                return authInfo.getAuthType() == AuthInfoType.Auth && effectiveAuthLabels.contains(authInfo.getKey());
            }).collect(Collectors.toList());
        }
    }

    @Override
    public Collection<AuthInfo> allAuthMenuCategoryByUser(String puid, String uid) {
        List<AuthInfo> tmpDef = this.rdpDsAuthManagerService.getAllCategory();
        List<String> support = new ArrayList<>();
        support.add(RdpFeatureIDs.PRODUCT_CLOUD_RDP);
        support.add(this.rdpConfig.getDefaultProduct().getFeatureID());
        for (String productType : this.rdpProductClusterMapper.supportProductType()) {
            RdpProduct rdpProduct = (RdpProduct) ConverterUtils.convert(productType, RdpProduct.class);
            if (rdpProduct != null) {
                support.add(rdpProduct.getFeatureID());
            }
        }

        List<AuthInfo> catTreeDef = tmpDef.stream().filter(a -> {
            return CollectionUtils.containsAny(a.getForProduct(), support);
        }).collect(Collectors.toList());

        // filter some category for saas
        filterSomeCategory(uid, catTreeDef);

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

    public static final String DEFAULT_CLOUD_BLACK_CATEGORY = "CAT_RDP_USER,CAT_RDP_ROLE";

    private void filterSomeCategory(String uid, List<AuthInfo> catTreeDef) {
        if (isMaintainer(uid) || GlobalDeployMode.inPrivate()) {
            return;
        }

        String cloudBlackCategory = rdpConfig.getMenuCategoryBlacklist();
        if (StringUtils.isBlank(cloudBlackCategory)) {
            cloudBlackCategory = DEFAULT_CLOUD_BLACK_CATEGORY;
        }

        Set<String> saasHiddenCategory = Arrays.stream(cloudBlackCategory.split(",")).collect(Collectors.toSet());

        catTreeDef.removeIf(info -> saasHiddenCategory.contains(info.getKey()));
    }

    @Override
    public RdpUserDO getUserByUid(String uid) {
        if (StringUtils.isBlank(uid)) {
            return null;
        } else {
            return rdpUserMapper.queryByUid(uid);
        }
    }

    @Override
    public RdpUserDO getUserById(long id) {
        if (id <= 0) {
            return null;
        } else {
            return this.rdpUserMapper.queryById(id);
        }
    }

    @Override
    public RdpUserDO getInnerUser() { return rdpUserMapper.queryPrimaryByPhone(INNER_USER_PHONE); }

    @Override
    public boolean isPrimaryUid(String uid) {
        RdpUserDO userDO = rdpUserMapper.queryByUid(uid);
        if (userDO == null) {
            return false;
        } else {
            return userDO.getParentId() == null || userDO.getParentId() <= 0;
        }
    }

    @Override
    public boolean isMaintainer(String uid) {
        RdpUserDO userDO = rdpUserMapper.queryByUid(uid);
        if (userDO == null) {
            return false;
        } else {
            return userDO.isMaintainer();
        }
    }

    @Override
    public boolean isCloudManagedUser(String puid, String uid) {
        // not cloud
        if (GlobalDeployMode.inPrivate()) {
            return false;
        }

        //illegal case
        if (puid == null || uid == null) {
            return false;
        }

        //is primary user
        if (puid.equals(uid)) {
            return false;
        }

        RdpUserDO userDO = rdpUserMapper.queryByUid(uid);
        return userDO != null && userDO.getBindType() != null && userDO.getBindType() == AccountBindType.MANAGED;
    }

    @Override
    public RdpUserDO cloudManagedBindUser(String puid, String uid) {
        // not cloud
        if (GlobalDeployMode.inPrivate()) {
            return null;
        }

        //illegal case
        if (puid == null || uid == null) {
            return null;
        }

        //is primary user
        if (puid.equals(uid)) {
            return null;
        }

        RdpUserDO userDO = rdpUserMapper.queryByUid(uid);
        if (userDO != null && userDO.getBindType() != null && userDO.getBindType() == AccountBindType.MANAGED) {
            return rdpUserMapper.queryByUid(userDO.getBindAccount());
        } else {
            return null;
        }
    }

    @Override
    public PwdValidateExprVO getPwdValidateExprWithoutEscape(String puid) {
        //unlogin state
        if (StringUtils.isBlank(puid)) {
            return getDefaultValidateExprVO();
        }

        RdpUserKvBaseConfigDO configDO = rdpUserConfigService.getSpecifiedConfig(puid, UserDefinedConfig.Fields.subAccountPwdVerifyExpr);
        if (configDO != null && StringUtils.isNotBlank(configDO.getConfigValue())) {
            RdpUserKvBaseConfigDO tipsConf = rdpUserConfigService.getSpecifiedConfig(puid, UserDefinedConfig.Fields.subAccountPwdVerifyTips);
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
        vo.setTips(RdpI18nUtils.getMessage(I18nRdpMsgKeys.SUB_ACCOUNT_PWD_VALIDATE_TIPS.name()));
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
        RdpUserDO userDO = rdpUserMapper.queryByUid(uid);

        if (userDO == null) {
            return new UpdateUserInfoMO(false, RdpI18nUtils.getMessage(I18nRdpMsgKeys.USER_NOT_EXIST_ERROR.name()));
        }

        AreaCode phoneAreaCode = userDO.getPhoneAreaCode();
        if (phoneAreaCode == null) {
            phoneAreaCode = AreaCode.CHINA;
        }

        CheckVerifyMO mo = new CheckVerifyMO();
        mo.setUid(uid);
        mo.setPhoneNumber(userDO.getPhone());
        mo.setPhoneAreaCode(phoneAreaCode);
        mo.setVerifyCode(fo.getVerifyCode());
        mo.setVerifyType(VerifyType.SMS_VERIFY_CODE);
        mo.setVerifyCodeType(VerifyCodeType.RESET_OP_PASSWORD);
        rdpVerifyService.checkVerifyCode(mo);

        String opPassword = CryptService.INSTANCE.encryptForOneWay(fo.getOpPassword()).getEncryptPassword();
        rdpUserMapper.updateOpPasswdById(userDO.getId(), opPassword);
        return new UpdateUserInfoMO(true, null);
    }

    @Override
    public OpPasswdVerifyMO opPasswdVerify(String opPassword, String uid) {
        RdpUserDO dmUserDO = rdpUserMapper.queryByUid(uid);
        if (dmUserDO.getOpPassword() == null) {
            return new OpPasswdVerifyMO(false, OpVerifyErrType.OP_PASSWD_NOT_SET, RdpI18nUtils.getMessage(I18nRdpMsgKeys.VERIFY_OP_PASSWORD_NOT_SET_ERROR.name()));
        }

        PasswordInfo passInfo = new PasswordInfo();
        passInfo.setEncryptPassword(dmUserDO.getOpPassword());
        passInfo.setPlainPassword(opPassword);
        boolean match = CryptService.INSTANCE.isMatchForOneWay(passInfo);
        if (match) {
            return new OpPasswdVerifyMO(true);
        } else {
            return new OpPasswdVerifyMO(false, OpVerifyErrType.OP_PASSWD_ERROR, RdpI18nUtils.getMessage(I18nRdpMsgKeys.VERIFY_OP_PASSWORD_ERROR.name()));
        }
    }

    @Override
    @Transactional(rollbackFor = Throwable.class, propagation = Propagation.REQUIRED)
    public UpdateUserInfoMO updateUserPhone(String uid, UpdateUserPhoneFO fo) {
        RdpUserDO consoleUserDO = rdpUserMapper.queryByUid(uid);
        String oldPhone = consoleUserDO.getPhone();
        String newPhone = fo.getPhone();
        if (!oldPhone.equals(newPhone)) {
            RdpUserDO phoneUser;
            if (consoleUserDO.getAccountType() == AccountType.PRIMARY_ACCOUNT) {
                phoneUser = rdpUserMapper.queryPrimaryByPhone(newPhone);
            } else if (consoleUserDO.getAccountType() == AccountType.SUB_ACCOUNT) {
                phoneUser = rdpUserMapper.queryByPhoneAndParentId(newPhone, consoleUserDO.getParentId());
            } else {
                throw new IllegalArgumentException("Unsupported accountType:" + consoleUserDO.getAccountType());
            }

            if (phoneUser != null) {
                return new UpdateUserInfoMO(false, RdpI18nUtils.getMessage(I18nRdpMsgKeys.USER_PHONE_EXIST_ERROR.name(), newPhone));
            }
        }

        CheckVerifyMO verifyData = new CheckVerifyMO();
        verifyData.setVerifyType(VerifyType.SMS_VERIFY_CODE);
        verifyData.setPhoneNumber(newPhone);
        verifyData.setPhoneAreaCode(fo.getPhoneAreaCode());
        verifyData.setVerifyCodeType(VerifyCodeType.UPDATE_USER_PHONE);
        verifyData.setVerifyCode(fo.getVerifyCode());
        rdpVerifyService.checkVerifyCode(verifyData);
        // phone console_user,alert_config_detail,system
        rdpUserMapper.updateUserContactInfo(uid, newPhone, null);
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
        RdpUserDO consoleUserDO = rdpUserMapper.queryByUid(uid);

        if (consoleUserDO == null) {
            throw new IllegalArgumentException("User(" + uid + ") is not exist.");
        }

        String newEmail = fo.getEmail();
        String oldEmail = consoleUserDO.getEmail();
        if (!oldEmail.equals(newEmail)) {
            RdpUserDO emailUser = rdpUserMapper.queryPrimaryByEmail(newEmail);
            if (emailUser != null) {
                return new UpdateUserInfoMO(false, RdpI18nUtils.getMessage(I18nRdpMsgKeys.USER_EMAIL_EXIST_ERROR.name(), newEmail));
            }
        }

        CheckVerifyMO verifyData = new CheckVerifyMO();
        switch (fo.getVerifyType()) {
            case SMS_VERIFY_CODE:
                AreaCode phoneAreaCode = consoleUserDO.getPhoneAreaCode();
                if (phoneAreaCode == null) {
                    phoneAreaCode = AreaCode.CHINA;
                }
                verifyData.setVerifyType(VerifyType.SMS_VERIFY_CODE);
                verifyData.setPhoneNumber(consoleUserDO.getPhone());
                verifyData.setPhoneAreaCode(phoneAreaCode);
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
                return new UpdateUserInfoMO(false, RdpI18nUtils.getMessage(I18nRdpMsgKeys.VERIFY_UNSUPPORTED_TYPE_ERROR.name()));
        }

        rdpVerifyService.checkVerifyCode(verifyData);
        // phone console_user,alert_config_detail,system
        rdpUserMapper.updateUserContactInfo(uid, null, newEmail);
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
        RdpUserDO userDO = this.rdpUserMapper.queryByUid(uid);

        boolean re = checkPassword(userDO, fo.getPassword());

        UpdateUserInfoMO mo = new UpdateUserInfoMO();
        if (re) {
            mo.setSuccess(false);
            mo.setErrorMsg(RdpI18nUtils.getMessage(I18nRdpMsgKeys.USER_ORIGIN_PASSWORD_ERROR.name()));
            return mo;
        }

        String oldPhone = userDO.getPhone();
        String newPhone = fo.getPhone();
        if (!oldPhone.equals(newPhone)) {
            RdpUserDO phoneUser;
            if (userDO.getAccountType() == AccountType.PRIMARY_ACCOUNT) {
                phoneUser = rdpUserMapper.queryPrimaryByPhone(newPhone);
            } else if (userDO.getAccountType() == AccountType.SUB_ACCOUNT) {
                phoneUser = rdpUserMapper.queryByPhoneAndParentId(newPhone, userDO.getParentId());
            } else {
                throw new IllegalArgumentException("Unsupported accountType:" + userDO.getAccountType());
            }

            if (phoneUser != null) {
                return new UpdateUserInfoMO(false, RdpI18nUtils.getMessage(I18nRdpMsgKeys.USER_PHONE_EXIST_ERROR.name(), newPhone));
            }
        }

        rdpUserMapper.updateUserContactInfo(uid, newPhone, null);
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

    private boolean checkPassword(RdpUserDO userDO, String password) {
        if (userDO.getAccountType() == AccountType.PRIMARY_ACCOUNT) {
            return RdpAuthUtils.isErrorPasswd(userDO.getPassword(), password);
        } else if (userDO.getBindType() == AccountBindType.INTERNAL) {
            return RdpAuthUtils.isErrorPasswd(userDO.getPassword(), password);
        }

        // other
        LoginProvider providerType = userDO.getBindType().getProvider();
        LoginProviderSpi loginProviderSpi = PluginManager.findSpi(LoginProviderSpi.class, providerType.name());
        if (loginProviderSpi == null) {
            throw new ErrorMessageException(RdpI18nUtils.getMessage(I18nRdpMsgKeys.LOGIN_FAIL_UNSUPPORTED_SUBACCOUNT_LOGIN_TYPE.name()));
        }

        RdpUserDO primaryUser = this.rdpUserMapper.queryPrimaryByDomain(userDO.getUserDomain());
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
        RdpUserDO userDO = this.rdpUserMapper.queryByUid(uid);
        boolean re = checkPassword(userDO, fo.getPassword());

        UpdateUserInfoMO mo = new UpdateUserInfoMO();
        if (re) {
            mo.setSuccess(false);
            mo.setErrorMsg(RdpI18nUtils.getMessage(I18nRdpMsgKeys.USER_ORIGIN_PASSWORD_ERROR.name()));
            return mo;
        }

        String newEmail = fo.getEmail();
        String oldEmail = userDO.getEmail();
        if (!oldEmail.equals(newEmail)) {
            RdpUserDO emailUser = rdpUserMapper.queryPrimaryByEmail(newEmail);
            if (emailUser != null) {
                return new UpdateUserInfoMO(false, RdpI18nUtils.getMessage(I18nRdpMsgKeys.USER_EMAIL_EXIST_ERROR.name(), newEmail));
            }
        }

        rdpUserMapper.updateUserContactInfo(uid, null, newEmail);
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
        this.rdpUserMapper.updateUserAliyunAkSk(puid, ak, encryptAliyunSk);
    }

    @Override
    public void cleanAliyunAkSk(String puid) {
        this.rdpUserMapper.updateUserAliyunAkSk(puid, null, null);
    }

    @Override
    public ResWebData<RdpUserAkSkVO> queryAkSk(String puid, QueryUserAkSkFO fo) {
        RdpUserDO userDO = this.rdpUserMapper.queryByUid(puid);
        AreaCode phoneAreaCode = userDO.getPhoneAreaCode();
        if (phoneAreaCode == null) {
            phoneAreaCode = AreaCode.CHINA;
        }

        CheckVerifyMO verifyData = new CheckVerifyMO();
        switch (fo.getVerifyType()) {
            case SMS_VERIFY_CODE:
                verifyData.setVerifyType(VerifyType.SMS_VERIFY_CODE);
                verifyData.setPhoneAreaCode(phoneAreaCode);
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
                return ResWebDataUtils.buildError(RdpI18nUtils.getMessage(I18nRdpMsgKeys.VERIFY_UNSUPPORTED_TYPE_ERROR.name()));
        }

        this.rdpVerifyService.checkVerifyCode(verifyData);

        //use parent user
        RdpUserDO parentUserDO = this.rdpUserMapper.queryByUid(puid);
        RdpUserAkSkVO akSkVO = new RdpUserAkSkVO();
        akSkVO.setAccessKey(parentUserDO.getAccessKey());
        akSkVO.setSecretKey(CryptService.INSTANCE.decryptUseDefaultKeyAndSalt(parentUserDO.getSecretKey()));
        return ResWebDataUtils.buildSuccess(akSkVO);
    }

    @Override
    public ResWebData<String> resetAkSk(String puid, ResetUserAkSkFO fo) {
        RdpUserDO userDO = this.rdpUserMapper.queryByUid(puid);
        AreaCode phoneAreaCode = userDO.getPhoneAreaCode();
        if (phoneAreaCode == null) {
            phoneAreaCode = AreaCode.CHINA;
        }

        CheckVerifyMO verifyData = new CheckVerifyMO();
        switch (fo.getVerifyType()) {
            case SMS_VERIFY_CODE:
                verifyData.setVerifyType(VerifyType.SMS_VERIFY_CODE);
                verifyData.setPhoneAreaCode(phoneAreaCode);
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
                return ResWebDataUtils.buildError(RdpI18nUtils.getMessage(I18nRdpMsgKeys.VERIFY_UNSUPPORTED_TYPE_ERROR.name()));
        }

        this.rdpVerifyService.checkVerifyCode(verifyData);

        //use parent user
        String newAccessKey = this.rdpNamingService.genAccessKey();
        String newSecretKey = CryptService.INSTANCE.encryptUseDefaultKeyAndSalt(this.rdpNamingService.genSecretKey());
        this.rdpUserMapper.updateUserAkSk(puid, newAccessKey, newSecretKey);
        return ResWebDataUtils.buildSuccess("OK");
    }

    //    @Override
    //    public LoginMO switchSaasResMode(String puid, String uid, SwitchSaasModeFO fo) {
    //        if (GlobalDeployMode.inPrivate()) {
    //            throw new RuntimeException("On-premise mode deployment not support saas mode switch.");
    //        }
    //
    //        String managedUid = rdpConfig.getSaasManagedPrimaryUid();
    //        if (StringUtils.isBlank(managedUid)) {
    //            throw new IllegalArgumentException("Have no saas managed uid config,can not switch saas resource mode.");
    //        }
    //
    //        RdpUserDO managedUser = rdpUserMapper.queryByUid(managedUid);
    //        if (managedUser == null) {
    //            throw new IllegalArgumentException("Saas managed user (" + managedUser + ") not exist.");
    //        }
    //
    //        RdpUserDO sUser;
    //        if (fo.getSaasResMode() == SaasResMode.MANAGED && puid.equals(uid)) {
    //            // Primary account change to MANAGED
    //            RdpUserDO bindSubAccount = rdpUserMapper.queryBySubAccountByBindInfo(managedUser.getId(), puid, AccountBindType.MANAGED);
    //            if (bindSubAccount == null) {
    //                RdpUserDO pUser = rdpUserMapper.queryByUid(puid);
    //
    //                //init one
    //                Long id = addSubAccountForSaasManagedBind(managedUser.getUid(), AccountBindType.MANAGED, pUser);
    //                bindSubAccount = rdpUserMapper.selectById(id);
    //
    //                if (bindSubAccount == null) {
    //                    throw new RuntimeException("Can not switch to MANAGED saas cause bind user failed.");
    //                }
    //            }
    //
    //            sUser = bindSubAccount;
    //        } else if (fo.getSaasResMode() == SaasResMode.BYOC && !puid.equals(uid)) {
    //            // Sub account change to BYOC
    //            RdpUserDO bindSubAccount = rdpUserMapper.queryByUid(uid);
    //            if (bindSubAccount == null || bindSubAccount.getParentId() == null || bindSubAccount.getParentId() <= 0) {
    //                throw new IllegalArgumentException("Managed sub uid is not exist or user in managed is an primary user.uid:" + uid);
    //            }
    //
    //            if (bindSubAccount.getBindType() != AccountBindType.MANAGED || StringUtils.isBlank(bindSubAccount.getBindAccount())) {
    //                throw new IllegalArgumentException("Managed sub user account bind type is not MANAGED or bind account is blank.uid:" + uid);
    //            }
    //
    //            RdpUserDO userDO = rdpUserMapper.queryByUid(bindSubAccount.getBindAccount());
    //            if (userDO == null) {
    //                throw new IllegalArgumentException("Managed sub user bind account user (" + bindSubAccount.getBindAccount() + ") is not exist.");
    //            }
    //
    //            sUser = userDO;
    //        } else {
    //            throw new IllegalArgumentException("Current user " + uid + "  can not switch to " + fo.getSaasResMode());
    //        }
    //
    //        String token = rdpJwtService.genJwtToken(sUser);
    //
    //        LoginMO l = new LoginMO();
    //        l.setSuccess(true);
    //        l.setToken(token);
    //        return l;
    //    }

    public Long addSubAccountForSaasManagedBind(String managedUid, AccountBindType bindType, RdpUserDO primaryUser) {
        String generatePwd = Long.toHexString(System.currentTimeMillis()) + "!@#";
        RdpUserDO managedUser = this.rdpUserMapper.queryByUid(managedUid);
        RdpRoleDO devRoleOfManager = findDevRoleForSaasManagedUser(managedUid);

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

        RdpUserDO userDO = new RdpUserDO();
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
        this.rdpUserMapper.insert(userDO);
        this.rdpUserConfigService.initSubAccountConfigs(userDO.getUid());
        this.notifyServices.forEach(s -> s.notifyUser(managedUid, userDO.getUid(), UserOperationType.ADD));

        return userDO.getId();
    }

    public RdpRoleDO findDevRoleForSaasManagedUser(String managedUid) {
        String roleName = SecSysRole.CC_SAAS_DEV_NAME;
        List<RdpRoleDO> roles = this.rdpRoleMapper.queryByRoleName(managedUid, roleName);
        RdpRoleDO role = CollectionUtils.isEmpty(roles) ? null : roles.get(0);
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
        RdpUserDO userDO = null;
        if (fo.getAccountType() == AccountType.PRIMARY_ACCOUNT) {
            if (fo.getVerifyType() == VerifyType.SMS_VERIFY_CODE) {
                userDO = this.rdpUserMapper.queryPrimaryByPhoneAndAreaCode(fo.getPhone(), fo.getPhoneAreaCode());
                if (userDO == null) {
                    //old user have no area code
                    userDO = this.rdpUserMapper.queryPrimaryByPhone(fo.getPhone());
                }
            } else if (fo.getVerifyType() == VerifyType.EMAIL_VERIFY_CODE) {
                userDO = this.rdpUserMapper.queryPrimaryByEmail(fo.getEmail());
            } else {
                throw new IllegalArgumentException("Unsupported verify type:" + fo.getVerifyType());
            }
        } else if (fo.getAccountType() == AccountType.SUB_ACCOUNT) {
            if (StringUtils.isBlank(fo.getSubAccount())) {
                return new UpdateUserInfoMO(false, RdpI18nUtils.getMessage(I18nRdpMsgKeys.USER_ACCOUNT_EMPTY_ERROR.name()));
            }

            userDO = this.rdpUserMapper.queryBySubAccount(fo.getSubAccount());
        }

        if (userDO == null) {
            return new UpdateUserInfoMO(false, RdpI18nUtils.getMessage(I18nRdpMsgKeys.USER_NOT_EXIST_ERROR.name()));
        }

        if (userDO.isDisable()) {
            return new UpdateUserInfoMO(false, RdpI18nUtils.getMessage(I18nRdpMsgKeys.USER_IS_DISABLED_ERROR.name()));
        }

        AreaCode phoneAreaCode = userDO.getPhoneAreaCode();
        if (phoneAreaCode == null) {
            phoneAreaCode = AreaCode.CHINA;
        }

        CheckVerifyMO verifyMO = new CheckVerifyMO();
        verifyMO.setSubAccount(fo.getAccountType() == AccountType.SUB_ACCOUNT);
        verifyMO.setSubAccountName(fo.getSubAccount());
        verifyMO.setPhoneNumber(fo.getPhone());
        verifyMO.setPhoneAreaCode(phoneAreaCode);
        verifyMO.setVerifyCode(fo.getVerifyCode());
        verifyMO.setVerifyType(fo.getVerifyType());
        verifyMO.setEmail(fo.getEmail());
        verifyMO.setVerifyCodeType(VerifyCodeType.RESET_PASSWORD);

        rdpVerifyService.checkVerifyCode(verifyMO);

        String password = CryptService.INSTANCE.encryptForOneWay(fo.getPassword()).getEncryptPassword();
        rdpUserMapper.updatePasswdById(userDO.getId(), password);

        return new UpdateUserInfoMO(true, null);
    }

    @Override
    public UpdateUserInfoMO resetPwdWithOriginPwd(ResetPwdWithOriginPwdFO fo, String targetUid, String puid) {
        RdpUserDO userDO = this.rdpUserMapper.queryByUid(targetUid);

        UpdateUserInfoMO mo = new UpdateUserInfoMO();
        boolean notSame = RdpAuthUtils.isErrorPasswd(userDO.getPassword(), fo.getNewPassword());
        if (!notSame) {
            //can not reset with same password
            mo.setSuccess(false);
            mo.setErrorMsg(RdpI18nUtils.getMessage(I18nRdpMsgKeys.RESET_WITH_SAME_PASSWORD_ERROR.name()));
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
            mo.setErrorMsg(RdpI18nUtils.getMessage(I18nRdpMsgKeys.USER_ORIGIN_PASSWORD_ERROR.name()));
            return mo;
        }

        String encryptPwd = CryptService.INSTANCE.encryptForOneWay(fo.getNewPassword()).getEncryptPassword();
        rdpUserMapper.updatePasswdById(userDO.getId(), encryptPwd);

        mo.setSuccess(true);
        return mo;
    }

    @Override
    public UpdateUserInfoMO resetSubAccountPwd(ResetSubAccountPwdFO fo, String operatorUid) {
        RdpUserDO opUserDO = this.rdpUserMapper.queryByUid(operatorUid);

        boolean re = RdpAuthUtils.isErrorPasswd(opUserDO.getPassword(), fo.getOperatorPwd());
        UpdateUserInfoMO mo = new UpdateUserInfoMO();
        if (re) {
            mo.setSuccess(false);
            mo.setErrorMsg(RdpI18nUtils.getMessage(I18nRdpMsgKeys.OPERATOR_PASSWORD_ERROR.name()));
            return mo;
        }

        RdpUserDO userDO = this.rdpUserMapper.queryByUid(fo.getSubAccountUid());

        String encryptPwd = CryptService.INSTANCE.encryptForOneWay(fo.getNewPassword()).getEncryptPassword();
        rdpUserMapper.updatePasswdById(userDO.getId(), encryptPwd);

        mo.setSuccess(true);
        return mo;
    }

    @Override
    public List<RdpUserDO> listSubAccounts(String puid) {
        RdpUserDO parentUser = this.rdpUserMapper.queryByUid(puid);
        return this.rdpUserMapper.listByParentId(parentUser.getId());
    }

    @Override
    public List<ListUserVO> listSubAccounts(String puid, ListSubAccountsFO fo) {
        RdpUserDO parentUser = this.rdpUserMapper.queryByUid(puid);

        String prefix = StringUtils.isBlank(fo.getUserNameOrSubAccountPrefix()) ? null : fo.getUserNameOrSubAccountPrefix();
        List<RdpUserDO> subAccounts = this.rdpUserMapper.listByCondition(parentUser.getId(), fo.getRoleId(), prefix);
        List<RdpRoleDO> roles = this.rdpRoleService.listRoleByUID(puid);
        Map<Long, RdpRoleDO> roleMap = new HashMap<>();
        for (RdpRoleDO role : roles) {
            roleMap.put(role.getId(), role);
        }

        return subAccounts.stream().map(u -> RdpConvertUtils.convertToListUserVO(u, roleMap)).collect(Collectors.toList());
    }

    private void addSubAccountCheck(AddSubAccountFO accountFO, RdpUserDO primaryUser, boolean skipMailCheck, boolean skipPhoneCheck) {
        //this.rdpLicenseCheckService.checkSubAccountCount();

        RdpUserDO user = this.rdpUserMapper.queryBySubAccount(accountFO.getSubAccount());
        if (user != null) {
            throw new ErrorMessageException(RdpI18nUtils.getMessage(I18nRdpMsgKeys.USER_ADD_EXIST_ERROR.name(), accountFO.getSubAccount()));
        }

        if (!skipMailCheck) {
            RdpUserDO emailUser = this.rdpUserMapper.queryByEmailAndParentId(accountFO.getEmail(), primaryUser.getId());
            if (emailUser != null) {
                throw new ErrorMessageException(RdpI18nUtils.getMessage(I18nRdpMsgKeys.USER_ADD_EXIST_ERROR.name(), accountFO.getEmail()));
            }
        }

        if (!skipPhoneCheck) {
            RdpUserDO phoneUser = this.rdpUserMapper.queryByPhoneAndParentId(accountFO.getPhone(), primaryUser.getId());
            if (phoneUser != null) {
                throw new ErrorMessageException(RdpI18nUtils.getMessage(I18nRdpMsgKeys.USER_ADD_EXIST_ERROR.name(), accountFO.getPhone()));
            }
        }

        RdpRoleDO roleDO = this.rdpRoleService.fetchRoleById(accountFO.getRoleId());
        if (roleDO == null) {
            throw new ErrorMessageException(RdpI18nUtils.getMessage(I18nRdpMsgKeys.USER_ROLE_NOT_EXIST_ERROR.name()));
        }

        if (!roleDO.getOwnerUid().equals(primaryUser.getUid())) {
            throw new ErrorMessageException(RdpI18nUtils.getMessage(I18nRdpMsgKeys.USER_ROLE_IS_NOT_BELONG_YOU_ERROR.name()));
        }

        ValidateResultMO pwdMO = validateSubAccountPwd(primaryUser.getUid(), accountFO.getPassword());
        if (pwdMO != null && !pwdMO.isSuccess()) {
            throw new ErrorMessageException(pwdMO.getErrorMsg());
        }
    }

    @Transactional(rollbackFor = Throwable.class, propagation = Propagation.REQUIRED)
    @Override
    public AddSubAccountMO addSubAccountForBind(String puid, AccountBindType bindType, RdpUserDO bindUser) {
        String generatePwd = Long.toHexString(System.currentTimeMillis()) + "!@#";
        RdpUserDO primaryUser = this.rdpUserMapper.queryByUid(puid);

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

        RdpUserDO userDO = new RdpUserDO();
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
        this.rdpUserMapper.insert(userDO);
        //        verifyService.initUserVerify(userDO);

        this.rdpUserConfigService.initSubAccountConfigs(userDO.getUid());

        this.notifyServices.forEach(s -> s.notifyUser(puid, userDO.getUid(), UserOperationType.ADD));
        return new AddSubAccountMO(true, null, userDO.getUid());
    }

    @Transactional(rollbackFor = Throwable.class, propagation = Propagation.REQUIRED)
    @Override
    public AddSubAccountMO addSubAccountForInternal(String puid, AddSubAccountFO fo) {
        RdpUserDO primaryUser = this.rdpUserMapper.queryByUid(puid);

        try {
            this.addSubAccountCheck(fo, primaryUser, false, false);
        } catch (ErrorMessageException e) {
            return new AddSubAccountMO(false, e.getErrorMessage());
        }

        RdpUserDO userDO = new RdpUserDO();
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
        this.rdpUserMapper.insert(userDO);
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
            mo.setErrorMsg(RdpI18nUtils.getMessage(I18nRdpMsgKeys.USER_INFO_EMPTY_ERROR.name()));
            return mo;
        }

        if (StringUtils.isNotBlank(fo.getSubAccount())) {
            RdpUserDO userWithNewSubAccount = this.rdpUserMapper.queryBySubAccount(fo.getSubAccount());
            if (userWithNewSubAccount != null && !userWithNewSubAccount.getUid().equals(fo.getTargetUid())) {
                mo.setSuccess(false);
                mo.setErrorMsg(RdpI18nUtils.getMessage(I18nRdpMsgKeys.REGISTER_ACCOUNT_EXIST_ERROR.name(), fo.getSubAccount()));
                return mo;
            }
        }

        RdpUserDO oldUser = this.rdpUserMapper.queryByUid(fo.getTargetUid());
        if (oldUser == null) {
            mo.setSuccess(false);
            mo.setErrorMsg(RdpI18nUtils.getMessage(I18nRdpMsgKeys.USER_NOT_EXIST_ERROR.name()));
            return mo;
        }

        UpdateUserInfoLO lo = new UpdateUserInfoLO();
        lo.setTargetUid(fo.getTargetUid());
        lo.setOldSubAccount(oldUser.getSubAccount());
        lo.setOldUserName(oldUser.getUsername());
        lo.setNewSubAccount(fo.getSubAccount());
        lo.setNewUserName(fo.getUserName());

        this.rdpUserMapper.updateSubAccountAndName(fo.getTargetUid(), fo.getSubAccount(), fo.getUserName());

        mo.setConfigLO(lo);
        mo.setSuccess(true);
        mo.setErrorMsg(null);
        return mo;
    }

    @Override
    public CheckSubAccountMO checkSubAccount(String puid, CheckSubAccountFO fo) {
        String content = fo.getCheckContent();
        if (fo.getCheckType() == CheckSubAccountType.SUB_ACCOUNT) {
            RdpUserDO user = this.rdpUserMapper.queryBySubAccount(content);
            if (user != null) {
                return new CheckSubAccountMO(false, RdpI18nUtils.getMessage(I18nRdpMsgKeys.REGISTER_ACCOUNT_EXIST_ERROR.name(), content));
            }
        } else if (fo.getCheckType() == CheckSubAccountType.PHONE) {
            RdpUserDO parentUser = this.rdpUserMapper.queryByUid(puid);
            RdpUserDO phoneUser = this.rdpUserMapper.queryByPhoneAndParentId(content, parentUser.getId());
            if (phoneUser != null) {
                return new CheckSubAccountMO(false, RdpI18nUtils.getMessage(I18nRdpMsgKeys.REGISTER_PHONE_EXIST_ERROR.name(), content));
            }
        } else if (fo.getCheckType() == CheckSubAccountType.EMAIL) {
            RdpUserDO parentUser = this.rdpUserMapper.queryByUid(puid);
            RdpUserDO emailUser = this.rdpUserMapper.queryByEmailAndParentId(content, parentUser.getId());
            if (emailUser != null) {
                return new CheckSubAccountMO(false, RdpI18nUtils.getMessage(I18nRdpMsgKeys.REGISTER_EMAIL_EXIST_ERROR.name(), content));
            }
        }

        return new CheckSubAccountMO(true, null);
    }

    @Transactional(rollbackFor = Throwable.class, propagation = Propagation.REQUIRED)
    @Override
    public ResWebData<Boolean> deleteSubAccount(String puid, DeleteSubAccountFO fo) {
        RdpUserDO userDO = this.rdpUserMapper.queryBySubAccount(fo.getSubAccount());
        rdpVerifyService.dropUserVerify(userDO.getUid());
        rdpUserMapper.deleteById(userDO.getId());
        resAuthMapper.deleteByUser(userDO.getUid());
        rdpUserMfaMapper.deleteByUid(userDO.getUid());

        this.notifyServices.forEach(s -> s.notifyUser(puid, userDO.getUid(), UserOperationType.DELETE));
        return ResWebDataUtils.buildSuccess();
    }

    @Override
    public UpdateUserRoleLO updateUserRole(UpdateUserRoleFO fo) {
        RdpUserDO theUser = this.rdpUserMapper.queryByUid(fo.getSubAccountUid());
        if (theUser == null) {
            throw new ErrorMessageException(RdpI18nUtils.getMessage(I18nRdpMsgKeys.USER_NOT_EXIST_ERROR.name()));
        }

        if (theUser.getRoleId() == fo.getRoleId()) {
            return convertToUpdateUserRoleLO(theUser, fo.getRoleId());
        }

        RdpRoleDO theRole = this.rdpRoleService.fetchRoleById(fo.getRoleId());
        if (theRole == null) {
            throw new ErrorMessageException(RdpI18nUtils.getMessage(I18nRdpMsgKeys.USER_ROLE_NOT_EXIST_ERROR.name()));
        }

        this.rdpUserMapper.updateRoleById(theUser.getId(), fo.getRoleId());
        return convertToUpdateUserRoleLO(theUser, fo.getRoleId());
    }

    private UpdateUserRoleLO convertToUpdateUserRoleLO(RdpUserDO rdpUserDO, long newRoleId) {
        RdpRoleDO oldRole = this.rdpRoleMapper.selectById(rdpUserDO.getRoleId());
        RdpRoleDO newRole = this.rdpRoleMapper.selectById(newRoleId);
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
        this.rdpUserMapper.updateAbilityByUid(fo.getUid(), fo.getDisable());
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
    public RdpUserDO getUserByAk(String ak) {
        if (StringUtils.isBlank(ak)) {
            return null;
        } else {
            return rdpUserMapper.queryByAccessKey(ak);
        }
    }

    @Override
    public String getPrimaryUid(String uid) {
        RdpUserDO userDO = rdpUserMapper.queryByUid(uid);
        if (userDO == null) {
            throw new IllegalArgumentException("uid not exist.");
        }

        if (userDO.getParentId() == null || userDO.getParentId() <= 0) {
            return uid;
        } else {
            RdpUserDO parentUserDO = rdpUserMapper.queryById(userDO.getParentId());
            return parentUserDO.getUid();
        }
    }

    @Override
    public RdpUserDO getPrimaryUser(String uid) {
        RdpUserDO userDO = rdpUserMapper.queryByUid(uid);
        if (userDO == null) {
            throw new IllegalArgumentException("uid not exist.");
        }

        if (userDO.getParentId() == null || userDO.getParentId() <= 0) {
            return userDO;
        } else {
            return rdpUserMapper.queryById(userDO.getParentId());
        }
    }

    @Override
    public List<RdpUserDO> listPrimaryUser() {
        return this.rdpUserMapper.listPrimaryAccount();
    }

    @Override
    public UpdateUserInfoMO updateResourceManage(UpdateResourceManageFO fo, String puid) {
        RdpUserDO user = this.rdpUserMapper.queryByUid(fo.getTargetUid());
        if (user == null) {
            return new UpdateUserInfoMO(false, RdpI18nUtils.getMessage(I18nRdpMsgKeys.USER_NOT_EXIST_ERROR.name(), fo.getTargetUid()));
        }

        this.rdpUserMapper.updateResourceMangeEnable(fo.getTargetUid(), fo.isResourceManage());

        return new UpdateUserInfoMO(true, null);
    }
}
