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
package com.clougence.clouddm.console.web.component.approval.impl;

import static com.clougence.clouddm.console.web.component.approval.CustomApprovalConfigService.serializeContext;
import static com.clougence.clouddm.console.web.component.approval.CustomApprovalConstants.*;

import java.util.*;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import com.clougence.clouddm.api.common.exception.ErrorMessageException;
import com.clougence.clouddm.console.web.component.approval.CustomApprovalConfigService;
import com.clougence.clouddm.console.web.component.approval.model.*;
import com.clougence.clouddm.console.web.global.i18n.DmI18nUtils;
import com.clougence.clouddm.console.web.global.i18n.I18nDmMsgKeys;
import com.clougence.clouddm.platform.dal.access.ApprovalDal;
import com.clougence.clouddm.platform.dal.access.AuthDal;
import com.clougence.clouddm.platform.dal.access.SystemDal;
import com.clougence.clouddm.platform.dal.model.approval.*;
import com.clougence.clouddm.platform.dal.model.auth.DmAuthRoleDO;
import com.clougence.clouddm.platform.dal.model.auth.DmAuthUserDO;
import com.clougence.clouddm.platform.dal.model.auth.UserStatus;
import com.clougence.clouddm.platform.dal.model.system.DmSysEnvParamDO;
import com.clougence.clouddm.platform.dal.model.system.DmSysUserConfDO;
import com.clougence.clouddm.sdk.model.env.EnvParamKeys;
import com.clougence.clouddm.sdk.security.auth.def.SecRoleAuthLabel;
import com.clougence.rdp.service.model.EnvTicketMO;
import com.clougence.utils.JsonUtils;
import com.clougence.utils.StringUtils;

import jakarta.annotation.Resource;

@Service
public class CustomApprovalConfigServiceImpl implements CustomApprovalConfigService {

    private static final String ENGINE_KEY = "internalApprovalEngines";
    private static final String TEAM_KEY   = "approvalTeams";
    @Resource
    private AuthDal             authDal;
    @Resource
    private SystemDal           systemDal;
    @Resource
    private ApprovalDal         approvalDal;

    @Override
    public void lockConfiguration(String puid) {
        if (authDal.userMapper().lockByUid(puid) == null) {
            throw new ErrorMessageException(DmI18nUtils.getMessage(I18nDmMsgKeys.CUSTOM_APPROVAL_CONFIG_INVALID.name()));
        }
    }

    @Override
    @Transactional(readOnly = true)
    public ApprovalConfigurationVO getConfiguration(String puid) {
        InternalApprovalEngineConfig engines = getEngineConfig(puid);
        ApprovalTeamConfig teams = getTeamConfig(puid);
        List<ApprovalFlowVO> flows = new ArrayList<>();
        List<DmApprovalTemplateDO> templates = listCustomTemplates(puid);
        for (DmApprovalTemplateDO template : templates) {
            ApprovalFlowVO flow = new ApprovalFlowVO();
            flow.setEngineConfigId(template.getEngineConfigId());
            flow.setVersion(template.getVersion());
            flow.setDefinition(parseFlowConfig(template));
            flows.add(flow);
        }

        DmAuthUserDO primary = authDal.userMapper().queryByUid(puid);
        List<DmAuthUserDO> users = new ArrayList<>(authDal.userMapper().listByParentId(primary.getId()));
        users.add(primary);
        List<ApprovalParticipantVO> participants = new ArrayList<>();
        for (DmAuthUserDO user : users) {
            boolean approve = isEligibleUser(puid, user, SecRoleAuthLabel.RDP_WORKER_ORDER_APPROVE);
            boolean execute = isEligibleUser(puid, user, SecRoleAuthLabel.RDP_WORKER_ORDER_EXECUTE);
            if (!approve && !execute) {
                continue;
            }

            ApprovalParticipantVO participant = new ApprovalParticipantVO();
            participant.setUserUid(user.getUid());
            participant.setUserName(user.getUsername());
            participant.setCanApprove(approve);
            participant.setCanExecute(execute);
            participants.add(participant);
        }

        ApprovalConfigurationVO result = new ApprovalConfigurationVO();
        result.setEngineConfig(engines);
        result.setTeamConfig(teams);
        result.setFlows(flows);
        result.setParticipants(participants);
        return result;
    }

