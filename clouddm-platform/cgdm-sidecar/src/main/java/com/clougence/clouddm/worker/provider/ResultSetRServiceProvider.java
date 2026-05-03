package com.clougence.clouddm.worker.provider;

import java.io.File;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.clougence.clouddm.api.console.notify.NotifyRService;
import com.clougence.clouddm.api.sidecar.session.execute.ResultColDTO;
import com.clougence.clouddm.api.sidecar.session.execute.ResultFileReadDTO;
import com.clougence.clouddm.api.sidecar.session.execute.ResultPageDTO;
import com.clougence.clouddm.api.sidecar.session.execute.ResultSetRService;
import com.clougence.clouddm.api.common.GlobalConfUtils;
import com.clougence.clouddm.base.metadata.ds.ColMetaData;
import com.clougence.clouddm.comm.RSocketApiClass;
import com.clougence.clouddm.comm.model.RSocketSendDTO;
import com.clougence.clouddm.platform.plugin.PluginManager;
import com.clougence.clouddm.sdk.execute.session.QueryRequest;
import com.clougence.clouddm.sdk.execute.session.result.ValueProcessService;
import com.clougence.clouddm.sdk.execute.resultset.file.DmFileType;
import com.clougence.clouddm.sdk.execute.resultset.file.FileFormatConvert;
import com.clougence.clouddm.sdk.execute.resultset.file.ResultReader;
import com.clougence.clouddm.sdk.execute.resultset.file.ResultReaderService;
import com.clougence.clouddm.sdk.execute.resultset.echo.ResultSetRow;
import com.clougence.clouddm.sdk.execute.resultset.echo.ResultSetValue;
import com.clougence.clouddm.sdk.service.execute.SessionService;
import com.clougence.clouddm.api.common.boot.UnifiedPostConstruct;
import com.clougence.clouddm.worker.services.SidecarFileServiceImpl;
import com.clougence.utils.StringUtils;
import com.clougence.utils.ThreadUtils;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RSocketApiClass
public class ResultSetRServiceProvider implements ResultSetRService, UnifiedPostConstruct {

    @Resource
    private NotifyRService         notifyRService;
    @Resource
    private SidecarFileServiceImpl fileService;
    @Resource
    private ResultReaderService    readerService;
    @Resource
    private SessionService         sessionService;
    private final AtomicBoolean    inited = new AtomicBoolean();
    private ThreadPoolExecutor     threadPoolExecutor;
    private String                 localWsn;

    /* ---------------------------------------------------------------------------------- */
    /*  commons  */
    /* ---------------------------------------------------------------------------------- */

    @Override
    public void init() throws Exception {
        if (!this.inited.compareAndSet(false, true)) {
            return;
        }

        ThreadFactory tf = ThreadUtils.daemonThreadFactory(this.getClass().getClassLoader(), "Work-auto-exec-%s");
        this.threadPoolExecutor = new ThreadPoolExecutor(2, 20, 1, TimeUnit.MINUTES, new LinkedBlockingQueue<>(200), tf, new ThreadPoolExecutor.AbortPolicy());

        this.localWsn = GlobalConfUtils.loadGlobalConf().getWsn();
    }

    @Override
    public void stop() {

    }

    @Override
    public String convertFile(RSocketSendDTO dto, String puid, String userId, String srcFileId, String exportId, DmFileType dmFileType, String srcFile, String dstFile,
                              String formatName, String option) {
        try {
            this.threadPoolExecutor.execute(() -> {
                try {
                    log.info("File convert task started, " + srcFile + " to " + dstFile);
                    this.notifyRService.notifyConvertProgress(puid, userId, srcFileId, exportId, "convert task started, file is " + dstFile, 0, 100, 0);

                    long total = doConvertFile(puid, userId, srcFileId, exportId, dmFileType, srcFile, dstFile, formatName, option);

                    log.info("File convert task finished, " + srcFile + " to " + dstFile);
                    this.notifyRService.notifyConvertFinish(puid, userId, srcFileId, exportId, "convert task started, file is " + dstFile, total);
                } catch (Exception e) {
                    log.error("File convert task failed, " + e.getMessage(), e);
                    this.notifyRService.notifyConvertFailed(puid, userId, srcFileId, exportId, "convert task failed, " + e.getMessage());
                }
            });
            return "wsn://" + this.localWsn + "/" + URLEncoder.encode(dstFile);
        } catch (RejectedExecutionException e) {
            log.error("File convert task is rejected, please try again later, " + e.getMessage(), e);
            this.notifyRService.notifyConvertFailed(puid, userId, srcFileId, exportId, "convert task is rejected, please try again later, " + e.getMessage());
            return null;
        }
    }

