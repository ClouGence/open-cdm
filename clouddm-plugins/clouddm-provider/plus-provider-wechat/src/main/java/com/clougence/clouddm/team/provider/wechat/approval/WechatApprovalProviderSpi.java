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
package com.clougence.clouddm.team.provider.wechat.approval;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;

import com.clougence.clouddm.sdk.LifeSpiRequest;
import com.clougence.clouddm.sdk.LifeSpiResponse;
import com.clougence.clouddm.sdk.LifeSpiStatus;
import com.clougence.clouddm.sdk.LoggerUtil;
import com.clougence.clouddm.sdk.approval.*;
import com.clougence.clouddm.sdk.approval.form.AuthForm;
import com.clougence.clouddm.sdk.approval.form.ChangeForm;
import com.clougence.clouddm.sdk.approval.form.QueryForm;
import com.clougence.clouddm.sdk.model.exception.ThirdPartyApiErrorType;
import com.clougence.clouddm.sdk.model.exception.ThirdPartyApiException;
import com.clougence.clouddm.sdk.service.approval.ApprovalActivity;
import com.clougence.clouddm.sdk.service.approval.ApprovalActivityStatus;
import com.clougence.clouddm.sdk.service.config.ConfigData;
import com.clougence.clouddm.sdk.service.config.ConsoleConfigService;
import com.clougence.clouddm.team.provider.wechat.client.WechatApi;
import com.clougence.clouddm.team.provider.wechat.client.WechatClient;
import com.clougence.clouddm.team.provider.wechat.constants.WechatConfigKey;
import com.clougence.clouddm.team.provider.wechat.constants.WechatI18nKey2;
import com.clougence.clouddm.team.provider.wechat.constants.approval.WechatConstant;
import com.clougence.utils.JsonUtils;
import com.clougence.utils.StringUtils;
import com.clougence.utils.io.IOUtils;

import cn.felord.domain.approval.*;
import cn.felord.enumeration.ProcessNodeStatus;

public class WechatApprovalProviderSpi implements ApprovalProviderSpi {

    private final static Logger             logger    = LoggerUtil.getLoggerAppender();
    private final ConsoleConfigService      configService;
    private final Map<String, WechatClient> clientMap = new ConcurrentHashMap<>();

    public WechatApprovalProviderSpi(ConsoleConfigService configService){
        this.configService = configService;
    }

    @Override
    public LifeSpiResponse start(String ownerUid, LifeSpiRequest requestDTO) {
        // fetch config
        List<ConfigData> configList = configService.fetchSettings(ownerUid, Arrays.asList(//
                WechatConfigKey.ApprovalEnable.getConfigKey(),//
                WechatConfigKey.ApprovalCorpId.getConfigKey(),//
                WechatConfigKey.ApprovalSecret.getConfigKey(),//
                WechatConfigKey.ApprovalAgentId.getConfigKey(),//
                WechatConfigKey.ApprovalToken.getConfigKey(),//
                WechatConfigKey.ApprovalEncodingAesKey.getConfigKey()));
        Map<String, String> configMap = new HashMap<>();
        for (ConfigData c : configList) {
            configMap.put(c.getConfigName(), c.getConfigValue());
        }

        // enable is false.
        String enableCfg = configMap.getOrDefault(WechatConfigKey.ApprovalEnable.getConfigKey(), "false");
        if (!StringUtils.equalsIgnoreCase(enableCfg, "true")) {
            logger.info("[WechatApprovalProviderSpi] IgnoreApproval[Wechat] primaryUid：" + ownerUid + ", enable is false.");
            return new LifeSpiResponse();
        }

        // start and init.
        synchronized (this) {
            if (this.clientMap.containsKey(ownerUid)) {
                logger.info("[WechatApprovalProviderSpi] IgnoreApproval[Wechat] primaryUid：" + ownerUid + ", already started.");
                return new LifeSpiResponse();
            }

            String corpId = configMap.get(WechatConfigKey.ApprovalCorpId.getConfigKey());
            String agentId = configMap.get(WechatConfigKey.ApprovalAgentId.getConfigKey());
            String secret = configMap.get(WechatConfigKey.ApprovalSecret.getConfigKey());
            String token = configMap.get(WechatConfigKey.ApprovalToken.getConfigKey());
            String encodingAesKey = configMap.get(WechatConfigKey.ApprovalEncodingAesKey.getConfigKey());

            // start api client
            WechatClient dingClient = new WechatClient(logger, corpId, agentId, secret, token, encodingAesKey);
            this.clientMap.put(ownerUid, dingClient);

            // finish
            logger.info("[WechatApprovalProviderSpi] StartApproval[Wechat] primaryUid：" + ownerUid);
            return new LifeSpiResponse();
        }
    }

