package com.clougence.clouddm.file.convert.xlsx;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

import com.clougence.clouddm.sdk.execute.resultset.file.*;
import org.slf4j.Logger;

import com.clougence.clouddm.base.metadata.ds.ColMetaData;
import com.clougence.clouddm.file.convert.config.xlsx.XlsxOption;
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

import cn.idev.excel.ExcelWriter;
import cn.idev.excel.FastExcel;
import cn.idev.excel.write.metadata.WriteSheet;

/**
 * @author mode 2020-01-20 21:04
 * @since 1.1.3
 */
public class MsExcelFileFormatConvert implements FileFormatConvert {

    private final SessionService service;

    public MsExcelFileFormatConvert(SessionService service){
        this.service = service;
    }

    @Override
    public String name() {
        return "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";
    }

    @Override
    public String extension() {
        return "xlsx";
    }

    @Override
    public String descriptionI18n() {
        return ConvertI18nKeys.FORMAT_RESULT_SET_TO_XLSX_DESC;
    }

    @Override
    public String iconName() {
        return "icon-v2-FileExcel";
    }

    @Override
    public long convert(String exportId, DmFileType srcType, File srcFile, File dstFile, Logger logger, FileFormatConvertReport report, String optionStr) throws IOException {
        logger.info("convert the " + srcFile + " file as " + dstFile + " using XLSX format.");

        if (srcType != DmFileType.ResultSet) {
            throw new IOException("Unsupported source file type: " + srcType);
        }

        try (ResultReader rr = service.getResultService().openReader(srcFile.getAbsoluteFile())) {
            QueryRequest query = rr.getQueryInfo();
            ColMetaData[] metaData = rr.getMetadataInfo();
            Map<String, ColMetaData> rowMeta = readResultColumns(metaData);

            long writeCnt = 0;
            File dstExcelFile = dstFile.getAbsoluteFile();
            FileUtils.mkdirs(dstExcelFile.getParentFile());
            try (FileOutputStream fos = new FileOutputStream(dstExcelFile)) {
                XlsxOption option = readXLSXOption(optionStr, metaData);
                writeCnt += this.doExportToExcel(exportId, query, rowMeta, rr, fos, report, option);
            }

            logger.info("convert done.");
            return writeCnt;
        }
    }

    private long doExportToExcel(String trackId, QueryRequest query, Map<String, ColMetaData> meta, ResultReader rr, FileOutputStream fos,//
                                 FileFormatConvertReport report, XlsxOption option) throws IOException {
        long rowCnt = rr.getRowCount();
        long lastReport = 0;
        AtomicLong lastFlashRowNumber = new AtomicLong(0);
        List<MsExcelRowData> rowBatch = new ArrayList<>();

        ExcelWriter writer = FastExcel.write(fos).build();
        WriteSheet sheet = FastExcel.writerSheet("ResultSet").build();
        sheet.setCustomWriteHandlerList(Collections.singletonList(new MsExcelCellWriteHandler(lastFlashRowNumber, rowBatch)));

        // reading data.
        List<String> header = meta.values().stream().filter(colMetaData -> {
            return option.getColumns().get(colMetaData.getIndex() - 1).isExport();
        }).map(colMetaData -> {
            return option.getColumns().get(colMetaData.getIndex() - 1).getColumnName();
        }).collect(Collectors.toList());
        writer.write(Collections.singletonList(header), sheet);

        long offset = option.getOffset();
        long limit = option.getLimit() < 0 ? rowCnt : Math.max(1, option.getLimit());
        long writeCnt = 0;
        int colSize = meta.size();
        List<String> tmpCache = new ArrayList<>(3);
        for (int r = 0; r < rowCnt; r++) {
            rr.nextRow();
            if (r < offset) {
                continue;
            }

            // read row data
            MsExcelRowData rowData = new MsExcelRowData(writeCnt, colSize);
            for (int c = 0; c < meta.size(); c++) {
                String prefix;
                int maxFetch;
                if (rr.nextDataType() == ResultType.Bytes) {
                    prefix = "0x";
                    maxFetch = 32765;
                } else {
                    prefix = "";
                    maxFetch = 32767; // cell max length
                }

                ResultSetValue value = rr.readAsString(meta.get(header.get(c)), query.getResultConf(), 0, maxFetch);
                this.readResultColumnValue(prefix, value, c, rowData, tmpCache);
            }
            rowBatch.add(rowData);
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

            // try flash write
            if (r % 500 == 0) {
                this.doWriteExcel(query, meta, rowBatch, option, writer, sheet);
                lastFlashRowNumber.getAndAdd(rowBatch.size());
                rowBatch.clear();
            }
        }

        if (!rowBatch.isEmpty()) {
            this.doWriteExcel(query, meta, rowBatch, option, writer, sheet);
            lastFlashRowNumber.getAndAdd(rowBatch.size());
            rowBatch.clear();
        }

        report.reportProcess("Processing[" + trackId + "] " + writeCnt + " rows", 0, writeCnt, writeCnt);
        writer.finish();
        fos.flush();
        return writeCnt;
    }

