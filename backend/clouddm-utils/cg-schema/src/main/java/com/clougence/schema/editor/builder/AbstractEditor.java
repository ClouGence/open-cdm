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
package com.clougence.schema.editor.builder;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.clougence.schema.DsType;
import com.clougence.schema.SchemaFramework;
import com.clougence.schema.editor.EditorContext;
import com.clougence.schema.editor.TableEditor;
import com.clougence.schema.editor.builder.actions.Action;
import com.clougence.schema.editor.builder.actions.ExecuteGenerateSql;
import com.clougence.schema.editor.builder.mappings.DefaultMappingHandler;
import com.clougence.schema.editor.builder.mappings.MappingHandler;
import com.clougence.schema.editor.domain.EColumn;
import com.clougence.schema.editor.domain.ETable;
import com.clougence.schema.editor.provider.SqlBuilder;
import com.clougence.schema.editor.triggers.TriggerContext;
import com.clougence.schema.handlers.*;

/**
 * @author mode 2021/5/21 19:56
 */
public abstract class AbstractEditor {

    protected final EditorContext context;
    protected List<Action>        actions;

    AbstractEditor(EditorContext context){
        this.context = context;
        this.actions = context.getActions();
    }

    public boolean useDelimited() {
        return this.context.isUseDelimited();
    }

    protected TriggerContext buildContext(DsInfo srcDsInfo, DsInfo tarDsInfo) {
        TriggerContext buildContext = new TriggerContext();
        buildContext.setSrcDsInfo(srcDsInfo);
        buildContext.setTarDsInfo(tarDsInfo);

        buildContext.setSkipHandlers(this.context.isSkipHandlers());
        buildContext.setUseDelimited(this.context.isUseDelimited());
        buildContext.setCascade(this.context.isCascade());
        buildContext.setMappingHandler(new DefaultMappingHandler(srcDsInfo.getDsType(), tarDsInfo.getDsType(), tarDsInfo.getMainVersion()));
        buildContext.merge(this.context);
        return buildContext;
    }

    // -------------------------------------------------------------------------------------------------------------------------- Attr

    protected abstract Map<String, String> attrMap();

    public String getAttr(String attrKey) {
        return this.attrMap().get(attrKey);
    }

    public void removeAttr(String attrKey) {
        this.attrMap().remove(attrKey);
    }

    public void addAttr(String attrKey, String attrValue) {
        this.attrMap().put(attrKey, attrValue);
    }

    public void addAttrToContext(String attrKey, String attrValue) {
        this.context.setAttribute(attrKey, attrValue);
    }

    // -------------------------------------------------------------------------------------------------------------------------- Table
    protected void triggerTableRename(DsInfo srcDsInfo, DsInfo tarDsInfo, String catalog, String schema, String table, String newName) {
        TriggerContext context = buildContext(srcDsInfo, tarDsInfo);
        SqlBuilder provider = context.getTarDsInfo().getProvider();

        List<String> sqlString = provider.tableRename(context, catalog, schema, table, newName);
        this.actions.add(new Action(sqlString, catalog, schema, table));
    }

    protected void triggerTableCreate(DsInfo srcDsInfo, DsInfo tarDsInfo, String catalog, String schema, String table, ETable eTable) {
        TriggerContext triggerContext = buildContext(srcDsInfo, tarDsInfo);

        ETable cloneTable = eTable.clone();

        processTableHandlers(triggerContext, cloneTable);

        List<String> sqlScript = triggerContext.getTarDsInfo().getProvider().tableCreate(triggerContext, catalog, schema, table, cloneTable);

        List<String> finalScript = new ArrayList<>(sqlScript);
        this.actions.add(new Action(finalScript, catalog, schema, table));
    }

    protected void processAndTriggerColumnBuild(DsInfo srcDsInfo, DsInfo tarDsInfo, ETable eTable, ExecuteGenerateSql executeGenerateSql) {
        TriggerContext triggerContext = buildContext(srcDsInfo, tarDsInfo);

        TableEditor tableEditor = restoreTableEditor(eTable, this.context.clone());

        List<String> columnList = eTable.getColumnList().stream().map(EColumn::getName).collect(Collectors.toList());
        for (String columnName : columnList) {
            EColumn eColumn = tableEditor.getColumn(columnName).getSource();
            processColumnHandlers(triggerContext, eColumn, tableEditor);
            if (tableEditor.getColumn(eColumn.getName()) == null) {
                continue;
            }
            List<String> sqlString = executeGenerateSql.execute(tarDsInfo, triggerContext, eTable, eColumn);
            this.actions.add(new Action(sqlString, eTable.getCatalog(), eTable.getSchema(), eTable.getName()));
        }
    }

    public static TableEditor restoreTableEditor(ETable eTable, EditorContext editorContext) {
        return new TableEditorImpl(eTable, editorContext);
    }

    public static TableEditor restoreTableEditor(ETable eTable, TriggerContext triggerContext) {
        return new TableEditorImpl(eTable, triggerContext.toEditorContext());
    }

