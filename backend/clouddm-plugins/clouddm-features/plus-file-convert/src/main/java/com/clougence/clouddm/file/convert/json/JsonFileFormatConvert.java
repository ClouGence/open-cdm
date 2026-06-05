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
package com.clougence.clouddm.file.convert.json;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

import com.clougence.clouddm.sdk.execute.resultset.file.*;
import org.slf4j.Logger;

import com.clougence.clouddm.base.metadata.ds.ColMetaData;
import com.clougence.clouddm.file.convert.config.json.JsonOption;
import com.clougence.clouddm.file.convert.constants.ConvertI18nKeys;
import com.clougence.clouddm.sdk.execute.session.QueryRequest;
import com.clougence.clouddm.sdk.execute.session.result.ValueProcessService;
import com.clougence.clouddm.sdk.execute.resultset.echo.ResultSetValue;
import com.clougence.clouddm.sdk.service.execute.SessionService;
import com.clougence.utils.CollectionUtils;
import com.clougence.utils.JsonUtils;
import com.clougence.utils.StringUtils;
import com.clougence.utils.io.FileUtils;
import com.fasterxml.jackson.core.type.TypeReference;

public class JsonFileFormatConvert implements FileFormatConvert {

    private final SessionService service;

    public JsonFileFormatConvert(SessionService service){
        this.service = service;
    }

    @Override
    public String name() {
        return "application/json";
    }

    @Override
    public String extension() {
        return "json";
    }

    @Override
    public String descriptionI18n() {
        return ConvertI18nKeys.FORMAT_RESULT_SET_TO_JSON_DESC;
    }

    @Override
    public String iconName() {
        return "icon-v2-FileJson";
    }

    @Override
    public long convert(String exportId, DmFileType srcType, File srcFile, File dstFile, Logger logger, FileFormatConvertReport report, String optionStr) throws IOException {
        logger.info("convert the " + srcFile + " file as " + dstFile + " using JSON format.");

        if (srcType != DmFileType.ResultSet) {
            throw new IOException("Unsupported source file type: " + srcType);
        }

        try (ResultReader rr = service.getResultService().openReader(srcFile.getAbsoluteFile())) {
            QueryRequest query = rr.getQueryInfo();
            ColMetaData[] metaData = rr.getMetadataInfo();
            Map<String, ColMetaData> rowMeta = readResultColumns(metaData);

            long writeCnt = 0;
            File dstJsonFile = dstFile.getAbsoluteFile();
            FileUtils.mkdirs(dstJsonFile.getParentFile());
            try (FileOutputStream fos = new FileOutputStream(dstJsonFile)) {
                JsonOption option = readJSONOption(optionStr, metaData);
                writeCnt += this.doExportToJson(exportId, query, rowMeta, rr, fos, report, option);
            }

            logger.info("convert done.");
            return writeCnt;
        }
    }

    private long doExportToJson(String trackId, QueryRequest query, Map<String, ColMetaData> meta, ResultReader rr, FileOutputStream fos, //
                                FileFormatConvertReport report, JsonOption option) throws IOException {
        long rowCnt = rr.getRowCount();
        long lastReport = 0;

        // reading data.
        List<String> header = meta.values().stream().map(ColMetaData::getColumn).collect(Collectors.toList());
        fos.write("[".getBytes());

        long offset = option.getOffset();
        long limit = option.getLimit() < 0 ? rowCnt : Math.max(1, option.getLimit());
        long writeCnt = 0;
        if (option.getColumns().stream().anyMatch(JsonOption.ColumnOption::isExport)) {
            for (int r = 0; r < rowCnt; r++) {
                rr.nextRow();
                if (r < offset) {
                    continue;
                }
                if (writeCnt > 0) {
                    fos.write(",\n".getBytes());
                }

                // read row data
                JsonRowData rowData = new JsonRowData();
                for (int c = 0; c < meta.size(); c++) {
                    ColMetaData dataMeta = meta.get(header.get(c));
                    byte dataType = rr.nextDataType();
                    ResultSetValue value = rr.readAsString(dataMeta, query.getResultConf());
                    String valueStr = value.getValue();
                    if (value.isError()) {
                        valueStr = null;
                    }

                    rowData.addRow(dataMeta.getColumn(), valueStr, dataTypeAsStr(dataType));
                }
                this.doWriteJson(query, meta, rowData, option, fos);
                writeCnt++;

                // check limit
                if (writeCnt >= limit) {
                    break;
                }

                // monitoring report
                if ((System.currentTimeMillis() - lastReport) > 1000) {
                    lastReport = System.currentTimeMillis();
                    report.reportProcess("Processing[" + trackId + "] " + writeCnt + " rows", 0, limit, writeCnt);
                }
            }
        }

        fos.write("]".getBytes());
        report.reportProcess("Processing[" + trackId + "] " + writeCnt + " rows", 0, writeCnt, writeCnt);
        fos.flush();
        return writeCnt;
    }

    private void doWriteJson(QueryRequest query, Map<String, ColMetaData> meta, JsonRowData row, JsonOption option,  //
                             FileOutputStream writer) throws IOException {
        this.doMask(query, meta, row);

        writer.write(row.toJsonString(option).getBytes());
    }

    private void doMask(QueryRequest query, Map<String, ColMetaData> meta, JsonRowData row) {
        if (query.isUsingValueProcess()) {
            ValueProcessService processSpi = this.service.getProcessSpi();
            if (processSpi == null) {
                throw new UnsupportedOperationException("plugin 'plus-sec-rules' is not exist.");
            }

            Map<String, Object> flash = new ConcurrentHashMap<>();

            processSpi.begin(query, meta, flash);
            List<String> rowData = processSpi.processRow(query, meta, row.getRowData(), flash);
            for (int i = 0; i < rowData.size(); i++) {
                row.getRowData().set(i, rowData.get(i));
            }
            processSpi.finish(query, flash);
        }
    }

    // --- utils ---

    private boolean dataTypeAsStr(byte dataType) {
        switch (dataType) {
            case ResultType.Code:
                return true;
            case ResultType.Boolean:
            case ResultType.Byte:
            case ResultType.Short:
            case ResultType.Integer:
            case ResultType.Long:
            case ResultType.BigInteger:
            case ResultType.Float:
            case ResultType.Double:
                return false;
            case ResultType.String:
            case ResultType.Bytes:
            case ResultType.Date:
            case ResultType.Time:
            case ResultType.TimeZ:
            case ResultType.DateTime:
            case ResultType.DateTimeZ:
                return true;
            case ResultType.BigDecimal:
                return false;
            default:
                return true;
        }
    }

    private JsonOption readJSONOption(String optionStr, ColMetaData[] metaData) {
        JsonOption option = StringUtils.isBlank(optionStr) ? new JsonOption() : JsonUtils.toList(optionStr, new TypeReference<JsonOption>() {
        });

        if (CollectionUtils.isEmpty(option.getColumns())) {
            option.setColumns(new ArrayList<>());
            for (ColMetaData m : metaData) {
                JsonOption.ColumnOption co = new JsonOption.ColumnOption();
                co.setColumnName(m.getColumn());
                co.setExport(true);
                option.getColumns().add(co);
            }
        }

        return option;
    }

    private Map<String, ColMetaData> readResultColumns(ColMetaData[] col) {
        Map<String, ColMetaData> columnMap = new LinkedHashMap<>();
        for (ColMetaData m : col) {
            columnMap.put(m.getColumn(), m);
        }
        return columnMap;
    }
}
