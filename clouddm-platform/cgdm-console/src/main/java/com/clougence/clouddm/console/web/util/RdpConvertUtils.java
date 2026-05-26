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
package com.clougence.clouddm.console.web.util;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

import com.clougence.clouddm.console.web.constants.LoginAuthType;
import com.clougence.clouddm.console.web.dal.enumeration.*;
import com.clougence.clouddm.console.web.dal.mapper.RdpDataSourceMapper;
import com.clougence.clouddm.console.web.dal.model.*;
import com.clougence.clouddm.console.web.model.fo.UpdateSecurityInfoFO;
import com.clougence.clouddm.console.web.model.fo.datasource.AddDsFO;
import com.clougence.clouddm.console.web.model.fo.security.ModifyAuthForAppend;
import com.clougence.clouddm.console.web.model.fo.security.ModifyAuthForDelete;
import com.clougence.clouddm.console.web.model.fo.security.ModifyAuthForUpdate;
import com.clougence.clouddm.console.web.model.fo.ticket.ApplyAuth;
import com.clougence.clouddm.console.web.model.vo.*;
import com.clougence.clouddm.console.web.model.vo.role.RoleAuthTreeVO;
import com.clougence.clouddm.console.web.model.vo.role.RoleInfoVO;
import com.clougence.clouddm.console.web.model.vo.role.RoleVO;
import com.clougence.clouddm.console.web.model.vo.ticket.RdpTicketActivityVO;
import com.clougence.clouddm.console.web.model.vo.ticket.RdpTicketProcessVO;
import com.clougence.clouddm.sdk.approval.ApprovalProvider;
import com.clougence.clouddm.sdk.model.analysis.resource.AuthBrowseObject;
import com.clougence.clouddm.sdk.security.auth.AuthInfo;
import com.clougence.clouddm.sdk.security.auth.AuthInfoType;
import com.clougence.clouddm.sdk.security.auth.AuthKind;
import com.clougence.clouddm.sdk.service.approval.ApprovalActivity;
import com.clougence.clouddm.sdk.service.approval.ApprovalActivityStatus;
import com.clougence.clouddm.sdk.service.config.ConfigData;
import com.clougence.clouddm.sdk.service.config.RoleData;
import com.clougence.clouddm.sdk.service.config.UserData;
import com.clougence.rdp.constant.KvConfValType;
import com.clougence.rdp.global.config.user.UserDefinedConfig;
import com.clougence.utils.ExceptionUtils;
import com.clougence.utils.JsonUtils;
import com.clougence.utils.StringUtils;
import com.clougence.utils.format.DateFormatType;
import com.clougence.utils.format.WellKnowFormat;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;

/**
 * @author mode 2021/1/6 11:25
 */
@Slf4j
public class RdpConvertUtils {

    public static String removeNoDescription(String insDesc) {
        if (StringUtils.isBlank(insDesc) || StringUtils.equalsIgnoreCase(insDesc, "No description")) {
            return null;
        } else {
            return insDesc;
        }
    }

    public static ListUserVO convertToListUserVO(RdpUserDO userDO, Map<Long, RdpRoleDO> roleMap) {
        if (userDO == null) {
            return null;
        }

        ListUserVO vo = new ListUserVO();
        vo.setUid(userDO.getUid());
        vo.setEmail(userDO.getEmail());
        vo.setPhone(userDO.getPhone());
        vo.setSubAccount(userDO.getSubAccount());
        if (userDO.getBindType() == AccountBindType.INTERNAL) {
            if (userDO.getSubAccount().contains("@")) {
                userDO.setBindAccount(userDO.getSubAccount().split("@")[0]);
            } else {
                userDO.setBindAccount(userDO.getSubAccount());
            }
        }
        vo.setSubAccount(userDO.getSubAccount());
        vo.setUserDomain(userDO.getUserDomain());
        vo.setUsername(userDO.getUsername());
        vo.setResourceManage(userDO.isResourceManageEnable());

        vo.setRoleId(userDO.getRoleId());
        if (roleMap.containsKey(userDO.getRoleId())) {
            RdpRoleDO roleDO = roleMap.get(userDO.getRoleId());
            if (roleDO.isInnerTag()) {
                vo.setInnerRole(true);
                vo.setRoleName(DmI18nUtils.getMessage(roleDO.getRoleName()));
            } else {
                vo.setRoleName(roleDO.getAliasName());
            }
        }

        vo.setBindType(userDO.getBindType());
        vo.setBindAccount(userDO.getBindAccount());
        vo.setDisable(userDO.isDisable());
        vo.setUseMfa(userDO.isUseMfa());

        if (userDO.getLastTryLoginTime() != null) {
            vo.setLastTryLoginTime(WellKnowFormat.WKF_DATE_TIME24.format(userDO.getLastTryLoginTime()));
        } else {
            vo.setLastTryLoginTime("--");
        }

        return vo;
    }

