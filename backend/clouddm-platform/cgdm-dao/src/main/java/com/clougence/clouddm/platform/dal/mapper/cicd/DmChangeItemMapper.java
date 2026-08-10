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
package com.clougence.clouddm.platform.dal.mapper.cicd;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.clougence.clouddm.platform.dal.model.cicd.ChangeItemType;
import com.clougence.clouddm.platform.dal.model.cicd.DmChangeItemDO;

public interface DmChangeItemMapper extends BaseMapper<DmChangeItemDO> {
    List<DmChangeItemDO> queryChangeItemByChangeId(String ownerUid, long refChangeId, ChangeItemType itemType);

    List<DmChangeItemDO> queryChangeItemByChangeIds(String ownerUid, Collection<Long> refChangeIds, ChangeItemType itemType);

    List<DmChangeItemDO> queryChangeItemMetaByChangeId(String ownerUid, long refChangeId, ChangeItemType itemType);

    DmChangeItemDO queryChangeItemByName(String ownerUid, long refChangeId, ChangeItemType itemType, String contentName);

    List<DmChangeItemDO> queryBaselineItemByFlowId(String ownerUid, long flowId, long changeId);

    default List<DmChangeItemDO> queryChangedItemMeta(String ownerUid, long flowId, long changeId) {
        List<DmChangeItemDO> currentItems = queryChangeItemByChangeId(ownerUid, changeId, ChangeItemType.SQL);
        List<DmChangeItemDO> baselineItems = queryBaselineItemByFlowId(ownerUid, flowId, changeId);

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

    int deleteByChangeItemType(String ownerUid, long refChangeId, ChangeItemType itemType);

    int deleteByChangeItemAll(String ownerUid, long refChangeId);
}
