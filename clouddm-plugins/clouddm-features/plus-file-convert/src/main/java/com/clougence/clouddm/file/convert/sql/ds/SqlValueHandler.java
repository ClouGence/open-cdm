package com.clougence.clouddm.file.convert.sql.ds;

import com.clougence.clouddm.file.convert.sql.SqlRowData;

public interface SqlValueHandler {

    void handle(SqlRowData row, String beforeData, int i);
}