    public static LoginUserVO convertToLoginUserVO(RdpUserDO userDO, RdpUserDO primaryUserDO) {
        if (userDO == null) {
            return null;
        }

        LoginUserVO vo = new LoginUserVO();

        if (primaryUserDO != null) {
            vo.setPuid(primaryUserDO.getUid());
            vo.setPUsername(primaryUserDO.getUsername());
            vo.setPEmail(primaryUserDO.getEmail());
        }

        vo.setUid(userDO.getUid());
        vo.setEmail(userDO.getEmail());
        vo.setUsername(userDO.getUsername());
        vo.setUserDomain(userDO.getUserDomain());
        vo.setOrganization(userDO.getCompany());
        vo.setPhone(userDO.getPhone());
        if (userDO.getAccountType() == AccountType.PRIMARY_ACCOUNT) {
            vo.setLoginAccount(userDO.getUsername());
        } else {
            vo.setLoginAccount(userDO.getSubAccount());
        }
        vo.setAccountType(userDO.getAccountType());
        vo.setBindType(userDO.getBindType());
        vo.setBindAccount(userDO.getBindAccount());
        vo.setMaintainer(userDO.isMaintainer());
        vo.setUseMfa(userDO.isUseMfa());

        return vo;
    }

    public static RoleVO convertToRoleVO(RdpRoleDO info, List<String> currentAllAuth) {
        RoleVO vo = new RoleVO();

        vo.setRoleId(info.getId());
        vo.setRoleName(info.getRoleName());
        if (info.isInnerTag()) {
            vo.setAliasName(DmI18nUtils.getMessage(info.getRoleName()));
        } else {
            vo.setAliasName(info.getAliasName());
        }
        vo.setInnerTag(info.isInnerTag());
        vo.setRoleLabels(info.getRoleAuthLabels());
        vo.getRoleLabels().retainAll(currentAllAuth);
        return vo;
    }

    public static RoleInfoVO convertToRoleInfoVO(RdpRoleDO info) {
        RoleInfoVO vo = new RoleInfoVO();
        vo.setRoleId(info.getId());
        vo.setRoleName(info.getRoleName());
        vo.setInnerTag(info.isInnerTag());
        if (info.isInnerTag()) {
            vo.setAliasName(DmI18nUtils.getMessage(info.getRoleName()));
        } else {
            vo.setAliasName(info.getAliasName());
        }
        return vo;
    }

    public static ResAuthVO convertToResAuthVO(DmResAuthDO dsAuthDO) {
        ResAuthVO vo = new ResAuthVO();

        vo.setId(dsAuthDO.getId());
        vo.setResId(dsAuthDO.getResId());
        vo.setResInstId(dsAuthDO.getResInstId());
        vo.setResDesc(dsAuthDO.getResDesc());
        vo.setLevel(dsAuthDO.getResPath());
        vo.setDsAuthKinds(dsAuthDO.getAuthLabels());
        vo.setStartTime(dsAuthDO.getStartTime() == null ? null : DateFormatType.s_yyyyMMdd_HHmmss.format(dsAuthDO.getStartTime()));
        vo.setEndTime(dsAuthDO.getEndTime() == null ? null : DateFormatType.s_yyyyMMdd_HHmmss.format(dsAuthDO.getEndTime()));
        return vo;
    }

