package com.clougence.clouddm.ds.gauss.execute.gs;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import com.clougence.clouddm.dsfamily.postgres.execute.PgMetaProviderDm;
import com.clougence.schema.metadata.MetaDataService;
import com.clougence.schema.umi.special.rdb.RdbParam;
import com.clougence.schema.umi.struts.UmiTypes;

import lombok.extern.slf4j.Slf4j;

/**
 * Postgres 元信息获取，参考资料：
 * <li>https://www.postgresql.org/docs/13/information-schema.html</li>
 *
 * @version : 2021-04-29
 * @author 赵永春 (zyc@hasor.net)
 */
@Slf4j
public class GsMetaProviderDm extends PgMetaProviderDm implements MetaDataService {

    public GsMetaProviderDm(Connection connection){
        super(connection);
        this.providerUtils = new GsMetaProviderUtils();
    }

    protected List<RdbParam> fetchParams(Connection conn, UmiTypes types, String schema, List<String> funs) throws SQLException {
        String sql = "select  row_number() over () as ordinal,base.oid as oid,base.proname,format_type(  base.paramtype, NULL :: INTEGER ) as paramType,base.paramName,base.paramMode  from (\n"
                     + "    SELECT pp.oid as oid,proname,unnest(pp.proargtypes) paramtype ,unnest(pp.proargnames) paramName ,null as  paramMode FROM pg_proc AS pp\n"
                     + "    LEFT JOIN pg_namespace AS ns ON pp.pronamespace = ns.oid\n" + "    WHERE ns.nspname = ? AND pp.prokind = '" + umiTypeMap.get(types) + "' AND pp.oid in "
                     + buildWhereIn(funs) + "              ) base;";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, schema);
            for (int i = 0; i < funs.size(); i++) {
                ps.setInt(i + 2, Integer.parseInt(funs.get(i)));
            }
            try (ResultSet rs = ps.executeQuery()) {
                return this.convertParam(rs);
            }
        }
    }
}
