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
 */package com.clougence.clouddm.worker.provider;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import jakarta.annotation.Resource;

import org.springframework.stereotype.Service;

import com.clougence.clouddm.api.sidecar.definition.DefinitionRService;
import com.clougence.clouddm.base.metadata.ds.DataSourceConfig;
import com.clougence.clouddm.base.metadata.ui.form.UiPanel;
import com.clougence.clouddm.comm.RSocketApiClass;
import com.clougence.clouddm.comm.model.RSocketSendDTO;
import com.clougence.clouddm.platform.plugin.PluginManager;
import com.clougence.clouddm.sdk.execute.session.Session;
import com.clougence.clouddm.sdk.execute.session.SessionContextDTO;
import com.clougence.clouddm.sdk.execute.session.SessionSpi;
import com.clougence.clouddm.sdk.ui.editor.property.PropertyUiPanel;
import com.clougence.clouddm.sdk.ui.editor.table.TableEditorUiPanel;
import com.clougence.clouddm.sdk.ui.editor.table.TableEditorVarKeys;
import com.clougence.clouddm.worker.component.definition.DefinitionManager;
import com.clougence.clouddm.worker.component.resource.OnlineDsResourceManager;
import com.clougence.clouddm.worker.component.session.SessionManager;
import com.clougence.schema.umi.struts.UmiTypes;
import com.clougence.utils.ExceptionUtils;
import com.clougence.utils.StringUtils;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RSocketApiClass
public class DefinitionRServiceProvider implements DefinitionRService {

    @Resource
    private OnlineDsResourceManager onlineRM;
    @Resource
    private SessionManager          sessionManager;
    @Resource
    private DefinitionManager       definitionManager;

    protected String genSessionId() {
        return UUID.randomUUID().toString().replace("-", "");
    }

    protected SessionContextDTO context(DataSourceConfig dbConfig, Map<UmiTypes, Object> levelsParam) {
        Map<String, Object> params = new HashMap<>();
        if (levelsParam != null) {
            params.put(SessionSpi.PARAMS_DEFAULT_DB, StringUtils.toString(levelsParam.get(UmiTypes.Catalog)));
            params.put(SessionSpi.PARAMS_DEFAULT_SCHEMA, StringUtils.toString(levelsParam.get(UmiTypes.Schema)));
        }

        SessionSpi spi = PluginManager.findSessionSpi(dbConfig.getDataSourceType());
        return spi.createSessionContext(dbConfig, params);
    }

    protected Session session(DataSourceConfig dbConfig, Map<UmiTypes, Object> levelsParam) {
        SessionContextDTO contextDTO = context(dbConfig, levelsParam);
        contextDTO.setSessionId(genSessionId());
        return this.sessionManager.createSession(this.onlineRM, dbConfig, contextDTO);
    }

    @Override
    public TableEditorUiPanel fetchTableEditorUiPanel(RSocketSendDTO sendDTO, DataSourceConfig dbConfig, Map<UmiTypes, Object> levelsParam, Map<String, String> envVariables) {
        SessionContextDTO contextDTO = context(dbConfig, levelsParam);
        try (Session s = session(dbConfig, levelsParam)) {
            Map<String, String> envVars = new HashMap<>(envVariables == null ? Collections.emptyMap() : envVariables);
            envVars.put(TableEditorVarKeys.CURRENT_DB_NAME.codeKey(), contextDTO.getRdbCatalog());
            envVars.put(TableEditorVarKeys.CURRENT_SCHEMA_NAME.codeKey(), contextDTO.getRdbSchema());

            return this.definitionManager.fetchTableEditorUiPanel(dbConfig, s, envVars);
        } catch (Exception e) {
            String msg = "fetchTableEditorDef error.msg:" + ExceptionUtils.getRootCauseMessage(e);
            log.error(msg, e);
            throw new RuntimeException(msg, e);
        }
    }