    private DmSysUserConfDO readConfiguration(String puid, String key) {
        if (TransactionSynchronizationManager.isActualTransactionActive() && !TransactionSynchronizationManager.isCurrentTransactionReadOnly()) {
            return systemDal.userConfMapper().queryByUidAndConfigNameForUpdate(puid, key);
        }
        return systemDal.userConfMapper().queryByUidAndConfigName(puid, key);
    }

    @Override
    public InternalApprovalEngineConfig getEngineConfig(String puid) {
        DmSysUserConfDO row = readConfiguration(puid, ENGINE_KEY);
        if (row == null) {
            return new InternalApprovalEngineConfig();
        }

        InternalApprovalEngineConfig config = JsonUtils.toObj(row.getConfigValue(), InternalApprovalEngineConfig.class);
        if (config.getSchemaVersion() != ENGINE_CONFIG_SCHEMA_VERSION || !ENGINE_CONFIG_CONTEXT_TYPE.equals(config.getContextType())) {
            throw new ErrorMessageException(DmI18nUtils.getMessage(I18nDmMsgKeys.CUSTOM_APPROVAL_CONFIG_INVALID.name()));
        }
        return config;
    }

    @Override
    public ApprovalTeamConfig getTeamConfig(String puid) {
        DmSysUserConfDO row = readConfiguration(puid, TEAM_KEY);
        if (row == null) {
            return new ApprovalTeamConfig();
        }

        ApprovalTeamConfig config = JsonUtils.toObj(row.getConfigValue(), ApprovalTeamConfig.class);
        if (config.getSchemaVersion() != TEAM_CONFIG_SCHEMA_VERSION || !TEAM_CONFIG_CONTEXT_TYPE.equals(config.getContextType())) {
            throw new ErrorMessageException(DmI18nUtils.getMessage(I18nDmMsgKeys.CUSTOM_APPROVAL_CONFIG_INVALID.name()));
        }
        return config;
    }

    @Override
    public List<DmApprovalTemplateDO> listCustomTemplates(String puid) {
        return approvalDal.templateMapper().listCustomTemplates(puid);
    }

    @Override
    public CustomApprovalFlowConfig parseFlowConfig(DmApprovalTemplateDO template) {
        CustomApprovalFlowConfig flowConfig = JsonUtils.toObj(template.getTemplateContent(), CustomApprovalFlowConfig.class);
        if (flowConfig == null || flowConfig.getSchemaVersion() != FLOW_SCHEMA_VERSION || !FLOW_CONTEXT_TYPE.equals(flowConfig.getContextType())) {
            throw new ErrorMessageException(DmI18nUtils.getMessage(I18nDmMsgKeys.CUSTOM_APPROVAL_CONFIG_INVALID.name()));
        }
        return flowConfig;
    }

