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
package com.clougence.clouddm.ds.es.definition.ui;

import java.util.ArrayList;
import java.util.List;

import com.clougence.clouddm.ds.es.i18n.EsDsI18nKeys;
import com.clougence.clouddm.sdk.ui.template.CmdExample;
import com.clougence.clouddm.sdk.ui.template.CmdTemplateOption;
import com.clougence.clouddm.sdk.ui.template.CmdTemplateSpi;

public class EsCmdTemplateSpi implements CmdTemplateSpi {
    @Override
    public String getQuickQuery(CmdTemplateOption option) {
        String name = option.getTargetName();
        if (name == null || name.isBlank()) {
            return "";
        }
        int limit = option.getDefaultLimit();
        if (limit <= 0) {
            limit = 20;
        }
        return EsCommandTemplates.quickQuery(name, limit);
    }

    @Override
    public List<CmdExample> getExamples() {
        List<CmdExample> examples = new ArrayList<>();
        examples.add(new CmdExample(EsDsI18nKeys.ES_EXAMPLE_SEARCH, EsCommandTemplates.quickQuery("orders", 20)));
        examples.add(new CmdExample(EsDsI18nKeys.ES_EXAMPLE_FILTER, "GET /orders/_search\n{\n  \"size\": 20,\n  \"query\": {\"term\": {\"status.keyword\": \"paid\"}}\n}"));
        examples.add(new CmdExample(EsDsI18nKeys.ES_EXAMPLE_AGGREGATION,
            "GET /orders/_search\n{\n  \"size\": 0,\n  \"aggs\": {\"by_status\": {\"terms\": {\"field\": \"status.keyword\", \"size\": 10}}}\n}"));
        examples.add(new CmdExample(EsDsI18nKeys.ES_EXAMPLE_PUT, "PUT /orders/_doc/demo-001\n{\n  \"status\": \"paid\",\n  \"amount\": 100\n}"));
        examples.add(new CmdExample(EsDsI18nKeys.ES_EXAMPLE_DELETE, "DELETE /orders/_doc/demo-001"));
        examples.add(new CmdExample(EsDsI18nKeys.ES_EXAMPLE_COUNT, "GET /orders/_count"));
        examples.add(new CmdExample(EsDsI18nKeys.ES_EXAMPLE_MAPPING, "GET /orders/_mapping"));
        examples.add(new CmdExample(EsDsI18nKeys.ES_EXAMPLE_SETTINGS, "GET /orders/_settings"));
        examples.add(new CmdExample(EsDsI18nKeys.ES_EXAMPLE_ALIASES, "GET /orders/_alias"));
        return examples;
    }
}