    @Override
    public LifeSpiResponse stop(String ownerUid, LifeSpiRequest requestDTO) {
        // remove api client
        IOUtils.closeQuietly(this.clientMap.remove(ownerUid));

        logger.info("[WechatApprovalProviderSpi] StopApproval[Wechat] ownerUid：" + ownerUid);
        return new LifeSpiResponse();
    }

    @Override
    public LifeSpiResponse status(String ownerUid, LifeSpiRequest requestDTO) {
        LifeSpiStatus dto = new LifeSpiStatus();
        dto.setRunning(this.clientMap.containsKey(ownerUid));
        dto.setNameKey(WechatI18nKey2.WECHAT_APPROVAL_SERVICES_NAME);
        return new LifeSpiResponse(JsonUtils.toJson(dto));
    }

    protected WechatApi wechatApi(String primaryUid) {
        if (this.clientMap.containsKey(primaryUid)) {
            return this.clientMap.get(primaryUid).getBindApi();
        } else {
            throw new UnsupportedOperationException("wechatApprovalService is was closed");
        }
    }

    //

    @Override
    public ApprovalCreateInstanceResult createApprovalInstance(String ownerUid, ApprovalForm form) throws ThirdPartyApiException {
        WechatApi approvalApi = this.wechatApi(ownerUid);
        ApprovalTmpDetailResponse template = approvalApi.getTemplate(form.getTemplateIdentity());
        List<TmpControl<? extends ControlConfig>> controls = template.getTemplateContent().getControls();
        List<ContentDataValue> dataValues = getContentDataValues(form);
        List<Summary> summaryList = new ArrayList<>();
        Summary summary1 = Summary.summary(ApprovalTitle.zhCN(safeLength(WechatConstant.TICKET_TITLE + form.getTicketTitle(), 200, true)));
        Summary summary2 = Summary.summary(ApprovalTitle.zhCN(safeLength(WechatConstant.TICKET_DESCRIPTION + form.getTicketDesc(), 2000, false)));
        summaryList.add(summary1);
        summaryList.add(summary2);

        String userIdByPhone = approvalApi.getUserIdByPhone(form.getTicketUserPhone());
        String instance = approvalApi.createInstance(form.getTemplateIdentity(), userIdByPhone, dataValues, controls, summaryList);
        ApprovalCreateInstanceResult result = new ApprovalCreateInstanceResult();
        result.setApprovalIdentity(instance);

        ApprovalActivityInfo aaObj = new ApprovalActivityInfo();
        aaObj.setOrder(1);
        aaObj.setActivityName(WechatConstant.DEFAULT_ACTIVITY_TITLE);
        aaObj.setActivityId(WechatConstant.DEFAULT_ACTIVITY_ID);
        result.setActivityList(new ArrayList<>());
        result.setActivityList(Collections.singletonList(aaObj));
        return result;
    }

