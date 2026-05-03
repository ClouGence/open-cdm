package com.clougence.clouddm.ds.clickhouse.dialect;

import com.clougence.clouddm.dsfamily.schema.dialect.AbstractDialect;
import com.clougence.schema.dialect.Dialect;
import com.clougence.utils.StringUtils;

/**
 * ClickHouse 的 SqlDialect 实现
 * @version : 2020-10-31
 * @author 赵永春 (zyc@hasor.net)
 */
public class ClickHouseDialect extends AbstractDialect {

    public static Dialect INSTANCE = new ClickHouseDialect();

    @Override
    protected String keyWordsResource() {
        return "/META-INF/clougence/db-keywords/clickhouse.keywords";
    }

    @Override
    protected String defaultQualifier() {
        return "`";
    }

    public String fmtComment(String str) {
        if (StringUtils.isBlank(str)) {
            return str;
        }

        return str.replace("'", "''");
    }

    @Override
    public String fmtTableName(boolean useDelimited, String catalog, String schema, String table) {
        StringBuilder sqlBuilder = new StringBuilder();
        if (StringUtils.isNotBlank(schema)) {
            sqlBuilder.append(fmtName(useDelimited, schema));
            sqlBuilder.append(".");
        }
        sqlBuilder.append(fmtName(useDelimited, table));

        return sqlBuilder.toString();
    }
}
