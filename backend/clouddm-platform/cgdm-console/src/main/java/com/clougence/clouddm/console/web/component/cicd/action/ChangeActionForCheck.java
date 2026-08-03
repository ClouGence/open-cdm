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

import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.*;
import java.util.stream.Stream;

import org.springframework.stereotype.Service;

import com.clougence.clouddm.base.metadata.ds.DataSourceConfig;
import com.clougence.clouddm.base.metadata.ds.DataSourceType;
import com.clougence.clouddm.console.web.component.cicd.ChangeSqlService;
import com.clougence.clouddm.console.web.component.cicd.ImMessageType;
import com.clougence.clouddm.console.web.component.cicd.model.ChangeCheckItemMO;
import com.clougence.clouddm.console.web.component.cicd.model.ChangeCheckMO;
import com.clougence.clouddm.console.web.component.detectrule.*;
import com.clougence.clouddm.console.web.component.dsconfig.DmDsConfigService;
import com.clougence.clouddm.console.web.component.dsconfig.mode.DsLevels;
import com.clougence.clouddm.console.web.global.i18n.DmI18nUtils;
import com.clougence.clouddm.console.web.global.i18n.I18nDmMsgKeys;
import com.clougence.clouddm.console.web.util.DmConvertUtils;
import com.clougence.clouddm.platform.dal.model.cicd.*;
import com.clougence.clouddm.platform.dal.model.secrule.WarnLevel;
import com.clougence.clouddm.sdk.service.secrules.Requester;
import com.clougence.clouddm.sdk.sql.SqlEngineSpi;
import com.clougence.clouddm.sdk.sql.SqlParserParameters;
import com.clougence.clouddm.sdk.sql.parser.SplitAnalysisSpi;
import com.clougence.clouddm.sdk.sql.parser.SplitScript;
import com.clougence.schema.umi.struts.UmiTypes;
import com.clougence.utils.JsonUtils;
import com.clougence.utils.i18n.I18nUtils;

