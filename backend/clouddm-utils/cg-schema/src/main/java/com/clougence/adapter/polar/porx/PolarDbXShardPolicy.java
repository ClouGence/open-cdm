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
package com.clougence.adapter.polar.porx;

import java.util.List;

import com.clougence.utils.CollectionUtils;

/**
 * @author bucketli 2020/12/25 18:19
 */
public enum PolarDbXShardPolicy {

    KEY,

    HASH,

    UNI_HASH,

    MM,

    DD,

    WEEK,

    MMDD,

    YYYYMM,

    YYYYWEEK,

    YYYYDD,

    YYYYMM_OPT,

    YYYYWEEK_OPT,

    YYYYDD_OPT,

    RANGE_COLUMNS,

    LIST,

    LIST_COLUMNS,

    RANGE,

    CO_HASH;

    private Integer param;

    public int getParam() { return param; }

    public void setParam(Integer param) { this.param = param; }

    public String genExpression(List<String> cols) {
        StringBuilder sb = new StringBuilder();
        sb.append(this.name());
        sb.append("(");
        if (CollectionUtils.isNotEmpty(cols)) {
            sb.append(genColsStrWithComma(cols));
            if (param != null) {
                sb.append(",");
                sb.append(param);
            }
        }
        sb.append(")");

        return sb.toString();
    }

    private String genColsStrWithComma(List<String> cols) {
        if (cols == null) {
            return "";
        }

        boolean first = true;
        StringBuilder sb = new StringBuilder();
        for (String col : cols) {
            if (first) {
                first = false;
            } else {
                sb.append(",");
            }

            sb.append(col);
        }

        return sb.toString();
    }
}