    @Override
    public UiPanel fetchFunctionUiPanel(RSocketSendDTO sendDTO, DataSourceConfig dbConfig, Map<UmiTypes, Object> levelsParam, Map<String, String> envVariables) {
        try (Session s = session(dbConfig, levelsParam)) {
            Map<String, String> envVars = new HashMap<>(envVariables == null ? Collections.emptyMap() : envVariables);
            return this.definitionManager.fetchFunctionUiPanel(dbConfig, s, envVars);
        } catch (Exception e) {
            String msg = "fetchFunctionUiPanel error.msg:" + ExceptionUtils.getRootCauseMessage(e);
            log.error(msg, e);
            throw new RuntimeException(msg, e);
        }
    }

    @Override
    public UiPanel fetchProcedureUiPanel(RSocketSendDTO sendDTO, DataSourceConfig dbConfig, Map<UmiTypes, Object> levelsParam, Map<String, String> envVariables) {
        try (Session s = session(dbConfig, levelsParam)) {
            Map<String, String> envVars = new HashMap<>(envVariables == null ? Collections.emptyMap() : envVariables);
            return this.definitionManager.fetchProcedureUiPanel(dbConfig, s, envVars);
        } catch (Exception e) {
            String msg = "fetchProcedureUiPanel error.msg:" + ExceptionUtils.getRootCauseMessage(e);
            log.error(msg, e);
            throw new RuntimeException(msg, e);
        }
    }

    @Override
    public UiPanel fetchViewUiPanel(RSocketSendDTO sendDTO, DataSourceConfig dbConfig, Map<UmiTypes, Object> levelsParam, Map<String, String> envVariables) {
        try (Session s = session(dbConfig, levelsParam)) {
            Map<String, String> envVars = new HashMap<>(envVariables == null ? Collections.emptyMap() : envVariables);
            return this.definitionManager.fetchViewUiPanel(dbConfig, s, envVars);
        } catch (Exception e) {
            String msg = "fetchViewUiPanel error.msg:" + ExceptionUtils.getRootCauseMessage(e);
            log.error(msg, e);
            throw new RuntimeException(msg, e);
        }
    }

    @Override
    public UiPanel fetchTablespaceUiPanel(RSocketSendDTO sendDTO, DataSourceConfig dbConfig, Map<UmiTypes, Object> levelsParam, Map<String, String> envVariables) {
        try (Session s = session(dbConfig, levelsParam)) {
            Map<String, String> envVars = new HashMap<>(envVariables == null ? Collections.emptyMap() : envVariables);
            return this.definitionManager.fetchTablespaceEditorUiPanel(dbConfig, s, envVars);
        } catch (Exception e) {
            String msg = "fetchViewUiPanel error.msg:" + ExceptionUtils.getRootCauseMessage(e);
            log.error(msg, e);
            throw new RuntimeException(msg, e);
        }
    }

    @Override
    public UiPanel fetchDbLinkUiPanel(RSocketSendDTO sendDTO, DataSourceConfig dbConfig, Map<UmiTypes, Object> levelsParam, Map<String, String> envVariables) {
        try (Session s = session(dbConfig, levelsParam)) {
            Map<String, String> envVars = new HashMap<>(envVariables == null ? Collections.emptyMap() : envVariables);
            return this.definitionManager.fetchDbLinkEditorUiPanel(dbConfig, s, envVars);
        } catch (Exception e) {
            String msg = "fetchViewUiPanel error.msg:" + ExceptionUtils.getRootCauseMessage(e);
            log.error(msg, e);
            throw new RuntimeException(msg, e);
        }
    }

    @Override
    public UiPanel fetchJobUiPanel(RSocketSendDTO sendDTO, DataSourceConfig dbConfig, Map<UmiTypes, Object> levelsParam, Map<String, String> envVariables) {
        try (Session s = session(dbConfig, levelsParam)) {
            Map<String, String> envVars = new HashMap<>(envVariables == null ? Collections.emptyMap() : envVariables);
            return this.definitionManager.fetchJobEditorUiPanel(dbConfig, s, envVars);
        } catch (Exception e) {
            String msg = "fetchViewUiPanel error.msg:" + ExceptionUtils.getRootCauseMessage(e);
            log.error(msg, e);
            throw new RuntimeException(msg, e);
        }
    }