    public static List<RoleAuthTreeVO> convertToCtrlAuthTree(List<AuthInfo> categoryList, List<AuthInfo> ctrlAuths) {
        List<AuthInfo> merge = new ArrayList<>();
        merge.addAll(categoryList);
        merge.addAll(ctrlAuths);

        List<AuthInfo> collect = merge.stream().sorted(Comparator.comparingInt(AuthInfo::getOrder)).collect(Collectors.toList());

        Map<String, RoleAuthTreeVO> treeMap = new LinkedHashMap<>();
        for (AuthInfo info : collect) {
            String infoKey = info.getKey();
            treeMap.put(infoKey, convertToRoleAuthTreeVO(info));
        }

        List<RoleAuthTreeVO> rootList = new ArrayList<>();
        for (AuthInfo info : collect) {
            String infoKey = info.getKey();
            RoleAuthTreeVO infoTreeNode = treeMap.get(infoKey);

            String parentKey = infoTreeNode.getParent();
            boolean hasParent = StringUtils.isNotBlank(parentKey) && treeMap.containsKey(parentKey);

            if (hasParent) {
                treeMap.get(parentKey).getChildren().add(infoTreeNode);
            } else {
                rootList.add(infoTreeNode);
            }
        }

        return filterEmptyCategory(rootList);
    }

    public static List<RoleAuthTreeVO> convertToResourceAuthTree(List<AuthInfo> categoryList, List<AuthInfo> resAuths) {
        List<AuthInfo> merge = new ArrayList<>();
        merge.addAll(categoryList);
        merge.addAll(resAuths);

        List<AuthInfo> collect = merge.stream().sorted(Comparator.comparingInt(AuthInfo::getOrder)).collect(Collectors.toList());

        Map<String, RoleAuthTreeVO> treeMap = new LinkedHashMap<>();
        for (AuthInfo info : collect) {
            String infoKey = info.getKey();
            treeMap.put(infoKey, convertToRoleAuthTreeVO(info));
        }

        List<RoleAuthTreeVO> rootList = new ArrayList<>();
        for (AuthInfo info : collect) {
            String infoKey = info.getKey();
            RoleAuthTreeVO infoTreeNode = treeMap.get(infoKey);

            String parentKey = infoTreeNode.getParent();
            boolean hasParent = StringUtils.isNotBlank(parentKey) && treeMap.containsKey(parentKey);

            if (hasParent) {
                treeMap.get(parentKey).getChildren().add(infoTreeNode);
            } else {
                rootList.add(infoTreeNode);
            }
        }

        return filterEmptyCategory(rootList);
    }

    private static List<RoleAuthTreeVO> filterEmptyCategory(List<RoleAuthTreeVO> list) {
        List<RoleAuthTreeVO> result = new ArrayList<>();
        for (RoleAuthTreeVO treeVO : list) {
            if (treeVO.isCategory()) {
                List<RoleAuthTreeVO> children = filterEmptyCategory(treeVO.getChildren());
                if (!children.isEmpty()) {
                    treeVO.setChildren(children);
                    result.add(treeVO);
                }
            } else {
                result.add(treeVO);
            }
        }
        return result;
    }

    private static RoleAuthTreeVO convertToRoleAuthTreeVO(AuthInfo info) {
        RoleAuthTreeVO vo = new RoleAuthTreeVO();

        vo.setKey(info.getKey());
        vo.setParent(info.getAuthType() == AuthInfoType.Category ? info.getParent() : info.getCategory());
        vo.setCategory(info.getAuthType() == AuthInfoType.Category);
        vo.setI18nName(DmI18nUtils.getMessage(info.getKeyI18n()));
        vo.setMustSelectAndReadOnly(info.isMustSelectAndReadOnly());
        return vo;
    }

    public static AddDsFO convertToAddDsFO(String data) {
        if (StringUtils.isBlank(data)) {
            return null;
        }

        try {
            ObjectMapper objectMapper = new ObjectMapper();
            return objectMapper.readValue(data, new TypeReference<AddDsFO>() {});
        } catch (Exception e) {
            String msg = "deserialize add ds info error.msg:" + ExceptionUtils.getRootCauseMessage(e);
            log.error(msg, e);
            throw new RuntimeException(msg, e);
        }
    }