    @Override
    @Transactional(rollbackFor = Throwable.class)
    public void saveEngine(String puid, ApprovalEngineSaveFO request) {
        lockConfiguration(puid);
        InternalApprovalEngineConfig config = getEngineConfig(puid);
        if (config.getVersion() != request.getConfigVersion()) {
            throw new ErrorMessageException(DmI18nUtils.getMessage(I18nDmMsgKeys.CUSTOM_APPROVAL_CONFIG_CONFLICT.name()));
        }

        String name = StringUtils.trim(request.getEngineName());
        if (StringUtils.isBlank(name) || name.length() > MAX_ENGINE_NAME_LENGTH) {
            throw new ErrorMessageException(DmI18nUtils.getMessage(I18nDmMsgKeys.CUSTOM_APPROVAL_CONFIG_INVALID.name()));
        }

        boolean creating = StringUtils.isBlank(request.getEngineConfigId());
        InternalApprovalEngine engine;
        DmApprovalTemplateDO template;
        if (creating) {
            if (request.getVersion() != 0) {
                throw new ErrorMessageException(DmI18nUtils.getMessage(I18nDmMsgKeys.CUSTOM_APPROVAL_CONFIG_CONFLICT.name()));
            }

            engine = new InternalApprovalEngine();
            engine.setEngineConfigId(UUID.randomUUID().toString());
            config.getEngines().add(engine);
            template = new DmApprovalTemplateDO();
            template.setPrimaryUid(puid);
            template.setApprovalType(ApprovalType.Internal);
            template.setTemplateSource(ApprovalTemplateSource.CUSTOM);
            template.setEngineConfigId(engine.getEngineConfigId());
            template.setTemplateIdentity(TEMPLATE_ID_PREFIX + UUID.randomUUID());
            template.setVersion(1L);
        } else {
            engine = null;
            for (InternalApprovalEngine candidate : config.getEngines()) {
                if (candidate.getEngineConfigId().equals(request.getEngineConfigId())) {
                    engine = candidate;
                    break;
                }
            }

            if (engine == null) {
                throw new ErrorMessageException(DmI18nUtils.getMessage(I18nDmMsgKeys.CUSTOM_APPROVAL_CONFIG_CONFLICT.name()));
            }

            template = approvalDal.templateMapper().queryCustomByEngineId(puid, engine.getEngineConfigId());
            if (template == null) {
                throw new ErrorMessageException(DmI18nUtils.getMessage(I18nDmMsgKeys.CUSTOM_APPROVAL_CONFIG_INVALID.name()));
            }

            if (template.getVersion() != request.getVersion()) {
                throw new ErrorMessageException(DmI18nUtils.getMessage(I18nDmMsgKeys.CUSTOM_APPROVAL_CONFIG_CONFLICT.name()));
            }
        }

        for (InternalApprovalEngine candidate : config.getEngines()) {
            if (!candidate.getEngineConfigId().equals(engine.getEngineConfigId()) && name.equalsIgnoreCase(candidate.getEngineName())) {
                throw new ErrorMessageException(DmI18nUtils.getMessage(I18nDmMsgKeys.CUSTOM_APPROVAL_NAME_DUPLICATE.name()));
            }
        }

        validateFlowConfig(puid, request.getDefinition(), getTeamConfig(puid));
        engine.setEngineName(name);
        template.setTemplateName(name);
        template.setTemplateContent(serializeContext(request.getDefinition()));
        template.setGmtModified(new Date());
        if (creating) {
            approvalDal.templateMapper().insert(template);
        } else {
            long version = template.getVersion();
            template.setVersion(version + 1);
            int changed = approvalDal.templateMapper().updateCustomTemplate(template, version);
            if (changed != 1) {
                throw new ErrorMessageException(DmI18nUtils.getMessage(I18nDmMsgKeys.CUSTOM_APPROVAL_CONFIG_CONFLICT.name()));
            }
        }

        config.setVersion(config.getVersion() + 1);
        saveConfiguration(puid, ENGINE_KEY, config);
    }

