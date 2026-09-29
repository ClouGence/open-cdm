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
package com.clougence.clouddm.console.web.service.editor.script;

import java.util.*;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;

import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.clougence.clouddm.api.common.boot.UnifiedPostConstruct;
import com.clougence.clouddm.api.common.exception.ErrorMessageException;
import com.clougence.clouddm.base.metadata.ds.DataSourceType;
import com.clougence.clouddm.console.web.global.i18n.DmI18nUtils;
import com.clougence.clouddm.console.web.global.i18n.I18nDmMsgKeys;
import com.clougence.clouddm.console.web.model.fo.editor.query.SqlScriptCreateFO;
import com.clougence.clouddm.console.web.model.fo.editor.query.SqlScriptListFO;
import com.clougence.clouddm.console.web.model.fo.editor.query.SqlScriptUpdateFO;
import com.clougence.clouddm.console.web.model.vo.editor.query.SqlScriptDetailVO;
import com.clougence.clouddm.console.web.model.vo.editor.query.SqlScriptListVO;
import com.clougence.clouddm.console.web.model.vo.editor.query.SqlScriptMutationVO;
import com.clougence.clouddm.console.web.model.vo.editor.query.SqlScriptSummaryVO;
import com.clougence.clouddm.platform.dal.access.ExecutionDal;
import com.clougence.clouddm.platform.dal.model.execution.DmExecSqlScriptDO;
import com.clougence.clouddm.platform.dal.model.execution.SqlScriptStatus;
import com.clougence.clouddm.platform.dal.util.PageObj;
import com.clougence.clouddm.platform.plugin.PluginManager;
import com.clougence.utils.ThreadUtils;

