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
package com.clougence.clouddm.dsfamily.postgres.execute;

import java.sql.*;
import java.util.*;
import java.util.stream.Collectors;

import com.clougence.adapter.postgre.PostgresMainVersion;
import com.clougence.clouddm.dsfamily.execute.AbstractMetadataProvider;
import com.clougence.schema.metadata.MainVersion;
import com.clougence.schema.metadata.MetaDataService;
import com.clougence.schema.umi.special.rdb.*;
import com.clougence.schema.umi.struts.UmiConstraint;
import com.clougence.schema.umi.struts.UmiTypes;
import com.clougence.schema.umi.struts.Value;
import com.clougence.utils.CollectionUtils;
import com.clougence.utils.StringUtils;
import com.clougence.utils.convert.ConverterUtils;
import com.clougence.utils.jdbc.mapper.SingleValueRowMapper;
import com.clougence.utils.jdbc.mapper.ValueRowMapper;

import lombok.extern.slf4j.Slf4j;

/**
 * Postgres 元信息获取，参考资料：
 * <li>https://www.postgresql.org/docs/13/information-schema.html</li>
 *
 * @version : 2021-04-29
 * @author 赵永春 (zyc@hasor.net)
 */
@Slf4j
public class PgMetaProviderDm extends AbstractMetadataProvider implements MetaDataService {

    private Long                      serverVersionNumber;

    private static final String       QUERY_TABLE_DETAIL   = "SELECT   nc.nspname, ###SPECIALCOLUMN### C.relname,  C.relkind,  C.relpersistence,  CASE\n"
                                                             + "        WHEN ts.spcname IS NULL THEN 'pg_default'    ELSE ts.spcname    END AS spcname,\n"
                                                             + "    d.description AS table_comment,\n"
                                                             + "    STRING_AGG(inh.inhparent::regclass::text, ',') AS parent_table,   C.reloptions FROM\n"
                                                             + "    pg_namespace nc LEFT JOIN pg_class C ON nc.oid = C.relnamespace\n"
                                                             + "LEFT JOIN pg_tablespace ts ON C.reltablespace = ts.oid LEFT JOIN pg_inherits inh ON C.oid = inh.inhrelid LEFT JOIN pg_description d ON C.oid = d.objoid AND d.objsubid=0\n"
                                                             + "WHERE   NOT pg_is_other_temp_schema(nc.oid)   AND ###CONDITION###\n"
                                                             + "    AND nc.nspname = ###NSPNAME###    AND C.relname  IN ###RELNAME###  GROUP BY    nc.nspname,\n"
                                                             + "    C.relname,    C.relkind,   C.relpersistence,   ts.spcname,  ###SPECIALCOLUMN###  C.relfilenode, C.reloptions,  d.description;";

    private static final String       QUERY_VIEW_DETAIL    = "SELECT   nc.nspname, ###SPECIALCOLUMN### C.relname,  C.relkind,  C.relpersistence, V.definition, CASE\n"
                                                             + "        WHEN ts.spcname IS NULL THEN 'pg_default'    ELSE ts.spcname    END AS spcname,\n"
                                                             + "    d.description AS table_comment,\n"
                                                             + "    STRING_AGG(inh.inhparent::regclass::text, ',') AS parent_table,   C.reloptions FROM\n"
                                                             + "    pg_namespace nc LEFT JOIN pg_class C ON nc.oid = C.relnamespace\n"
                                                             + "LEFT JOIN pg_tablespace ts ON C.reltablespace = ts.oid LEFT JOIN pg_inherits inh ON C.oid = inh.inhrelid LEFT JOIN pg_description d ON C.oid = d.objoid AND d.objsubid=0\n"
                                                             + "LEFT JOIN pg_views V ON V.viewname = C.relname\n"
                                                             + "WHERE   NOT pg_is_other_temp_schema(nc.oid)   AND ###CONDITION###\n"
                                                             + "    AND nc.nspname = ###NSPNAME###    AND C.relname  IN ###RELNAME###  GROUP BY    nc.nspname,\n"
                                                             + "    C.relname,    C.relkind,   C.relpersistence,   ts.spcname,  ###SPECIALCOLUMN###  C.relfilenode, C.reloptions,  d.description,V.definition;";
    private static final String       QUERY_VERSION        = "select version()";