    @Override
    @Transactional(rollbackFor = Throwable.class)
    public void saveTeams(String puid, ApprovalTeamConfig request) {
        lockConfiguration(puid);
        ApprovalTeamConfig current = getTeamConfig(puid);
        if (request.getVersion() != current.getVersion()) {
            throw new ErrorMessageException(DmI18nUtils.getMessage(I18nDmMsgKeys.CUSTOM_APPROVAL_CONFIG_CONFLICT.name()));
        }

        Set<String> ids = new HashSet<>();
        Set<String> names = new HashSet<>();
        if (request.getTeams() == null) {
            throw new ErrorMessageException(DmI18nUtils.getMessage(I18nDmMsgKeys.CUSTOM_APPROVAL_CONFIG_INVALID.name()));
        }

        for (ApprovalTeam team : request.getTeams()) {
            if (StringUtils.isBlank(team.getTeamId())) {
                team.setTeamId(UUID.randomUUID().toString());
            }

            validateUuid(team.getTeamId());
            team.setTeamName(StringUtils.trim(team.getTeamName()));
            if (!ids.add(team.getTeamId()) || StringUtils.isBlank(team.getTeamName()) || team.getTeamName().length() > MAX_TEAM_NAME_LENGTH
                || !names.add(team.getTeamName().toLowerCase(Locale.ROOT)) || (team.getDescription() != null && team.getDescription().length() > MAX_TEAM_DESCRIPTION_LENGTH)) {
                throw new ErrorMessageException(DmI18nUtils.getMessage(I18nDmMsgKeys.CUSTOM_APPROVAL_CONFIG_INVALID.name()));
            }

            if (team.getMemberUids() == null || team.getMemberUids().isEmpty()) {
                throw new ErrorMessageException(DmI18nUtils.getMessage(I18nDmMsgKeys.CUSTOM_APPROVAL_PARTICIPANTS_INVALID.name()));
            }

            Set<String> canonicalUids = new LinkedHashSet<>();
            for (String uid : new LinkedHashSet<>(team.getMemberUids())) {
                DmAuthUserDO user = authDal.userMapper().queryByUid(uid);
                if (!isEligibleUser(puid, user, SecRoleAuthLabel.RDP_WORKER_ORDER_APPROVE) && !isEligibleUser(puid, user, SecRoleAuthLabel.RDP_WORKER_ORDER_EXECUTE)) {
                    throw new ErrorMessageException(DmI18nUtils.getMessage(I18nDmMsgKeys.CUSTOM_APPROVAL_PARTICIPANTS_INVALID.name()));
                }

                canonicalUids.add(user.getUid());
            }

            team.setMemberUids(new ArrayList<>(canonicalUids));
        }

        List<DmApprovalTemplateDO> templates = listCustomTemplates(puid);
        for (DmApprovalTemplateDO template : templates) {
            validateFlowConfig(puid, parseFlowConfig(template), request);
        }

        request.setContextType(TEAM_CONFIG_CONTEXT_TYPE);
        request.setSchemaVersion(TEAM_CONFIG_SCHEMA_VERSION);
        request.setVersion(current.getVersion() + 1);
        saveConfiguration(puid, TEAM_KEY, request);
    }

    @Override
    @Transactional(rollbackFor = Throwable.class)
    public void deleteEngine(String puid, ApprovalEngineDeleteFO request) {
        lockConfiguration(puid);
        InternalApprovalEngineConfig config = getEngineConfig(puid);
        if (request.getConfigVersion() != config.getVersion()) {
            throw new ErrorMessageException(DmI18nUtils.getMessage(I18nDmMsgKeys.CUSTOM_APPROVAL_CONFIG_CONFLICT.name()));
        }

        InternalApprovalEngine engine = null;
        for (InternalApprovalEngine candidate : config.getEngines()) {
            if (candidate.getEngineConfigId().equals(request.getEngineConfigId())) {
                engine = candidate;
                break;
            }
        }

        if (engine == null) {
            throw new ErrorMessageException(DmI18nUtils.getMessage(I18nDmMsgKeys.CUSTOM_APPROVAL_CONFIG_CONFLICT.name()));
        }

        DmApprovalTemplateDO template = approvalDal.templateMapper().queryCustomByEngineId(puid, engine.getEngineConfigId());
        if (template == null) {
            throw new ErrorMessageException(DmI18nUtils.getMessage(I18nDmMsgKeys.CUSTOM_APPROVAL_CONFIG_INVALID.name()));
        }

        List<DmSysEnvParamDO> parameters = systemDal.envParamMapper().queryByUid(puid);
        for (DmSysEnvParamDO param : parameters) {
            if (!Set.of(EnvParamKeys.SQL_TICKET_INFO, EnvParamKeys.CHANGE_TICKET_INFO, EnvParamKeys.AUTH_TICKET_INFO).contains(param.getConfigKey())
                || StringUtils.isBlank(param.getConfigValue())) {
                continue;
            }

            EnvTicketMO binding = JsonUtils.toObj(param.getConfigValue(), EnvTicketMO.class);
            if (template.getTemplateIdentity().equals(binding.getTemplateId())) {
                throw new ErrorMessageException(DmI18nUtils.getMessage(I18nDmMsgKeys.CUSTOM_APPROVAL_CONFIG_IN_USE.name()));
            }
        }

        List<String> terminalStatuses = new ArrayList<>();
        for (ApprovalStatus status : ApprovalStatus.values()) {
            if (ApprovalStatus.isEndStatus(status)) {
                terminalStatuses.add(status.name());
            }
        }

        long activeTickets = approvalDal.approvalMapper().countActiveByTemplate(puid, template.getTemplateIdentity(), ApprovalType.Internal.name(), terminalStatuses);
        if (activeTickets > 0) {
            throw new ErrorMessageException(DmI18nUtils.getMessage(I18nDmMsgKeys.CUSTOM_APPROVAL_CONFIG_IN_USE.name()));
        }

        approvalDal.templateMapper().deleteById(template.getId());
        config.getEngines().remove(engine);
        config.setVersion(config.getVersion() + 1);
        saveConfiguration(puid, ENGINE_KEY, config);
    }