import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class SqlScriptServiceImpl implements SqlScriptService, UnifiedPostConstruct {

    private static final int            DEFAULT_PAGE_SIZE = 20;
    private static final int            MAX_PAGE_SIZE     = 100;
    private static final int            CLEAN_BATCH       = 200;
    private static final long           FILE_GRACE_MS     = TimeUnit.HOURS.toMillis(1);
    @Resource
    private ExecutionDal                executionDal;
    @Resource
    private SqlScriptStorage            sqlScriptStorage;
    private ScheduledThreadPoolExecutor cleanExecutor;

    @Override
    public void init() {
        ThreadFactory factory = ThreadUtils.daemonThreadFactory(this.getClass().getClassLoader(), "SqlScriptClear-%s");
        this.cleanExecutor = new ScheduledThreadPoolExecutor(1, factory);
        this.cleanExecutor.scheduleWithFixedDelay(this::cleanup, 1, 10, TimeUnit.MINUTES);
    }

    @Override
    public void stop() {
        if (this.cleanExecutor != null) {
            this.cleanExecutor.shutdown();
        }
    }

    @Override
    public SqlScriptMutationVO create(String ownerUid, SqlScriptCreateFO fo) {
        String name = normalizeName(fo.getName());
        validateDsType(fo.getDsType());
        validateContent(fo.getSqlContent());

        String fileUri = store(fo.getSqlContent());
        DmExecSqlScriptDO script = new DmExecSqlScriptDO();
        script.setOwnerUid(ownerUid);
        script.setName(name);
        script.setDsType(fo.getDsType());
        script.setFileUri(fileUri);
        script.setStatus(SqlScriptStatus.Ready);
        script.setVersion(0L);
        Date modified = currentSecond();
        script.setGmtCreate(modified);
        script.setGmtModified(modified);
        try {
            this.executionDal.sqlScriptMapper().insert(script);
        } catch (DuplicateKeyException e) {
            deleteStored(fileUri);
            throw error(I18nDmMsgKeys.CONSOLE_QUERY_SCRIPT_NAME_EXISTS_ERROR);
        } catch (RuntimeException e) {
            deleteStored(fileUri);
            throw e;
        }

        return new SqlScriptMutationVO(script.getId(), 0L, modified);
    }

    @Override
    public SqlScriptMutationVO update(String ownerUid, SqlScriptUpdateFO fo) {
        String name = normalizeName(fo.getName());
        validateDsType(fo.getDsType());
        validateContent(fo.getSqlContent());

        DmExecSqlScriptDO current = requireScript(ownerUid, fo.getScriptId());
        if (!Objects.equals(current.getVersion(), fo.getVersion())) {
            throw error(I18nDmMsgKeys.CONSOLE_QUERY_SCRIPT_VERSION_CONFLICT_ERROR);
        }
        touchStored(current.getFileUri(), fo.getScriptId());
        String fileUri = store(fo.getSqlContent());
        Date modified = currentSecond();
        int updated;
        try {
            updated = this.executionDal.sqlScriptMapper().updateByVersion(fo.getScriptId(), ownerUid, fo.getVersion(), name, fo.getDsType(), fileUri, modified);
        } catch (DuplicateKeyException e) {
            deleteStored(fileUri);
            throw error(I18nDmMsgKeys.CONSOLE_QUERY_SCRIPT_NAME_EXISTS_ERROR);
        } catch (RuntimeException e) {
            deleteStored(fileUri);
            throw e;
        }

        if (updated == 0) {
            deleteStored(fileUri);
            if (this.executionDal.sqlScriptMapper().detail(fo.getScriptId(), ownerUid) == null) {
                throw error(I18nDmMsgKeys.CONSOLE_QUERY_SCRIPT_NOT_EXIST_ERROR);
            }
            throw error(I18nDmMsgKeys.CONSOLE_QUERY_SCRIPT_VERSION_CONFLICT_ERROR);
        }

        return new SqlScriptMutationVO(fo.getScriptId(), fo.getVersion() + 1, modified);
    }

    @Override
    public SqlScriptListVO list(String ownerUid, SqlScriptListFO fo) {
        PageObj pageObj = fo.getPage();
        long pageNumber = 1;
        long pageSize = DEFAULT_PAGE_SIZE;
        if (pageObj != null) {
            pageNumber = Math.max(1, pageObj.getPageNum());
            pageSize = Math.max(1, Math.min(MAX_PAGE_SIZE, pageObj.getPageSize()));
        }

        String keyword = escapeLike(fo.getKeyword());
        IPage<DmExecSqlScriptDO> page = this.executionDal.sqlScriptMapper().listByOwner(new Page<>(pageNumber, pageSize), ownerUid, keyword);
        List<SqlScriptSummaryVO> records = page.getRecords().stream().map(this::toSummary).toList();
        return new SqlScriptListVO(page.getCurrent(), page.getSize(), page.getTotal(), records);
    }

    @Override
    public SqlScriptDetailVO detail(String ownerUid, long scriptId) {
        DmExecSqlScriptDO script = requireScript(ownerUid, scriptId);
        for (int attempt = 0; attempt < 2; attempt++) {
            try {
                SqlScriptDetailVO vo = new SqlScriptDetailVO();
                copySummary(script, vo);
                vo.setSqlContent(this.sqlScriptStorage.read(script.getFileUri()));
                return vo;
            } catch (RuntimeException e) {
                DmExecSqlScriptDO latest = this.executionDal.sqlScriptMapper().detail(scriptId, ownerUid);
                if (latest != null && !Objects.equals(script.getFileUri(), latest.getFileUri())) {
                    script = latest;
                    continue;
                }
                log.error("read SQL script failed, scriptId: {}", scriptId, e);
                throw error(I18nDmMsgKeys.CONSOLE_QUERY_SCRIPT_STORAGE_ERROR);
            }
        }
        throw error(I18nDmMsgKeys.CONSOLE_QUERY_SCRIPT_STORAGE_ERROR);
    }

    @Override
    public void delete(String ownerUid, long scriptId) {
        for (int attempt = 0; attempt < 3; attempt++) {
            DmExecSqlScriptDO script = requireScript(ownerUid, scriptId);
            touchStored(script.getFileUri(), scriptId);
            Date modified = currentSecond();
            if (this.executionDal.sqlScriptMapper().markDeleting(scriptId, ownerUid, script.getVersion(), modified) > 0) {
                return;
            }
        }
        throw error(I18nDmMsgKeys.CONSOLE_QUERY_SCRIPT_VERSION_CONFLICT_ERROR);
    }

    private void cleanup() {
        try {
            Date expireBefore = new Date(System.currentTimeMillis() - FILE_GRACE_MS);
            IPage<DmExecSqlScriptDO> deleting = this.executionDal.sqlScriptMapper().listDeleting(new Page<>(1, CLEAN_BATCH), expireBefore);
            deleting.getRecords().forEach(this::tryDelete);
            Set<String> references = new HashSet<>(this.executionDal.sqlScriptMapper().listFileUris());
            this.sqlScriptStorage.cleanupOrphans(references);
        } catch (Throwable e) {
            log.error("clean SQL scripts failed", e);
        }
    }

    private void tryDelete(DmExecSqlScriptDO script) {
        try {
            this.sqlScriptStorage.delete(script.getFileUri());
            this.executionDal.sqlScriptMapper().deleteDeleting(script.getId());
        } catch (RuntimeException e) {
            log.warn("delete SQL script will be retried, scriptId: {}", script.getId(), e);
        }
    }

    private DmExecSqlScriptDO requireScript(String ownerUid, long scriptId) {
        DmExecSqlScriptDO script = this.executionDal.sqlScriptMapper().detail(scriptId, ownerUid);
        if (script == null) {
            throw error(I18nDmMsgKeys.CONSOLE_QUERY_SCRIPT_NOT_EXIST_ERROR);
        }
        return script;
    }

    private void validateContent(String content) {
        if (content.trim().isEmpty()) {
            throw error(I18nDmMsgKeys.CONSOLE_QUERY_SCRIPT_CONTENT_EMPTY_ERROR);
        }
    }

    private static void validateDsType(String dsType) {
        DataSourceType dataSourceType = DataSourceType.getTypeByName(dsType);
        if (dataSourceType == null || PluginManager.findDsPlugin(dataSourceType) == null) {
            throw error(I18nDmMsgKeys.DS_UNSUPPORTED_ERROR, dsType);
        }
    }

    private static String normalizeName(String name) {
        String normalized = name.trim();
        if (normalized.isEmpty()) {
            throw error(I18nDmMsgKeys.CONSOLE_QUERY_SCRIPT_NAME_EMPTY_ERROR);
        }
        return normalized;
    }

    private String store(String content) {
        try {
            return this.sqlScriptStorage.put(content);
        } catch (RuntimeException e) {
            log.error("store SQL script failed", e);
            throw error(I18nDmMsgKeys.CONSOLE_QUERY_SCRIPT_STORAGE_ERROR);
        }
    }

    private void touchStored(String fileUri, long scriptId) {
        try {
            this.sqlScriptStorage.touch(fileUri);
        } catch (RuntimeException e) {
            log.error("touch SQL script failed, scriptId: {}", scriptId, e);
            throw error(I18nDmMsgKeys.CONSOLE_QUERY_SCRIPT_STORAGE_ERROR);
        }
    }

    private void deleteStored(String fileUri) {
        try {
            this.sqlScriptStorage.delete(fileUri);
        } catch (RuntimeException e) {
            log.warn("compensating SQL script delete failed; orphan cleanup will retry", e);
        }
    }

    private SqlScriptSummaryVO toSummary(DmExecSqlScriptDO script) {
        SqlScriptSummaryVO vo = new SqlScriptSummaryVO();
        copySummary(script, vo);
        return vo;
    }

    private static void copySummary(DmExecSqlScriptDO script, SqlScriptSummaryVO vo) {
        vo.setScriptId(script.getId());
        vo.setName(script.getName());
        vo.setDsType(script.getDsType());
        vo.setVersion(script.getVersion());
        vo.setGmtCreate(script.getGmtCreate());
        vo.setGmtModified(script.getGmtModified());
    }

    private static String escapeLike(String keyword) {
        if (keyword == null || keyword.isBlank()) {
            return null;
        }
        return keyword.trim().replace("=", "==").replace("%", "=%").replace("_", "=_");
    }

    private static ErrorMessageException error(I18nDmMsgKeys key, Object... args) {
        return new ErrorMessageException(DmI18nUtils.getMessage(key.name(), args));
    }

    private static Date currentSecond() {
        return new Date(System.currentTimeMillis() / 1000 * 1000);
    }
}
