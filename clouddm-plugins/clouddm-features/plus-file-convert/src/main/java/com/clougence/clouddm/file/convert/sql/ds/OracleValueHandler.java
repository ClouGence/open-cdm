package com.clougence.clouddm.file.convert.sql.ds;

import com.clougence.clouddm.file.convert.sql.SqlRowData;
import com.clougence.utils.io.result.EntityType;

public class OracleValueHandler extends DefaultValueHandler {

    public static final OracleValueHandler HANDLER = new OracleValueHandler();

    @Override
    public void handle(SqlRowData row, String data, int i) {
        Byte b = row.getEntityTypes().get(i);
        switch (b) {
            case EntityType.Date: {
                row.getRowData().set(i, "DATE " + addQuotation(data));
                break;
            }
            case EntityType.Time:
            case EntityType.TimeZ:
            case EntityType.DateTimeZ:
            case EntityType.DateTime: {
                row.getRowData().set(i, "TIMESTAMP " + addQuotation(data));
                break;
            }
            default: {
                super.handle(row, data, i);
            }
        }
    }
}
