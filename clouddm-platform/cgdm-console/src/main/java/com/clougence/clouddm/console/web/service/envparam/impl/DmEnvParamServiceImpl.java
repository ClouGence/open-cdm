package com.clougence.clouddm.console.web.service.envparam.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import com.clougence.clouddm.sdk.approval.ApprovalProviderSpi;
import org.springframework.stereotype.Service;

import com.clougence.clouddm.console.web.dal.mapper.DmSecSpecMapper;
import com.clougence.clouddm.console.web.dal.model.DmSecSpecDO;
import com.clougence.clouddm.console.web.model.fo.envparam.DmBindEnvParamFO;
import com.clougence.clouddm.console.web.model.fo.envparam.DmUnbindEnvParamFO;
import com.clougence.clouddm.console.web.model.vo.envparam.DmEnvParamOpenVO;
import com.clougence.clouddm.console.web.model.vo.envparam.DmEnvParamSecDesVO;
import com.clougence.clouddm.console.web.model.vo.envparam.DmEnvParamTicketDesVO;
import com.clougence.clouddm.console.web.service.envparam.DmEnvParamService;
import com.clougence.clouddm.platform.plugin.PluginManager;
import com.clougence.clouddm.sdk.model.env.EnvParamKeys;
import com.clougence.rdp.component.ticket.RdpApprovalService;
import com.clougence.rdp.dal.enumeration.RdpApprovalType;
import com.clougence.rdp.dal.mapper.RdpCacheApproTemplateMapper;
import com.clougence.rdp.dal.mapper.RdpDsEnvMapper;
import com.clougence.rdp.dal.mapper.RdpEnvParamMapper;
import com.clougence.rdp.dal.model.RdpCacheApproTemplateDO;
import com.clougence.rdp.dal.model.RdpDsEnvDO;
import com.clougence.rdp.dal.model.RdpEnvParamDO;
import com.clougence.rdp.service.model.EnvTicketMO;
import com.clougence.rdp.util.RdpI18nUtils;
import com.clougence.utils.CollectionUtils;
import com.clougence.utils.JsonUtils;
import com.clougence.utils.StringUtils;

import lombok.extern.slf4j.Slf4j;

/**
 * @Author: Ekko
 * @Date: 2024-05-31 10:05
 */
@Service
@Slf4j
public class DmEnvParamServiceImpl implements DmEnvParamService {

    @Resource
    private RdpEnvParamMapper           rdpEnvParam;
    @Resource
    private DmSecSpecMapper             dmSecSpecMapper;
    @Resource
    private RdpCacheApproTemplateMapper rdpCacheApproTemplateMapper;
    @Resource
    private RdpDsEnvMapper              rdpDsEnvMapper;

    @Override
    public void bindEnvParam(String ownerUid, String uid, DmBindEnvParamFO fo) {
        long envId = fo.getEnvId();
        String paramKey = fo.getParamKey();
        String paramValue = fo.getParamValue();

        RdpEnvParamDO rdpEnvParamDO = this.rdpEnvParam.queryByParamKey(ownerUid, paramKey, envId);
        if (rdpEnvParamDO != null && StringUtils.isNotBlank(rdpEnvParamDO.getConfigValue())) {
            processTicketEnvParam(ownerUid, envId, paramKey, rdpEnvParamDO.getConfigValue(), paramValue);

            // update
            rdpEnvParamDO.setConfigValue(paramValue);
            this.rdpEnvParam.updateById(rdpEnvParamDO);
        } else {
            processTicketEnvParam(ownerUid, envId, paramKey, null, paramValue);

            // insert
            rdpEnvParamDO = new RdpEnvParamDO();
            rdpEnvParamDO.setEnvId(envId);
            rdpEnvParamDO.setConfigKey(paramKey);
            rdpEnvParamDO.setConfigValue(paramValue);
            rdpEnvParamDO.setPrimaryUid(ownerUid);
            this.rdpEnvParam.insert(rdpEnvParamDO);
        }
    }

