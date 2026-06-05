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
package com.clougence.clouddm.onlineddl.ghost;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.atomic.AtomicBoolean;

import com.alibaba.druid.sql.ast.SQLStatement;
import com.alibaba.druid.sql.dialect.mysql.parser.MySqlStatementParser;
import com.alibaba.druid.sql.parser.SQLParserFeature;
import com.alibaba.druid.sql.parser.SQLStatementParser;
import com.alibaba.druid.sql.visitor.VisitorFeature;
import com.clougence.clouddm.base.metadata.ds.rdb.mysql.MySqlConfig;
import com.clougence.clouddm.sdk.model.onlineddl.GhostConfig;
import com.clougence.clouddm.sdk.model.onlineddl.GhostOptionsDTO;
import com.clougence.utils.ExceptionUtils;
import com.clougence.utils.StringUtils;
import com.clougence.utils.io.IOUtils;

import lombok.extern.slf4j.Slf4j;

/**
 * @author bucketli 2022/6/1 15:01:38
 */
@Slf4j
public class GhostSessionOld {

    private final AtomicBoolean   inited                 = new AtomicBoolean(false);

    private static final String   SOCK_FILE_SUFFIX       = ".sock";

    private static final String   PAUSE_FLAG_FILE_SUFFIX = ".pause.flag";

    private static final String   PANIC_FLAG_FILE_SUFFIX = ".panic.flag";

    private final MySqlConfig     mySqlConfig;

    private final GhostConfig     contextDTO;

    private final GhostOptionsDTO optionsDTO;

    private String                database;

    private String                tableName;

    private String                ddlSql;

    private String                cmd;

    private String                cmdJustForLog;

    private String                sockFile;

    private String                pauseFile;

    private String                panicFile;

    public GhostSessionOld(MySqlConfig mySqlConfig, GhostConfig contextDTO){
        this.mySqlConfig = mySqlConfig;
        this.contextDTO = contextDTO;
        this.database = contextDTO.getSchema();
        this.ddlSql = contextDTO.getDdlSql();
        this.optionsDTO = contextDTO.getGhostOptionsDTO();
    }

    //@Override
    public void init() {
        if (inited.compareAndSet(false, true)) {
            initFileDir();
            sockFile = contextDTO.getFileDir() + contextDTO.getSessionId() + SOCK_FILE_SUFFIX;
            pauseFile = contextDTO.getFileDir() + contextDTO.getSessionId() + PAUSE_FLAG_FILE_SUFFIX;
            panicFile = contextDTO.getFileDir() + contextDTO.getSessionId() + PANIC_FLAG_FILE_SUFFIX;
            initAlterTable();

            String toolPath = contextDTO.getToolHomeDir() + "/gh-ost";
            this.cmd = initCmd(toolPath, mySqlConfig, database, tableName, ddlSql, contextDTO.getLogFileFullPath(), sockFile, panicFile, pauseFile, optionsDTO);
        }
    }

    protected void initFileDir() {
        File f = new File(contextDTO.getFileDir());
        if (!f.exists()) {
            boolean suc = f.mkdirs();
            if (!suc) {
                throw new RuntimeException(contextDTO.getFileDir() + " directory create failed.");
            }
        }
    }

    protected void initAlterTable() {
        SQLStatementParser parser = new MySqlStatementParser(ddlSql, SQLParserFeature.IgnoreNameQuotes);
        SQLStatement statement = parser.parseAlter();

        if (statement == null) {
            throw new IllegalArgumentException("illegal alter sql:" + ddlSql);
        }

        StringBuilder tabBuilder = new StringBuilder();
        StringBuilder dbBuilder = new StringBuilder();
        StringBuilder sqlBuilder = new StringBuilder();

        MySqlCollectAlterTableVisitor v = new MySqlCollectAlterTableVisitor(sqlBuilder, tabBuilder, dbBuilder);
        v.config(VisitorFeature.OutputNameQuote, true);
        v.config(VisitorFeature.OutputPrettyFormat, false);
        statement.accept(v);

        String rewriteSql = sqlBuilder.toString();
        if (StringUtils.isNotBlank(rewriteSql)) {
            this.ddlSql = rewriteSql;
        }

        String tabInDdl = tabBuilder.toString();
        if (StringUtils.isNotBlank(tabInDdl)) {
            this.tableName = tabInDdl;
        }

        String dbInDdl = dbBuilder.toString();
        if (StringUtils.isNotBlank(dbBuilder.toString())) {
            if (StringUtils.isNotBlank(this.database) && !this.database.equals(dbInDdl)) {
                log.info("ddl sql specified database (" + dbInDdl + ") is different from passed database parameter(" + this.database + "),use value in ddl sql.");
            }

            this.database = dbInDdl;
        }
    }

