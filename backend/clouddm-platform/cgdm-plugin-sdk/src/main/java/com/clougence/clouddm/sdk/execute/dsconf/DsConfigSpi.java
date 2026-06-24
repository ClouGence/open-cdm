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
package com.clougence.clouddm.sdk.execute.dsconf;

import java.util.List;
import java.util.Map;

import com.clougence.clouddm.base.metadata.ds.DataSourceConfig;
import com.clougence.clouddm.base.metadata.ds.DsConfigGroup;
import com.clougence.clouddm.base.metadata.ds.SecurityType;
import com.clougence.clouddm.base.metadata.ui.form.UiPanel;
import com.clougence.clouddm.sdk.Spi;

public interface DsConfigSpi extends Spi {

    String ENV_ID_FIELD                  = "envId";
    String CLUSTER_ID_FIELD              = "clusterId";
    String PORT_FIELD                    = "port";
    String AUTO_COMMIT_FIELD             = "autoCommit";
    String TRANSACTION_CONTROL_FIELD     = "transactionControl";
    String TRANSACTION_MODE_AUTO_VALUE   = "txAuto";
    String TRANSACTION_MODE_MANUAL_VALUE = "txManual";
    String CLIENT_TIME_ZONE_FIELD        = "clientTimeZone";
    String DEFAULT_CATALOG_FIELD         = "defaultCatalog";
    String DEFAULT_SCHEMA_FIELD          = "defaultSchema";
    String SSH_TUNNEL_FIELD              = "sshTunnel";

    Class<? extends DataSourceConfig> newConfig();

    boolean supportSSL();

    boolean supportSSH();

    List<SecurityType> securityTypes();

    void customizeAddPanels(Map<DsConfigGroup, UiPanel> panels);

    DataSourceConfig fillConfig(DataSourceConfig dsConfig, Map<String, String> defaultConfig);
}
