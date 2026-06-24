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
package com.clougence.clouddm.console.web.component.schema;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import com.clougence.clouddm.base.metadata.ds.DataSourceConfig;
import com.clougence.clouddm.base.metadata.ui.form.UiPanel;
import com.clougence.clouddm.console.web.global.i18n.DmI18nUtils;
import com.clougence.clouddm.console.web.global.i18n.I18nDmMsgKeys;
import com.clougence.clouddm.platform.dal.model.datasource.DmDsDO;
import com.clougence.clouddm.sdk.execute.meta.DsElement;
import com.clougence.clouddm.sdk.ui.editor.property.PropertyUiPanel;
import com.clougence.clouddm.sdk.ui.editor.table.TableEditorUiPanel;
import com.clougence.clouddm.sdk.ui.template.CmdTemplateOption;
import com.clougence.schema.editor.EditorContext;
import com.clougence.schema.editor.EditorHelperDm;
import com.clougence.schema.editor.EditorOptions;
import com.clougence.schema.editor.TableEditor;
import com.clougence.schema.editor.builder.actions.Action;
import com.clougence.schema.umi.special.rdb.RdbColumn;
import com.clougence.schema.umi.struts.UmiTypes;
import com.clougence.schema.umi.struts.Value;
import com.clougence.utils.CollectionUtils;
import com.clougence.utils.StringUtils;

import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;

/**
 * @author mode 2021/1/8 19:56
 */
@Slf4j
@Primary
@Service
public class DsSchemaServiceImpl implements DsSchemaService {

    @Resource
    private LocalDsSchemaService  localSchemaService;
    @Resource
    private RemoteDsSchemaService remoteSchemaService;

    @Override
    public String realTimeFetchVersion(long clusterId, DataSourceConfig dsConfig, Map<UmiTypes, Object> levelsParam) {
        return this.remoteSchemaService.realTimeFetchVersion(clusterId, dsConfig, levelsParam);
    }

    @Override
    public Value realTimeFetchSelectObject(DmDsDO dsDO, Map<UmiTypes, Object> levelsParam, String leafName) {
        return this.remoteSchemaService.realTimeFetchSelectObject(dsDO, levelsParam, leafName);
    }

    @Override
    public List<String> realTimeRequestObjectScript(DmDsDO dsDO, Map<UmiTypes, Object> levelsParam, UmiTypes leafType, String leafName) {
        return this.remoteSchemaService.realTimeRequestObjectScript(dsDO, levelsParam, leafType, leafName);
    }

    @Override
    public String realTimeFetchVersion(DmDsDO dsDO, Map<UmiTypes, Object> levelsParam) {
        String version = this.localSchemaService.realTimeFetchVersion(dsDO, levelsParam);
        if (StringUtils.isNotBlank(version)) {
            return version;
        }
        return this.remoteSchemaService.realTimeFetchVersion(dsDO, levelsParam);
    }

    @Override
    public List<DsElement> cachedObjectNames(DmDsDO dsDO, List<UmiTypes> levels, Map<UmiTypes, Object> levelsParam) {
        List<DsElement> elements = this.localSchemaService.cachedObjectNames(dsDO, levels, levelsParam);
        if (CollectionUtils.isNotEmpty(elements)) {
            return elements;
        }
        return this.remoteSchemaService.cachedObjectNames(dsDO, levels, levelsParam);
    }

    //

    @Override
    public List<DsElement> listLevels(DmDsDO dsDO, List<UmiTypes> levels, Map<UmiTypes, Object> levelsParam, boolean refreshCache) {
        List<DsElement> elements = this.localSchemaService.listLevels(dsDO, levels, levelsParam, refreshCache);
        if (elements != null) {
            return elements;
        }
        return this.remoteSchemaService.listLevels(dsDO, levels, levelsParam, refreshCache);
    }

    @Override
    public DsElement detailLevel(DmDsDO dsDO, List<UmiTypes> levels, Map<UmiTypes, Object> levelsParam) {
        DsElement element = this.localSchemaService.detailLevel(dsDO, levels, levelsParam);
        if (element != null) {
            return element;
        }
        return this.remoteSchemaService.detailLevel(dsDO, levels, levelsParam);
    }

    @Override
    public List<DsElement> listLeaf(DmDsDO dsDO, Map<UmiTypes, Object> levelsParam, UmiTypes leafType, String pattern, boolean refreshCache) {
        List<DsElement> elements = this.localSchemaService.listLeaf(dsDO, levelsParam, leafType, pattern, refreshCache);
        if (elements != null) {
            return elements;
        }
        return this.remoteSchemaService.listLeaf(dsDO, levelsParam, leafType, pattern, refreshCache);
    }

