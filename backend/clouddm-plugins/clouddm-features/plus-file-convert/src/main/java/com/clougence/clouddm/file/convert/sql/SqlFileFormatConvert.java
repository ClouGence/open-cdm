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
package com.clougence.clouddm.file.convert.sql;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

import com.clougence.clouddm.sdk.execute.resultset.file.*;
import org.slf4j.Logger;

import com.clougence.clouddm.base.metadata.ds.ColMetaData;
import com.clougence.clouddm.file.convert.config.sql.SqlOption;
import com.clougence.clouddm.file.convert.constants.ConvertI18nKeys;
import com.clougence.clouddm.file.convert.sql.ds.*;
import com.clougence.clouddm.sdk.execute.ExecuteVariables;
import com.clougence.clouddm.sdk.execute.session.QueryRequest;
import com.clougence.clouddm.sdk.execute.session.result.ValueProcessService;
import com.clougence.clouddm.sdk.execute.resultset.echo.ResultSetValue;
import com.clougence.clouddm.sdk.service.config.ConfigService;
import com.clougence.clouddm.sdk.service.execute.SessionService;
import com.clougence.clouddm.base.metadata.ds.DataSourceType;
import com.clougence.schema.dialect.Dialect;
import com.clougence.utils.CollectionUtils;
import com.clougence.utils.JsonUtils;
import com.clougence.utils.StringUtils;
import com.clougence.utils.format.WellKnowFormat;
import com.clougence.utils.io.FileUtils;
import com.fasterxml.jackson.core.type.TypeReference;

public class SqlFileFormatConvert implements FileFormatConvert {

    private final SessionService                       service;
    private final ConfigService                        configService;

    private final Map<DataSourceType, SqlValueHandler> map            = new HashMap<>();
    private final SqlValueHandler                      defaultHandler = new DefaultValueHandler();

    public SqlFileFormatConvert(SessionService service, ConfigService configService){
        this.service = service;
        this.configService = configService;

        map.put(DataSourceType.Oracle, OracleValueHandler.HANDLER);
        map.put(DataSourceType.ObForOracle, OracleValueHandler.HANDLER);
        map.put(DataSourceType.SQLServer, SqlServerValueHandler.HANDLER);
        map.put(DataSourceType.MaxCompute, McValueHandler.HANDLER);
    }

    @Override
    public String name() {
        return "application/sql";
    }

    @Override
    public String extension() {
        return "sql";
    }

    @Override
    public String descriptionI18n() {
        return ConvertI18nKeys.FORMAT_RESULT_SET_TO_SQL_DESC;
    }

    @Override
    public String iconName() {
        return "icon-v2-FileSql";
    }

    @Override
    public Map<String, Object> getOption() {
        HashMap<String, Object> option = new HashMap<>();
        option.put("unsupportedDataSource", Arrays.asList(DataSourceType.Redis, DataSourceType.MongoDB));
        option.put("unsupportedMergeInsert", Arrays.asList(DataSourceType.Oracle, DataSourceType.ObForOracle));
        return option;
    }

    @Override
    public long convert(String exportId, DmFileType srcType, File srcFile, File dstFile, Logger logger, FileFormatConvertReport report, String optionStr) throws IOException {
        logger.info("convert the " + srcFile + " file as " + dstFile + " using INSERT format.");

        if (srcType != DmFileType.ResultSet) {
            throw new IOException("Unsupported source file type: " + srcType);
        }

        try (ResultReader rr = service.getResultService().openReader(srcFile.getAbsoluteFile())) {
            QueryRequest query = rr.getQueryInfo();
            ColMetaData[] metaData = rr.getMetadataInfo();
            Map<String, ColMetaData> rowMeta = readResultColumns(metaData);

            SqlOption option = readSqlOption(optionStr, metaData);
            if (option.getColumns().stream().noneMatch(SqlOption.ColumnOption::isExport)) {
                return 0;
            }

            long writeCnt = 0;
            long batchSize = readSqlValueBatchSize(option);
            File dstSqlFile = dstFile.getAbsoluteFile();
            FileUtils.mkdirs(dstSqlFile.getParentFile());
            try (FileOutputStream fos = new FileOutputStream(dstSqlFile)) {
                writeCnt += this.doExportToSql(exportId, query, rowMeta, rr, fos, report, option, batchSize);
            }

            logger.info("convert done.");
            return writeCnt;
        }
    }

