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
package com.clougence.clouddm.console.web.controller.editor.query;

import org.springframework.stereotype.Service;

import com.clougence.clouddm.base.metadata.ds.DataSourceConfig;
import com.clougence.clouddm.console.web.component.whitelist.WhiteListService;
import com.clougence.clouddm.sdk.execute.session.rdb.RdbSupportLevel;
import com.clougence.clouddm.sdk.execute.session.rdb.RdbSupportSpi;

import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class DmSupportSpiWrapper {

    @Resource
    private WhiteListService whiteListService;

    public RdbSupportLevel supportChangeCatalog(RdbSupportSpi spi, DataSourceConfig dsConfig) {
        if (this.whiteListService.checkChangeCatalog(dsConfig.getDataSourceType())) {
            return spi.supportChangeCatalog(dsConfig);
        } else {
            return RdbSupportLevel.No;
        }
    }

    public RdbSupportLevel supportChangeSchema(RdbSupportSpi spi, DataSourceConfig dsConfig) {
        if (this.whiteListService.checkChangeSchema(dsConfig.getDataSourceType())) {
            return spi.supportChangeSchema(dsConfig);
        } else {
            return RdbSupportLevel.No;
        }
    }

    public RdbSupportLevel supportChangeIsolation(RdbSupportSpi spi, DataSourceConfig dsConfig) {
        if (this.whiteListService.checkChangeIsolation(dsConfig.getDataSourceType())) {
            return spi.supportChangeIsolation(dsConfig);
        } else {
            return RdbSupportLevel.No;
        }
    }

    public RdbSupportLevel supportChangeAutoCommit(RdbSupportSpi spi, DataSourceConfig dsConfig) {
        if (this.whiteListService.checkChangeAutoCommit(dsConfig.getDataSourceType())) {
            return spi.supportChangeAutoCommit(dsConfig);
        } else {
            return RdbSupportLevel.No;
        }
    }

    public RdbSupportLevel supportChangeReadOnly(RdbSupportSpi spi, DataSourceConfig dsConfig) {
        if (this.whiteListService.checkChangeReadOnly(dsConfig.getDataSourceType())) {
            return spi.supportChangeReadOnly(dsConfig);
        } else {
            return RdbSupportLevel.No;
        }
    }

    public RdbSupportLevel supportCancelQuery(RdbSupportSpi spi, DataSourceConfig dsConfig) {
        if (this.whiteListService.checkCancelQuery(dsConfig.getDataSourceType())) {
            return spi.supportCancelQuery(dsConfig);
        } else {
            return RdbSupportLevel.No;
        }
    }

    public RdbSupportLevel supportExplain(RdbSupportSpi spi, DataSourceConfig dsConfig) {
        if (this.whiteListService.checkExplain(dsConfig.getDataSourceType())) {
            return spi.supportExplain(dsConfig);
        } else {
            return RdbSupportLevel.No;
        }
    }

    public RdbSupportLevel supportFormat(RdbSupportSpi spi, DataSourceConfig dsConfig) {
        if (this.whiteListService.checkFormat(dsConfig.getDataSourceType())) {
            return spi.supportFormat(dsConfig);
        } else {
            return RdbSupportLevel.No;
        }
    }

    public RdbSupportLevel supportArgs(RdbSupportSpi spi, DataSourceConfig dsConfig) {
        if (this.whiteListService.checkArgs(dsConfig.getDataSourceType())) {
            return spi.supportArgs(dsConfig);
        } else {
            return RdbSupportLevel.No;
        }
    }
}
