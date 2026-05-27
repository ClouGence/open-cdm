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
package com.clougence.rdp.service.impl;

import com.clougence.clouddm.platform.dal.access.ApprovalDal;
import com.clougence.clouddm.platform.dal.access.AuthDal;
import com.clougence.clouddm.platform.dal.access.DataSourceDal;
import com.clougence.clouddm.platform.dal.access.SystemDal;

import java.util.*;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.clougence.clouddm.console.web.component.approval.ApprovalFlowService;
import com.clougence.clouddm.platform.dal.model.approval.ApprovalBiz;
import com.clougence.clouddm.platform.dal.model.approval.ApprovalType;
import com.clougence.clouddm.platform.dal.model.approval.ApprovalStatus;
import com.clougence.clouddm.platform.dal.model.datasource.*;
import com.clougence.clouddm.platform.dal.model.system.*;
import com.clougence.clouddm.platform.dal.model.approval.*;
import com.clougence.clouddm.platform.dal.model.auth.*;
import com.clougence.clouddm.console.web.model.fo.ticket.ApplyAuth;
import com.clougence.clouddm.console.web.model.fo.ticket.RdpAddAuthTicketFO;
import com.clougence.clouddm.console.web.model.vo.ticket.RdpAuthTicketDetailVO;
import com.clougence.clouddm.console.web.global.i18n.DmI18nUtils;
import com.clougence.clouddm.console.web.util.RandomStrUtils;
import com.clougence.clouddm.sdk.model.env.EnvParamKeys;
import com.clougence.clouddm.sdk.security.auth.AuthInfo;
import com.clougence.clouddm.sdk.security.auth.AuthKind;
import com.clougence.clouddm.console.web.global.i18n.I18nRdpLabelKeys;
import com.clougence.clouddm.console.web.global.i18n.I18nRdpMsgKeys;
import com.clougence.clouddm.api.common.exception.ErrorMessageException;
import com.clougence.rdp.service.RdpAuthServiceForManage;
import com.clougence.rdp.service.RdpAuthTicketService;
import com.clougence.rdp.service.RdpUserService;
import com.clougence.rdp.service.model.EnvTicketMO;
import com.clougence.utils.CollectionUtils;
import com.clougence.utils.JsonUtils;
import com.clougence.utils.StringUtils;
import com.fasterxml.jackson.core.type.TypeReference;