    private static final String       COLUMNS_BASIS        = "  SELECT c.* ###RESULT_SET### FROM (  SELECT  n.nspname AS SCHEMA_NAME,\n"
                                                             + "      C.relname AS TABLE_NAME,   A.attname AS COLUMN_NAME,  A.atttypid AS type_oid,\n"
                                                             + "      A.atttypmod AS type_mod,  CASE      WHEN T.typtype = 'd' THEN\n"
                                                             + "         format_type ( T.typbasetype, NULL :: INTEGER ) ELSE format_type ( A.atttypid, NULL :: INTEGER ) \n"
                                                             + "      END AS type_name,   ( T.typelem <> 0 :: oid AND T.typlen = '-1' :: INTEGER ) AS type_is_array,\n"
                                                             + "         T.typbasetype,    T.typtype,   A.attnotnull \n"
                                                             + "         OR ( T.typtype = 'd' AND T.typnotnull ) AS not_null,A.attndims, A.attlen,\n"
                                                             + "         information_schema._pg_char_max_length ( information_schema._pg_truetypid ( A.*, T.* ), information_schema._pg_truetypmod ( A.*, T.* ) ) AS character_maximum_length,\n"
                                                             + "         information_schema._pg_char_octet_length ( information_schema._pg_truetypid ( A.*, T.* ), information_schema._pg_truetypmod ( A.*, T.* ) ) AS character_octet_length,\n"
                                                             + "         information_schema._pg_numeric_precision ( information_schema._pg_truetypid ( A.*, T.* ), information_schema._pg_truetypmod ( A.*, T.* ) ) AS numeric_precision,\n"
                                                             + "         information_schema._pg_numeric_precision_radix ( information_schema._pg_truetypid ( A.*, T.* ), information_schema._pg_truetypmod ( A.*, T.* ) ) AS numeric_precision_radix,\n"
                                                             + "         information_schema._pg_numeric_scale ( information_schema._pg_truetypid ( A.*, T.* ), information_schema._pg_truetypmod ( A.*, T.* ) ) AS numeric_scale,\n"
                                                             + "         information_schema._pg_datetime_precision ( information_schema._pg_truetypid ( A.*, T.* ), information_schema._pg_truetypmod ( A.*, T.* ) ) AS datetime_precision,\n"
                                                             + "         T.typtypmod,    ROW_NUMBER ( ) OVER ( PARTITION BY A.attrelid ORDER BY A.attnum ) AS attnum,\n"
                                                             + "         dsc.description AS comments,   sch.column_default AS column_default,\n"
                                                             + "         sch.identity_generation,   sch.identity_increment,   sch.identity_minimum,\n"
                                                             + "         sch.identity_maximum,     sch.identity_start,     sch.identity_cycle,\n"
                                                             + "         sch.generation_expression, co.collname AS COLLATION_NAME,\n"
                                                             + "         nc.nspname AS collation_schema_name     FROM    pg_catalog.pg_namespace n\n"
                                                             + "         JOIN pg_catalog.pg_class C ON ( C.relnamespace = n.oid )\n"
                                                             + "         JOIN pg_catalog.pg_attribute A ON ( A.attrelid = C.oid )\n"
                                                             + "         LEFT JOIN pg_catalog.pg_type T ON ( A.atttypid = T.oid )\n"
                                                             + "         LEFT JOIN pg_catalog.pg_attrdef def ON ( A.attrelid = def.adrelid AND A.attnum = def.adnum )\n"
                                                             + "         LEFT JOIN pg_catalog.pg_description dsc ON ( C.oid = dsc.objoid AND A.attnum = dsc.objsubid )\n"
                                                             + "         LEFT JOIN pg_catalog.pg_class dc ON ( dc.oid = dsc.classoid AND dc.relname = 'pg_class' )\n"
                                                             + "         LEFT JOIN pg_catalog.pg_namespace dn ON ( dc.relnamespace = dn.oid AND dn.nspname = 'pg_catalog' )\n"
                                                             + "         LEFT JOIN information_schema.COLUMNS sch ON ( sch.TABLE_NAME = C.relname AND sch.COLUMN_NAME = A.attname AND sch.table_schema = n.nspname ) "
                                                             + "         LEFT JOIN pg_catalog.pg_collation co ON ( A.attcollation = co.oid )\n"
                                                             + "         LEFT JOIN pg_catalog.pg_namespace nc ON ( co.collnamespace = nc.oid )   WHERE\n"
                                                             + "         C.relkind IN ( 'r', 'p', 'v', 'f', 'm' )     AND A.attnum > 0 \n"
                                                             + "         AND NOT A.attisdropped    AND n.nspname =  ###SCHEMA_NAME### AND C.relname IN ###TABLE_NAME### "
                                                             + "      ) C ###JOIN_TABLE###   WHERE   TRUE   ORDER BY     SCHEMA_NAME,   C.TABLE_NAME,    attnum;";
    private static final String       COLUMNS_VERSION_GT10 = COLUMNS_BASIS.replace("###RESULT_SET###", ",s.cache_size")
        .replace("###JOIN_TABLE###", " LEFT JOIN pg_catalog.pg_sequences S ON (S.schemaname = C.SCHEMA_NAME AND S.sequencename = (C.TABLE_NAME || '_' || C.COLUMN_NAME || '_seq'))\n");

    private static final String       COLUMNS_VERSION_LE10 = COLUMNS_BASIS.replace("###RESULT_SET###", "").replace("###JOIN_TABLE###", "");