    private void processTableHandlers(TriggerContext context, ETable eTable) {
        DsInfo srcDsInfo = context.getSrcDsInfo();
        DsInfo tarDsInfo = context.getTarDsInfo();
        Key[] handlerKeys = KeyUtils.buildMappingKey(srcDsInfo.getDsType(), tarDsInfo.getDsType(), srcDsInfo.getMainVersion());
        List<TableBeforeMappingHandler> beforeTableHandler = SchemaFramework.getHandlers(handlerKeys, TableBeforeMappingHandler.class);
        List<TableAfterMappingHandler> afterTableHandler = SchemaFramework.getHandlers(handlerKeys, TableAfterMappingHandler.class);

        TableEditor editor = restoreTableEditor(eTable, context);

        if (!context.isSkipHandlers()) {
            for (TableBeforeMappingHandler handler : beforeTableHandler) {
                handler.beforeMapping(editor, context, //
                        srcDsInfo.getDsType(), srcDsInfo.getMainVersion(), tarDsInfo.getDsType(), tarDsInfo.getMainVersion());
            }
        }

        List<String> columnList = eTable.getColumnList().stream().map(EColumn::getName).collect(Collectors.toList());
        for (String columnName : columnList) {
            EColumn eColumn = editor.getColumn(columnName).getSource();
            processColumnHandlers(context, eColumn, editor);
        }

        if (!context.isSkipHandlers()) {
            for (TableAfterMappingHandler handler : afterTableHandler) {
                handler.afterMapping(editor, context, //
                        srcDsInfo.getDsType(), srcDsInfo.getMainVersion(), tarDsInfo.getDsType(), tarDsInfo.getMainVersion());
            }
        }
    }

    private void processColumnHandlers(TriggerContext context, EColumn eColumn, TableEditor tableEditor) {
        DsInfo srcDsInfo = context.getSrcDsInfo();
        DsInfo tarDsInfo = context.getTarDsInfo();
        Key[] handlerKeys = KeyUtils.buildMappingKey(srcDsInfo.getDsType(), tarDsInfo.getDsType(), srcDsInfo.getMainVersion());
        TableEditor.ColumnEditor columnEditor = tableEditor.getColumn(eColumn.getName());

        if (!context.isSkipHandlers()) {
            List<ColumnBeforeMappingHandler> beforeColumnHandler = SchemaFramework.getHandlers(handlerKeys, ColumnBeforeMappingHandler.class);
            for (ColumnBeforeMappingHandler handler : beforeColumnHandler) {
                handler.beforeMapping(tableEditor, columnEditor, context, srcDsInfo.getDsType(), srcDsInfo.getMainVersion(), tarDsInfo.getDsType(), tarDsInfo.getMainVersion());
            }
        }

        boolean success = processColumnMapping(context, eColumn);
        if (!success) {
            columnEditor.delete();
            return;
        }

        if (!context.isSkipHandlers()) {
            List<ColumnAfterMappingHandler> afterColumnHandler = SchemaFramework.getHandlers(handlerKeys, ColumnAfterMappingHandler.class);
            for (ColumnAfterMappingHandler handler : afterColumnHandler) {
                handler.afterMapping(tableEditor, columnEditor, context, srcDsInfo.getDsType(), srcDsInfo.getMainVersion(), tarDsInfo.getDsType(), tarDsInfo.getMainVersion());
            }
        }
    }

    private boolean processColumnMapping(TriggerContext context, EColumn eColumn) {
        MappingHandler mappingHandler = context.getMappingHandler();

        // columnType
        String dbType = eColumn.getDbType();
        if (mappingHandler.hasColumnMapping(dbType)) {
            eColumn.setDbType(mappingHandler.columnMapping(dbType, eColumn.getAttribute()));
        } else {
            DsType dsType = context.getSrcDsInfo().getDsType();
            String msg = "src '" + dsType.getTypeName() + "' dataType '" + dbType + "' is not supported";
            context.addErrors(new UnsupportedOperationException(msg));
            return false;
        }

        // default value
        String defaultValue = eColumn.getDefaultValue();
        if (eColumn.isDefaultValueIsFunc()) {
            if (mappingHandler.hasFunctionMapping(defaultValue)) {
                String mappingTo = mappingHandler.functionMapping(defaultValue, eColumn.getAttribute());
                eColumn.setDefaultValue(mappingTo);
                eColumn.setDefaultValueIsFunc(true);
            } else {
                eColumn.setDefaultValue(null);
                eColumn.setDefaultValueIsFunc(false);
            }
        }

        return true;
    }

    protected void triggerDropTable(DsInfo srcDsInfo, DsInfo tarDsInfo, String catalog, String schema, String table, ETable eTable) {
        TriggerContext context = buildContext(srcDsInfo, tarDsInfo);
        SqlBuilder provider = context.getTarDsInfo().getProvider();

        ETable cloneTable = eTable.clone();
        List<String> sqlString = provider.tableDrop(context, catalog, schema, table, cloneTable);
        this.actions.add(new Action(sqlString, catalog, schema, table));
    }

    protected void triggerTruncateTable(DsInfo srcDsInfo, DsInfo tarDsInfo, String catalog, String schema, String table, ETable eTable) {
        TriggerContext context = buildContext(srcDsInfo, tarDsInfo);
        SqlBuilder provider = context.getTarDsInfo().getProvider();

        ETable cloneTable = eTable.clone();
        List<String> sqlString = provider.tableTruncate(context, catalog, schema, table, cloneTable);
        this.actions.add(new Action(sqlString, catalog, schema, table));
    }

    // -------------------------------------------------------------------------------------------------------------------------- Column

    protected void triggerColumnRename(DsInfo srcDsInfo, DsInfo tarDsInfo, String catalog, String schema, String table, EColumn columnInfo, String newColumnName) {
        TriggerContext context = buildContext(srcDsInfo, tarDsInfo);
        doTriggerColumnRename(catalog, schema, table, columnInfo, newColumnName, context);
    }

    private void doTriggerColumnRename(String catalog, String schema, String table, EColumn columnInfo, String newColumnName, TriggerContext context) {
        SqlBuilder provider = context.getTarDsInfo().getProvider();

        EColumn cloneColumn = columnInfo.clone();
        List<String> sqlString = provider.columnRename(context, catalog, schema, table, cloneColumn, newColumnName);
        this.actions.add(new Action(sqlString, catalog, schema, table));
    }
}
