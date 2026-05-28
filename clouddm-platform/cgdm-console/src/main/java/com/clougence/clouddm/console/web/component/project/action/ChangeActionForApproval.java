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
package com.clougence.clouddm.console.web.component.project.action;

import java.util.*;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.clougence.clouddm.api.common.exception.ErrorMessageException;
import com.clougence.clouddm.console.web.component.approval.ApprovalFlowService;
import com.clougence.clouddm.console.web.component.approval.model.ApprovalMO;
import com.clougence.clouddm.console.web.component.dsconfig.DmDsConfigService;
import com.clougence.clouddm.console.web.component.dsconfig.mode.DsLevels;
import com.clougence.clouddm.console.web.component.project.ImMessageType;
import com.clougence.clouddm.console.web.component.project.model.ChangeApprovalInfo;
import com.clougence.clouddm.console.web.component.project.model.ChangeCheckItemMO;
import com.clougence.clouddm.console.web.component.project.model.ChangeCheckMO;
import com.clougence.clouddm.console.web.component.project.model.ChangeTicketInfo;
import com.clougence.clouddm.console.web.global.i18n.DmI18nUtils;
import com.clougence.clouddm.console.web.global.i18n.I18nDmMsgKeys;
import com.clougence.clouddm.console.web.global.i18n.I18nRdpMsgKeys;
import com.clougence.clouddm.console.web.model.vo.ticket.CheckedVO;
import com.clougence.clouddm.platform.dal.access.NamingDao;
import com.clougence.clouddm.platform.dal.access.ApprovalDal;
import com.clougence.clouddm.platform.dal.access.SystemDal;
import com.clougence.clouddm.platform.dal.model.approval.*;
import com.clougence.clouddm.platform.dal.model.datasource.DmDsDO;
import com.clougence.clouddm.platform.dal.model.project.*;
import com.clougence.clouddm.platform.dal.model.secrule.WarnLevel;
import com.clougence.clouddm.platform.dal.model.system.DmSysEnvDO;
import com.clougence.clouddm.platform.dal.model.system.DmSysEnvParamDO;
import com.clougence.clouddm.sdk.model.env.EnvParamKeys;
import com.clougence.rdp.service.model.EnvTicketMO;
import com.clougence.schema.umi.struts.UmiTypes;
import com.clougence.utils.JsonUtils;
import com.clougence.utils.format.WellKnowFormat;
import com.clougence.utils.i18n.I18nUtils;