    @Override
    public UiPanel fetchScheduleJobEditorUiPanel(RSocketSendDTO sendDTO, DataSourceConfig dbConfig, Map<UmiTypes, Object> levelsParam, Map<String, String> envVariables) {
        try (Session s = session(dbConfig, levelsParam)) {
            Map<String, String> envVars = new HashMap<>(envVariables == null ? Collections.emptyMap() : envVariables);
            return this.definitionManager.fetchScheduleJobEditorUiPanel(dbConfig, s, envVars);
        } catch (Exception e) {
            String msg = "fetchViewUiPanel error.msg:" + ExceptionUtils.getRootCauseMessage(e);
            log.error(msg, e);
            throw new RuntimeException(msg, e);
        }
    }

    @Override
    public PropertyUiPanel fetchJobPropertyUiPanel(RSocketSendDTO sendDTO, DataSourceConfig dbConfig, Map<UmiTypes, Object> levelsParam, Map<String, String> envVariables) {
        try (Session s = session(dbConfig, levelsParam)) {
            Map<String, String> envVars = new HashMap<>(envVariables == null ? Collections.emptyMap() : envVariables);
            return this.definitionManager.fetchJobPropertyUiPanel(dbConfig, s, envVars);
        } catch (Exception e) {
            String msg = "fetchViewUiPanel error.msg:" + ExceptionUtils.getRootCauseMessage(e);
            log.error(msg, e);
            throw new RuntimeException(msg, e);
        }
    }

    @Override
    public PropertyUiPanel fetchUserPropertyUiPanel(RSocketSendDTO sendDTO, DataSourceConfig dbConfig, Map<UmiTypes, Object> levelsParam, Map<String, String> envVariables) {
        try (Session s = session(dbConfig, levelsParam)) {
            Map<String, String> envVars = new HashMap<>(envVariables == null ? Collections.emptyMap() : envVariables);
            return this.definitionManager.fetchUserPropertyUiPanel(dbConfig, s, envVars);
        } catch (Exception e) {
            String msg = "fetchViewUiPanel error.msg:" + ExceptionUtils.getRootCauseMessage(e);
            log.error(msg, e);
            throw new RuntimeException(msg, e);
        }
    }

    @Override
    public PropertyUiPanel fetchDbLinkPropertyUiPanel(RSocketSendDTO sendDTO, DataSourceConfig dbConfig, Map<UmiTypes, Object> levelsParam, Map<String, String> envVariables) {
        try (Session s = session(dbConfig, levelsParam)) {
            Map<String, String> envVars = new HashMap<>(envVariables == null ? Collections.emptyMap() : envVariables);
            return this.definitionManager.fetchDbLinkPropertyUiPanel(dbConfig, s, envVars);
        } catch (Exception e) {
            String msg = "fetchViewUiPanel error.msg:" + ExceptionUtils.getRootCauseMessage(e);
            log.error(msg, e);
            throw new RuntimeException(msg, e);
        }
    }

    @Override
    public PropertyUiPanel fetchTablePropertyUiPanel(RSocketSendDTO sendDTO, DataSourceConfig dbConfig, Map<UmiTypes, Object> levelsParam, Map<String, String> envVariables) {
        try (Session s = session(dbConfig, levelsParam)) {
            Map<String, String> envVars = new HashMap<>(envVariables == null ? Collections.emptyMap() : envVariables);
            return this.definitionManager.fetchTablePropertyUiPanel(dbConfig, s, envVars);
        } catch (Exception e) {
            String msg = "fetchViewUiPanel error.msg:" + ExceptionUtils.getRootCauseMessage(e);
            log.error(msg, e);
            throw new RuntimeException(msg, e);
        }
    }