    private static final String       CONST_PUI            = "SELECT tmp.table_schema, tmp.TABLE_NAME, tmp.isunique, tmp.isprimary, tmp.index_name, tmp.TYPE, tmp.attnum,\n"
                                                             + "\tTRIM ( BOTH '\\\"' FROM pg_catalog.pg_get_indexdef ( tmp.ci_oid, tmp.attnum, FALSE ) ) AS COLUMN_NAME, CASE\n"
                                                             + " WHEN tmp.am_name = 'btree' THEN CASE "
                                                             + "\t\t\tWHEN tmp.i_indoption [ tmp.attnum - 1 ] & 1 = 1 THEN 'D' ELSE 'A'  END ELSE NULL \n"
                                                             + "\tEND AS asc_or_desc, CASE WHEN tmp.am_name = 'btree' THEN CASE "
                                                             + "\t\t\tWHEN tmp.i_indoption [ tmp.attnum - 1 ] & 2 = 2 THEN 'NULLS FIRST' ELSE 'NULLS LAST'  END ELSE NULL \n"
                                                             + "\tEND AS nullsFirst, tmp.am_name, tmp.DEFERRABLE, tmp.INITIALLY, tmp.COLLATION_NAME,\n"
                                                             + "\ttmp.collation_schema_name, tmp.opcname, tmp.opcname_schema_name,\n"
                                                             + "\tpd.description AS description,tmp.attcollation,tmp.reloptions,   tmp.tablespace,  tmp.predicate  FROM ( SELECT n.nspname AS table_schema,\n"
                                                             + "\t\tct.relname AS TABLE_NAME, i.indisunique AS isunique, i.indisprimary AS isprimary,\n"
                                                             + "\t\tci.relname AS index_name, A.attnum, CASE  WHEN i.indisclustered = TRUE THEN 1 ELSE CASE "
                                                             + "\t\t\t\tWHEN am.amname = 'hash' THEN 2 ELSE 3 END END AS TYPE,\n"
                                                             + "\t\t\tci.oid AS ci_oid, i.indoption AS i_indoption, am.amname AS am_name,\n"
                                                             + "\t\t\tcon.condeferrable AS DEFERRABLE, CASE WHEN con.condeferred = FALSE THEN\n"
                                                             + "\t\t\t\t'INITIALLY IMMEDIATE' ELSE 'INITIALLY DEFERRED' END AS INITIALLY, co.collname AS COLLATION_NAME,\n"
                                                             + "\t\t\tnc.nspname AS collation_schema_name, po.opcname, np.nspname AS opcname_schema_name,a.attcollation,ci.reloptions,pt.spcname        as tablespace,  pg_get_expr(i.indpred, i.indrelid) AS predicate\n"
                                                             + "\t\tFROM pg_catalog.pg_class ct LEFT JOIN pg_catalog.pg_namespace n ON ( ct.relnamespace = n.oid )\n"
                                                             + "\t\t\tLEFT JOIN pg_catalog.pg_index i ON ( ct.oid = i.indrelid )\n"
                                                             + "\t\t\tLEFT JOIN pg_catalog.pg_class ci ON ( ci.oid = i.indexrelid )\n"
                                                             + "\t\t\tleft join pg_catalog.pg_tablespace pt on (pt.oid = ci.reltablespace)\n"
                                                             + "\t\t\tLEFT JOIN pg_catalog.pg_am am ON ( ci.relam = am.oid )\n"
                                                             + "\t\t\tLEFT JOIN pg_catalog.pg_constraint con ON ( con.conname = ci.relname AND con.connamespace = n.oid )\n"
                                                             + "\t\t\tLEFT JOIN pg_catalog.pg_attribute A ON ( A.attrelid = ci.oid )\n"
                                                             + "\t\t\tLEFT JOIN pg_catalog.pg_collation co ON ( A.attcollation = co.oid )\n"
                                                             + "\t\t\tLEFT JOIN pg_catalog.pg_namespace nc ON ( co.collnamespace = nc.oid )\n"
                                                             + "\t\t\tLEFT JOIN pg_catalog.pg_opclass po ON ( po.oid = i.indclass [ A.attnum - 1 ] )\n"
                                                             + "\t\t\tLEFT JOIN pg_catalog.pg_namespace np ON ( po.opcnamespace = np.oid ) WHERE TRUE \n"
                                                             + "\t\t\tAND n.nspname = ###SCHEMA_NAME### AND ct.relname IN  ###TABLE_NAME### ###PUI### ) AS tmp\n"
                                                             + "\t\tLEFT JOIN pg_catalog.pg_description pd ON ( tmp.ci_oid = pd.objoid )  ORDER BY tmp.isunique,\n"
                                                             + "\t\ttmp.TYPE, tmp.index_name, tmp.attnum";
    private static final String       CONST_PKUK           = CONST_PUI.replace("###PUI###", "and (i.indisprimary or i.indisunique)");
    private static final String       CONST_INDEX          = CONST_PUI.replace("###PUI###", "and (i.indisprimary = false and i.indisunique = false)");
    private static final String       FK                   = "select pkic.relname as pk_name, pkn.nspname as pk_table_schema, pkc.relname as pk_table_name, pka.attname as pk_column_name,\n"                                                                             //
                                                             + "       con.conname  as fk_name, fkn.nspname as fk_table_schema, fkc.relname as fk_table_name, fka.attname as fk_column_name,\n"                                                                           //
                                                             + "       pos.n        as key_seq,\n"                                                                                                                                                                        //
                                                             + "       case con.confmatchtype when 'f' then 'FULL' when 'p' then 'PARTIAL' when 's' then 'NONE' else null end as match_option,\n"                                                                         //
                                                             + "       case con.confupdtype   when 'c' then 'CASCADE' when 'n' then 'SET NULL' when 'd' then 'SET DEFAULT' when 'r' then 'RESTRICT' when 'a' then 'NO ACTION' else null end as update_rule,\n"            //
                                                             + "       case con.confdeltype   when 'c' then 'CASCADE' when 'n' then 'SET NULL' when 'd' then 'SET DEFAULT' when 'r' then 'RESTRICT' when 'a' then 'NO ACTION' else null end as delete_rule\n"             //
                                                             + "from pg_catalog.pg_namespace pkn, pg_catalog.pg_class pkc, pg_catalog.pg_attribute pka,\n"                                                                                                                //
                                                             + "     pg_catalog.pg_namespace fkn, pg_catalog.pg_class fkc, pg_catalog.pg_attribute fka,\n"                                                                                                                //
                                                             + "     pg_catalog.pg_constraint con,\n"                                                                                                                                                                     //
                                                             + "     pg_catalog.generate_series(1, 32) pos(n),\n"                                                                                                                                                         //
                                                             + "     pg_catalog.pg_class pkic\n"                                                                                                                                                                          //
                                                             + "where pkn.oid = pkc.relnamespace\n"                                                                                                                                                                       //
                                                             + "  and pkc.oid = pka.attrelid\n"                                                                                                                                                                           //
                                                             + "  and pka.attnum = con.confkey[pos.n]\n"                                                                                                                                                                  //
                                                             + "  and con.confrelid = pkc.oid\n"                                                                                                                                                                          //
                                                             + "  and fkn.oid = fkc.relnamespace\n"                                                                                                                                                                       //
                                                             + "  and fkc.oid = fka.attrelid\n"                                                                                                                                                                           //
                                                             + "  and fka.attnum = con.conkey[pos.n]\n"                                                                                                                                                                   //
                                                             + "  and con.conrelid = fkc.oid\n"                                                                                                                                                                           //
                                                             + "  and con.contype = 'f'\n"                                                                                                                                                                                //
                                                             + "  and (pkic.relkind = 'i' or pkic.relkind = 'i')\n"                                                                                                                                                       //
                                                             + "  and pkic.oid = con.conindid\n"                                                                                                                                                                          //
                                                             + "  and fkn.nspname = ###SCHEMA_NAME### and fkc.relname in ###TABLE_NAME###\n"                                                                                                                              //
                                                             + "order by pkn.nspname, pkc.relname, con.conname, pos.n";

