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

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.clougence.clouddm.api.common.exception.ErrorMessageException;
import com.clougence.clouddm.console.web.component.schema.DsSchemaService;
import com.clougence.clouddm.console.web.global.i18n.DmI18nUtils;
import com.clougence.clouddm.console.web.global.i18n.I18nDmMsgKeys;
import com.clougence.clouddm.platform.dal.access.DataSourceDal;
import com.clougence.clouddm.platform.dal.model.datasource.DmDsDO;
import com.clougence.clouddm.sdk.service.execute.MetaCol;
import com.clougence.clouddm.sdk.service.execute.MetaService;
import com.clougence.schema.umi.special.rdb.RdbColumn;
import com.clougence.schema.umi.special.rdb.RdbTable;
import com.clougence.schema.umi.struts.UmiTypes;
import com.clougence.schema.umi.struts.Value;

import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class ConsoleMetaServiceImpl implements MetaService {

    @Resource
    private DataSourceDal   dsDal;
    @Resource
    private DsSchemaService dsSchemaService;

    @Override
    public List<MetaCol> fetchTableColumns(String uid, long dsId, Map<UmiTypes, Object> levelsParam, String tableName, int tableId) {
        DmDsDO dsDO = dsDal.dsMapper().queryDsIdentityById(dsId);
        Value value = dsSchemaService.fetchSelectObject(uid, dsDO, levelsParam, tableName);
        if (value == null) {
            throw new ErrorMessageException(DmI18nUtils.getMessage(I18nDmMsgKeys.DS_TABLE_NOT_EXIST_ERROR.name(), tableName));
        }

        RdbTable rdbTable = (RdbTable) value;
        return rdbTable.getColumns().values().stream().sorted(Comparator.comparingInt(RdbColumn::getIndex)).map(this::convertToMetaCol).collect(Collectors.toList());
    }

    private MetaCol convertToMetaCol(RdbColumn rdbColumn) {
        MetaCol metaCol = new MetaCol();
        metaCol.setCatalog(rdbColumn.getCatalog());
        metaCol.setSchema(rdbColumn.getSchema());
        metaCol.setTable(rdbColumn.getTable());
        metaCol.setColumn(rdbColumn.getName());
        return metaCol;
    }

}
