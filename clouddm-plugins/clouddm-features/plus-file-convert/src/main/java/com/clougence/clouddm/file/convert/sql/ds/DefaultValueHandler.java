package com.clougence.clouddm.file.convert.sql.ds;

import com.clougence.clouddm.file.convert.sql.SqlRowData;
import com.clougence.utils.io.result.EntityType;

public class DefaultValueHandler implements SqlValueHandler {

    protected String addQuotation(String str) {
        return "'" + str + "'";
    }

    @Override
    public void handle(SqlRowData row, String data, int i) {
        Byte b = row.getEntityTypes().get(i);
        switch (b) {
            case EntityType.Date:
            case EntityType.Time:
            case EntityType.TimeZ:
            case EntityType.DateTimeZ:
            case EntityType.DateTime:
            case EntityType.String:
            case EntityType.Byte:
            case EntityType.Code: {
                row.getRowData().set(i, addQuotation(data));
                break;
            }
            default: {
                row.getRowData().set(i, data);
            }
        }
    }
}
