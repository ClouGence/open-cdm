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

import java.util.Locale;

import org.springframework.stereotype.Service;

import com.clougence.clouddm.console.web.component.cicd.ImMessageType;
import com.clougence.clouddm.console.web.component.cicd.ImSenderService;
import com.clougence.clouddm.console.web.global.i18n.DmI18nUtils;
import com.clougence.clouddm.console.web.global.i18n.I18nDmMsgKeys;
import com.clougence.clouddm.console.web.service.cicd.DmChangeFlowService;
import com.clougence.clouddm.platform.dal.access.ChangeFlowDal;
import com.clougence.clouddm.platform.dal.model.cicd.*;
import com.clougence.clouddm.platform.dal.model.gitops.DmGitOpsScmDO;
import com.clougence.utils.i18n.I18nUtils;

import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public abstract class AbstractChangeAction implements ChangeAction {
    @Resource
    protected ChangeFlowDal       changeFlowDal;
    @Resource
    protected DmChangeFlowService changeFlowService;
    @Resource
    protected ImSenderService     senderService;

    protected boolean doCommonAction(DmChangeDO change) {
        int newVersion = change.getVersion() + 1;
        int assignAgain = changeFlowDal.changeMapper().assignReadyChange(change.getId(), newVersion);
        if (assignAgain == 0) {
            log.info("change " + change.getId() + " assigned failed, maybe has already processed.");
            return false;
        } else {
            newVersion++;
        }

        String language = this.senderService.getFlowLanguage(change.getOwnerUid(), change.getRefFlowId());
        Locale locale = I18nUtils.getLocale(language);

        DmChangeFlowDO flowDO = changeFlowDal.flowMapper().queryByOwnerAndId(change.getOwnerUid(), change.getRefFlowId());
        if (flowDO == null) {
            String errorMsg = DmI18nUtils.getMessage(I18nDmMsgKeys.CICD_CHANGE_FLOW_NOT_EXIST_ERROR.name(), locale, change.getChangeName());
            this.senderService.sendMessage(change.getOwnerUid(), change.getRefFlowId(), ImMessageType.ChangeNotice, errorMsg);
            int res = changeFlowDal.changeMapper().updateStatusTo(change.getId(), newVersion, ChangeStatus.FAILED, errorMsg);
            return false;
        }
        if (flowDO.getChangeFlowStatus() != ChangeFlowStatus.NORMAL) {
            String errorMsg = DmI18nUtils.getMessage(I18nDmMsgKeys.CICD_CHANGE_FLOW_IS_ARCHIVE_OR_DELETE_ERROR.name(), locale, change.getChangeName());
            this.senderService.sendMessage(change.getOwnerUid(), change.getRefFlowId(), ImMessageType.ChangeNotice, errorMsg);
            int res = changeFlowDal.changeMapper().updateStatusTo(change.getId(), newVersion, ChangeStatus.FAILED, errorMsg);
            return false;
        }

        DmChangeFlowDO gitOpsFlowDO = changeFlowDal.flowMapper().queryByOwnerAndId(change.getOwnerUid(), change.getRefFlowId());
        if (gitOpsFlowDO == null || gitOpsFlowDO.isDeleted()) {
            String errorMsg = DmI18nUtils.getMessage(I18nDmMsgKeys.CICD_CHANGE_DEVOPS_NOT_EXIST_ERROR.name(), locale, change.getChangeName());
            this.senderService.sendMessage(change.getOwnerUid(), change.getRefFlowId(), ImMessageType.ChangeNotice, errorMsg);
            int res = changeFlowDal.changeMapper().updateStatusTo(change.getId(), newVersion, ChangeStatus.FAILED, errorMsg);
            return false;
        }
        if (!gitOpsFlowDO.isEnable()) {
            String errorMsg = DmI18nUtils.getMessage(I18nDmMsgKeys.CICD_CHANGE_DEVOPS_IS_DISABLED_ERROR.name(), locale, change.getChangeName());
            this.senderService.sendMessage(change.getOwnerUid(), change.getRefFlowId(), ImMessageType.ChangeNotice, errorMsg);
            int res = changeFlowDal.changeMapper().updateStatusTo(change.getId(), newVersion, ChangeStatus.FAILED, errorMsg);
            return false;
        }

        ChangeFlowType flowType = gitOpsFlowDO.getFlowType() == null ? ChangeFlowType.SCM : gitOpsFlowDO.getFlowType();
        if (flowType == ChangeFlowType.SCM) {
            DmGitOpsScmDO scmDO = changeFlowDal.scmMapper().queryByOwnerAndId(change.getOwnerUid(), gitOpsFlowDO.getRefScmId());
            if (scmDO == null) {
                String errorMsg = DmI18nUtils.getMessage(I18nDmMsgKeys.CICD_CHANGE_SCM_NOT_EXIST_ERROR.name(), locale, change.getChangeName());
                this.senderService.sendMessage(change.getOwnerUid(), change.getRefFlowId(), ImMessageType.ChangeNotice, errorMsg);
                int res = changeFlowDal.changeMapper().updateStatusTo(change.getId(), newVersion, ChangeStatus.FAILED, errorMsg);
                return false;
            }
        }

        return true;
    }
}
