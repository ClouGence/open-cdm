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
package com.clougence.clouddm.ds.hana.definition.ui.ddl;

import com.clougence.adapter.hana.HanaAttributeNames;
import com.clougence.clouddm.base.metadata.ds.DataSourceType;
import com.clougence.clouddm.ds.hana.definition.ui.editor.table.HanaCreateUtils;
import com.clougence.clouddm.ds.hana.definition.ui.editor.table.HanaEditorProvider;
import com.clougence.clouddm.sdk.model.exception.ThirdPartyApiException;
import com.clougence.clouddm.sdk.ui.ddl.ConvertTableDDLSpi;
import com.clougence.clouddm.sdk.ui.ddl.DDLType;
import com.clougence.schema.DsType;
import com.clougence.schema.editor.EditorContext;
import com.clougence.schema.editor.TableEditor;
import com.clougence.schema.editor.builder.TableEditorImpl;
import com.clougence.schema.editor.builder.actions.Action;
import com.clougence.schema.editor.domain.EColumn;
import com.clougence.schema.editor.domain.ETable;
import com.clougence.schema.editor.provider.SqlBuilder;
import com.clougence.schema.editor.triggers.TriggerContext;
import com.clougence.utils.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author chunlin
 * @date 2024/4/2
 */
public class HanaConvertTableDDLSpi implements ConvertTableDDLSpi {

    private static final List<DataSourceType> targetList  = new ArrayList<>();
    private static final List<DDLType>        ddlTypeList = new ArrayList<>();
    static {
        // targetList
        targetList.add(DataSourceType.MySQL);
        targetList.add(DataSourceType.Doris);
        targetList.add(DataSourceType.Hana);
        targetList.add(DataSourceType.AdbForMySQL);
        targetList.add(DataSourceType.OceanBase);
        targetList.add(DataSourceType.StarRocks);
        targetList.add(DataSourceType.TiDB);

        // ddlType
        ddlTypeList.add(DDLType.Convert);
    }

    @Override
    public List<String> convertDDL(TableEditor sourceEditor, SqlBuilder targetSqlBuilder) {
        ETable table = sourceEditor.getSource().clone();
        table.setSchema(null);
        table.setCatalog(null);
        if (targetSqlBuilder.getDataSourceType() == DsType.Hana) {
            if (!table.getConstraints().isEmpty()) {
                throw ThirdPartyApiException.as().with(new IllegalArgumentException("Use native DDL to preserve HANA check constraints"));
            }

            TriggerContext context = new TriggerContext();
            context.setUseDelimited(true);
            // Same-dialect DDL must not run migration handlers that drop/reorder keys or columns.
            return new HanaCreateUtils().buildCreate(context, table);
        } else {
            validateConversion(table, targetSqlBuilder.getDataSourceType());
            EditorContext context = new EditorContext(HanaEditorProvider.INSTANCE);
            context.setUseDelimited(true);
            TableEditor editor = new TableEditorImpl(table, context);
            List<Action> actions = editor.buildCreate(targetSqlBuilder, null);
            List<String> actionScripts = actions.stream().flatMap((Function<Action, Stream<String>>) action -> action.getSqlString().stream()).collect(Collectors.toList());

            return actionScripts.isEmpty() ? new ArrayList<>() : actionScripts;
        }
    }

    private void validateConversion(ETable table, DsType target) {
        if (!table.getForeignKeys().isEmpty() || !table.getConstraints().isEmpty()) {
            throw ThirdPartyApiException.as().with(new IllegalArgumentException("HANA DDL conversion cannot preserve foreign/check constraints for " + target));
        }

        if (target == DsType.Doris || target == DsType.StarRocks || target == DsType.AdbForMySQL) {
            if ((table.getPrimaryKey() != null && !table.getPrimaryKey().getColumnList().isEmpty()) || !table.getIndices().isEmpty()) {
                throw ThirdPartyApiException.as().with(new IllegalArgumentException("HANA DDL conversion cannot preserve keys/indexes for " + target));
            }
        }

        for (EColumn column : table.getColumnList()) {
            if (column.isAutoGenerate() || StringUtils.isNotBlank(HanaAttributeNames.GENERATION_TYPE.getValue(column.getAttribute()))) {
                throw ThirdPartyApiException.as().with(new IllegalArgumentException("HANA generated column requires an explicit target definition: " + column.getName()));
            }
            if ("SMALLDECIMAL".equalsIgnoreCase(column.getDbType()) || ("DECIMAL".equalsIgnoreCase(column.getDbType()) && column.getNumericPrecision() == null)) {
                throw ThirdPartyApiException.as().with(new IllegalArgumentException("HANA floating decimal requires an explicit target precision: " + column.getName()));
            }
            if (target == DsType.AdbForMySQL && column.getLength() != null) {
                throw ThirdPartyApiException.as().with(new IllegalArgumentException("HANA DDL conversion cannot preserve column length for " + target + ": " + column.getName()));
            }
        }
    }

    @Override
    public List<DataSourceType> convertDDLTargetList() {
        return targetList;
    }

    @Override
    public List<DDLType> ddlTypeList() {
        return ddlTypeList;
    }
}
