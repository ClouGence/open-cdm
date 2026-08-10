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
package com.clougence.clouddm.console.web.component.cicd.action;

import java.nio.file.Path;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.clougence.clouddm.api.common.exception.ErrorMessageException;
import com.clougence.clouddm.console.web.component.approval.ApprovalFlowService;
import com.clougence.clouddm.console.web.component.approval.model.ApprovalMO;
import com.clougence.clouddm.console.web.component.cicd.ChangeSqlService;
import com.clougence.clouddm.console.web.component.cicd.ImMessageType;
import com.clougence.clouddm.console.web.component.cicd.model.ChangeApprovalInfo;
import com.clougence.clouddm.console.web.component.cicd.model.ChangeTicketInfo;
import com.clougence.clouddm.console.web.component.dsconfig.DmDsConfigService;
import com.clougence.clouddm.console.web.component.dsconfig.mode.DsLevels;
import com.clougence.clouddm.console.web.component.file.LocalFileService;
import com.clougence.clouddm.console.web.global.i18n.DmI18nUtils;
import com.clougence.clouddm.console.web.global.i18n.I18nDmMsgKeys;
import com.clougence.clouddm.console.web.global.i18n.I18nRdpMsgKeys;
import com.clougence.clouddm.platform.dal.access.ApprovalDal;
import com.clougence.clouddm.platform.dal.access.NamingDao;
import com.clougence.clouddm.platform.dal.access.SystemDal;
import com.clougence.clouddm.platform.dal.model.approval.*;
import com.clougence.clouddm.platform.dal.model.cicd.*;
import com.clougence.clouddm.platform.dal.model.datasource.DmDsDO;
import com.clougence.clouddm.platform.dal.model.system.DmSysEnvDO;
import com.clougence.clouddm.platform.dal.model.system.DmSysEnvParamDO;
import com.clougence.clouddm.platform.dal.model.system.SysAttachmentType;
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
    private NamingDao           namingDao;
    @Resource
    private DmDsConfigService   dmDsConfigService;
    @Resource
    private ApprovalFlowService approvalFlowService;
    @Resource
    private ChangeSqlService    changeSqlService;
    @Resource
    private LocalFileService    localFileService;

    @Override
    public void doAction(DmChangeDO change) {
        if (!super.doCommonAction(change)) {
            return;
        } else {
            change = changeFlowDal.changeMapper().queryChangeById(change.getId());
        }

        // message i18n
        String language = this.senderService.getFlowLanguage(change.getOwnerUid(), change.getRefFlowId());
        Locale locale = I18nUtils.getLocale(language);

        // create ticket
        DmApprovalDO ticket;
        try {
            DmChangeFlowDO gitOpsFlowDO = changeFlowDal.flowMapper().queryByOwnerAndId(change.getOwnerUid(), change.getRefFlowId());
            DsLevels dsLevels = this.dmDsConfigService.parseLevels(gitOpsFlowDO.getDsPath());
            DmChangeDO currentChange = change;
            ticket = this.changeSqlService.consumeSqlFile(change.getId(), sqlFile -> {
                return this.createApproval(currentChange, dsLevels, sqlFile, locale);
            });
        } catch (Exception e) {
            String message = null;
            if (e instanceof ErrorMessageException) {
                message = ((ErrorMessageException) e).getErrorMessage();
            } else {
                message = e.getMessage();
            }

            message = DmI18nUtils.getMessage(I18nDmMsgKeys.CICD_CHANGE_CREATE_TICKET_FAILED_MESSAGE.name(), locale, change.getChangeName(), message);
            this.senderService.sendMessage(change.getOwnerUid(), change.getRefFlowId(), ImMessageType.ChangeNotice, message);
            changeFlowDal.changeMapper().updateStatusTo(change.getId(), change.getVersion(), ChangeStatus.FAILED, message);
            return;
        }

        // store step info
        DmChangeItemDO itemDO = new DmChangeItemDO();
        itemDO.setOwnerUid(change.getOwnerUid());
        itemDO.setRefFlowId(change.getRefFlowId());
        itemDO.setRefChangeId(change.getId());
        itemDO.setChangeItemType(ChangeItemType.TICKET);
        itemDO.setContent(JsonUtils.toJson(createChangeTicketInfo(ticket)));
        itemDO.setContentIndex(1);
        itemDO.setContentName(ticket.getTicketTitle());
        this.changeFlowDal.changeItemMapper().deleteByChangeItemType(change.getOwnerUid(), change.getId(), ChangeItemType.TICKET);
        this.changeFlowDal.changeItemMapper().insert(itemDO);

        // next wait.
        log.info("changeAction[" + change.getId() + "] create Ticket " + ticket.getId() + ".");
        String message = DmI18nUtils.getMessage(I18nDmMsgKeys.CICD_CHANGE_CREATE_TICKET_MESSAGE.name(), locale, change.getChangeName(), ticket.getTicketTitle());
        this.senderService.sendMessage(change.getOwnerUid(), change.getRefFlowId(), ImMessageType.ChangeNotice, message);
        changeFlowDal.changeMapper().updateStatusTo(change.getId(), change.getVersion(), ChangeStatus.WAIT, message);
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

    @Transactional(rollbackFor = Throwable.class, propagation = Propagation.REQUIRED)
    public DmApprovalDO createApproval(DmChangeDO change, DsLevels dsLevels, Path sqlFile, Locale locale) {
        DmChangeFlowDO flowDO = changeFlowDal.flowMapper().queryByOwnerAndId(change.getOwnerUid(), change.getRefFlowId());
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

        // create Ticket
        String bizId = this.namingDao.genApprovalBizId();
        DmApprovalDO ticket = new DmApprovalDO();
        ticket.setBizId(bizId);
        String applicantUid = change.getTriggerUid();
        if (applicantUid == null) {
            applicantUid = flowDO.getFlowManagerUid();
        }
        ticket.setOwnerUid(applicantUid);
        ticket.setPrimaryUid(change.getOwnerUid());
        ticket.setBindDsId(dsDO.getId());
        ticket.setTargetInfo(targetInfo);
        ticket.setDescription(generateTicketDescription(change, flowDO, locale));
        ticket.setTicketTitle(generateTicketTitle(change, flowDO, locale));
        ticket.setTicketStatus(ApprovalStatus.PRE_INIT_WAIT);
        ticket.setApproBiz(ApprovalBiz.DM_CHANGE);
        ticket.setStatusMessage(DmI18nUtils.getMessage(I18nDmMsgKeys.TICKET_STATUS_WAIT_EXPLAIN.name(), locale));
        ticket.setApproType(approvalType);
        ticket.setEnvName(envDO.getEnvName());
        ticket.setApproTemplateName(approvalInfo.getTemplateName());
        ticket.setApproTemplateIdentity(approvalInfo.getTemplateId());
        ticket.setRawSql(null);
        ticket.setContentType(SqlContentType.ATTACHMENT);
        ticket.setFeatures(List.of(ApprovalFeature.values()));
        ticket.setExpectedAffectedRows(0L);
        ApprovalMO ticketInfo = new ApprovalMO();
        ticketInfo.setChangeOwnerUid(change.getOwnerUid());
        ticketInfo.setChangeId(change.getId());
        ticket.setTicketInfo(JsonUtils.toJson(ticketInfo));
        ticket.setLevels(dsLevels.dbLevels());
        ticket.setRollBackSql("");

        //
        if (approvalInfo.getApprovalType() == ApprovalType.Internal) {
            DmApprovalPersonDO primary = new DmApprovalPersonDO();
            primary.setPersonUid(flowDO.getFlowManagerUid());
            primary.setTicketBzId(bizId);
            this.approvalDal.personMapper().insert(primary);
        }

        this.approvalDal.approvalMapper().insert(ticket);
        this.localFileService.addAsLocked(change.getOwnerUid(), sqlFile, "cicd-" + change.getId() + ".sql", SysAttachmentType.SQL_FILE, ticket.getId());
        this.approvalFlowService.createProcess(ticket.getId(), ApprovalBiz.DM_CHANGE, true);
        return ticket;
    }

    private String generateTicketTitle(DmChangeDO change, DmChangeFlowDO flowDO, Locale locale) {
        return DmI18nUtils.getMessage(I18nDmMsgKeys.CICD_CHANGE_GENERATE_TICKET_TITLE_MESSAGE.name(), locale, //
                flowDO.getFlowName(), change.getChangeName());
    }

    private String generateTicketDescription(DmChangeDO change, DmChangeFlowDO flowDO, Locale locale) {
        return DmI18nUtils.getMessage(I18nDmMsgKeys.CICD_CHANGE_GENERATE_TICKET_DESC_MESSAGE.name(), locale, //
                flowDO.getFlowName(), change.getChangeName(), WellKnowFormat.WKF_DATE_TIME24.format(change.getChangeTime()));
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
