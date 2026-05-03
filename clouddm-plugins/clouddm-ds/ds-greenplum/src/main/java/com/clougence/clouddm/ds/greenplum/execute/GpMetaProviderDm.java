package com.clougence.clouddm.ds.greenplum.execute;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import com.clougence.clouddm.dsfamily.postgres.execute.PgMetaProviderDm;
import com.clougence.schema.metadata.MetaDataService;
import com.clougence.schema.umi.special.rdb.RdbFunction;
import com.clougence.schema.umi.special.rdb.RdbParam;
import com.clougence.schema.umi.special.rdb.RdbTable;
import com.clougence.schema.umi.struts.UmiTypes;
import com.clougence.schema.umi.struts.Value;

import lombok.extern.slf4j.Slf4j;

/**
 * Postgres 元信息获取，参考资料：
 * <li>https://www.postgresql.org/docs/13/information-schema.html</li>
 *
 * @version : 2021-04-29
 * @author 赵永春 (zyc@hasor.net)
 */
@Slf4j
public class GpMetaProviderDm extends PgMetaProviderDm implements MetaDataService {

    private static final String QUERY_TABLE_DETAIL = "SELECT nc.nspname,\n"//
                                                     + "       C.relhasoids,\n"//
                                                     + "       C.relname,\n"//
                                                     + "       C.relkind,\n"//
                                                     + "       C.relpersistence,\n"//
                                                     + "       CASE\n"//
                                                     + "           WHEN ts.spcname IS NULL THEN 'pg_default'\n"//
                                                     + "           ELSE ts.spcname END                            AS spcname,\n"//
                                                     + "       d.description                                      AS table_comment,\n"//
                                                     + "       STRING_AGG(inh.inhparent :: regclass :: TEXT, ',') AS parent_table,\n"//
                                                     + "       C.reloptions,\n"//
                                                     + "       gdp.policytype,\n"//
                                                     + "       STRING_AGG(a.attname, ',')                         AS distkey_columns,\n"//
                                                     + "       STRING_AGG(pn.nspname || '.' || po.opcname, ',')   AS distclass_opcname\n"//
                                                     + "FROM pg_namespace nc\n"//
                                                     + "         LEFT JOIN pg_class C ON nc.oid = C.relnamespace\n"
                                                     + "         LEFT JOIN pg_tablespace ts ON C.reltablespace = ts.oid\n"
                                                     + "         LEFT JOIN pg_inherits inh ON C.oid = inh.inhrelid\n"
                                                     + "         LEFT JOIN pg_description d ON C.oid = d.objoid AND d.objsubid = 0\n"
                                                     + "         LEFT JOIN gp_distribution_policy gdp on C.oid = gdp.localoid\n"//
                                                     + "         LEFT JOIN LATERAL(\n"//
                                                     + "    SELECT generate_subscripts(gdp.distkey, 1) AS i\n"//
                                                     + "  FROM gp_distribution_policy gdp\n"//
                                                     + "  LEFT JOIN pg_class pc ON pc.oid = gdp.localoid\n"//
                                                     + ") AS tmp ON true\n"//
                                                     + "         LEFT JOIN pg_opclass po ON gdp.distclass[tmp.i] = po.oid\n"
                                                     + "         LEFT JOIN pg_attribute a ON a.attrelid = c.oid AND a.attnum = gdp.distkey[tmp.i]\n"
                                                     + "         LEFT JOIN pg_namespace pn on pn.oid = po.opcnamespace\n"//
                                                     + "WHERE NOT pg_is_other_temp_schema(nc.oid)\n" + "  AND ###CONDITION###\n"//
                                                     + "  AND nc.nspname = ###NSPNAME###\n"//
                                                     + "  AND C.relname IN  ###RELNAME###\n"//
                                                     + "GROUP BY nc.nspname, C.relname, C.relkind, C.relpersistence, ts.spcname, C.relhasoids, C.relfilenode, C.reloptions, d.description, gdp.policytype";

