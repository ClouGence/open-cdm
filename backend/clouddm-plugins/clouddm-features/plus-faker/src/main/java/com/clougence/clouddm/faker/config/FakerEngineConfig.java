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
package com.clougence.clouddm.faker.config;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ThreadFactory;

import org.apache.ibatis.type.TypeHandlerRegistry;

import com.clougence.clouddm.faker.config.processor.TypeProcessorFactory;
import com.clougence.clouddm.faker.generator.loader.DataLoaderFactory;
import com.clougence.clouddm.faker.types.TypeHandlerRegistryUtils;
import com.clougence.clouddm.faker.utils.RandomRatio;
import com.clougence.clouddm.faker.utils.RatioUtils;
import com.clougence.clouddm.base.metadata.ds.DataSourceType;
import com.clougence.utils.RandomUtils;
import com.clougence.utils.StringUtils;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Tolerate;

/**
 * Faker 全局配置
 *
 * @version : 2022-07-25
 * @author 赵永春 (zyc@hasor.net)
 */
@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
@Builder
public class FakerEngineConfig {

    private final RandomRatio<OpsType> opsRatio;
    private final Set<String>          ignoreErrors;
    // generator
    private ClassLoader                classLoader;
    private TypeHandlerRegistry        typeRegistry;
    private DataLoaderFactory          dataLoaderFactory;
    private TypeProcessorFactory       typeProcessorFactory;
    private String                     policy;
    private DataSourceType             dsType;
    private String                     customTpcConf;
    private boolean                    useQualifier;
    private RandomMode                 randomMode;
    private boolean                    keyChanges;
    private boolean                    printSql;
    private Integer                    pThreadCnt;
    private Integer                    wThreadCnt;

    // one trans
    private int                        minBatchSizePerOps;
    private int                        maxBatchSizePerOps;
    private int                        minOpsCountPerTransaction;
    private int                        maxOpsCountPerTransaction;
    // trans stream
    private boolean                    transaction;
    private int                        minPausePerTransactionMs;
    private int                        maxPausePerTransactionMs;
    // worker
    private ThreadFactory              threadFactory;
    private int                        queueCapacity;
    private int                        writeQps;
    private int                        queryTimeout;
    private boolean                    ignoreAnyErrors;

    @Tolerate
    public FakerEngineConfig(){
        this.classLoader = Thread.currentThread().getContextClassLoader();
        this.typeRegistry = TypeHandlerRegistryUtils.DEFAULT;
        this.dataLoaderFactory = null;
        this.typeProcessorFactory = null;
        this.useQualifier = true;
        this.randomMode = RandomMode.RandomQuery;
        //
        this.minBatchSizePerOps = 2;
        this.maxBatchSizePerOps = 50;
        this.opsRatio = RatioUtils.passerByConfig("INSERT#30;UPDATE#30;DELETE#30");
        this.minOpsCountPerTransaction = 5;
        this.maxOpsCountPerTransaction = 10;
        //
        this.transaction = true;
        this.minPausePerTransactionMs = 0;
        this.maxPausePerTransactionMs = 0;
        //
        this.queueCapacity = 4096;
        this.writeQps = -1;
        this.queryTimeout = -1;
        this.ignoreErrors = new HashSet<>();
        this.ignoreAnyErrors = false;
        //
        this.pThreadCnt = 3;
        this.wThreadCnt = 10;
    }

    public int randomOpsCountPerTrans() {
        return RandomUtils.nextInt(Math.min(1, this.minOpsCountPerTransaction), Math.max(1, this.maxOpsCountPerTransaction));
    }

    public int randomPausePerTransactionMs() {
        return RandomUtils.nextInt(Math.min(1, this.minPausePerTransactionMs), Math.max(1, this.maxPausePerTransactionMs));
    }

    public int randomBatchSizePerOps() {
        return RandomUtils.nextInt(Math.min(1, this.minBatchSizePerOps), Math.max(1, this.maxBatchSizePerOps));
    }

    public OpsType randomOps() {
        return this.opsRatio.getByRandom();
    }

    public boolean ignoreError(Exception e) {
        if (this.ignoreAnyErrors) {
            return true;
        }

        if (this.ignoreErrors.isEmpty()) {
            return false;
        }

        for (String term : this.ignoreErrors) {
            if (StringUtils.containsIgnoreCase(e.getMessage(), term)) {
                return true;
            }
        }
        return false;
    }

    public void addIgnoreError(String keyWords) {
        if (StringUtils.isNotBlank(keyWords)) {
            this.ignoreErrors.add(keyWords);
        }
    }

    public void setOpsRatio(String opsRatio) {
        this.opsRatio.clearRatio();
        RatioUtils.fillByConfig(opsRatio, this.opsRatio);
    }
}
