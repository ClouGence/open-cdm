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

import java.util.Collections;
import java.util.List;
import java.util.Locale;

import org.springframework.stereotype.Service;

import com.clougence.clouddm.base.metadata.ds.DataSourceConfig;
import com.clougence.clouddm.console.web.component.autoexec.AutoExecService;
import com.clougence.clouddm.console.web.component.cicd.ImMessageType;
import com.clougence.clouddm.console.web.component.cicd.model.ChangeExecuteInfo;
import com.clougence.clouddm.console.web.component.dsconfig.DmDsConfigService;
import com.clougence.clouddm.console.web.component.dsconfig.mode.DsLevels;
import com.clougence.clouddm.console.web.global.i18n.DmI18nUtils;
import com.clougence.clouddm.console.web.global.i18n.I18nDmMsgKeys;
import com.clougence.clouddm.console.web.model.fo.ticket.DmAutoExecConfigFO;
import com.clougence.clouddm.console.web.service.analysis.QueryAnalysisService;
import com.clougence.clouddm.platform.dal.model.cicd.*;
import com.clougence.clouddm.platform.dal.model.execution.AutoExecType;
import com.clougence.clouddm.platform.dal.model.execution.SQLJobBizType;
import com.clougence.clouddm.sdk.security.auth.SecQueryType;
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

    @Override
    public void doAction(DmChangeDO change) {
        if (!super.doCommonAction(change)) {
            return;
        } else {
            change = changeFlowDal.changeMapper().queryChangeById(change.getOwnerUid(), change.getId());
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
        // change sql
        List<DmChangeItemDO> diffChange = this.changeFlowDal.changeItemMapper().queryChangeItemByChangeId(change.getOwnerUid(), change.getId(), ChangeItemType.REVIEW);
        String changeSql = diffChange.isEmpty() ? "" : diffChange.get(0).getContent();

        DmAutoExecConfigFO fo = new DmAutoExecConfigFO();
        fo.setAutoExecType(config.getExecType());
        fo.setEnableTransactional(config.isTransactional());
        fo.setErrorStrategy(config.getErrorStrategy());
        fo.setRetryWaitTime(config.getRetryWaitTime());
        fo.setRetryCount(config.getRetryCount());
        fo.setExecTime(config.getExecTime());

        DmChangeFlowDO flowDO = changeFlowDal.flowMapper().queryByOwnerAndId(change.getOwnerUid(), change.getRefFlowId());
        DsLevels dsLevels = this.dmDsConfigService.parseLevels(gitOpsFlowDO.getDsPath());

        List<SplitScript> scripts;
        try {
            DataSourceConfig dsConfig = this.dmDsConfigService.fetchDsConfigFromExists(dsLevels.dsDO().getId());
            scripts = this.queryAnalysisService.analysisSplit(dsConfig, changeSql, Collections.emptyList(), 1, 0);
        } catch (Exception e) {
            log.warn("can not parse sql");
            SplitScript splitScript = new SplitScript();
            splitScript.setScript(changeSql);
            splitScript.setType(Collections.singleton(SecQueryType.UNKNOWN));
            scripts = Collections.singletonList(splitScript);
        }

        try {
            this.autoExecService.createJob(flowDO.getOwnerUid(), flowDO.getFlowManagerUid(), fo, dsLevels, SQLJobBizType.CHANGE, String.valueOf(change.getId()), scripts);
        } catch (Exception e) {
            change = changeFlowDal.changeMapper().queryChangeById(change.getOwnerUid(), change.getId());
            String changeMessageStr = DmI18nUtils.getMessage(I18nDmMsgKeys.CICD_CHANGE_EXECUTE_JOB_ERROR.name(), locale, change.getChangeName(), e.getMessage());
            int res = changeFlowDal.changeMapper().updateStatusTo(change.getId(), change.getVersion(), ChangeStatus.FAILED, changeMessageStr);
            this.senderService.sendMessage(change.getOwnerUid(), change.getRefFlowId(), ImMessageType.ChangeNotice, changeMessageStr);
        }
    }
}
