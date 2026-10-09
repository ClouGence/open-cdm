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
package com.clougence.clouddm.ds.kafka.execute.dsfactory;

import java.time.Duration;
import java.util.Properties;

import com.clougence.clouddm.sdk.model.exception.ThirdPartyApiException;
import com.clougence.drivers.DsConfigKeys;
import com.clougence.drivers.DsFactory;
import com.clougence.drivers.DsObject;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.admin.Admin;
import org.apache.kafka.common.KafkaException;

@Slf4j
public class KafkaDsFactory implements DsFactory<Admin> {

    @Override
    public DsObject<Admin> create(Properties dsConfig) {
        Properties props = new Properties();
        props.putAll(dsConfig);
        for (DsConfigKeys key : DsConfigKeys.values()) {
            props.remove(key.getConfigKey());
        }

        try {
            Admin admin = Admin.create(props);
            return new DsObject<>(dsConfig, admin, this, client -> client.close(Duration.ofSeconds(5)));
        } catch (KafkaException e) {
            String msg = "Create Kafka admin failed, instanceID=" + dsConfig.getProperty(DsConfigKeys.ID.getConfigKey());
            log.error(msg, e);
            throw ThirdPartyApiException.as().with(e);
        }
    }
}
