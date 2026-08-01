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
package com.clougence.clouddm.console.web.component.autoexec.handler;

import java.util.Locale;

import org.springframework.stereotype.Service;

import com.clougence.clouddm.console.web.component.autoexec.AutoExecHelper;
import com.clougence.clouddm.console.web.component.cicd.ImMessageType;
import com.clougence.clouddm.console.web.component.cicd.ImSenderService;
import com.clougence.clouddm.console.web.global.i18n.DmI18nUtils;
import com.clougence.clouddm.console.web.global.i18n.I18nDmMsgKeys;
import com.clougence.clouddm.platform.dal.access.ChangeFlowDal;
import com.clougence.clouddm.platform.dal.access.ExecutionDal;
import com.clougence.clouddm.platform.dal.model.cicd.ChangeStatus;
import com.clougence.clouddm.platform.dal.model.cicd.ChangeStep;
import com.clougence.clouddm.platform.dal.model.cicd.DmChangeDO;
import com.clougence.clouddm.platform.dal.model.execution.DmExecAutoJobDO;
import com.clougence.clouddm.platform.dal.model.execution.SQLJobBizType;
import com.clougence.utils.i18n.I18nUtils;

import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class AutoExecHelperForChange implements AutoExecHelper {
    @Resource
    private ChangeFlowDal   changeFlowDal;
    @Resource
    private ExecutionDal    executionDal;
    @Resource
    private ImSenderService senderService;

    @Override
    public SQLJobBizType getHandleType() { return SQLJobBizType.CHANGE; }

    @Override
    public void execStart(SQLJobBizType bizType, String bizId) {

    }

    @Override
    public void execCompleted(SQLJobBizType bizType, String bizId) {
        DmExecAutoJobDO job = this.executionDal.autoJobMapper().queryByBizId(bizId);
        if (job.getDependOnBizType() != SQLJobBizType.CHANGE) {
            return;
        }

        DmChangeDO change = this.changeFlowDal.changeMapper().queryChangeById(Long.parseLong(job.getDependOnBizId()));

        // language
        String language = this.senderService.getFlowLanguage(change.getOwnerUid(), change.getRefFlowId());
        Locale locale = I18nUtils.getLocale(language);
        String msg = DmI18nUtils.getMessage(I18nDmMsgKeys.CICD_CHANGE_EXECUTE_FINISH.name(), locale, change.getChangeName());

        // finish
        int res1 = this.changeFlowDal.changeMapper().updateStepTo(change.getId(), change.getVersion(), ChangeStep.FINISH, "");
        int res2 = this.changeFlowDal.changeMapper().updateStatusTo(change.getId(), change.getVersion() + 1, ChangeStatus.READY, "");
        this.senderService.sendMessage(change.getOwnerUid(), change.getRefFlowId(), ImMessageType.ChangeLife, msg);
    }

    @Override
    public void execAbort(SQLJobBizType bizType, String bizId) {
        DmExecAutoJobDO job = this.executionDal.autoJobMapper().queryByBizId(bizId);
        if (job.getDependOnBizType() != SQLJobBizType.CHANGE) {
            return;
        }

        DmChangeDO change = this.changeFlowDal.changeMapper().queryChangeById(Long.parseLong(job.getDependOnBizId()));

        // language
        String language = this.senderService.getFlowLanguage(change.getOwnerUid(), change.getRefFlowId());
        Locale locale = I18nUtils.getLocale(language);
        String msg = DmI18nUtils.getMessage(I18nDmMsgKeys.CICD_CHANGE_EXECUTE_FAILED_BY_ABORT.name(), locale, change.getChangeName());

        // abort
        int res = this.changeFlowDal.changeMapper().updateStatusTo(change.getId(), change.getVersion(), ChangeStatus.FAILED, msg);
        this.senderService.sendMessage(change.getOwnerUid(), change.getRefFlowId(), ImMessageType.ChangeNotice, msg);
    }

    @Override
    public void execFailed(SQLJobBizType bizType, String bizId) {
        DmExecAutoJobDO job = this.executionDal.autoJobMapper().queryByBizId(bizId);
        if (job.getDependOnBizType() != SQLJobBizType.CHANGE) {
            return;
        }

        DmChangeDO change = this.changeFlowDal.changeMapper().queryChangeById(Long.parseLong(job.getDependOnBizId()));

        // language
        String language = this.senderService.getFlowLanguage(change.getOwnerUid(), change.getRefFlowId());
        Locale locale = I18nUtils.getLocale(language);
        String msg = DmI18nUtils.getMessage(I18nDmMsgKeys.CICD_CHANGE_EXECUTE_FAILED_BY_FAILED.name(), locale, change.getChangeName());

        // send message
        int res = this.changeFlowDal.changeMapper().updateStatusTo(change.getId(), change.getVersion(), ChangeStatus.FAILED, msg);
        this.senderService.sendMessage(change.getOwnerUid(), change.getRefFlowId(), ImMessageType.ChangeNotice, msg);
    }
}
