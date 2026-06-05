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

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Service;

import com.clougence.clouddm.api.common.exception.ErrorMessageException;
import com.clougence.clouddm.base.metadata.ds.DataSourceType;
import com.clougence.clouddm.console.web.component.dsconfig.DmDsConfigService;
import com.clougence.clouddm.console.web.component.dsconfig.mode.DsConfig;
import com.clougence.clouddm.console.web.component.dsconfig.mode.DsLevels;
import com.clougence.clouddm.console.web.component.schema.DsSchemaService;
import com.clougence.clouddm.console.web.global.i18n.DmI18nUtils;
import com.clougence.clouddm.console.web.global.i18n.I18nDmMsgKeys;
import com.clougence.clouddm.console.web.model.vo.editor.table.TableEditorForm;
import com.clougence.clouddm.console.web.util.DmConvertUtils;
import com.clougence.clouddm.console.web.util.UiWebUtil;
import com.clougence.clouddm.platform.dal.model.datasource.DmDsDO;
import com.clougence.clouddm.sdk.ui.editor.property.PropertyEditorUiData;
import com.clougence.clouddm.sdk.ui.editor.property.PropertyUiPanel;
import com.clougence.clouddm.sdk.ui.editor.table.TableEditorVarKeys;
import com.clougence.schema.umi.special.rdb.*;
import com.clougence.schema.umi.struts.UmiTypes;
import com.clougence.schema.umi.struts.Value;
import com.clougence.utils.i18n.I18nUtils;

