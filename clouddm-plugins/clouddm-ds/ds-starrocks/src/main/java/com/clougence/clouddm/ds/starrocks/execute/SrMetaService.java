package com.clougence.clouddm.ds.starrocks.execute;

import java.sql.Connection;
import java.util.List;
import java.util.Map;

import com.clougence.clouddm.ds.starrocks.definition.ui.editor.table.SrEditorProvider;
import com.clougence.clouddm.ds.starrocks.definition.ui.editor.table.SrParserUtil;
import com.clougence.clouddm.dsfamily.mysql.execute.MyMetaService;
import com.clougence.clouddm.sdk.execute.session.Session;
import com.clougence.clouddm.sdk.execute.session.rdb.DmRdbUmiService;
import com.clougence.schema.editor.EditorHelperDm;
import com.clougence.schema.editor.domain.ETable;
import com.clougence.schema.editor.provider.SqlBuilder;
import com.clougence.schema.umi.service.RdbUmiServiceDm;
import com.clougence.schema.umi.special.rdb.RdbTable;
import com.clougence.schema.umi.struts.UmiTypes;
import com.clougence.schema.umi.struts.Value;
import com.clougence.utils.CollectionUtils;
import com.clougence.utils.ExceptionUtils;
import com.clougence.utils.StringUtils;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;

/**
 * @Author: Ekko
 * @Date: 2023-08-30 11:28
 */
@Slf4j
public class SrMetaService extends MyMetaService {

    public SrMetaService(Session rdbSession){
        super(rdbSession);
    }

    @Override
    protected DmRdbUmiService rdbUmiService(Connection con) {
        return new SrUmiServiceDm(con);
    }

    @Override
    protected SqlBuilder getSqlBuilder() { return SrEditorProvider.INSTANCE; }

    @Override
    public String loadTableEditor(Map<UmiTypes, Object> levelsParam, String table) {
        try {
            return this.rdbSession.executeQuery(con -> {
                RdbUmiServiceDm umiService = rdbUmiService(con);
                SqlBuilder sqlBuilder = getSqlBuilder();
                EditorHelperDm editorHelper = new EditorHelperDm(umiService, sqlBuilder);

                String catalog = StringUtils.toString(levelsParam.get(UmiTypes.Catalog));
                String schema = StringUtils.toString(levelsParam.get(UmiTypes.Schema));
                ETable eTable = new ETable();
                if (StringUtils.isBlank(table)) {
                    return null;
                }
                List<String> createTable = showCreateTable(con, schema, table);
                if (CollectionUtils.isNotEmpty(createTable)) {
                    RdbTable rdbTable = SrParserUtil.parser(createTable.get(0));
                    eTable = editorHelper.loadTableByParser(catalog, schema, table, rdbTable);
                }

                return new ObjectMapper().writeValueAsString(eTable);
            });
        } catch (Exception e) {
            String msg = "loadTableEditor error.msg:" + ExceptionUtils.getRootCauseMessage(e);
            log.error(msg, e);
            throw new RuntimeException(msg, e);
        }
    }

    @Override
    public Value detailLeaf(Map<UmiTypes, Object> levelsParam, UmiTypes leafType, String leafName) {
        try {
            return this.rdbSession.executeQuery(con -> {
                if (leafType.equals(UmiTypes.Table)) {
                    String schema = StringUtils.toString(levelsParam.get(UmiTypes.Schema));
                    if (StringUtils.isBlank(leafName)) {
                        return null;
                    }
                    List<String> createTable = showCreateTable(con, schema, leafName);
                    if (CollectionUtils.isNotEmpty(createTable)) {
                        return SrParserUtil.parser(createTable.get(0));
                    }
                }
                return rdbUmiService(con).detailLeaf(levelsParam, leafType, leafName);
            });
        } catch (Exception e) {
            String msg = "detailLeaf error.msg:" + ExceptionUtils.getRootCauseMessage(e);
            log.error(msg, e);
            throw new RuntimeException(msg, e);
        }
    }

}
