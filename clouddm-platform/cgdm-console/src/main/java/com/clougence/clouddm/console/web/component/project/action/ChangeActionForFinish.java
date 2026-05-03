package com.clougence.clouddm.console.web.component.project.action;

import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.clougence.clouddm.console.web.component.project.ImMessageType;
import com.clougence.clouddm.console.web.component.project.model.ChangeExecuteInfo;
import com.clougence.clouddm.console.web.constants.I18nDmMsgKeys;
import com.clougence.clouddm.console.web.dal.enumeration.DmChangeItemType;
import com.clougence.clouddm.console.web.dal.enumeration.DmProjectVersionType;
import com.clougence.clouddm.console.web.dal.enumeration.ProjectChangeStatus;
import com.clougence.clouddm.console.web.dal.mapper.DmProjectChangeItemMapper;
import com.clougence.clouddm.console.web.dal.mapper.DmProjectDevopsItemMapper;
import com.clougence.clouddm.console.web.dal.mapper.DmProjectVersionMapper;
import com.clougence.clouddm.console.web.dal.model.*;
import org.springframework.transaction.annotation.Transactional;
import com.clougence.clouddm.console.web.util.DmI18nUtils;
import com.clougence.clouddm.console.web.util.HttpUtils;
import com.clougence.rdp.global.exception.ErrorMessageException;
import com.clougence.utils.CollectionUtils;
import com.clougence.utils.JsonUtils;
import com.clougence.utils.StringUtils;
import com.clougence.utils.i18n.I18nUtils;

import lombok.extern.slf4j.Slf4j;
import okhttp3.Response;

@Slf4j
@Service
public class ChangeActionForFinish extends AbstractChangeAction {

    @Resource
    private DmProjectVersionMapper    dmProjectVersionMapper;
    @Resource
    private DmProjectDevopsItemMapper dmProjectDevopsItemMapper;
    @Resource
    private DmProjectChangeItemMapper dmProjectChangeItemMapper;

    @Transactional(rollbackFor = Throwable.class)
    @Override
    public void doAction(DmProjectChangeDO change) {
        if (!super.doCommonAction(change)) {
            return;
        } else {
            change = this.dmProjectChangeMapper.queryChangeById(change.getOwnerUid(), change.getId());
        }

        // message i18n
        String language = this.imSenderService.getProjectLanguage(change.getOwnerUid(), change.getRefProjectId());
        Locale locale = I18nUtils.getLocale(language);

        // store to devops version
        this.storeToDevOps(locale, change);
        this.storeToSnapshot(locale, change);

        this.dmProjectChangeMapper.updateStatusTo(change.getId(), change.getVersion(), ProjectChangeStatus.FINISH, "");
        this.dmProjectChangeMapper.lockChangeById(change.getId(), change.getVersion() + 1);

        // callback
        DmProjectDevopsDO devopsDO = this.dmProjectDevopsMapper.queryByOwnerAndId(change.getOwnerUid(), change.getRefDevopsId());
        if (devopsDO.isEnableCallback()) {
            this.doCallBack(locale, change, devopsDO);
        }
    }

    private void storeToDevOps(Locale locale, DmProjectChangeDO change) {
        this.dmProjectDevopsItemMapper.deleteItemByDevopsId(change.getOwnerUid(), change.getRefDevopsId());
        List<DmProjectChangeItemDO> itemList = this.dmProjectChangeItemMapper.queryChangeItemByChangeId(change.getOwnerUid(), change.getId(), DmChangeItemType.SQL);
        for (DmProjectChangeItemDO item : itemList) {
            DmProjectDevopsItemDO itemDO = new DmProjectDevopsItemDO();
            itemDO.setOwnerUid(change.getOwnerUid());
            itemDO.setRefProjectId(change.getRefProjectId());
            itemDO.setRefDevopsId(change.getRefDevopsId());
            itemDO.setContentName(item.getContentName());
            itemDO.setContentIndex(item.getContentIndex());
            itemDO.setContent(item.getContent());
            this.dmProjectDevopsItemMapper.insert(itemDO);
        }

        String messageStr = DmI18nUtils.getMessage(I18nDmMsgKeys.PROJECT_CHANGE_UPDATE_SQL_BASE_LINE_MESSAGE.name(), locale, change.getChangeName());
        this.imSenderService.sendMessage(change.getOwnerUid(), change.getRefProjectId(), ImMessageType.ChangeNotice, messageStr);
    }

    private void storeToSnapshot(Locale locale, DmProjectChangeDO change) {
        List<DmProjectChangeItemDO> items = this.dmProjectChangeItemMapper.queryChangeItemByChangeId(change.getOwnerUid(), change.getId(), DmChangeItemType.EXECUTE);
        DmProjectChangeItemDO item = CollectionUtils.isEmpty(items) ? null : items.get(0);
        if (item == null || StringUtils.isBlank(item.getContent())) {
            return;
        }
        ChangeExecuteInfo config = JsonUtils.toObj(item.getContent(), ChangeExecuteInfo.class);
        if (!config.isSnapshot()) {
            return;
        }

        //
        List<DmProjectChangeItemDO> diffChange = this.dmProjectChangeItemMapper.queryChangeItemByChangeId(change.getOwnerUid(), change.getId(), DmChangeItemType.REVIEW);
        String changeSql = diffChange.isEmpty() ? "" : diffChange.get(0).getContent();

        DmProjectVersionDO versionDO = new DmProjectVersionDO();
        versionDO.setOwnerUid(change.getOwnerUid());
        versionDO.setRefProjectId(change.getRefProjectId());
        versionDO.setRefChangeId(change.getId());
        versionDO.setRefDevopsId(change.getRefDevopsId());
        versionDO.setVersion(new Date());
        versionDO.setCommitId(change.getLastCommitId());
        versionDO.setContent(changeSql);
        versionDO.setType(DmProjectVersionType.Change);
        this.dmProjectVersionMapper.insert(versionDO);

        String messageStr = DmI18nUtils.getMessage(I18nDmMsgKeys.PROJECT_CHANGE_CREATE_SNAPSHOT_MESSAGE.name(), locale, change.getChangeName());
        this.imSenderService.sendMessage(change.getOwnerUid(), change.getRefProjectId(), ImMessageType.ChangeNotice, messageStr);
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

            this.imSenderService.sendMessage(change.getOwnerUid(), change.getRefProjectId(), ImMessageType.ChangeNotice, messageStr);
        } catch (ErrorMessageException e) {
            this.imSenderService.sendMessage(change.getOwnerUid(), change.getRefProjectId(), ImMessageType.ChangeNotice, e.getErrorMessage());
            log.error(e.getMessage(), e);
        } catch (Throwable e) {
            String messageStr = DmI18nUtils.getMessage(I18nDmMsgKeys.PROJECT_CHANGE_CALLBACK_ERROR.name(), locale, change.getChangeName(), e.getMessage());
            this.imSenderService.sendMessage(change.getOwnerUid(), change.getRefProjectId(), ImMessageType.ChangeNotice, messageStr);
            log.error(e.getMessage(), e);
        }
    }
}
