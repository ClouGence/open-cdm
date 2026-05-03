package com.clougence.clouddm.file.convert.sql;

import java.util.ArrayList;
import java.util.List;

import com.clougence.clouddm.file.convert.config.sql.SqlOption;

import lombok.Getter;

@Getter
public class SqlRowData {

    private List<String> rowData     = new ArrayList<>();
    private List<Byte>   entityTypes = new ArrayList<>();

    public void addData(String rowData, byte entityType) {
        this.rowData.add(rowData);
        this.entityTypes.add(entityType);
    }

    public String toString(List<SqlOption.ColumnOption> options) {
        StringBuilder sb = new StringBuilder("(");
        for (int i = 0; i < rowData.size(); i++) {
            if (options.get(i).isExport()) {
                sb.append(rowData.get(i)).append(",");
            }
        }
        sb.deleteCharAt(sb.length() - 1);
        sb.append(")");
        return sb.toString();
    }
}
