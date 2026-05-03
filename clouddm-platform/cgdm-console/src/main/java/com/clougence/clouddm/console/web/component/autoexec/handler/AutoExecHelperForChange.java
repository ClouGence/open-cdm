package com.clougence.clouddm.console.web.component.autoexec.handler;

import java.util.Locale;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.clougence.clouddm.console.web.component.autoexec.AutoExecHelper;
import com.clougence.clouddm.console.web.component.project.ImMessageType;
import com.clougence.clouddm.console.web.component.project.ImSenderService;
import com.clougence.clouddm.console.web.constants.I18nDmMsgKeys;
import com.clougence.clouddm.console.web.dal.enumeration.ProjectChangeStatus;
import com.clougence.clouddm.console.web.dal.enumeration.ProjectChangeStep;
import com.clougence.clouddm.console.web.dal.enumeration.SQLJobBizType;
import com.clougence.clouddm.console.web.dal.mapper.DmAutoExecJobMapper;
import com.clougence.clouddm.console.web.dal.mapper.DmProjectChangeMapper;
import com.clougence.clouddm.console.web.dal.model.DmProjectChangeDO;
import com.clougence.clouddm.console.web.dal.model.exec.DmAutoExecJobDO;
import com.clougence.clouddm.console.web.util.DmI18nUtils;
import com.clougence.utils.i18n.I18nUtils;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class AutoExecHelperForChange implements AutoExecHelper {

    @Resource
    private DmAutoExecJobMapper   dmAutoExecJobMapper;
    @Resource
    private DmProjectChangeMapper dmProjectChangeMapper;
    @Resource
    private ImSenderService       imSenderService;

    @Override
    public SQLJobBizType getHandleType() { return SQLJobBizType.CHANGE; }

    @Override
    public void execStart(SQLJobBizType bizType, String bizId) {

    }

    @Override
    public void execCompleted(SQLJobBizType bizType, String bizId) {
        DmAutoExecJobDO job = this.dmAutoExecJobMapper.queryByBizId(bizId);
        if (job.getDependOnBizType() != SQLJobBizType.CHANGE) {
            return;
        }

        DmProjectChangeDO change = this.dmProjectChangeMapper.queryChangeById(job.getPrimaryUid(), Long.parseLong(job.getDependOnBizId()));

        // language
        String language = this.imSenderService.getProjectLanguage(change.getOwnerUid(), change.getRefProjectId());
        Locale locale = I18nUtils.getLocale(language);
        String msg = DmI18nUtils.getMessage(I18nDmMsgKeys.PROJECT_CHANGE_EXECUTE_FINISH.name(), locale, change.getChangeName());

        // finish
        int res1 = this.dmProjectChangeMapper.updateStepTo(change.getId(), change.getVersion(), ProjectChangeStep.FINISH, "");
        int res2 = this.dmProjectChangeMapper.updateStatusTo(change.getId(), change.getVersion() + 1, ProjectChangeStatus.READY, "");
        this.imSenderService.sendMessage(change.getOwnerUid(), change.getRefProjectId(), ImMessageType.ChangeLife, msg);
    }

    @Override
    public void execAbort(SQLJobBizType bizType, String bizId) {
        DmAutoExecJobDO job = this.dmAutoExecJobMapper.queryByBizId(bizId);
        if (job.getDependOnBizType() != SQLJobBizType.CHANGE) {
            return;
        }

        DmProjectChangeDO change = this.dmProjectChangeMapper.queryChangeById(job.getPrimaryUid(), Long.parseLong(job.getDependOnBizId()));

        // language
        String language = this.imSenderService.getProjectLanguage(change.getOwnerUid(), change.getRefProjectId());
        Locale locale = I18nUtils.getLocale(language);
        String msg = DmI18nUtils.getMessage(I18nDmMsgKeys.PROJECT_CHANGE_EXECUTE_FAILED_BY_ABORT.name(), locale, change.getChangeName());

        // abort
        int res = this.dmProjectChangeMapper.updateStatusTo(change.getId(), change.getVersion(), ProjectChangeStatus.FAILED, msg);
        this.imSenderService.sendMessage(change.getOwnerUid(), change.getRefProjectId(), ImMessageType.ChangeNotice, msg);
    }

    @Override
    public void execFailed(SQLJobBizType bizType, String bizId) {
        DmAutoExecJobDO job = this.dmAutoExecJobMapper.queryByBizId(bizId);
        if (job.getDependOnBizType() != SQLJobBizType.CHANGE) {
            return;
        }

        DmProjectChangeDO change = this.dmProjectChangeMapper.queryChangeById(job.getPrimaryUid(), Long.parseLong(job.getDependOnBizId()));

        // language
        String language = this.imSenderService.getProjectLanguage(change.getOwnerUid(), change.getRefProjectId());
        Locale locale = I18nUtils.getLocale(language);
        String msg = DmI18nUtils.getMessage(I18nDmMsgKeys.PROJECT_CHANGE_EXECUTE_FAILED_BY_FAILED.name(), locale, change.getChangeName());

        // send message
        int res = this.dmProjectChangeMapper.updateStatusTo(change.getId(), change.getVersion(), ProjectChangeStatus.FAILED, msg);
        this.imSenderService.sendMessage(change.getOwnerUid(), change.getRefProjectId(), ImMessageType.ChangeNotice, msg);
    }
}
