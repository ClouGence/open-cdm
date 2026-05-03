//package com.clougence.rdp.component.sso.Impl;
//
//import java.util.*;
//
//import javax.annotation.Resource;
//
//import org.springframework.transaction.annotation.Propagation;
//import org.springframework.transaction.annotation.Transactional;
//
//import com.clougence.clouddm.api.common.crypt.AesSymmetricCryptService;
//import com.clougence.clouddm.api.common.crypt.provider.BCryptOneWayCryptService;
//import com.clougence.rdp.component.jwtsession.RdpJwtService;
//import com.clougence.rdp.component.sso.RdpSsoLoginRegService;
//import com.clougence.rdp.component.sso.model.SsoUserInfo;
//import com.clougence.rdp.component.sso.model.fo.SsoRegisterAndLoginFO;
//import com.clougence.rdp.constant.I18nRdpMsgKeys;
//import com.clougence.rdp.dal.enumeration.AccountType;
//import com.clougence.rdp.dal.enumeration.AreaCode;
//import com.clougence.rdp.dal.enumeration.RegisterStatus;
//import com.clougence.rdp.dal.mapper.RdpSsoRegisterMapper;
//import com.clougence.rdp.dal.mapper.RdpUserMapper;
//import com.clougence.rdp.dal.model.RdpRoleDO;
//import com.clougence.rdp.dal.model.RdpSsoRegisterDO;
//import com.clougence.rdp.dal.model.RdpUserDO;
//import com.clougence.rdp.global.config.RdpConsoleConfig;
//import com.clougence.clouddm.base.metadata.rdp.enumeration.GlobalDeployMode;
//import com.clougence.clouddm.base.metadata.rdp.enumeration.GlobalDeploySite;
//import com.clougence.clouddm.sdk.security.auth.def.SecSysRole;
//import com.clougence.rdp.service.*;
//import com.clougence.rdp.service.model.LoginMO;
//import com.clougence.rdp.util.RandomStrUtils;
//import com.clougence.rdp.util.RdpI18nUtils;
//import com.clougence.utils.JsonUtils;
//import com.clougence.utils.StringUtils;
//
//import lombok.extern.slf4j.Slf4j;
//
//@Slf4j
//public abstract class AbstractSsoLoginRegService implements RdpSsoLoginRegService {
//
//    @Resource
//    private RdpUserMapper        rdpUserMapper;
//
//    @Resource
//    private RdpSsoRegisterMapper rdpSsoRegisterMapper;
//
//    @Resource
//    private RdpNamingService     rdpNamingService;
//
//    @Resource
//    private RdpRoleService       rdpRoleService;
//
//    @Resource
//    private RdpUserConfigService rdpUserConfigService;
//
//    @Resource
//    private RdpSysConfigService  rdpSysConfigService;
//
//    @Resource
//    private RdpDsEnvService      rdpDsEnvService;
//
//    @Resource
//    private RdpJwtService        rdpJwtService;
//
//    @Resource
//    private RdpConsoleConfig     rdpConfig;
//
//    @Resource
//    private SaasService          saasService;
//
//    public abstract SsoUserInfo fetchUserInfo(SsoRegisterAndLoginFO fo);
//
//    public abstract String fetchCallback(String src, String target);
//
//    public abstract String fetchSsoType();
//
//    protected String encodeState(String src, String target) {
//        Map<String, String> stateMap = new HashMap<>();
//
//        stateMap.put(SSO_TYPE_KEY, this.fetchSsoType());
//
//        if (StringUtils.isNotBlank(src)) {
//            stateMap.put(SSO_SRC_KEY, dataTruncate(src));
//        }
//
//        if (StringUtils.isNotBlank(target)) {
//            stateMap.put(SSO_TARGET_KEY, dataTruncate(target));
//        }
//
//        return Base64.getUrlEncoder().withoutPadding().encodeToString(JsonUtils.toJson(stateMap).getBytes());
//    }
//
//    private String dataTruncate(String src) {
//        if (src != null) {
//            if (src.length() >= RdpWebViewLogService.SRC_CONTENT_LENGTH) {
//                return src.substring(0, RdpWebViewLogService.SRC_CONTENT_LENGTH);
//            }
//        }
//        return src;
//    }
//
//    protected RdpUserDO checkDbUser(SsoUserInfo userInfo) {
//        RdpUserDO user = null;
//
//        // (ALL) check the unionId first
//        if (StringUtils.isNotBlank(userInfo.getUnionId())) {
//            user = rdpUserMapper.queryPrimaryByUnionIdAndSsoType(userInfo.getUnionId(), userInfo.getSsoType());
//        }
//
//        // (OVERSEA) if unionId is null, use email
//        if (user == null && GlobalDeploySite.currDeploySite != GlobalDeploySite.china && StringUtils.isNotBlank(userInfo.getEmail())) {
//            user = rdpUserMapper.queryPrimaryByEmail(userInfo.getEmail());
//        }
//
//        // (CHINA) if unionId is null, use phone
//        if (user == null && GlobalDeploySite.currDeploySite == GlobalDeploySite.china && StringUtils.isNotBlank(userInfo.getMobile())) {
//            user = rdpUserMapper.queryPrimaryByPhone(userInfo.getMobile());
//        }
//
//        return user;
//    }
//
//    @Transactional(rollbackFor = Throwable.class, propagation = Propagation.REQUIRED)
//    public LoginMO doRegister(SsoUserInfo userInfo) {
//        if (!validRegisterInfo(userInfo)) {
//            return new LoginMO(false, RdpI18nUtils.getMessage(I18nRdpMsgKeys.REGISTER_SSO_NEED_ERROR.name()));
//        }
//        RdpUserDO newUser = registerInner(userInfo);
//
//        this.rdpRoleService.repairRoleForUser(newUser.getUid());
//
//        List<RdpRoleDO> roles = this.rdpRoleService.listRoleByUID(newUser.getUid());
//        RdpRoleDO adminRole = roles.stream().filter(roleDO -> StringUtils.equals(roleDO.getRoleName(), SecSysRole.ADMIN_ROLE_NAME)).findFirst().orElse(null);
//        if (adminRole == null) {
//            throw new IllegalStateException(I18nRdpMsgKeys.REGISTER_INNER_INIT_ROLE_TYPE_ERROR.name());
//        }
//
//        this.rdpUserMapper.updateRoleById(newUser.getId(), adminRole.getId());
//        this.rdpUserConfigService.initUserConfigs(newUser.getUid());
//        this.rdpSysConfigService.initUserSystemEnv(newUser.getUid());
//        this.rdpDsEnvService.initPrimaryUserDefaultEnv(newUser.getUid(), newUser.getUid());
//
//        if (GlobalDeployMode.inCloud() && GlobalDeploySite.initVoucherEnable()) {
//            saasService.newRegisterInitVoucher(newUser.getUid());
//        }
//
//        return doLogin(newUser, userInfo);
//    }
//
//    private RdpUserDO registerInner(SsoUserInfo userInfo) {
//        String uid = this.rdpNamingService.genUid();
//
//        String domain;
//        if (StringUtils.isNotBlank(rdpConfig.getUserDomainSuffix())) {
//            domain = uid + "." + rdpConfig.getUserDomainSuffix();
//        } else {
//            domain = uid + "." + RdpUserService.DEFAULT_USER_DOMAIN_SUFFIX;
//        }
//
//        AreaCode code = null;
//        if (StringUtils.isNotBlank(userInfo.getStateCode())) {
//            code = AreaCode.valueOfCode(Integer.valueOf(userInfo.getStateCode()));
//        }
//
//        String pwd = RandomStrUtils.fixedLenRandomStrWithSpecialChars(16);
//
//        RdpUserDO userDO = new RdpUserDO();
//        userDO.setUid(uid);
//        userDO.setPassword(BCryptOneWayCryptService.getInstance().encrypt(pwd).getEncryptPassword());
//        userDO.setEmail(StringUtils.isBlank(userInfo.getEmail()) ? RdpSsoLoginRegService.SSO_DEFAULT_EMAIL : userInfo.getEmail());
//        userDO.setPhoneAreaCode(code);
//        userDO.setPhone(StringUtils.isBlank(userInfo.getMobile()) ? RdpSsoLoginRegService.SSO_DEFAULT_PHONE : userInfo.getMobile());
//        userDO.setCompany(userInfo.getCompany());
//        userDO.setUsername(userInfo.getNick());
//        userDO.setContactMe(true);
//        userDO.setAccessKey(this.rdpNamingService.genAccessKey());
//        userDO.setAccountType(AccountType.PRIMARY_ACCOUNT);
//        userDO.setUserDomain(domain);
//        userDO.setSecretKey(AesSymmetricCryptService.getInstance().encryptUseDefaultKeyAndSalt(this.rdpNamingService.genSecretKey()));
//        userDO.setUnionId(userInfo.getUnionId());
//        userDO.setSsoType(userInfo.getSsoType());
//        userDO.setSrc(userInfo.getSrc());
//
//        this.rdpUserMapper.insert(userDO);
//
//        return userDO;
//    }
//
//    protected LoginMO doLogin(RdpUserDO user, SsoUserInfo ssoUserInfo) {
//        if (user.isDisable()) {
//            return new LoginMO(false, RdpI18nUtils.getMessage(I18nRdpMsgKeys.USER_IS_DISABLED_ERROR.name()));
//        }
//        long nowMs = System.currentTimeMillis();
//
//        this.rdpUserMapper.updateLoginLimitInfo(new Date(nowMs), 0, false, user.getId());
//
//        // if user don't have the unionId need to fill by the ssoUserInfo
//        if (StringUtils.isEmpty(user.getUnionId()) || user.getSsoType() == null) {
//            this.rdpUserMapper.updateUnionId(user.getId(), ssoUserInfo.getUnionId(), ssoUserInfo.getSsoType());
//        }
//
//        LoginMO re = new LoginMO();
//        re.setSuccess(true);
//        re.setUid(user.getUid());
//        re.setUsername(user.getUsername());
//        re.setToken(this.rdpJwtService.genJwtToken(user));
//        return re;
//    }
//
//    private boolean validRegisterInfo(SsoUserInfo userInfo) {
//        if (GlobalDeploySite.currDeploySite == GlobalDeploySite.china) {
//            return StringUtils.isNotBlank(userInfo.getMobile());
//        } else {
//            return StringUtils.isNotBlank(userInfo.getEmail());
//        }
//    }
//
//    @Override
//    public LoginMO registerAndLogin(SsoRegisterAndLoginFO fo, String src) {
//        SsoUserInfo userInfo = fetchUserInfo(fo);
//        RdpUserDO rdpUser = checkDbUser(userInfo);
//
//        if (StringUtils.isNotBlank(src)) {
//            userInfo.setSrc(src);
//        }
//
//        if (StringUtils.isNotBlank(userInfo.getUnionId()) && rdpUser == null && !validRegisterInfo(userInfo)) {
//            String requestId = RandomStrUtils.fixedLenRandomStr(32);
//            RdpSsoRegisterDO registerDo = new RdpSsoRegisterDO();
//            registerDo.setRequestId(requestId);
//            registerDo.setUnionId(userInfo.getUnionId());
//            registerDo.setNickname(userInfo.getNick());
//            registerDo.setRegisterStatus(RegisterStatus.WAIT_REGISTER);
//            rdpSsoRegisterMapper.insert(registerDo);
//            return new LoginMO(false, RdpI18nUtils.getMessage(I18nRdpMsgKeys.REGISTER_SSO_NEED_DOUBLE.name()), requestId);
//        }
//
//        if (rdpUser != null) {
//            return doLogin(rdpUser, userInfo);
//        } else {
//            return doRegister(userInfo);
//        }
//    }
//}
