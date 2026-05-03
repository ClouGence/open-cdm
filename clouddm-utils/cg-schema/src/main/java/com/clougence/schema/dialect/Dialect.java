package com.clougence.schema.dialect;

import java.util.Set;

/**
 * Dialect
 * @version : 2020-10-31
 * @author 赵永春 (zyc@hasor.net)
 */
public interface Dialect {

    default int maxArg() {
        return Integer.MAX_VALUE;
    }

    /** Cannot be used as a key for column names. when column name is key words, Generate SQL using Qualifier warp it. */
    Set<String> keywords();

    String leftQualifier();

    String rightQualifier();

    String fmtName(boolean useDelimited, String name);

    String fmtValue(String value);

    String fmtComment(String str);

    String fmtTableName(boolean useDelimited, String catalog, String schema, String table);
}
