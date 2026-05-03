package com.clougence.clouddm.ds.starrocks.execute;

import java.sql.JDBCType;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

import com.clougence.utils.jdbc.mapper.ValueRowMapper;

@FunctionalInterface
public interface SpecialValueRowMapper<T> extends ValueRowMapper<T> {

    @Override
    public default T mapRow(final ResultSet rs, final int rowNum) throws SQLException {
        ResultSetMetaData rsmd = rs.getMetaData();
        int nrOfColumns = rsmd.getColumnCount();
        if (nrOfColumns != 1) {
            throw new SQLException("Incorrect column count: expected 1, actual " + nrOfColumns);
        }

        int columnType = rs.getMetaData().getColumnType(1);
        String columnTypeName = rs.getMetaData().getColumnTypeName(1);
        String columnClassName = rs.getMetaData().getColumnClassName(1);
        if ("YEAR".equalsIgnoreCase(columnTypeName)) {
            // TODO with mysql `YEAR` type, columnType is DATE. but getDate() throw Long cast Date failed.
            columnType = JDBCType.INTEGER.getVendorTypeNumber();
        }

        if (rs.next()) {
            return getResultSetValue(rs, columnType, columnTypeName, columnClassName);
        } else {
            return null;
        }
    }

}
