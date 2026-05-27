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

import java.util.*;

import org.springframework.stereotype.Service;

import com.clougence.clouddm.base.metadata.ds.DataSourceType;
import com.clougence.clouddm.console.web.component.detectrule.SecHintInfo;
import com.clougence.clouddm.console.web.component.detectrule.SecRulesCheckContext;
import com.clougence.clouddm.console.web.component.detectrule.SecRulesCheckResult;
import com.clougence.clouddm.console.web.component.detectrule.SecRulesEngine;
import com.clougence.clouddm.console.web.component.dsconfig.DmDsConfigService;
import com.clougence.clouddm.console.web.component.dsconfig.mode.DsLevels;
import com.clougence.clouddm.console.web.component.project.ImMessageType;
import com.clougence.clouddm.console.web.component.project.model.ChangeCheckItemMO;
import com.clougence.clouddm.console.web.component.project.model.ChangeCheckMO;
import com.clougence.clouddm.console.web.global.i18n.DmI18nUtils;
import com.clougence.clouddm.console.web.global.i18n.I18nDmMsgKeys;
import com.clougence.clouddm.console.web.util.DmConvertUtils;
import com.clougence.clouddm.platform.dal.model.project.*;
import com.clougence.clouddm.platform.dal.model.secrule.WarnLevel;
import com.clougence.clouddm.platform.plugin.PluginManager;
import com.clougence.clouddm.sdk.analysis.split.SplitAnalysisSpi;
import com.clougence.clouddm.sdk.analysis.split.SplitScript;
import com.clougence.clouddm.sdk.service.secrules.Requester;
import com.clougence.schema.umi.struts.UmiTypes;
import com.clougence.utils.JsonUtils;
import com.clougence.utils.i18n.I18nUtils;

