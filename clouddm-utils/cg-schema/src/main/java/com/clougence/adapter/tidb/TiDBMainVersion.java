package com.clougence.adapter.tidb;

import com.clougence.schema.DsType;
import com.clougence.schema.metadata.MainVersion;
import com.clougence.schema.metadata.Version;
import com.clougence.utils.StringUtils;

/**
 * TiDB version
 * @version : 2021-09-27
 * @author mode
 */
public enum TiDBMainVersion implements MainVersion {

    TiDB_8_x("TiDB-v8."),
    TiDB_7_x("TiDB-v7."),
    TiDB_6_x("TiDB-v6."),
    TiDB_5_x("TiDB-v5."),
    TiDB_4_x("TiDB-v4."),
    TiDB_3_x("TiDB-v3.");

    private final MainVersion mainVersion;

    TiDBMainVersion(String mainVersion){
        this.mainVersion = new Version(DsType.TiDB, mainVersion);
    }

    @Override
    public DsType getDsType() { return mainVersion.getDsType(); }

    public String getMainVersion() { return this.mainVersion.getMainVersion(); }

    public MainVersion getVersion() { return this.mainVersion; }

    public static TiDBMainVersion parserVersion(String version) {
        if (StringUtils.isBlank(version)) {
            return null;
        }

        if (checkInVersion(version, TiDB_8_x)) {
            return TiDB_8_x;// 8.0.11-TiDB-v8.4.0
        } else if (checkInVersion(version, TiDB_7_x)) {
            return TiDB_7_x;// 5.7.25-TiDB-v7.0.7
        } else if (checkInVersion(version, TiDB_6_x)) {
            return TiDB_6_x;// 5.7.25-TiDB-v6.0.7
        } else if (checkInVersion(version, TiDB_5_x)) {
            return TiDB_5_x;// 5.7.25-TiDB-v5.0.7
        } else if (checkInVersion(version, TiDB_4_x)) {
            return TiDB_4_x;// 5.7.25-TiDB-v4.0.7
        } else if (checkInVersion(version, TiDB_3_x)) {
            return TiDB_3_x;// 5.7.25-TiDB-v3.0.19
        } else {
            throw new UnsupportedOperationException("Unsupported tidb version " + version);
        }

    }

    private static boolean checkInVersion(String version, MainVersion mainVersion) {
        String referenceVersion = mainVersion.getMainVersion();
        return version.equals(referenceVersion) || StringUtils.containsIgnoreCase(version, referenceVersion);
    }
}
