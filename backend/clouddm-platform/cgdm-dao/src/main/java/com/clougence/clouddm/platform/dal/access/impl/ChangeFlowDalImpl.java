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
import java.util.*;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import com.clougence.clouddm.platform.dal.access.ChangeFlowDal;
import com.clougence.clouddm.platform.dal.mapper.cicd.*;
import com.clougence.clouddm.platform.dal.mapper.gitops.DmGitOpsScmMapper;
import com.clougence.clouddm.platform.dal.model.cicd.ChangeItemType;
import com.clougence.clouddm.platform.dal.model.cicd.DmChangeItemDO;

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
    private DmChangeBatchMapper          batchMapper;
    @Resource
    private DmChangeTransferMapper       transferMapper;
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
    public DmChangeBatchMapper batchMapper() {
        return batchMapper;
    }

    @Override
    public DmChangeTransferMapper transferMapper() {
        return transferMapper;
    }

    @Override
    public DmGitOpsScmMapper scmMapper() {
        return scmMapper;
    }

    @Override
    public List<DmChangeItemDO> queryChangedItemMeta(String ownerUid, long flowId, long changeId) {
        List<DmChangeItemDO> currentItems = changeItemMapper.queryChangeItemByChangeId(ownerUid, changeId, ChangeItemType.SQL);
        List<DmChangeItemDO> baselineItems = changeItemMapper.queryBaselineItemByFlowId(ownerUid, flowId, changeId);

        // Match the current change against the release flow baseline by SQL file name.
        Map<String, DmChangeItemDO> baselineByName = new LinkedHashMap<>();
        for (DmChangeItemDO baseline : baselineItems) {
            baselineByName.put(baseline.getContentName(), baseline);
        }

        // Treat new files and files whose content differs from the baseline as changed items.
        List<DmChangeItemDO> changedItems = new ArrayList<>();
        for (DmChangeItemDO current : currentItems) {
            DmChangeItemDO baseline = baselineByName.remove(current.getContentName());
            if (baseline == null || !Objects.equals(baseline.getContent(), current.getContent())) {
                changedItems.add(current);
            }
        }

        // Baseline files left unmatched were deleted from the current version and remain part of the diff metadata.
        changedItems.addAll(baselineByName.values());
        changedItems.sort(Comparator.comparingInt(DmChangeItemDO::getContentIndex));
        return changedItems;
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
