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
package com.clougence.rdp.component.dskvconfig.impl;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import com.clougence.clouddm.api.common.boot.UnifiedPostConstruct;
import com.clougence.clouddm.base.metadata.ds.DataSourceType;
import com.clougence.rdp.component.dskvconfig.RdpDsExtraConfGen;
import com.clougence.rdp.component.dskvconfig.RdpDsResourceService;
import com.clougence.rdp.component.dskvconfig.model.RdpDsResource;
import com.clougence.rdp.component.dskvconfig.operate.*;

import jakarta.annotation.Resource;

/**
 * @author wanshao create time is 2021/12/3
 **/
@Component
public class RdpDsResourceServiceImpl implements RdpDsResourceService, UnifiedPostConstruct {

    @Resource
    private ApplicationContext                       appCtx;

    private final Map<DataSourceType, RdpDsResource> dsResourceMap = new HashMap<>();

    private final AtomicBoolean                      inited        = new AtomicBoolean();

    @Override
    public void init() {
        if (this.inited.compareAndSet(false, true)) {

            RdpDsResource elasticSearch = new RdpDsResource(//
                appCtx.getBean(EsExtraConfGen.class)//
            );
            dsResourceMap.put(DataSourceType.ElasticSearch, elasticSearch);

            RdpDsResource hive = new RdpDsResource(//
                appCtx.getBean(HiveExtraConfGen.class) //
            );
            dsResourceMap.put(DataSourceType.Hive, hive);

            RdpDsResource hudi = new RdpDsResource(//
                appCtx.getBean(HudiExtraConfGen.class) //
            );
            dsResourceMap.put(DataSourceType.Hudi, hudi);

            RdpDsResource redis = new RdpDsResource(//
                appCtx.getBean(RedisExtraConfGen.class)//
            );
            dsResourceMap.put(DataSourceType.Redis, redis);
            dsResourceMap.put(DataSourceType.ElastiCache, redis);

            RdpDsResource tunnel = new RdpDsResource(//
                appCtx.getBean(TunnelExtraConfGen.class)//
            );
            dsResourceMap.put(DataSourceType.Tunnel, tunnel);

            RdpDsResource ragApi = new RdpDsResource(//
                appCtx.getBean(RagApiExtraConfGen.class)//
            );
            dsResourceMap.put(DataSourceType.RagApi, ragApi);

            RdpDsResource mongodb = new RdpDsResource(//
                appCtx.getBean(MongoExtraConfGen.class)//
            );
            dsResourceMap.put(DataSourceType.MongoDB, mongodb);
            dsResourceMap.put(DataSourceType.DocumentDB, mongodb);

            RdpDsResource dynamodb = new RdpDsResource(//
                appCtx.getBean(DynamoExtraConfGen.class)//
            );
            dsResourceMap.put(DataSourceType.DynamoDB, dynamodb);

            RdpDsResource starRocks = new RdpDsResource(//
                appCtx.getBean(SrExtraConfGen.class) //
            );
            dsResourceMap.put(DataSourceType.StarRocks, starRocks);

            RdpDsResource doris = new RdpDsResource(//
                appCtx.getBean(DorisExtraConfGen.class)//
            );
            dsResourceMap.put(DataSourceType.Doris, doris);
            dsResourceMap.put(DataSourceType.SelectDB, doris);

            RdpDsResource oceanBase = new RdpDsResource(//
                appCtx.getBean(ObExtraConfGen.class)//
            );
            dsResourceMap.put(DataSourceType.OceanBase, oceanBase);

            RdpDsResource obForOracle = new RdpDsResource(//
                appCtx.getBean(ObForOracleExtraConfGen.class)//
            );
            dsResourceMap.put(DataSourceType.ObForOracle, obForOracle);

            RdpDsResource mySql = new RdpDsResource(//
                appCtx.getBean(MySqlExtraConfGen.class) //
            );
            dsResourceMap.put(DataSourceType.MySQL, mySql);
            dsResourceMap.put(DataSourceType.AuroraMySQL, mySql);
            dsResourceMap.put(DataSourceType.MariaDB, mySql);
            dsResourceMap.put(DataSourceType.Lindorm, mySql);

            RdpDsResource gaussDbMySql = new RdpDsResource(//
                appCtx.getBean(MySqlExtraConfGen.class)//
            );
            dsResourceMap.put(DataSourceType.GaussDBForMySQL, gaussDbMySql);

            RdpDsResource polarDbMySql = new RdpDsResource(//
                null//
            );
            dsResourceMap.put(DataSourceType.PolarDbMySQL, polarDbMySql);

            RdpDsResource oracle = new RdpDsResource(//
                appCtx.getBean(OraExtraConfGen.class) //
            );
            dsResourceMap.put(DataSourceType.Oracle, oracle);

            RdpDsResource pg = new RdpDsResource(//
                appCtx.getBean(PgExtraConfGen.class)//
            );
            dsResourceMap.put(DataSourceType.PostgreSQL, pg);
            dsResourceMap.put(DataSourceType.PolarDBPg, pg);
            dsResourceMap.put(DataSourceType.AuroraPostgreSQL, pg);

            RdpDsResource gaussForOpen = new RdpDsResource(//
                appCtx.getBean(PgExtraConfGen.class) //
            );
            dsResourceMap.put(DataSourceType.GaussDBForOpenGauss, gaussForOpen);

            RdpDsResource gp = new RdpDsResource(//
                appCtx.getBean(PgExtraConfGen.class)//
            );
            dsResourceMap.put(DataSourceType.Greenplum, gp);//

            RdpDsResource duckdb = new RdpDsResource(//
                appCtx.getBean(DuckExtraConfGen.class)//
            );
            dsResourceMap.put(DataSourceType.DuckDB, duckdb);

            RdpDsResource tidb = new RdpDsResource(//
                appCtx.getBean(TiDbExtraConfGen.class) //
            );
            dsResourceMap.put(DataSourceType.TiDB, tidb);

            RdpDsResource polardbx = new RdpDsResource(//
                null//
            );
            dsResourceMap.put(DataSourceType.PolarDbX, polardbx);

            RdpDsResource adbMySql = new RdpDsResource(//
                null //
            );
            dsResourceMap.put(DataSourceType.AdbForMySQL, adbMySql);

            RdpDsResource sqlServer = new RdpDsResource(//
                null//
            );
            dsResourceMap.put(DataSourceType.SQLServer, sqlServer);

            RdpDsResource db2 = new RdpDsResource(//
                null //
            );
            dsResourceMap.put(DataSourceType.Db2, db2);
            dsResourceMap.put(DataSourceType.Db2Fori, db2);

            RdpDsResource clickHouse = new RdpDsResource(//
                appCtx.getBean(CkExtraConfGen.class)//
            );
            dsResourceMap.put(DataSourceType.ClickHouse, clickHouse);

            RdpDsResource kafka = new RdpDsResource(//
                appCtx.getBean(KafkaExtraConfGen.class)//
            );
            dsResourceMap.put(DataSourceType.Kafka, kafka);
            dsResourceMap.put(DataSourceType.AutoMQ, kafka);
            dsResourceMap.put(DataSourceType.AmazonMSK, kafka);

            RdpDsResource rocketMq = new RdpDsResource(//
                appCtx.getBean(RocketMqExtraConfGen.class)//
            );
            dsResourceMap.put(DataSourceType.RocketMQ, rocketMq);

            RdpDsResource rabbitMq = new RdpDsResource(//
                null //
            );
            dsResourceMap.put(DataSourceType.RabbitMQ, rabbitMq);

            RdpDsResource kudu = new RdpDsResource(//
                null//
            );
            dsResourceMap.put(DataSourceType.Kudu, kudu);

            RdpDsResource dameng = new RdpDsResource(//
                appCtx.getBean(DamengExtraConfGen.class)//
            );
            dsResourceMap.put(DataSourceType.Dameng, dameng);

            RdpDsResource hana = new RdpDsResource(//
                null //
            );
            dsResourceMap.put(DataSourceType.Hana, hana);

            RdpDsResource redshift = new RdpDsResource(//
                null//
            );
            dsResourceMap.put(DataSourceType.Redshift, redshift);

            RdpDsResource iceberg = new RdpDsResource(//
                appCtx.getBean(IcebergExtraConfGen.class) //
            );
            dsResourceMap.put(DataSourceType.Iceberg, iceberg);

            RdpDsResource paimon = new RdpDsResource(//
                appCtx.getBean(PaimonExtraConfGen.class) //
            );
            dsResourceMap.put(DataSourceType.Paimon, paimon);
            dsResourceMap.put(DataSourceType.DataLakeFormation, paimon);

            RdpDsResource deltaLake = new RdpDsResource(//
                appCtx.getBean(DeltaLakeExtraConfGen.class) //
            );
            dsResourceMap.put(DataSourceType.DeltaLake, deltaLake);

            RdpDsResource pulsar = new RdpDsResource(//
                appCtx.getBean(PulsarExtraConfGen.class)//
            );
            dsResourceMap.put(DataSourceType.Pulsar, pulsar);

            RdpDsResource greptimeDB = new RdpDsResource(//
                appCtx.getBean(GreptimeDBExtraConfGen.class)//
            );
            dsResourceMap.put(DataSourceType.GreptimeDB, greptimeDB);

            RdpDsResource tdsqlcmysql = new RdpDsResource(//
                null//
            );
            dsResourceMap.put(DataSourceType.TdsqlCMySQL, tdsqlcmysql);

            RdpDsResource tdsqlmysql = new RdpDsResource(//
                null//
            );
            dsResourceMap.put(DataSourceType.TdsqlMySQL, tdsqlmysql);

            RdpDsResource sshFile = new RdpDsResource(//
                appCtx.getBean(SshFileExtraConfGen.class)//
            );
            dsResourceMap.put(DataSourceType.SshFile, sshFile);

            RdpDsResource s3File = new RdpDsResource(//
                appCtx.getBean(S3FileExtraConfGen.class)//
            );
            dsResourceMap.put(DataSourceType.S3File, s3File);

            RdpDsResource ossFile = new RdpDsResource(//
                appCtx.getBean(OssFileExtraConfGen.class)//
            );
            dsResourceMap.put(DataSourceType.OssFile, ossFile);

            RdpDsResource googleDrive = new RdpDsResource(//
                appCtx.getBean(GoogleDriveExtraConfGen.class)//
            );
            dsResourceMap.put(DataSourceType.GoogleDrive, googleDrive);

            RdpDsResource yuque = new RdpDsResource(//
                appCtx.getBean(YuqueExtraConfGen.class)//
            );
            dsResourceMap.put(DataSourceType.Yuque, yuque);

            RdpDsResource openai = new RdpDsResource(//
                appCtx.getBean(OpenAIExtraConfGen.class)//
            );
            dsResourceMap.put(DataSourceType.OpenAI, openai);

            RdpDsResource dashscope = new RdpDsResource(//
                appCtx.getBean(DashScopeAIExtraConfGen.class)//
            );
            dsResourceMap.put(DataSourceType.DashScope, dashscope);

            RdpDsResource huggingFace = new RdpDsResource(//
                appCtx.getBean(HuggingFaceAIExtraConfGen.class)//
            );
            dsResourceMap.put(DataSourceType.HuggingFace, huggingFace);

            RdpDsResource cohere = new RdpDsResource(//
                appCtx.getBean(CohereFaceAIExtraConfGen.class)//
            );
            dsResourceMap.put(DataSourceType.Cohere, cohere);

            RdpDsResource deepSeek = new RdpDsResource(//
                appCtx.getBean(DeepSeekFaceAIExtraConfGen.class)//
            );
            dsResourceMap.put(DataSourceType.DeepSeek, deepSeek);

            RdpDsResource localAi = new RdpDsResource(//
                appCtx.getBean(LocalAIFaceAIExtraConfGen.class)//
            );
            dsResourceMap.put(DataSourceType.LocalAI, localAi);

            RdpDsResource ollama = new RdpDsResource(//
                appCtx.getBean(OllamaExtraConfGen.class)//
            );
            dsResourceMap.put(DataSourceType.Ollama, ollama);

            RdpDsResource zhipuAi = new RdpDsResource(//
                appCtx.getBean(ZhipuAIExtraConfGen.class)//
            );
            dsResourceMap.put(DataSourceType.ZhipuAI, zhipuAi);

            RdpDsResource anthropic = new RdpDsResource(//
                appCtx.getBean(AnthropicExtraConfGen.class)//
            );
            dsResourceMap.put(DataSourceType.Anthropic, anthropic);

            RdpDsResource bedrock = new RdpDsResource(//
                appCtx.getBean(BedrockExtraConfGen.class)//
            );
            dsResourceMap.put(DataSourceType.Bedrock, bedrock);

            RdpDsResource mc = new RdpDsResource(//
                appCtx.getBean(McExtraConfGen.class)//
            );
            dsResourceMap.put(DataSourceType.MaxCompute, mc);

        }
    }

    @Override
    public void stop() {

    }

    @Override
    public RdpDsExtraConfGen getDsExtraConfGen(DataSourceType dsType) {
        if (dsResourceMap.containsKey(dsType)) {
            return dsResourceMap.get(dsType).dsExtraConfigOperate;
        } else {
            return null;
        }
    }
}
