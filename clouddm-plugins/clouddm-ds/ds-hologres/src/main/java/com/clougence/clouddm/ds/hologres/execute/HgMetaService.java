package com.clougence.clouddm.ds.hologres.execute;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Collections;
import java.util.List;

import com.clougence.clouddm.dsfamily.postgres.dialect.PostgreDialect;
import com.clougence.clouddm.dsfamily.postgres.execute.PgMetaService;
import com.clougence.clouddm.sdk.execute.session.Session;
import com.clougence.clouddm.sdk.execute.session.rdb.DmRdbUmiService;

import lombok.extern.slf4j.Slf4j;

/**
 * @author mode 2021/1/15 17:11
 */
@Slf4j
public class HgMetaService extends PgMetaService {

    public HgMetaService(Session rdbSession){
        super(rdbSession);
    }

    @Override
    protected DmRdbUmiService rdbUmiService(Connection con) {
        return new HgPgUmiServiceDm(con);
    }

    protected List<String> showCreateView(Connection con, String catalog, String schema, String view) throws SQLException {
        String showSql = "select pg_get_viewdef('" + PostgreDialect.INSTANCE.fmtTableName(true, catalog, schema, view) + "'::regclass, true)";

        try (Statement s = con.createStatement(); ResultSet rs = s.executeQuery(showSql)) {
            if (rs.next()) {
                return Collections.singletonList("create view " + view + "\n as \n" + rs.getString(1));
            } else {
                return Collections.emptyList();
            }
        }
    }
}
