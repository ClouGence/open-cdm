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
package com.clougence.clouddm.console.web.service.envparam;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.clougence.clouddm.console.web.component.approval.ApprovalFlowService;
import com.clougence.clouddm.console.web.global.i18n.DmI18nUtils;
import com.clougence.clouddm.console.web.model.fo.envparam.DmBindEnvParamFO;
import com.clougence.clouddm.console.web.model.fo.envparam.DmUnbindEnvParamFO;
import com.clougence.clouddm.console.web.model.vo.envparam.DmEnvParamOpenVO;
import com.clougence.clouddm.console.web.model.vo.envparam.DmEnvParamSecDesVO;
import com.clougence.clouddm.console.web.model.vo.envparam.DmEnvParamTicketDesVO;
import com.clougence.clouddm.platform.dal.access.ApprovalDal;
import com.clougence.clouddm.platform.dal.access.SecRuleDal;
import com.clougence.clouddm.platform.dal.access.SystemDal;
import com.clougence.clouddm.platform.dal.model.approval.ApprovalType;
import com.clougence.clouddm.platform.dal.model.approval.DmApprovalTemplateDO;
import com.clougence.clouddm.platform.dal.model.secrule.DmSecSpecDO;
import com.clougence.clouddm.platform.dal.model.system.DmSysEnvDO;
import com.clougence.clouddm.platform.dal.model.system.DmSysEnvParamDO;
import com.clougence.clouddm.platform.plugin.PluginManager;
import com.clougence.clouddm.sdk.approval.ApprovalProviderSpi;
import com.clougence.clouddm.sdk.model.env.EnvParamKeys;
import com.clougence.rdp.service.model.EnvTicketMO;
import com.clougence.utils.CollectionUtils;
import com.clougence.utils.JsonUtils;
import com.clougence.utils.StringUtils;

import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;

/**
 * @Author: Ekko
 * @Date: 2024-05-31 10:05
 */
@Service
@Slf4j
public class DmEnvParamServiceImpl implements DmEnvParamService {
    @Resource
    private SystemDal   systemDal;
    @Resource
    private SecRuleDal  secRuleDal;
    @Resource
    private ApprovalDal approvalDal;

    @Override
    public void bindEnvParam(String ownerUid, String uid, DmBindEnvParamFO fo) {
        long envId = fo.getEnvId();
        String paramKey = fo.getParamKey();
        String paramValue = fo.getParamValue();

        DmSysEnvParamDO rdpEnvParamDO = this.systemDal.envParamMapper().queryByParamKey(ownerUid, paramKey, envId);
        if (rdpEnvParamDO != null && StringUtils.isNotBlank(rdpEnvParamDO.getConfigValue())) {
            processTicketEnvParam(ownerUid, envId, paramKey, rdpEnvParamDO.getConfigValue(), paramValue);

            // update
            rdpEnvParamDO.setConfigValue(paramValue);
            this.systemDal.envParamMapper().updateById(rdpEnvParamDO);
        } else {
            processTicketEnvParam(ownerUid, envId, paramKey, null, paramValue);

            // insert
            rdpEnvParamDO = new DmSysEnvParamDO();
            rdpEnvParamDO.setEnvId(envId);
            rdpEnvParamDO.setConfigKey(paramKey);
            rdpEnvParamDO.setConfigValue(paramValue);
            rdpEnvParamDO.setPrimaryUid(ownerUid);
            this.systemDal.envParamMapper().insert(rdpEnvParamDO);
        }
    }

    @Override
    public void unbindEnvParam(String ownerUid, String uid, DmUnbindEnvParamFO fo) {
        long envId = fo.getEnvId();
        String paramKey = fo.getParamKey();
        String paramValue = null;
        DmSysEnvParamDO rdpEnvParamDO = this.systemDal.envParamMapper().queryByParamKey(ownerUid, paramKey, envId);
        if (rdpEnvParamDO != null && StringUtils.isNotBlank(rdpEnvParamDO.getConfigValue())) {
            paramValue = rdpEnvParamDO.getConfigValue();
        }

        processTicketEnvParam(ownerUid, fo.getEnvId(), fo.getParamKey(), paramValue, null);
        this.systemDal.envParamMapper().deleteEnvParam(fo.getParamKey(), ownerUid, fo.getEnvId());
    }

