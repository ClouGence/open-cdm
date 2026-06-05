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
package com.clougence.clouddm.sdk.execute.session.rdb;

import java.util.List;

import com.clougence.clouddm.base.metadata.ds.DataSourceConfig;
import com.clougence.clouddm.sdk.Spi;

/**
 * @author mode 2022/2/21 13:13:09
 */
public interface RdbSupportSpi extends Spi {

    String HINT_FOR_CHANGE_CATALOG     = "SUPPORT_LEVEL_HINT_FOR_CHANGE_CATALOG";
    String HINT_FOR_CHANGE_SCHEMA      = "SUPPORT_LEVEL_HINT_FOR_CHANGE_SCHEMA";
    String HINT_FOR_CHANGE_ISOLATION   = "SUPPORT_LEVEL_HINT_FOR_CHANGE_ISOLATION";
    String HINT_FOR_CHANGE_AUTO_COMMIT = "SUPPORT_LEVEL_HINT_FOR_CHANGE_AUTO_COMMIT";
    String HINT_FOR_CHANGE_READONLY    = "SUPPORT_LEVEL_HINT_FOR_CHANGE_READONLY";
    String HINT_FOR_CANCEL_QUERY       = "SUPPORT_LEVEL_HINT_FOR_CANCEL_QUERY";
    String HINT_FOR_EXPLAIN_QUERY      = "SUPPORT_LEVEL_HINT_FOR_EXPLAIN_QUERY";
    String HINT_FOR_FORMAT_QUERY       = "SUPPORT_LEVEL_HINT_FOR_FORMAT_QUERY";
    String HINT_FOR_ARGS_QUERY         = "SUPPORT_LEVEL_HINT_FOR_ARGS_QUERY";

    /** when RdbSupportLevel is Hint, need i18n : SUPPORT_LEVEL_HINT_FOR_CHANGE_CATALOG */
    RdbSupportLevel supportChangeCatalog(DataSourceConfig dsConfig);

    /** when RdbSupportLevel is Hint, need i18n : SUPPORT_LEVEL_HINT_FOR_CHANGE_SCHEMA */
    RdbSupportLevel supportChangeSchema(DataSourceConfig dsConfig);

    /** when RdbSupportLevel is Hint, need i18n : SUPPORT_LEVEL_HINT_FOR_CHANGE_ISOLATION */
    RdbSupportLevel supportChangeIsolation(DataSourceConfig dsConfig);

    /** when RdbSupportLevel is Hint, need i18n : SUPPORT_LEVEL_HINT_FOR_CHANGE_AUTO_COMMIT */
    RdbSupportLevel supportChangeAutoCommit(DataSourceConfig dsConfig);

    /** when RdbSupportLevel is Hint, need i18n : SUPPORT_LEVEL_HINT_FOR_CHANGE_READONLY */
    RdbSupportLevel supportChangeReadOnly(DataSourceConfig dsConfig);

    /** when RdbSupportLevel is Hint, need i18n : SUPPORT_LEVEL_HINT_FOR_CANCEL_QUERY */
    RdbSupportLevel supportCancelQuery(DataSourceConfig dsConfig);

    /** when RdbSupportLevel is Hint, need i18n : SUPPORT_LEVEL_HINT_FOR_EXPLAIN_QUERY */
    RdbSupportLevel supportExplain(DataSourceConfig dsConfig);

    /** when RdbSupportLevel is Hint, need i18n : SUPPORT_LEVEL_HINT_FOR_FORMAT_QUERY */
    RdbSupportLevel supportFormat(DataSourceConfig dsConfig);

    /** when RdbSupportLevel is Hint, need i18n : SUPPORT_LEVEL_HINT_FOR_ARGS_QUERY */
    RdbSupportLevel supportArgs(DataSourceConfig dsConfig);

    List<RdbIsolation> supportIsolation();

    boolean supportMultiStatement(boolean isDesktop);
}