    private static final List<String> HIDE_SCHEMA          = Arrays.asList("pg_toast", "pg_temp_1", "pg_toast_temp_1");

    protected PgMetaProviderUtils     providerUtils        = new PgMetaProviderUtils();

    public PgMetaProviderDm(Connection connection){
        super(connection);
        initUmiTypeMap();
    }

    @Override
    public String getVersion() throws SQLException {
        try (Connection con = this.connectSupplier.eGet(); Statement s = con.createStatement(); ResultSet resultSet = s.executeQuery("show server_version")) {
            return getVersion(con);
        }
    }

    protected String getVersion(Connection con) throws SQLException {
        try (Statement s = con.createStatement(); ResultSet resultSet = s.executeQuery("show server_version")) {
            return ((SingleValueRowMapper<String>) (rs, columnType, columnTypeName, columnClassName) -> rs.getString(1)).mapRow(resultSet);
        }
    }

    private long getServerVersionNumber(Connection conn) throws SQLException {
        if (this.serverVersionNumber == null) {
            String sql = "select current_setting('server_version_num')";
            try (PreparedStatement ps = conn.prepareStatement(sql); ResultSet resultSet = ps.executeQuery()) {
                String serverVersion = ((ValueRowMapper<String>) (rs, columnType, columnTypeName, columnClassName) -> rs.getString(1)).mapRow(resultSet);
                this.serverVersionNumber = (Long) ConverterUtils.convert(serverVersion, Long.class);
            }
        }

        return this.serverVersionNumber;
    }

    public List<Value> selectCatalogs() throws SQLException {
        String sql = "select datname from pg_database where datistemplate = false and datallowconn = true order by datname asc";
        try (Connection conn = this.connectSupplier.eGet(); PreparedStatement ps = conn.prepareStatement(sql)) {
            try (ResultSet rs = ps.executeQuery()) {
                return this.providerUtils.convertCatalog(rs);
            }
        }
    }