import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class ChangeActionForCheck extends AbstractChangeAction {

    private static final int  MAX_CHECK_DETAILS = 50;

    @Resource
    private SecRulesEngine    ruleCheckService;
    @Resource
    private DmDsConfigService dmDsConfigService;
    @Resource
    private ChangeSqlService  changeSqlService;

    @Override
    public void doAction(DmChangeDO change) {
        if (!super.doCommonAction(change)) {
            return;
        } else {
            change = changeFlowDal.changeMapper().queryChangeById(change.getId());
        }

        String language = this.senderService.getFlowLanguage(change.getOwnerUid(), change.getRefFlowId());
        Locale locale = I18nUtils.getLocale(language);

        // test skip
        DmChangeFlowDO flowDO = changeFlowDal.flowMapper().queryByOwnerAndId(change.getOwnerUid(), change.getRefFlowId());
        ChangeCheckStrategy checkOpt = flowDO.getFlowCheck();
        if (checkOpt == ChangeCheckStrategy.Skip) {
            log.info("changeAction[" + change.getId() + "] skip check.");
            changeFlowDal.changeMapper().updateStepTo(change.getId(), change.getVersion(), ChangeStep.APPROVAL, "");
            changeFlowDal.changeMapper().updateFlowWalkedAppend(change.getId(), change, checkOpt);
            return;
        } else {
            changeFlowDal.changeMapper().updateFlowWalkedAppend(change.getId(), change, checkOpt);
        }

        // check
        try {
            this.checkSql(locale, flowDO, change);
        } catch (Throwable e) {
            log.error("changeAction[" + change.getId() + "] sql check failed," + e.getMessage(), e);
            String errorMsg = DmI18nUtils.getMessage(I18nDmMsgKeys.CICD_CHANGE_CHECK_SQL_ERROR.name(), locale, change.getChangeName(), e.getMessage());
            this.senderService.sendMessage(change.getOwnerUid(), change.getRefFlowId(), ImMessageType.ChangeNotice, errorMsg);
            changeFlowDal.changeMapper().updateStatusTo(change.getId(), change.getVersion(), ChangeStatus.FAILED, errorMsg);
        }
    }

    private void checkSql(Locale locale, DmChangeFlowDO flowDO, DmChangeDO change) {
        DmChangeFlowDO gitOpsFlowDO = changeFlowDal.flowMapper().queryByOwnerAndId(change.getOwnerUid(), change.getRefFlowId());

        // context
        String ownerUid = gitOpsFlowDO.getOwnerUid();
        DsLevels dsLevels = this.dmDsConfigService.parseLevels(gitOpsFlowDO.getDsPath());
        DataSourceConfig dsConfig = this.dmDsConfigService.fetchDsConfigFromExists(dsLevels.dsDO().getId());
        DataSourceType dsType = dsConfig.getDataSourceType();
        SqlEngineSpi sqlEngine = this.dmDsConfigService.fetchSqlEngineSpi(dsLevels.dsDO().getId());
        Map<UmiTypes, Object> levelsParam = dsLevels.levelsParam();
        SqlParserParameters sqlParameters = this.dmDsConfigService.fetchSqlParserParameters(dsLevels.dsDO().getId(), levelsParam);

        SplitAnalysisSpi analysisSpi = sqlEngine.splitAnalysisSpi(sqlParameters);
        if (analysisSpi == null) {
            log.error("changeAction[" + change.getId() + "] check review sql failed, SplitAnalysisSpi not found.");
            String errorMsg = DmI18nUtils.getMessage(I18nDmMsgKeys.CICD_CHANGE_MISSING_SPLIT_SQL_PLUGIN_ERROR.name(), locale, change.getChangeName(), dsType.name());
            this.senderService.sendMessage(change.getOwnerUid(), change.getRefFlowId(), ImMessageType.ChangeNotice, errorMsg);
            changeFlowDal.changeMapper().updateStatusTo(change.getId(), change.getVersion(), ChangeStatus.FAILED, errorMsg);
            return;
        }

        // check
        this.changeFlowDal.changeItemMapper().deleteByChangeItemType(change.getOwnerUid(), change.getId(), ChangeItemType.CHECKS_DETAIL);
        this.changeFlowDal.changeItemMapper().deleteByChangeItemType(change.getOwnerUid(), change.getId(), ChangeItemType.CHECK_SUMMARY);
        SecRulesCheckContext ruleContext = SecRulesCheckContext.builder()
            .dsId(dsLevels.dsDO().getId())
            .currentUID(ownerUid)
            .currentCatalog((String) levelsParam.get(UmiTypes.Catalog))
            .currentSchema((String) levelsParam.get(UmiTypes.Schema))
            .requester(Requester.CHANGE)
            .unsupportedLevel(WarnLevel.FAILURE)
            .sqlParameters(sqlParameters)
            .build();

        SecRulesCheckSession session = this.ruleCheckService.openQueryCheck(flowDO.getFlowManagerUid(), dsConfig, ruleContext);
        WarnLevel maxLevel = this.changeSqlService.consumeSqlFile(change.getId(), sqlFile -> {
            if (!session.isEnabled()) {
                return WarnLevel.PASS;
            }

            SecRulesCheckResult summary = new SecRulesCheckResult();
            WarnLevel currentMaxLevel = WarnLevel.PASS;
            int index = 0;
            int detailCount = 0;
            try (Reader reader = Files.newBufferedReader(sqlFile, StandardCharsets.UTF_8);
                    Stream<SplitScript> scripts = analysisSpi.splitScriptStream(reader, Collections.emptyList(), 0, 0)) {
                Iterator<SplitScript> iterator = scripts.iterator();
                while (iterator.hasNext()) {
                    SplitScript splitScript = iterator.next();
                    String trimSql = splitScript.getScript().trim();
                    SecRulesCheckResult result = session.applyCheck(trimSql, splitScript.getBodyStartCodeLine(), splitScript.getBodyStartCodeColumn());
                    if (result.isAllSuccess()) {
                        index++;
                        continue;
                    }

                    summary.merge(result);
                    ChangeCheckMO checkMO = this.convertToChangeCheck(splitScript, result);
                    currentMaxLevel = checkMaxWarnLevel(currentMaxLevel, checkMO);
                    if (detailCount < MAX_CHECK_DETAILS) {
                        DmChangeItemDO itemDO = new DmChangeItemDO();
                        itemDO.setOwnerUid(change.getOwnerUid());
                        itemDO.setRefFlowId(change.getRefFlowId());
                        itemDO.setRefChangeId(change.getId());
                        itemDO.setChangeItemType(ChangeItemType.CHECKS_DETAIL);
                        itemDO.setContent(JsonUtils.toJson(checkMO));
                        itemDO.setContentIndex(index);
                        itemDO.setContentName(trimSql);
                        this.changeFlowDal.changeItemMapper().insert(itemDO);
                        detailCount++;
                    }
                    index++;
                }
            }

            DmChangeItemDO summaryItem = new DmChangeItemDO();
            summaryItem.setOwnerUid(change.getOwnerUid());
            summaryItem.setRefFlowId(change.getRefFlowId());
            summaryItem.setRefChangeId(change.getId());
            summaryItem.setChangeItemType(ChangeItemType.CHECK_SUMMARY);
            summaryItem.setContent(JsonUtils.toJson(DmConvertUtils.convertToTicketRuleCheckResults(summary)));
            summaryItem.setContentIndex(0);
            summaryItem.setContentName("rule-summary");
            this.changeFlowDal.changeItemMapper().insert(summaryItem);
            return currentMaxLevel;
        });

        // pause or not.
        boolean isPause = false;
        String pauseMessage = null;
        if (flowDO.getFlowCheck() == ChangeCheckStrategy.Always) {
            isPause = true;
            pauseMessage = DmI18nUtils.getMessage(I18nDmMsgKeys.CICD_CHANGE_CHECK_SQL_PAUSE_BY_ALWAYS_MESSAGE.name(), locale, change.getChangeName());
        } else if (maxLevel != WarnLevel.PASS) {
            isPause = true;
            pauseMessage = DmI18nUtils.getMessage(I18nDmMsgKeys.CICD_CHANGE_CHECK_SQL_PAUSE_FLOW_MESSAGE.name(), locale, change.getChangeName());
        }

        // send message.
        if (isPause) {
            this.senderService.sendMessage(change.getOwnerUid(), change.getRefFlowId(), ImMessageType.ChangeNotice, pauseMessage);
            changeFlowDal.changeMapper().updateStatusTo(change.getId(), change.getVersion(), ChangeStatus.WAIT, pauseMessage);
        } else {
            String message = DmI18nUtils.getMessage(I18nDmMsgKeys.CICD_CHANGE_SQL_REVIEW_SUCCESS.name(), locale, change.getChangeName());
            this.senderService.sendMessage(change.getOwnerUid(), change.getRefFlowId(), ImMessageType.ChangeLife, message);
            changeFlowDal.changeMapper().updateStepTo(change.getId(), change.getVersion(), ChangeStep.APPROVAL, "");
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

    private ChangeCheckMO convertToChangeCheck(SplitScript ss, SecRulesCheckResult result) {
        ChangeCheckMO checkMO = new ChangeCheckMO();
        checkMO.setContent(ss.getScript());
        checkMO.setStartCodeLine(ss.getBodyStartCodeLine());
        checkMO.setStartCodeColumn(ss.getBodyStartCodeColumn());
        checkMO.setEndCodeLine(ss.getBodyEndCodeLine());
        checkMO.setEndCodeColumn(ss.getBodyEndCodeColumn());
        checkMO.setLevel(WarnLevel.PASS);
        checkMO.setCheckList(new ArrayList<>());
        for (SecHintInfo info : result.toSecHintList()) {
            ChangeCheckItemMO itemMO = DmConvertUtils.convertToChangeCheckItemMO(info);
            checkMO.getCheckList().add(itemMO);
            if (itemMO.getLevel().getLevel() <= checkMO.getLevel().getLevel()) {
                checkMO.setLevel(itemMO.getLevel());
            }
        }
        return checkMO;
    }
}
