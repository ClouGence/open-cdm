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
package com.clougence.clouddm.worker.component.session.result;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.clougence.clouddm.api.common.GlobalConfUtils;
import com.clougence.clouddm.component.resultfile.ResultSetOutputStream;
import com.clougence.clouddm.sdk.execute.resultset.echo.ReceiveMode;
import com.clougence.clouddm.sdk.execute.resultset.echo.ResultSetMeta;
import com.clougence.clouddm.sdk.execute.resultset.echo.ResultType;
import com.clougence.clouddm.sdk.execute.resultset.file.DmFileType;
import com.clougence.clouddm.sdk.execute.session.QueryRequest;
import com.clougence.clouddm.sdk.execute.session.ResultBuilder.ResultSetMetaBuild;
import com.clougence.clouddm.sdk.execute.session.ResultBuilder.ResultSetRowsBuild;
import com.clougence.clouddm.sdk.execute.session.ResultColMeta;
import com.clougence.clouddm.sdk.execute.session.result.ReaderOptions;
import com.clougence.clouddm.sdk.execute.session.result.fetcher.ValueFetcherContext;
import com.clougence.clouddm.worker.component.session.SessionSupport;
import com.clougence.clouddm.worker.component.session.storage.ResultStorage;
import com.clougence.clouddm.worker.component.session.storage.RowStorage;
import com.clougence.utils.HashUtils;
import com.clougence.utils.JsonUtils;

import lombok.extern.slf4j.Slf4j;

@Slf4j
class ImplResultSetMetaBuild extends AbstractResultBuild<ResultSetMeta> implements ResultSetMetaBuild {

    private final QueryRequest     query;
    private final ReaderOptions    options;
    private final SessionSupport   ss;
    private ImplResultSetRowsBuild resultBuild;

    public ImplResultSetMetaBuild(String resultId, String sessionID, QueryRequest query, ResultListenerContainer listeners, SessionSupport ss){
        super(resultId, sessionID, query, listeners);
        this.query = query;
        this.ss = ss;

        this.options = new ReaderOptions();
        this.options.setColumnBytesLimit(query.getResultConf().getFetchColumnBytesLimit());
        this.options.setElementBytesLimit(query.getResultConf().getFetchElementBytesLimit());
        this.options.setTempPath(GlobalConfUtils.getTempDataHome());
        this.options.setDataFormat(query.getResultConf().getDataFormat());
        this.options.setTimeFormat(query.getResultConf().getTimeFormat());
        this.options.setDataTimeFormat(query.getResultConf().getDataTimeFormat());
        this.options.setTimeWithZoneFormat(query.getResultConf().getTimeWithZoneFormat());
        this.options.setDataTimeWithZoneFormat(query.getResultConf().getDataTimeWithZoneFormat());
    }

    private void resetCurrent() {
        this.resultBuild = null;
    }

    @Override
    protected ResultSetMeta createResult() {
        ResultSetMeta r = new ResultSetMeta();
        r.setResultType(ResultType.ResultSetMeta);
        r.setColumnList(new LinkedList<>());
        r.setColumnType(new LinkedList<>());
        return r;
    }

    @Override
    protected void initResult(ResultSetMeta result) {
        ReceiveMode receiveMode = this.query.getResultConf().getReceiveMode();
        boolean useResultCache = this.query.getResultConf().isCacheResult();

        if (useResultCache) {
            String localWsn = this.ss.getLocalWsn();
            String cacheName = ("results" + File.separator + this.get().getResultId() + ".dat").toLowerCase();
            String cacheFileUri = "wsn://" + localWsn + "/" + cacheName;
            result.setCacheFilePath(cacheName);
            result.setCacheFileUri(cacheFileUri);
        }

        result.setCacheFileFormat(DmFileType.ResultSet);
        result.setReceiveMode(receiveMode);
    }