    public Value selectCatalog(String catalog) throws SQLException {
        String sql = "select datname from pg_database where datistemplate = false and datallowconn = true and datname = ? limit 1";
        try (Connection conn = this.connectSupplier.eGet();) {
            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setString(1, catalog);
                try (ResultSet rs = ps.executeQuery()) {
                    List<Value> valueList = this.providerUtils.convertCatalog(rs);
                    return CollectionUtils.isEmpty(valueList) ? null : valueList.get(0);
                }
            }
        }
    }

    public List<Value> selectSchemas(String catalog) throws SQLException {
        try (Connection conn = this.connectSupplier.eGet()) {
            String curCatalog = null;
            String queryCurCatalog = "select current_database()";
            try (Statement s = conn.createStatement(); ResultSet rs = s.executeQuery(queryCurCatalog)) {
                if (rs.next()) {
                    curCatalog = rs.getString(1);
                }
            }

            if (!StringUtils.equals(curCatalog, catalog)) {
                return Collections.emptyList();
            }

            String querySchema = "select nspname from pg_namespace order by nspname asc";
            try (PreparedStatement ps = conn.prepareStatement(querySchema); ResultSet rs = ps.executeQuery()) {
                List<Value> schemas = this.providerUtils.convertSchema(rs);
                return schemas.stream().filter(s -> !HIDE_SCHEMA.contains(s.asValue())).collect(Collectors.toList());
            }
        }
    }

    public Value selectSchema(String catalog, String schema) throws SQLException {
        try (Connection conn = this.connectSupplier.eGet()) {
            String curCatalog = null;
            String queryCurCatalog = "select current_database()";
            try (Statement s = conn.createStatement(); ResultSet rs = s.executeQuery(queryCurCatalog)) {
                if (rs.next()) {
                    curCatalog = rs.getString(1);
                }
            }

            if (!StringUtils.equals(curCatalog, catalog)) {
                return null;
            }

            String querySchema = "select nspname from pg_namespace where nspname = ? limit 1";
            try (PreparedStatement ps = conn.prepareStatement(querySchema)) {
                ps.setString(1, schema);
                try (ResultSet rs = ps.executeQuery()) {
                    List<Value> valueList = this.providerUtils.convertSchema(rs);
                    return CollectionUtils.isEmpty(valueList) ? null : valueList.get(0);
                }
            }
        }
    }

    public List<Value> selectTables(String catalog, String schema) throws SQLException {
        return this.selectByConditions(catalog, schema, "c.relkind in ('r','p')");
    }

    public List<Value> selectViews(String catalog, String schema) throws SQLException {
        return this.selectByConditions(catalog, schema, "c.relkind in ('v')");
    }

    public List<Value> selectMaterialized(String catalog, String schema) throws SQLException {
        return this.selectByConditions(catalog, schema, "c.relkind in ('m')");
    }

    private List<Value> selectByConditions(String catalog, String schema, String conditions) throws SQLException {
        try (Connection conn = this.connectSupplier.eGet()) {

            if (!checkIsUseCurrentlyDataBase(conn, catalog)) {
                return Collections.emptyList();
            }

            String sql = "select c.relname as table_name, c.relkind table_type, cast(obj_description(c.relfilenode, 'pg_class') as varchar) as comment\n"                                         //
                         + "from pg_namespace nc join pg_class c on nc.oid = c.relnamespace\n"                                                                                                                               //
                         + "where not pg_is_other_temp_schema(nc.oid) and nc.nspname = ? and " + conditions + " order by c.relname asc";
            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setString(1, schema);
                try (ResultSet rs = ps.executeQuery()) {
                    List<Value> schemas = this.providerUtils.convertTableName(rs);
                    return schemas.stream().filter(value -> value.getUmiType() != null).collect(Collectors.toList());
                }
            }
        }
    }

    /**
     * check currently database in accordance with catalog param
     * @param conn Connection
     * @param catalog database name
     * @return catalog == database
     * @throws SQLException sqlException
     */
    protected boolean checkIsUseCurrentlyDataBase(Connection conn, String catalog) throws SQLException {
        String curCatalog = null;
        String queryCurCatalog = "select current_database()";
        try (Statement s = conn.createStatement(); ResultSet rs = s.executeQuery(queryCurCatalog)) {
            if (rs.next()) {
                curCatalog = rs.getString(1);
            }
        }

        return StringUtils.equals(curCatalog, catalog);
    }

    protected final Map<UmiTypes, String> umiTypeMap = new HashMap<>();

    private void initUmiTypeMap() {
        umiTypeMap.put(UmiTypes.Procedure, "p");
        umiTypeMap.put(UmiTypes.Function, "f");
    }

    protected String procSqlBuilderByType(UmiTypes umiTypes) {
        return "select aa.oid as oid,aa.proname,format_type(aa.prorettype, NULL :: INTEGER ) returnType,format_type(  aa.paramtype, NULL :: INTEGER ) paramType  from (\n"
               + "    SELECT pn.oid as oid,proname,pn.prorettype,unnest(pn.proargtypes) paramtype  FROM pg_proc AS pn\n"
               + "    LEFT JOIN pg_namespace AS ns ON pn.pronamespace = ns.oid\n" + "    WHERE ns.nspname = ? AND pn.prokind = '" + umiTypeMap.get(umiTypes) + "'\n"
               + "              ) aa union all\n"
               + "SELECT pn.oid as oid,pn.proname as proname,format_type(pn.prorettype, NULL :: INTEGER ) returnType ,null as paramType FROM pg_proc AS pn\n"
               + "LEFT JOIN pg_namespace AS ns ON pn.pronamespace = ns.oid    WHERE ns.nspname = ? AND pn.prokind = '" + umiTypeMap.get(umiTypes) + "' AND proargtypes = ''";
    }

    public List<Value> selectProcedures(String catalog, String schema) throws SQLException {
        try (Connection conn = this.connectSupplier.eGet()) {

            if (!checkIsUseCurrentlyDataBase(conn, catalog)) {
                return Collections.emptyList();
            }

            String sql = procSqlBuilderByType(UmiTypes.Procedure);
            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setString(1, schema);
                ps.setString(2, schema);
                try (ResultSet rs = ps.executeQuery()) {
                    List<Value> schemas = this.providerUtils.convertProcedure(rs);
                    return schemas.stream().filter(value -> value.getUmiType() != null).collect(Collectors.toList());
                }
            }
        }
    }

    public List<Value> selectFunctions(String catalog, String schema) throws SQLException {
        try (Connection conn = this.connectSupplier.eGet()) {

            if (!checkIsUseCurrentlyDataBase(conn, catalog)) {
                return Collections.emptyList();
            }

            String sql = procSqlBuilderByType(UmiTypes.Function);
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

    /**
     * contains database level trigger which in pg_event_trigger table and table level trigger which in pg_trigger table.
     * @param catalog database
     * @param schema schema
     * @return trigger list
     * @throws SQLException sqlException
     */
    public List<Value> selectTriggers(String catalog, String schema) throws SQLException {
        try (Connection conn = this.connectSupplier.eGet()) {

            if (!checkIsUseCurrentlyDataBase(conn, catalog)) {
                return Collections.emptyList();
            }

            String sql = "SELECT tr.oid as oid, tgname AS name ,relname FROM pg_class AS cla LEFT JOIN pg_namespace AS ns ON cla.relnamespace = ns.oid "
                         + " RIGHT JOIN pg_trigger AS tr ON tr.tgrelid = cla.oid WHERE ns.nspname = ? AND cla.relpersistence = 'p' AND cla.relkind = 'r' UNION ALL "
                         + " SELECT oid, evtname AS name,null as relname FROM pg_event_trigger  order by relname asc";
            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setString(1, schema);
                try (ResultSet rs = ps.executeQuery()) {
                    List<Value> schemas = this.providerUtils.convertTrigger(rs);
                    return schemas.stream().filter(value -> value.getUmiType() != null).collect(Collectors.toList());
                }
            }
        }
    }

    public List<Value> selectSequence(String catalog, String schema) throws SQLException {
        try (Connection conn = this.connectSupplier.eGet()) {

            if (!checkIsUseCurrentlyDataBase(conn, catalog)) {
                return Collections.emptyList();
            }

            String sql = "SELECT relname FROM pg_class AS cla LEFT JOIN pg_namespace AS ns ON cla.relnamespace = ns.oid "
                         + "WHERE ns.nspname = ? AND cla.relpersistence = 'p' AND cla.relkind = 'S'";
            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setString(1, schema);
                try (ResultSet rs = ps.executeQuery()) {
                    List<Value> schemas = this.providerUtils.convertSequence(rs);
                    return schemas.stream().filter(value -> value.getUmiType() != null).collect(Collectors.toList());
                }
            }
        }
    }

    @Override
    protected List<RdbTable> fetchTableByPart(Connection conn, String catalog, String schema, List<String> tabs) throws SQLException {
        return this.findByPart(conn, schema, tabs, "c.relkind in ('r','t','f','p') ");
    }

    @Override
    protected List<RdbTable> fetchViewByPart(Connection conn, String catalog, String schema, List<String> tabs) throws SQLException {
        return this.findViewByPart(conn, schema, tabs, "c.relkind in ('v')");
    }

    @Override
    protected List<RdbTable> fetchMaterializedByPart(Connection conn, String catalog, String schema, List<String> tabs) throws SQLException {
        return Collections.emptyList();
    }

    protected List<RdbTable> findViewByPart(Connection conn, String schema, List<String> tabs, String conditions) throws SQLException {
        PostgresMainVersion mainVersion = PostgresMainVersion.parserVersion(getVersion(conn));
        String sql = QUERY_VIEW_DETAIL.replace("###NSPNAME###", "?").replace("###RELNAME###", buildWhereIn(tabs)).replace("###CONDITION###", conditions);
        //        if (!PostgresMainVersion.PostgreSQL_12.isGt(mainVersion)) {
        sql = sql.replace("###SPECIALCOLUMN###", "");
        //        } else {
        //            sql = sql.replace("###SPECIALCOLUMN###", "C.relhasoids,"); //"C.ellipsoids," ???
        //        }

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            List<String> params = new ArrayList<>(tabs);
            params.add(0, schema);
            for (int i = 1; i <= params.size(); i++) {
                ps.setString(i, params.get(i - 1));
            }

            try (ResultSet resultSet = ps.executeQuery()) {
                List<RdbTable> tables = this.convertView(resultSet, mainVersion);
                if (tables.isEmpty()) {
                    return Collections.emptyList();
                }

                return tables;
            }
        }
    }

    protected List<RdbTable> findByPart(Connection conn, String schema, List<String> tabs, String conditions) throws SQLException {
        PostgresMainVersion mainVersion = PostgresMainVersion.parserVersion(getVersion(conn));
        String sql = QUERY_TABLE_DETAIL.replace("###NSPNAME###", "?").replace("###RELNAME###", buildWhereIn(tabs)).replace("###CONDITION###", conditions);
        //        if (!PostgresMainVersion.PostgreSQL_12.isGt(mainVersion)) {
        sql = sql.replace("###SPECIALCOLUMN###", "");
        //        } else {
        //            sql = sql.replace("###SPECIALCOLUMN###", "C.relhasoids,"); //"C.ellipsoids," ???
        //        }

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            List<String> params = new ArrayList<>(tabs);
            params.add(0, schema);
            for (int i = 1; i <= params.size(); i++) {
                ps.setString(i, params.get(i - 1));
            }

            try (ResultSet resultSet = ps.executeQuery()) {
                List<RdbTable> tables = this.convertTable(resultSet, mainVersion);
                if (tables.isEmpty()) {
                    return Collections.emptyList();
                }

                return tables;
            }
        }
    }

    @Override
    protected List<RdbTable> fetchSelectObjectByPart(Connection conn, String catalog, String schema, List<String> tabs) throws SQLException {
        return this.findByPart(conn, schema, tabs, " 1=1 ");
    }

    @Override
    protected Map<String, List<RdbColumn>> fetchTableColumns(Connection conn, String catalog, String schema, List<String> tabs) throws SQLException {
        PostgresMainVersion mainVersion = PostgresMainVersion.parserVersion(getVersion(conn));
        Map<String, List<RdbColumn>> result = new LinkedHashMap<>();

        String sql;
        boolean gt10 = false;
        if (mainVersion != null && PostgresMainVersion.PostgreSQL_10.isGt(mainVersion)) {
            sql = COLUMNS_VERSION_LE10.replace("###SCHEMA_NAME###", "?").replace("###TABLE_NAME###", buildWhereIn(tabs));
        } else {
            sql = COLUMNS_VERSION_GT10.replace("###SCHEMA_NAME###", "?").replace("###TABLE_NAME###", buildWhereIn(tabs));
            gt10 = true;
        }

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            List<String> params = new ArrayList<>(tabs);
            params.add(0, schema);
            for (int i = 1; i <= params.size(); i++) {
                ps.setString(i, params.get(i - 1));
            }

            try (ResultSet resultSet = ps.executeQuery()) {
                List<RdbColumn> columns = this.convertColumn(resultSet, getServerVersionNumber(conn), gt10);
                if (columns.isEmpty()) {
                    return Collections.emptyMap();
                }

                for (RdbColumn column : columns) {
                    result.computeIfAbsent(column.getTable(), s -> new ArrayList<>()).add(column);
                }
            }
        }

        return result;
    }

    @Override
    protected Map<String, List<RdbIndex>> fetchIndexes(Connection conn, String catalog, String schema, List<String> tabs) throws SQLException {
        String sql = CONST_INDEX.replace("###SCHEMA_NAME###", "?").replace("###TABLE_NAME###", buildWhereIn(tabs));
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            List<String> params = new ArrayList<>(tabs);
            params.add(0, schema);
            for (int i = 1; i <= params.size(); i++) {
                ps.setString(i, params.get(i - 1));
            }

            try (ResultSet resultSet = ps.executeQuery()) {

                List<RdbIndex> idxesFromDb = this.convertIndex(resultSet);

                Map<String, Map<String, RdbIndex>> indexesMap = new LinkedHashMap<>();
                this.providerUtils.fillIdxMapExt(indexesMap, idxesFromDb);
                return indexesMap.entrySet().stream().collect(Collectors.toMap(Map.Entry::getKey, idx -> new ArrayList<>(idx.getValue().values())));
            }
        }
    }

    @Override
    protected Map<String, List<RdbForeignKey>> fetchForeignKeys(Connection conn, String catalog, String schema, List<String> tabs) throws SQLException {
        String sql = FK.replace("###SCHEMA_NAME###", "?").replace("###TABLE_NAME###", buildWhereIn(tabs));

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            List<String> params = new ArrayList<>(tabs);
            params.add(0, schema);
            for (int i = 1; i <= params.size(); i++) {
                ps.setString(i, params.get(i - 1));
            }

            try (ResultSet resultSet = ps.executeQuery()) {
                List<RdbForeignKey> fkList = this.providerUtils.convertForeignKey(resultSet);
                if (fkList == null) {
                    return Collections.emptyMap();
                }

                Map<String, Map<String, RdbForeignKey>> result = new LinkedHashMap<>();
                for (RdbForeignKey foreignKey : fkList) {
                    Map<String, RdbForeignKey> fkMap = result.computeIfAbsent(foreignKey.getTable(), s -> new LinkedHashMap<>());
                    String fkName = foreignKey.getName();
                    if (fkMap.containsKey(fkName)) {
                        fkMap.get(fkName).getColumnList().addAll(foreignKey.getColumnList());
                        fkMap.get(fkName).getReferenceMapping().putAll(foreignKey.getReferenceMapping());
                    } else {
                        fkMap.put(fkName, foreignKey);
                    }
                }

                return result.entrySet().stream().collect(Collectors.toMap(Map.Entry::getKey, uks -> new ArrayList<>(uks.getValue().values())));
            }
        }
    }

    @Override
    protected Map<String, Map<String, UmiConstraint>> fetchPrimaryUnique(Connection conn, String catalog, String schema, List<String> tabs) throws SQLException {
        String sql = CONST_PKUK.replace("###SCHEMA_NAME###", "?").replace("###TABLE_NAME###", buildWhereIn(tabs));

        Map<String, Map<String, UmiConstraint>> pkUkMap = new LinkedHashMap<>();
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            List<String> params = new ArrayList<>(tabs);
            params.add(0, schema);
            for (int i = 1; i <= params.size(); i++) {
                ps.setString(i, params.get(i - 1));
            }

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    String constName = rs.getString("index_name");
                    boolean isUk = rs.getBoolean("isunique");
                    boolean isPk = rs.getBoolean("isprimary");
                    String table = rs.getString("table_name");
                    Map<String, UmiConstraint> constraints = pkUkMap.computeIfAbsent(table, t -> new LinkedHashMap<>());
                    if (isPk) {
                        this.providerUtils.mapToPkExt(rs, constraints);
                    } else if (isUk) {
                        this.providerUtils.mapToUkExt(rs, constraints);
                    } else {
                        throw new IllegalArgumentException("unsupported type constraint type, name is '" + constName + "'");
                    }
                }
            }
        }

        return pkUkMap;
    }

    public List<RdbFunction> loadFunctions(String catalog, String schema, List<String> functionNames) throws SQLException {
        functionNames = stringArray2List(functionNames);
        if (functionNames.isEmpty()) {
            return Collections.emptyList();
        }
        List<RdbFunction> result = new ArrayList<>();
        try (Connection conn = this.connectSupplier.eGet()) {
            for (List<String> funs : CollectionUtils.splitList(functionNames, defaultGroupSize())) {
                List<RdbFunction> rdbFunctions = this.fetchFunctionByPart(conn, catalog, schema, funs);
                List<RdbParam> rdbParams = this.fetchParams(conn, UmiTypes.Function, schema, funs);
                if (rdbParams != null && !rdbParams.isEmpty()) {
                    result.addAll(rdbFunctions.stream().peek(rdbFunction -> {
                        List<RdbParam> rdbParamList = rdbParams.stream()
                            .sorted(Comparator.comparingInt(RdbParam::getOrdinal))
                            .filter(rdbParam -> rdbParam.getReferenceObject().equals(rdbFunction.getAttribute(RdbAttributeNames.RDB_OBJ_ID)))
                            .collect(Collectors.toList());
                        rdbFunction.setRdbParams(rdbParamList);

                    }).collect(Collectors.toList()));
                }
            }
        }
        return result;
    }

    private List<RdbProcedure> fetchProcedureByPart(Connection conn, String catalog, String schema, List<String> procs) throws SQLException {
        String sql = "select pp.oid oid,proname,prosrc,nspname,format_type(pp.prorettype, NULL :: INTEGER) returnType from pg_proc pp left join pg_namespace pn on  pp.pronamespace = pn.oid  where pn.nspname = ? and pp.prokind = 'p' and pp.oid in "
                     + buildWhereIn(procs);
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, schema);
            for (int i = 0; i < procs.size(); i++) {
                ps.setInt(i + 2, Integer.parseInt(procs.get(i)));
            }

            try (ResultSet rs = ps.executeQuery()) {
                return this.convertProcedures(rs);
            }
        }
    }

    private List<RdbProcedure> convertProcedures(ResultSet rs) throws SQLException {
        return this.providerUtils.convertProcedures(rs);
    }

    public List<RdbProcedure> loadProcedures(String catalog, String schema, List<String> procedureNames) throws SQLException {
        procedureNames = stringArray2List(procedureNames);
        if (procedureNames.isEmpty()) {
            return Collections.emptyList();
        }
        List<RdbProcedure> result = new ArrayList<>();
        try (Connection conn = this.connectSupplier.eGet()) {
            for (List<String> funs : CollectionUtils.splitList(procedureNames, defaultGroupSize())) {
                List<RdbProcedure> rdbProcedures = this.fetchProcedureByPart(conn, catalog, schema, funs);
                List<RdbParam> rdbParams = this.fetchParams(conn, UmiTypes.Procedure, schema, funs);
                if (rdbParams != null && !rdbParams.isEmpty()) {
                    result.addAll(rdbProcedures.stream().peek(rdbProcedure -> {
                        List<RdbParam> rdbParamList = rdbParams.stream()
                            .sorted(Comparator.comparingInt(RdbParam::getOrdinal))
                            .filter(rdbParam -> rdbParam.getReferenceObject().equals(rdbProcedure.getAttribute(RdbAttributeNames.RDB_OBJ_ID)))
                            .collect(Collectors.toList());
                        rdbProcedure.setRdbParams(rdbParamList);

                    }).collect(Collectors.toList()));
                }
            }
        }
        return result;
    }

    protected List<RdbParam> fetchParams(Connection conn, UmiTypes types, String schema, List<String> funs) throws SQLException {
        String sql = "select  row_number() over () as ordinal,base.oid as oid,base.proname,format_type(  base.paramtype, NULL :: INTEGER ) as paramType,base.paramName,base.paramMode  from (\n"
                     + "    SELECT pp.oid as oid,proname,unnest(pp.proargtypes) paramtype ,unnest(pp.proargnames) paramName ,unnest(pp.proargmodes) paramMode FROM pg_proc AS pp\n"
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

    protected List<RdbParam> convertParam(ResultSet rs) throws SQLException {
        return this.providerUtils.convertParams(rs);
    }

    protected List<RdbFunction> fetchFunctionByPart(Connection conn, String catalog, String schema, List<String> funs) throws SQLException {
        String sql = "select pp.oid oid,proname,prosrc,nspname,format_type(pp.prorettype, NULL :: INTEGER) returnType from pg_proc pp left join pg_namespace pn on  pp.pronamespace = pn.oid  where pn.nspname = ? and pp.prokind = 'f' and pp.oid in "
                     + buildWhereIn(funs);
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

    protected List<RdbFunction> convertFunctions(ResultSet rs) throws SQLException {
        return this.providerUtils.convertFunctions(rs);
    }

    protected List<RdbTable> convertTable(ResultSet rs, MainVersion mainVersion) throws SQLException {
        return this.providerUtils.convertTable(rs, mainVersion);
    }

    protected List<RdbTable> convertView(ResultSet rs, MainVersion mainVersion) throws SQLException {
        return this.providerUtils.convertView(rs, mainVersion);
    }

    protected List<RdbColumn> convertColumn(ResultSet rs, long serverVersionNumber, boolean gt10) throws SQLException {
        return this.providerUtils.convertColumn(rs, serverVersionNumber, gt10);
    }

    protected List<RdbIndex> convertIndex(ResultSet rs) throws SQLException {
        return this.providerUtils.convertIndex(rs);
    }

}
