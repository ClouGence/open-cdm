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
package com.clougence.clouddm.ds.es.execute;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import com.clougence.clouddm.ds.es.definition.ui.EsCommandTemplates;
import com.clougence.clouddm.ds.es.i18n.EsDsI18nKeys;
import com.clougence.clouddm.ds.es.metadata.EsFieldType;
import com.clougence.clouddm.sdk.execute.meta.DsElement;
import com.clougence.clouddm.sdk.execute.meta.DsMetaService;
import com.clougence.clouddm.sdk.model.exception.ThirdPartyApiException;
import com.clougence.clouddm.sdk.sql.SqlParserParameters;
import com.clougence.schema.umi.special.rdb.RdbColumn;
import com.clougence.schema.umi.special.rdb.RdbTable;
import com.clougence.schema.umi.struts.UmiTypes;
import com.clougence.schema.umi.struts.Value;
import com.fasterxml.jackson.databind.JsonNode;

public class EsMetaService implements DsMetaService {
    private final EsSession session;

    public EsMetaService(EsSession session){
        this.session = session;
    }

    @Override
    public void testConnect() {
        session.readVersion();
    }

    @Override
    public String getVersion() { return session.readVersion().number(); }

    @Override
    public Map<String, String> getSqlParserParameters() { return Map.of(SqlParserParameters.VERSION, getVersion()); }

    @Override
    public String getCurrentCatalog() { return null; }

    @Override
    public String getCurrentSchema() { return null; }

    @Override
    public List<DsElement> listLevels(List<UmiTypes> levels, Map<UmiTypes, Object> parameters) {
        throw ThirdPartyApiException.as().with(EsDsI18nKeys.ES_OPERATION_UNSUPPORTED);
    }

    @Override
    public DsElement detailLevel(List<UmiTypes> levels, Map<UmiTypes, Object> parameters) {
        throw ThirdPartyApiException.as().with(EsDsI18nKeys.ES_OPERATION_UNSUPPORTED);
    }

    @Override
    public List<DsElement> listLeaf(Map<UmiTypes, Object> parameters, UmiTypes type, String pattern) {
        if (type != UmiTypes.Table) {
            throw ThirdPartyApiException.as().with(EsDsI18nKeys.ES_OPERATION_UNSUPPORTED);
        }
        List<DsElement> result = new ArrayList<>();
        for (String name : session.readIndices()) {
            DsElement element = new DsElement();
            element.setObjId(-1);
            element.setObjName(name);
            element.setObjType(UmiTypes.Table);
            element.setObjAttr(Map.of("quickQuery", EsCommandTemplates.quickQuery(name, 20)));
            result.add(element);
        }
        if (pattern != null && !pattern.isBlank() && !pattern.equals("*")) {
            String regex = Pattern.quote(pattern).replace("*", "\\E.*\\Q").replace("?", "\\E.\\Q");
            Pattern filter = Pattern.compile(regex, Pattern.DOTALL);
            result.removeIf(element -> !filter.matcher(element.getObjName()).matches());
        }
        result.sort(Comparator.comparing(DsElement::getObjName));
        return result;
    }

    @Override
    public Value detailLeaf(Map<UmiTypes, Object> parameters, UmiTypes type, String name) {
        if (type != UmiTypes.Table) {
            throw ThirdPartyApiException.as().with(EsDsI18nKeys.ES_OPERATION_UNSUPPORTED);
        }
        JsonNode definition = session.readIndexDefinition(name);
        RdbTable table = new RdbTable();
        table.setName(name);
        table.setTableType("INDEX");
        table.setColumns(new LinkedHashMap<>());
        for (String section : List.of("mappings", "settings", "aliases")) {
            table.setAttribute(section, definition.get(section).toString());
        }
        JsonNode mapping = definition.get("mappings");
        // Older ES 7 indices may expose a mapping type wrapper.
        if (!mapping.has("properties") && !mapping.has("runtime") && !mapping.has("_meta") && mapping.size() == 1) {
            JsonNode wrapped = mapping.elements().next();
            if (wrapped.has("properties")) {
                mapping = wrapped;
            }
        }
        collectFields(table, mapping.path("properties"), "", false);
        collectFields(table, mapping.path("runtime"), "", true);
        return table;
    }

    private void collectFields(RdbTable table, JsonNode fields, String prefix, boolean runtime) {
        if (!fields.isMissingNode() && !fields.isObject()) {
            throw ThirdPartyApiException.as().with(EsDsI18nKeys.ES_METADATA_INVALID);
        }
        for (var entry : fields.properties()) {
            JsonNode mapping = entry.getValue();
            if (!mapping.isObject()) {
                throw ThirdPartyApiException.as().with(EsDsI18nKeys.ES_METADATA_INVALID);
            }
            String name = prefix + entry.getKey();
            String type = mapping.path("type").asText("object");
            RdbColumn column = new RdbColumn();
            column.setTable(table.getName());
            column.setName(name);
            RdbColumn previous = table.getColumns().get(name);
            int index = table.getColumns().size();
            if (previous != null) {
                index = previous.getIndex();
            }
            column.setIndex(index);
            column.setSqlType(EsFieldType.valueOfCode(type));
            column.setComment(mapping.toString());
            column.setAttribute("mapping", mapping.toString());
            column.setAttribute("runtime", String.valueOf(runtime));
            table.addColumn(column);
            collectFields(table, mapping.path("properties"), name + ".", runtime);
            collectFields(table, mapping.path("fields"), name + ".", runtime);
        }
    }

    @Override
    public Value fetchSelectObject(Map<UmiTypes, Object> parameters, String name) {
        return detailLeaf(parameters, UmiTypes.Table, name);
    }

    @Override
    public Map<String, List<RdbColumn>> batchColumns(Map<UmiTypes, Object> parameters, UmiTypes type, List<String> names) {
        if (type != UmiTypes.Table) {
            throw ThirdPartyApiException.as().with(EsDsI18nKeys.ES_OPERATION_UNSUPPORTED);
        }
        Map<String, List<RdbColumn>> result = new LinkedHashMap<>();
        for (String name : names) {
            RdbTable table = (RdbTable) detailLeaf(parameters, type, name);
            result.put(name, new ArrayList<>(table.getColumns().values()));
        }
        return result;
    }

    @Override
    public String loadTableEditor(Map<UmiTypes, Object> parameters, String table) {
        throw ThirdPartyApiException.as().with(EsDsI18nKeys.ES_OPERATION_UNSUPPORTED);
    }

    @Override
    public List<String> requestObjectScript(Map<UmiTypes, Object> parameters, UmiTypes type, String name) {
        if (type != UmiTypes.Table) {
            throw ThirdPartyApiException.as().with(EsDsI18nKeys.ES_OPERATION_UNSUPPORTED);
        }
        return EsCommandTemplates.commands(name);
    }
}
