package com.clougence.clouddm.ds.maxcompute.execute;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import com.clougence.clouddm.ds.maxcompute.definition.ui.editor.table.McEditorProvider;
import com.clougence.clouddm.ds.maxcompute.dialect.McDialect;
import com.clougence.clouddm.ds.maxcompute.dsconf.McConfig;
import com.clougence.clouddm.sdk.execute.session.Session;
import com.clougence.clouddm.sdk.execute.session.rdb.DefaultRdbMetaService;
import com.clougence.clouddm.sdk.execute.session.rdb.DmRdbUmiService;
import com.clougence.schema.editor.provider.SqlBuilder;
import com.clougence.schema.umi.special.rdb.RdbTable;
import com.clougence.schema.umi.special.rdb.RdbView;
import com.clougence.schema.umi.struts.UmiTypes;
import com.clougence.utils.ExceptionUtils;
import com.clougence.utils.StringUtils;
import com.clougence.utils.jdbc.JdbcTemplate;

import lombok.extern.slf4j.Slf4j;

/**
 * @author mode 2021/1/15 17:11
 */
@Slf4j
public class McMetaService extends DefaultRdbMetaService {

    public McMetaService(Session rdbSession){
        super(rdbSession);
    }

    @Override
    protected DmRdbUmiService rdbUmiService(Connection con) {
        return new McUmiServiceDm(con, (McConfig) this.rdbSession.getDsConfig());
    }

    @Override
    protected SqlBuilder getSqlBuilder() { return McEditorProvider.INSTANCE; }

    @Override
    public void testConnect() {
        try {
            int res = this.rdbSession.executeQuery(con -> new JdbcTemplate(con).queryForInt("select 1"));
            if (res != 1) {
                throw new SQLException("Test SQL 'select 1' failed.");
            }
        } catch (Exception e) {
            String msg = "testConnect failed, " + ExceptionUtils.getRootCauseMessage(e);
            log.error(msg, e);
            throw new RuntimeException(msg, e);
        }
    }

    @Override
    public String getCurrentCatalog() { return null; }

    @Override
    public String getCurrentSchema() {
        //        try {
        //            return this.rdbSession.executeQuery(con -> {
        //                String queryString = "select SYS_CONTEXT('USERENV','CURRENT_SCHEMA') CURRENT_SCHEMA from dual";
        //                try (Statement s = con.createStatement(); ResultSet resultSet = s.executeQuery(queryString)) {
        //                    return ((SingleValueRowMapper<String>) (rs, columnType, columnTypeName, columnClassName) -> rs.getString(1)).mapRow(resultSet);
        //                }
        //            });
        //        } catch (Exception e) {
        //            String msg = "getCurrentSchema error.msg:" + ExceptionUtils.getRootCauseMessage(e);
        //            log.error(msg, e);
        //            throw new RuntimeException(msg, e);
        //        }
        return null;
    }

    @Override
    public List<String> requestObjectScript(Map<UmiTypes, Object> levelsParam, UmiTypes leafType, String leafName) {
        try {
            return this.rdbSession.executeQuery(con -> {
                String catalog = StringUtils.toString(levelsParam.get(UmiTypes.Catalog));
                String schema = StringUtils.toString(levelsParam.get(UmiTypes.Schema));
                switch (leafType) {
                    case Table:
                        return showCreateTable(con, catalog, schema, leafName);
                    case View:
                        return showCreateView(con, catalog, schema, leafName);
                    default:
                        throw new UnsupportedOperationException("MaxCompute '" + leafType + "' Unsupported.");
                }
            });
        } catch (Exception e) {
            String msg = "requestObjectScript error.msg:" + ExceptionUtils.getRootCauseMessage(e);
            log.error(msg, e);
            throw new RuntimeException(msg, e);
        }
    }

    protected List<String> showCreateTable(Connection con, String catalog, String schema, String table) throws SQLException {
        String showSql = "show create table " + McDialect.INSTANCE.fmtTableName(true, catalog, schema, table);

        try (Statement s = con.createStatement(); ResultSet rs = s.executeQuery(showSql)) {
            if (rs.next()) {
                return Collections.singletonList(rs.getString("Info"));
            } else {
                return Collections.emptyList();
            }
        }
    }

    protected List<String> showCreateView(Connection con, String catalog, String schema, String table) throws SQLException {
        List<RdbTable> rdbTables = new McMetaProviderDm(con, (McConfig) this.rdbSession.getDsConfig()).loadViews(catalog, schema, Collections.singletonList(table));

        if (!rdbTables.isEmpty()) {
            RdbTable rdbTable = rdbTables.get(0);
            if (rdbTable instanceof RdbView) {
                ArrayList<String> lines = new ArrayList<>();

                lines.add("create view if not exists " + McDialect.INSTANCE.fmtTableName(false, catalog, schema, table));

                StringBuilder strBuilder = new StringBuilder();
                strBuilder.append("(");
                boolean hasAny = false;
                for (String col : rdbTable.getColumns().keySet()) {
                    strBuilder.append(McDialect.INSTANCE.fmtName(false, col));
                    strBuilder.append(", ");
                    hasAny = true;
                }
                if (hasAny) {
                    strBuilder.delete(strBuilder.length() - 2, strBuilder.length());
                }
                strBuilder.append(")");
                lines.add(strBuilder.toString());

                if (StringUtils.isNotBlank(rdbTable.getComment())) {
                    lines.add("comment '" + McDialect.INSTANCE.fmtComment(rdbTable.getComment()) + "'");
                }

                lines.add("AS");
                lines.add(((RdbView) rdbTable).getSql());
                return Collections.singletonList(StringUtils.join(lines.toArray(), "\n"));
            }
        }
        return Collections.emptyList();
    }
}
