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
package com.clougence.clouddm.ds.greenplum.definition.ui.editor.table;

import static com.clougence.clouddm.base.metadata.ui.form.UiUtils.*;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.clougence.clouddm.base.metadata.ds.DataSourceConfig;
import com.clougence.clouddm.base.metadata.ui.form.UiPanelField;
import com.clougence.clouddm.base.metadata.ui.form.UiPanelFieldType;
import com.clougence.clouddm.base.metadata.ui.form.value.ValueDef;
import com.clougence.clouddm.ds.greenplum.i18n.GpDsI18nKeys;
import com.clougence.clouddm.dsfamily.postgres.definition.ui.editor.table.PgTableEditorUiPanelFactory;
import com.clougence.clouddm.sdk.ui.editor.EditorViewMode;
import com.clougence.clouddm.sdk.ui.editor.table.TableEditorUiPanel;
import com.clougence.utils.ExceptionUtils;
import com.clougence.utils.jdbc.extractor.MultipleRowResultSetExtractor;

import lombok.extern.slf4j.Slf4j;

/**
 * @Author: Ekko
 * @Date: 2023-09-22 16:54
 */
@Slf4j
public class GpTableEditorUiPanelFactory extends PgTableEditorUiPanelFactory implements GpTableEditorFields {

    // tableEditor TableInfo panel
    @Override
    protected void fillTableInfoUiPanelForAdvanced(TableEditorUiPanel uiPanel, DataSourceConfig dsConfig, EditorViewMode viewMode, Connection con) {
        super.fillTableInfoUiPanelForAdvanced(uiPanel, dsConfig, viewMode, con);
        boolean readOnly = viewMode == EditorViewMode.Alter;

        uiPanel.getTableInfo()
            .addField(UiPanelField.builder()
                .field(FIELD_TABLE_DISTRIBUTED_TYPE)
                .type(UiPanelFieldType.Radios)
                .defaultValue(strValueDef(null))
                .hide(viewMode == EditorViewMode.Alter)
                .options(fetchDistributed())
                .titleI18N(GpDsI18nKeys.EDITOR_TABLEINFO_DISTRIBUTED_TYPE_TITLE)
                .descI18N(GpDsI18nKeys.EDITOR_TABLEINFO_DISTRIBUTED_TYPE_DESC)
                .build());

        // uiPanel.getTableInfo()
        //     .addField(UiPanelField.builder()
        //         .field(FIELD_TABLE_APPEND_OPTIMIZED)
        //         .type(UiPanelFieldType.Check)
        //         .titleI18N(GpDsI18nKeys.EDITOR_TABLEINFO_APPEND_OPTIMIZED_TITLE)
        //         .descI18N(GpDsI18nKeys.EDITOR_TABLEINFO_APPEND_OPTIMIZED_DESC)
        //         .build());
        // uiPanel.getTableInfo()
        //     .addField(UiPanelField.builder()
        //         .field(FIELD_TABLE_BLOCK_SIZE)
        //         .type(UiPanelFieldType.Input)
        //         .titleI18N(GpDsI18nKeys.EDITOR_TABLEINFO_BLOCK_SIZE_TITLE)
        //         .descI18N(GpDsI18nKeys.EDITOR_TABLEINFO_BLOCK_SIZE_DESC)
        //         .build());

        uiPanel.getTableInfo()
            .addField(UiPanelField.builder()
                .field(FIELD_TABLE_ORIENTATION)
                .type(UiPanelFieldType.Radios)
                .defaultValue(strValueDef(null))
                .readOnly(readOnly)
                .options(fetchOrientation())
                .titleI18N(GpDsI18nKeys.EDITOR_TABLEINFO_ORIENTATION_TITLE)
                .descI18N(GpDsI18nKeys.EDITOR_TABLEINFO_ORIENTATION_DESC)
                .build());

        // uiPanel.getTableInfo()
        //     .addField(UiPanelField.builder()
        //         .field(FIELD_TABLE_CHECK_SUM)
        //         .type(UiPanelFieldType.Check)
        //         .titleI18N(GpDsI18nKeys.EDITOR_TABLEINFO_CHECK_SUM_TITLE)
        //         .descI18N(GpDsI18nKeys.EDITOR_TABLEINFO_CHECK_SUM_DESC)
        //         .build());

        uiPanel.getTableInfo()
            .addField(UiPanelField.builder()
                .field(FIELD_TABLE_COMPRESS_TYPE)
                .type(UiPanelFieldType.Radios)
                .defaultValue(strValueDef(null))
                .readOnly(readOnly)
                .options(fetchCompressType())
                .titleI18N(GpDsI18nKeys.EDITOR_TABLEINFO_COMPRESS_TYPE_TITLE)
                .descI18N(GpDsI18nKeys.EDITOR_TABLEINFO_COMPRESS_TYPE_DESC)
                .build());

        // uiPanel.getTableInfo()
        //     .addField(UiPanelField.builder()
        //         .field(FIELD_TABLE_COMPRESS_LEVEL)
        //         .type(UiPanelFieldType.Input)
        //         .titleI18N(GpDsI18nKeys.EDITOR_TABLEINFO_COMPRESS_LEVEL_TITLE)
        //         .descI18N(GpDsI18nKeys.EDITOR_TABLEINFO_COMPRESS_LEVEL_DESC)
        //         .build());
    }