    @Override
    public Value detailLeaf(DmDsDO dsDO, Map<UmiTypes, Object> levelsParam, UmiTypes leafType, String leafName, boolean refreshCache) {
        Value result = this.localSchemaService.detailLeaf(dsDO, levelsParam, leafType, leafName, refreshCache);
        if (result != null) {
            return result;
        }
        return this.remoteSchemaService.detailLeaf(dsDO, levelsParam, leafType, leafName, refreshCache);
    }

    @Override
    public List<String> generateObjectScript(DmDsDO dsDO, Map<UmiTypes, Object> levelsParam, UmiTypes leafType, String leafName, CmdTemplateOption option) {
        if (leafType != UmiTypes.Table) {
            String objType = DmI18nUtils.getMessage("UI_LEAF_TITLE_" + leafType.getTypeName());
            return Collections.singletonList(DmI18nUtils.getMessage(I18nDmMsgKeys.CONSOLE_BROWSE_SQL_GEN_NOT_SUPPORT_ERROR.name(), objType));
        }

        EditorOptions options = null;
        if (option != null) {
            options = new EditorOptions();
            options.setUseDelimited(option.isDelimited());
        }

        EditorContext editorContext = this.createEditorContext(dsDO, levelsParam, options);
        editorContext.setSkipHandlers(true);
        String editorData = this.loadTableEditor(dsDO, levelsParam, leafName, false);
        TableEditor editor = EditorHelperDm.restoreTableEditor(editorData, editorContext);

        List<Action> actions = editor.diffActions(editor.getSource(), true);
        if (CollectionUtils.isEmpty(actions)) {
            return Collections.emptyList();
        } else {
            return actions.stream().flatMap((Function<Action, Stream<String>>) action -> {
                return action.getSqlString().stream();
            }).filter(StringUtils::isNotBlank).collect(Collectors.toList());
        }
    }

    @Override
    public TableEditorUiPanel fetchTableEditorUiPanel(DmDsDO dsDO, Map<UmiTypes, Object> levelsParam, Map<String, String> envVariables) {
        TableEditorUiPanel result = this.localSchemaService.fetchTableEditorUiPanel(dsDO, levelsParam, envVariables);
        if (result != null) {
            return result;
        }
        return this.remoteSchemaService.fetchTableEditorUiPanel(dsDO, levelsParam, envVariables);
    }

    @Override
    public UiPanel fetchFunctionUiPanel(DmDsDO dsDO, Map<UmiTypes, Object> levelsParam, Map<String, String> envVariables) {
        UiPanel result = this.localSchemaService.fetchFunctionUiPanel(dsDO, levelsParam, envVariables);
        if (result != null) {
            return result;
        }
        return this.remoteSchemaService.fetchFunctionUiPanel(dsDO, levelsParam, envVariables);
    }

    @Override
    public UiPanel fetchProcedureUiPanel(DmDsDO dsDO, Map<UmiTypes, Object> levelsParam, Map<String, String> envVariables) {
        UiPanel result = this.localSchemaService.fetchProcedureUiPanel(dsDO, levelsParam, envVariables);
        if (result != null) {
            return result;
        }
        return this.remoteSchemaService.fetchProcedureUiPanel(dsDO, levelsParam, envVariables);
    }

    @Override
    public UiPanel fetchViewUiPanel(DmDsDO dsDO, Map<UmiTypes, Object> levelsParam, Map<String, String> envVariables) {
        UiPanel result = this.localSchemaService.fetchViewUiPanel(dsDO, levelsParam, envVariables);
        if (result != null) {
            return result;
        }
        return this.remoteSchemaService.fetchViewUiPanel(dsDO, levelsParam, envVariables);
    }

    @Override
    public UiPanel fetchTriggerEditorUiPanel(DmDsDO dsDO, Map<UmiTypes, Object> levelsParam, Map<String, String> envVariables) {
        UiPanel result = this.localSchemaService.fetchTriggerEditorUiPanel(dsDO, levelsParam, envVariables);
        if (result != null) {
            return result;
        }
        return this.remoteSchemaService.fetchTriggerEditorUiPanel(dsDO, levelsParam, envVariables);
    }

    @Override
    public UiPanel fetchTablespaceUiPanel(DmDsDO dsDO, Map<UmiTypes, Object> levelsParam, Map<String, String> envVariables) {
        UiPanel result = this.localSchemaService.fetchTablespaceUiPanel(dsDO, levelsParam, envVariables);
        if (result != null) {
            return result;
        }
        return this.remoteSchemaService.fetchTablespaceUiPanel(dsDO, levelsParam, envVariables);
    }