    private static List<ContentDataValue> getContentDataValues(ApprovalForm form) {
        if (form instanceof QueryForm) {
            QueryForm queryForm = (QueryForm) form;
            return Arrays.asList(//
                    TextValue.from(safeLength(queryForm.getTicketTitle(), 400, true)),   //
                    TextValue.from(safeLength(queryForm.getTargetDs(), 400, true)),      //
                    TextValue.from(safeLength(queryForm.getTicketDesc(), 4000, false)),  //
                    TextValue.from(safeLength(queryForm.getExecuteSql(), 4000, false)),  //
                    TextValue.from(safeLength(queryForm.getRollBackSql(), 4000, false)), //
                    queryForm.getAffectCount() == null ? TextValue.nullValue() : TextValue.from(queryForm.getAffectCount().toString())//
            );
        } else if (form instanceof AuthForm) {
            AuthForm authForm = (AuthForm) form;
            ListContentDataValue tableDataValue = ListContentDataValue.of();
            for (AuthForm.ApplyAuth applyAuth : authForm.getApplyAuths()) {
                List<ContentDataValue> objects = new ArrayList<>();
                objects.add(TextValue.from(safeLength(applyAuth.getResInstId() + "(" + applyAuth.getResDesc() + ")", 400, true)));
                objects.add(TextValue.from(safeLength("/" + String.join("/", applyAuth.getResPaths()), 400, true)));
                objects.add(TextValue.from(safeLength(authTimeRange(applyAuth), 400, true)));
                objects.add(TextValue.from(safeLength(String.join(",\n", applyAuth.getAuthLabels()), 4000, false)));
                tableDataValue.append(objects);
            }
            return Arrays.asList(//
                    TextValue.from(safeLength(form.getTicketTitle(), 400, true)),  //
                    TextValue.from(safeLength(form.getTicketDesc(), 4000, false)),   //
                    tableDataValue                                                  //
            );
        } else if (form instanceof ChangeForm) {
            ChangeForm changeForm = (ChangeForm) form;
            return Arrays.asList(//
                    TextValue.from(safeLength(changeForm.getTicketTitle(), 400, true)),//
                    TextValue.from(safeLength(changeForm.getTicketDesc(), 4000, false)), //
                    TextValue.from(safeLength(changeForm.getTargetDs(), 400, true)),   //
                    TextValue.from(safeLength(changeForm.getProjectName(), 400, true)),//
                    TextValue.from(safeLength(changeForm.getChangeName(), 400, true)), //
                    TextValue.from(safeLength(changeForm.getBranch(), 400, true)),     //
                    TextValue.from(safeLength(changeForm.getExecuteSql(), 4000, false))  //
            );
        } else {
            String message = String.format("Unsupported approval form type %s", form.getClass().getName());
            throw ThirdPartyApiException.asRDP().with(ThirdPartyApiErrorType.OTHER, WechatI18nKey2.WECHAT_CALL_API_UNKNOWN_ERROR, message);
        }
    }

    private static String safeLength(String str, int length, boolean inline) {
        if (StringUtils.isBlank(str)) {
            return "";
        } else if (str.length() > length) {
            if (inline) {
                return str.substring(0, length) + " ...";
            } else {
                return str.substring(0, length) + "\n\n......";
            }
        } else {
            return str;
        }
    }

    private static String authTimeRange(AuthForm.ApplyAuth applyAuth) {
        String time;
        if (applyAuth.getStartTime() == null && applyAuth.getEndTime() == null) {
            time = ("永久");
        } else if (applyAuth.getStartTime() != null && applyAuth.getEndTime() != null) {
            time = applyAuth.getStartTime() + " - " + applyAuth.getEndTime();
        } else if (applyAuth.getStartTime() != null) {
            time = ("从 " + applyAuth.getStartTime() + " 开始 至 永久");
        } else {
            time = ("从审批通过到 " + applyAuth.getEndTime() + " 结束");
        }
        return time;
    }