    @Override
    public void validateFlowConfig(String puid, CustomApprovalFlowConfig flowConfig, ApprovalTeamConfig teams) {
        if (flowConfig == null || flowConfig.getSchemaVersion() != FLOW_SCHEMA_VERSION || !FLOW_CONTEXT_TYPE.equals(flowConfig.getContextType()) || flowConfig.getNodes() == null
            || flowConfig.getNodes().isEmpty() || flowConfig.getExecutionMode() == null) {
            throw new ErrorMessageException(DmI18nUtils.getMessage(I18nDmMsgKeys.CUSTOM_APPROVAL_CONFIG_INVALID.name()));
        }

        Set<String> ids = new HashSet<>();
        for (CustomApprovalNodeConfig node : flowConfig.getNodes()) {
            validateUuid(node.getNodeId());
            if (!ids.add(node.getNodeId())) {
                throw new ErrorMessageException(DmI18nUtils.getMessage(I18nDmMsgKeys.CUSTOM_APPROVAL_CONFIG_INVALID.name()));
            }

            List<DmAuthUserDO> users = resolveParticipants(puid, node.getApproverType(), node.getTeamId(), node.getUserUid(), teams, SecRoleAuthLabel.RDP_WORKER_ORDER_APPROVE);
            if (node.getApproverType() == ApprovalParticipantType.TEAM) {
                if (node.getRequiredCount() == null || node.getRequiredCount() < 1 || node.getRequiredCount() > users.size()) {
                    throw new ErrorMessageException(DmI18nUtils.getMessage(I18nDmMsgKeys.CUSTOM_APPROVAL_PARTICIPANTS_INVALID.name()));
                }
            } else if (node.getRequiredCount() != null) {
                throw new ErrorMessageException(DmI18nUtils.getMessage(I18nDmMsgKeys.CUSTOM_APPROVAL_CONFIG_INVALID.name()));
            }
        }

        ApprovalExecutorConfig executorConfig = flowConfig.getExecutionAssignment();
        if (flowConfig.getExecutionMode() == ApprovalExecutionMode.MANUAL) {
            if (executorConfig == null) {
                throw new ErrorMessageException(DmI18nUtils.getMessage(I18nDmMsgKeys.CUSTOM_APPROVAL_CONFIG_INVALID.name()));
            }

            resolveParticipants(puid, executorConfig.getParticipantType(), executorConfig.getTeamId(), executorConfig
                .getUserUid(), teams, SecRoleAuthLabel.RDP_WORKER_ORDER_EXECUTE);
        } else if (executorConfig != null) {
            throw new ErrorMessageException(DmI18nUtils.getMessage(I18nDmMsgKeys.CUSTOM_APPROVAL_CONFIG_INVALID.name()));
        }

        serializeContext(flowConfig);
    }

