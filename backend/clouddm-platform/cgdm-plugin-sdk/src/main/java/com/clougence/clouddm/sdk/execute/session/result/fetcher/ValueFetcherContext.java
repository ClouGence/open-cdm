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
package com.clougence.clouddm.sdk.execute.session.result.fetcher;

import java.io.File;

import com.clougence.clouddm.sdk.execute.session.ResultColMeta;
import com.clougence.clouddm.sdk.execute.session.result.ReaderOptions;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ValueFetcherContext {

    private final ResultColMeta     meta;
    private final ReaderOptions     options;
    private String                  vfcId;
    private File                    tmpFile;
    private ValueFetcherContextData context;
    private boolean                 errStatus = false;
    private Exception               errObject = null;

    public ValueFetcherContext(String vfcId, ResultColMeta meta, ReaderOptions options){
        this.vfcId = vfcId;
        this.meta = meta;
        this.options = options;
    }

    public void free() {
        if (this.context != null) {
            this.context.free();
        }
        if (this.tmpFile != null) {
            if (this.tmpFile.exists()) {
                this.tmpFile.delete();
            }
            this.tmpFile = null;
        }
        this.context = null;
        this.errStatus = false;
        this.errObject = null;
    }

    public void markError(Exception e) {
        this.errStatus = true;
        this.errObject = e;
    }
}
