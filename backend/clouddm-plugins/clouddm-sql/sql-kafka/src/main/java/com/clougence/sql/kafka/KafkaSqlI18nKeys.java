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
package com.clougence.sql.kafka;

import com.clougence.utils.i18n.I18nResource;

@I18nResource("/META-INF/clougence/i18n/sql-kafka")
public interface KafkaSqlI18nKeys {
    String KAFKA_SYNTAX_ERROR       = "KAFKA_SYNTAX_ERROR";
    String KAFKA_PATTERN_INVALID    = "KAFKA_PATTERN_INVALID";
    String KAFKA_OPTION_UNKNOWN     = "KAFKA_OPTION_UNKNOWN";
    String KAFKA_OPTION_DUPLICATE   = "KAFKA_OPTION_DUPLICATE";
    String KAFKA_VALUE_REQUIRED     = "KAFKA_VALUE_REQUIRED";
    String KAFKA_ACTION_REQUIRED    = "KAFKA_ACTION_REQUIRED";
    String KAFKA_OPTION_ACTION      = "KAFKA_OPTION_ACTION";
    String KAFKA_NUMBER_INVALID     = "KAFKA_NUMBER_INVALID";
    String KAFKA_OFFSET_CONFLICT    = "KAFKA_OFFSET_CONFLICT";
    String KAFKA_OFFSET_UNSUPPORTED = "KAFKA_OFFSET_UNSUPPORTED";
    String KAFKA_GROUP_MODE         = "KAFKA_GROUP_MODE";
    String KAFKA_CONFIG_INVALID     = "KAFKA_CONFIG_INVALID";
    String KAFKA_CONFIG_DUPLICATE   = "KAFKA_CONFIG_DUPLICATE";
    String KAFKA_TOPIC_INVALID      = "KAFKA_TOPIC_INVALID";
    String KAFKA_UNFINISHED         = "KAFKA_UNFINISHED";
    String KAFKA_SHELL_UNSUPPORTED  = "KAFKA_SHELL_UNSUPPORTED";
    String KAFKA_ARGS_UNSUPPORTED   = "KAFKA_ARGS_UNSUPPORTED";
    String KAFKA_CONSUMER_DEFAULTS  = "KAFKA_CONSUMER_DEFAULTS";
    String KAFKA_HELP_TOPICS        = "KAFKA_HELP_TOPICS";
    String KAFKA_HELP_GROUPS        = "KAFKA_HELP_GROUPS";
    String KAFKA_HELP_CONSUMER      = "KAFKA_HELP_CONSUMER";
    String KAFKA_HELP_COMMON        = "KAFKA_HELP_COMMON";
}
