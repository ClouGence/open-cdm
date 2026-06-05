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
package com.clougence.clouddm.ds.doris.execute;

import java.sql.Connection;
import java.util.List;
import java.util.Map;

import com.clougence.clouddm.ds.doris.definition.ui.editor.table.DrEditorProvider;
import com.clougence.clouddm.ds.doris.definition.ui.editor.table.DrParserUtil;
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
* @author Ekko
* @date 2023/3/28 14:13
*/
@Slf4j
public class DrMetaService extends MyMetaService {

    public DrMetaService(Session rdbSession){
        super(rdbSession);
    }

    @Override
    protected DmRdbUmiService rdbUmiService(Connection con) {
        return new DrUmiServiceDm(con);
    }

    @Override
    protected SqlBuilder getSqlBuilder() { return DrEditorProvider.INSTANCE; }

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
                    RdbTable rdbTable = DrParserUtil.parseTable(createTable.get(0));
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
            if (levelsParam.get(UmiTypes.Catalog).equals("internal")) {
                return this.rdbSession.executeQuery(con -> {
                    if (leafType.equals(UmiTypes.Table)) {
                        String schema = StringUtils.toString(levelsParam.get(UmiTypes.Schema));
                        if (StringUtils.isBlank(leafName)) {
                            return null;
                        }
                        List<String> createTable = showCreateTable(con, schema, leafName);
                        if (CollectionUtils.isNotEmpty(createTable)) {
                            return DrParserUtil.parseTable(createTable.get(0));
                        }
                    }
                    return rdbUmiService(con).detailLeaf(levelsParam, leafType, leafName);
                });
            } else {
                return super.detailLeaf(levelsParam, leafType, leafName);
            }

        } catch (Exception e) {
            String msg = "detailLeaf error.msg:" + ExceptionUtils.getRootCauseMessage(e);
            log.error(msg, e);
            throw new RuntimeException(msg, e);
        }
    }
}
