package com.clougence.clouddm.faker.config.ui;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.clougence.clouddm.faker.config.FakerEngineConfig;
import com.clougence.clouddm.faker.config.UseFor;
import com.clougence.clouddm.faker.config.dsl.TypeProcessConf;
import com.clougence.clouddm.faker.config.processor.DslTypeProcessorFactory;
import com.clougence.clouddm.faker.seed.SeedType;
import com.clougence.clouddm.sdk.model.faker.FakerRunModel;
import com.clougence.clouddm.sdk.ui.faker.FakerUiData;
import com.clougence.clouddm.base.metadata.ds.DataSourceType;
import com.clougence.schema.metadata.FieldType;
import com.clougence.schema.umi.special.rdb.RdbColumn;
import com.clougence.utils.BeanUtils;
import com.clougence.utils.ExceptionUtils;

/**
 * @author olddream
 */
public class FakerUiDataService implements FakerUiFieldKeys {

    private static final Logger log = LoggerFactory.getLogger(FakerUiDataService.class);

    public FakerUiData fillFakerUiData(DataSourceType dsType, List<RdbColumn> columns, FakerRunModel type) {
        FakerUiData uiData = new FakerUiData();
        fillColumnUiData(dsType, columns, uiData, type);

        return uiData;
    }

    private void fillColumnUiData(DataSourceType dsType, List<RdbColumn> columns, FakerUiData uiData, FakerRunModel type) {
        List<Map<String, Object>> columnList = new ArrayList<>();

        FakerEngineConfig fakerConfig = new FakerEngineConfig();
        fakerConfig.setDsType(dsType);
        DslTypeProcessorFactory dslTypeProcessorFactory;
        try {
            dslTypeProcessorFactory = new DslTypeProcessorFactory(dsType, new HashMap<>(), new FakerEngineConfig());
        } catch (IOException e) {
            throw new RuntimeException(ExceptionUtils.getMessage(e));
        }
        for (RdbColumn column : columns) {
            Map<String, Object> columnSeed = new HashMap<>();
            columnSeed.put(COLUMN_NAME, column.getName());

            boolean columnSupport = true;
            List<String> ignoreAct = new ArrayList<>();

            SeedType seedType;
            try {
                seedType = dslTypeProcessorFactory.findSeedType(column);
                if (seedType != null) {
                    columnSeed.put(COLUMN_SEED, seedType.name());
                } else {
                    columnSupport = false;
                }

                // faker not support enums, now
                if (seedType != null && seedType.equals(SeedType.Enums)) {
                    columnSupport = false;
                } else {
                    FieldType colType = column.getSqlType();
                    String colTypeStr = colType.getCodeKey();
                    TypeProcessConf typeProcessConf = dslTypeProcessorFactory.findTypeProcessConf(colTypeStr.toLowerCase(), "ignoreAct");
                    if (typeProcessConf != null) {
                        Map<String, Object> variables = new HashMap<>();
                        BeanUtils.copyProperties(variables, column);
                        List<UseFor> useForList = (List<UseFor>) typeProcessConf.recover(variables, "recover colType [" + colType + "]");
                        if (useForList != null && !useForList.isEmpty()) {
                            if (type == FakerRunModel.FULL && useForList.contains(UseFor.Insert)) {
                                columnSupport = false;
                            }
                        }
                    }
                }
            } catch (Exception e) {
                columnSupport = false; // TODO may be error, missing COLUMN_SEED
                log.error(e.getMessage(), e);
            }

            columnSeed.put(COLUMN_SUPPORT, columnSupport);
            columnSeed.put(COLUMN_IGNORE_ACT, ignoreAct);
            columnList.add(columnSeed);
        }

        uiData.setColumns(columnList);
    }
}
