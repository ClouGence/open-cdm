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

import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.stream.Stream;

import org.springframework.stereotype.Service;

import com.clougence.clouddm.base.metadata.ds.DataSourceConfig;
import com.clougence.clouddm.console.web.component.analysis.QueryAnalysisService;
import com.clougence.clouddm.console.web.component.autoexec.AutoExecService;
import com.clougence.clouddm.console.web.component.autoexec.model.AutoExecJobCreateRequest;
import com.clougence.clouddm.console.web.component.cicd.ChangeSqlService;
import com.clougence.clouddm.console.web.component.cicd.ImMessageType;
import com.clougence.clouddm.console.web.component.cicd.model.ChangeExecuteInfo;
import com.clougence.clouddm.console.web.component.dsconfig.DmDsConfigService;
import com.clougence.clouddm.console.web.component.dsconfig.mode.DsLevels;
import com.clougence.clouddm.console.web.global.i18n.DmI18nUtils;
import com.clougence.clouddm.console.web.global.i18n.I18nDmMsgKeys;
import com.clougence.clouddm.platform.dal.access.AuthDal;
import com.clougence.clouddm.platform.dal.model.cicd.*;
import com.clougence.clouddm.platform.dal.model.execution.AutoExecType;
import com.clougence.clouddm.platform.dal.model.execution.SQLJobBizType;
import com.clougence.clouddm.sdk.sql.parser.SplitScript;
import com.clougence.utils.CollectionUtils;
import com.clougence.utils.JsonUtils;
import com.clougence.utils.StringUtils;
import com.clougence.utils.i18n.I18nUtils;

