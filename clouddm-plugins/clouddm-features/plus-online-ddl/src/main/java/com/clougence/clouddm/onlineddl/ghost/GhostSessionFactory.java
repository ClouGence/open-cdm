package com.clougence.clouddm.onlineddl.ghost;

import com.clougence.clouddm.base.metadata.ds.DataSourceType;
import com.clougence.clouddm.base.metadata.ds.rdb.mysql.MySqlConfig;
import com.clougence.clouddm.sdk.execute.resource.DsResourceManager;
import com.clougence.clouddm.sdk.model.onlineddl.GhostConfig;

/**
 * @author bucketli 2022/6/1 15:23:51
 */
public class GhostSessionFactory /*implements ToolSessionFactoryOld<MySqlConfig>*/ {

    //@Override
    public GhostSessionOld createSession(DsResourceManager rm, MySqlConfig dsConfig, GhostConfig contextDTO) {
        if (dsConfig.getDataSourceType() != DataSourceType.MySQL) {
            throw new IllegalArgumentException("gh-ost only support MySQL DDL.");
        }

        //        ExportSqlExecType execType = contextDTO.getExportSqlExecType();
        //        if (execType != ExportSqlExecType.GH_OST) {
        //            throw new IllegalArgumentException("execType is not GH_OST.");
        //        }

        GhostSessionOld session = new GhostSessionOld(dsConfig, contextDTO);
        session.init();
        return session;
    }
}
