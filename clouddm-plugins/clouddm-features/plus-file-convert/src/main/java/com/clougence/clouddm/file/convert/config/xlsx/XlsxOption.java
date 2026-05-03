package com.clougence.clouddm.file.convert.config.xlsx;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class XlsxOption {

    private long               offset;
    private long               limit;
    private List<ColumnOption> columns;

    @Getter
    @Setter
    public static class ColumnOption {

        private String  columnName;
        private boolean export;
    }

}
