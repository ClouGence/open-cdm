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
package com.clougence.clouddm.console.web.service.kafka;

import java.sql.Connection;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.function.BiFunction;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import jakarta.annotation.Resource;

import org.springframework.stereotype.Service;

import com.clougence.clouddm.api.common.exception.ErrorMessageException;
import com.clougence.clouddm.api.sidecar.session.drivers.DriverRef;
import com.clougence.clouddm.api.sidecar.session.drivers.DriverUtils;
import com.clougence.clouddm.base.metadata.ds.DataSourceConfig;
import com.clougence.clouddm.base.metadata.ds.DataSourceType;
import com.clougence.clouddm.console.web.component.dsconfig.DmDsConfigService;
import com.clougence.clouddm.console.web.global.i18n.DmI18nUtils;
import com.clougence.clouddm.console.web.global.i18n.I18nDmMsgKeys;
import com.clougence.clouddm.console.web.service.envparam.DmEnvParamService;
import com.clougence.clouddm.platform.dal.access.ObjectCacheDao;
import com.clougence.clouddm.platform.dal.access.entry.DsCacheEntry;
import com.clougence.clouddm.platform.plugin.DsPluginInfo;
import com.clougence.clouddm.platform.plugin.PluginManager;
import com.clougence.clouddm.platform.plugin.info.LeasedDsFactory;
import com.clougence.clouddm.sdk.execute.kafka.KafkaOpsSpi;
import com.clougence.clouddm.sdk.model.env.EnvParamKeys;
import com.clougence.drivers.DsObject;
import com.clougence.utils.ExceptionUtils;
import com.clougence.utils.StringUtils;

@Service
public class KafkaOpsService {

    private static final Pattern KAFKA_OPS_I18N_KEY = Pattern.compile("(CONSOLE_KAFKA_OPS_[A-Z0-9_]+)");

    @Resource
    private ObjectCacheDao    objectCacheDao;
    @Resource
    private DmDsConfigService dmDsConfigService;
    @Resource
    private DmEnvParamService dmEnvParamService;

    public void createTopic(String puid, Long dsId, String topicName, int partitions, short replicationFactor, Map<String, String> configs) {
        withConnection(puid, dsId, true, (ops, connection) -> {
            ops.createTopic(connection, topicName, partitions, replicationFactor, configs);
            return null;
        });
    }

    public void deleteTopic(String puid, Long dsId, String topicName) {
        withConnection(puid, dsId, true, (ops, connection) -> {
            ops.deleteTopic(connection, topicName);
            return null;
        });
    }

    public Map<String, String> describeTopicConfigs(String puid, Long dsId, String topicName) {
        return withConnection(puid, dsId, false, (ops, connection) -> ops.describeTopicConfigs(connection, topicName));
    }

    public void alterTopicConfigs(String puid, Long dsId, String topicName, Map<String, String> configs) {
        withConnection(puid, dsId, true, (ops, connection) -> {
            ops.alterTopicConfigs(connection, topicName, configs);
            return null;
        });
    }

    public void alterTopicLayout(String puid, Long dsId, String topicName, int partitions, short replicationFactor) {
        withConnection(puid, dsId, true, (ops, connection) -> {
            ops.alterTopicLayout(connection, topicName, partitions, replicationFactor);
            return null;
        });
    }

    public void deleteConsumerGroup(String puid, Long dsId, String groupId) {
        withConnection(puid, dsId, true, (ops, connection) -> {
            ops.deleteConsumerGroup(connection, groupId);
            return null;
        });
    }

    public void resetConsumerGroupOffsets(String puid, Long dsId, String groupId, String mode, List<String> topics, Long timestampMs, Long offset) {
        withConnection(puid, dsId, true, (ops, connection) -> {
            ops.resetConsumerGroupOffsets(connection, groupId, mode, topics, timestampMs, offset);
            return null;
        });
    }