    @Override
    public ApprovalInstanceInfo getLastInfo(String ownerUid, String identity) throws ThirdPartyApiException {
        if (StringUtils.isBlank(identity)) {
            throw ThirdPartyApiException.asRDP().with(WechatI18nKey2.WECHAT_APPROVAL_INSTANCE_IS_EMPTY);
        }

        WechatApi approvalApi = this.wechatApi(ownerUid);

        ApprovalDetail instanceDetail = approvalApi.getInstanceDetail(identity);

        ApprovalInstanceInfo info = new ApprovalInstanceInfo();
        switch (instanceDetail.getSpStatus()) {
            case APPROVAL: {
                info.setStatus(ApprovalInstanceStatus.RUNNING);
                break;
            }
            case ACCEPTED: {
                info.setStatus(ApprovalInstanceStatus.COMPLETED);
                break;
            }
            case REJECTED: {
                info.setStatus(ApprovalInstanceStatus.REFUSE);
                break;
            }
            case REVOKED: {
                info.setStatus(ApprovalInstanceStatus.CANCELED);
                break;
            }
            default: {
                info.setStatus(ApprovalInstanceStatus.FAILED);
            }
        }

        Map<String, List<ApprovalActivity>> map = new HashMap<>();
        List<ApprovalActivity> tasks = new ArrayList<>();
        info.setMap(map);
        for (ProcessNodeDetail item : instanceDetail.getProcessList().getNodeList()) {
            ProcessNodeStatus spStatus = item.getSpStatus();
            ApprovalActivity task = new ApprovalActivity();
            if (spStatus == ProcessNodeStatus.ACCEPTED) {
                task.setStatus(ApprovalActivityStatus.COMPLETED);
            } else if (spStatus == ProcessNodeStatus.REJECTED) {
                task.setStatus(ApprovalActivityStatus.REFUSE);
            } else {
                continue;
            }

            ProcessSubNode subNode = item.getSubNodeList().get(0);
            task.setFinishTime(new Date(subNode.getSptime().toEpochMilli()));
            task.setUserId(subNode.getUserid());
            task.setRemark(subNode.getSpeech());
            task.setActivityId(WechatConstant.DEFAULT_ACTIVITY_ID);
            String name = approvalApi.getUserNameById(subNode.getUserid());
            task.setUserName(name);
            task.setTaskId(WechatConstant.DEFAULT_TASK_ID);
            tasks.add(task);
        }
        if (!tasks.isEmpty()) {
            map.put(WechatConstant.DEFAULT_ACTIVITY_ID, tasks);
        }

        return info;
    }

    @Override
    public List<ApprovalTemplate> getTemplates(String ownerUid) throws ThirdPartyApiException {
        WechatApi approvalApi = this.wechatApi(ownerUid);
        String templateListConfigKey = WechatConfigKey.ApprovalTemplateList.getConfigKey();
        String templateLangConfigKey = WechatConfigKey.ApprovalTemplateLang.getConfigKey();

        List<ConfigData> configList = this.configService.fetchSettings(ownerUid, Arrays.asList(//
                templateListConfigKey,//
                templateLangConfigKey //
        ));

        Map<String, ConfigData> cfgMap = new HashMap<>();
        for (ConfigData item : configList) {
            cfgMap.put(item.getConfigName(), item);
        }

        if (!cfgMap.containsKey(templateListConfigKey)) {
            return Collections.emptyList();
        } else {
            String templateList = cfgMap.get(templateListConfigKey).getConfigValue();
            String templateLang = cfgMap.containsKey(templateLangConfigKey) ? cfgMap.get(templateLangConfigKey).getConfigValue() : null;
            return approvalApi.getTemplates(templateList, templateLang);
        }
    }

    @Override
    public void useTemplate(String ownerUid, String before, String current) throws ThirdPartyApiException {
        // do nothing.
    }

    // no use
    @Override
    public ApprovalUserInfo getUserDetailByUid(String ownerUid, String userId) throws ThirdPartyApiException {
        return null;
    }

    @Override
    public void cancelApprovalInst(String ownerUid, ApprovalInstanceCancelInfo info) throws ThirdPartyApiException {
        throw ThirdPartyApiException.asRDP().with(WechatI18nKey2.WECHAT_NOT_SUPPORT_CLOSE_TICKET);
    }
}
