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
package com.clougence.rdp.component.dskvconfig.model;

import com.clougence.rdp.constant.DsConfigDef;
import com.clougence.clouddm.console.web.global.i18n.I18nDsConfigMsgKeys;
import com.clougence.clouddm.platform.dal.model.system.KvConfValType;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldNameConstants;

/**
 * @author bucketli 2022/8/10 09:33:48
 */
@Getter
@Setter
@FieldNameConstants
@JsonIgnoreProperties(ignoreUnknown = true)
public class CkExtraConfig extends CommonDsExtraConfig {

    @DsConfigDef(name = "clusterName", descKey = I18nDsConfigMsgKeys.DS_CONFIG_CK_CLUSTER_NAME, readOnly = false)
    private String  clusterName;

    @DsConfigDef(name = "multiReplica", defaultValue = "false", descKey = I18nDsConfigMsgKeys.DS_CONFIG_CK_IS_MULTI_REPLICA, readOnly = false, kvConfWebOp = KvConfValType.BOOLEAN)
    private Boolean multiReplica;

    @DsConfigDef(name = "shardName", defaultValue = "{shard}", descKey = I18nDsConfigMsgKeys.DS_CONFIG_CK_SHARD_NAME, readOnly = false)
    private String  shardName;

    @DsConfigDef(name = "replicaName", defaultValue = "{replica}", descKey = I18nDsConfigMsgKeys.DS_CONFIG_CK_REPLICA_NAME, readOnly = false)
    private String  replicaName;
}