import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class ChangeActionForApproval extends AbstractChangeAction {

    @Resource
    private SystemDal           systemDal;
    @Resource
    private ApprovalDal         approvalDal;
    @Resource
    private NamingDao       namingDao;
    @Resource
    private DmDsConfigService   dmDsConfigService;
    @Resource
    private ApprovalFlowService approvalFlowService;

    @Override
    public void doAction(DmProjectChangeDO change) {
        if (!super.doCommonAction(change)) {
            return;
        } else {
            change = projectDal.changeMapper().queryChangeById(change.getOwnerUid(), change.getId());
        }

        // message i18n
        String language = this.senderService.getProjectLanguage(change.getOwnerUid(), change.getRefProjectId());
        Locale locale = I18nUtils.getLocale(language);

        // test skip
        DmProjectDO project = projectDal.projectMapper().queryByOwnerAndId(change.getOwnerUid(), change.getRefProjectId());
        ChangeApproveStrategy approveOpt = project.getFlowApprove();
        if (approveOpt == ChangeApproveStrategy.Disable) {
            log.info("changeAction[" + change.getId() + "] skip approval.");
            int res = projectDal.changeMapper().updateStepTo(change.getId(), change.getVersion(), ProjectChangeStep.EXECUTE, "");
            projectDal.changeMapper().updateFlowWalkedAppend(change.getId(), change, approveOpt);
            return;
        } else {
            projectDal.changeMapper().updateFlowWalkedAppend(change.getId(), change, approveOpt);
        }

        // change sql
        List<DmProjectChangeItemDO> diffChange = this.projectDal.changeItemMapper().queryChangeItemByChangeId(change.getOwnerUid(), change.getId(), ChangeItemType.REVIEW);
        String sqlChange = diffChange.isEmpty() ? "" : diffChange.get(0).getContent();

        // create ticket
        DmApprovalDO ticket;
        try {
            DmProjectDevopsDO devopsDO = projectDal.devopsMapper().queryByOwnerAndId(change.getOwnerUid(), change.getRefDevopsId());
            DsLevels dsLevels = this.dmDsConfigService.parseLevels(devopsDO.getDsPath());
            ticket = createTicket(change, dsLevels, sqlChange, locale);
        } catch (Exception e) {
            String message = null;
            if (e instanceof ErrorMessageException) {
                message = ((ErrorMessageException) e).getErrorMessage();
            } else {
                message = e.getMessage();
            }

            message = DmI18nUtils.getMessage(I18nDmMsgKeys.PROJECT_CHANGE_CREATE_TICKET_FAILED_MESSAGE.name(), locale, change.getChangeName(), message);
            this.senderService.sendMessage(change.getOwnerUid(), change.getRefProjectId(), ImMessageType.ChangeNotice, message);
            projectDal.changeMapper().updateStatusTo(change.getId(), change.getVersion(), ProjectChangeStatus.FAILED, message);
            return;
        }

        // store step info
        DmProjectChangeItemDO itemDO = new DmProjectChangeItemDO();
        itemDO.setOwnerUid(change.getOwnerUid());
        itemDO.setRefProjectId(change.getRefProjectId());
        itemDO.setRefChangeId(change.getId());
        itemDO.setChangeItemType(ChangeItemType.TICKET);
        itemDO.setContent(JsonUtils.toJson(createChangeTicketInfo(ticket)));
        itemDO.setContentIndex(1);
        itemDO.setContentName(ticket.getTicketTitle());
        this.projectDal.changeItemMapper().deleteByChangeItemType(change.getOwnerUid(), change.getId(), ChangeItemType.TICKET);
        this.projectDal.changeItemMapper().insert(itemDO);

        // next wait.
        log.info("changeAction[" + change.getId() + "] create Ticket " + ticket.getId() + ".");
        String message = DmI18nUtils.getMessage(I18nDmMsgKeys.PROJECT_CHANGE_CREATE_TICKET_MESSAGE.name(), locale, change.getChangeName(), ticket.getTicketTitle());
        this.senderService.sendMessage(change.getOwnerUid(), change.getRefProjectId(), ImMessageType.ChangeNotice, message);
        projectDal.changeMapper().updateStatusTo(change.getId(), change.getVersion(), ProjectChangeStatus.WAIT, message);
    }

    private ChangeTicketInfo createChangeTicketInfo(DmApprovalDO ticket) {
        ChangeTicketInfo info = new ChangeTicketInfo();

        info.setTicketId(ticket.getId());
        info.setTicketBizId(ticket.getBizId());
        info.setTicketBizType(ticket.getApproBiz());
        info.setApprovalType(ticket.getApproType());
        info.setTemplateId(ticket.getApproTemplateIdentity());
        info.setTemplateName(ticket.getApproTemplateName());
        return info;
    }

    @Transactional(rollbackFor = Throwable.class)
    public DmApprovalDO createTicket(DmProjectChangeDO change, DsLevels dsLevels, String sqlContent, Locale locale) {
        DmProjectDO projectDO = projectDal.projectMapper().queryByOwnerAndId(change.getOwnerUid(), change.getRefProjectId());
        DmDsDO dsDO = dsLevels.dsDO();
        DmSysEnvDO envDO = this.systemDal.envMapper().queryByEnvID(change.getOwnerUid(), Long.valueOf(dsLevels.envId()));

        // targetInfo
        String targetInfo = "/" + dsLevels.dsDO().getInstanceId();
        Map<UmiTypes, Object> levelsParam = dsLevels.levelsParam();
        if (dsLevels.levelsDef().contains(UmiTypes.Catalog)) {
            targetInfo += String.format("/%s/%s", levelsParam.get(UmiTypes.Catalog), levelsParam.get(UmiTypes.Schema));
        } else {
            targetInfo += String.format("/%s", levelsParam.get(UmiTypes.Schema));
        }

        // find approvalType.
        ChangeApprovalInfo approvalInfo = findRdpApprovalType(change.getOwnerUid(), dsDO, locale);
        ApprovalType approvalType = approvalInfo.getApprovalType();
        if (approvalType != ApprovalType.Internal) {
            if (!this.approvalFlowService.checkEnableApproval(change.getOwnerUid(), approvalType.getProviderType())) {
                throw new ErrorMessageException(DmI18nUtils.getMessage(I18nRdpMsgKeys.TICKET_APPROVAL_TYPE_NOT_ENABLE.name(), locale, approvalType));
            }
        }

        //  ChangeCheckMO to CheckedVO
        Map<String, CheckedVO> checkMap = new LinkedHashMap<>();
        List<DmProjectChangeItemDO> checks = this.projectDal.changeItemMapper().queryChangeItemByChangeId(change.getOwnerUid(), change.getId(), ChangeItemType.CHECKS);
        for (DmProjectChangeItemDO item : checks) {
            ChangeCheckMO useType = JsonUtils.toObjUseType(item.getContent(), ChangeCheckMO.class);
            for (ChangeCheckItemMO checkItem : useType.getCheckList()) {
                String specName = checkItem.getSpecName();
                String ruleName = checkItem.getRuleName();
                WarnLevel level = checkItem.getLevel();

                CheckedVO vo;
                if (checkMap.containsKey(ruleName)) {
                    vo = checkMap.get(ruleName);
                } else {
                    vo = new CheckedVO();
                    vo.setName(ruleName);
                    vo.setDesc(specName);
                    vo.setRuleLevel(level.getRuleLevel());
                    vo.setLines(new ArrayList<>());
                    checkMap.put(ruleName, vo);
                }

                vo.getLines().add(useType.getStartCodeLine());
            }
        }

        // create Ticket
        String bizId = this.namingDao.genTicketBizId();
        DmApprovalDO ticket = new DmApprovalDO();
        ticket.setBizId(bizId);
        ticket.setOwnerUid(projectDO.getProjectUid());
        ticket.setPrimaryUid(change.getOwnerUid());
        ticket.setBindDsId(dsDO.getId());
        ticket.setTargetInfo(targetInfo);
        ticket.setDescription(generateTicketDescription(change, projectDO, locale));
        ticket.setTicketTitle(generateTicketTitle(change, projectDO, locale));
        ticket.setTicketStatus(ApprovalStatus.PRE_INIT);
        ticket.setApproBiz(ApprovalBiz.DM_CHANGE);
        ticket.setStatusMessage(DmI18nUtils.getMessage(I18nDmMsgKeys.TICKET_STATUS_WAIT_EXPLAIN.name(), locale));
        ticket.setApproType(approvalType);
        ticket.setEnvName(envDO.getEnvName());
        ticket.setApproTemplateName(approvalInfo.getTemplateName());
        ticket.setApproTemplateIdentity(approvalInfo.getTemplateId());

        ticket.setRawSql(sqlContent);
        ticket.setTotalCount(0);
        ticket.setExpectedAffectedRows(0L);
        ApprovalMO ticketInfo = new ApprovalMO();
        ticketInfo.setChangeOwnerUid(change.getOwnerUid());
        ticketInfo.setChangeId(change.getId());
        ticket.setTicketInfo(JsonUtils.toJson(ticketInfo));
        ticket.setLevels(dsLevels.dbLevels());
        ticket.setRollBackSql("");
        ticket.setCheckedInfo(JsonUtils.toJson(checkMap.values()));

        //
        if (approvalInfo.getApprovalType() == ApprovalType.Internal) {
            DmApprovalPersonDO primary = new DmApprovalPersonDO();
            primary.setPersonUid(projectDO.getProjectUid());
            primary.setTicketBzId(bizId);
            this.approvalDal.personMapper().insert(primary);
        }

        this.approvalDal.approvalMapper().insert(ticket);
        this.approvalFlowService.createProcess(ticket.getId(), ApprovalBiz.DM_CHANGE, true);
        return ticket;
    }

    private String generateTicketTitle(DmProjectChangeDO change, DmProjectDO projectDO, Locale locale) {
        return DmI18nUtils.getMessage(I18nDmMsgKeys.PROJECT_CHANGE_GENERATE_TICKET_TITLE_MESSAGE.name(), locale, //
                projectDO.getProjectName(), change.getChangeName());
    }

    private String generateTicketDescription(DmProjectChangeDO change, DmProjectDO projectDO, Locale locale) {
        return DmI18nUtils.getMessage(I18nDmMsgKeys.PROJECT_CHANGE_GENERATE_TICKET_DESC_MESSAGE.name(), locale, //
                projectDO.getProjectName(), change.getChangeName(), WellKnowFormat.WKF_DATE_TIME24.format(change.getChangeTime()));
    }

    private ChangeApprovalInfo findRdpApprovalType(String ownerUid, DmDsDO dsDO, Locale locale) {
        Long dsEnvId = dsDO.getDsEnvId();
        DmSysEnvParamDO paramDO = this.systemDal.envParamMapper().queryByParamKey(ownerUid, EnvParamKeys.CHANGE_TICKET_INFO, dsEnvId);
        if (paramDO == null) {
            return new ChangeApprovalInfo(//
                ApprovalType.Internal,
                ApprovalFlowService.INNER_TEMPLATE_ID,
                DmI18nUtils.getMessage(I18nRdpMsgKeys.TICKET_INTERNAL_TEMPLATE.name()));
        }

        EnvTicketMO ticketMO = JsonUtils.toObj(paramDO.getConfigValue(), EnvTicketMO.class);
        ApprovalType rdpApprovalType = ApprovalType.getByName(ticketMO.getApprovalType());
        String templateId = ticketMO.getTemplateId();
        String templateName = ticketMO.getTemplateName();
        if (rdpApprovalType != ApprovalType.Internal) {
            DmApprovalTemplateDO templateDO = this.approvalFlowService.checkApprovalAndReturnTemplate(ownerUid, rdpApprovalType, templateId, locale);
            templateName = templateDO.getTemplateName();
        }
        return new ChangeApprovalInfo(rdpApprovalType, templateId, templateName);
    }
}