    private List<ValueDef> fetchOrientation() {
        List<ValueDef> result = new ArrayList<>();
        result.add(optionDef(GpDsI18nKeys.EDITOR_TABLEINFO_ORIENTATION_EMPTY, null));
        result.add(optionDef(GpDsI18nKeys.EDITOR_TABLEINFO_ORIENTATION_ROW, "row"));
        result.add(optionDef(GpDsI18nKeys.EDITOR_TABLEINFO_ORIENTATION_COLUMN, "column"));
        return result;
    }

    private List<ValueDef> fetchCompressType() {
        List<ValueDef> result = new ArrayList<>();
        result.add(optionDef(GpDsI18nKeys.EDITOR_TABLEINFO_COMPRESS_TYPE_EMPTY, null));
        result.add(optionDef(GpDsI18nKeys.EDITOR_TABLEINFO_COMPRESS_TYPE_NONE, "none"));
        result.add(optionDef(GpDsI18nKeys.EDITOR_TABLEINFO_COMPRESS_TYPE_ZLIB, "zlib"));
        result.add(optionDef(GpDsI18nKeys.EDITOR_TABLEINFO_COMPRESS_TYPE_ZSTD, "zstd"));
        //result.add(optionDef(GpDsI18nKeys.EDITOR_TABLEINFO_COMPRESS_TYPE_QUICKLZ, "quicklz"));
        //result.add(optionDef(GpDsI18nKeys.EDITOR_TABLEINFO_COMPRESS_TYPE_RLE_TYPE, "rle_type"));
        return result;
    }

    private List<ValueDef> fetchDistributed() {
        List<ValueDef> result = new ArrayList<>();
        result.add(optionDef(GpDsI18nKeys.EDITOR_TABLEINFO_DISTRIBUTED_EMPTY, null));
        //        result.add(fieldOptionDef(GpDsI18nKeys.EDITOR_TABLEINFO_DISTRIBUTED_BY, "p").addField(UiPanelField.builder()
        //            .field(FIELD_TABLE_DISTRIBUTED_COLUMN)
        //            .type(UiPanelFieldType.CheckboxColumns)
        //            .titleI18N(GpDsI18nKeys.EDITOR_TABLEINFO_DISTRIBUTED_COLUMN_TITLE)
        //            .descI18N(GpDsI18nKeys.EDITOR_TABLEINFO_DISTRIBUTED_COLUMN_DESC)
        //            .build()));
        result.add(optionDef(GpDsI18nKeys.EDITOR_TABLEINFO_DISTRIBUTED_RANDOMLY, "RANDOMLY"));
        result.add(optionDef(GpDsI18nKeys.EDITOR_TABLEINFO_DISTRIBUTED_REPLICATED, "REPLICATED"));
        return result;
    }