    public static UpdateSecurityInfoFO convertToUpdateSecurityInfoFO(String data) {
        if (StringUtils.isBlank(data)) {
            return null;
        }

        try {
            ObjectMapper objectMapper = new ObjectMapper();
            return objectMapper.readValue(data, new TypeReference<UpdateSecurityInfoFO>() {});
        } catch (Exception e) {
            String msg = "deserialize updateFO ds info error.msg:" + ExceptionUtils.getRootCauseMessage(e);
            log.error(msg, e);
            throw new RuntimeException(msg, e);
        }
    }

    public static RdpDsKvConfigVO convertToDsKvConfigVO(RdpDsKvBaseConfigDO config) {
        RdpDsKvConfigVO vo = new RdpDsKvConfigVO();

        if (!config.isSecret()) {
            vo.setConfigValue(config.getConfigValue());
        }

        vo.setId(config.getId());
        vo.setSecret(config.isSecret());
        vo.setReadOnly(config.isReadOnly());
        vo.setDescription(DmI18nUtils.getMessage(config.getDescKey()));
        vo.setDefaultValue(config.getDefaultValue());
        vo.setValueRequire(config.isValueRequire());
        vo.setValueValidRegex(config.getValueValidRegex());
        vo.setValueAdvance(config.getValueAdvance());

        if (config.getConfValType() != null) {
            vo.setConfValType(config.getConfValType());
        } else {
            vo.setConfValType(KvConfValType.TEXT);
        }

        vo.setConfigGroup(config.getConfigGroup());
        vo.setConfigName(config.getConfigName());
        return vo;
    }

    public static RdpDataSourceVO convertToRdpDataSourceVO(RdpDataSourceDO dsDO) {
        RdpDataSourceVO vo = new RdpDataSourceVO();

        vo.setId(dsDO.getId());
        vo.setInstanceId(dsDO.getInstanceId());
        vo.setInstanceDesc(dsDO.getInstanceDesc());
        vo.setDataSourceType(dsDO.getDataSourceType());
        if (dsDO.getDeployType() != null) {
            vo.setDeployType(dsDO.getDeployType());
            vo.setDeployTypeI18n(DmI18nUtils.getMessage(dsDO.getDeployType().name()));
        }

        vo.setInfoFetchType(dsDO.getInfoFetchType());
        vo.setVersion(dsDO.getVersion());
        vo.setGmtCreate(dsDO.getGmtCreate());
        vo.setHostType(dsDO.getHostType());
        vo.setPrivateHost(dsDO.getPrivateHost());
        vo.setPublicHost(dsDO.getPublicHost());

        vo.setHasPassword(StringUtils.isNotBlank(dsDO.getAccount()) || StringUtils.isNotBlank(dsDO.getPassword()));
        vo.setAccountName(dsDO.getAccount());
        vo.setLifeCycleState(dsDO.getLifeCycleState());
        vo.setSecurityType(dsDO.getSecurityType());
        return vo;
    }

    public static RdpAuthObjectVO convertToRdpAuthObjectVO(AuthBrowseObject dto) {
        RdpAuthObjectVO vo = new RdpAuthObjectVO();

        vo.setObjId(dto.getObjId());
        vo.setObjName(dto.getObjName());
        vo.setObjDesc(dto.getObjDesc());
        vo.setObjType(dto.getObjType().name());
        vo.setObjAttr(dto.getObjAttr());
        vo.setLeaf(dto.isLeaf());
        return vo;
    }

    public static DmResAuthDO convertToAuthDOFromInsert(String targetUid, ModifyAuthForAppend info, String instId, String instDesc, AuthKind authKind) {
        DmResAuthDO dsAuthDO = new DmResAuthDO();
        dsAuthDO.setResId(info.getResId());
        dsAuthDO.setResInstId(instId);
        dsAuthDO.setResDesc(instDesc);
        fillResPath(dsAuthDO, info.getResPaths());
        dsAuthDO.setAuthLabels(info.getAuthLabels());
        dsAuthDO.setKindType(authKind);
        dsAuthDO.setOwnerUid(targetUid);
        dsAuthDO.setStartTime(parseData(info.getStartTime()));
        dsAuthDO.setEndTime(parseData(info.getEndTime()));
        return dsAuthDO;
    }

