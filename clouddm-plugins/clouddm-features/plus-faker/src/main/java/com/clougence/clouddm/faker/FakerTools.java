package com.clougence.clouddm.faker;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

import org.slf4j.Logger;

import com.clougence.clouddm.base.metadata.ds.DataSourceConfig;
import com.clougence.clouddm.base.metadata.ds.tools.FakerPluginConfig;
import com.clougence.clouddm.faker.config.FakerEngineConfig;
import com.clougence.clouddm.faker.config.OpsType;
import com.clougence.clouddm.faker.engine.*;
import com.clougence.clouddm.faker.generator.FakerFactory;
import com.clougence.clouddm.faker.generator.FakerRepository;
import com.clougence.clouddm.faker.generator.FakerTable;
import com.clougence.clouddm.faker.generator.loader.PrecociousDataLoaderFactory;
import com.clougence.clouddm.sdk.execute.session.SessionSpi;
import com.clougence.clouddm.sdk.execute.tools.*;
import com.clougence.clouddm.sdk.model.faker.*;
import com.clougence.clouddm.sdk.service.config.ConfigService;
import com.clougence.clouddm.sdk.service.execute.SessionService;
import com.clougence.schema.dialect.Dialect;
import com.clougence.utils.ExceptionUtils;
import com.clougence.utils.HostUtil;
import com.clougence.utils.JsonUtils;
import com.clougence.utils.format.WellKnowFormat;

import com.clougence.utils.setting.provider.StreamType;

/**
 * @author olddream
 */
public class FakerTools implements Tool, FakerMethod {

    private static final Logger     logger            = ToolUtils.getLoggerAppender();
    private final SessionService    sessionService;
    private final ConfigService     configService;
    private final FakerPluginConfig globalConfig;
    private FakerConfigDTO          fakerConfig;
    private String                  toolSessionId;
    private FakerEngine             engine;

    // for tool status
    private long                    statusProgressMax = 0;

    public FakerTools(SessionService sessionService, ConfigService configService, FakerPluginConfig globalConfig){
        this.sessionService = sessionService;
        this.configService = configService;
        this.globalConfig = globalConfig;
    }

    @Override
    public void init(ToolSessionContextDTO contextDTO) throws Exception {
        FakerConfigDTO fakerConfig = JsonUtils.toObj(contextDTO.getConfiguration(), FakerConfigDTO.class);
        this.toolSessionId = contextDTO.getSessionId();
        this.fakerConfig = fakerConfig;

        // session
        DataSourceConfig dsConfig = this.configService.fetchDsConfig(fakerConfig.getDsId(), fakerConfig.getDsType());
        SessionFactory sessionFactory = this.createSessionFactory(dsConfig, fakerConfig.getCatalog(), fakerConfig.getSchema());

        // engineConfig
        FakerEngineConfig engineConfig = new FakerEngineConfig();
        engineConfig.setDsType(fakerConfig.getDsType());
        engineConfig.setTransaction(fakerConfig.isTransaction());
        engineConfig.setPThreadCnt(fakerConfig.getPThreadCnt());
        engineConfig.setWThreadCnt(fakerConfig.getWThreadCnt());
        //engineConfig.setPolicy("extreme");
        engineConfig.setDataLoaderFactory(new PrecociousDataLoaderFactory());
        if (fakerConfig.isIgnoreErrors()) {
            engineConfig.addIgnoreError("Duplicate");
            engineConfig.addIgnoreError("Data truncation");
            engineConfig.addIgnoreError("You have an error in your SQL syntax");
        }
        engineConfig.setOpsRatio(parseOpsRatio(fakerConfig));
        //engineConfig.setWriteQps(10);

        // init engine
        Dialect dialect = this.configService.findDialectByDsType(fakerConfig.getDsType());
        FakerFactory factory = new FakerFactory(engineConfig, sessionFactory, dialect);
        FakerRepository generator = new FakerRepository(factory);
        generator.loadConfig(fakerConfig.getYaml(), StreamType.Yaml, Collections.emptyMap());

        this.engine = new FakerEngine(fakerConfig, sessionFactory, generator, createCondition(generator, fakerConfig));
    }

    private AbstractCondition createCondition(FakerRepository repository, FakerConfigDTO config) {
        if (config.getRunModel() == FakerRunModel.FULL) {
            Map<String, Integer> writerTotal = config.getWriterTotal();

            for (int tabCnt : config.getWriterTotal().values()) {
                this.statusProgressMax = this.statusProgressMax + tabCnt;
            }

            for (FakerTable tab : repository.getGeneratorTables()) {
                if (writerTotal.containsKey(tab.getTable())) {
                    tab.acceptTotal(OpsType.Insert, writerTotal.get(tab.getTable()));
                    tab.apply();
                } else {
                    repository.removeTable(tab.getCatalog(), tab.getSchema(), tab.getTable());
                }
            }

            logger.info("work at Full, writerTotal is " + this.statusProgressMax);
            return new ConditionForFull(repository);
        } else if (config.getRunModel() == FakerRunModel.INCREMENT) {
            logger.info("work at Increment, workingTime is " + config.getWorkingTime() + " second.");
            return new ConditionForIncrement(config.getWorkingTime() * 1000);
        } else {
            throw new UnsupportedOperationException(config.getRunModel() + " Unsupported.");
        }
    }

