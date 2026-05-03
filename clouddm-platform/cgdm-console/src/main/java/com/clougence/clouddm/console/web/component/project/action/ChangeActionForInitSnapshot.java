package com.clougence.clouddm.console.web.component.project.action;

import java.io.File;
import java.io.FileReader;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.clougence.clouddm.console.web.component.project.ImMessageType;
import com.clougence.clouddm.console.web.constants.I18nDmMsgKeys;
import com.clougence.clouddm.console.web.dal.enumeration.ProjectChangeStatus;
import com.clougence.clouddm.console.web.dal.enumeration.ProjectStatus;
import com.clougence.clouddm.console.web.dal.mapper.DmProjectDevopsItemMapper;
import com.clougence.clouddm.console.web.dal.model.*;
import com.clougence.clouddm.console.web.util.DmI18nUtils;
import com.clougence.clouddm.platform.plugin.PluginManager;
import com.clougence.clouddm.sdk.scm.ScmProviderSpi;
import com.clougence.clouddm.sdk.scm.ScmRepo;
import com.clougence.clouddm.sdk.scm.ScmSaveTo;
import com.clougence.clouddm.sdk.scm.ScmProvider;
import com.clougence.utils.i18n.I18nUtils;
import com.clougence.utils.io.FileUtils;
import com.clougence.utils.io.IOUtils;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class ChangeActionForInitSnapshot extends AbstractChangeAction {

    @Resource
    private DmProjectDevopsItemMapper dmProjectDevopsItemMapper;

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
    }

    private void initSqlItem(DmProjectChangeDO change, File projectPath, DmProjectDevopsDO devopsDO) throws Exception {
        this.dmProjectDevopsItemMapper.deleteItemByDevopsId(devopsDO.getOwnerUid(), devopsDO.getId());

        // foreach local file script
        File scriptPath = new File(projectPath, devopsDO.getScmRepoScript());
        List<File> files = FileUtils.walkDown(scriptPath, file -> {
            return file.isDirectory() || (file.isFile() && file.getName().endsWith(".sql"));
        }).stream().filter(File::isFile).collect(Collectors.toList());

        // fileMap for new
        int basePathLength = scriptPath.getAbsolutePath().length();
        int i = 0;
        files.sort(Comparator.comparing(File::getName));
        for (File file : files) {
            String fileName = file.getAbsolutePath().substring(basePathLength + 1);
            try (FileReader reader = new FileReader(file)) {
                DmProjectDevopsItemDO itemDO = new DmProjectDevopsItemDO();
                itemDO.setOwnerUid(change.getOwnerUid());
                itemDO.setRefProjectId(change.getRefProjectId());
                itemDO.setRefDevopsId(devopsDO.getId());
                itemDO.setContentName(fileName);
                itemDO.setContentIndex(i++);
                itemDO.setContent(IOUtils.toString(reader));
                this.dmProjectDevopsItemMapper.insert(itemDO);
            }
        }

        this.dmProjectChangeMapper.updateStatusTo(change.getId(), change.getVersion(), ProjectChangeStatus.FINISH, "");
        this.dmProjectChangeMapper.lockChangeById(change.getId(), change.getVersion() + 1);
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
}
