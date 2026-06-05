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
package com.clougence.clouddm.sec.rules.execute.sensitive;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.clougence.clouddm.base.metadata.ds.ColMetaData;
import com.clougence.clouddm.sdk.analysis.column.RealColumn;
import com.clougence.clouddm.sdk.service.secrules.CheckerData;
import com.clougence.clouddm.sdk.service.secrules.CheckerOptions;
import com.clougence.clouddm.sdk.service.secrules.CheckerRule;
import com.clougence.clouddm.sdk.service.secrules.SecResult;
import com.clougence.clouddm.sdk.execute.ExecuteVariables;
import com.clougence.clouddm.sdk.execute.session.QueryRequest;
import com.clougence.clouddm.sdk.execute.session.result.ValueProcessService;
import com.clougence.clouddm.sdk.model.analysis.TargetType;
import com.clougence.clouddm.sdk.service.secrules.RuleDomain;
import com.clougence.clouddm.sdk.service.secrules.SensitiveConfig;
import com.clougence.clouddm.sdk.service.secrules.SensitiveMode;
import com.clougence.clouddm.sdk.service.cache.CacheService;
import com.clougence.clouddm.sdk.service.config.ConfigService;
import com.clougence.clouddm.sec.rules.domain.special.rdb.RdbValueDomain;
import com.clougence.schema.umi.struts.UmiTypes;
import com.clougence.utils.CollectionUtils;
import com.clougence.utils.StringUtils;

import lombok.SneakyThrows;

/**
 * @author mode create time is 2023/05/21 13:27
 **/
public class SecValueProcessServiceProvider implements ValueProcessService {

    private final static String       SEN_KEY_CFG      = SensitiveConfig.class.getSimpleName() + "-root";
    private final static String       SEN_KEY_SKIP     = SensitiveConfig.class.getSimpleName() + "-skip";
    private final static String       SEN_KEY_DS_LEVEL = SensitiveConfig.class.getSimpleName() + "-dsLevel";

    private final CacheService        cacheService;
    private final ConfigService       configService;
    private final SecSensitiveChecker checkerProvider;

    public SecValueProcessServiceProvider(CacheService cacheService, ConfigService configService){
        this.cacheService = cacheService;
        this.configService = configService;
        this.checkerProvider = new SecSensitiveChecker(cacheService);
    }

    @Override
    @SneakyThrows
    public void begin(QueryRequest query, Map<String, ColMetaData> rowMeta, Map<String, Object> flash) {
        String dsId = query.getVariables().get(ExecuteVariables.DS_ID);
        if (StringUtils.isBlank(dsId)) {
            throw new IllegalArgumentException("process Value failed, the variable DS_ID is required.");
        }

        SensitiveConfig conf = configService.fetchSensitiveConfigByDs(Long.parseLong(dsId));
        //        SensitiveConfig conf = (SensitiveConfig) this.cacheService.getObjectIfAbsent("ValueProcess-cache-key-" + dsId, s -> {
        //            return configService.fetchSensitiveConfigByDs(Long.parseLong(dsId));
        //        });

        if (conf == null) {
            flash.put(SEN_KEY_SKIP, true);
            return;
        } else {
            flash.put(SEN_KEY_SKIP, false);
        }

        flash.put(SEN_KEY_CFG, conf);
        flash.put(SEN_KEY_DS_LEVEL, this.configService.fetchDsLevelDef(conf.getDsType()));
    }

    @Override
    public void finish(QueryRequest query, Map<String, Object> flash) {
        flash.clear();
    }

    @Override
    public List<String> processRow(QueryRequest query, Map<String, ColMetaData> meta, List<String> rowData, Map<String, Object> flash) {
        if ((Boolean) flash.get(SEN_KEY_SKIP)) {
            return rowData;
        }

        SensitiveConfig conf = (SensitiveConfig) flash.get(SEN_KEY_CFG);
        ColMetaData[] colMeta = meta.values().toArray(new ColMetaData[0]);
        List<String> allColumns = meta.values().stream().map(ColMetaData::getColumn).collect(Collectors.toList());
        String rowAlgorithm = SenAlgorithm.SEN_ORIGINAL;

        SenColValue[] colValues = new SenColValue[colMeta.length];
        if (CollectionUtils.isNotEmpty(query.getColumnList())) {
            int i = 0;
            for (String s : meta.keySet()) {
                ColMetaData metaData = meta.get(s);
                colValues[i] = new SenColValue(metaData, rowData.get(i), SenAlgorithm.SEN_ORIGINAL);
                CheckerData checkerData = this.createCheckerData(i, query, conf, colValues[i], flash);
                List<RealColumn> realColumns = query.getColumnList().get(s);
                for (RealColumn column : realColumns) {
                    if (!column.isSkipDesensitization()) {
                        RuleDomain domain = checkerData.getDomain();
                        RdbValueDomain valueDomain = (RdbValueDomain) domain;
                        valueDomain.setCatalog(column.getCatalog());
                        valueDomain.setSchema(column.getSchema());
                        valueDomain.setTable(column.getTable());
                        valueDomain.setColumn(column.getColumn());
                        rowAlgorithm = doCheck(query, conf, checkerData, allColumns, rowAlgorithm, colValues[i]);
                    }
                }
                i++;
            }
        } else {
            for (int i = 0; i < colMeta.length; i++) {
                colValues[i] = new SenColValue(colMeta[i], rowData.get(i), SenAlgorithm.SEN_ORIGINAL);
                CheckerData checkerData = this.createCheckerData(i, query, conf, colValues[i], flash);
                rowAlgorithm = doCheck(query, conf, checkerData, allColumns, rowAlgorithm, colValues[i]);
            }
        }

        if (!StringUtils.equalsIgnoreCase(rowAlgorithm, SenAlgorithm.SEN_ORIGINAL)) {
            for (SenColValue colValue : colValues) {
                String algorithm = SenAlgorithm.chooseAlgorithm(colValue.getAlgorithm(), rowAlgorithm);
                colValue.setColValue(doAlgorithm(algorithm, colValue.getColValue()));
            }
        } else {
            for (SenColValue colValue : colValues) {
                colValue.setColValue(doAlgorithm(colValue.getAlgorithm(), colValue.getColValue()));
            }
        }

        return Arrays.stream(colValues).map(SenColValue::getColValue).collect(Collectors.toList());
    }