    private static String parseOpsRatio(FakerConfigDTO config) {
        String result;
        if (config.getRunModel().equals(FakerRunModel.FULL)) {
            result = "INSERT#100";
        } else {
            Integer deleteRatio = config.getDeleteRatio();
            Integer updateRatio = config.getUpdateRatio();
            Integer insertRatio = config.getInsertRatio();
            result = String.format("INSERT#%s;UPDATE#%s;DELETE#%s", insertRatio, updateRatio, deleteRatio);
        }

        logger.info("config generate opsType is " + result);
        return result;
    }

    private ToolResultDTO preview() {
        Map<String, List<FakerLineDTO>> detail = new HashMap<>();
        for (FakerTable tab : this.engine.getRepository().getGeneratorTables()) {
            List<FakerLineDTO> preview = tab.buildPreview(50);
            detail.put(tab.getTable(), preview);
        }

        FakerPreviewDTO resultDTO = new FakerPreviewDTO();
        resultDTO.setPreview(detail);
        return ToolUtils.buildSuccess(JsonUtils.toJson(resultDTO));
    }

    @Override
    public ToolResultDTO invoke(String methodKey, ToolRequestDTO requestDTO) {
        if (START.equals(methodKey)) {
            return ToolUtils.build(this.engine.start());
        } else if (PAUSE.equals(methodKey)) {
            return ToolUtils.build(this.engine.pause());
        } else if (RESUME.equals(methodKey)) {
            return ToolUtils.build(this.engine.resume());
        } else if (PREVIEW.equals(methodKey)) {
            return this.preview();
        } else {
            String msg = "'" + methodKey + "' undefined method.";
            logger.error(msg);
            throw new IllegalStateException(msg);
        }
    }

    @Override
    public void close() {
        logger.info("faker to stop.");
        if (this.engine != null) {
            this.engine.stop(true);
        }
        logger.info("faker stop complete.");

        if (!Boolean.TRUE.equals(this.globalConfig.getKeepLog())) {
            File logAbsFile = ToolUtils.getLogFile();
            logAbsFile.delete();
            logAbsFile.getParentFile().delete();
        }
    }

    private FakerTailResponseDTO createTailInfo(String logAbsoluteFile, FakerTailRequestDTO request) {
        FakerTailResponseDTO logVO = new FakerTailResponseDTO();

        logVO.setSuccessTotal(this.engine.getMonitor().getSucceed());
        logVO.setSuccessInsertTotal(this.engine.getMonitor().getSucceedInsert());
        logVO.setSuccessUpdateTotal(this.engine.getMonitor().getSucceedUpdate());
        logVO.setSuccessDeleteTotal(this.engine.getMonitor().getSucceedDelete());
        logVO.setFailedTotal(this.engine.getMonitor().getFailed());
        logVO.setFailedInsertTotal(this.engine.getMonitor().getFailedInsert());
        logVO.setFailedUpdateTotal(this.engine.getMonitor().getFailedUpdate());
        logVO.setFailedDeleteTotal(this.engine.getMonitor().getFailedDelete());

        logVO.setWriteAvgTimeMs(this.engine.getMonitor().getWriteAvgTime());
        logVO.setEndLine(request.getStartLine());
        logVO.setLogFile(logAbsoluteFile);
        logVO.setLogHost(HostUtil.getHostIp());

        return logVO;
    }