import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class RdpAuthTicketServiceImpl implements RdpAuthTicketService {
    @Resource
    private SystemDal systemDal;

    @Resource
    private DataSourceDal datasourceDal;

    @Resource
    private AuthDal authDal;

    @Resource
    private ApprovalDal approvalDal;

    @Resource
    private ApprovalFlowService     approvalFlowService;
    @Resource
    private RdpUserService          userService;
    @Resource
    private RdpAuthServiceForManage authServiceForManage;

    @Override
    @Transactional(rollbackFor = Throwable.class, propagation = Propagation.REQUIRED)
    public void createAuthTicket(String ownerUid, String uid, RdpAddAuthTicketFO fo) {
        // fetch auth ds objects and group by envId
        List<Long> dsIds1 = fo.getApplyAuths().stream().map(ApplyAuth::getResId).sorted().collect(Collectors.toList());
        if (CollectionUtils.isEmpty(dsIds1)) {
            throw new ErrorMessageException(DmI18nUtils.getMessage(I18nRdpMsgKeys.TICKET_AUTH_TICKET_IS_EMPTY_MESSAGE.name()));
        }

        List<DmDsDO> list = this.datasourceDal.dsMapper().listByIds(dsIds1);
        Map<Long, List<Long>> groupByEnv = CollectionUtils.groupBy(list, DmDsDO::getDsEnvId, DmDsDO::getId);

        // split request by envId to multiple RdpAddAuthTicketFO
        for (Long envId : groupByEnv.keySet()) {
            RdpAddAuthTicketFO tfo = new RdpAddAuthTicketFO();
            tfo.setAuthKind(fo.getAuthKind());
            tfo.setApplyAuths(fo.getApplyAuths().stream().filter(a -> {
                return groupByEnv.get(envId).contains(a.getResId());
            }).collect(Collectors.toList()));

            this.createAuthTicketItem(ownerUid, uid, tfo, envId);
        }
    }

    private void createAuthTicketItem(String ownerUid, String uid, RdpAddAuthTicketFO fo, long envId) {
        DmAuthUserDO user = this.userService.getUserByUid(uid);
        String bizId = this.genTicketBizId();
        DmApprovalDO ticket = new DmApprovalDO();
        ticket.setBizId(bizId);
        ticket.setOwnerUid(uid);
        ticket.setPrimaryUid(ownerUid);
        ticket.setTargetInfo(DmI18nUtils.getMessage(I18nRdpLabelKeys.AUTH_TICKET_TARGET.name()));
        ticket.setDescription(DmI18nUtils.getMessage(I18nRdpMsgKeys.TICKET_TITLE_AUTH.name(), user.getUsername()));
        ticket.setTicketTitle(DmI18nUtils.getMessage(I18nRdpMsgKeys.TICKET_TITLE_AUTH.name(), user.getUsername()));
        ticket.setTicketStatus(ApprovalStatus.WAIT_APPROVAL);
        ticket.setApproBiz(ApprovalBiz.DATA_SOURCE_AUTH);

        // applyAppro
        DmSysEnvParamDO paramDO = this.systemDal.envParamMapper().queryByParamKey(ownerUid, EnvParamKeys.AUTH_TICKET_INFO, envId);
        if (paramDO != null) {
            EnvTicketMO ticketMO = JsonUtils.toObj(paramDO.getConfigValue(), EnvTicketMO.class);
            ticket.setApproType(ApprovalType.getByName(ticketMO.getApprovalType()));
            ticket.setApproTemplateIdentity(ticketMO.getTemplateId());
            ticket.setApproTemplateName(ticketMO.getTemplateName());

            if (ticket.getApproType() != ApprovalType.Internal) {
                DmApprovalTemplateDO templateDO = this.approvalFlowService.checkApprovalAndReturnTemplate(ownerUid, ticket.getApproType(), ticketMO.getTemplateId(), null);
                ticket.setApproTemplateName(templateDO.getTemplateName());
            }
        } else {
            ticket.setApproType(ApprovalType.Internal);
            ticket.setApproTemplateIdentity(ApprovalFlowService.INNER_TEMPLATE_ID);
            ticket.setApproTemplateName(DmI18nUtils.getMessage(I18nRdpMsgKeys.TICKET_INTERNAL_TEMPLATE.name()));
        }

        // apply more info.
        this.fillAuthInfo(fo.getApplyAuths());

        DmAuthApprovalDO authTicket = new DmAuthApprovalDO();
        authTicket.setRdpTicketInsId(bizId);
        authTicket.setApplyAuthInfo(JsonUtils.toJson(fo));
        authTicket.setKindType(fo.getAuthKind());
        DmApprovalPersonDO primary = new DmApprovalPersonDO();
        primary.setPersonUid(ownerUid);
        primary.setTicketBzId(bizId);

        this.approvalDal.personMapper().insert(primary);
        this.approvalDal.approvalMapper().insert(ticket);
        this.authDal.approvalMapper().insert(authTicket);
        this.approvalFlowService.createProcess(ticket.getId(), ApprovalBiz.DATA_SOURCE_AUTH, true);
    }

    @Override
    public RdpAuthTicketDetailVO queryAuthTicketDetail(String ownerUid, String uid, long ticketId) {
        DmApprovalDO ticketDO = this.approvalDal.approvalMapper().queryById(ticketId);
        DmAuthApprovalDO authTicketInfo = this.authDal.approvalMapper().getAuthTicketInfo(ticketDO.getBizId());
        RdpAddAuthTicketFO fo = JsonUtils.toList(authTicketInfo.getApplyAuthInfo(), new TypeReference<RdpAddAuthTicketFO>() {});

        RdpAuthTicketDetailVO vo = new RdpAuthTicketDetailVO();
        vo.setApplyAuths(fo.getApplyAuths().stream().map(this::labelI18).collect(Collectors.toList()));
        vo.setAuthKind(fo.getAuthKind());
        return vo;
    }

    private ApplyAuth labelI18(ApplyAuth applyAuth) {
        List<AuthInfo> allAuthLabel = authServiceForManage.getAllAuthLabel(AuthKind.DataSource);
        Map<String, String> collect = allAuthLabel.stream().collect(Collectors.toMap(AuthInfo::getKey, AuthInfo::getKeyI18n));
        List<String> labels = new ArrayList<>();
        for (String authLabel : applyAuth.getAuthLabels()) {
            labels.add(DmI18nUtils.getMessage(collect.get(authLabel)));
        }

        applyAuth.setAuthLabels(labels);
        return applyAuth;
    }

    private List<ApplyAuth> fillAuthInfo(List<ApplyAuth> applyAuths) {
        Set<Long> dsIds = applyAuths.stream().map(ApplyAuth::getResId).collect(Collectors.toSet());
        if (dsIds.isEmpty()) {
            throw new ErrorMessageException(DmI18nUtils.getMessage(I18nRdpMsgKeys.TICKET_AUTH_TICKET_IS_EMPTY_MESSAGE.name()));
        }

        Map<Long, String> resInstIdMap = new HashMap<>();
        Map<Long, String> resDescMap = new HashMap<>();
        List<DmDsDO> dss = datasourceDal.dsMapper().listByIds(new ArrayList<>(dsIds));
        for (DmDsDO ds : dss) {
            resInstIdMap.put(ds.getId(), ds.getInstanceId());

            if (StringUtils.isBlank(ds.getInstanceDesc())) {
                resDescMap.put(ds.getId(), ds.getInstanceId());
            } else {
                resDescMap.put(ds.getId(), ds.getInstanceDesc());
            }
        }

        for (ApplyAuth applyAuth : applyAuths) {
            applyAuth.setResInstId(resInstIdMap.get(applyAuth.getResId()));
            applyAuth.setResDesc(resDescMap.get(applyAuth.getResId()));
        }

        return applyAuths;
    }

    public String genTicketBizId() {
        String namePattern = "ticket%s";
        while (true) {
            String bizId = String.format(namePattern, RandomStrUtils.fixedLenRandomStr(10));
            DmApprovalDO ticketDO = approvalDal.approvalMapper().queryByBizId(bizId);
            if (ticketDO == null) {
                return bizId;
            }
        }
    }

}
