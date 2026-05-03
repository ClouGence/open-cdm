package com.clougence.clouddm.ds.hologres.dialect;

import com.clougence.clouddm.dsfamily.schema.dialect.AbstractDialect;
import com.clougence.schema.dialect.Dialect;
import com.clougence.utils.StringUtils;

/**
 * ODPS 的 SqlDialect 实现
 * @version : 2020-10-31
 * @author 赵永春 (zyc@hasor.net)
 */
public class HgDialect extends AbstractDialect {

    public static Dialect INSTANCE = new HgDialect();

    @Override
    protected String keyWordsResource() {
        return "/META-INF/clougence/db-keywords/hologres.keywords";
    }

    @Override
    protected String defaultQualifier() {
        return "`";
    }

    protected String fmtNameValue(boolean useQualifier, String fmtString) {
        return fmtString.replace("`", "``");
    }

    public String fmtComment(String str) {
        if (StringUtils.isBlank(str)) {
            return str;
        }

        return str.replace("'", "''").replace("\\", "\\\\");
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