    //@Override
    public void start() {
        Process process = null;
        try {
            log.info("[GH-OST] exec start.ddl sql:" + ddlSql);
            String[] cmds = { "/bin/sh", "-c", cmd };

            process = Runtime.getRuntime().exec(cmds);

            //when execute , this will block.
            standardOutput(process);

            String resultError = getErrorOutput(process);
            if (StringUtils.isNotBlank(resultError)) {
                throw new RuntimeException("gh-ost execute cmd error.msg:" + resultError);
            }
        } catch (Throwable e) {
            String errMsg = "execute shell error,ddl sql:" + ddlSql + ",msg:" + ExceptionUtils.getRootCauseMessage(e);
            log.error(errMsg, e);
            throw new RuntimeException(errMsg, e);
        } finally {
            if (null != process) {
                process.destroy();
            }
        }
    }

    //@Override
    public void pause() {
        File f = new File(pauseFile);
        if (f.exists()) {
            log.warn(pauseFile + " already exist,ghost should stop already.");
            return;
        }

        try {
            boolean suc = f.createNewFile();
            if (!suc) {
                log.warn(pauseFile + " create return false.");
            }
        } catch (Exception e) {
            throw new IllegalArgumentException("create " + pauseFile + " failed,msg:" + ExceptionUtils.getRootCauseMessage(e), e);
        }
    }

    //@Override
    public void resume() {
        File f = new File(pauseFile);
        if (!f.exists()) {
            log.warn(pauseFile + " not exist,ghost should running yet.");
            return;
        }

        try {
            boolean suc = f.delete();
            if (!suc) {
                log.warn(pauseFile + " delete return false.");
            }
        } catch (Exception e) {
            throw new IllegalArgumentException("delete " + pauseFile + " failed,msg:" + ExceptionUtils.getRootCauseMessage(e), e);
        }
    }

    //@Override
    public void stop() {
        File f = new File(panicFile);
        if (f.exists()) {
            log.warn(panicFile + " already exist,ghost should exit already.");
            return;
        }

        try {
            boolean suc = f.createNewFile();
            if (!suc) {
                log.warn(panicFile + " create return false.");
            }
        } catch (Exception e) {
            throw new IllegalArgumentException("create " + panicFile + " failed,msg:" + ExceptionUtils.getRootCauseMessage(e), e);
        }
    }

    //@Override
    public void clean() {
        File sf = new File(sockFile);
        if (sf.exists()) {
            boolean suc = sf.delete();
            if (!suc) {
                log.warn(sockFile + " delete return false.");
            }
        }

        File pf = new File(pauseFile);
        if (pf.exists()) {
            boolean suc = pf.delete();
            if (!suc) {
                log.warn(pauseFile + " delete return false.");
            }
        }

        File paf = new File(panicFile);
        if (paf.exists()) {
            boolean suc = paf.delete();
            if (!suc) {
                log.warn(panicFile + " delete return false.");
            }
        }
    }

    //@Override
    public String getCmdWithoutSecret() { return cmdJustForLog; }

    private void standardOutput(Process process) throws IOException {
        try (InputStream input = process.getInputStream(); OutputStream output = new BufferedOutputStream(new FileOutputStream(contextDTO.getLogFileFullPath()))) {
            IOUtils.copy(input, output);
        }
    }

    private String getErrorOutput(Process process) throws IOException {
        try (InputStream es = process.getErrorStream()) {
            return getStrFromInputSteam(es);
        }
    }

    private String getStrFromInputSteam(InputStream inputStream) throws IOException {
        try (InputStreamReader ir = new InputStreamReader(inputStream, StandardCharsets.UTF_8); BufferedReader br = new BufferedReader(ir)) {
            StringBuilder buffer = new StringBuilder();
            String line;

            while ((line = br.readLine()) != null) {
                buffer.append(line).append("\n");
            }

            return buffer.toString();
        }
    }