    private long doConvertFile(String puid, String userId, String srcFileId, String exportId, DmFileType dmFileType, String srcFileStr, String dstFileStr, String formatName,
                               String option) throws IOException {
        FileFormatConvert convert = PluginManager.findSpi(FileFormatConvert.class, formatName);
        if (convert == null) {
            throw new IOException("File convert task failed, unsupported format: " + formatName);
        }

        File srcFile = this.fileService.createFileObject(srcFileStr, false);
        File dstFile = this.fileService.createFileObject(dstFileStr, false);

        if (!this.fileService.existsFile(srcFile)) {
            throw new IOException("File convert task failed, source file does not exist: " + srcFile);
        }
        if (this.fileService.existsFile(dstFile)) {
            throw new IOException("File convert task failed, target file already exists: " + dstFile);
        }

        long total = convert.convert(exportId, dmFileType, srcFile, dstFile, log, (m, from, to, current) -> {
            this.notifyRService.notifyConvertProgress(puid, userId, srcFileId, exportId, m, from, to, current);
        }, option);

        log.info("File convert task finished, " + srcFile + " to " + dstFile);
        this.notifyRService.notifyConvertProgress(puid, userId, srcFileId, exportId, "convert task finished, " + srcFile + " to " + dstFile, 0, total, total);
        return total;
    }

    @Override
    public void deleteFile(RSocketSendDTO sendDTO, String filePath, boolean tempFile) {
        File file = this.fileService.createFileObject(filePath, tempFile);
        if (!this.fileService.existsFile(file)) {
            return;
        }

        this.fileService.deleteFile(file);
    }

    @Override
    public long fileSize(RSocketSendDTO sendDTO, String filePath) {
        File file = this.fileService.createFileObject(filePath, false);
        if (!this.fileService.existsFile(file)) {
            return -1;
        }

        return this.fileService.fileSize(file);
    }

    @Override
    public ResultFileReadDTO fileRead(RSocketSendDTO sendDTO, String filePath, long offset, int length) {
        File file = this.fileService.createFileObject(filePath, false);
        if (!this.fileService.existsFile(file)) {
            ResultFileReadDTO readDTO = new ResultFileReadDTO();
            readDTO.setSuccess(false);
            readDTO.setMessage("file not found: " + filePath);
            readDTO.setContent(null);
            return readDTO;
        }

        try {
            byte[] bytes = this.fileService.fileRead(file, offset, length);
            ResultFileReadDTO readDTO = new ResultFileReadDTO();
            readDTO.setSuccess(true);
            readDTO.setMessage(bytes == null ? "eof file." : "read " + bytes.length + " bytes.");
            readDTO.setContent(bytes);
            return readDTO;
        } catch (Exception e) {
            ResultFileReadDTO readDTO = new ResultFileReadDTO();
            readDTO.setSuccess(false);
            readDTO.setMessage(e.getMessage());
            readDTO.setContent(null);
            return readDTO;
        }
    }

    @Override
    public ResultPageDTO resultPageRead(RSocketSendDTO sendDTO, String filePath, long rowOffset, int pageSize) {
        File file = this.fileService.createFileObject(filePath, false);
        if (!this.fileService.existsFile(file)) {
            ResultPageDTO readDTO = new ResultPageDTO();
            readDTO.setSuccess(false);
            readDTO.setMessage("file not found: " + filePath);
            readDTO.setRowSet(null);
            return readDTO;
        }

        try (ResultReader rr = this.readerService.openReader(file.getAbsoluteFile())) {
            // position
            if (rowOffset >= rr.getRowCount()) {
                ResultPageDTO readDTO = new ResultPageDTO();
                readDTO.setSuccess(false);
                readDTO.setMessage("No more rows.");
                readDTO.setRowSet(null);
                return readDTO;
            } else {
                rr.nextRow(rowOffset);
            }

            // read rows
            QueryRequest queryInfo = rr.getQueryInfo();
            ColMetaData[] metaInfo = rr.getMetadataInfo();
            int readRows = 0;

            ResultPageDTO result = new ResultPageDTO();
            result.setRowSet(new ArrayList<>());
            while (rr.nextRow()) {
                result.getRowSet().add(this.readRow(rr, queryInfo, metaInfo, rowOffset + readRows));
                readRows++;
                if (readRows >= pageSize) {
                    break;
                }
            }

            doMask(queryInfo, metaInfo, result);
            result.setSuccess(true);
            return result;
        } catch (Exception e) {
            ResultPageDTO result = new ResultPageDTO();
            result.setSuccess(false);
            result.setMessage(e.getMessage());
            result.setRowSet(new ArrayList<>());
            return result;
        }
    }

