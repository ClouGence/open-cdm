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
package com.clougence.clouddm.ds.etcd.execute;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.clougence.clouddm.ds.etcd.i18n.EtcdDsI18nKeys;
import com.clougence.clouddm.sdk.execute.meta.DsElement;
import com.clougence.clouddm.sdk.execute.meta.DsMetaService;
import com.clougence.clouddm.sdk.model.exception.ThirdPartyApiException;
import com.clougence.schema.umi.special.rdb.RdbColumn;
import com.clougence.schema.umi.special.rdb.RdbValue;
import com.clougence.schema.umi.struts.UmiTypes;
import com.clougence.schema.umi.struts.Value;

public class EtcdMetaService implements DsMetaService {

    private final EtcdSession session;

    public EtcdMetaService(EtcdSession session){
        this.session = session;
    }

    @Override
    public void testConnect() {
        this.session.testConnect();
    }

    @Override
    public String getVersion() {
        this.session.testConnect();
        return null;
    }

    @Override
    public Map<String, String> getSqlParserParameters() {
        return Map.of();
    }

    @Override
    public String getCurrentCatalog() {
        return null;
    }

    @Override
    public String getCurrentSchema() {
        return null;
    }

    @Override
    public List<DsElement> listLevels(List<UmiTypes> levels, Map<UmiTypes, Object> levelsParam) {
        return List.of();
    }

    @Override
    public DsElement detailLevel(List<UmiTypes> levels, Map<UmiTypes, Object> levelsParam) {
        throw ThirdPartyApiException.as().with(EtcdDsI18nKeys.ETCD_METADATA_UNSUPPORTED);
    }

    @Override
    public List<DsElement> listLeaf(Map<UmiTypes, Object> levelsParam, UmiTypes leafType, String pattern) {
        if (leafType != UmiTypes.Key) {
            throw ThirdPartyApiException.as().with(EtcdDsI18nKeys.ETCD_METADATA_UNSUPPORTED);
        }
        String prefix = prefixOf(pattern);
        List<Map<String, String>> rows = this.session.call(clients -> clients.listKeys(prefix));
        List<DsElement> result = new ArrayList<>();
        for (Map<String, String> row : rows) {
            DsElement element = new DsElement();
            element.setObjId(-1);
            element.setObjName(row.get("key"));
            element.setObjType(UmiTypes.Key);
            element.setObjAttr(new HashMap<>(row));
            result.add(element);
        }
        result.sort(Comparator.comparing(DsElement::getObjName));
        return result;
    }

    @Override
    public Value detailLeaf(Map<UmiTypes, Object> levelsParam, UmiTypes leafType, String leafName) {
        if (leafType != UmiTypes.Key) {
            throw ThirdPartyApiException.as().with(EtcdDsI18nKeys.ETCD_METADATA_UNSUPPORTED);
        }
        Map<String, String> row = this.session.call(clients -> clients.getKey(leafName));
        if (row == null) {
            return null;
        }
        RdbValue value = new RdbValue();
        value.setValue(leafName);
        value.setUmiType(UmiTypes.Key);
        for (Map.Entry<String, String> entry : row.entrySet()) {
            value.setAttribute(entry.getKey(), entry.getValue());
        }
        return value;
    }

    @Override
    public Value fetchSelectObject(Map<UmiTypes, Object> levelsParam, String leafName) {
        throw ThirdPartyApiException.as().with(EtcdDsI18nKeys.ETCD_METADATA_UNSUPPORTED);
    }

    @Override
    public Map<String, List<RdbColumn>> batchColumns(Map<UmiTypes, Object> levelsParam, UmiTypes leafType, List<String> leafNames) {
        throw ThirdPartyApiException.as().with(EtcdDsI18nKeys.ETCD_METADATA_UNSUPPORTED);
    }

    @Override
    public String loadTableEditor(Map<UmiTypes, Object> levelsParam, String table) {
        throw ThirdPartyApiException.as().with(EtcdDsI18nKeys.ETCD_METADATA_UNSUPPORTED);
    }

    @Override
    public List<String> requestObjectScript(Map<UmiTypes, Object> levelsParam, UmiTypes leafType, String leafName) {
        return List.of();
    }

    private static String prefixOf(String pattern) {
        if (pattern == null || pattern.isBlank() || "*".equals(pattern)) {
            return "";
        }
        int star = pattern.indexOf('*');
        if (star >= 0) {
            return pattern.substring(0, star);
        }
        return pattern;
    }
}
