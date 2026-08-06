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
package com.clougence.clouddm.platform.dal.access.impl;

import java.io.*;
import java.nio.charset.StandardCharsets;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import com.clougence.clouddm.platform.dal.access.ChangeFlowDal;
import com.clougence.clouddm.platform.dal.mapper.cicd.*;
import com.clougence.clouddm.platform.dal.mapper.gitops.DmGitOpsScmMapper;
import com.clougence.clouddm.platform.dal.model.cicd.ChangeItemType;

import jakarta.annotation.Resource;

@Service
public class ChangeFlowDalImpl implements ChangeFlowDal {
    @Resource
    private DmChangeFlowMapper           flowMapper;
    @Resource
    private DmChangeFlowItemMapper       flowItemMapper;
    @Resource
    private DmChangeMapper               changeMapper;
    @Resource
    private DmChangeItemMapper           changeItemMapper;
    @Resource
    private DmChangeVersionMapper        versionMapper;
    @Resource
    private DmChangeTriggerReceiptMapper triggerReceiptMapper;
    @Resource
    private DmGitOpsScmMapper            scmMapper;
    @Resource
    private JdbcTemplate                 jdbcTemplate;

    @Override
    public DmChangeFlowMapper flowMapper() {
        return flowMapper;
    }

    @Override
    public DmChangeFlowItemMapper flowItemMapper() {
        return flowItemMapper;
    }

    @Override
    public DmChangeMapper changeMapper() {
        return changeMapper;
    }

    @Override
    public DmChangeItemMapper changeItemMapper() {
        return changeItemMapper;
    }

    @Override
    public DmChangeVersionMapper versionMapper() {
        return versionMapper;
    }

    @Override
    public DmChangeTriggerReceiptMapper triggerReceiptMapper() {
        return triggerReceiptMapper;
    }

    @Override
    public DmGitOpsScmMapper scmMapper() {
        return scmMapper;
    }

    @Override
    public boolean readChangeItemContent(String ownerUid, long changeId, ChangeItemType itemType, OutputStream output) {
        Boolean found = this.jdbcTemplate.query("""
                select content
                from dm_change_item
                where owner_uid = ? and ref_change_id = ? and ref_change_item_type = ?
                order by content_index asc
                limit 1
                """, ps -> {
            ps.setString(1, ownerUid);
            ps.setLong(2, changeId);
            ps.setString(3, itemType.name());
        }, rs -> {
            if (!rs.next()) {
                return false;
            }
            try (Reader reader = rs.getCharacterStream(1)) {
                Writer writer = new OutputStreamWriter(output, StandardCharsets.UTF_8);
                reader.transferTo(writer);
                writer.flush();
            } catch (IOException e) {
                throw new UncheckedIOException(e);
            }
            return true;
        });
        return Boolean.TRUE.equals(found);
    }

}