    private void processTicketEnvParam(String ownerUid, long envId, String configKey, String beforeValue, String afterValue) {
        boolean isTicket = StringUtils.equals(configKey, EnvParamKeys.AUTH_TICKET_INFO) ||//
                           StringUtils.equals(configKey, EnvParamKeys.SQL_TICKET_INFO) ||//
                           StringUtils.equals(configKey, EnvParamKeys.CHANGE_TICKET_INFO);
        if (!isTicket) {
            return;
        }

        if (StringUtils.isNotBlank(beforeValue)) {
            EnvTicketMO ticketMO = JsonUtils.toObj(beforeValue, EnvTicketMO.class);
            if (StringUtils.isNotBlank(ticketMO.getApprovalType())) {
                ApprovalType approvalType = ApprovalType.Internal.valueOfCode(ticketMO.getApprovalType());
                ApprovalProviderSpi approvalService = PluginManager.findSpi(ApprovalProviderSpi.class, approvalType.name());
                if (approvalService != null) {
                    approvalService.useTemplate(ownerUid, ticketMO.getTemplateId(), null);
                }
            }
        }
        if (StringUtils.isNotBlank(afterValue)) {
            EnvTicketMO ticketMO = JsonUtils.toObj(afterValue, EnvTicketMO.class);
            if (StringUtils.isNotBlank(ticketMO.getApprovalType())) {
                ApprovalType approvalType = ApprovalType.Internal.valueOfCode(ticketMO.getApprovalType());
                ApprovalProviderSpi approvalService = PluginManager.findSpi(ApprovalProviderSpi.class, approvalType.name());
                if (approvalService != null) {
                    approvalService.useTemplate(ownerUid, null, ticketMO.getTemplateId());
                }
            }
        }
    }

    @Override
    public List<DmEnvParamOpenVO> listEnvParamOpen(String puid, String uid) {
        List<DmEnvParamOpenVO> result = new ArrayList<>();
        List<DmSysEnvParamDO> paramList = this.systemDal.envParamMapper().queryByUid(puid);
        List<DmSysEnvDO> envList = this.systemDal.envMapper().queryListByUid(puid);

        for (DmSysEnvDO envDO : envList) {
            DmEnvParamOpenVO vo = new DmEnvParamOpenVO();
            vo.setEnvId(envDO.getId());
            vo.setEnvName(envDO.getEnvName());
            vo.setEnvDesc(envDO.getDescription());
            vo.setSecDesVO(DmEnvParamSecDesVO.builder().build());
            vo.setSqlTicketInfo(innerTicket());
            vo.setChangeTicketInfo(innerTicket());
            vo.setAuthTicketInfo(innerTicket());
            vo.setAllowAllStatements(false);

            for (DmSysEnvParamDO paramDO : paramList) {
                if (envDO.getId() != paramDO.getEnvId()) {
                    continue;
                }

                if (paramDO.getConfigKey().equals(EnvParamKeys.DM_BIND_CHECK_SPEC)) {
                    processCheckSpec(puid, paramDO, vo);
                }

                if (paramDO.getConfigKey().equals(EnvParamKeys.SQL_TICKET_INFO)) {
                    vo.setSqlTicketInfo(processTicket(puid, paramDO.getConfigValue()));
                }

                if (paramDO.getConfigKey().equals(EnvParamKeys.CHANGE_TICKET_INFO)) {
                    vo.setChangeTicketInfo(processTicket(puid, paramDO.getConfigValue()));
                }

                if (paramDO.getConfigKey().equals(EnvParamKeys.AUTH_TICKET_INFO)) {
                    vo.setAuthTicketInfo(processTicket(puid, paramDO.getConfigValue()));
                }

                if (paramDO.getConfigKey().equals(EnvParamKeys.DM_ALLOW_ALL_STATEMENTS)) {
                    vo.setAllowAllStatements(StringUtils.equalsIgnoreCase("true", paramDO.getConfigValue()));
                }

            }
            result.add(vo);
        }

        return result;
    }