    public static DmResAuthDO convertToAuthDOFromApply(String targetUid, ApplyAuth info, AuthKind authKind) {
        DmResAuthDO dsAuthDO = new DmResAuthDO();
        dsAuthDO.setResId(info.getResId());
        dsAuthDO.setResInstId(info.getResInstId());
        dsAuthDO.setResDesc(info.getResDesc());
        fillResPath(dsAuthDO, info.getResPaths());
        dsAuthDO.setAuthLabels(info.getAuthLabels());
        dsAuthDO.setKindType(authKind);
        dsAuthDO.setOwnerUid(targetUid);
        dsAuthDO.setStartTime(parseData(info.getStartTime()));
        dsAuthDO.setEndTime(parseData(info.getEndTime()));
        return dsAuthDO;
    }

    public static DmResAuthDO convertToAuthDOFromUpdate(String targetUid, ModifyAuthForUpdate info, String instId, String instDesc, AuthKind authKind) {
        DmResAuthDO dsAuthDO = new DmResAuthDO();
        dsAuthDO.setId(info.getAuthId());
        dsAuthDO.setResId(info.getResId());
        fillResPath(dsAuthDO, info.getResPaths());
        dsAuthDO.setResInstId(instId);
        dsAuthDO.setResDesc(instDesc);
        dsAuthDO.setAuthLabels(info.getAuthLabels());
        dsAuthDO.setKindType(authKind);
        dsAuthDO.setOwnerUid(targetUid);
        dsAuthDO.setStartTime(parseData(info.getStartTime()));
        dsAuthDO.setEndTime(parseData(info.getEndTime()));
        return dsAuthDO;
    }

    private static void fillResPath(DmResAuthDO dsAuthDO, List<String> resPaths) {
        dsAuthDO.setResPath(RdpAuthUtils.genResPathByList(resPaths).getResPath());
        // default
        dsAuthDO.setLevelOne("/");
        switch (resPaths.size()) {
            case 4: {
                dsAuthDO.setLevelFour(resPaths.get(3));
            }
            case 3: {
                dsAuthDO.setLevelThree(resPaths.get(2));
            }
            case 2: {
                dsAuthDO.setLevelTwo(resPaths.get(1));
            }
            case 1: {
                dsAuthDO.setLevelOne(resPaths.get(0));
                break;
            }
        }
    }

    public static DmResAuthDO convertToAuthDOFromUpdate(String targetUid, DmResAuthDO info, AuthKind authKind, RdpDataSourceMapper rdpDsMapper) {
        RdpDataSourceDO dsDO = rdpDsMapper.selectById(info.getResId());
        DmResAuthDO dsAuthDO = new DmResAuthDO();
        dsAuthDO.setResId(info.getResId());
        dsAuthDO.setResInstId(dsDO.getInstanceId());
        dsAuthDO.setResDesc(dsDO.getInstanceDesc());
        dsAuthDO.setResPath(info.getResPath());
        dsAuthDO.setAuthLabels(info.getAuthLabels());
        dsAuthDO.setLevelOne(info.getLevelOne());
        dsAuthDO.setLevelTwo(info.getLevelTwo());
        dsAuthDO.setLevelThree(info.getLevelThree());
        dsAuthDO.setLevelFour(info.getLevelFour());
        dsAuthDO.setKindType(authKind);
        dsAuthDO.setOwnerUid(targetUid);
        dsAuthDO.setStartTime(info.getStartTime());
        dsAuthDO.setEndTime(info.getEndTime());
        return dsAuthDO;
    }

    private static Date parseData(String date) {
        if (StringUtils.isBlank(date)) {
            return null;
        }
        try {
            return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(date);
        } catch (Exception e) {
            throw ExceptionUtils.toRuntime(e);
        }
    }

    public static DmResAuthDO convertToAuthDOFromDelete(String targetUid, ModifyAuthForDelete info, AuthKind authKind) {
        DmResAuthDO dsAuthDO = new DmResAuthDO();
        dsAuthDO.setId(info.getAuthId());
        dsAuthDO.setResId(info.getResId());
        //dsAuthDO.setResInstId(dsDO.getInstanceId());
        //dsAuthDO.setResDesc(dsDO.getInstanceDesc());
        fillResPath(dsAuthDO, info.getResPaths());
        //dsAuthDO.setAuthLabels(info.getAuthLabels());
        dsAuthDO.setKindType(authKind);
        dsAuthDO.setOwnerUid(targetUid);
        //dsAuthDO.setStartTime(info.getStartTime());
        //dsAuthDO.setEndTime(info.getEndTime());
        return dsAuthDO;
    }

