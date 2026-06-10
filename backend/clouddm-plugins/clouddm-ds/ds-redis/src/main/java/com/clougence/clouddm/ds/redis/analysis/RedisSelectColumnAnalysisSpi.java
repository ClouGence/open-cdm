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
package com.clougence.clouddm.ds.redis.analysis;

import java.util.Collections;
import java.util.List;

import com.clougence.clouddm.sdk.analysis.column.SelectColumnAnalysisSpi;
import com.clougence.clouddm.sdk.analysis.column.SelectItem;
import com.clougence.clouddm.sdk.model.analysis.ContextInfo;
import com.clougence.clouddm.sdk.service.execute.MetaService;

public class RedisSelectColumnAnalysisSpi implements SelectColumnAnalysisSpi {

    public RedisSelectColumnAnalysisSpi(MetaService metaService){
    }

    @Override
    public List<SelectItem> parseSelectColumn(String script, ContextInfo info) {
        return Collections.emptyList(); // TODO needs to return the corresponding column information according to the specific Redis command
    }

    @Override
    public boolean supportParseSelectColumn() {
        return true;
    }
}
