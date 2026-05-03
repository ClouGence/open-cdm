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