    private long doExportToSql(String trackId, QueryRequest query, Map<String, ColMetaData> meta, ResultReader rr, FileOutputStream fos,//
                               FileFormatConvertReport report, SqlOption option, long batchSize) throws IOException {
        long rowCnt = rr.getRowCount();
        long lastReport = 0;
        DataSourceType dsType = option.getDataSourceType();
        Dialect dialect = this.configService.findDialectByDsType(dsType == null ? DataSourceType.MySQL : dsType);

        // reading data.
        List<String> header = meta.values().stream().map(ColMetaData::getColumn).collect(Collectors.toList());
        StringBuilder sqlHeader = new StringBuilder();
        boolean hasAnyHeader = false;
        for (int i = 0; i < header.size(); i++) {
            SqlOption.ColumnOption columnOption = option.getColumns().get(i);
            if (!columnOption.isExport()) {
                continue;
            }

            if (hasAnyHeader) {
                sqlHeader.append(',');
            }

            sqlHeader.append(dialect.fmtName(true, columnOption.getColumnName()));
            hasAnyHeader = true;
        }

        // sql file header
        Map<String, String> variables = infoMap(query);
        fos.write("/*".getBytes());
        fos.write(("\n  Info         : " + JsonUtils.toJson(variables)).getBytes());
        fos.write(("\n  Execute Time : " + WellKnowFormat.WKF_DATE_TIME24_S3.format(query.getRequestTime())).getBytes());
        fos.write(("\n  Current Time : " + WellKnowFormat.WKF_DATE_TIME24_S3.format(new Date())).getBytes());
        fos.write(("\n  SQL Command  : " + query.getQueryBody()).getBytes());
        fos.write("\n*/\n".getBytes());

        List<SqlRowData> rows = new ArrayList<>();
        long offset = option.getOffset();
        long limit = option.getLimit() < 0 ? rowCnt : Math.max(1, option.getLimit());
        long writeLength = 0;
        long writeCnt = 0;
        for (int r = 0; r < rowCnt; r++) {
            rr.nextRow();
            if (r < offset) {
                continue;
            }

            // read row data
            SqlRowData rowData = new SqlRowData();
            for (int c = 0; c < meta.size(); c++) {
                ColMetaData dataMeta = meta.get(header.get(c));
                byte dataType = rr.nextDataType();
                ResultSetValue value = rr.readAsString(dataMeta, query.getResultConf());
                String valueStr = value.getValue();
                if (value.isError()) {
                    valueStr = null;
                }

                writeLength += (valueStr == null ? 4 : valueStr.length());
                rowData.addData(valueStr, dataType);
            }
            rows.add(rowData);
            writeCnt++;

            // flash data to file
            if (writeLength >= batchSize) {
                this.doWriteSql(query, meta, fos, rows, sqlHeader.toString(), option);
                writeLength = 0;
                rows.clear();
            }

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

        // flash last data to file
        if (!rows.isEmpty()) {
            this.doWriteSql(query, meta, fos, rows, sqlHeader.toString(), option);
            rows.clear();
        }

        report.reportProcess("Processing[" + trackId + "] " + writeCnt + " rows", 0, writeCnt, writeCnt);
        fos.flush();
        return writeCnt;
    }

    private static Map<String, String> infoMap(QueryRequest query) {
        Map<String, String> variables = query.getVariables();
        variables = variables == null ? Collections.emptyMap() : variables;
        String envName = variables.getOrDefault(ExecuteVariables.ENV_NAME, "");
        String dsName = variables.getOrDefault(ExecuteVariables.DS_NAME, "");
        String userName = variables.getOrDefault(ExecuteVariables.USER_NAME, "");

        Map<String, String> linkMap = new LinkedHashMap<>();
        linkMap.put("Environment", envName);
        linkMap.put("DataSource", dsName);
        linkMap.put("User", userName);

        return linkMap;
    }

    private void doWriteSql(QueryRequest query, Map<String, ColMetaData> meta, //
                            FileOutputStream writer, List<SqlRowData> rows, String sqlHeader, SqlOption option) throws IOException {
        doMask(query, meta, rows);

        SqlValueHandler handler = getValueHandler(option.getDataSourceType());
        for (SqlRowData row : rows) {
            for (int i = 0; i < row.getRowData().size(); i++) {
                String data = row.getRowData().get(i);
                if (data == null) {
                    row.getRowData().set(i, "NULL");
                } else {
                    handler.handle(row, data, i);
                }
            }
        }

        Dialect dialect = this.configService.findDialectByDsType(option.getDataSourceType());
        writer.write(("INSERT INTO " + dialect.fmtTableName(false, null, null, option.getTableName())).getBytes());
        writer.write((" (" + sqlHeader + ") VALUES ").getBytes());
        for (int i = 0; i < rows.size(); i++) {
            if (i > 0) {
                writer.write(",".getBytes());
            }
            writer.write(rows.get(i).toString(option.getColumns()).getBytes());
        }
        writer.write(";\n".getBytes());
    }

    private SqlOption readSqlOption(String optionStr, ColMetaData[] metaData) {
        SqlOption option = StringUtils.isBlank(optionStr) ? new SqlOption() : JsonUtils.toList(optionStr, new TypeReference<SqlOption>() {
        });

        if (CollectionUtils.isEmpty(option.getColumns())) {
            option.setColumns(new ArrayList<>());
            for (ColMetaData m : metaData) {
                SqlOption.ColumnOption co = new SqlOption.ColumnOption();
                co.setColumnName(m.getColumn());
                co.setExport(true);
                option.getColumns().add(co);
            }
        }

        return option;
    }

    private long readSqlValueBatchSize(SqlOption option) {
        if (!option.isMergeInsert() || option.getValueSize() == null || option.getValueSize() <= 0 || option.getDataSourceType() == DataSourceType.Oracle
            || option.getDataSourceType() == DataSourceType.ObForOracle) {
            return 0;
        } else {
            return option.getValueSize() * 1024 * 1024; // MB to Bytes
        }
    }

    private Map<String, ColMetaData> readResultColumns(ColMetaData[] col) {
        Map<String, ColMetaData> columnMap = new LinkedHashMap<>();
        for (ColMetaData m : col) {
            columnMap.put(m.getColumn(), m);
        }
        return columnMap;
    }

    private SqlValueHandler getValueHandler(DataSourceType type) {
        return map.getOrDefault(type, defaultHandler);
    }

    private void doMask(QueryRequest query, Map<String, ColMetaData> meta, List<SqlRowData> dataBatch) {
        ValueProcessService processSpi = this.service.getProcessSpi();
        Map<String, Object> flash = new ConcurrentHashMap<>();

        if (query.isUsingValueProcess()) {
            if (processSpi == null) {
                throw new UnsupportedOperationException("plugin 'plus-sec-rules' is not exist.");
            }

            processSpi.begin(query, meta, flash);
            dataBatch.parallelStream().forEach(row -> {
                List<String> rowData = processSpi.processRow(query, meta, row.getRowData(), flash);
                for (int i = 0; i < rowData.size(); i++) {
                    String beforeData = row.getRowData().get(i);
                    String afterData = rowData.get(i);

                    row.getRowData().set(i, rowData.get(i));
                    if (!StringUtils.equals(beforeData, afterData)) {
                        row.getEntityTypes().set(i, ResultType.String); // masked data as string
                    }
                }
            });
            processSpi.finish(query, flash);
        }
    }
}