    @Override
    public UiPanel fetchDbLinkUiPanel(DmDsDO dsDO, Map<UmiTypes, Object> levelsParam, Map<String, String> envVariables) {
        UiPanel result = this.localSchemaService.fetchDbLinkUiPanel(dsDO, levelsParam, envVariables);
        if (result != null) {
            return result;
        }
        return this.remoteSchemaService.fetchDbLinkUiPanel(dsDO, levelsParam, envVariables);
    }

    @Override
    public UiPanel fetchJobUiPanel(DmDsDO dsDO, Map<UmiTypes, Object> levelsParam, Map<String, String> envVariables) {
        UiPanel result = this.localSchemaService.fetchJobUiPanel(dsDO, levelsParam, envVariables);
        if (result != null) {
            return result;
        }
        return this.remoteSchemaService.fetchJobUiPanel(dsDO, levelsParam, envVariables);
    }

    @Override
    public UiPanel fetchScheduleJobEditorUiPanel(DmDsDO dsDO, Map<UmiTypes, Object> levelsParam, Map<String, String> envVariables) {
        UiPanel result = this.localSchemaService.fetchScheduleJobEditorUiPanel(dsDO, levelsParam, envVariables);
        if (result != null) {
            return result;
        }
        return this.remoteSchemaService.fetchScheduleJobEditorUiPanel(dsDO, levelsParam, envVariables);
    }

    @Override
    public PropertyUiPanel fetchJobPropertyUiPanel(DmDsDO dsDO, Map<UmiTypes, Object> levelsParam, Map<String, String> envVariables) {
        PropertyUiPanel result = this.localSchemaService.fetchJobPropertyUiPanel(dsDO, levelsParam, envVariables);
        if (result != null) {
            return result;
        }
        return this.remoteSchemaService.fetchJobPropertyUiPanel(dsDO, levelsParam, envVariables);
    }

    @Override
    public PropertyUiPanel fetchUserPropertyUiPanel(DmDsDO dsDO, Map<UmiTypes, Object> levelsParam, Map<String, String> envVariables) {
        PropertyUiPanel result = this.localSchemaService.fetchUserPropertyUiPanel(dsDO, levelsParam, envVariables);
        if (result != null) {
            return result;
        }
        return this.remoteSchemaService.fetchUserPropertyUiPanel(dsDO, levelsParam, envVariables);
    }

    @Override
    public PropertyUiPanel fetchSequencePropertyUiPanel(DmDsDO dsDO, Map<UmiTypes, Object> levelsParam, Map<String, String> envVariables) {
        PropertyUiPanel result = this.localSchemaService.fetchSequencePropertyUiPanel(dsDO, levelsParam, envVariables);
        if (result != null) {
            return result;
        }
        return this.remoteSchemaService.fetchSequencePropertyUiPanel(dsDO, levelsParam, envVariables);
    }

    @Override
    public PropertyUiPanel fetchSynonymPropertyUiPanel(DmDsDO dsDO, Map<UmiTypes, Object> levelsParam, Map<String, String> envVariables) {
        PropertyUiPanel result = this.localSchemaService.fetchSynonymPropertyUiPanel(dsDO, levelsParam, envVariables);
        if (result != null) {
            return result;
        }
        return this.remoteSchemaService.fetchSynonymPropertyUiPanel(dsDO, levelsParam, envVariables);
    }

    @Override
    public PropertyUiPanel fetchTriggerPropertyUiPanel(DmDsDO dsDO, Map<UmiTypes, Object> levelsParam, Map<String, String> envVariables) {
        PropertyUiPanel result = this.localSchemaService.fetchTriggerPropertyUiPanel(dsDO, levelsParam, envVariables);
        if (result != null) {
            return result;
        }
        return this.remoteSchemaService.fetchTriggerPropertyUiPanel(dsDO, levelsParam, envVariables);
    }

    @Override
    public PropertyUiPanel fetchViewPropertyUiPanel(DmDsDO dsDO, Map<UmiTypes, Object> levelsParam, Map<String, String> envVariables) {
        PropertyUiPanel result = this.localSchemaService.fetchViewPropertyUiPanel(dsDO, levelsParam, envVariables);
        if (result != null) {
            return result;
        }
        return this.remoteSchemaService.fetchViewPropertyUiPanel(dsDO, levelsParam, envVariables);
    }

