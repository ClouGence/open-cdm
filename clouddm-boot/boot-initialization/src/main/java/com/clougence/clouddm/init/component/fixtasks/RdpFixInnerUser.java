package com.clougence.clouddm.init.component.fixtasks;

import java.util.ArrayList;
import java.util.Set;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.clougence.clouddm.api.common.crypt.CryptService;
import com.clougence.clouddm.sdk.security.auth.def.SecSysRole;
import com.clougence.rdp.dal.enumeration.AccountType;
import com.clougence.rdp.dal.mapper.RdpRoleMapper;
import com.clougence.rdp.dal.mapper.RdpUserMapper;
import com.clougence.rdp.dal.model.RdpRoleDO;
import com.clougence.rdp.dal.model.RdpUserDO;
import com.clougence.rdp.global.config.RdpConsoleConfig;
import com.clougence.rdp.service.RdpNamingService;
import com.clougence.rdp.service.RdpRoleService;
import com.clougence.rdp.service.RdpUserService;
import com.clougence.utils.StringUtils;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class RdpFixInnerUser {

    @Resource
    private RdpUserMapper    rdpUserMapper;
    @Resource
    private RdpRoleMapper    rdpRoleMapper;
    @Resource
    private RdpRoleService   rdpRoleService;
    @Resource
    private RdpNamingService rdpNamingService;
    @Resource
    private RdpConsoleConfig rdpConfig;

    @Transactional(rollbackFor = Throwable.class, propagation = Propagation.REQUIRED)
    public void init() throws Exception {
        RdpUserDO userDO = this.rdpUserMapper.queryPrimaryByPhone(RdpUserService.INNER_USER_PHONE);
        if (userDO == null) {
            log.info("[RdpFixInnerUser] RdpFixInnerUser: missing internal user try fix it.");
            RdpUserDO registerFO = new RdpUserDO();
            registerFO.setEmail(RdpUserService.INNER_USER_EMAIL);
            registerFO.setUsername(RdpUserService.INNER_USER_NAME);
            registerFO.setPassword(this.rdpNamingService.genInnerUserPwd());
            registerFO.setPhone(RdpUserService.INNER_USER_PHONE);
            registerFO.setCompany(RdpUserService.INNER_USER_COMPANY_NAME);
            userDO = registerInner(registerFO);

            // register role
            Set<String> roleLabel = this.rdpRoleService.getInnerRoleLabel(SecSysRole.ADMIN_ROLE_NAME);

            RdpRoleDO newRole = new RdpRoleDO();
            newRole.setOwnerUid(userDO.getUid());
            newRole.setRoleAuthLabels(new ArrayList<>(roleLabel));
            newRole.setRoleName(SecSysRole.ADMIN_ROLE_NAME);
            newRole.setAliasName(SecSysRole.ADMIN_ROLE_NAME);
            newRole.setInnerTag(true);

            this.rdpRoleMapper.insert(newRole);
            this.rdpUserMapper.updateRoleById(userDO.getId(), newRole.getId());
        } else {
            log.info("[RdpFixInnerUser] RdpFixInnerUser: internal user are normal.");
        }
    }

    private RdpUserDO registerInner(RdpUserDO userInfo) {
        String uid = this.rdpNamingService.genUid();

        String domain;
        if (StringUtils.isNotBlank(rdpConfig.getUserDomainSuffix())) {
            domain = uid + "." + rdpConfig.getUserDomainSuffix();
        } else {
            domain = uid + "." + RdpUserService.DEFAULT_USER_DOMAIN_SUFFIX;
        }

        RdpUserDO userDO = new RdpUserDO();
        userDO.setUid(uid);
        userDO.setPassword(CryptService.INSTANCE.encryptForOneWay(userInfo.getPassword()).getEncryptPassword());
        userDO.setCompany(userInfo.getCompany());
        userDO.setEmail(userInfo.getEmail());
        userDO.setPhoneAreaCode(userInfo.getPhoneAreaCode());
        userDO.setPhone(userInfo.getPhone());
        userDO.setUsername(userInfo.getUsername());
        userDO.setContactMe(userInfo.isContactMe());
        userDO.setSrc(userInfo.getSrc());
        userDO.setKeyword(userInfo.getKeyword());
        userDO.setClientId(userInfo.getClientId());
        userDO.setAccessKey(this.rdpNamingService.genAccessKey());
        userDO.setAccountType(AccountType.PRIMARY_ACCOUNT);
        userDO.setUserDomain(domain);
        userDO.setSecretKey(CryptService.INSTANCE.encryptUseDefaultKeyAndSalt(rdpNamingService.genSecretKey()));
        rdpUserMapper.insert(userDO);
        return userDO;
    }
}
