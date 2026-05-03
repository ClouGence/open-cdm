package com.clougence.clouddm.console.web.component.project.action;

import java.util.*;

import javax.annotation.Resource;

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
import com.clougence.clouddm.console.web.constants.I18nDmMsgKeys;
import com.clougence.clouddm.console.web.dal.enumeration.*;
import com.clougence.clouddm.console.web.dal.mapper.DmProjectChangeItemMapper;
import com.clougence.clouddm.console.web.dal.model.DmProjectChangeDO;
import com.clougence.clouddm.console.web.dal.model.DmProjectChangeItemDO;
import com.clougence.clouddm.console.web.dal.model.DmProjectDO;
import com.clougence.clouddm.console.web.dal.model.DmProjectDevopsDO;
import com.clougence.clouddm.console.web.util.DmConvertUtils;
import com.clougence.clouddm.console.web.util.DmI18nUtils;
import com.clougence.clouddm.platform.plugin.PluginManager;
import com.clougence.clouddm.sdk.service.secrules.Requester;
import com.clougence.clouddm.sdk.analysis.split.SplitAnalysisSpi;
import com.clougence.clouddm.sdk.analysis.split.SplitScript;
import com.clougence.schema.umi.struts.UmiTypes;
import com.clougence.utils.JsonUtils;
import com.clougence.utils.i18n.I18nUtils;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class ChangeActionForCheck extends AbstractChangeAction {

    @Resource
    private DmProjectChangeItemMapper dmProjectChangeItemMapper;
    @Resource
    private SecRulesEngine            ruleCheckService;
    @Resource
    private DmDsConfigService         dmDsConfigService;

    @Override
    public void doAction(DmProjectChangeDO change) {
        if (!super.doCommonAction(change)) {
            return;
        } else {
            change = this.dmProjectChangeMapper.queryChangeById(change.getOwnerUid(), change.getId());
        }

        String language = this.imSenderService.getProjectLanguage(change.getOwnerUid(), change.getRefProjectId());
        Locale locale = I18nUtils.getLocale(language);

        // test skip
        DmProjectDO projectDO = this.dmProjectMapper.queryByOwnerAndId(change.getOwnerUid(), change.getRefProjectId());
        DmChangeCheckStrategy checkOpt = projectDO.getFlowCheck();
        if (checkOpt == DmChangeCheckStrategy.Skip) {
            log.info("changeAction[" + change.getId() + "] skip check.");
            this.dmProjectChangeMapper.updateStepTo(change.getId(), change.getVersion(), ProjectChangeStep.APPROVAL, "");
            this.dmProjectChangeMapper.updateFlowWalkedAppend(change.getId(), change, checkOpt);
            return;
        } else {
            this.dmProjectChangeMapper.updateFlowWalkedAppend(change.getId(), change, checkOpt);
        }

        // check
        try {
            List<DmProjectChangeItemDO> diffChange = this.dmProjectChangeItemMapper.queryChangeItemByChangeId(change.getOwnerUid(), change.getId(), DmChangeItemType.REVIEW);
            String sqlChange = diffChange.isEmpty() ? "" : diffChange.get(0).getContent();
            this.checkSql(locale, projectDO, change, sqlChange);
        } catch (Throwable e) {
            log.error("changeAction[" + change.getId() + "] sql check failed," + e.getMessage(), e);
            String errorMsg = DmI18nUtils.getMessage(I18nDmMsgKeys.PROJECT_CHANGE_CHECK_SQL_ERROR.name(), locale, change.getChangeName(), e.getMessage());
            this.imSenderService.sendMessage(change.getOwnerUid(), change.getRefProjectId(), ImMessageType.ChangeNotice, errorMsg);
            this.dmProjectChangeMapper.updateStatusTo(change.getId(), change.getVersion(), ProjectChangeStatus.FAILED, errorMsg);
        }
    }

    private void checkSql(Locale locale, DmProjectDO projectDO, DmProjectChangeDO change, String diffResult) {
        DmProjectDevopsDO devopsDO = this.dmProjectDevopsMapper.queryByOwnerAndId(change.getOwnerUid(), change.getRefDevopsId());
        DataSourceType dsType = devopsDO.getDsType();
        SplitAnalysisSpi analysisSpi = PluginManager.findSplitAnalysisSpi(dsType);
        if (analysisSpi == null) {
            log.error("changeAction[" + change.getId() + "] check review sql failed, SplitAnalysisSpi not found.");
            String errorMsg = DmI18nUtils.getMessage(I18nDmMsgKeys.PROJECT_CHANGE_MISSING_SPLIT_SQL_PLUGIN_ERROR.name(), locale, change.getChangeName(), dsType.name());
            this.imSenderService.sendMessage(change.getOwnerUid(), change.getRefProjectId(), ImMessageType.ChangeNotice, errorMsg);
            this.dmProjectChangeMapper.updateStatusTo(change.getId(), change.getVersion(), ProjectChangeStatus.FAILED, errorMsg);
            return;
        }

        // context
        String ownerUid = devopsDO.getOwnerUid();
        DsLevels dsLevels = this.dmDsConfigService.parseLevels(devopsDO.getDsPath());
        Map<UmiTypes, Object> levelsParam = dsLevels.getLevelsParam();

        // check
        WarnLevel maxLevel = WarnLevel.PASS;
        this.dmProjectChangeItemMapper.deleteByChangeItemType(change.getOwnerUid(), change.getId(), DmChangeItemType.CHECKS);
        List<SplitScript> splits;
        try {
            splits = analysisSpi.splitScript(diffResult, Collections.emptyList(), 0, 0);
        } catch (Exception e) {
            log.error("changeAction[" + change.getId() + "] check review sql failed, " + e.getMessage(), e);
            String errorMsg = DmI18nUtils.getMessage(I18nDmMsgKeys.PROJECT_CHANGE_SQL_PARSER_ERROR.name(), locale, change.getChangeName(), dsType.name());
            this.imSenderService.sendMessage(change.getOwnerUid(), change.getRefProjectId(), ImMessageType.ChangeNotice, errorMsg);
            this.dmProjectChangeMapper.updateStatusTo(change.getId(), change.getVersion(), ProjectChangeStatus.FAILED, errorMsg);
            return;
        }

        for (int i = 0; i < splits.size(); i++) {
            SplitScript splitScript = splits.get(i);
            String trimSql = splitScript.getScript().trim();

            // do check
            SecRulesCheckContext ruleCtx = SecRulesCheckContext.builder()
                .basicCodeLine(splitScript.getBodyStartCodeLine())
                .basicCodeColumn(splitScript.getBodyStartCodeColumn())
                .dsId(dsLevels.getDsDO().getId())
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
            itemDO.setChangeItemType(DmChangeItemType.CHECKS);
            itemDO.setContent(JsonUtils.toJson(checkMO));
            itemDO.setContentIndex(i);
            itemDO.setContentName(trimSql);
            this.dmProjectChangeItemMapper.insert(itemDO);

            maxLevel = checkMaxWarnLevel(maxLevel, checkMO);
        }

        // pause or not.
        boolean isPause = false;
        String pauseMessage = null;
        if (projectDO.getFlowCheck() == DmChangeCheckStrategy.Always) {
            isPause = true;
            pauseMessage = DmI18nUtils.getMessage(I18nDmMsgKeys.PROJECT_CHANGE_CHECK_SQL_PAUSE_BY_ALWAYS_MESSAGE.name(), locale, change.getChangeName());
        } else if (maxLevel != WarnLevel.PASS) {
            isPause = true;
            pauseMessage = DmI18nUtils.getMessage(I18nDmMsgKeys.PROJECT_CHANGE_CHECK_SQL_PAUSE_FLOW_MESSAGE.name(), locale, change.getChangeName());
        }

        // send message.
        if (isPause) {
            this.imSenderService.sendMessage(change.getOwnerUid(), change.getRefProjectId(), ImMessageType.ChangeNotice, pauseMessage);
            this.dmProjectChangeMapper.updateStatusTo(change.getId(), change.getVersion(), ProjectChangeStatus.WAIT, pauseMessage);
        } else {
            String message = DmI18nUtils.getMessage(I18nDmMsgKeys.PROJECT_CHANGE_SQL_REVIEW_SUCCESS.name(), locale, change.getChangeName());
            this.imSenderService.sendMessage(change.getOwnerUid(), change.getRefProjectId(), ImMessageType.ChangeLife, message);
            this.dmProjectChangeMapper.updateStepTo(change.getId(), change.getVersion(), ProjectChangeStep.APPROVAL, "");
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
