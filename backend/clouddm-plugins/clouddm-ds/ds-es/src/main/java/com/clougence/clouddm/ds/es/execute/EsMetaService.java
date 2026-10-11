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
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import com.clougence.clouddm.ds.es.i18n.EsDsI18nKeys;
import com.clougence.clouddm.sdk.execute.meta.DsElement;
import com.clougence.clouddm.sdk.execute.meta.DsMetaService;
import com.clougence.clouddm.sdk.model.exception.ThirdPartyApiException;
import com.clougence.clouddm.sdk.sql.SqlParserParameters;
import com.clougence.schema.umi.special.rdb.RdbColumn;
import com.clougence.schema.umi.struts.UmiTypes;
import com.clougence.schema.umi.struts.Value;

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
            element.setObjAttr(Map.of());
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
        throw ThirdPartyApiException.as().with(EsDsI18nKeys.ES_OPERATION_UNSUPPORTED);
    }

    @Override
    public Value fetchSelectObject(Map<UmiTypes, Object> parameters, String name) {
        throw ThirdPartyApiException.as().with(EsDsI18nKeys.ES_OPERATION_UNSUPPORTED);
    }

    @Override
    public Map<String, List<RdbColumn>> batchColumns(Map<UmiTypes, Object> parameters, UmiTypes type, List<String> names) {
        throw ThirdPartyApiException.as().with(EsDsI18nKeys.ES_OPERATION_UNSUPPORTED);
    }

    @Override
    public String loadTableEditor(Map<UmiTypes, Object> parameters, String table) {
        throw ThirdPartyApiException.as().with(EsDsI18nKeys.ES_OPERATION_UNSUPPORTED);
    }

    @Override
    public List<String> requestObjectScript(Map<UmiTypes, Object> parameters, UmiTypes type, String name) {
        throw ThirdPartyApiException.as().with(EsDsI18nKeys.ES_OPERATION_UNSUPPORTED);
    }
}
