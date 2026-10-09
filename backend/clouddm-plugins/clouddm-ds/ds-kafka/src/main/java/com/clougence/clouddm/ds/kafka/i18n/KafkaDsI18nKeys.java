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
package com.clougence.clouddm.ds.kafka.i18n;

import com.clougence.utils.i18n.I18nResource;

@I18nResource("/META-INF/clougence/i18n/kafka-plugin")
public interface KafkaDsI18nKeys {

    String PLUGIN_NAME_KAFKA             = "PLUGIN_NAME_KAFKA";

    String KAFKA_AUTH_READ               = "KAFKA_AUTH_READ";
    String KAFKA_AUTH_MANAGE             = "KAFKA_AUTH_MANAGE";
    String KAFKA_SESSION_BUSY            = "KAFKA_SESSION_BUSY";
    String KAFKA_SESSION_CLOSED          = "KAFKA_SESSION_CLOSED";
    String KAFKA_SESSION_READ_ONLY       = "KAFKA_SESSION_READ_ONLY";
    String KAFKA_SESSION_UNSUPPORTED     = "KAFKA_SESSION_UNSUPPORTED";
    String KAFKA_QUERY_CANCELLED         = "KAFKA_QUERY_CANCELLED";
    String KAFKA_METADATA_UNSUPPORTED    = "KAFKA_METADATA_UNSUPPORTED";

    String KAFKA_QUERY_TIMEOUT           = "KAFKA_QUERY_TIMEOUT";
    String KAFKA_RESULT_TRUNCATED        = "KAFKA_RESULT_TRUNCATED";
    String KAFKA_TOPIC_NOT_FOUND         = "KAFKA_TOPIC_NOT_FOUND";
    String KAFKA_TOPIC_DELETE_FAILED     = "KAFKA_TOPIC_DELETE_FAILED";

    String KAFKA_CONSUMER_LIMIT_REQUIRED = "KAFKA_CONSUMER_LIMIT_REQUIRED";
    String KAFKA_CONSUMER_IDLE_TIMEOUT   = "KAFKA_CONSUMER_IDLE_TIMEOUT";
    String KAFKA_CONSUMER_BOUNDS_REWRITE = "KAFKA_CONSUMER_BOUNDS_REWRITE";
    String KAFKA_PARTITION_NOT_FOUND     = "KAFKA_PARTITION_NOT_FOUND";
}
