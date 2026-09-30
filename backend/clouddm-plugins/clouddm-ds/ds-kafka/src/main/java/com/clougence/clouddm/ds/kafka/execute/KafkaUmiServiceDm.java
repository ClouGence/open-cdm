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
package com.clougence.clouddm.ds.kafka.execute;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import com.clougence.clouddm.ds.kafka.execute.jdbc.KafkaKeys;
import com.clougence.clouddm.dsfamily.execute.AbstractRdbUmiService;
import com.clougence.schema.umi.service.RdbUmiServiceDm;
import com.clougence.schema.umi.struts.UmiTypes;
import com.clougence.schema.umi.struts.Value;
import com.clougence.utils.StringUtils;

public class KafkaUmiServiceDm extends AbstractRdbUmiService<KafkaMetaProviderDm> implements RdbUmiServiceDm {

    public KafkaUmiServiceDm(Connection connection){
        super(() -> new KafkaMetaProviderDm(connection));
    }

    @Override
    public List<Value> listLevels(List<UmiTypes> levels, Map<UmiTypes, Object> levelsParam) throws SQLException {
        if (levels.isEmpty()) {
            return this.metadataSupplier.eGet().selectSchemas();
        }
        throw new UnsupportedOperationException("listLevels[" + StringUtils.join(levels.toArray(), ",") + "] Unsupported.");
    }

    @Override
    public List<Value> listLeaf(Map<UmiTypes, Object> levelsParam, UmiTypes leafType, String pattern) throws SQLException {
        UmiTypes dimension = resolveDimension(levelsParam, leafType);
        switch (dimension) {
            case Topic:
                return this.metadataSupplier.eGet().selectTopics(pattern);
            case ConsumerGroup:
                return this.metadataSupplier.eGet().selectConsumerGroups(pattern);
            case Endpoint:
                return this.metadataSupplier.eGet().selectBrokers(pattern);
            default:
                throw new UnsupportedOperationException("listLeaf of " + dimension + " Unsupported.");
        }
    }

    @Override
    public Value detailLeaf(Map<UmiTypes, Object> levelsParam, UmiTypes leafType, String leafName) throws SQLException {
        UmiTypes dimension = resolveDimension(levelsParam, leafType);
        switch (dimension) {
            case Topic:
                return this.metadataSupplier.eGet().loadTopic(leafName);
            case ConsumerGroup:
                return this.metadataSupplier.eGet().loadConsumerGroup(leafName);
            case Endpoint:
                return this.metadataSupplier.eGet().loadBroker(leafName);
            default:
                throw new UnsupportedOperationException("detailLeaf of " + dimension + " Unsupported.");
        }
    }

    /**
     * Prefer the logical schema dimension (TOPIC/CONSUMER_GROUP/ENDPOINT), so opening ENDPOINT still works
     * even if the UI default leafType is still TOPIC. Endpoint stands for Kafka Broker.
     */
    private static UmiTypes resolveDimension(Map<UmiTypes, Object> levelsParam, UmiTypes leafType) {
        if (levelsParam != null) {
            Object schemaObj = levelsParam.get(UmiTypes.Schema);
            if (schemaObj != null) {
                String schema = String.valueOf(schemaObj);
                if (StringUtils.equalsIgnoreCase(schema, KafkaKeys.SCHEMA_TOPIC)) {
                    return UmiTypes.Topic;
                }
                if (StringUtils.equalsIgnoreCase(schema, KafkaKeys.SCHEMA_CONSUMER_GROUP)) {
                    return UmiTypes.ConsumerGroup;
                }
                if (StringUtils.equalsIgnoreCase(schema, KafkaKeys.SCHEMA_ENDPOINT)) {
                    return UmiTypes.Endpoint;
                }
            }
        }
        if (leafType == null) {
            return UmiTypes.Topic;
        }
        return leafType;
    }
}