    @Override
    public PropertyUiPanel fetchSequencePropertyUiPanel(RSocketSendDTO sendDTO, DataSourceConfig dbConfig, Map<UmiTypes, Object> levelsParam, Map<String, String> envVariables) {
        try (Session s = session(dbConfig, levelsParam)) {
            Map<String, String> envVars = new HashMap<>(envVariables == null ? Collections.emptyMap() : envVariables);
            return this.definitionManager.fetchSequencePropertyUiPanel(dbConfig, s, envVars);
        } catch (Exception e) {
            String msg = "fetchViewUiPanel error.msg:" + ExceptionUtils.getRootCauseMessage(e);
            log.error(msg, e);
            throw new RuntimeException(msg, e);
        }
    }

    @Override
    public PropertyUiPanel fetchSynonymPropertyUiPanel(RSocketSendDTO sendDTO, DataSourceConfig dbConfig, Map<UmiTypes, Object> levelsParam, Map<String, String> envVariables) {
        try (Session s = session(dbConfig, levelsParam)) {
            Map<String, String> envVars = new HashMap<>(envVariables == null ? Collections.emptyMap() : envVariables);
            return this.definitionManager.fetchSynonymPropertyUiPanel(dbConfig, s, envVars);
        } catch (Exception e) {
            String msg = "fetchViewUiPanel error.msg:" + ExceptionUtils.getRootCauseMessage(e);
            log.error(msg, e);
            throw new RuntimeException(msg, e);
        }
    }

    @Override
    public PropertyUiPanel fetchTriggerPropertyUiPanel(RSocketSendDTO sendDTO, DataSourceConfig dbConfig, Map<UmiTypes, Object> levelsParam, Map<String, String> envVariables) {
        try (Session s = session(dbConfig, levelsParam)) {
            Map<String, String> envVars = new HashMap<>(envVariables == null ? Collections.emptyMap() : envVariables);
            return this.definitionManager.fetchTriggerPropertyUiPanel(dbConfig, s, envVars);
        } catch (Exception e) {
            String msg = "fetchViewUiPanel error.msg:" + ExceptionUtils.getRootCauseMessage(e);
            log.error(msg, e);
            throw new RuntimeException(msg, e);
        }
    }

    @Override
    public PropertyUiPanel fetchViewPropertyUiPanel(RSocketSendDTO sendDTO, DataSourceConfig dbConfig, Map<UmiTypes, Object> levelsParam, Map<String, String> envVariables) {
        try (Session s = session(dbConfig, levelsParam)) {
            Map<String, String> envVars = new HashMap<>(envVariables == null ? Collections.emptyMap() : envVariables);
            return this.definitionManager.fetchViewPropertyUiPanel(dbConfig, s, envVars);
        } catch (Exception e) {
            String msg = "fetchViewUiPanel error.msg:" + ExceptionUtils.getRootCauseMessage(e);
            log.error(msg, e);
            throw new RuntimeException(msg, e);
        }
    }

    @Override
    public PropertyUiPanel fetchMaterializedViewPropertyUiPanel(RSocketSendDTO sendDTO, DataSourceConfig dbConfig, Map<UmiTypes, Object> levelsParam,
                                                                Map<String, String> envVariables) {
        try (Session s = session(dbConfig, levelsParam)) {
            Map<String, String> envVars = new HashMap<>(envVariables == null ? Collections.emptyMap() : envVariables);
            return this.definitionManager.fetchMaterializedViewPropertyUiPanel(dbConfig, s, envVars);
        } catch (Exception e) {
            String msg = "fetchViewUiPanel error.msg:" + ExceptionUtils.getRootCauseMessage(e);
            log.error(msg, e);
            throw new RuntimeException(msg, e);
        }
    }

