package com.clougence.clouddm.console.web.service.sdk;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.clougence.clouddm.console.web.component.schema.DsSchemaService;
import com.clougence.clouddm.console.web.constants.I18nDmMsgKeys;
import com.clougence.clouddm.console.web.util.DmI18nUtils;
import com.clougence.clouddm.sdk.service.execute.MetaCol;
import com.clougence.clouddm.sdk.service.execute.MetaService;
import com.clougence.rdp.dal.mapper.RdpDataSourceMapper;
import com.clougence.rdp.dal.model.RdpDataSourceDO;
import com.clougence.rdp.global.exception.ErrorMessageException;
import com.clougence.schema.umi.special.rdb.RdbColumn;
import com.clougence.schema.umi.special.rdb.RdbTable;
import com.clougence.schema.umi.struts.UmiTypes;
import com.clougence.schema.umi.struts.Value;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class ConsoleMetaServiceImpl implements MetaService {

    @Resource
    private DsSchemaService     dsSchemaService;
    @Resource
    private RdpDataSourceMapper rdpDataSourceMapper;

    @Override
    public List<MetaCol> fetchTableColumns(String uid, long dsId, Map<UmiTypes, Object> levelsParam, String tableName, int tableId) {
        RdpDataSourceDO dsDO = rdpDataSourceMapper.queryDsIdentityById(dsId);
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
