package com.clougence.clouddm.ds.oceanbase.execute.obforora;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.clougence.adapter.ob.obfororacle.ObForOracleTypes;
import com.clougence.schema.umi.special.rdb.RdbColumn;
import com.clougence.schema.umi.special.rdb.RdbTable;
import com.clougence.schema.umi.special.rdb.RdbView;
import com.clougence.schema.umi.struts.UmiTypes;
import com.clougence.schema.umi.struts.constraint.NonNull;
import com.clougence.utils.StringUtils;

public class ObForOracleMetaProviderUtils {

    public static List<RdbTable> convertTable(ResultSet rs) throws SQLException {
        List<RdbTable> result = new ArrayList<>();
        while (rs.next()) {
            String tableType = rs.getString("TABLE_TYPE");
            RdbTable tab;
            if (StringUtils.equalsIgnoreCase(tableType, "VIEW")) {
                RdbView rdbView = new RdbView();
                rdbView.setSql(rs.getString("SQL"));
                tab = rdbView;
                tab.setUmiType(UmiTypes.View);
            } else if (StringUtils.equalsIgnoreCase(tableType, "TABLE")) {
                tab = new RdbTable();
                tab.setUmiType(UmiTypes.Table);
                tab.setComment(rs.getString("COMMENTS"));
            } else {
                tab = new RdbTable();
                tab.setUmiType(null);
            }
            tab.setSchema(rs.getString("OWNER"));
            tab.setName(rs.getString("TABLE_NAME"));
            //            tab.setComment(rs.getString("COMMENTS"));
            //
            //            tab.setAttribute(TABLESPACE, rs.getString("TABLESPACE_NAME"));
            //            tab.setAttribute(TEMPORARY, StringUtils.toString("Y".equalsIgnoreCase(rs.getString("TEMPORARY"))));
            //            //tab.setAttribute(READ_ONLY, StringUtils.toString("YES".equalsIgnoreCase(rs.getString("READ_ONLY"))));
            //
            //            String logTable = rs.getString("LOG_TABLE");
            //            if (StringUtils.isNotBlank(logTable)) {
            //                tab.setAttribute(LOG_TABLE, logTable);
            //                tab.setAttribute(LOG_ROWIDS, StringUtils.toString("YES".equalsIgnoreCase(rs.getString("LOG_ROWIDS"))));
            //                tab.setAttribute(LOG_PK, StringUtils.toString("YES".equalsIgnoreCase(rs.getString("LOG_PK"))));
            //                tab.setAttribute(LOG_SEQ, StringUtils.toString("YES".equalsIgnoreCase(rs.getString("LOG_SEQ"))));
            //            }

            result.add(tab);
        }
        return result;
    }

    public static List<RdbColumn> convertColumn(ResultSet resultSet) throws SQLException {
        List<RdbColumn> columns = new ArrayList<>();
        while (resultSet.next()) {
            RdbColumn rdbColumn = new RdbColumn();
            rdbColumn.setTable(resultSet.getString("TABLE_NAME"));
            rdbColumn.setSchema(resultSet.getString("OWNER"));
            rdbColumn.setName(resultSet.getString("COLUMN_NAME"));
            String dataType = resultSet.getString("DATA_TYPE").replaceAll("\\(.*?\\)", "");
            rdbColumn.setSqlType(ObForOracleTypes.toOracleType(dataType, resultSet.getInt("DATA_LENGTH"), resultSet.getInt("DATA_SCALE")));
            rdbColumn.setDatetimePrecision(resultSet.getInt("DATA_LENGTH"));
            rdbColumn.setNumericScale(resultSet.getInt("DATA_SCALE"));
            rdbColumn.setCharLength(resultSet.getLong("CHAR_LENGTH"));
            rdbColumn.setIndex(resultSet.getInt("COLUMN_ID"));

            if (!"Y".equals(resultSet.getString("NULLABLE"))) {
                rdbColumn.addConstraint(new NonNull());
            }
            columns.add(rdbColumn);
        }
        return columns;
    }
}
