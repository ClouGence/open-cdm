package com.clougence.clouddm.ds.hologres.execute;

import com.clougence.clouddm.dsfamily.postgres.execute.PgHooks;
import com.clougence.clouddm.sdk.execute.meta.DsMetaService;
import com.clougence.clouddm.sdk.execute.session.Session;

/**
 * https://docs.vmware.com/en/VMware-Greenplum/7/greenplum-database/admin_guide-intro-about_mvcc.html
 * @author mode create time is 2021/1/12
 **/
public class HgHooks extends PgHooks {

    @Override
    public DsMetaService createMetaService(Session session) {
        return new HgMetaService(session);
    }

}