    private <T> T withConnection(String puid, Long dsId, boolean writable, BiFunction<KafkaOpsSpi, Connection, T> action) {
        DsCacheEntry entry = requireEntry(puid, dsId);
        if (entry.getDsType() != DataSourceType.Kafka) {
            throw new ErrorMessageException(DmI18nUtils.getMessage(I18nDmMsgKeys.CONSOLE_KAFKA_OPS_UNSUPPORTED_ERROR.name()));
        }
        if (writable) {
            assertCanModify(entry);
        }
        KafkaOpsSpi ops = PluginManager.findKafkaOpsSpi(entry.getDsType());
        if (ops == null) {
            throw new ErrorMessageException(DmI18nUtils.getMessage(I18nDmMsgKeys.CONSOLE_KAFKA_OPS_UNSUPPORTED_ERROR.name()));
        }

        DataSourceConfig dsConfig = this.dmDsConfigService.fetchDsConfigFromExists(entry.getDsNumId());
        DriverRef driverRef = DriverUtils.parseDriverRef(dsConfig.getDriverVersion());
        DsPluginInfo pluginInfo = PluginManager.findDsPlugin(entry.getDsType());
        if (pluginInfo == null) {
            throw new ErrorMessageException(DmI18nUtils.getMessage(I18nDmMsgKeys.CONSOLE_KAFKA_OPS_UNSUPPORTED_ERROR.name()));
        }

        try (LeasedDsFactory<?> dsFactory = pluginInfo.createDriver(driverRef.getDriverFamily(), driverRef.getDriverVersion())) {
            Properties properties = dsConfig.asDriverProperties();
            try (DsObject<?> dsObject = dsFactory.create(properties)) {
                Object target = dsObject.getTarget();
                if (!(target instanceof Connection)) {
                    throw new ErrorMessageException(DmI18nUtils.getMessage(I18nDmMsgKeys.CONSOLE_KAFKA_OPS_UNSUPPORTED_ERROR.name()));
                }
                return action.apply(ops, (Connection) target);
            }
        } catch (ErrorMessageException e) {
            throw e;
        } catch (Exception e) {
            throw wrapOpsError(e);
        }
    }

    private static ErrorMessageException wrapOpsError(Exception e) {
        String root = ExceptionUtils.getRootCauseMessage(e);
        if (StringUtils.isNotBlank(root)) {
            Matcher matcher = KAFKA_OPS_I18N_KEY.matcher(root);
            if (matcher.find()) {
                return new ErrorMessageException(DmI18nUtils.getMessage(matcher.group(1)));
            }
        }
        return new ErrorMessageException(e);
    }

    private DsCacheEntry requireEntry(String puid, Long dsId) {
        this.objectCacheDao.ownDataSource(puid, dsId);
        DsCacheEntry entry = this.objectCacheDao.queryByDsId(dsId);
        if (entry == null) {
            throw new ErrorMessageException(DmI18nUtils.getMessage(I18nDmMsgKeys.DS_NOT_EXIST_ERROR.name()));
        }
        return entry;
    }

    private void assertCanModify(DsCacheEntry entry) {
        DataSourceConfig dsConfig = this.dmDsConfigService.fetchDsConfigFromExists(entry.getDsNumId());
        if (Boolean.TRUE.equals(dsConfig.getReadOnly())) {
            throw new ErrorMessageException(DmI18nUtils.getMessage(I18nDmMsgKeys.CONSOLE_KAFKA_OPS_DS_READONLY_ERROR.name()));
        }
        if (entry.getEnvId() != null) {
            String allowAllStatements = this.dmEnvParamService.queryParam(entry.getOwnerUid(), entry.getEnvId(), EnvParamKeys.DM_ALLOW_ALL_STATEMENTS);
            if (StringUtils.equalsIgnoreCase("true", allowAllStatements)) {
                throw new ErrorMessageException(DmI18nUtils.getMessage(I18nDmMsgKeys.CONSOLE_QUERY_ONLY_QUERY_MESSAGE.name()));
            }
        }
    }
}