import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class ChangeActionForCheck extends AbstractChangeAction {

    @Resource
    private SecRulesEngine    ruleCheckService;
    @Resource
    private DmDsConfigService dmDsConfigService;

    @Override
    public void doAction(DmProjectChangeDO change) {
        if (!super.doCommonAction(change)) {
            return;
        } else {
            change = projectDal.changeMapper().queryChangeById(change.getOwnerUid(), change.getId());
        }

        String language = this.senderService.getProjectLanguage(change.getOwnerUid(), change.getRefProjectId());
        Locale locale = I18nUtils.getLocale(language);

        // test skip
        DmProjectDO projectDO = projectDal.projectMapper().queryByOwnerAndId(change.getOwnerUid(), change.getRefProjectId());
        ChangeCheckStrategy checkOpt = projectDO.getFlowCheck();
        if (checkOpt == ChangeCheckStrategy.Skip) {
            log.info("changeAction[" + change.getId() + "] skip check.");
            projectDal.changeMapper().updateStepTo(change.getId(), change.getVersion(), ProjectChangeStep.APPROVAL, "");
            projectDal.changeMapper().updateFlowWalkedAppend(change.getId(), change, checkOpt);
            return;
        } else {
            projectDal.changeMapper().updateFlowWalkedAppend(change.getId(), change, checkOpt);
        }

        // check
        try {
            List<DmProjectChangeItemDO> diffChange = this.projectDal.changeItemMapper().queryChangeItemByChangeId(change.getOwnerUid(), change.getId(), ChangeItemType.REVIEW);
            String sqlChange = diffChange.isEmpty() ? "" : diffChange.get(0).getContent();
            this.checkSql(locale, projectDO, change, sqlChange);
        } catch (Throwable e) {
            log.error("changeAction[" + change.getId() + "] sql check failed," + e.getMessage(), e);
            String errorMsg = DmI18nUtils.getMessage(I18nDmMsgKeys.PROJECT_CHANGE_CHECK_SQL_ERROR.name(), locale, change.getChangeName(), e.getMessage());
            this.senderService.sendMessage(change.getOwnerUid(), change.getRefProjectId(), ImMessageType.ChangeNotice, errorMsg);
            projectDal.changeMapper().updateStatusTo(change.getId(), change.getVersion(), ProjectChangeStatus.FAILED, errorMsg);
        }
    }

    private void checkSql(Locale locale, DmProjectDO projectDO, DmProjectChangeDO change, String diffResult) {
        DmProjectDevopsDO devopsDO = projectDal.devopsMapper().queryByOwnerAndId(change.getOwnerUid(), change.getRefDevopsId());
        DataSourceType dsType = devopsDO.getDsType();
        SplitAnalysisSpi analysisSpi = PluginManager.findSplitAnalysisSpi(dsType);
        if (analysisSpi == null) {
            log.error("changeAction[" + change.getId() + "] check review sql failed, SplitAnalysisSpi not found.");
            String errorMsg = DmI18nUtils.getMessage(I18nDmMsgKeys.PROJECT_CHANGE_MISSING_SPLIT_SQL_PLUGIN_ERROR.name(), locale, change.getChangeName(), dsType.name());
            this.senderService.sendMessage(change.getOwnerUid(), change.getRefProjectId(), ImMessageType.ChangeNotice, errorMsg);
            projectDal.changeMapper().updateStatusTo(change.getId(), change.getVersion(), ProjectChangeStatus.FAILED, errorMsg);
            return;
        }

        // context
        String ownerUid = devopsDO.getOwnerUid();
        DsLevels dsLevels = this.dmDsConfigService.parseLevels(devopsDO.getDsPath());
        Map<UmiTypes, Object> levelsParam = dsLevels.levelsParam();

        // check
        WarnLevel maxLevel = WarnLevel.PASS;
        this.projectDal.changeItemMapper().deleteByChangeItemType(change.getOwnerUid(), change.getId(), ChangeItemType.CHECKS);
        List<SplitScript> splits;
        try {
            splits = analysisSpi.splitScript(diffResult, Collections.emptyList(), 0, 0);
        } catch (Exception e) {
            log.error("changeAction[" + change.getId() + "] check review sql failed, " + e.getMessage(), e);
            String errorMsg = DmI18nUtils.getMessage(I18nDmMsgKeys.PROJECT_CHANGE_SQL_PARSER_ERROR.name(), locale, change.getChangeName(), dsType.name());
            this.senderService.sendMessage(change.getOwnerUid(), change.getRefProjectId(), ImMessageType.ChangeNotice, errorMsg);
            projectDal.changeMapper().updateStatusTo(change.getId(), change.getVersion(), ProjectChangeStatus.FAILED, errorMsg);
            return;
        }

        for (int i = 0; i < splits.size(); i++) {
            SplitScript splitScript = splits.get(i);
            String trimSql = splitScript.getScript().trim();

            // do check
            SecRulesCheckContext ruleCtx = SecRulesCheckContext.builder()
                .basicCodeLine(splitScript.getBodyStartCodeLine())
                .basicCodeColumn(splitScript.getBodyStartCodeColumn())
                .dsId(dsLevels.dsDO().getId())
                .currentUID(ownerUid)
                .currentCatalog((String) levelsParam.get(UmiTypes.Catalog))
                .currentSchema((String) levelsParam.get(UmiTypes.Schema))
                .requester(Requester.CHANGE)
                .unsupportedLevel(WarnLevel.FAILURE)
                .build();
            SecRulesCheckResult result = this.ruleCheckService.doQueryCheck(ownerUid, projectDO.getProjectUid(), trimSql, ruleCtx);
            if (result.isAllSuccess()) {
                continue;
            }

            // convert to DmProjectChangeItemDO
            ChangeCheckMO checkMO = new ChangeCheckMO();
            checkMO.setContent(splitScript.getScript());
            checkMO.setContentKind(splitScript.getType().getAuditKind());
            checkMO.setStartCodeLine(splitScript.getBodyStartCodeLine());
            checkMO.setStartCodeColumn(splitScript.getBodyStartCodeColumn());
            checkMO.setEndCodeLine(splitScript.getBodyEndCodeLine());
            checkMO.setEndCodeColumn(splitScript.getBodyEndCodeColumn());
            checkMO.setLevel(WarnLevel.PASS);
            checkMO.setCheckList(new ArrayList<>());
            for (SecHintInfo info : result.toSecHintList()) {
                ChangeCheckItemMO itemMO = DmConvertUtils.convertToChangeCheckItemMO(info);
                checkMO.getCheckList().add(itemMO);

                if (itemMO.getLevel().getLevel() <= checkMO.getLevel().getLevel()) {
                    checkMO.setLevel(itemMO.getLevel());
                }
            }

            DmProjectChangeItemDO itemDO = new DmProjectChangeItemDO();
            itemDO.setOwnerUid(change.getOwnerUid());
            itemDO.setRefProjectId(change.getRefProjectId());
            itemDO.setRefChangeId(change.getId());
            itemDO.setChangeItemType(ChangeItemType.CHECKS);
            itemDO.setContent(JsonUtils.toJson(checkMO));
            itemDO.setContentIndex(i);
            itemDO.setContentName(trimSql);
            this.projectDal.changeItemMapper().insert(itemDO);

            maxLevel = checkMaxWarnLevel(maxLevel, checkMO);
        }

        // pause or not.
        boolean isPause = false;
        String pauseMessage = null;
        if (projectDO.getFlowCheck() == ChangeCheckStrategy.Always) {
            isPause = true;
            pauseMessage = DmI18nUtils.getMessage(I18nDmMsgKeys.PROJECT_CHANGE_CHECK_SQL_PAUSE_BY_ALWAYS_MESSAGE.name(), locale, change.getChangeName());
        } else if (maxLevel != WarnLevel.PASS) {
            isPause = true;
            pauseMessage = DmI18nUtils.getMessage(I18nDmMsgKeys.PROJECT_CHANGE_CHECK_SQL_PAUSE_FLOW_MESSAGE.name(), locale, change.getChangeName());
        }

        // send message.
        if (isPause) {
            this.senderService.sendMessage(change.getOwnerUid(), change.getRefProjectId(), ImMessageType.ChangeNotice, pauseMessage);
            projectDal.changeMapper().updateStatusTo(change.getId(), change.getVersion(), ProjectChangeStatus.WAIT, pauseMessage);
        } else {
            String message = DmI18nUtils.getMessage(I18nDmMsgKeys.PROJECT_CHANGE_SQL_REVIEW_SUCCESS.name(), locale, change.getChangeName());
            this.senderService.sendMessage(change.getOwnerUid(), change.getRefProjectId(), ImMessageType.ChangeLife, message);
            projectDal.changeMapper().updateStepTo(change.getId(), change.getVersion(), ProjectChangeStep.APPROVAL, "");
        }
    }

    private WarnLevel checkMaxWarnLevel(WarnLevel curLevel, ChangeCheckMO checkMO) {
        WarnLevel check = checkMO.getLevel();
        if (check.getLevel() <= curLevel.getLevel()) {
            return check;
        } else {
            return curLevel;
        }
    }
}