    public static DmResAuthDO convertToAuthDOByDataSource(RdpDataSourceDO rdpDatasourceDo, List<String> authLabels) {
        DmResAuthDO resAuthDO = new DmResAuthDO();
        resAuthDO.setResId(rdpDatasourceDo.getId());
        resAuthDO.setResInstId(rdpDatasourceDo.getInstanceId());
        resAuthDO.setResDesc(rdpDatasourceDo.getInstanceDesc());
        resAuthDO.setKindType(AuthKind.DataSource);
        resAuthDO.setAuthLabels(authLabels);
        return resAuthDO;
    }

    public static ConfigData convertToRdpConfigData(RdpUserKvBaseConfigDO kvConfig) {
        ConfigData data = new ConfigData();
        data.setConfigName(kvConfig.getConfigName());
        data.setConfigValue(kvConfig.getConfigValue());
        data.setDefaultValue(kvConfig.getDefaultValue());
        return data;
    }

    public static RoleData convertToRdpRoleData(RdpRoleDO rdpRoleDO) {
        RoleData data = new RoleData();
        data.setRoleId(rdpRoleDO.getId());
        data.setRoleName(rdpRoleDO.getRoleName());
        data.setAliasName(rdpRoleDO.getAliasName());
        data.setInnerTag(rdpRoleDO.isInnerTag());
        data.setLabels(rdpRoleDO.getRoleAuthLabels());
        return data;
    }

    public static UserData convertToRdpUserData(RdpUserDO userDO) {
        UserData data = new UserData();
        data.setInternalUID(userDO.getUid());
        data.setUserName(userDO.getUsername());
        data.setEmail(userDO.getEmail());
        data.setPhone(userDO.getPhone());
        data.setSubAccount(userDO.getSubAccount());
        data.setUserDomain(userDO.getUserDomain());
        data.setRoleId(userDO.getRoleId());
        data.setBindAccount(userDO.getBindAccount());
        return data;
    }

    public static RdpUserDO convertToRdpUserDO(LoginAuthType loginType, RdpUserDO primaryUser, UserData loginUser) {
        RdpUserDO user = new RdpUserDO();
        user.setUsername(loginUser.getUserName());
        user.setEmail(loginUser.getEmail());
        user.setPhone(loginUser.getPhone());
        user.setSubAccount(loginUser.getSubAccount());
        user.setCompany(primaryUser.getCompany());
        //user.setPassword(BCryptOneWayCryptService.getInstance().encrypt(Long.toHexString(System.currentTimeMillis())).getEncryptPassword());
        user.setLoginLocked(false);
        user.setAccountType(AccountType.SUB_ACCOUNT);
        user.setBindType(loginType.getBindType());
        user.setBindAccount(loginUser.getBindAccount());
        user.setUserDomain(primaryUser.getUserDomain());
        user.setDisable(false);
        user.setParentId(primaryUser.getId());
        user.setUserStatus(UserStatus.NORMAL);
        user.setRoleId(loginUser.getRoleId());
        user.setAccessToken(loginUser.getAccessToken());
        return user;
    }

    public static String convertToApprovalEnableConfigKey(ApprovalProvider type) {
        switch (type) {
            case Feishu:
                return UserDefinedConfig.Fields.feishuEnableApprovalService;
            case Wechat:
                return UserDefinedConfig.Fields.wechatEnableApprovalService;
            case DingTalk:
                return UserDefinedConfig.Fields.dingEnableApprovalService;
            case Internal:
            case Custom:
            default:
                return null;
        }
    }

    public static RdpTicketProcessVO convertToTicketProcessVO(DmApprovalProcessDO processDO) {
        RdpTicketProcessVO vo = new RdpTicketProcessVO();
        vo.setTicketProcessId(processDO.getId());
        vo.setGmtCreate(DateFormatType.s_yyyyMMdd_HHmmss.format(processDO.getGmtCreate()));
        vo.setGmtModified(DateFormatType.s_yyyyMMdd_HHmmss.format(processDO.getGmtModified()));
        vo.setTicketStage(processDO.getTicketStage());
        vo.setTicketStageTitle(DmI18nUtils.getMessage(processDO.getTicketStage().getI18nKey()));
        vo.setFinishTime(DateFormatType.s_yyyyMMdd_HHmmss.format(processDO.getFinishTime()));
        vo.setStageContext(processDO.getStageContext());
        vo.setTicketProcessStatus(processDO.getProcessStatus());
        return vo;
    }

