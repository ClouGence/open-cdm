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
import java.util.Date;
import java.util.List;
import java.util.Locale;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.clougence.clouddm.api.common.exception.ErrorMessageException;
import com.clougence.clouddm.console.web.component.project.ImMessageType;
import com.clougence.clouddm.console.web.component.project.model.ChangeExecuteInfo;
import com.clougence.clouddm.console.web.global.i18n.DmI18nUtils;
import com.clougence.clouddm.console.web.global.i18n.I18nDmMsgKeys;
import com.clougence.clouddm.console.web.util.HttpUtils;
import com.clougence.clouddm.platform.dal.model.project.*;
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
    public void doAction(DmProjectChangeDO change) {
        if (!super.doCommonAction(change)) {
            return;
        } else {
            change = projectDal.changeMapper().queryChangeById(change.getOwnerUid(), change.getId());
        }

        // message i18n
        String language = this.senderService.getProjectLanguage(change.getOwnerUid(), change.getRefProjectId());
        Locale locale = I18nUtils.getLocale(language);

        // store to devops version
        this.storeToDevOps(locale, change);
        this.storeToSnapshot(locale, change);

        projectDal.changeMapper().updateStatusTo(change.getId(), change.getVersion(), ProjectChangeStatus.FINISH, "");
        projectDal.changeMapper().lockChangeById(change.getId(), change.getVersion() + 1);

        // callback
        DmProjectDevopsDO devopsDO = projectDal.devopsMapper().queryByOwnerAndId(change.getOwnerUid(), change.getRefDevopsId());
        if (devopsDO.isEnableCallback()) {
            this.doCallBack(locale, change, devopsDO);
        }
    }

    private void storeToDevOps(Locale locale, DmProjectChangeDO change) {
        this.projectDal.devopsItemMapper().deleteItemByDevopsId(change.getOwnerUid(), change.getRefDevopsId());
        List<DmProjectChangeItemDO> itemList = this.projectDal.changeItemMapper().queryChangeItemByChangeId(change.getOwnerUid(), change.getId(), ChangeItemType.SQL);
        for (DmProjectChangeItemDO item : itemList) {
            DmProjectDevopsItemDO itemDO = new DmProjectDevopsItemDO();
            itemDO.setOwnerUid(change.getOwnerUid());
            itemDO.setRefProjectId(change.getRefProjectId());
            itemDO.setRefDevopsId(change.getRefDevopsId());
            itemDO.setContentName(item.getContentName());
            itemDO.setContentIndex(item.getContentIndex());
            itemDO.setContent(item.getContent());
            this.projectDal.devopsItemMapper().insert(itemDO);
        }

        String messageStr = DmI18nUtils.getMessage(I18nDmMsgKeys.PROJECT_CHANGE_UPDATE_SQL_BASE_LINE_MESSAGE.name(), locale, change.getChangeName());
        this.senderService.sendMessage(change.getOwnerUid(), change.getRefProjectId(), ImMessageType.ChangeNotice, messageStr);
    }

    private void storeToSnapshot(Locale locale, DmProjectChangeDO change) {
        List<DmProjectChangeItemDO> items = this.projectDal.changeItemMapper().queryChangeItemByChangeId(change.getOwnerUid(), change.getId(), ChangeItemType.EXECUTE);
        DmProjectChangeItemDO item = CollectionUtils.isEmpty(items) ? null : items.get(0);
        if (item == null || StringUtils.isBlank(item.getContent())) {
            return;
        }
        ChangeExecuteInfo config = JsonUtils.toObj(item.getContent(), ChangeExecuteInfo.class);
        if (!config.isSnapshot()) {
            return;
        }

        //
        List<DmProjectChangeItemDO> diffChange = this.projectDal.changeItemMapper().queryChangeItemByChangeId(change.getOwnerUid(), change.getId(), ChangeItemType.REVIEW);
        String changeSql = diffChange.isEmpty() ? "" : diffChange.get(0).getContent();

        DmProjectVersionDO versionDO = new DmProjectVersionDO();
        versionDO.setOwnerUid(change.getOwnerUid());
        versionDO.setRefProjectId(change.getRefProjectId());
        versionDO.setRefChangeId(change.getId());
        versionDO.setRefDevopsId(change.getRefDevopsId());
        versionDO.setVersion(new Date());
        versionDO.setCommitId(change.getLastCommitId());
        versionDO.setContent(changeSql);
        versionDO.setType(ProjectVersionType.Change);
        this.projectDal.versionMapper().insert(versionDO);

        String messageStr = DmI18nUtils.getMessage(I18nDmMsgKeys.PROJECT_CHANGE_CREATE_SNAPSHOT_MESSAGE.name(), locale, change.getChangeName());
        this.senderService.sendMessage(change.getOwnerUid(), change.getRefProjectId(), ImMessageType.ChangeNotice, messageStr);
    }

    private void doCallBack(Locale locale, DmProjectChangeDO change, DmProjectDevopsDO devopsDO) {
        try {
            String callbackMethod = devopsDO.getCallbackMethod();
            Response res;
            if (StringUtils.equalsIgnoreCase(callbackMethod, "post")) {
                res = HttpUtils.post(devopsDO.getCallbackUrl(), Collections.emptyMap());
            } else if (StringUtils.equalsIgnoreCase(callbackMethod, "get")) {
                res = HttpUtils.get(devopsDO.getCallbackUrl());
            } else {
                throw new ErrorMessageException(DmI18nUtils.getMessage(I18nDmMsgKeys.PROJECT_CHANGE_CALLBACK_METHOD_NOT_SUPPORT_ERROR.name(), locale, change.getChangeName()));
            }

            // message
            String messageStr;
            if (res.isSuccessful()) {
                messageStr = DmI18nUtils.getMessage(I18nDmMsgKeys.PROJECT_CHANGE_CALLBACK_MESSAGE.name(), locale, change.getChangeName());
            } else {
                String httpCode = res.code() + ":" + res.message();
                messageStr = DmI18nUtils.getMessage(I18nDmMsgKeys.PROJECT_CHANGE_CALLBACK_FAILED.name(), locale, change.getChangeName(), httpCode);
            }

            this.senderService.sendMessage(change.getOwnerUid(), change.getRefProjectId(), ImMessageType.ChangeNotice, messageStr);
        } catch (ErrorMessageException e) {
            this.senderService.sendMessage(change.getOwnerUid(), change.getRefProjectId(), ImMessageType.ChangeNotice, e.getErrorMessage());
            log.error(e.getMessage(), e);
        } catch (Throwable e) {
            String messageStr = DmI18nUtils.getMessage(I18nDmMsgKeys.PROJECT_CHANGE_CALLBACK_ERROR.name(), locale, change.getChangeName(), e.getMessage());
            this.senderService.sendMessage(change.getOwnerUid(), change.getRefProjectId(), ImMessageType.ChangeNotice, messageStr);
            log.error(e.getMessage(), e);
        }
    }
}
