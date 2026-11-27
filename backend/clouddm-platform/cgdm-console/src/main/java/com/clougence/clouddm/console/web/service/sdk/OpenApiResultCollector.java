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
package com.clougence.clouddm.console.web.service.sdk;

import static com.clougence.clouddm.sdk.openapi.OpenApiErrorCode.EXECUTION_TIMEOUT;
import static com.clougence.clouddm.sdk.openapi.OpenApiErrorCode.SQL_EXECUTION_FAILED;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import com.clougence.clouddm.api.sidecar.session.execute.ResultPhaseOfBatch;
import com.clougence.clouddm.sdk.execute.resultset.echo.*;
import com.clougence.clouddm.sdk.execute.session.MessageLevel;
import com.clougence.clouddm.sdk.execute.session.QueryFailure;
import com.clougence.clouddm.sdk.execute.session.result.fetcher.ValueType;
import com.clougence.clouddm.sdk.openapi.OpenApiException;
import com.clougence.clouddm.sdk.openapi.data.OpenApiOperationResult;
import com.clougence.clouddm.sdk.openapi.data.OpenApiResultColumn;
import com.clougence.clouddm.sdk.openapi.data.OpenApiResultSet;
import com.clougence.clouddm.sdk.openapi.data.OpenApiUpdateCount;
import com.clougence.utils.JsonUtils;

final class OpenApiResultCollector {

    static final int                           MAX_RESPONSE_BYTES   = 1024 * 1024;
    private static final int                   ENVELOPE_BYTES       = 1024;
    private static final int                   RESULT_FRAMING_BYTES = 96;

    private final String                       batchId;
    private final int                          rowLimit;
    private final long                         workerRowLimit;
    private final List<OpenApiOperationResult> results              = new ArrayList<>();

    private int                                remainingBytes       = MAX_RESPONSE_BYTES - ENVELOPE_BYTES;
    private String                             activeSet;
    private List<OpenApiResultColumn>          columns;
    private List<ValueType>                    valueTypes;
    private List<List<Object>>                 rows;
    private boolean                            truncated;
    private boolean                            failed;
    private QueryFailure                       batchFailure;

    OpenApiResultCollector(String batchId, int rowLimit, long workerRowLimit){
        this.batchId = batchId;
        this.rowLimit = rowLimit;
        this.workerRowLimit = workerRowLimit;
    }

    boolean accept(List<Result> incoming) {
        for (Result result : incoming) {
            if (!this.batchId.equals(result.getBatchId())) {
                continue;
            }

            if (result instanceof ResultPhaseOfBatch batch && batch.getPhaseType() == ResultPhaseType.After) {
                this.batchFailure = batch.getFailure();
                return true;
            }

            if (!result.isSuccess() || (result instanceof ResultMessage message && message.getLevel() == MessageLevel.Error)
                || (result instanceof ResultPhase phase && phase.getPhaseType() == ResultPhaseType.Cancel)) {
                this.failed = true;
            } else if (!this.failed) {
                this.collect(result);
            }
        }

        return false;
    }

    List<OpenApiOperationResult> finish() {
        if (this.batchFailure == QueryFailure.TIMED_OUT) {
            throw new OpenApiException(EXECUTION_TIMEOUT);
        }

        if (this.failed || this.batchFailure != null) {
            throw new OpenApiException(SQL_EXECUTION_FAILED);
        }

        this.finishSet();
        return List.copyOf(this.results);
    }

    private void collect(Result result) {
        switch (result.getResultType()) {
            case ResultSetMeta -> this.startSet((ResultSetMeta) result);
            case ResultSet -> this.appendRows((ResultSet) result);
            case ResultSetRows -> {
                if (this.activeSet != null && this.activeSet.equals(result.getResultId()) && ((ResultSetCount) result).getFetchCount() > this.rows.size()) {
                    this.truncated = true;
                }
            }
            case ResultCount -> {
                this.finishSet();
                long count = ((ResultCount) result).getUpdateCount();
                if (count >= 0) {
                    this.reserve(RESULT_FRAMING_BYTES);
                    this.results.add(new OpenApiUpdateCount(count));
                }
            }
            default -> {
                // Worker progress and diagnostic messages are outside the result payload.
            }
        }
    }

    private void startSet(ResultSetMeta meta) {
        this.finishSet();
        this.activeSet = meta.getResultId();
        this.valueTypes = meta.getValueTypes();
        this.columns = new ArrayList<>(meta.getColumnList().size());
        for (int i = 0; i < meta.getColumnList().size(); i++) {
            this.columns.add(new OpenApiResultColumn(meta.getColumnList().get(i), meta.getColumnType().get(i)));
        }
        this.reserve(RESULT_FRAMING_BYTES + jsonBytes(this.columns));
        this.rows = new ArrayList<>();
        this.truncated = false;
    }

    private void appendRows(ResultSet chunk) {
        if (!chunk.getResultId().equals(this.activeSet)) {
            throw new OpenApiException(SQL_EXECUTION_FAILED);
        }

        if (this.truncated) {
            return;
        }

        for (ResultSetRow row : chunk.getRowSet()) {
            if (row.getData().stream().anyMatch(ResultSetValue::isError)) {
                throw new OpenApiException(SQL_EXECUTION_FAILED);
            }
            if (this.rows.size() >= this.rowLimit || row.getData().stream().anyMatch(value -> !value.isComplete() || value.getMoreSize() > 0)) {
                this.truncated = true;
                break;
            }

            List<Object> values = new ArrayList<>(row.getData().size());
            for (int i = 0; i < row.getData().size(); i++) {
                ValueType type = this.valueTypes == null ? null : this.valueTypes.get(i);
                values.add(toJsonValue(row.getData().get(i), type));
            }

            int bytes = jsonBytes(values) + 1;
            if (bytes > this.remainingBytes) {
                this.truncated = true;
                break;
            }

            this.remainingBytes -= bytes;
            this.rows.add(values);
        }

        if (chunk.getFetchCount() > this.rows.size() || (this.workerRowLimit > 0 && chunk.getFetchCount() >= this.workerRowLimit && this.workerRowLimit <= this.rowLimit)) {
            this.truncated = true;
        }
    }

    private static Object toJsonValue(ResultSetValue value, ValueType type) {
        String text = value.getValue();
        if (text == null || value.isMask() || type == null) {
            return text;
        }

        try {
            return switch (type) {
                case Boolean -> switch (text) {
                    case "True", "Yes", "Y" -> true;
                    case "False", "No", "N" -> false;
                    default -> text;
                };
                case Byte, Short, Integer, Long, BigInteger -> new BigInteger(text);
                case BigDecimal, Float, Double -> new BigDecimal(text);
                default -> text;
            };
        } catch (NumberFormatException e) {
            // Special floating-point values have no JSON number representation.
            return text;
        }
    }

    private void finishSet() {
        if (this.activeSet != null) {
            this.results.add(new OpenApiResultSet(this.columns, this.rows, this.truncated));
            this.activeSet = null;
        }
    }

    private void reserve(int bytes) {
        if (bytes > this.remainingBytes) {
            throw new OpenApiException(SQL_EXECUTION_FAILED);
        }
        this.remainingBytes -= bytes;
    }

    private static int jsonBytes(Object value) {
        return JsonUtils.toJson(value).getBytes(StandardCharsets.UTF_8).length;
    }
}