import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class DsObjPropertyServiceImpl implements DsObjPropertyService {

    @Resource
    private DsSchemaService                    dmDsSchemaService;
    @Resource
    private DmDsConfigService                  dmDsConfigService;

    private final Map<String, TableEditorForm> dsUiEditorCache = new ConcurrentHashMap<>();

    /** for service API '/editor/table/editorDef' */
    @Override
    public TableEditorForm loadPropertyDef(String puid, String uid, DsLevels levels, UmiTypes types) {
        DmDsDO dsDO = levels.dsDO();
        DataSourceType dsType = dsDO.getDataSourceType();
        DsConfig dsConfig = this.dmDsConfigService.dsConstantSettings(dsType);
        if (dsConfig == null) {
            return null;
        }

        String i18nKey = I18nUtils.toI18nKey(DmI18nUtils.getLocale());
        Map<UmiTypes, Object> levelsParam = levels.levelsParam();
        String cacheKey = String.format("%s_%s_%s", dsDO.getDataSourceType(), types, i18nKey);

        if (this.dsUiEditorCache.containsKey(cacheKey)) {
            return this.dsUiEditorCache.get(cacheKey);
        }
        synchronized (this) {
            if (this.dsUiEditorCache.containsKey(cacheKey)) {
                return this.dsUiEditorCache.get(cacheKey);
            }

            Map<String, String> envVariables = new HashMap<>();
            envVariables.put(TableEditorVarKeys.I18N_LOCAL_USER.codeKey(), i18nKey);
            envVariables.put(TableEditorVarKeys.I18N_LOCAL_DEFAULT.codeKey(), I18nUtils.DEFAULT.getDefaultI18nKey());
            PropertyUiPanel uiPanel;
            switch (types) {
                case Job: {
                    uiPanel = this.dmDsSchemaService.fetchJobPropertyUiPanel(uid, dsDO, levelsParam, envVariables);
                    break;
                }
                case Sequence: {
                    uiPanel = this.dmDsSchemaService.fetchSequencePropertyUiPanel(uid, dsDO, levelsParam, envVariables);
                    break;
                }
                case View: {
                    uiPanel = this.dmDsSchemaService.fetchViewPropertyUiPanel(uid, dsDO, levelsParam, envVariables);
                    break;
                }
                case Materialized: {
                    uiPanel = this.dmDsSchemaService.fetchMaterializedViewPropertyUiPanel(uid, dsDO, levelsParam, envVariables);
                    break;
                }
                case ScheduleJob: {
                    uiPanel = this.dmDsSchemaService.fetchScheduleJobPropertyUiPanel(uid, dsDO, levelsParam, envVariables);
                    break;
                }
                case Procedure: {
                    uiPanel = this.dmDsSchemaService.fetchProcedurePropertyUiPanel(uid, dsDO, levelsParam, envVariables);
                    break;
                }
                case Function: {
                    uiPanel = this.dmDsSchemaService.fetchFunctionPropertyUiPanel(uid, dsDO, levelsParam, envVariables);
                    break;
                }
                case DBLink: {
                    uiPanel = this.dmDsSchemaService.fetchDbLinkPropertyUiPanel(uid, dsDO, levelsParam, envVariables);
                    break;
                }
                case Trigger: {
                    uiPanel = this.dmDsSchemaService.fetchTriggerPropertyUiPanel(uid, dsDO, levelsParam, envVariables);
                    break;
                }
                case Table: {
                    uiPanel = this.dmDsSchemaService.fetchTablePropertyUiPanel(uid, dsDO, levelsParam, envVariables);
                    break;
                }
                case USER: {
                    uiPanel = this.dmDsSchemaService.fetchUserPropertyUiPanel(uid, dsDO, levelsParam, envVariables);
                    break;
                }
                case ROLE: {
                    uiPanel = this.dmDsSchemaService.fetchRolePropertyUiPanel(uid, dsDO, levelsParam, envVariables);
                    break;
                }
                case Synonym: {
                    uiPanel = this.dmDsSchemaService.fetchSynonymPropertyUiPanel(uid, dsDO, levelsParam, envVariables);
                    break;
                }
                default: {
                    throw new UnsupportedOperationException();
                }
            }

            TableEditorForm editorForm = UiWebUtil.tableEditorUi2Form(uiPanel);

            this.dsUiEditorCache.put(cacheKey, editorForm);
            return editorForm;
        }
    }

    @Override
    public PropertyEditorUiData loadPropertyData(String puid, String uid, DsLevels levels, UmiTypes types, String leafName) {
        DmDsDO dsDO = levels.dsDO();
        Map<UmiTypes, Object> levelsParam = levels.levelsParam();

        Value value = this.dmDsSchemaService.detailLeaf(uid, dsDO, levelsParam, types, leafName, true);
        if (value == null) {
            throw new ErrorMessageException(DmI18nUtils.getMessage(I18nDmMsgKeys.DS_OBJECT_NOT_EXIST.name(), leafName));
        }

        switch (types) {
            case Job: {
                return DmConvertUtils.convertToJobUiData((RdbJob) value);
            }
            case Materialized:
            case View: {
                return DmConvertUtils.convertToViewUiData((RdbView) value);
            }
            case ScheduleJob: {
                return DmConvertUtils.convertToScheduleJobUiData((RdbScheduleJob) value);
            }
            case DBLink: {
                return DmConvertUtils.convertToDbLinkUiData((RdbDbLink) value);
            }
            case Procedure: {
                return DmConvertUtils.convertToProcedureUiData((RdbProcedure) value);
            }
            case Function: {
                return DmConvertUtils.convertToFunctionUiData((RdbFunction) value);
            }
            case Trigger: {
                return DmConvertUtils.convertToTriggerUiData((RdbTrigger) value);
            }
            case Table: {
                return DmConvertUtils.convertToTableUiData((RdbTable) value);
            }
            case Sequence: {
                return DmConvertUtils.convertToSequence((RdbSequence) value);
            }
            case USER: {
                return DmConvertUtils.convertToUser((RdbUser) value);
            }
            case ROLE: {
                return DmConvertUtils.convertToRole((RdbRole) value);
            }
            case Synonym: {
                return DmConvertUtils.convertToSynonym((RdbSynonym) value);
            }
            default: {
                throw new UnsupportedOperationException();
            }
        }

    }
}