    @Override
    public void unbindEnvParam(String ownerUid, String uid, DmUnbindEnvParamFO fo) {
        long envId = fo.getEnvId();
        String paramKey = fo.getParamKey();
        String paramValue = null;
        RdpEnvParamDO rdpEnvParamDO = this.rdpEnvParam.queryByParamKey(ownerUid, paramKey, envId);
        if (rdpEnvParamDO != null && StringUtils.isNotBlank(rdpEnvParamDO.getConfigValue())) {
            paramValue = rdpEnvParamDO.getConfigValue();
        }

        processTicketEnvParam(ownerUid, fo.getEnvId(), fo.getParamKey(), paramValue, null);
        this.rdpEnvParam.deleteEnvParam(fo.getParamKey(), ownerUid, fo.getEnvId());
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
                RdpApprovalType approvalType = RdpApprovalType.Internal.valueOfCode(ticketMO.getApprovalType());
                ApprovalProviderSpi approvalService = PluginManager.findSpi(ApprovalProviderSpi.class, approvalType.name());
                if (approvalService != null) {
                    approvalService.useTemplate(ownerUid, ticketMO.getTemplateId(), null);
                }
            }
        }
        if (StringUtils.isNotBlank(afterValue)) {
            EnvTicketMO ticketMO = JsonUtils.toObj(afterValue, EnvTicketMO.class);
            if (StringUtils.isNotBlank(ticketMO.getApprovalType())) {
                RdpApprovalType approvalType = RdpApprovalType.Internal.valueOfCode(ticketMO.getApprovalType());
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
        List<RdpEnvParamDO> paramList = this.rdpEnvParam.queryByUid(puid);
        List<RdpDsEnvDO> envList = this.rdpDsEnvMapper.queryListByUid(puid);

        for (RdpDsEnvDO envDO : envList) {
            DmEnvParamOpenVO vo = new DmEnvParamOpenVO();
            vo.setEnvId(envDO.getId());
            vo.setEnvName(envDO.getEnvName());
            vo.setEnvDesc(envDO.getDescription());
            vo.setSecDesVO(DmEnvParamSecDesVO.builder().build());
            vo.setSqlTicketInfo(innerTicket());
            vo.setChangeTicketInfo(innerTicket());
            vo.setAuthTicketInfo(innerTicket());
            vo.setAllowAllStatements(false);

            for (RdpEnvParamDO paramDO : paramList) {
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

    private void processCheckSpec(String puid, RdpEnvParamDO paramDO, DmEnvParamOpenVO vo) {
        long id = Long.parseLong(paramDO.getConfigValue());
        DmSecSpecDO specDO = this.dmSecSpecMapper.queryByIdAndUid(puid, id);
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
    public List<RdpDsEnvDO> queryListByParamKeyValue(String puid, String paramKey, String paramValue) {
        List<RdpEnvParamDO> params = this.rdpEnvParam.queryByParamKeySet(puid, Collections.singletonList(paramKey));
        List<Long> collect = params.stream().filter(p -> {
            return StringUtils.equals(p.getConfigValue(), paramValue);
        }).map(RdpEnvParamDO::getEnvId).collect(Collectors.toList());

        if (CollectionUtils.isEmpty(collect)) {
            return Collections.emptyList();
        }

        return this.rdpDsEnvMapper.queryListByUidAndId(puid, collect);
    }

    @Override
    public List<RdpDsEnvDO> queryListByParamKey(String puid, String paramKey) {
        return this.rdpDsEnvMapper.queryListByParameterKey(puid, paramKey);
    }

    //
    //
    //

    @Override
    public String queryParam(String ownerUid, long envID, String paramKey) {
        RdpEnvParamDO param = this.rdpEnvParam.queryByParamKey(ownerUid, paramKey, envID);
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
            .type(RdpApprovalType.Internal.name())
            .typeI18n(RdpI18nUtils.getMessage(RdpApprovalType.Internal.getI18nKey()))
            .templateId(RdpApprovalService.innerTemplate().getTemplateIdentity())
            .templateName(RdpApprovalService.innerTemplate().getApproTemplateName())
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
            .typeI18n(RdpI18nUtils.getMessage(RdpApprovalType.Internal.valueOfCode(providerCode).getI18nKey()))
            .templateId(ticketMO.getTemplateId())
            .templateName(ticketMO.getTemplateName())
            .build();

        RdpCacheApproTemplateDO templateDO = this.rdpCacheApproTemplateMapper.queryByUidAndTemId(ownerUid, ticketMO.getTemplateId());
        if (templateDO == null && !RdpApprovalType.Internal.name().equals(providerCode)) {
            tVO.setDelete(true);
        }
        return tVO;
    }
}