    private void doWriteExcel(QueryRequest query, Map<String, ColMetaData> meta, List<MsExcelRowData> dataBatch, XlsxOption option,//
                              ExcelWriter writer, WriteSheet sheet) {
        this.doMask(query, meta, dataBatch);

        List<List<String>> v = new ArrayList<>();
        for (MsExcelRowData batch : dataBatch) {
            List<String> rowData = new ArrayList<>();
            for (int i = 0; i < batch.getRowData().size(); i++) {
                if (option.getColumns().get(i).isExport()) {
                    rowData.add(batch.getRowData().get(i));
                }
            }
            v.add(rowData);
        }

        writer.write(v, sheet);
    }

    private void doMask(QueryRequest query, Map<String, ColMetaData> meta, List<MsExcelRowData> dataBatch) {
        if (query.isUsingValueProcess()) {
            ValueProcessService processSpi = this.service.getProcessSpi();
            if (processSpi == null) {
                throw new UnsupportedOperationException("plugin 'plus-sec-rules' is not exist.");
            }

            Map<String, Object> flash = new ConcurrentHashMap<>();

            processSpi.begin(query, meta, flash);
            dataBatch.parallelStream().forEach(row -> {
                List<String> rowData = processSpi.processRow(query, meta, row.getRowData(), flash);
                for (int i = 0; i < rowData.size(); i++) {
                    String beforeData = row.getRowData().get(i);
                    String afterData = rowData.get(i);

                    row.getRowData().set(i, rowData.get(i));
                    if (!StringUtils.equals(beforeData, afterData)) {
                        String oldComment = row.getRowComment().get(i);
                        String newComment = (StringUtils.isBlank(oldComment) ? "数据已脱敏" : oldComment + "; 数据已脱敏");
                        row.getRowComment().set(i, newComment);
                    }
                }
            });
            processSpi.finish(query, flash);
        }
    }

    // --- utils ---

    private Map<String, ColMetaData> readResultColumns(ColMetaData[] col) {
        Map<String, ColMetaData> columnMap = new LinkedHashMap<>();
        for (ColMetaData m : col) {
            columnMap.put(m.getColumn(), m);
        }
        return columnMap;
    }

    private void readResultColumnValue(String prefix, ResultSetValue value, int colNum, MsExcelRowData rowData, List<String> tmpCache) {
        tmpCache.clear();
        String valueStr = prefix + value.getValue();

        if (!value.isComplete()) {
            tmpCache.add("数据不完整");
        }
        if (value.isMask()) {
            tmpCache.add("数据已脱敏");
        }
        if (value.isError()) {
            valueStr = "-- ERROR ---";
            tmpCache.add(StringUtils.isNotBlank(value.getValue()) ? value.getValue() : "读取错误");
        }

        String commentStr = tmpCache.isEmpty() ? "" : StringUtils.join(tmpCache.toArray(), "; ");
        rowData.getRowData().set(colNum, valueStr);
        rowData.getRowComment().set(colNum, commentStr);
    }

    private static XlsxOption readXLSXOption(String optionStr, ColMetaData[] metaData) {
        XlsxOption option = StringUtils.isBlank(optionStr) ? new XlsxOption() : JsonUtils.toList(optionStr, new TypeReference<XlsxOption>() {
        });

        if (CollectionUtils.isEmpty(option.getColumns())) {
            option.setColumns(new ArrayList<>());
            for (ColMetaData m : metaData) {
                XlsxOption.ColumnOption co = new XlsxOption.ColumnOption();
                co.setColumnName(m.getColumn());
                co.setExport(true);
                option.getColumns().add(co);
            }
        }

        return option;
    }
}