    public ResultColDTO resultDataRead(RSocketSendDTO sendDTO, String filePath, long rowNumber, long colNumber, long offset, int length) {
        File file = this.fileService.createFileObject(filePath, false);
        if (!this.fileService.existsFile(file)) {
            ResultColDTO readDTO = new ResultColDTO();
            readDTO.setSuccess(false);
            readDTO.setMessage("file not found: " + filePath);
            return readDTO;
        }

        try (ResultReader rr = this.readerService.openReader(file.getAbsoluteFile())) {
            rr.nextRow(); // begin read first row

            // position
            if (rowNumber >= rr.getRowCount()) {
                ResultColDTO readDTO = new ResultColDTO();
                readDTO.setSuccess(false);
                readDTO.setMessage("No more rows.");
                return readDTO;
            } else {
                rr.nextRow(rowNumber); // skip to row
            }
            if (colNumber >= rr.getDataCount()) {
                ResultColDTO readDTO = new ResultColDTO();
                readDTO.setSuccess(false);
                readDTO.setMessage("No more data.");
                return readDTO;
            } else {
                rr.nextData(colNumber);
            }

            // read column
            QueryRequest queryInfo = rr.getQueryInfo();
            ColMetaData meta = rr.getMetadataInfo()[Math.toIntExact(colNumber)];
            ResultSetValue col = rr.readAsString(meta, queryInfo.getResultConf(), offset, length);

            ResultColDTO result = new ResultColDTO();
            result.setSuccess(true);
            result.setValue(col);
            return result;
        } catch (Exception e) {
            ResultColDTO result = new ResultColDTO();
            result.setSuccess(false);
            result.setMessage(e.getClass().getSimpleName() + ": " + e.getMessage());
            log.error("Read result data failed, " + e.getMessage(), e);
            return result;
        }
    }

    private ResultSetRow readRow(ResultReader rr, QueryRequest queryInfo, ColMetaData[] metaInfo, long index) throws IOException {
        ResultSetRow rowDTO = new ResultSetRow();
        rowDTO.setRowId(String.valueOf(index));
        rowDTO.setData(new ArrayList<>());

        for (int i = 0; i < rr.getDataCount(); i++) {
            if (rr.hasNextData()) {
                ResultSetValue col = rr.readAsString(metaInfo[i], queryInfo.getResultConf(), 0, queryInfo.getResultConf().getDisplayChars());
                rowDTO.getData().add(col);
            }
        }

        return rowDTO;
    }

    private void doMask(QueryRequest query, ColMetaData[] metaInfo, ResultPageDTO dataBatch) {
        if (!query.isUsingValueProcess()) {
            return;
        }

        ValueProcessService processSpi = this.sessionService.getProcessSpi();
        if (processSpi == null) {
            throw new UnsupportedOperationException("plugin 'plus-sec-rules' is not exist.");
        }

        Map<String, Object> flash = new ConcurrentHashMap<>();
        Map<String, ColMetaData> meta = new LinkedHashMap<>();
        for (ColMetaData m : metaInfo) {
            meta.put(m.getColumn(), m);
        }

        processSpi.begin(query, meta, flash);
        dataBatch.getRowSet().parallelStream().forEach(row -> {
            List<String> tmpRowData = row.getData().stream().map(ResultSetValue::getValue).collect(Collectors.toList());
            List<String> maskRowData = processSpi.processRow(query, meta, tmpRowData, flash);
            for (int i = 0; i < row.getData().size(); i++) {
                ResultSetValue column = row.getData().get(i);
                String beforeData = column.getValue();
                String afterData = maskRowData.get(i);

                column.setValue(afterData);
                if (!StringUtils.equals(beforeData, afterData)) {
                    column.setMask(true);
                }
            }
        });
        processSpi.finish(query, flash);
    }

}
