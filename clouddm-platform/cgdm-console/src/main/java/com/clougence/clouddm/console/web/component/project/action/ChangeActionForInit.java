package com.clougence.clouddm.console.web.component.project.action;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import com.clougence.clouddm.sdk.scm.*;
import org.springframework.stereotype.Service;

import com.clougence.clouddm.console.web.component.project.ImMessageType;
import com.clougence.clouddm.console.web.constants.I18nDmMsgKeys;
import com.clougence.clouddm.console.web.dal.enumeration.DmChangeItemType;
import com.clougence.clouddm.console.web.dal.enumeration.ProjectChangeStatus;
import com.clougence.clouddm.console.web.dal.enumeration.ProjectChangeStep;
import com.clougence.clouddm.console.web.dal.enumeration.ProjectStatus;
import com.clougence.clouddm.console.web.dal.mapper.DmProjectChangeItemMapper;
import com.clougence.clouddm.console.web.dal.mapper.DmProjectDevopsItemMapper;
import com.clougence.clouddm.console.web.dal.model.*;
import org.springframework.transaction.annotation.Transactional;
import com.clougence.clouddm.console.web.util.DmI18nUtils;
import com.clougence.clouddm.platform.plugin.PluginManager;
import com.clougence.utils.StringUtils;
import com.clougence.utils.i18n.I18nUtils;
import com.clougence.utils.io.FileUtils;
import com.clougence.utils.io.IOUtils;
import com.github.difflib.DiffUtils;
import com.github.difflib.patch.AbstractDelta;
import com.github.difflib.patch.DeltaType;
import com.github.difflib.patch.Patch;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class ChangeActionForInit extends AbstractChangeAction {

    @Resource
    private DmProjectChangeItemMapper dmProjectChangeItemMapper;
    @Resource
    private DmProjectDevopsItemMapper dmProjectDevopsItemMapper;

    private boolean checkChange(String ownerUid, long changeId) {
        DmProjectChangeDO changeDO = this.dmProjectChangeMapper.queryChangeById(ownerUid, changeId);
        DmProjectDO projectDO = this.dmProjectMapper.queryByOwnerAndId(ownerUid, changeDO.getRefProjectId());

        if (projectDO == null || projectDO.getProjectStatus() != ProjectStatus.NORMAL) {
            return false;
        }

        DmProjectDevopsDO devopsDO = this.dmProjectDevopsMapper.queryByOwnerAndId(ownerUid, changeDO.getRefDevopsId());
        if (devopsDO == null || devopsDO.isDeleted() || !devopsDO.isEnable()) {
            return false;
        }

        DmProjectScmDO scmDO = dmProjectScmMapper.queryById(devopsDO.getRefScmId());
        return scmDO != null;
    }

    @Transactional(rollbackFor = Throwable.class)
    @Override
    public void doAction(DmProjectChangeDO change) throws Exception {
        if (!super.doCommonAction(change)) {
            return;
        } else {
            change = this.dmProjectChangeMapper.queryChangeById(change.getOwnerUid(), change.getId());
        }

        DmProjectDO projectDO = this.dmProjectMapper.queryByOwnerAndId(change.getOwnerUid(), change.getRefProjectId());
        DmProjectDevopsDO devopsDO = this.dmProjectDevopsMapper.queryByOwnerAndId(change.getOwnerUid(), change.getRefDevopsId());
        File space = this.dmProjectService.getProjectSpace(projectDO.getOwnerUid(), projectDO.getId());
        File projectPath = new File(space, projectDO.getProjectCode() + File.separator + change.getRefDevopsId() + "-" + change.getLastCommitId());
        String language = this.imSenderService.getProjectLanguage(change.getOwnerUid(), change.getRefProjectId());
        Locale locale = I18nUtils.getLocale(language);

        // checkout source code
        this.imSenderService.sendMessage(change.getOwnerUid(), change.getRefProjectId(), ImMessageType.ChangeNotice,//
                DmI18nUtils.getMessage(I18nDmMsgKeys.PROJECT_CHANGE_SCM_INIT_FETCH.name(), locale, change.getChangeName()));
        if (!checkoutSource(projectDO, devopsDO, change, locale, projectPath)) {
            return;
        }

        // save sql snapshot
        change = this.dmProjectChangeMapper.queryChangeById(change.getOwnerUid(), change.getId()); // Update version
        this.initSqlItem(change, projectPath, devopsDO);

        // diff sql
        try {
            initDiffSql(locale, change);
        } catch (Throwable e) {
            log.error("changeAction[" + change.getId() + "] refresh review sql failed," + e.getMessage(), e);
            String errorMsg = DmI18nUtils.getMessage(I18nDmMsgKeys.PROJECT_CHANGE_DIFF_CONTENT_ERROR.name(), locale, change.getChangeName(), e.getMessage());
            this.imSenderService.sendMessage(change.getOwnerUid(), change.getRefProjectId(), ImMessageType.ChangeNotice, errorMsg);
            this.dmProjectChangeMapper.updateStatusTo(change.getId(), change.getVersion(), ProjectChangeStatus.FAILED, errorMsg);
        }
    }

    private boolean checkoutSource(DmProjectDO projectDO, DmProjectDevopsDO devopsDO, DmProjectChangeDO change, Locale locale, File projectPath) throws Exception {
        DmProjectScmDO scmDO = dmProjectScmMapper.queryById(devopsDO.getRefScmId());
        AtomicInteger versionLock = new AtomicInteger(change.getVersion());

        // check plugin
        ScmProviderSpi service = PluginManager.findSpi(ScmProviderSpi.class, scmDO.getScmType().getProviderType().name());
        if (service == null) {
            String errorMsg = DmI18nUtils.getMessage(I18nDmMsgKeys.PROJECT_CHANGE_SCM_UNAVAILABLE_ERROR.name(), locale, change.getChangeName());
            this.imSenderService.sendMessage(change.getOwnerUid(), change.getRefProjectId(), ImMessageType.ChangeNotice, errorMsg);
            this.dmProjectChangeMapper.updateStatusTo(change.getId(), versionLock.get(), ProjectChangeStatus.FAILED, errorMsg);
            return false;
        }

        // temp path
        File temp = this.dmProjectService.getTempSpace(projectDO.getOwnerUid(), projectDO.getId());
        File tempPath = new File(temp, projectDO.getProjectCode());

        // download source.
        final long changeId = change.getId();
        final String changeOwnerUid = change.getOwnerUid();
        final AtomicLong timestamp = new AtomicLong(System.currentTimeMillis());

        ScmProvider scm = new ScmProvider();
        scm.setAccessToken(scmDO.getScmAccessToken());
        scm.setServiceUrl(scmDO.getScmServiceUrl());
        ScmRepo repo = new ScmRepo();
        repo.setRepoUrl(devopsDO.getScmRepoUrl());
        repo.setRepoName(devopsDO.getScmRepoName());
        repo.setBranchName(devopsDO.getScmRepoBranch());
        ScmSaveTo saveTo = new ScmSaveTo();
        saveTo.setSaveToLocal(projectPath);
        saveTo.setTempPath(tempPath);
        saveTo.setScriptPath(devopsDO.getScmRepoScript());

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

            int assignAgain = this.dmProjectChangeMapper.assignReadyChange(changeId, versionLock.get());
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

    private void initSqlItem(DmProjectChangeDO change, File projectPath, DmProjectDevopsDO devopsDO) throws Exception {
        int res = this.dmProjectChangeItemMapper.deleteByChangeItemType(change.getOwnerUid(), change.getId(), DmChangeItemType.SQL);

        // foreach local file script
        File scriptPath = new File(projectPath, devopsDO.getScmRepoScript());
        List<File> files = FileUtils.walkDown(scriptPath, file -> {
            return file.isDirectory() || (file.isFile() && file.getName().endsWith(".sql"));
        }).stream().filter(File::isFile).collect(Collectors.toList());

        // update script body. (append)
        int basePathLength = scriptPath.getAbsolutePath().length();
        int i = 0;
        files.sort(Comparator.comparing(File::getName));
        for (File file : files) {
            String fileName = file.getAbsolutePath().substring(basePathLength + 1);
            try (FileReader reader = new FileReader(file)) {
                DmProjectChangeItemDO itemDO = new DmProjectChangeItemDO();
                itemDO.setOwnerUid(change.getOwnerUid());
                itemDO.setRefProjectId(change.getRefProjectId());
                itemDO.setRefChangeId(change.getId());
                itemDO.setChangeItemType(DmChangeItemType.SQL);
                itemDO.setContentName(fileName);
                itemDO.setContentIndex(i++);
                itemDO.setContent(IOUtils.toString(reader));
                this.dmProjectChangeItemMapper.insert(itemDO);
            }
        }
    }

    private void initDiffSql(Locale locale, DmProjectChangeDO change) throws IOException {
        int res = this.dmProjectChangeItemMapper.deleteByChangeItemType(change.getOwnerUid(), change.getId(), DmChangeItemType.REVIEW);

        // current content.
        List<DmProjectDevopsItemDO> itemList = this.dmProjectDevopsItemMapper.queryItemByDevopsId(change.getOwnerUid(), change.getRefDevopsId());
        Map<String, DmProjectDevopsItemDO> itemMap = new HashMap<>();
        for (DmProjectDevopsItemDO item : itemList) {
            itemMap.put(item.getContentName(), item);
        }

        // change content.
        List<DmProjectChangeItemDO> changeList = this.dmProjectChangeItemMapper.queryChangeItemByChangeId(change.getOwnerUid(), change.getId(), DmChangeItemType.SQL);

        String diffResult = diffAlgorithm(changeList, itemMap);
        if (StringUtils.isNotBlank(diffResult)) {
            DmProjectChangeItemDO itemDO = new DmProjectChangeItemDO();
            itemDO.setOwnerUid(change.getOwnerUid());
            itemDO.setRefProjectId(change.getRefProjectId());
            itemDO.setRefChangeId(change.getId());
            itemDO.setChangeItemType(DmChangeItemType.REVIEW);
            itemDO.setContent(diffResult);
            itemDO.setContentIndex(1);
            itemDO.setContentName("none");
            this.dmProjectChangeItemMapper.insert(itemDO);

            String message = DmI18nUtils.getMessage(I18nDmMsgKeys.PROJECT_CHANGE_SCM_INIT_SUCCESS.name(), locale, change.getChangeName());
            this.imSenderService.sendMessage(change.getOwnerUid(), change.getRefProjectId(), ImMessageType.ChangeLife, message);
            this.dmProjectChangeMapper.updateStepTo(change.getId(), change.getVersion(), ProjectChangeStep.CHECK, "");
        } else {
            String message = DmI18nUtils.getMessage(I18nDmMsgKeys.PROJECT_CHANGE_SCM_NO_CHANGE.name(), locale, change.getChangeName());
            this.imSenderService.sendMessage(change.getOwnerUid(), change.getRefProjectId(), ImMessageType.ChangeNotice, message);
            this.dmProjectChangeMapper.updateStatusTo(change.getId(), change.getVersion(), ProjectChangeStatus.CLOSED, message);
            this.dmProjectChangeMapper.lockChangeById(change.getId(), change.getVersion() + 1);
        }
    }

    private String diffAlgorithm(List<DmProjectChangeItemDO> changeList, Map<String, DmProjectDevopsItemDO> itemMap) throws IOException {
        StringBuilder diffResult = new StringBuilder();
        for (DmProjectChangeItemDO changeItem : changeList) {
            String contentName = changeItem.getContentName();

            if (itemMap.containsKey(contentName)) {
                DmProjectDevopsItemDO oldItem = itemMap.get(contentName);
                DmProjectChangeItemDO newItem = changeItem;

                String diffed = diffKeepLastAppend(oldItem.getContent(), newItem.getContent());
                if (StringUtils.isNotBlank(diffed)) {
                    diffResult.append(diffResult.length() == 0 ? "" : "\n\n");
                    diffResult.append("/* sourceCode: " + contentName + " */\n");
                    diffResult.append(diffed);
                    diffResult.append("\n");
                }
            } else {
                diffResult.append(diffResult.length() == 0 ? "" : "\n\n");
                diffResult.append("/* sourceCode: " + contentName + " */\n");
                diffResult.append(changeItem.getContent().trim());
                diffResult.append("\n");
            }
        }
        return diffResult.toString().trim();
    }

    private String diffKeepLastAppend(String oldContent, String newContent) throws IOException {
        List<String> oldVersion = IOUtils.readLines(new StringReader(oldContent));
        List<String> newVersion = IOUtils.readLines(new StringReader(newContent));

        Patch<String> patch = DiffUtils.diff(oldVersion, newVersion);

        // find last add
        StringBuilder builder = new StringBuilder();
        List<AbstractDelta<String>> reverse = new ArrayList<>(patch.getDeltas());
        Collections.reverse(reverse);
        for (AbstractDelta<String> delta : reverse) {
            if (delta.getType() != DeltaType.INSERT) {
                break;
            }

            builder.append(StringUtils.join(delta.getTarget().getLines(), "\n"));
        }

        return builder.toString().trim();
    }
}
