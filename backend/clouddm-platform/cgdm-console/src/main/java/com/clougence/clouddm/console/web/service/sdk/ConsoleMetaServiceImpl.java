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
package com.clougence.clouddm.console.web.service.sdk;

import java.util.*;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.clougence.clouddm.api.common.exception.ErrorMessageException;
import com.clougence.clouddm.console.web.component.auth.DmAuthServiceForBiz;
import com.clougence.clouddm.console.web.component.auth.DmResAuthService;
import com.clougence.clouddm.console.web.component.auth.model.ResourceAccessInfo;
import com.clougence.clouddm.console.web.component.dsconfig.mode.DsLevels;
import com.clougence.clouddm.console.web.component.schema.DsSchemaService;
import com.clougence.clouddm.console.web.global.i18n.DmI18nUtils;
import com.clougence.clouddm.console.web.global.i18n.I18nDmMsgKeys;
import com.clougence.clouddm.console.web.util.DmConvertUtils;
import com.clougence.clouddm.platform.dal.access.DataSourceDal;
import com.clougence.clouddm.platform.dal.model.datasource.DmDsDO;
import com.clougence.clouddm.sdk.execute.meta.DsElement;
import com.clougence.clouddm.sdk.security.auth.AuthKind;
import com.clougence.clouddm.sdk.security.auth.def.SecDataAuthLabel;
import com.clougence.clouddm.sdk.service.execute.MetaCol;
import com.clougence.clouddm.sdk.service.execute.MetaObj;
import com.clougence.clouddm.sdk.service.execute.MetaService;
import com.clougence.schema.umi.special.rdb.RdbColumn;
import com.clougence.schema.umi.special.rdb.RdbPrimaryKey;
import com.clougence.schema.umi.special.rdb.RdbTable;
import com.clougence.schema.umi.struts.UmiTypes;
import com.clougence.schema.umi.struts.Value;
import com.clougence.utils.CollectionUtils;
import com.clougence.utils.StringUtils;

import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class ConsoleMetaServiceImpl implements MetaService {

    @Resource
    private DataSourceDal       dsDal;
    @Resource
    private DsSchemaService     dsSchemaService;
    @Resource
    private DmAuthServiceForBiz authServiceForBiz;
    @Resource
    private DmResAuthService    resAuthService;

    @Override
    public List<MetaCol> fetchTableColumns(String uid, long dsId, Map<UmiTypes, Object> levelsParam, String tableName) {
        DmDsDO dsDO = dsDal.dsMapper().queryDsIdentityById(dsId);
        Value value = dsSchemaService.realTimeFetchSelectObject(uid, dsDO, levelsParam, tableName);
        if (value == null) {
            throw new ErrorMessageException(DmI18nUtils.getMessage(I18nDmMsgKeys.DS_TABLE_NOT_EXIST_ERROR.name(), tableName));
        }

        RdbTable rdbTable = (RdbTable) value;
        RdbPrimaryKey primaryKey = rdbTable.getPrimaryKey();
        List<String> keyCols = primaryKey == null ? Collections.emptyList() : primaryKey.getColumnList();
        List<String> ukCols = notNullList(rdbTable.getUniqueKeys()).stream().flatMap(key -> key.getColumnList().stream()).collect(Collectors.toList());
        List<String> idxCols = notNullList(rdbTable.getIndices()).stream().flatMap(index -> index.getColumnList().stream()).collect(Collectors.toList());
        List<String> fkCols = notNullList(rdbTable.getForeignKeys()).stream().flatMap(key -> key.getColumnList().stream()).collect(Collectors.toList());
        return rdbTable.getColumns()
            .values()
            .stream()
            .sorted(Comparator.comparingInt(RdbColumn::getIndex))
            .map(column -> this.convertToMetaCol(column, keyCols, idxCols, ukCols, fkCols))
            .collect(Collectors.toList());
    }

    @Override
    public List<MetaObj> cachedObjectNames(String puid, String uid, long dsId, List<UmiTypes> levels, Map<UmiTypes, Object> levelsParam) {
        DmDsDO dsDO = this.dsDal.dsMapper().queryDsIdentityById(dsId);
        if (dsDO == null) {
            throw new ErrorMessageException(DmI18nUtils.getMessage(I18nDmMsgKeys.DS_NOT_EXIST_ERROR.name()));
        }

        DsLevels dsLevels = toDsLevels(dsDO, levels, levelsParam);
        this.authServiceForBiz.checkBrowseAuth(puid, uid, dsId, AuthKind.DataSource, dsLevels.asResPath(), SecDataAuthLabel.DM_DAUTH_QUERY);

        List<DsElement> elements = this.dsSchemaService.cachedObjectNames(puid, dsDO, levels, levelsParam);
        if (CollectionUtils.isEmpty(elements)) {
            return Collections.emptyList();
        }

        ResourceAccessInfo accessInfo = this.resAuthService.getAllowBrowseInfo(dsLevels, uid);
        if (accessInfo.isAllAllow()) {
            return elements.stream().map(this::convertToMetaObj).collect(Collectors.toList());
        }
        return elements.stream().filter(element -> {
            return accessInfo.getAllowQueryList() != null && accessInfo.getAllowQueryList().contains(element.getObjName());
        }).map(this::convertToMetaObj).collect(Collectors.toList());
    }

    private MetaCol convertToMetaCol(RdbColumn rdbColumn, List<String> keyCols, List<String> idxCols, List<String> ukCols, List<String> fkCols) {
        MetaCol metaCol = new MetaCol();
        metaCol.setCatalog(rdbColumn.getCatalog());
        metaCol.setSchema(rdbColumn.getSchema());
        metaCol.setTable(rdbColumn.getTable());
        metaCol.setColumn(rdbColumn.getName());
        var browseColumn = DmConvertUtils.convertToBrowseColumnMOTipsType(rdbColumn, keyCols, idxCols, ukCols, fkCols);
        metaCol.setIcon(DmConvertUtils.convertToBrowseColumnIcon(browseColumn));
        return metaCol;
    }

    private static <T> List<T> notNullList(List<T> list) {
        return CollectionUtils.isEmpty(list) ? Collections.emptyList() : list;
    }

    private MetaObj convertToMetaObj(DsElement element) {
        MetaObj metaObj = new MetaObj();
        metaObj.setType(element.getObjType());
        metaObj.setName(element.getObjName());
        return metaObj;
    }

    private static DsLevels toDsLevels(DmDsDO dsDO, List<UmiTypes> levels, Map<UmiTypes, Object> levelsParam) {
        List<String> levelValues = new ArrayList<>();
        levelValues.add(String.valueOf(dsDO.getDsEnvId()));
        levelValues.add(String.valueOf(dsDO.getId()));

        List<String> dbLevels = new ArrayList<>();
        List<UmiTypes> levelDefs = new ArrayList<>();
        if (levels != null) {
            for (UmiTypes level : levels) {
                String value = StringUtils.toString(levelsParam == null ? null : levelsParam.get(level));
                if (StringUtils.isBlank(value)) {
                    continue;
                }

                levelValues.add(value);
                dbLevels.add(value);
                levelDefs.add(level);
            }
        }

        return new DsLevels(String.valueOf(dsDO.getDsEnvId()), dsDO, levelValues, dbLevels, levelDefs, levelsParam);
    }
}