    @Override
    public PropertyUiPanel fetchMaterializedViewPropertyUiPanel(DmDsDO dsDO, Map<UmiTypes, Object> levelsParam, Map<String, String> envVariables) {
        PropertyUiPanel result = this.localSchemaService.fetchMaterializedViewPropertyUiPanel(dsDO, levelsParam, envVariables);
        if (result != null) {
            return result;
        }
        return this.remoteSchemaService.fetchMaterializedViewPropertyUiPanel(dsDO, levelsParam, envVariables);
    }

    @Override
    public PropertyUiPanel fetchRolePropertyUiPanel(DmDsDO dsDO, Map<UmiTypes, Object> levelsParam, Map<String, String> envVariables) {
        PropertyUiPanel result = this.localSchemaService.fetchRolePropertyUiPanel(dsDO, levelsParam, envVariables);
        if (result != null) {
            return result;
        }
        return this.remoteSchemaService.fetchRolePropertyUiPanel(dsDO, levelsParam, envVariables);
    }

    @Override
    public PropertyUiPanel fetchScheduleJobPropertyUiPanel(DmDsDO dsDO, Map<UmiTypes, Object> levelsParam, Map<String, String> envVariables) {
        PropertyUiPanel result = this.localSchemaService.fetchScheduleJobPropertyUiPanel(dsDO, levelsParam, envVariables);
        if (result != null) {
            return result;
        }
        return this.remoteSchemaService.fetchScheduleJobPropertyUiPanel(dsDO, levelsParam, envVariables);
    }

    @Override
    public PropertyUiPanel fetchProcedurePropertyUiPanel(DmDsDO dsDO, Map<UmiTypes, Object> levelsParam, Map<String, String> envVariables) {
        PropertyUiPanel result = this.localSchemaService.fetchProcedurePropertyUiPanel(dsDO, levelsParam, envVariables);
        if (result != null) {
            return result;
        }
        return this.remoteSchemaService.fetchProcedurePropertyUiPanel(dsDO, levelsParam, envVariables);
    }

    @Override
    public PropertyUiPanel fetchFunctionPropertyUiPanel(DmDsDO dsDO, Map<UmiTypes, Object> levelsParam, Map<String, String> envVariables) {
        PropertyUiPanel result = this.localSchemaService.fetchFunctionPropertyUiPanel(dsDO, levelsParam, envVariables);
        if (result != null) {
            return result;
        }
        return this.remoteSchemaService.fetchFunctionPropertyUiPanel(dsDO, levelsParam, envVariables);
    }

    @Override
    public PropertyUiPanel fetchDbLinkPropertyUiPanel(DmDsDO dsDO, Map<UmiTypes, Object> levelsParam, Map<String, String> envVariables) {
        PropertyUiPanel result = this.localSchemaService.fetchDbLinkPropertyUiPanel(dsDO, levelsParam, envVariables);
        if (result != null) {
            return result;
        }
        return this.remoteSchemaService.fetchDbLinkPropertyUiPanel(dsDO, levelsParam, envVariables);
    }

    @Override
    public PropertyUiPanel fetchTablePropertyUiPanel(DmDsDO dsDO, Map<UmiTypes, Object> levelsParam, Map<String, String> envVariables) {
        PropertyUiPanel result = this.localSchemaService.fetchDbLinkPropertyUiPanel(dsDO, levelsParam, envVariables);
        if (result != null) {
            return result;
        }
        return this.remoteSchemaService.fetchTablePropertyUiPanel(dsDO, levelsParam, envVariables);
    }

    @Override
    public String loadTableEditor(DmDsDO dsDO, Map<UmiTypes, Object> levelsParam, String table, boolean refreshCache) {
        String result = this.localSchemaService.loadTableEditor(dsDO, levelsParam, table, refreshCache);
        if (result != null) {
            return result;
        }
        return this.remoteSchemaService.loadTableEditor(dsDO, levelsParam, table, refreshCache);
    }

    @Override
    public EditorContext createEditorContext(DmDsDO dsDO, Map<UmiTypes, Object> levelsParam, EditorOptions options) {
        EditorContext result = this.localSchemaService.createEditorContext(dsDO, levelsParam, options);
        if (result != null) {
            return result;
        }
        return this.remoteSchemaService.createEditorContext(dsDO, levelsParam, options);
    }

    @Override
    public Map<String, List<RdbColumn>> loadColumns(DmDsDO dsDO, Map<UmiTypes, Object> levelsParam, UmiTypes leafType, List<String> names) {
        Map<String, List<RdbColumn>> result = this.localSchemaService.loadColumns(dsDO, levelsParam, leafType, names);
        if (result != null) {
            return result;
        }
        return this.remoteSchemaService.loadColumns(dsDO, levelsParam, leafType, names);
    }
}
