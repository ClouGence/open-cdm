package com.clougence.clouddm.ds.hologres.execute;

import java.sql.Connection;

import com.clougence.clouddm.dsfamily.postgres.execute.PgMetaProviderDm;
import com.clougence.schema.metadata.MetaDataService;

import lombok.extern.slf4j.Slf4j;

/**
 * Postgres 元信息获取，参考资料：
 * <li>https://www.postgresql.org/docs/13/information-schema.html</li>
 * @version : 2021-04-29
 * @author 赵永春 (zyc@hasor.net)
 */
@Slf4j
public class HgMetaProviderDm extends PgMetaProviderDm implements MetaDataService {

    public HgMetaProviderDm(Connection connection){
        super(connection);
        this.providerUtils = new HgMetaProviderUtils();
    }
}
