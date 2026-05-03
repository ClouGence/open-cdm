package com.clougence.clouddm.ds.polardb.execute.pormy;

import java.sql.Connection;

import com.clougence.clouddm.dsfamily.mysql.execute.MyMetaProviderDm;
import com.clougence.schema.metadata.MetaDataService;

import lombok.extern.slf4j.Slf4j;

/**
 * PolarDBMy 元信息获取，参考资料：
 * <li>https://dev.mysql.com/doc/refman/8.0/en/information-schema.html</li>
 *
 * @author 赵永春 (zyc@hasor.net)
 * @version : 2020-01-22
 */
@Slf4j
public class PorMyMetaProviderDm extends MyMetaProviderDm implements MetaDataService {

    public PorMyMetaProviderDm(Connection connection){
        super(connection);
        this.providerUtils = new PorMyMetaProviderUtils();
    }
}
