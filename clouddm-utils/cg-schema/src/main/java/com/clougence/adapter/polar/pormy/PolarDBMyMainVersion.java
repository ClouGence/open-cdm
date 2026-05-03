package com.clougence.adapter.polar.pormy;

import com.clougence.schema.DsType;
import com.clougence.schema.metadata.MainVersion;
import com.clougence.schema.metadata.Version;
import com.clougence.utils.StringUtils;

/**
 * @author mode 2020/4/18 11:42
 */
public enum PolarDBMyMainVersion implements MainVersion {

    PolarDBMy_5_6("5.6"),
    PolarDBMy_5_7("5.7"),
    PolarDBMy_8_0("8.0");

    private final MainVersion mainVersion;

    PolarDBMyMainVersion(String mainVersion){
        this.mainVersion = new Version(DsType.PolarDBMySQL, mainVersion);
    }

    @Override
    public DsType getDsType() { return mainVersion.getDsType(); }

    public String getMainVersion() { return this.mainVersion.getMainVersion(); }

    public MainVersion getVersion() { return this.mainVersion; }

    public static PolarDBMyMainVersion parserVersion(String version) {
        if (StringUtils.isBlank(version)) {
            return null;
        }

        if (checkInVersion(version, PolarDBMy_5_6)) {
            return PolarDBMy_5_6;
        } else if (checkInVersion(version, PolarDBMy_5_7)) {
            return PolarDBMy_5_7;
        } else if (checkInVersion(version, PolarDBMy_8_0)) {
            return PolarDBMy_8_0;
        } else {
            throw new UnsupportedOperationException("Unsupported PolarDBMy version " + version);
        }
    }

    private static boolean checkInVersion(String version, MainVersion mainVersion) {
        String referenceVersion = mainVersion.getMainVersion();
        return version.equals(referenceVersion) || StringUtils.startsWithIgnoreCase(version, referenceVersion);
    }
}
