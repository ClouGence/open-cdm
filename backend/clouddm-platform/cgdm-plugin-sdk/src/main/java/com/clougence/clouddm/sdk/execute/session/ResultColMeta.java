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
package com.clougence.clouddm.sdk.execute.session;

import com.clougence.clouddm.base.metadata.ds.ColMetaData;
import com.clougence.clouddm.sdk.execute.session.result.fetcher.ValueFetcher;

import lombok.Getter;
import lombok.Setter;

/**
 * @author mode 2022/2/17 17:02:59
 */
@Getter
@Setter
public class ResultColMeta {

    private final ColMetaData  meta;
    private final ValueFetcher fetcher;

    public ResultColMeta(ColMetaData meta, ValueFetcher fetcher){
        this.meta = meta;
        this.fetcher = fetcher;
    }
}