import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class ChangeActionForExecute extends AbstractChangeAction {

    @Resource
    private AutoExecService      autoExecService;
    @Resource
    private DmDsConfigService    dmDsConfigService;
    @Resource
    private QueryAnalysisService queryAnalysisService;
    @Resource
    private ChangeSqlService      changeSqlService;

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

        // test skip
        DmChangeFlowDO flow = changeFlowDal.flowMapper().queryByOwnerAndId(change.getOwnerUid(), change.getRefFlowId());
        DmChangeFlowDO gitOpsFlowDO = changeFlowDal.flowMapper().queryByOwnerAndId(change.getOwnerUid(), change.getRefFlowId());
        changeFlowDal.changeMapper().updateFlowWalkedAppend(change.getId(), change, flow.getFlowExecute());
        switch (flow.getFlowExecute()) {
            case Auto: {
                // to WAIT status ,waiting execute job finish.
                String changeMessageStr = DmI18nUtils.getMessage(I18nDmMsgKeys.CICD_CHANGE_EXECUTE_USE_AUTO_MESSAGE.name(), locale, change.getChangeName());
                int res1 = changeFlowDal.changeMapper().updateStatusTo(change.getId(), change.getVersion(), ChangeStatus.WAIT, changeMessageStr);
                this.senderService.sendMessage(change.getOwnerUid(), change.getRefFlowId(), ImMessageType.ChangeNotice, changeMessageStr);

                // start job
                RsChangeFlowOptionObj options = flow.getOptions();
                ChangeExecuteInfo config = new ChangeExecuteInfo();
                config.setExecType(AutoExecType.IMMEDIATE);
                config.setTransactional(options.isTransactional());
                config.setErrorStrategy(options.getErrorStrategy());
                config.setRetryWaitTime(options.getRetryWaitTime());
                config.setRetryCount(options.getRetryCount());
                config.setSnapshot(options.isSnapshot());
                doStartExecuteJob(locale, change, gitOpsFlowDO, config);
                break;
            }
            case Manual: {
                List<DmChangeItemDO> items = this.changeFlowDal.changeItemMapper().queryChangeItemByChangeId(change.getOwnerUid(), change.getId(), ChangeItemType.EXECUTE);
                DmChangeItemDO item = CollectionUtils.isEmpty(items) ? null : items.get(0);
                if (item == null || StringUtils.isEmpty(item.getContent())) {
                    String changeMessageStr = DmI18nUtils.getMessage(I18nDmMsgKeys.CICD_CHANGE_EXECUTE_WAIT_CONFIRM_MESSAGE.name(), locale, change.getChangeName());
                    int res1 = changeFlowDal.changeMapper().updateStatusTo(change.getId(), change.getVersion(), ChangeStatus.OPEN, changeMessageStr);
                    this.senderService.sendMessage(change.getOwnerUid(), change.getRefFlowId(), ImMessageType.ChangeNotice, changeMessageStr);
                } else {
                    ChangeExecuteInfo config = JsonUtils.toObj(item.getContent(), ChangeExecuteInfo.class);
                    if (config.getExecType() == AutoExecType.MANUAL_EXEC) {

                        String changeMessageStr = DmI18nUtils.getMessage(I18nDmMsgKeys.CICD_CHANGE_EXECUTE_SKIP_MESSAGE.name(), locale, change.getChangeName());
                        int res = changeFlowDal.changeMapper().updateStepTo(change.getId(), change.getVersion(), ChangeStep.FINISH, changeMessageStr);
                        this.senderService.sendMessage(change.getOwnerUid(), change.getRefFlowId(), ImMessageType.ChangeLife, changeMessageStr);

                    } else {

                        // to WAIT status ,waiting execute job finish.
                        String changeMessageStr = DmI18nUtils.getMessage(I18nDmMsgKeys.CICD_CHANGE_EXECUTE_USE_MANUAL_MESSAGE.name(), locale, change.getChangeName());
                        int res1 = changeFlowDal.changeMapper().updateStatusTo(change.getId(), change.getVersion(), ChangeStatus.WAIT, changeMessageStr);
                        this.senderService.sendMessage(change.getOwnerUid(), change.getRefFlowId(), ImMessageType.ChangeNotice, changeMessageStr);

                        // start job
                        doStartExecuteJob(locale, change, gitOpsFlowDO, config);
                    }
                }
                break;
            }
            case Disabled: {
                int res = changeFlowDal.changeMapper().updateStepTo(change.getId(), change.getVersion(), ChangeStep.FINISH, "");
                break;
            }
        }
    }

    private void doStartExecuteJob(Locale locale, DmChangeDO change, DmChangeFlowDO gitOpsFlowDO, ChangeExecuteInfo config) {
        DsLevels dsLevels = this.dmDsConfigService.parseLevels(gitOpsFlowDO.getDsPath());
        AutoExecJobCreateRequest request = AutoExecJobCreateRequest.builder()//
            .dsLevels(dsLevels)
            .bizType(SQLJobBizType.CHANGE)
            .bizId(String.valueOf(change.getId()))
            .execType(config.getExecType())
            .transactional(config.isTransactional())
            .errorStrategy(config.getErrorStrategy())
            .retryWaitTime(config.getRetryWaitTime())
            .retryCount(config.getRetryCount())
            .execTime(config.getExecTime())
            .build();

        DataSourceConfig dsConfig = this.dmDsConfigService.fetchDsConfigFromExists(dsLevels.dsDO().getId());

        try {
            long jobId = this.changeSqlService.consumeSqlFile(change.getId(), sqlFile -> {
                try (Reader reader = Files.newBufferedReader(sqlFile, StandardCharsets.UTF_8);
                     Stream<SplitScript> scripts = this.queryAnalysisService.analysisSplitStream(dsConfig, reader, Collections.emptyList(), 1, 0)) {
                    return this.autoExecService.createJob(request, scripts);
                }
            });
            String operatorUid = config.getOperatorUid();
            if (StringUtils.isBlank(operatorUid)) {
                operatorUid = AuthDal.ROOT_USER_UID;
            }
            this.autoExecService.startJob(jobId, operatorUid);
        } catch (Exception e) {
            change = changeFlowDal.changeMapper().queryChangeById(change.getId());
            String changeMessageStr = DmI18nUtils.getMessage(I18nDmMsgKeys.CICD_CHANGE_EXECUTE_JOB_ERROR.name(), locale, change.getChangeName(), e.getMessage());
            int res = changeFlowDal.changeMapper().updateStatusTo(change.getId(), change.getVersion(), ChangeStatus.FAILED, changeMessageStr);
            this.senderService.sendMessage(change.getOwnerUid(), change.getRefFlowId(), ImMessageType.ChangeNotice, changeMessageStr);
        }
    }
}
