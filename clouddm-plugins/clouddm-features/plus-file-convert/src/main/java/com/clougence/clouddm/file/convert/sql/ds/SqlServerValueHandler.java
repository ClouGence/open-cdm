package com.clougence.clouddm.file.convert.sql.ds;

import com.clougence.clouddm.file.convert.sql.SqlRowData;
import com.clougence.utils.io.result.EntityType;

public class SqlServerValueHandler extends DefaultValueHandler {

    public static SqlServerValueHandler HANDLER = new SqlServerValueHandler();

    @Override
    public void handle(SqlRowData row, String data, int i) {
        Byte b = row.getEntityTypes().get(i);
        switch (b) {
            case EntityType.Date:
            case EntityType.Time:
            case EntityType.TimeZ:
            case EntityType.DateTimeZ:
            case EntityType.DateTime:
            case EntityType.String: {
                row.getRowData().set(i, "N" + addQuotation(data));
                break;
            }
            default: {
                super.handle(row, data, i);
            }
        }
    }
}
