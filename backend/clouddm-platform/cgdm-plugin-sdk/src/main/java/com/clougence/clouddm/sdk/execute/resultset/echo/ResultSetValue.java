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
package com.clougence.clouddm.sdk.execute.resultset.echo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class ResultSetValue {

    private boolean complete;  // in cache file is complete
    private boolean mask;
    private boolean error;
    private long    moreSize;  // totalSize - value.size
    private long    totalSize; // dataSize
    private String  value;     // data for display

    public static ResultSetValue of(boolean complete, boolean mask, String value, long moreSize, long totalSize) {
        ResultSetValue v = new ResultSetValue();
        v.setError(false);
        v.setComplete(complete);
        v.setMask(mask);
        v.setTotalSize(totalSize);
        v.setMoreSize(moreSize);
        v.setValue(value);
        return v;
    }

    public static ResultSetValue ofError(boolean complete, boolean mask, String message) {
        ResultSetValue v = new ResultSetValue();
        v.setError(true);
        v.setComplete(complete);
        v.setMask(mask);
        v.setTotalSize(0);
        v.setMoreSize(0);
        v.setValue(message);
        return v;
    }

    @Override
    public String toString() {
        return this.value;
    }
}
