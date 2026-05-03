package com.clougence.clouddm.console.web.component.auth.impl;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.clougence.clouddm.console.web.component.auth.DmAuthServiceForBiz;
import com.clougence.clouddm.console.web.component.auth.DmResAuthService;
import com.clougence.clouddm.console.web.constants.I18nDmMsgKeys;
import com.clougence.clouddm.console.web.dal.mapper.DmFileMapper;
import com.clougence.clouddm.console.web.dal.model.DmFileDO;
import com.clougence.clouddm.console.web.service.envparam.DmEnvParamService;
import com.clougence.clouddm.console.web.util.DmI18nUtils;
import com.clougence.clouddm.sdk.model.env.EnvParamKeys;
import com.clougence.rdp.constant.I18nRdpMsgKeys;
import com.clougence.rdp.dal.enumeration.AccountType;
import com.clougence.rdp.dal.mapper.RdpDataSourceMapper;
import com.clougence.rdp.dal.mapper.RdpResAuthMapper;
import com.clougence.rdp.dal.mapper.RdpUserMapper;
import com.clougence.rdp.dal.model.RdpDataSourceDO;
import com.clougence.rdp.dal.model.RdpResAuthDO;
import com.clougence.rdp.dal.model.RdpUserDO;
import com.clougence.rdp.global.exception.ErrorMessageException;
import com.clougence.clouddm.sdk.security.auth.AuthKind;
import com.clougence.clouddm.sdk.model.analysis.resource.DsResPath;
import com.clougence.clouddm.sdk.security.auth.def.SecDataAuthLabel;
import com.clougence.clouddm.sdk.security.auth.AuthInfo;
import com.clougence.rdp.service.RdpAuthServiceForBiz;
import com.clougence.rdp.util.RdpI18nUtils;
import com.clougence.utils.CollectionUtils;
import com.clougence.utils.StringUtils;

import lombok.extern.slf4j.Slf4j;

/**
 * @author bucketli 2024/2/21 12:51:24
 */
@Service
@Slf4j
public class DmAuthServiceForBizImpl implements DmAuthServiceForBiz {

    @Resource
    private RdpDataSourceMapper  rdpDsMapper;
    @Resource
    private DmResAuthService     dmDsAuthService;
    @Resource
    private RdpAuthServiceForBiz rdpAuthServiceForBiz;
    @Resource
    private RdpUserMapper        rdpUserMapper;
    @Resource
    private RdpResAuthMapper     rdpResAuthMapper;
    @Resource
    private DmFileMapper         dmFileMapper;
    @Resource
    private DmEnvParamService    dmEnvParamService;

    @Override
    public void checkResPath(String puid, String uid, long resId, AuthKind authKind, DsResPath resPath, String dataAuthLabel) {
        if (!this.checkResPathWithoutError(puid, uid, resId, authKind, resPath, dataAuthLabel)) {
            throwMessageError(resId, resPath, dataAuthLabel);
        }
    }

    @Override
    public void checkBrowseAuth(String puid, String uid, long resId, AuthKind authKind, DsResPath resPath, String dataAuthLabel) {
        if (!checkBrowseResPath(resId, uid, puid, resPath.getResPath(), dataAuthLabel)) {
            throwMessageError(resId, resPath, dataAuthLabel);
        }
    }

    private void throwMessageError(long resId, DsResPath resPath, String dataAuthLabel) {
        AuthInfo authKeyInfo = this.dmDsAuthService.getAuthInfo(dataAuthLabel);

        RdpDataSourceDO dsDO = this.rdpDsMapper.selectById(resId);
        String authRes = dsDO.getInstanceId() + resPath.getResPath();

        String dataAuthMsg = RdpI18nUtils.getMessage(authKeyInfo.getKeyI18n());
        String authMessage = RdpI18nUtils.getMessage(I18nRdpMsgKeys.COMM_DATA_AUTH_PERMISSION_ERROR.name(), authRes, dataAuthMsg);
        throw new ErrorMessageException(authMessage);
    }