    private void processCheckSpec(String puid, DmSysEnvParamDO paramDO, DmEnvParamOpenVO vo) {
        long id = Long.parseLong(paramDO.getConfigValue());
        DmSecSpecDO specDO = this.secRuleDal.specMapper().queryByIdAndUid(puid, id);
        if (specDO != null) {
            vo.setSecDesVO(DmEnvParamSecDesVO.builder()//
                .openSec(true)
                .id(id)
                .name(specDO.getName())
                .desc(specDO.getDescription())
                .build());
        }
    }

    @Override
    public List<DmSysEnvDO> queryListByParamKeyValue(String puid, String paramKey, String paramValue) {
        List<DmSysEnvParamDO> params = this.systemDal.envParamMapper().queryByParamKeySet(puid, Collections.singletonList(paramKey));
        List<Long> collect = params.stream().filter(p -> {
            return StringUtils.equals(p.getConfigValue(), paramValue);
        }).map(DmSysEnvParamDO::getEnvId).collect(Collectors.toList());

        if (CollectionUtils.isEmpty(collect)) {
            return Collections.emptyList();
        }

        return this.systemDal.envMapper().queryListByUidAndId(puid, collect);
    }

    @Override
    public List<DmSysEnvDO> queryListByParamKey(String puid, String paramKey) {
        return this.systemDal.envMapper().queryListByParameterKey(puid, paramKey);
    }

    //
    //
    //

    @Override
    public String queryParam(String ownerUid, long envID, String paramKey) {
        DmSysEnvParamDO param = this.systemDal.envParamMapper().queryByParamKey(ownerUid, paramKey, envID);
        if (param == null) {
            return null;
        } else {
            return param.getConfigValue();
        }
    }

    @Override
    public DmEnvParamTicketDesVO queryAuthTicketInfoParam(String ownerUid, long envId) {
        return processTicket(ownerUid, this.queryParam(ownerUid, envId, EnvParamKeys.AUTH_TICKET_INFO));
    }

    @Override
    public DmEnvParamTicketDesVO querySqlTicketInfoParam(String ownerUid, long envId) {
        return processTicket(ownerUid, this.queryParam(ownerUid, envId, EnvParamKeys.SQL_TICKET_INFO));
    }

    @Override
    public DmEnvParamTicketDesVO queryChangeTicketInfoParam(String ownerUid, long envId) {
        return processTicket(ownerUid, this.queryParam(ownerUid, envId, EnvParamKeys.CHANGE_TICKET_INFO));
    }

    private static DmEnvParamTicketDesVO innerTicket() {
        return DmEnvParamTicketDesVO.builder()//
            .openTicket(true)
            .type(ApprovalType.Internal.name())
            .typeI18n(DmI18nUtils.getMessage(ApprovalType.Internal.getI18nKey()))
            .templateId(ApprovalFlowService.innerTemplate().getTemplateIdentity())
            .templateName(ApprovalFlowService.innerTemplate().getApproTemplateName())
            .build();
    }

    private DmEnvParamTicketDesVO processTicket(String ownerUid, String configValue) {
        if (StringUtils.isBlank(configValue)) {
            return innerTicket();
        }

        EnvTicketMO ticketMO = JsonUtils.toObj(configValue, EnvTicketMO.class);
        String providerCode = ticketMO.getApprovalType();
        if (StringUtils.isBlank(providerCode)) {
            return DmEnvParamTicketDesVO.builder().openTicket(false).type("").typeI18n("").templateName("").build();
        }

        DmEnvParamTicketDesVO tVO = DmEnvParamTicketDesVO.builder()//
            .openTicket(true)
            .type(providerCode)
            .typeI18n(DmI18nUtils.getMessage(ApprovalType.Internal.valueOfCode(providerCode).getI18nKey()))
            .templateId(ticketMO.getTemplateId())
            .templateName(ticketMO.getTemplateName())
            .build();

        DmApprovalTemplateDO templateDO = this.approvalDal.templateMapper().queryByUidAndTemId(ownerUid, ticketMO.getTemplateId());
        if (templateDO == null && !ApprovalType.Internal.name().equals(providerCode)) {
            tVO.setDelete(true);
        }
        return tVO;
    }
}