    protected String initCmd(String toolPath, MySqlConfig config, String database, String tableName, String ddlSql, String logFile, String sockFile, String panicFile,
                             String pauseFile, GhostOptionsDTO options) {
        StringBuilder cmdBuilder = new StringBuilder(toolPath);
        StringBuilder cmdBuidlerForLog = new StringBuilder(toolPath);
        appendHostPort(cmdBuilder, config.getHost());
        appendHostPort(cmdBuidlerForLog, config.getHost());

        cmdBuilder.append(" --user=\"").append(config.getUserName()).append("\"");
        cmdBuilder.append(" --password=\"").append(config.getPassword()).append("\"");
        cmdBuilder.append(" --database=\"").append(database).append("\"");
        cmdBuilder.append(" --table=\"").append(tableName).append("\"");
        cmdBuilder.append(" --alter=\"").append(ddlSql).append("\"");

        cmdBuidlerForLog.append(" --user=\"").append("******").append("\"");
        cmdBuidlerForLog.append(" --password=\"").append("******").append("\"");
        cmdBuidlerForLog.append(" --database=\"").append(database).append("\"");
        cmdBuidlerForLog.append(" --table=\"").append(tableName).append("\"");
        cmdBuidlerForLog.append(" --alter=\"").append(ddlSql).append("\"");

        //if datasource is aliyun rds or proxy in front os ds or ds in docker.set this config and let --port affect.
        if (StringUtils.isNotBlank(config.getAliyunInstanceId()) || (options != null && options.getAssumeDsProxyInFront() != null && options.getAssumeDsProxyInFront())) {
            cmdBuilder.append(" --aliyun-rds");
            cmdBuidlerForLog.append(" --aliyun-rds");
        }

        if (options != null && options.getAssumeRbr()) {
            cmdBuilder.append(" --assume-rbr");
            cmdBuidlerForLog.append(" --assume-rbr");
        }

        if (options != null && options.getAllowOnMaster()) {
            cmdBuilder.append(" --allow-on-master");
            cmdBuidlerForLog.append(" --allow-on-master");
        }

        if (options != null && options.getCutOverLockTimeoutSec() > 0) {
            cmdBuilder.append(" --cut-over-lock-timeout-seconds=").append(options.getCutOverLockTimeoutSec());
            cmdBuidlerForLog.append(" --cut-over-lock-timeout-seconds=").append(options.getCutOverLockTimeoutSec());
        }

        if (options != null && options.getDmlBatchSize() > 0) {
            cmdBuilder.append(" --dml-batch-size=").append(options.getDmlBatchSize());
            cmdBuidlerForLog.append(" --dml-batch-size=").append(options.getDmlBatchSize());
        }

        if (options != null && options.getChunkSize() > 0) {
            cmdBuilder.append(" --chunk-size=").append(options.getChunkSize());
            cmdBuidlerForLog.append(" --chunk-size=").append(options.getChunkSize());
        }

        if (options != null && options.getVerbose()) {
            cmdBuilder.append(" --verbose");
            cmdBuidlerForLog.append(" --verbose");
        }

        if (options != null && options.getCutOverType() != null) {
            cmdBuilder.append(" --cut-over=").append(options.getCutOverType().getVal());
            cmdBuidlerForLog.append(" --cut-over=").append(options.getCutOverType().getVal());
        }

        if (options != null && options.getExactRowCount()) {
            cmdBuilder.append(" --exact-rowcount");
            cmdBuidlerForLog.append(" --exact-rowcount");
        }

        if (options != null && options.getConcurrentRowCount()) {
            cmdBuilder.append(" --concurrent-rowcount");
            cmdBuidlerForLog.append(" --concurrent-rowcount");
        }

        if (options != null && options.getHbIntervalMs() > 0) {
            cmdBuilder.append(" --heartbeat-interval-millis=").append(options.getHbIntervalMs());
            cmdBuidlerForLog.append(" --heartbeat-interval-millis=").append(options.getHbIntervalMs());
        }

        if (options != null && options.getInitiallyDropGhostTable()) {
            cmdBuilder.append(" --initially-drop-ghost-table");
            cmdBuidlerForLog.append(" --initially-drop-ghost-table");
        }

        if (options != null && options.getTimestampOldTable()) {
            cmdBuilder.append(" --timestamp-old-table");
            cmdBuidlerForLog.append(" --timestamp-old-table");
        }

        cmdBuilder.append(" --serve-socket-file=\"").append(sockFile).append("\"");
        cmdBuilder.append(" --throttle-flag-file=\"").append(pauseFile).append("\"");
        cmdBuilder.append(" --panic-flag-file=\"").append(panicFile).append("\"");
        cmdBuilder.append(" --execute");

        cmdBuidlerForLog.append(" --serve-socket-file=\"").append(sockFile).append("\"");
        cmdBuidlerForLog.append(" --throttle-flag-file=\"").append(pauseFile).append("\"");
        cmdBuidlerForLog.append(" --panic-flag-file=\"").append(panicFile).append("\"");
        cmdBuidlerForLog.append(" --execute");

        cmdJustForLog = cmdBuidlerForLog.toString();

        return cmdBuilder.toString();
    }

    private void appendHostPort(StringBuilder cmd, String host) {
        String[] ipPort = host.split(":");
        String ip;
        String port;
        if (ipPort.length == 2) {
            ip = ipPort[0];
            port = ipPort[1];
        } else if (ipPort.length == 1) {
            ip = ipPort[0];
            port = "3306";
        } else {
            throw new IllegalArgumentException("MySQL host is illegal:" + host);
        }

        cmd.append(" --host=").append(ip);
        cmd.append(" --port=").append(port);
        cmd.append(" --assume-master-host=").append(ip).append(":").append(port);
    }
}