    @Override
    public boolean checkResPathWithoutError(String puid, String uid, long resId, AuthKind authKind, DsResPath resPath, String dataAuthLabel) {
        if (authKind == AuthKind.DataSource) {
            RdpDataSourceDO dsDO = this.rdpDsMapper.selectById(resId);
            String enable = this.dmEnvParamService.queryParam(puid, dsDO.getDsEnvId(), EnvParamKeys.DM_ALLOW_ALL_STATEMENTS);
            if (StringUtils.equals(SecDataAuthLabel.DM_DAUTH_OTHER, dataAuthLabel) && StringUtils.equalsIgnoreCase("true", enable)) {
                return false;
            }
        }

        try {
            return this.rdpAuthServiceForBiz.checkResAuthWithoutError(puid, uid, resId, resPath, dataAuthLabel, authKind);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return false;
        }
    }

    @Override
    public boolean checkResPathChildrenWithoutError(String puid, String uid, long resID, AuthKind authKind, DsResPath resPath, String dataAuthLabel) {
        return this.checkBrowseResPath(resID, uid, puid, resPath.getResPath(), dataAuthLabel);
    }

    @Override
    public boolean checkRoleAuthWithoutError(String puid, String uid, String roleAuthLabel) {
        try {
            return this.rdpAuthServiceForBiz.checkRoleAuth(puid, uid, roleAuthLabel);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return false;
        }
    }

    @Override
    public void checkResultFile(String puid, String uid, String fileUniqueId) {
        DmFileDO fileDO = this.dmFileMapper.queryFileByUniqueId(fileUniqueId);
        if (fileDO == null) {
            throw new ErrorMessageException(DmI18nUtils.getMessage(I18nDmMsgKeys.CONSOLE_QUERY_RESULT_FILE_NOT_EXIST_ERROR.name()));
        }

        if (StringUtils.equals(fileDO.getOwnerUid(), puid)) {
            if (StringUtils.equals(puid, uid) || StringUtils.equals(fileDO.getUserId(), uid)) {
                return; // is primary account or owner
            }
        }

        throw new ErrorMessageException(DmI18nUtils.getMessage(I18nDmMsgKeys.CONSOLE_QUERY_RESULT_FILE_NOT_PERMISSION_ERROR.name()));
    }

    private boolean checkBrowseResPath(long dsId, String uid, String puid, String path, String dataAuthLabel) {
        RdpUserDO userDO = rdpUserMapper.queryByUid(uid);
        if (userDO.getAccountType() == AccountType.PRIMARY_ACCOUNT || userDO.isResourceManageEnable()) {
            return true;
        }

        RdpDataSourceDO dsDO = rdpDsMapper.selectById(dsId);
        if (!dsDO.getUid().equals(puid)) {
            throw new IllegalArgumentException(RdpI18nUtils.getMessage(I18nRdpMsgKeys.DS_IS_NOT_BELONG_YOU_PRIMARY_ERROR.name(), dsDO.getId()));
        }

        List<RdpResAuthDO> parentAndSelfAuth = this.rdpResAuthMapper.queryByPathLike(dsId, uid, AuthKind.DataSource, Collections.singletonList(path));
        List<RdpResAuthDO> subAuth = this.rdpResAuthMapper.queryByLikePath(dsId, uid, AuthKind.DataSource, path);

        parentAndSelfAuth = parentAndSelfAuth.stream().filter(r -> r.getAuthLabels().contains(dataAuthLabel)).collect(Collectors.toList());
        subAuth = subAuth.stream().filter(r -> r.getAuthLabels().contains(dataAuthLabel)).collect(Collectors.toList());

        return CollectionUtils.isNotEmpty(parentAndSelfAuth) || CollectionUtils.isNotEmpty(subAuth);
    }
}