    @Override
    public ResultSetRowsBuild receiveMeta(Map<String, ResultColMeta> rowMeta) throws IOException {
        if (this.resultBuild == null) {
            List<ValueFetcherContext> metaCtx = new ArrayList<>();

            // metadata and fetcher
            String firstCatalog = null;
            String firstSchema = null;
            String firstTable = null;
            boolean sameTable = true;
            for (String colName : rowMeta.keySet()) {
                ResultColMeta m = rowMeta.get(colName);
                String vfcId = this.get().getResultId() + "_" + HashUtils.fnvHash(colName);
                metaCtx.add(new ValueFetcherContext(vfcId, m, this.options));

                this.get().getColumnList().add(m.getMeta().getColumn());
                this.get().getColumnType().add(m.getMeta().getColumnType());

                String colTable = m.getMeta().getTable();
                if (colTable == null || colTable.isEmpty()) {
                    continue;
                }
                if (firstTable == null) {
                    firstCatalog = m.getMeta().getCatalog();
                    firstSchema = m.getMeta().getSchema();
                    firstTable = colTable;
                } else if (sameTable && !java.util.Objects.equals(firstTable, colTable)) {
                    sameTable = false;
                }
            }

            if (sameTable && firstTable != null && !firstTable.isEmpty()) {
                this.get().setCatalog(firstCatalog);
                this.get().setSchema(firstSchema);
                this.get().setTable(firstTable);
                this.get().setTargetType("TABLE");
            } else {
                // JDBC getTableName() may return empty for some drivers (e.g. MySQL Connector/J 8.x).
                // Fall back to extracting the table name from the SQL query.
                extractTableFromSql();
            }

            log.info("ResultSetMeta table detection: catalog={}, schema={}, table={}, targetType={}",
                this.get().getCatalog(), this.get().getSchema(), this.get().getTable(),
                this.get().getTargetType());

            // init storage
            boolean useResultCache = this.query.getResultConf().isCacheResult();
            long resultBytesLimit = this.query.getResultConf().getFetchResultSetBytesLimit();
            long columnBytesLimit = this.query.getResultConf().getFetchColumnBytesLimit();
            int displayChars = this.query.getResultConf().getDisplayChars();
            ResultStorage storage = new ResultStorage(this.get().getResultId(), resultBytesLimit, columnBytesLimit, displayChars);

            if (useResultCache) {
                String cacheFilePath = this.get().getCacheFilePath();
                File cacheFile = this.ss.getFileService().createFileObject(cacheFilePath, false);
                storage.init(this.ss.getFileService(), cacheFilePath, cacheFile);
                writeMetaData(storage, metaCtx);
            } else {
                storage.init();
            }

            String resultId = this.get().getResultId();
            String sessionId = this.get().getSessionId();
            this.resultBuild = new ImplResultSetRowsBuild(resultId, sessionId, this.query, this.listeners, this.ss, storage, metaCtx);
            this.resultBuild.initBuild();
        }

        return this.resultBuild;
    }

    /**
     * Fallback: extract table name from SQL query when JDBC getTableName() returns empty.
     * Handles simple SELECT ... FROM table queries. Returns without setting fields for complex queries.
     */
    private void extractTableFromSql() {
        // Prefer the original user SQL (before any rewrite), fall back to the executed SQL.
        String sql = this.query.getOriginalBody();
        if (sql == null || sql.isEmpty()) {
            sql = this.query.getQueryBody();
        }
        if (sql == null || sql.isEmpty()) {
            return;
        }
        // Strip single-line comments and normalize whitespace
        String normalized = sql.replaceAll("--.*", " ").replaceAll("/\\*.*?\\*/", " ").replaceAll("\\s+", " ").trim();
        // Match: SELECT ... FROM [schema.]table (stop at WHERE/JOIN/ORDER/GROUP/LIMIT/etc.)
        Pattern p = Pattern.compile(
            "\\bFROM\\s+(?:`?([^`\\s.]+)`?\\.)?`?([^`\\s,;()]+)`?(?!.*\\bJOIN\\b)",
            Pattern.CASE_INSENSITIVE
        );
        Matcher m = p.matcher(normalized);
        if (m.find()) {
            String schemaOrCatalog = m.group(1);
            String tableName = m.group(2);
            if (tableName != null && !tableName.isEmpty() && !"DUAL".equalsIgnoreCase(tableName)) {
                // Check that this is the only table (no JOIN, no comma-separated tables)
                String afterFrom = normalized.substring(m.start()).toUpperCase();
                if (!afterFrom.contains("JOIN") && !afterFrom.substring(0, Math.min(100, afterFrom.length())).contains(",")) {
                    this.get().setTable(tableName);
                    this.get().setTargetType("TABLE");
                    if (schemaOrCatalog != null) {
                        this.get().setSchema(schemaOrCatalog);
                    }
                }
            }
        }
    }

    private void writeMetaData(ResultStorage storage, List<ValueFetcherContext> metaCtx) throws IOException {
        // write meta
        try (RowStorage row = storage.nextRsRow()) {
            // -- write query
            ResultSetOutputStream out = row.getOutput();
            out.writeString((byte) 0, JsonUtils.toJson(this.query), StandardCharsets.UTF_8);
        }
        try (RowStorage row = storage.nextRsRow()) {
            // -- write colMeta
            ResultSetOutputStream out = row.getOutput();
            for (ValueFetcherContext ctx : metaCtx) {
                out.writeString((byte) 0, JsonUtils.toJson(ctx.getMeta().getMeta()), StandardCharsets.UTF_8);
            }
        }
    }

    @Override
    public void finishRecord(boolean success) {
        this.resetCurrent();
        super.finishRecord(success);
    }

    @Override
    public void finishRecord(boolean success, String message, Throwable e) {
        this.resetCurrent();
        super.finishRecord(success, message, e);
    }
}
