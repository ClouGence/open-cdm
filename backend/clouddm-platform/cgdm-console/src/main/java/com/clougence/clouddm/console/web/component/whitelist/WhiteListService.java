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
package com.clougence.clouddm.console.web.component.whitelist;

import com.clougence.clouddm.base.metadata.ds.DataSourceType;

public interface WhiteListService {

    // menu need SecRoleAuthLabel.DM_QUERY_CONSOLE
    boolean checkMenuQuery(String menuId);

    // menu need SecRoleAuthLabel.DM_OBJECT_MANAGER
    boolean checkMenuManager(String menuId);

    // menu need SecRoleAuthLabel.DM_DS_MAINTENANCE
    boolean checkMenuMaintenance(String menuId);

    boolean checkDs(DataSourceType dsType);

    boolean checkChangeCatalog(DataSourceType dsType);

    boolean checkChangeSchema(DataSourceType dsType);

    boolean checkChangeIsolation(DataSourceType dsType);

    boolean checkChangeAutoCommit(DataSourceType dsType);

    boolean checkChangeReadOnly(DataSourceType dsType);

    boolean checkCancelQuery(DataSourceType dsType);

    boolean checkExplain(DataSourceType dsType);

    boolean checkFormat(DataSourceType dsType);

    boolean checkArgs(DataSourceType dsType);

    boolean checkUserConfigNumber(String configKey, String configValue);
}
