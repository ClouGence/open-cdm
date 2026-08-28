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

import java.io.File;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.clougence.clouddm.console.web.component.cicd.CicdSqlFileUtils;
import com.clougence.clouddm.console.web.component.cicd.ImMessageType;
import com.clougence.clouddm.console.web.global.i18n.DmI18nUtils;
import com.clougence.clouddm.console.web.global.i18n.I18nDmMsgKeys;
import com.clougence.clouddm.platform.dal.model.cicd.*;
import com.clougence.clouddm.platform.dal.model.gitops.DmGitOpsScmDO;
import com.clougence.clouddm.platform.plugin.PluginManager;
import com.clougence.clouddm.sdk.scm.*;
import com.clougence.utils.i18n.I18nUtils;
import com.clougence.utils.io.FileUtils;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class ChangeActionForInitSnapshot extends AbstractChangeAction {

    @Override
    public void doAction(DmChangeDO change) throws Exception {
        if (!super.doCommonAction(change)) {
            return;
        } else {
            change = changeFlowDal.changeMapper().queryChangeById(change.getId());
        }

        DmChangeFlowDO flowDO = changeFlowDal.flowMapper().queryByOwnerAndId(change.getOwnerUid(), change.getRefFlowId());
        DmChangeFlowDO gitOpsFlowDO = changeFlowDal.flowMapper().queryByOwnerAndId(change.getOwnerUid(), change.getRefFlowId());
        File space = this.changeFlowService.getCicdWorkspace(flowDO.getOwnerUid(), flowDO.getId());
        File checkoutPath = new File(space, flowDO.getFlowUid() + File.separator + change.getRefFlowId() + "-" + change.getLastCommitId());
        String language = this.senderService.getFlowLanguage(change.getOwnerUid(), change.getRefFlowId());
        Locale locale = I18nUtils.getLocale(language);

        // checkout source code
        this.senderService.sendMessage(change.getOwnerUid(), change.getRefFlowId(), ImMessageType.ChangeNotice,//
                DmI18nUtils.getMessage(I18nDmMsgKeys.CICD_CHANGE_SCM_INIT_FETCH.name(), locale, change.getChangeName()));
        if (!checkoutSource(flowDO, gitOpsFlowDO, change, locale, checkoutPath)) {
            return;
        }

        // save sql snapshot
        change = changeFlowDal.changeMapper().queryChangeById(change.getId()); // Update version
        this.initSqlItem(change, checkoutPath, gitOpsFlowDO);
    }

    private void initSqlItem(DmChangeDO change, File checkoutPath, DmChangeFlowDO gitOpsFlowDO) throws Exception {
        this.changeFlowDal.flowItemMapper().deleteItemByFlowId(gitOpsFlowDO.getOwnerUid(), gitOpsFlowDO.getId());

        // foreach local file script
        File scriptPath = new File(checkoutPath, gitOpsFlowDO.getScmRepoScript());
        List<File> files = FileUtils.walkDown(scriptPath, file -> {
            return file.isDirectory() || (file.isFile() && file.getName().toLowerCase(Locale.ROOT).endsWith(".sql"));
        }).stream().filter(File::isFile).collect(Collectors.toList());

        // fileMap for new
        int basePathLength = scriptPath.getAbsolutePath().length();
        int i = 0;
        files.sort(Comparator.comparing(File::getName));
        for (File file : files) {
            String fileName = file.getAbsolutePath().substring(basePathLength + 1);
            DmChangeFlowItemDO itemDO = new DmChangeFlowItemDO();
            itemDO.setOwnerUid(change.getOwnerUid());
            itemDO.setRefFlowId(change.getRefFlowId());
            itemDO.setRefFlowId(gitOpsFlowDO.getId());
            itemDO.setContentName(fileName);
            itemDO.setContentIndex(i++);
            itemDO.setContent(CicdSqlFileUtils.readUtf8(file));
            this.changeFlowDal.flowItemMapper().insert(itemDO);
        }

        changeFlowDal.changeMapper().updateStatusTo(change.getId(), change.getVersion(), ChangeStatus.FINISH, "");
        changeFlowDal.changeMapper().lockChangeById(change.getId(), change.getVersion() + 1);
    }

    private boolean checkoutSource(DmChangeFlowDO flowDO, DmChangeFlowDO gitOpsFlowDO, DmChangeDO change, Locale locale, File checkoutPath) throws Exception {
        DmGitOpsScmDO scmDO = changeFlowDal.scmMapper().queryByOwnerAndId(change.getOwnerUid(), gitOpsFlowDO.getRefScmId());
        AtomicInteger versionLock = new AtomicInteger(change.getVersion());

        // check plugin
        ScmProviderSpi service = PluginManager.findSpi(ScmProviderSpi.class, scmDO.getScmType().getProviderType().name());
        if (service == null) {
            String errorMsg = DmI18nUtils.getMessage(I18nDmMsgKeys.CICD_CHANGE_SCM_UNAVAILABLE_ERROR.name(), locale, change.getChangeName());
            this.senderService.sendMessage(change.getOwnerUid(), change.getRefFlowId(), ImMessageType.ChangeNotice, errorMsg);
            changeFlowDal.changeMapper().updateStatusTo(change.getId(), versionLock.get(), ChangeStatus.FAILED, errorMsg);
            return false;
        }

        // temp path
        File temp = this.changeFlowService.getCicdTempSpace(flowDO.getOwnerUid(), flowDO.getId());
        File tempPath = new File(temp, flowDO.getFlowUid());

        // download source.
        final long changeId = change.getId();
        final String changeOwnerUid = change.getOwnerUid();
        final AtomicLong timestamp = new AtomicLong(System.currentTimeMillis());

        ScmProvider scm = new ScmProvider();
        scm.setAccessToken(scmDO.getScmAccessToken());
        scm.setServiceUrl(scmDO.getScmServiceUrl());
        ScmRepo repo = new ScmRepo();
        String repoSpace = gitOpsFlowDO.getScmRepoSpace();
        String repoName = gitOpsFlowDO.getScmRepoName();
        repo.setRepoId(gitOpsFlowDO.getScmRepoIdentifier());
        repo.setRepoPath(ScmUtils.buildRepoPath(repoSpace, repoName));
        repo.setRepoSpace(repoSpace);
        repo.setRepoUrl(gitOpsFlowDO.getScmRepoUrl());
        repo.setRepoName(repoName);
        repo.setBranchName(gitOpsFlowDO.getScmRepoBranch());
        repo.setCommitId(change.getLastCommitId());
        ScmSaveTo saveTo = new ScmSaveTo();
        saveTo.setSaveToLocal(checkoutPath);
        saveTo.setTempPath(tempPath);
        saveTo.setScriptPath(gitOpsFlowDO.getScmRepoScript());

        log.error("changeAction[" + changeId + "] clear sourceCode files.");
        service.downloadToLocal(scm, repo, saveTo, () -> {
            if (!checkChange(changeOwnerUid, changeId)) {
                log.error("changeAction[" + changeId + "] watchdog checkChange status failed, downloadScm is blocked.");
                return false;
            }

            // version heartbeat
            if ((timestamp.get() + 1000) > System.currentTimeMillis()) {
                return true;
            }

            int assignAgain = changeFlowDal.changeMapper().assignReadyChange(changeId, versionLock.get());
            if (assignAgain == 0) {
                log.error("changeAction[" + changeId + "] watchdog failed, downloadScm is blocked.");
                return false;
            } else {
                versionLock.incrementAndGet();
                timestamp.set(System.currentTimeMillis());
            }
            return true;
        });

        return true;
    }

    private boolean checkChange(String ownerUid, long changeId) {
        DmChangeDO changeDO = changeFlowDal.changeMapper().queryChangeById(changeId);
        DmChangeFlowDO flowDO = changeFlowDal.flowMapper().queryByOwnerAndId(ownerUid, changeDO.getRefFlowId());

        if (flowDO == null || flowDO.getChangeFlowStatus() != ChangeFlowStatus.NORMAL) {
            return false;
        }

        DmChangeFlowDO gitOpsFlowDO = changeFlowDal.flowMapper().queryByOwnerAndId(ownerUid, changeDO.getRefFlowId());
        if (gitOpsFlowDO == null || gitOpsFlowDO.isDeleted() || !gitOpsFlowDO.isEnable()) {
            return false;
        }

        DmGitOpsScmDO scmDO = changeFlowDal.scmMapper().queryByOwnerAndId(ownerUid, gitOpsFlowDO.getRefScmId());
        return scmDO != null;
    }
}