    @Override
    public List<DmAuthUserDO> resolveParticipants(String puid, ApprovalParticipantType type, String teamId, String userUid, ApprovalTeamConfig teams, String role) {
        List<String> uids;
        if (type == ApprovalParticipantType.TEAM && StringUtils.isNotBlank(teamId) && StringUtils.isBlank(userUid)) {
            uids = null;
            for (ApprovalTeam team : teams.getTeams()) {
                if (teamId.equals(team.getTeamId())) {
                    uids = team.getMemberUids();
                    break;
                }
            }

            if (uids == null) {
                throw new ErrorMessageException(DmI18nUtils.getMessage(I18nDmMsgKeys.CUSTOM_APPROVAL_CONFIG_IN_USE.name()));
            }
        } else if (type == ApprovalParticipantType.PERSON && StringUtils.isNotBlank(userUid) && StringUtils.isBlank(teamId)) {
            uids = List.of(userUid);
        } else {
            throw new ErrorMessageException(DmI18nUtils.getMessage(I18nDmMsgKeys.CUSTOM_APPROVAL_CONFIG_INVALID.name()));
        }

        List<DmAuthUserDO> users = new ArrayList<>();
        for (String uid : new LinkedHashSet<>(uids)) {
            DmAuthUserDO user = authDal.userMapper().queryByUid(uid);
            if (!isEligibleUser(puid, user, role)) {
                throw new ErrorMessageException(DmI18nUtils.getMessage(I18nDmMsgKeys.CUSTOM_APPROVAL_PARTICIPANTS_INVALID.name()));
            }

            users.add(user);
        }

        if (users.isEmpty()) {
            throw new ErrorMessageException(DmI18nUtils.getMessage(I18nDmMsgKeys.CUSTOM_APPROVAL_PARTICIPANTS_INVALID.name()));
        }
        return users;
    }

    @Override
    public boolean isEligibleUser(String puid, DmAuthUserDO user, String label) {
        if (user == null || user.isDisable() || user.getUserStatus() != UserStatus.NORMAL) {
            return false;
        }

        DmAuthUserDO primary = authDal.userMapper().queryByUid(puid);
        if (primary == null || (!puid.equals(user.getUid()) && !primary.getId().equals(user.getParentId()))) {
            return false;
        }

        DmAuthRoleDO role = authDal.roleMapper().selectById(user.getRoleId());
        return role != null && role.getRoleAuthLabels() != null && role.getRoleAuthLabels().contains(label);
    }

    @Override
    @Transactional(rollbackFor = Throwable.class)
    public DmApprovalTemplateDO lockAndValidateTemplate(String puid, String identity) {
        lockConfiguration(puid);
        DmApprovalTemplateDO template = approvalDal.templateMapper().queryCustomForUpdate(puid, identity);
        if (template == null) {
            throw new ErrorMessageException(DmI18nUtils.getMessage(I18nDmMsgKeys.CUSTOM_APPROVAL_CONFIG_INVALID.name()));
        }

        validateFlowConfig(puid, parseFlowConfig(template), getTeamConfig(puid));
        return template;
    }

    private void saveConfiguration(String puid, String key, Object value) {
        String json = serializeContext(value);
        if (systemDal.userConfMapper().queryByUidAndConfigName(puid, key) == null) {
            systemDal.userConfMapper().insertUserConfig(puid, key, json);
        } else {
            systemDal.userConfMapper().updateUserConfig(puid, key, json);
        }
    }

    private static void validateUuid(String value) {
        try {
            if (value == null || !UUID.fromString(value).toString().equalsIgnoreCase(value)) {
                throw new ErrorMessageException(DmI18nUtils.getMessage(I18nDmMsgKeys.CUSTOM_APPROVAL_CONFIG_INVALID.name()));
            }
        } catch (IllegalArgumentException e) {
            throw new ErrorMessageException(DmI18nUtils.getMessage(I18nDmMsgKeys.CUSTOM_APPROVAL_CONFIG_INVALID.name()));
        }
    }

}
