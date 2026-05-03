package com.clougence.clouddm.console.web.component.project.action;

import java.util.Locale;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.clougence.clouddm.console.web.component.project.ImMessageType;
import com.clougence.clouddm.console.web.component.project.ImSenderService;
import com.clougence.clouddm.console.web.constants.I18nDmMsgKeys;
import com.clougence.clouddm.console.web.dal.enumeration.ProjectChangeStatus;
import com.clougence.clouddm.console.web.dal.enumeration.ProjectStatus;
import com.clougence.clouddm.console.web.dal.mapper.DmProjectChangeMapper;
import com.clougence.clouddm.console.web.dal.mapper.DmProjectDevopsMapper;
import com.clougence.clouddm.console.web.dal.mapper.DmProjectMapper;
import com.clougence.clouddm.console.web.dal.mapper.DmProjectScmMapper;
import com.clougence.clouddm.console.web.dal.model.DmProjectChangeDO;
import com.clougence.clouddm.console.web.dal.model.DmProjectDO;
import com.clougence.clouddm.console.web.dal.model.DmProjectDevopsDO;
import com.clougence.clouddm.console.web.dal.model.DmProjectScmDO;
import com.clougence.clouddm.console.web.service.project.DmProjectService;
import com.clougence.clouddm.console.web.util.DmI18nUtils;
import com.clougence.utils.i18n.I18nUtils;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public abstract class AbstractChangeAction implements ChangeAction {

    @Resource
    protected DmProjectMapper       dmProjectMapper;
    @Resource
    protected DmProjectScmMapper    dmProjectScmMapper;
    @Resource
    protected DmProjectDevopsMapper dmProjectDevopsMapper;
    @Resource
    protected DmProjectChangeMapper dmProjectChangeMapper;
    @Resource
    protected DmProjectService      dmProjectService;
    @Resource
    protected ImSenderService       imSenderService;

    protected boolean doCommonAction(DmProjectChangeDO change) {
        int newVersion = change.getVersion() + 1;
        int assignAgain = this.dmProjectChangeMapper.assignReadyChange(change.getId(), newVersion);
        if (assignAgain == 0) {
            log.info("projectChange " + change.getId() + " assigned failed, maybe has already processed.");
            return false;
        } else {
            newVersion++;
        }

        String language = this.imSenderService.getProjectLanguage(change.getOwnerUid(), change.getRefProjectId());
        Locale locale = I18nUtils.getLocale(language);

        DmProjectDO projectDO = this.dmProjectMapper.queryByOwnerAndId(change.getOwnerUid(), change.getRefProjectId());
        if (projectDO == null) {
            String errorMsg = DmI18nUtils.getMessage(I18nDmMsgKeys.PROJECT_CHANGE_PROJECT_NOT_EXIST_ERROR.name(), locale, change.getChangeName());
            this.imSenderService.sendMessage(change.getOwnerUid(), change.getRefProjectId(), ImMessageType.ChangeNotice, errorMsg);
            int res = this.dmProjectChangeMapper.updateStatusTo(change.getId(), newVersion, ProjectChangeStatus.FAILED, errorMsg);
            return false;
        }
        if (projectDO.getProjectStatus() != ProjectStatus.NORMAL) {
            String errorMsg = DmI18nUtils.getMessage(I18nDmMsgKeys.PROJECT_CHANGE_PROJECT_IS_ARCHIVE_OR_DELETE_ERROR.name(), locale, change.getChangeName());
            this.imSenderService.sendMessage(change.getOwnerUid(), change.getRefProjectId(), ImMessageType.ChangeNotice, errorMsg);
            int res = this.dmProjectChangeMapper.updateStatusTo(change.getId(), newVersion, ProjectChangeStatus.FAILED, errorMsg);
            return false;
        }

        DmProjectDevopsDO devopsDO = this.dmProjectDevopsMapper.queryByOwnerAndId(change.getOwnerUid(), change.getRefDevopsId());
        if (devopsDO == null || devopsDO.isDeleted()) {
            String errorMsg = DmI18nUtils.getMessage(I18nDmMsgKeys.PROJECT_CHANGE_DEVOPS_NOT_EXIST_ERROR.name(), locale, change.getChangeName());
            this.imSenderService.sendMessage(change.getOwnerUid(), change.getRefProjectId(), ImMessageType.ChangeNotice, errorMsg);
            int res = this.dmProjectChangeMapper.updateStatusTo(change.getId(), newVersion, ProjectChangeStatus.FAILED, errorMsg);
            return false;
        }
        if (!devopsDO.isEnable()) {
            String errorMsg = DmI18nUtils.getMessage(I18nDmMsgKeys.PROJECT_CHANGE_DEVOPS_IS_DISABLED_ERROR.name(), locale, change.getChangeName());
            this.imSenderService.sendMessage(change.getOwnerUid(), change.getRefProjectId(), ImMessageType.ChangeNotice, errorMsg);
            int res = this.dmProjectChangeMapper.updateStatusTo(change.getId(), newVersion, ProjectChangeStatus.FAILED, errorMsg);
            return false;
        }

        DmProjectScmDO scmDO = dmProjectScmMapper.queryById(devopsDO.getRefScmId());
        if (scmDO == null) {
            String errorMsg = DmI18nUtils.getMessage(I18nDmMsgKeys.PROJECT_CHANGE_SCM_NOT_EXIST_ERROR.name(), locale, change.getChangeName());
            this.imSenderService.sendMessage(change.getOwnerUid(), change.getRefProjectId(), ImMessageType.ChangeNotice, errorMsg);
            int res = this.dmProjectChangeMapper.updateStatusTo(change.getId(), newVersion, ProjectChangeStatus.FAILED, errorMsg);
            return false;
        }

        return true;
    }
}