    @Override
    public ToolResultDTO tailLog(ToolRequestDTO requestDTO) {
        FakerTailRequestDTO request = JsonUtils.toObj(requestDTO.getBody(), FakerTailRequestDTO.class);
        File logAbsFile = ToolUtils.getLogFile();
        String logAbsoluteFile = logAbsFile.getAbsolutePath();

        if (!logAbsFile.exists()) {
            FakerTailResponseDTO logVO = this.createTailInfo(logAbsoluteFile, request);
            logVO.setLogArr(Collections.singletonList("logFile " + logAbsoluteFile + " does not exist."));
            return ToolUtils.buildSuccess(JsonUtils.toJson(logVO));
        }

        if (!logAbsFile.canRead()) {
            FakerTailResponseDTO logVO = this.createTailInfo(logAbsoluteFile, request);
            logVO.setLogArr(Collections.singletonList("logFile " + logAbsoluteFile + " no read permission."));
            return ToolUtils.buildSuccess(JsonUtils.toJson(logVO));
        }

        List<String> logData = new LinkedList<>();
        int startLine = request.getStartLine();
        int endLine = startLine + 25;
        try (BufferedReader reader = new BufferedReader(new FileReader(logAbsFile))) {
            LinkedList<InnerLine> tmpData = new LinkedList<>();

            // get total
            int count = 0;
            for (;;) {
                String test = reader.readLine();
                if (test == null) {
                    break;
                }
                tmpData.add(new InnerLine(count, test));
                count++;
            }

            // page
            if (startLine == 0) {
                for (int i = tmpData.size() - 1; i >= 0; i--) { // make sure new message
                    if (endLine <= 0) {
                        break;
                    }
                    InnerLine line = tmpData.get(i);
                    logData.add(line.test);
                    endLine--;
                }
                Collections.reverse(logData);
                endLine = 25;
            } else {
                for (int i = 0; i < tmpData.size(); i++) {
                    if (i > endLine) {
                        break;
                    }
                    if (i >= startLine) {
                        InnerLine line = tmpData.get(i);
                        logData.add(line.test);
                    }
                }
            }

            //            if (startLine != -1) {
            //                for (InnerLine line : tmpData) {
            //                    if (line.line <= startLine) {
            //                        continue;
            //                    }
            //                    logData.add(line.test);
            //                    endLine = line.line;
            //                }
            //            } else {
            //                for (InnerLine line : tmpData) {
            //                    logData.add(line.test);
            //                    endLine = line.line;
            //                }
            //            }
        } catch (Exception e) {
            FakerTailResponseDTO logVO = this.createTailInfo(logAbsoluteFile, request);
            logVO.setLogArr(Collections.singletonList("logFile " + logAbsoluteFile + " read failed, " + ExceptionUtils.getRootCauseMessage(e)));
            return ToolUtils.buildSuccess(JsonUtils.toJson(logVO));
        }

        FakerTailResponseDTO logVO = this.createTailInfo(logAbsoluteFile, request);
        logVO.setStatus(this.engine.getStatus());
        logVO.setLogArr(logData);
        logVO.setEndLine(endLine);
        return ToolUtils.buildSuccess(JsonUtils.toJson(logVO));
    }

    @Override
    public ToolResultDTO tailStatus(ToolRequestDTO requestDTO) {
        FakerStatusDTO statusDTO = new FakerStatusDTO();
        statusDTO.setStatus(this.engine.getStatus());

        switch (this.engine.getStatus()) {
            case INIT:
                statusDTO.setUseProgress(false);
                statusDTO.setMessage("[Ready] Generate data for " + this.fakerConfig.getWriterTotal().size() + " tables.");
                break;
            case COMPLETE:
                statusDTO.setUseProgress(false);
                statusDTO.setMessage("[Complete] Generator Task finish.");
                break;
            case PAUSE:
            case WAITING_PAUSE:
            case RUNNING:
            case WAITING_RESUME:
                tailStatusForEngine(statusDTO);
                break;
        }

        return ToolUtils.buildSuccess(JsonUtils.toJson(statusDTO));
    }

    private void tailStatusForEngine(FakerStatusDTO statusDTO) {
        FakerMonitor monitor = this.engine.getMonitor();
        switch (this.fakerConfig.getRunModel()) {
            case FULL: {
                statusDTO.setUseProgress(true);
                statusDTO.setCurValue(monitor.getSucceedInsert());
                statusDTO.setMaxValue(this.statusProgressMax);
                statusDTO.setMessage(monitor.getInformation(new OpsType[] { OpsType.Insert }));
                break;
            }
            case INCREMENT: {
                statusDTO.setUseProgress(true);
                DateTimeFormatter tf = WellKnowFormat.WKF_TIME24.toPattern();
                ZonedDateTime runTime = new Date(monitor.getWorkingTimeMs()).toInstant().atZone(ZoneId.of("UTC"));
                int cur = this.getSecond(runTime);
                statusDTO.setCurValue(cur);
                ZonedDateTime totalTime = new Date(this.fakerConfig.getWorkingTime() * 1000).toInstant().atZone(ZoneId.of("UTC"));
                int total = this.getSecond(totalTime);
                statusDTO.setMaxValue(total);
                String fmtTemp = "%s/%s, Succeed: %s, Failed: %s";
                statusDTO.setMessage(String.format(fmtTemp, tf.format(runTime), tf.format(totalTime), monitor.getSucceed(), monitor.getFailed()));
                break;
            }
        }
    }

    private int getSecond(ZonedDateTime time) {
        int second = 0;
        second += (time.getHour() * 60 * 60);
        second += (time.getMinute() * 60);
        second += time.getSecond();
        return second;
    }

    private SessionFactory createSessionFactory(DataSourceConfig dsConfig, String catalog, String schema) {
        Map<String, Object> params = new HashMap<>();
        params.put(SessionSpi.PARAMS_DEFAULT_DB, catalog);
        params.put(SessionSpi.PARAMS_DEFAULT_SCHEMA, schema);
        return new SessionFactory(this.sessionService, dsConfig, params);
    }

    private static class InnerLine {

        private final int    line;
        private final String test;

        public InnerLine(int line, String test){
            this.line = line;
            this.test = test;
        }
    }
}