    private String doCheck(QueryRequest query, SensitiveConfig conf, CheckerData checkerData, List<String> allColumns, String rowAlgorithm, SenColValue colValues) {
        CheckerOptions options = new CheckerOptions();
        options.setDsType(conf.getDsType());
        options.setRequester(query.getRequester());
        for (CheckerRule checker : conf.getSenRuleList()) {
            options.setParameters(checker.getParameters());
            ((RdbValueDomain) checkerData.getDomain()).setSenAlgorithm(colValues.getAlgorithm());
            ((RdbValueDomain) checkerData.getDomain()).setHasRange(!checker.getRangeList().isEmpty());
            ((RdbValueDomain) checkerData.getDomain()).setAllColumns(allColumns);

            SecResult result = this.checkerProvider.doChecker(checker, checkerData, options);
            String curAlgorithm = colValues.getAlgorithm();
            String suggestAlgorithm = (String) result.getResult();
            colValues.setAlgorithm(SenAlgorithm.chooseAlgorithm(curAlgorithm, suggestAlgorithm));

            if (checker.getSenMode() == SensitiveMode.Row) {
                rowAlgorithm = SenAlgorithm.chooseAlgorithm(rowAlgorithm, suggestAlgorithm);
            }
        }
        return rowAlgorithm;
    }

    private List<UmiTypes> getDsLevels(Map<String, Object> flash) {
        Object dsLevels = flash.get(SEN_KEY_DS_LEVEL);
        if (!(dsLevels instanceof List<?>)) {
            return Collections.emptyList();
        }

        return ((List<?>) dsLevels).stream()
            .filter(UmiTypes.class::isInstance)
            .map(UmiTypes.class::cast)
            .collect(Collectors.toList());
    }

    private CheckerData createCheckerData(int index, QueryRequest query, SensitiveConfig conf, SenColValue senCol, Map<String, Object> flash) {
        String catalog = senCol.getColMeta().getCatalog();
        String schema = senCol.getColMeta().getSchema();
        String table = senCol.getColMeta().getTable();
        String column = senCol.getColMeta().getColumn();
        List<UmiTypes> typesList = getDsLevels(flash);

        if (CollectionUtils.isNotEmpty(query.getResource())) {
            Map<TargetType, String> stringMap = query.getResource().get(0);
            catalog = typesList.contains(UmiTypes.Catalog) ? stringMap.get(TargetType.Catalog) : null;
            schema = typesList.contains(UmiTypes.Schema) ? stringMap.get(TargetType.Schema) : null;
            if (stringMap.containsKey(TargetType.Table)) {
                table = stringMap.get(TargetType.Table);
            } else if (stringMap.containsKey(TargetType.View)) {
                table = stringMap.get(TargetType.View);
            } else if (stringMap.containsKey(TargetType.Materialized)) {
                table = stringMap.get(TargetType.Materialized);
            }
        }

        RdbValueDomain domain = new RdbValueDomain();

        domain.setSqlType(query.getQueryType());
        domain.setAuditKind(query.getQueryType().getAuditKind());
        domain.setOptions(Collections.emptyMap());

        domain.setCatalog(catalog);
        domain.setSchema(schema);
        domain.setTable(table);
        domain.setColumn(column);
        domain.setDbType(senCol.getColMeta().getColumnType());
        domain.setIndex(index);

        String value = senCol.getColValue();
        domain.setValue(value == null ? "" : value);
        domain.setItIsNull(value == null);

        domain.setEnvId(conf.getEnvId());
        domain.setEnvName(conf.getEnvName());
        domain.setDsId(conf.getDsId());
        domain.setDsName(conf.getDsName());
        domain.setDsType(conf.getDsType());
        Map<String, String> variables = query.getVariables();
        domain.setUserName(variables.get(ExecuteVariables.USER_NAME));
        domain.setUserRole(variables.get(ExecuteVariables.ROLE_NAME));

        CheckerData checkerData = new CheckerData(query.getQueryBody(), domain);
    checkerData.setDsLevelsDef(typesList);
        checkerData.setCurrentCatalog(catalog); // default, domain.catalog is first
        checkerData.setCurrentSchema(schema); // default, domain.schema is first
        checkerData.setStartLine(1);
        checkerData.setStartColumn(0);

        return checkerData;
    }

    private static final int FIX_LENGTH = "algorithm::".length();

    private String doAlgorithm(String algorithm, String cv) {
        String str = algorithm.substring(FIX_LENGTH);

        if (StringUtils.startsWith(str, "ORIGINAL")) {
            return SenAlgorithm.doOriginalAlgorithm(str, cv);
        } else if (StringUtils.equalsIgnoreCase(str, "FULL_MASK")) {
            return SenAlgorithm.doFullMaskAlgorithm(str, cv);
        } else {
            return cv;
        }
    }
}
