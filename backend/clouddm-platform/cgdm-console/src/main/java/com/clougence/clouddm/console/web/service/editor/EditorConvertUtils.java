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
package com.clougence.clouddm.console.web.service.editor;

import java.util.*;
import java.util.stream.Collectors;

import com.clougence.clouddm.base.metadata.ds.DataSourceType;
import com.clougence.clouddm.console.web.model.fo.editor.data.ChangeRowFO;
import com.clougence.clouddm.console.web.model.vo.editor.data.DataEditorColumnVO;
import com.clougence.clouddm.console.web.model.vo.editor.data.DataEditorResultVO;
import com.clougence.clouddm.console.web.service.editor.model.DataEditorResultDTO;
import com.clougence.clouddm.console.web.service.editor.model.DataEditorUpdateDTO;
import com.clougence.clouddm.dsfamily.definition.TypeMapUtils;
import com.clougence.clouddm.sdk.execute.resultset.echo.*;
import com.clougence.clouddm.sdk.ui.editor.data.DataEditorAttributeKeys;
import com.clougence.clouddm.sdk.ui.editor.data.DataEditorColumn;
import com.clougence.clouddm.sdk.ui.editor.data.DataEditorSqlType;
import com.clougence.clouddm.sdk.ui.editor.data.reload.EditorResultSet;
import com.clougence.clouddm.sdk.ui.editor.data.reload.SqlData;
import com.clougence.schema.umi.special.rdb.*;
import com.clougence.schema.umi.struts.UmiTypes;
import com.clougence.schema.umi.struts.constraint.GeneralConstraintType;
import com.clougence.schema.umi.struts.constraint.NonNull;
import com.clougence.schema.umi.struts.constraint.Primary;
import com.clougence.schema.umi.struts.constraint.Unique;
import com.clougence.utils.CollectionUtils;
import com.clougence.utils.StringUtils;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class EditorConvertUtils {

    public static EditorResultSet convertToEditorResultSet(ResultCount resultCount, ResultOut resultOut) {
        EditorResultSet dto = new EditorResultSet();

        dto.setFetchTimeMs(resultCount.getCostTimeMs());
        dto.setResultId(resultCount.getResultId());

        dto.setSuccess(resultCount.isSuccess());
        dto.setMessage(resultCount.getMessage());

        dto.setUpdateCountResult(true);
        dto.setUpdateCount(resultCount.getUpdateCount());
        dto.setGeneratedKeys(resultCount.getGeneratedKeys());

        dto.setSql(resultCount.getQuerySql());
        dto.setResource(resultCount.getResource());
        dto.setColumnList(Collections.emptyList());
        dto.setColumnType(Collections.emptyList());
        dto.setRowSet(Collections.emptyList());

        dto.setOutParams(resultOut.getOutParams());
        return dto;
    }

    public static EditorResultSet convertToEditorResultSet(ResultSet r) {
        EditorResultSet dto = new EditorResultSet();

        dto.setFetchTimeMs(r.getCostTimeMs());
        dto.setResultId(r.getResultId());
        dto.setFetchCount(r.getFetchCount());

        dto.setSuccess(r.isSuccess());
        dto.setMessage(r.getMessage());

        dto.setUpdateCountResult(false);
        dto.setUpdateCount(0);
        //dto.setGeneratedKeys(r.getGeneratedKeys());

        dto.setSql(r.getQuerySql());
        dto.setResource(r.getResource());
        //dto.setColumnList(r.getColumnList());
        //dto.setColumnType(r.getColumnType());
        //dto.setRowSet(r.getRowSet());
        //dto.setCacheFile(r.getCacheFileUri());
        throw new UnsupportedOperationException("convertToEditorResultSet not support ResultSetRows temporarily");
        //return dto;
    }

    public static DataEditorResultDTO convertToDataEditorResultDTO(EditorResultSet result) {
        List<ResultSetRow> rowSet = result.getRowSet();
        if (CollectionUtils.isEmpty(rowSet)) {
            rowSet = new ArrayList<>();
        }
        List<String> columnList = result.getColumnList();

        DataEditorResultDTO resultDto = new DataEditorResultDTO();
        List<Map<String, Object>> resultSet = new ArrayList<>();
        List<Map<String, Long>> resultSetMore = new ArrayList<>();
        for (ResultSetRow ResultSetRow : rowSet) {
            List<ResultSetValue> rowData = ResultSetRow.getData();
            Map<String, Object> resultMap = new HashMap<>();
            Map<String, Long> moreMap = new HashMap<>();
            for (int j = 0; j < columnList.size(); j++) {
                ResultSetValue dto = rowData.get(j);
                resultMap.put(columnList.get(j), dto.getValue());
                moreMap.put(columnList.get(j), dto.getMoreSize());
            }
            resultSet.add(resultMap);
            resultSetMore.add(moreMap);
        }
        resultDto.setResultSet(resultSet);
        resultDto.setResultSetMore(resultSetMore);
        resultDto.setExecuteSuccess(result.isSuccess());
        resultDto.setMessage(messageCut(result.getMessage()));
        resultDto.setInfluenceCount(result.getUpdateCount());
        Map<String, String> outParams = result.getOutParams();
        if (CollectionUtils.isNotEmpty(outParams)) {
            resultDto.setRowId(outParams.get("1"));
        }
        resultDto.setGeneratedKeys(result.getGeneratedKeys());
        return resultDto;
    }

    private static String messageCut(String message) {
        if (StringUtils.isBlank(message) || message.length() < 200) {
            return message;
        } else {
            return message.substring(0, 200) + "...";
        }
    }

    public static DataEditorUpdateDTO convertRenewDataFO2DTO(ChangeRowFO data) {
        Map<String, String> whereData = data.getWhereData();
        Map<String, String> newData = data.getNewData();

        DataEditorUpdateDTO paramDTO = new DataEditorUpdateDTO();
        paramDTO.setSequence(data.getSequence());
        paramDTO.setUpdateData(newData);
        paramDTO.setWhereData(whereData);
        boolean hasNewData = newData != null && !newData.isEmpty();
        boolean hasWhereData = whereData != null && !whereData.isEmpty();
        if (hasNewData && hasWhereData) {
            paramDTO.setDmlType(DataEditorSqlType.UPDATE);
        } else if (hasNewData) {
            paramDTO.setDmlType(DataEditorSqlType.INSERT);
        } else if (hasWhereData) {
            paramDTO.setDmlType(DataEditorSqlType.DELETE);
        }
        return paramDTO;
    }

    public static void convertDataEditorResultDto(List<RdbColumn> columns, DataEditorResultDTO dto, RdbTable rdbTable) {
        boolean existPk = columns.stream().anyMatch(c -> c.hasConstraint(GeneralConstraintType.Primary));
        RdbPrimaryKey primaryKey = rdbTable.getPrimaryKey();
        List<RdbUniqueKey> uniqueKeys = rdbTable.getUniqueKeys();
        Set<String> ukSet = new HashSet<>();
        if (CollectionUtils.isNotEmpty(uniqueKeys)) {
            uniqueKeys.forEach(item -> ukSet.addAll(item.getColumnList()));
        }
        List<DataEditorColumn> basics = columns.stream().map(c -> {
            DataEditorColumn basic = new DataEditorColumn();
            basic.setColumn(c.getName());
            basic.setIsPk(c.hasConstraint(GeneralConstraintType.Primary) || (primaryKey != null && primaryKey.getColumnList().contains(c.getName())));
            basic.setIsUk(c.hasConstraint(GeneralConstraintType.Unique) || ukSet.contains(c.getName()));
            basic.setLength(c.getCharLength());
            basic.setNumericPrecision(c.getNumericPrecision());
            basic.setNumericScale(c.getNumericScale());
            basic.setColumnType(c.getSqlType().getCodeKey());
            basic.setHasDefault(c.getDefaultValue() != null);
            basic.setIsNullable(!c.hasConstraint(GeneralConstraintType.NonNull));
            //auto
            Map<String, String> attrMap = c.getAttributes();
            String auto = attrMap.get(RdbAttributeNames.AUTO_INCREMENT.getCodeKey());
            basic.setAutoincrement(!StringUtils.isBlank(auto) && Boolean.parseBoolean(auto));
            //where key
            basic.setWhereKey(existPk ? basic.getIsPk() : basic.getIsUk());
            return basic;
        }).collect(Collectors.toList());
        dto.setColumnList(basics);
    }

    public static DataEditorResultVO convertResultDTO2VO(DataEditorResultDTO resultDto) {
        DataEditorResultVO vo = new DataEditorResultVO();
        vo.setColumnList(convertColumnDTO2VO(resultDto.getColumnList()));
        vo.setExecuteSuccess(resultDto.getExecuteSuccess());
        vo.setIsFirst(resultDto.getIsFirst());
        vo.setCatalog(resultDto.getCatalog());
        vo.setSchema(resultDto.getSchema());
        vo.setTable(resultDto.getTable());
        vo.setOffset(resultDto.getOffset());
        vo.setHasNext(resultDto.getHasNext());
        vo.setPageSize(resultDto.getPageSize());
        vo.setResultSet(resultDto.getResultSet());
        vo.setMessage(resultDto.getMessage());
        vo.setResultSetMore(resultDto.getResultSetMore());
        return vo;
    }

    public static RdbTable convertColumnVO2DTO(DataSourceType dsType, String catalog, String schema, String table, UmiTypes umiType, List<DataEditorColumnVO> vo) {
        List<RdbColumn> rdbColumns = vo.stream().filter(i -> StringUtils.isNotBlank(i.getColumnType())).map(item -> {
            RdbColumn dto = new RdbColumn();
            dto.setName(item.getColumn());
            dto.setSqlType(TypeMapUtils.findColumnTypes(dsType, item.getColumnType()));
            if (Boolean.TRUE.equals(item.getAutoincrement())) {
                dto.setAttribute(RdbAttributeNames.AUTO_INCREMENT, "true");
            }
            dto.setCharLength(item.getLength());
            if (item.getHasDefault()) {
                dto.setDefaultValue("");
            } else {
                dto.setDefaultValue(null);
            }
            if (!Boolean.TRUE.equals(item.getIsNullable())) {
                dto.addConstraint(new NonNull());
            }
            if (Boolean.TRUE.equals(item.getIsUk())) {
                dto.addConstraint(new Unique());
            }
            if (Boolean.TRUE.equals(item.getIsPk())) {
                dto.addConstraint(new Primary());
            }
            dto.setNumericPrecision(item.getNumericPrecision());
            dto.setNumericScale(item.getNumericScale());
            dto.setAttribute(DataEditorAttributeKeys.AUTOINCREMENT, String.valueOf(item.getAutoincrement()));
            dto.setAttribute(DataEditorAttributeKeys.INSERT_READ_ONLY, String.valueOf(item.getInsertReadOnly()));
            dto.setAttribute(DataEditorAttributeKeys.UPDATE_READ_ONLY, String.valueOf(item.getUpdateReadOnly()));
            dto.setAttribute(DataEditorAttributeKeys.DO_WHERE, String.valueOf(item.getSpareWhere() || item.getWhereKey()));
            return dto;
        }).collect(Collectors.toList());

        RdbTable tableMeta = new RdbTable();
        tableMeta.setCatalog(catalog);
        tableMeta.setSchema(schema);
        tableMeta.setName(table);
        tableMeta.setUmiType(umiType);
        for (RdbColumn col : rdbColumns) {
            tableMeta.addColumn(col);
        }
        return tableMeta;
    }

    public static List<DataEditorColumnVO> convertColumnDTO2VO(List<DataEditorColumn> dto) {
        return dto.stream().map(item -> {
            DataEditorColumnVO vo = new DataEditorColumnVO();
            vo.setColumn(item.getColumn());
            vo.setColumnType(item.getColumnType());
            vo.setAutoincrement(item.getAutoincrement());
            vo.setLength(item.getLength());
            vo.setIsPk(item.getIsPk());
            vo.setIsUk(item.getIsUk());
            vo.setHasDefault(item.getHasDefault());
            vo.setUiType(item.getUiType());
            vo.setCheck(item.getCheck());
            vo.setUpdateReadOnly(item.getUpdateReadOnly());
            vo.setInsertReadOnly(item.getInsertReadOnly());
            vo.setHide(item.getHide());
            vo.setIsNullable(item.getIsNullable());
            vo.setNumericPrecision(item.getNumericPrecision());
            vo.setNumericScale(item.getNumericScale());
            vo.setOption(item.getOption());
            vo.setSpareWhere(item.getSpareWhere());
            vo.setWhereKey(item.getWhereKey());
            return vo;
        }).collect(Collectors.toList());
    }

    public static SqlData convertDTO2SqlData(DataEditorUpdateDTO dto) {
        SqlData sqlData = new SqlData();
        sqlData.setWhereData(dto.getWhereData());
        sqlData.setUpdateData(dto.getUpdateData());
        sqlData.setDmlType(dto.getDmlType());
        return sqlData;
    }
}
