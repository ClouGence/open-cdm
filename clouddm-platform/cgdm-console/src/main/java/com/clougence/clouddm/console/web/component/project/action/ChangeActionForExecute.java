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

import java.util.Collections;
import java.util.List;
import java.util.Locale;

import org.springframework.stereotype.Service;

import com.clougence.clouddm.console.web.component.autoexec.AutoExecService;
import com.clougence.clouddm.console.web.component.dsconfig.DmDsConfigService;
import com.clougence.clouddm.console.web.component.dsconfig.mode.DsLevels;
import com.clougence.clouddm.console.web.component.project.ImMessageType;
import com.clougence.clouddm.console.web.component.project.model.ChangeExecuteInfo;
import com.clougence.clouddm.console.web.global.i18n.DmI18nUtils;
import com.clougence.clouddm.console.web.global.i18n.I18nDmMsgKeys;
import com.clougence.clouddm.console.web.model.fo.ticket.DmAutoExecConfigFO;
import com.clougence.clouddm.console.web.service.analysis.QueryAnalysisService;
import com.clougence.clouddm.platform.dal.model.execution.AutoExecType;
import com.clougence.clouddm.platform.dal.model.execution.SQLJobBizType;
import com.clougence.clouddm.platform.dal.model.project.*;
import com.clougence.clouddm.sdk.analysis.split.SplitScript;
import com.clougence.clouddm.sdk.security.auth.SecQueryType;
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
        DmProjectDevopsDO devopsDO = projectDal.devopsMapper().queryByOwnerAndId(change.getOwnerUid(), change.getRefDevopsId());
        projectDal.changeMapper().updateFlowWalkedAppend(change.getId(), change, project.getFlowExecute());
        switch (project.getFlowExecute()) {
            case Auto: {
                // to WAIT status ,waiting execute job finish.
                String changeMessageStr = DmI18nUtils.getMessage(I18nDmMsgKeys.PROJECT_CHANGE_EXECUTE_USE_AUTO_MESSAGE.name(), locale, change.getChangeName());
                int res1 = projectDal.changeMapper().updateStatusTo(change.getId(), change.getVersion(), ProjectChangeStatus.WAIT, changeMessageStr);
                this.senderService.sendMessage(change.getOwnerUid(), change.getRefProjectId(), ImMessageType.ChangeNotice, changeMessageStr);

                // start job
                RsProjectOptionObj options = project.getOptions();
                ChangeExecuteInfo config = new ChangeExecuteInfo();
                config.setExecType(AutoExecType.IMMEDIATE);
                config.setTransactional(options.isTransactional());
                config.setErrorStrategy(options.getErrorStrategy());
                config.setRetryWaitTime(options.getRetryWaitTime());
                config.setRetryCount(options.getRetryCount());
                config.setSnapshot(options.isSnapshot());
                doStartExecuteJob(locale, change, devopsDO, config);
                break;
            }
            case Manual: {
                List<DmProjectChangeItemDO> items = this.projectDal.changeItemMapper().queryChangeItemByChangeId(change.getOwnerUid(), change.getId(), ChangeItemType.EXECUTE);
                DmProjectChangeItemDO item = CollectionUtils.isEmpty(items) ? null : items.get(0);
                if (item == null || StringUtils.isEmpty(item.getContent())) {
                    String changeMessageStr = DmI18nUtils.getMessage(I18nDmMsgKeys.PROJECT_CHANGE_EXECUTE_WAIT_CONFIRM_MESSAGE.name(), locale, change.getChangeName());
                    int res1 = projectDal.changeMapper().updateStatusTo(change.getId(), change.getVersion(), ProjectChangeStatus.OPEN, changeMessageStr);
                    this.senderService.sendMessage(change.getOwnerUid(), change.getRefProjectId(), ImMessageType.ChangeNotice, changeMessageStr);
                } else {
                    ChangeExecuteInfo config = JsonUtils.toObj(item.getContent(), ChangeExecuteInfo.class);
                    if (config.getExecType() == AutoExecType.MANUAL_EXEC) {

                        String changeMessageStr = DmI18nUtils.getMessage(I18nDmMsgKeys.PROJECT_CHANGE_EXECUTE_SKIP_MESSAGE.name(), locale, change.getChangeName());
                        int res = projectDal.changeMapper().updateStepTo(change.getId(), change.getVersion(), ProjectChangeStep.FINISH, changeMessageStr);
                        this.senderService.sendMessage(change.getOwnerUid(), change.getRefProjectId(), ImMessageType.ChangeLife, changeMessageStr);

                    } else {

                        // to WAIT status ,waiting execute job finish.
                        String changeMessageStr = DmI18nUtils.getMessage(I18nDmMsgKeys.PROJECT_CHANGE_EXECUTE_USE_MANUAL_MESSAGE.name(), locale, change.getChangeName());
                        int res1 = projectDal.changeMapper().updateStatusTo(change.getId(), change.getVersion(), ProjectChangeStatus.WAIT, changeMessageStr);
                        this.senderService.sendMessage(change.getOwnerUid(), change.getRefProjectId(), ImMessageType.ChangeNotice, changeMessageStr);

                        // start job
                        doStartExecuteJob(locale, change, devopsDO, config);
                    }
                }
                break;
            }
            case Disabled: {
                int res = projectDal.changeMapper().updateStepTo(change.getId(), change.getVersion(), ProjectChangeStep.FINISH, "");
                break;
            }
        }
    }

    private void doStartExecuteJob(Locale locale, DmProjectChangeDO change, DmProjectDevopsDO devopsDO, ChangeExecuteInfo config) {
        // change sql
        List<DmProjectChangeItemDO> diffChange = this.projectDal.changeItemMapper().queryChangeItemByChangeId(change.getOwnerUid(), change.getId(), ChangeItemType.REVIEW);
        String changeSql = diffChange.isEmpty() ? "" : diffChange.get(0).getContent();

        DmAutoExecConfigFO fo = new DmAutoExecConfigFO();
        fo.setAutoExecType(config.getExecType());
        fo.setEnableTransactional(config.isTransactional());
        fo.setErrorStrategy(config.getErrorStrategy());
        fo.setRetryWaitTime(config.getRetryWaitTime());
        fo.setRetryCount(config.getRetryCount());
        fo.setExecTime(config.getExecTime());

        DmProjectDO projectDO = projectDal.projectMapper().queryByOwnerAndId(change.getOwnerUid(), change.getRefProjectId());
        DsLevels dsLevels = this.dmDsConfigService.parseLevels(devopsDO.getDsPath());

        List<SplitScript> scripts;
        try {
            scripts = this.queryAnalysisService.analysisSplit(dsLevels.dsDO().getDataSourceType(), changeSql, Collections.emptyList(), 1, 0);
        } catch (Exception e) {
            log.warn("can not parse sql");
            SplitScript splitScript = new SplitScript();
            splitScript.setScript(changeSql);
            splitScript.setType(SecQueryType.UNKNOWN);
            scripts = Collections.singletonList(splitScript);
        }

        try {
            this.autoExecService.createJob(projectDO.getOwnerUid(), projectDO.getProjectUid(), fo, dsLevels, SQLJobBizType.CHANGE, String.valueOf(change.getId()), scripts);
        } catch (Exception e) {
            change = projectDal.changeMapper().queryChangeById(change.getOwnerUid(), change.getId());
            String changeMessageStr = DmI18nUtils.getMessage(I18nDmMsgKeys.PROJECT_CHANGE_EXECUTE_JOB_ERROR.name(), locale, change.getChangeName(), e.getMessage());
            int res = projectDal.changeMapper().updateStatusTo(change.getId(), change.getVersion(), ProjectChangeStatus.FAILED, changeMessageStr);
            this.senderService.sendMessage(change.getOwnerUid(), change.getRefProjectId(), ImMessageType.ChangeNotice, changeMessageStr);
        }
    }
}