    // tableEditor Indexes panel
    @Override
    protected void fillTableIndexesUiPanelForAdvanced(TableEditorUiPanel uiPanel, DataSourceConfig dsConfig, EditorViewMode viewMode, Connection con) {
        uiPanel.getIndexes()
            .beforeAddField(UiPanelField.builder()
                .field(FIELD_INDEXES_INDEX_TYPE)
                .type(UiPanelFieldType.Radios)
                .options(fetchIndexTypes())
                .defaultValue(strValueDef("btree"))
                .titleI18N(GpDsI18nKeys.EDITOR_INDEXES_TYPE_TITLE)
                .descI18N(GpDsI18nKeys.EDITOR_INDEXES_TYPE_DESC)
                .build(), FIELD_INDEXES_COLUMNS);
    }

    @Override
    protected List<ValueDef> fetchIndexTypes() {
        UiPanelField fillPanel = UiPanelField.builder()
            .field(FIELD_INDEXES_WITH_FILLFACTOR)
            .type(UiPanelFieldType.Input)
            .titleI18N(GpDsI18nKeys.EDITOR_INDEXES_WITH_FILLFACTOR_TITLE)
            .descI18N(GpDsI18nKeys.EDITOR_INDEXES_WITH_FILLFACTOR_DESC)
            .build();
        UiPanelField bufferPanel = UiPanelField.builder()
            .field(FIELD_INDEXES_WITH_BUFFERING)
            .type(UiPanelFieldType.Radios)
            .defaultValue(strValueDef(""))
            .options(fetchBuffering())
            .titleI18N(GpDsI18nKeys.EDITOR_INDEXES_WITH_BUFFERING_TITLE)
            .descI18N(GpDsI18nKeys.EDITOR_INDEXES_WITH_BUFFERING_DESC)
            .build();
        UiPanelField fastPanel = UiPanelField.builder()
            .field(FIELD_INDEXES_WITH_FASTUPDATE)
            .type(UiPanelFieldType.Check)
            .titleI18N(GpDsI18nKeys.EDITOR_INDEXES_WITH_FASTUPDATE_TITLE)
            .descI18N(GpDsI18nKeys.EDITOR_INDEXES_WITH_FASTUPDATE_DESC)
            .build();

        List<ValueDef> result = new ArrayList<>();
        result.add(fieldOptionDef(GpDsI18nKeys.EDITOR_INDEXES_TYPE_BTREE_LABEL, "btree").addField(fillPanel));
        result.add(fieldOptionDef(GpDsI18nKeys.EDITOR_INDEXES_TYPE_BITMAP_LABEL, "bitmap").addField(fillPanel));
        result.add(fieldOptionDef(GpDsI18nKeys.EDITOR_INDEXES_TYPE_GIST_LABEL, "gist").addField(fillPanel).addField(bufferPanel));
        result.add(fieldOptionDef(GpDsI18nKeys.EDITOR_INDEXES_TYPE_SPGIST_LABEL, "spgist").addField(fillPanel));
        result.add(fieldOptionDef(GpDsI18nKeys.EDITOR_INDEXES_TYPE_GIN_LABEL, "gin").addField(fastPanel));
        return result;
    }

    @Override
    protected List<ValueDef> fetchOperatorType(Connection con) {
        List<ValueDef> result = new ArrayList<>();
        List<String> nspnameList = new ArrayList<>();
        String sqlQuery = "SELECT concat(pn.nspname, '.', po.opcname) OPERATOR FROM pg_opclass po left join pg_namespace pn on po.opcnamespace = pn.oid ORDER BY opcname;";
        try (Statement s = con.createStatement(); ResultSet resultSet = s.executeQuery(sqlQuery)) {
            new MultipleRowResultSetExtractor<>((rs, rowNum) -> {
                nspnameList.add(rs.getString(1));
                return null;
            }).extractData(resultSet);
            //Operator categories are actually divided according to index categories, but there are some duplicates
            List<String> collect = nspnameList.stream().distinct().collect(Collectors.toList());
            collect.forEach(nspname -> result.add(optionDef(nspname, nspname)));
        } catch (SQLException e) {
            String msg = "Init opclass type error" + ",msg:" + ExceptionUtils.getRootCauseMessage(e);
            log.error(msg, e);
            throw new IllegalStateException(msg, e);
        }
        return result;
    }
}
