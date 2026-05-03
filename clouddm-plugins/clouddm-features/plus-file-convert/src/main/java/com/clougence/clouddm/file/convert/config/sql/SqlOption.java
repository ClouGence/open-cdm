package com.clougence.clouddm.file.convert.config.sql;

import java.util.List;

import com.clougence.clouddm.base.metadata.ds.DataSourceType;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SqlOption {

    private long               offset;
    private long               limit;
    private String             tableName;
    private List<ColumnOption> columns;
    private DataSourceType     dataSourceType;

    private boolean            mergeInsert;
    private Long               valueSize;

    @Getter
    @Setter
    public static class ColumnOption {

        private String  columnName;
        private boolean export;
    }

}
