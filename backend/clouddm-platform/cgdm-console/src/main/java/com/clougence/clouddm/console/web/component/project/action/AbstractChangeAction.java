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

import java.util.Locale;

import org.springframework.stereotype.Service;

import com.clougence.clouddm.console.web.component.project.ImMessageType;
import com.clougence.clouddm.console.web.component.project.ImSenderService;
import com.clougence.clouddm.console.web.global.i18n.DmI18nUtils;
import com.clougence.clouddm.console.web.global.i18n.I18nDmMsgKeys;
import com.clougence.clouddm.console.web.service.project.DmProjectService;
import com.clougence.clouddm.platform.dal.access.ProjectDal;
import com.clougence.clouddm.platform.dal.model.project.*;
import com.clougence.utils.i18n.I18nUtils;

import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public abstract class AbstractChangeAction implements ChangeAction {
    @Resource
    protected ProjectDal       projectDal;
    @Resource
    protected DmProjectService projectService;
    @Resource
    protected ImSenderService  senderService;

    protected boolean doCommonAction(DmProjectChangeDO change) {
        int newVersion = change.getVersion() + 1;
        int assignAgain = projectDal.changeMapper().assignReadyChange(change.getId(), newVersion);
        if (assignAgain == 0) {
            log.info("projectChange " + change.getId() + " assigned failed, maybe has already processed.");
            return false;
        } else {
            newVersion++;
        }

        String language = this.senderService.getProjectLanguage(change.getOwnerUid(), change.getRefProjectId());
        Locale locale = I18nUtils.getLocale(language);

        DmProjectDO projectDO = projectDal.projectMapper().queryByOwnerAndId(change.getOwnerUid(), change.getRefProjectId());
        if (projectDO == null) {
            String errorMsg = DmI18nUtils.getMessage(I18nDmMsgKeys.PROJECT_CHANGE_PROJECT_NOT_EXIST_ERROR.name(), locale, change.getChangeName());
            this.senderService.sendMessage(change.getOwnerUid(), change.getRefProjectId(), ImMessageType.ChangeNotice, errorMsg);
            int res = projectDal.changeMapper().updateStatusTo(change.getId(), newVersion, ProjectChangeStatus.FAILED, errorMsg);
            return false;
        }
        if (projectDO.getProjectStatus() != ProjectStatus.NORMAL) {
            String errorMsg = DmI18nUtils.getMessage(I18nDmMsgKeys.PROJECT_CHANGE_PROJECT_IS_ARCHIVE_OR_DELETE_ERROR.name(), locale, change.getChangeName());
            this.senderService.sendMessage(change.getOwnerUid(), change.getRefProjectId(), ImMessageType.ChangeNotice, errorMsg);
            int res = projectDal.changeMapper().updateStatusTo(change.getId(), newVersion, ProjectChangeStatus.FAILED, errorMsg);
            return false;
        }

        DmProjectDevopsDO devopsDO = projectDal.devopsMapper().queryByOwnerAndId(change.getOwnerUid(), change.getRefDevopsId());
        if (devopsDO == null || devopsDO.isDeleted()) {
            String errorMsg = DmI18nUtils.getMessage(I18nDmMsgKeys.PROJECT_CHANGE_DEVOPS_NOT_EXIST_ERROR.name(), locale, change.getChangeName());
            this.senderService.sendMessage(change.getOwnerUid(), change.getRefProjectId(), ImMessageType.ChangeNotice, errorMsg);
            int res = projectDal.changeMapper().updateStatusTo(change.getId(), newVersion, ProjectChangeStatus.FAILED, errorMsg);
            return false;
        }
        if (!devopsDO.isEnable()) {
            String errorMsg = DmI18nUtils.getMessage(I18nDmMsgKeys.PROJECT_CHANGE_DEVOPS_IS_DISABLED_ERROR.name(), locale, change.getChangeName());
            this.senderService.sendMessage(change.getOwnerUid(), change.getRefProjectId(), ImMessageType.ChangeNotice, errorMsg);
            int res = projectDal.changeMapper().updateStatusTo(change.getId(), newVersion, ProjectChangeStatus.FAILED, errorMsg);
            return false;
        }

        DmProjectScmDO scmDO = projectDal.scmMapper().queryById(devopsDO.getRefScmId());
        if (scmDO == null) {
            String errorMsg = DmI18nUtils.getMessage(I18nDmMsgKeys.PROJECT_CHANGE_SCM_NOT_EXIST_ERROR.name(), locale, change.getChangeName());
            this.senderService.sendMessage(change.getOwnerUid(), change.getRefProjectId(), ImMessageType.ChangeNotice, errorMsg);
            int res = projectDal.changeMapper().updateStatusTo(change.getId(), newVersion, ProjectChangeStatus.FAILED, errorMsg);
            return false;
        }

        return true;
    }
}
