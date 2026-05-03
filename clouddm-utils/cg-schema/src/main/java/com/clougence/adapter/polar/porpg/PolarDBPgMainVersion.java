package com.clougence.adapter.polar.porpg;

import com.clougence.schema.DsType;
import com.clougence.schema.metadata.MainVersion;
import com.clougence.schema.metadata.Version;
import com.clougence.utils.StringUtils;

/**
 * @author mode 2020/4/18 11:42
 */
public enum PolarDBPgMainVersion implements MainVersion {

    PolarDBPg_11("11");

    private final MainVersion mainVersion;

    PolarDBPgMainVersion(String mainVersion){
        this.mainVersion = new Version(DsType.PolarDBPostgre, mainVersion);
    }

    @Override
    public DsType getDsType() { return mainVersion.getDsType(); }

    public String getMainVersion() { return this.mainVersion.getMainVersion(); }

    public MainVersion getVersion() { return this.mainVersion; }

    public static PolarDBPgMainVersion parserVersion(String version) {
        if (StringUtils.isBlank(version)) {
            return null;
        }

        if (checkInVersion(version, PolarDBPg_11)) {
            return PolarDBPg_11;
        } else {
            throw new UnsupportedOperationException("Unsupported PolarDBPg version " + version);
        }
    }

    private static boolean checkInVersion(String version, MainVersion mainVersion) {
        String referenceVersion = mainVersion.getMainVersion();
        return version.equals(referenceVersion) || StringUtils.startsWithIgnoreCase(version, referenceVersion);
    }
}
