package com.clougence.clouddm.file.convert.sql.ds;

import com.clougence.clouddm.file.convert.sql.SqlRowData;
import com.clougence.utils.io.result.EntityType;

public class McValueHandler extends DefaultValueHandler {

    public static final McValueHandler HANDLER = new McValueHandler();

    @Override
    public void handle(SqlRowData row, String data, int i) {
        Byte b = row.getEntityTypes().get(i);
        switch (b) {
            case EntityType.Byte: {
                row.getRowData().set(i, "CAST(" + data + " AS TINYINT)");
                break;
            }
            case EntityType.Short: {
                row.getRowData().set(i, "CAST(" + data + " AS SMALLINT)");
                break;
            }
            case EntityType.Date: {
                row.getRowData().set(i, "DATE " + addQuotation(data));
                break;
            }
            case EntityType.Float: {
                row.getRowData().set(i, "CAST(" + data + " AS FLOAT)");
                break;
            }
            case EntityType.Bytes: {
                row.getRowData().set(i, "UNHEX(" + addQuotation(data) + ")");
                break;
            }
            case EntityType.Time:
            case EntityType.TimeZ:
            case EntityType.DateTimeZ:
            case EntityType.DateTime: {
                row.getRowData().set(i, "DATETIME " + addQuotation(data));
                break;
            }
            default: {
                super.handle(row, data, i);
            }
        }
    }
}
