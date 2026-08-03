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
import org.springframework.transaction.annotation.Transactional;

import com.clougence.clouddm.api.common.exception.ErrorMessageException;
import com.clougence.clouddm.console.web.component.cicd.ImMessageType;
import com.clougence.clouddm.console.web.component.cicd.model.ChangeExecuteInfo;
import com.clougence.clouddm.console.web.global.i18n.DmI18nUtils;
import com.clougence.clouddm.console.web.global.i18n.I18nDmMsgKeys;
import com.clougence.clouddm.console.web.util.CallUtils;
import com.clougence.clouddm.platform.dal.model.cicd.*;
import com.clougence.utils.CollectionUtils;
import com.clougence.utils.JsonUtils;
import com.clougence.utils.StringUtils;
import com.clougence.utils.i18n.I18nUtils;

import lombok.extern.slf4j.Slf4j;
import okhttp3.Response;

@Slf4j
@Service
public class ChangeActionForFinish extends AbstractChangeAction {

    @Transactional(rollbackFor = Throwable.class)
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

        // store to devops version
        this.storeToDevOps(locale, change);
        this.storeToSnapshot(locale, change);

        changeFlowDal.changeMapper().updateStatusTo(change.getId(), change.getVersion(), ChangeStatus.FINISH, "");
        changeFlowDal.changeMapper().lockChangeById(change.getId(), change.getVersion() + 1);

        // callback
        DmChangeFlowDO gitOpsFlowDO = changeFlowDal.flowMapper().queryByOwnerAndId(change.getOwnerUid(), change.getRefFlowId());
        if (gitOpsFlowDO.isEnableCallback()) {
            this.doCallBack(locale, change, gitOpsFlowDO);
        }
    }

    private void storeToDevOps(Locale locale, DmChangeDO change) {
        this.changeFlowDal.flowItemMapper().deleteItemByFlowId(change.getOwnerUid(), change.getRefFlowId());
        this.changeFlowDal.flowItemMapper().insertFromChangeItems(change.getOwnerUid(), change.getId(), change.getRefFlowId());

        String messageStr = DmI18nUtils.getMessage(I18nDmMsgKeys.CICD_CHANGE_UPDATE_SQL_BASE_LINE_MESSAGE.name(), locale, change.getChangeName());
        this.senderService.sendMessage(change.getOwnerUid(), change.getRefFlowId(), ImMessageType.ChangeNotice, messageStr);
    }

    private void storeToSnapshot(Locale locale, DmChangeDO change) {
        List<DmChangeItemDO> items = this.changeFlowDal.changeItemMapper().queryChangeItemByChangeId(change.getOwnerUid(), change.getId(), ChangeItemType.EXECUTE);
        DmChangeItemDO item = CollectionUtils.isEmpty(items) ? null : items.get(0);
        if (item == null || StringUtils.isBlank(item.getContent())) {
            return;
        }
        ChangeExecuteInfo config = JsonUtils.toObj(item.getContent(), ChangeExecuteInfo.class);
        if (!config.isSnapshot()) {
            return;
        }

        this.changeFlowDal.versionMapper().insertReviewSnapshot(change.getOwnerUid(), change.getRefFlowId(), change.getId(), change.getLastCommitId());

        String messageStr = DmI18nUtils.getMessage(I18nDmMsgKeys.CICD_CHANGE_CREATE_SNAPSHOT_MESSAGE.name(), locale, change.getChangeName());
        this.senderService.sendMessage(change.getOwnerUid(), change.getRefFlowId(), ImMessageType.ChangeNotice, messageStr);
    }

    private void doCallBack(Locale locale, DmChangeDO change, DmChangeFlowDO gitOpsFlowDO) {
        try {
            String callbackMethod = gitOpsFlowDO.getCallbackMethod();
            Response res;
            if (StringUtils.equalsIgnoreCase(callbackMethod, "post")) {
                res = CallUtils.post(gitOpsFlowDO.getCallbackUrl(), Collections.emptyMap());
            } else if (StringUtils.equalsIgnoreCase(callbackMethod, "get")) {
                res = CallUtils.get(gitOpsFlowDO.getCallbackUrl());
            } else {
                throw new ErrorMessageException(DmI18nUtils.getMessage(I18nDmMsgKeys.CICD_CHANGE_CALLBACK_METHOD_NOT_SUPPORT_ERROR.name(), locale, change.getChangeName()));
            }

            // message
            String messageStr;
            if (res.isSuccessful()) {
                messageStr = DmI18nUtils.getMessage(I18nDmMsgKeys.CICD_CHANGE_CALLBACK_MESSAGE.name(), locale, change.getChangeName());
            } else {
                String httpCode = res.code() + ":" + res.message();
                messageStr = DmI18nUtils.getMessage(I18nDmMsgKeys.CICD_CHANGE_CALLBACK_FAILED.name(), locale, change.getChangeName(), httpCode);
            }

            this.senderService.sendMessage(change.getOwnerUid(), change.getRefFlowId(), ImMessageType.ChangeNotice, messageStr);
        } catch (ErrorMessageException e) {
            this.senderService.sendMessage(change.getOwnerUid(), change.getRefFlowId(), ImMessageType.ChangeNotice, e.getErrorMessage());
            log.error(e.getMessage(), e);
        } catch (Throwable e) {
            String messageStr = DmI18nUtils.getMessage(I18nDmMsgKeys.CICD_CHANGE_CALLBACK_ERROR.name(), locale, change.getChangeName(), e.getMessage());
            this.senderService.sendMessage(change.getOwnerUid(), change.getRefFlowId(), ImMessageType.ChangeNotice, messageStr);
            log.error(e.getMessage(), e);
        }
    }
}
