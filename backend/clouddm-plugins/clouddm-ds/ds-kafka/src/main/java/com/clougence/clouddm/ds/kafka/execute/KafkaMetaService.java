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
package com.clougence.clouddm.ds.kafka.execute;

import java.util.List;
import java.util.Map;

import com.clougence.clouddm.ds.kafka.i18n.KafkaDsI18nKeys;
import com.clougence.clouddm.sdk.execute.meta.DsElement;
import com.clougence.clouddm.sdk.execute.meta.DsMetaService;
import com.clougence.clouddm.sdk.model.exception.ThirdPartyApiException;
import com.clougence.schema.umi.special.rdb.RdbColumn;
import com.clougence.schema.umi.struts.UmiTypes;
import com.clougence.schema.umi.struts.Value;

public class KafkaMetaService implements DsMetaService {
    private final KafkaSession session;

    public KafkaMetaService(KafkaSession session){
        this.session = session;
    }

    @Override
    public void testConnect() {
        session.testConnect();
    }

    @Override
    public String getVersion() {
        // The Admin API does not expose the broker product version.
        return null;
    }

    @Override
    public Map<String, String> getSqlParserParameters() { return Map.of(); }

    @Override
    public String getCurrentCatalog() { return null; }

    @Override
    public String getCurrentSchema() { return null; }

    @Override
    public List<DsElement> listLevels(List<UmiTypes> levels, Map<UmiTypes, Object> parameters) {
        throw ThirdPartyApiException.as().with(KafkaDsI18nKeys.KAFKA_METADATA_UNSUPPORTED);
    }

    @Override
    public DsElement detailLevel(List<UmiTypes> levels, Map<UmiTypes, Object> parameters) {
        throw ThirdPartyApiException.as().with(KafkaDsI18nKeys.KAFKA_METADATA_UNSUPPORTED);
    }

    @Override
    public List<DsElement> listLeaf(Map<UmiTypes, Object> parameters, UmiTypes type, String pattern) {
        throw ThirdPartyApiException.as().with(KafkaDsI18nKeys.KAFKA_METADATA_UNSUPPORTED);
    }

    @Override
    public Value detailLeaf(Map<UmiTypes, Object> parameters, UmiTypes type, String name) {
        throw ThirdPartyApiException.as().with(KafkaDsI18nKeys.KAFKA_METADATA_UNSUPPORTED);
    }

    @Override
    public Value fetchSelectObject(Map<UmiTypes, Object> parameters, String name) {
        throw ThirdPartyApiException.as().with(KafkaDsI18nKeys.KAFKA_METADATA_UNSUPPORTED);
    }

    @Override
    public Map<String, List<RdbColumn>> batchColumns(Map<UmiTypes, Object> parameters, UmiTypes type, List<String> names) {
        throw ThirdPartyApiException.as().with(KafkaDsI18nKeys.KAFKA_METADATA_UNSUPPORTED);
    }

    @Override
    public String loadTableEditor(Map<UmiTypes, Object> parameters, String table) {
        throw ThirdPartyApiException.as().with(KafkaDsI18nKeys.KAFKA_METADATA_UNSUPPORTED);
    }

    @Override
    public List<String> requestObjectScript(Map<UmiTypes, Object> parameters, UmiTypes type, String name) {
        throw ThirdPartyApiException.as().with(KafkaDsI18nKeys.KAFKA_METADATA_UNSUPPORTED);
    }
}