    public GpMetaProviderDm(Connection connection){
        super(connection);
        this.providerUtils = new GpMetaProviderUtils();
    }

    protected List<RdbTable> findByPart(Connection conn, String schema, List<String> tabs, String conditions) throws SQLException {
        String sql = QUERY_TABLE_DETAIL.replace("###NSPNAME###", "?")//
            .replace("###RELNAME###", buildWhereIn(tabs))//
            .replace("###CONDITION###", conditions);

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            List<String> params = new ArrayList<>(tabs);
            params.add(0, schema);
            for (int i = 1; i <= params.size(); i++) {
                ps.setString(i, params.get(i - 1));
            }

            try (ResultSet resultSet = ps.executeQuery()) {
                List<RdbTable> tables = this.convertTable(resultSet, null);
                if (tables == null) {
                    return Collections.emptyList();
                }
                return tables;
            }
        }
    }

    @Override
    public List<Value> selectFunctions(String catalog, String schema) throws SQLException {
        try (Connection conn = this.connectSupplier.eGet()) {

            if (!super.checkIsUseCurrentlyDataBase(conn, catalog)) {
                return Collections.emptyList();
            }

            String sql = "select oid,aa.proname,format_type(aa.prorettype, NULL :: INTEGER ) returnType,format_type(  aa.paramtype, NULL :: INTEGER ) paramType  from (\n"
                         + "    SELECT pn.oid as oid,proname,pn.prorettype,unnest(pn.proargtypes) paramtype  FROM pg_proc AS pn \n"
                         + "    LEFT JOIN pg_namespace AS ns ON pn.pronamespace = ns.oid  WHERE ns.nspname = ? ) aa union all\n"
                         + "SELECT pn.oid as oid,pn.proname as proname,format_type(pn.prorettype, NULL :: INTEGER ) returnType ,null as paramType FROM pg_proc AS pn\n"
                         + " LEFT JOIN pg_namespace AS ns ON pn.pronamespace = ns.oid    WHERE ns.nspname = ? AND proargtypes = ''";
            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setString(1, schema);
                ps.setString(2, schema);
                try (ResultSet rs = ps.executeQuery()) {
                    List<Value> schemas = this.providerUtils.convertFunction(rs);
                    return schemas.stream().filter(value -> value.getUmiType() != null).collect(Collectors.toList());
                }
            }
        }
    }

    @Override
    protected List<RdbFunction> fetchFunctionByPart(Connection conn, String catalog, String schema, List<String> funs) throws SQLException {
        String sql = "select pp.oid oid,proname,prosrc,nspname,format_type(pp.prorettype, NULL :: INTEGER) returnType \n" //
                     + "from pg_proc pp left join pg_namespace pn on  pp.pronamespace = pn.oid \n" //
                     + "where pn.nspname = ? and pp.oid in " + buildWhereIn(funs);
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, schema);
            for (int i = 0; i < funs.size(); i++) {
                ps.setInt(i + 2, Integer.parseInt(funs.get(i)));
            }

            try (ResultSet rs = ps.executeQuery()) {
                return this.convertFunctions(rs);
            }
        }
    }

    @Override
    protected List<RdbParam> fetchParams(Connection conn, UmiTypes types, String schema, List<String> funs) throws SQLException {
        String sql = "select  row_number() over () as ordinal,base.oid as oid,base.proname,format_type(  base.paramtype, NULL :: INTEGER ) as paramType,base.paramName,base.paramMode  from (\n"
                     + "    SELECT pp.oid as oid,proname,unnest(pp.proargtypes) paramtype ,unnest(pp.proargnames) paramName ,unnest(pp.proargmodes) paramMode FROM pg_proc AS pp\n"
                     + "    LEFT JOIN pg_namespace AS ns ON pp.pronamespace = ns.oid\n" + "    WHERE ns.nspname = ? AND pp.oid in " + buildWhereIn(funs) + "              ) base;";

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