    public static List<RdpTicketActivityVO> convertToTicketActivityVO(RdpTicketProcessStatus processStatus, DmApprovalProcessActivityDO activityDO) {

        // not arrived nodes
        if (StringUtils.isEmpty(activityDO.getContext())) {
            if (activityDO.getOrderNumber() == -1) {
                return Collections.emptyList();
            }
            RdpTicketActivityVO vo = new RdpTicketActivityVO();
            vo.setProcessId(activityDO.getProcessId());
            vo.setTicketId(activityDO.getTicketId());
            vo.setActivityTitle(activityDO.getActivityTitle());
            vo.setGmtModified(DateFormatType.s_yyyyMMdd_HHmmss.format(activityDO.getGmtModified()));
            vo.setGmtCreate(DateFormatType.s_yyyyMMdd_HHmmss.format(activityDO.getGmtCreate()));

            if (processStatus == RdpTicketProcessStatus.INIT) {
                vo.setActivityStatus(RdpTicketProcessActivityStatus.NEW);
            } else if (processStatus == RdpTicketProcessStatus.REJECT) {
                vo.setActivityStatus(RdpTicketProcessActivityStatus.CANCELED);
            } else if (processStatus == RdpTicketProcessStatus.CLOSED) {
                vo.setActivityStatus(RdpTicketProcessActivityStatus.CANCELED);
            }

            return Collections.singletonList(vo);
        } else {
            List<String> approvalUsers = new ArrayList<>();
            ArrayList<RdpTicketActivityVO> list = new ArrayList<>();
            List<ApprovalActivity> list1 = JsonUtils.toList(activityDO.getContext(), new TypeReference<List<ApprovalActivity>>() {});
            RdpTicketProcessActivityStatus status = null;

            for (ApprovalActivity task : list1) {
                if (task.getStatus() == ApprovalActivityStatus.NEW || task.getStatus() == null) {
                    continue;
                }

                if (task.getStatus() == ApprovalActivityStatus.RUNNING || task.getStatus() == ApprovalActivityStatus.CANCELED) {
                    approvalUsers.add(task.getUserName());
                    status = RdpTicketProcessActivityStatus.valueOf(task.getStatus().name());
                    continue;
                }

                if (task.getStatus() == ApprovalActivityStatus.CLOSE) {
                    continue;
                }
                RdpTicketActivityVO vo = new RdpTicketActivityVO();
                vo.setStartTime(DateFormatType.s_yyyyMMdd_HHmmss.format(task.getStartTime()));
                vo.setActivityTitle(activityDO.getActivityTitle());
                vo.setFinishTime(DateFormatType.s_yyyyMMdd_HHmmss.format(task.getFinishTime()));
                //                vo.setGmtModified(DateFormatType.s_yyyyMMdd_HHmmss.format(activityDO.getGmtModified()));
                //                vo.setGmtCreate(DateFormatType.s_yyyyMMdd_HHmmss.format(activityDO.getGmtCreate()));
                vo.setActivityStatus(RdpTicketProcessActivityStatus.valueOf(task.getStatus().name()));
                vo.setRemark(task.getRemark());
                vo.setApprovalUserList(Collections.singletonList(task.getUserName()));
                list.add(vo);
            }

            if (!approvalUsers.isEmpty()) {
                RdpTicketActivityVO vo = new RdpTicketActivityVO();
                vo.setActivityTitle(activityDO.getActivityTitle());
                vo.setGmtModified(DateFormatType.s_yyyyMMdd_HHmmss.format(activityDO.getGmtModified()));
                vo.setGmtCreate(DateFormatType.s_yyyyMMdd_HHmmss.format(activityDO.getGmtCreate()));
                vo.setApprovalUserList(approvalUsers);
                vo.setActivityStatus(status);
                list.add(vo);
            }

            return list;
        }
    }
}