    @Override
    public PropertyUiPanel fetchRolePropertyUiPanel(RSocketSendDTO sendDTO, DataSourceConfig dbConfig, Map<UmiTypes, Object> levelsParam, Map<String, String> envVariables) {
        try (Session s = session(dbConfig, levelsParam)) {
            Map<String, String> envVars = new HashMap<>(envVariables == null ? Collections.emptyMap() : envVariables);
            return this.definitionManager.fetchRolePropertyUiPanel(dbConfig, s, envVars);
        } catch (Exception e) {
            String msg = "fetchViewUiPanel error.msg:" + ExceptionUtils.getRootCauseMessage(e);
            log.error(msg, e);
            throw new RuntimeException(msg, e);
        }
    }

    @Override
    public PropertyUiPanel fetchScheduleJobPropertyUiPanel(RSocketSendDTO sendDTO, DataSourceConfig dbConfig, Map<UmiTypes, Object> levelsParam, Map<String, String> envVariables) {
        try (Session s = session(dbConfig, levelsParam)) {
            Map<String, String> envVars = new HashMap<>(envVariables == null ? Collections.emptyMap() : envVariables);
            return this.definitionManager.fetchScheduleJobPropertyUiPanel(dbConfig, s, envVars);
        } catch (Exception e) {
            String msg = "fetchViewUiPanel error.msg:" + ExceptionUtils.getRootCauseMessage(e);
            log.error(msg, e);
            throw new RuntimeException(msg, e);
        }
    }

    @Override
    public PropertyUiPanel fetchProcedurePropertyUiPanel(RSocketSendDTO sendDTO, DataSourceConfig dbConfig, Map<UmiTypes, Object> levelsParam, Map<String, String> envVariables) {
        try (Session s = session(dbConfig, levelsParam)) {
            Map<String, String> envVars = new HashMap<>(envVariables == null ? Collections.emptyMap() : envVariables);
            return this.definitionManager.fetchProcedurePropertyUiPanel(dbConfig, s, envVars);
        } catch (Exception e) {
            String msg = "fetchViewUiPanel error.msg:" + ExceptionUtils.getRootCauseMessage(e);
            log.error(msg, e);
            throw new RuntimeException(msg, e);
        }
    }

    @Override
    public PropertyUiPanel fetchFunctionPropertyUiPanel(RSocketSendDTO sendDTO, DataSourceConfig dbConfig, Map<UmiTypes, Object> levelsParam, Map<String, String> envVariables) {
        try (Session s = session(dbConfig, levelsParam)) {
            Map<String, String> envVars = new HashMap<>(envVariables == null ? Collections.emptyMap() : envVariables);
            return this.definitionManager.fetchFunctionPropertyUiPanel(dbConfig, s, envVars);
        } catch (Exception e) {
            String msg = "fetchViewUiPanel error.msg:" + ExceptionUtils.getRootCauseMessage(e);
            log.error(msg, e);
            throw new RuntimeException(msg, e);
        }
    }

    @Override
    public UiPanel fetchTriggerEditorUiPanel(RSocketSendDTO sendDTO, DataSourceConfig dbConfig, Map<UmiTypes, Object> levelsParam, Map<String, String> envVariables) {
        //        SessionContextDTO contextDTO = context(dbConfig, levelsParam);
        try (Session s = session(dbConfig, levelsParam)) {
            Map<String, String> envVars = new HashMap<>(envVariables == null ? Collections.emptyMap() : envVariables);
            //            envVars.put(UmiTypes.Schema.getTypeName(), contextDTO.getRdbSchema());
            //            envVars.put(UmiTypes.Catalog.getTypeName(), contextDTO.getRdbCatalog());
            return this.definitionManager.fetchTriggerEditorUiPanel(dbConfig, s, envVars);
        } catch (Exception e) {
            String msg = "fetchViewUiPanel error.msg:" + ExceptionUtils.getRootCauseMessage(e);
            log.error(msg, e);
            throw new RuntimeException(msg, e);
        }
    }
}
