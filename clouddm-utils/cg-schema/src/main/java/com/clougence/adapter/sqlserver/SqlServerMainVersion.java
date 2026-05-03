package com.clougence.adapter.sqlserver;

import com.clougence.schema.DsType;
import com.clougence.schema.metadata.MainVersion;
import com.clougence.schema.metadata.Version;
import com.clougence.utils.StringUtils;

/**
 * <a href="https://learn.microsoft.com/zh-cn/sql/sql-server/end-of-support/sql-server-end-of-support-overview#lifecycle-dates">SQL Server lifecycle dates</a>
 * @author mode 2020/4/18 11:42
 */
public enum SqlServerMainVersion implements MainVersion {

    SqlServer_2000("SQL Server 2000"),
    SqlServer_2005("SQL Server 2005"),
    SqlServer_2008("SQL Server 2008"),
    SqlServer_2012("SQL Server 2012"),
    SqlServer_2014("SQL Server 2014"),
    SqlServer_2016("SQL Server 2016"),
    SqlServer_2017("SQL Server 2017"),
    SqlServer_2019("SQL Server 2019"),
    SqlServer_2022("SQL Server 2022"),
    MICROSOFT_SQL_AZURE_12("Microsoft SQL Azure (RTM) - 12"),
    MICROSOFT_SQL_AZURE("Microsoft SQL Azure (RTM)"),

    ;

    private final MainVersion mainVersion;

    SqlServerMainVersion(String mainVersion){
        this.mainVersion = new Version(DsType.SqlServer, mainVersion);
    }

    @Override
    public DsType getDsType() { return mainVersion.getDsType(); }

    public String getMainVersion() { return this.mainVersion.getMainVersion(); }

    public MainVersion getVersion() { return this.mainVersion; }

    public static SqlServerMainVersion parserVersion(String version) {
        if (StringUtils.isBlank(version)) {
            return null;
        }

        for (SqlServerMainVersion sqlVersion : values()) {
            if (checkInVersion(version, sqlVersion)) {
                return sqlVersion;
            }
        }
        throw new UnsupportedOperationException("Unsupported SqlServer version " + version);
    }

    private static boolean checkInVersion(String version, MainVersion mainVersion) {
        String referenceVersion = mainVersion.getMainVersion();
        return version.equals(referenceVersion) || StringUtils.containsIgnoreCase(version, referenceVersion);
    }
}
