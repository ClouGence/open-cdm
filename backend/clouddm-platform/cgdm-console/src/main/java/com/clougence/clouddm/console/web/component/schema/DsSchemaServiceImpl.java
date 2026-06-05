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
    public String realTimeFetchVersion(String uid, long clusterId, DataSourceConfig dsConfig, Map<UmiTypes, Object> levelsParam) {
        return this.remoteSchemaService.realTimeFetchVersion(uid, clusterId, dsConfig, levelsParam);
    }

    @Override
    public Value realTimeFetchSelectObject(String uid, DmDsDO dsDO, Map<UmiTypes, Object> levelsParam, String leafName) {
        return this.remoteSchemaService.realTimeFetchSelectObject(uid, dsDO, levelsParam, leafName);
    }

    @Override
    public List<String> realTimeRequestObjectScript(String uid, DmDsDO dsDO, Map<UmiTypes, Object> levelsParam, UmiTypes leafType, String leafName) {
        return this.remoteSchemaService.realTimeRequestObjectScript(uid, dsDO, levelsParam, leafType, leafName);
    }

    @Override
    public String realTimeFetchVersion(String uid, DmDsDO dsDO, Map<UmiTypes, Object> levelsParam) {
        String version = this.localSchemaService.realTimeFetchVersion(uid, dsDO, levelsParam);
        if (StringUtils.isNotBlank(version)) {
            return version;
        }
        return this.remoteSchemaService.realTimeFetchVersion(uid, dsDO, levelsParam);
    }

    @Override
    public List<DsElement> cachedObjectNames(String uid, DmDsDO dsDO, List<UmiTypes> levels, Map<UmiTypes, Object> levelsParam) {
        List<DsElement> elements = this.localSchemaService.cachedObjectNames(uid, dsDO, levels, levelsParam);
        if (CollectionUtils.isNotEmpty(elements)) {
            return elements;
        }
        return this.remoteSchemaService.cachedObjectNames(uid, dsDO, levels, levelsParam);
    }

    //

    @Override
    public List<DsElement> listLevels(String uid, DmDsDO dsDO, List<UmiTypes> levels, Map<UmiTypes, Object> levelsParam, boolean refreshCache) {
        List<DsElement> elements = this.localSchemaService.listLevels(uid, dsDO, levels, levelsParam, refreshCache);
        if (elements != null) {
            return elements;
        }
        return this.remoteSchemaService.listLevels(uid, dsDO, levels, levelsParam, refreshCache);
    }

    @Override
    public DsElement detailLevel(String uid, DmDsDO dsDO, List<UmiTypes> levels, Map<UmiTypes, Object> levelsParam) {
        DsElement element = this.localSchemaService.detailLevel(uid, dsDO, levels, levelsParam);
        if (element != null) {
            return element;
        }
        return this.remoteSchemaService.detailLevel(uid, dsDO, levels, levelsParam);
    }

    @Override
    public List<DsElement> listLeaf(String uid, DmDsDO dsDO, Map<UmiTypes, Object> levelsParam, UmiTypes leafType, String pattern, boolean refreshCache) {
        List<DsElement> elements = this.localSchemaService.listLeaf(uid, dsDO, levelsParam, leafType, pattern, refreshCache);
        if (elements != null) {
            return elements;
        }
        return this.remoteSchemaService.listLeaf(uid, dsDO, levelsParam, leafType, pattern, refreshCache);
    }

    @Override
    public Value detailLeaf(String uid, DmDsDO dsDO, Map<UmiTypes, Object> levelsParam, UmiTypes leafType, String leafName, boolean refreshCache) {
        Value result = this.localSchemaService.detailLeaf(uid, dsDO, levelsParam, leafType, leafName, refreshCache);
        if (result != null) {
            return result;
        }
        return this.remoteSchemaService.detailLeaf(uid, dsDO, levelsParam, leafType, leafName, refreshCache);
    }

    @Override
    public List<String> generateObjectScript(String uid, DmDsDO dsDO, Map<UmiTypes, Object> levelsParam, UmiTypes leafType, String leafName, CmdTemplateOption option) {
        if (leafType != UmiTypes.Table) {
            String objType = DmI18nUtils.getMessage("UI_LEAF_TITLE_" + leafType.getTypeName());
            return Collections.singletonList(DmI18nUtils.getMessage(I18nDmMsgKeys.CONSOLE_BROWSE_SQL_GEN_NOT_SUPPORT_ERROR.name(), objType));
        }

        EditorOptions options = null;
        if (option != null) {
            options = new EditorOptions();
            options.setUseDelimited(option.isDelimited());
        }

        EditorContext editorContext = this.createEditorContext(uid, dsDO, levelsParam, options);
        editorContext.setSkipHandlers(true);
        String editorData = this.loadTableEditor(uid, dsDO, levelsParam, leafName, false);
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
    public TableEditorUiPanel fetchTableEditorUiPanel(String uid, DmDsDO dsDO, Map<UmiTypes, Object> levelsParam, Map<String, String> envVariables) {
        TableEditorUiPanel result = this.localSchemaService.fetchTableEditorUiPanel(uid, dsDO, levelsParam, envVariables);
        if (result != null) {
            return result;
        }
        return this.remoteSchemaService.fetchTableEditorUiPanel(uid, dsDO, levelsParam, envVariables);
    }

    @Override
    public UiPanel fetchFunctionUiPanel(String uid, DmDsDO dsDO, Map<UmiTypes, Object> levelsParam, Map<String, String> envVariables) {
        UiPanel result = this.localSchemaService.fetchFunctionUiPanel(uid, dsDO, levelsParam, envVariables);
        if (result != null) {
            return result;
        }
        return this.remoteSchemaService.fetchFunctionUiPanel(uid, dsDO, levelsParam, envVariables);
    }

    @Override
    public UiPanel fetchProcedureUiPanel(String uid, DmDsDO dsDO, Map<UmiTypes, Object> levelsParam, Map<String, String> envVariables) {
        UiPanel result = this.localSchemaService.fetchProcedureUiPanel(uid, dsDO, levelsParam, envVariables);
        if (result != null) {
            return result;
        }
        return this.remoteSchemaService.fetchProcedureUiPanel(uid, dsDO, levelsParam, envVariables);
    }

    @Override
    public UiPanel fetchViewUiPanel(String uid, DmDsDO dsDO, Map<UmiTypes, Object> levelsParam, Map<String, String> envVariables) {
        UiPanel result = this.localSchemaService.fetchViewUiPanel(uid, dsDO, levelsParam, envVariables);
        if (result != null) {
            return result;
        }
        return this.remoteSchemaService.fetchViewUiPanel(uid, dsDO, levelsParam, envVariables);
    }

    @Override
    public UiPanel fetchTriggerEditorUiPanel(String uid, DmDsDO dsDO, Map<UmiTypes, Object> levelsParam, Map<String, String> envVariables) {
        UiPanel result = this.localSchemaService.fetchTriggerEditorUiPanel(uid, dsDO, levelsParam, envVariables);
        if (result != null) {
            return result;
        }
        return this.remoteSchemaService.fetchTriggerEditorUiPanel(uid, dsDO, levelsParam, envVariables);
    }

    @Override
    public UiPanel fetchTablespaceUiPanel(String uid, DmDsDO dsDO, Map<UmiTypes, Object> levelsParam, Map<String, String> envVariables) {
        UiPanel result = this.localSchemaService.fetchTablespaceUiPanel(uid, dsDO, levelsParam, envVariables);
        if (result != null) {
            return result;
        }
        return this.remoteSchemaService.fetchTablespaceUiPanel(uid, dsDO, levelsParam, envVariables);
    }

    @Override
    public UiPanel fetchDbLinkUiPanel(String uid, DmDsDO dsDO, Map<UmiTypes, Object> levelsParam, Map<String, String> envVariables) {
        UiPanel result = this.localSchemaService.fetchDbLinkUiPanel(uid, dsDO, levelsParam, envVariables);
        if (result != null) {
            return result;
        }
        return this.remoteSchemaService.fetchDbLinkUiPanel(uid, dsDO, levelsParam, envVariables);
    }

    @Override
    public UiPanel fetchJobUiPanel(String uid, DmDsDO dsDO, Map<UmiTypes, Object> levelsParam, Map<String, String> envVariables) {
        UiPanel result = this.localSchemaService.fetchJobUiPanel(uid, dsDO, levelsParam, envVariables);
        if (result != null) {
            return result;
        }
        return this.remoteSchemaService.fetchJobUiPanel(uid, dsDO, levelsParam, envVariables);
    }

    @Override
    public UiPanel fetchScheduleJobEditorUiPanel(String uid, DmDsDO dsDO, Map<UmiTypes, Object> levelsParam, Map<String, String> envVariables) {
        UiPanel result = this.localSchemaService.fetchScheduleJobEditorUiPanel(uid, dsDO, levelsParam, envVariables);
        if (result != null) {
            return result;
        }
        return this.remoteSchemaService.fetchScheduleJobEditorUiPanel(uid, dsDO, levelsParam, envVariables);
    }

    @Override
    public PropertyUiPanel fetchJobPropertyUiPanel(String uid, DmDsDO dsDO, Map<UmiTypes, Object> levelsParam, Map<String, String> envVariables) {
        PropertyUiPanel result = this.localSchemaService.fetchJobPropertyUiPanel(uid, dsDO, levelsParam, envVariables);
        if (result != null) {
            return result;
        }
        return this.remoteSchemaService.fetchJobPropertyUiPanel(uid, dsDO, levelsParam, envVariables);
    }

    @Override
    public PropertyUiPanel fetchUserPropertyUiPanel(String uid, DmDsDO dsDO, Map<UmiTypes, Object> levelsParam, Map<String, String> envVariables) {
        PropertyUiPanel result = this.localSchemaService.fetchUserPropertyUiPanel(uid, dsDO, levelsParam, envVariables);
        if (result != null) {
            return result;
        }
        return this.remoteSchemaService.fetchUserPropertyUiPanel(uid, dsDO, levelsParam, envVariables);
    }

    @Override
    public PropertyUiPanel fetchSequencePropertyUiPanel(String uid, DmDsDO dsDO, Map<UmiTypes, Object> levelsParam, Map<String, String> envVariables) {
        PropertyUiPanel result = this.localSchemaService.fetchSequencePropertyUiPanel(uid, dsDO, levelsParam, envVariables);
        if (result != null) {
            return result;
        }
        return this.remoteSchemaService.fetchSequencePropertyUiPanel(uid, dsDO, levelsParam, envVariables);
    }

    @Override
    public PropertyUiPanel fetchSynonymPropertyUiPanel(String uid, DmDsDO dsDO, Map<UmiTypes, Object> levelsParam, Map<String, String> envVariables) {
        PropertyUiPanel result = this.localSchemaService.fetchSynonymPropertyUiPanel(uid, dsDO, levelsParam, envVariables);
        if (result != null) {
            return result;
        }
        return this.remoteSchemaService.fetchSynonymPropertyUiPanel(uid, dsDO, levelsParam, envVariables);
    }

    @Override
    public PropertyUiPanel fetchTriggerPropertyUiPanel(String uid, DmDsDO dsDO, Map<UmiTypes, Object> levelsParam, Map<String, String> envVariables) {
        PropertyUiPanel result = this.localSchemaService.fetchTriggerPropertyUiPanel(uid, dsDO, levelsParam, envVariables);
        if (result != null) {
            return result;
        }
        return this.remoteSchemaService.fetchTriggerPropertyUiPanel(uid, dsDO, levelsParam, envVariables);
    }

    @Override
    public PropertyUiPanel fetchViewPropertyUiPanel(String uid, DmDsDO dsDO, Map<UmiTypes, Object> levelsParam, Map<String, String> envVariables) {
        PropertyUiPanel result = this.localSchemaService.fetchViewPropertyUiPanel(uid, dsDO, levelsParam, envVariables);
        if (result != null) {
            return result;
        }
        return this.remoteSchemaService.fetchViewPropertyUiPanel(uid, dsDO, levelsParam, envVariables);
    }

    @Override
    public PropertyUiPanel fetchMaterializedViewPropertyUiPanel(String uid, DmDsDO dsDO, Map<UmiTypes, Object> levelsParam, Map<String, String> envVariables) {
        PropertyUiPanel result = this.localSchemaService.fetchMaterializedViewPropertyUiPanel(uid, dsDO, levelsParam, envVariables);
        if (result != null) {
            return result;
        }
        return this.remoteSchemaService.fetchMaterializedViewPropertyUiPanel(uid, dsDO, levelsParam, envVariables);
    }

    @Override
    public PropertyUiPanel fetchRolePropertyUiPanel(String uid, DmDsDO dsDO, Map<UmiTypes, Object> levelsParam, Map<String, String> envVariables) {
        PropertyUiPanel result = this.localSchemaService.fetchRolePropertyUiPanel(uid, dsDO, levelsParam, envVariables);
        if (result != null) {
            return result;
        }
        return this.remoteSchemaService.fetchRolePropertyUiPanel(uid, dsDO, levelsParam, envVariables);
    }

    @Override
    public PropertyUiPanel fetchScheduleJobPropertyUiPanel(String uid, DmDsDO dsDO, Map<UmiTypes, Object> levelsParam, Map<String, String> envVariables) {
        PropertyUiPanel result = this.localSchemaService.fetchScheduleJobPropertyUiPanel(uid, dsDO, levelsParam, envVariables);
        if (result != null) {
            return result;
        }
        return this.remoteSchemaService.fetchScheduleJobPropertyUiPanel(uid, dsDO, levelsParam, envVariables);
    }

    @Override
    public PropertyUiPanel fetchProcedurePropertyUiPanel(String uid, DmDsDO dsDO, Map<UmiTypes, Object> levelsParam, Map<String, String> envVariables) {
        PropertyUiPanel result = this.localSchemaService.fetchProcedurePropertyUiPanel(uid, dsDO, levelsParam, envVariables);
        if (result != null) {
            return result;
        }
        return this.remoteSchemaService.fetchProcedurePropertyUiPanel(uid, dsDO, levelsParam, envVariables);
    }

    @Override
    public PropertyUiPanel fetchFunctionPropertyUiPanel(String uid, DmDsDO dsDO, Map<UmiTypes, Object> levelsParam, Map<String, String> envVariables) {
        PropertyUiPanel result = this.localSchemaService.fetchFunctionPropertyUiPanel(uid, dsDO, levelsParam, envVariables);
        if (result != null) {
            return result;
        }
        return this.remoteSchemaService.fetchFunctionPropertyUiPanel(uid, dsDO, levelsParam, envVariables);
    }

    @Override
    public PropertyUiPanel fetchDbLinkPropertyUiPanel(String uid, DmDsDO dsDO, Map<UmiTypes, Object> levelsParam, Map<String, String> envVariables) {
        PropertyUiPanel result = this.localSchemaService.fetchDbLinkPropertyUiPanel(uid, dsDO, levelsParam, envVariables);
        if (result != null) {
            return result;
        }
        return this.remoteSchemaService.fetchDbLinkPropertyUiPanel(uid, dsDO, levelsParam, envVariables);
    }

    @Override
    public PropertyUiPanel fetchTablePropertyUiPanel(String uid, DmDsDO dsDO, Map<UmiTypes, Object> levelsParam, Map<String, String> envVariables) {
        PropertyUiPanel result = this.localSchemaService.fetchDbLinkPropertyUiPanel(uid, dsDO, levelsParam, envVariables);
        if (result != null) {
            return result;
        }
        return this.remoteSchemaService.fetchTablePropertyUiPanel(uid, dsDO, levelsParam, envVariables);
    }

    @Override
    public String loadTableEditor(String uid, DmDsDO dsDO, Map<UmiTypes, Object> levelsParam, String table, boolean refreshCache) {
        String result = this.localSchemaService.loadTableEditor(uid, dsDO, levelsParam, table, refreshCache);
        if (result != null) {
            return result;
        }
        return this.remoteSchemaService.loadTableEditor(uid, dsDO, levelsParam, table, refreshCache);
    }

    @Override
    public EditorContext createEditorContext(String uid, DmDsDO dsDO, Map<UmiTypes, Object> levelsParam, EditorOptions options) {
        EditorContext result = this.localSchemaService.createEditorContext(uid, dsDO, levelsParam, options);
        if (result != null) {
            return result;
        }
        return this.remoteSchemaService.createEditorContext(uid, dsDO, levelsParam, options);
    }

    @Override
    public Map<String, List<RdbColumn>> loadColumns(String uid, DmDsDO dsDO, Map<UmiTypes, Object> levelsParam, UmiTypes leafType, List<String> names) {
        Map<String, List<RdbColumn>> result = this.localSchemaService.loadColumns(uid, dsDO, levelsParam, leafType, names);
        if (result != null) {
            return result;
        }
        return this.remoteSchemaService.loadColumns(uid, dsDO, levelsParam, leafType, names);
    }
}
