package com.clougence.adapter.polar.porx;

import com.clougence.schema.DsType;
import com.clougence.schema.metadata.MainVersion;
import com.clougence.schema.metadata.Version;
import com.clougence.utils.StringUtils;

/**
 * @author mode 2020/4/18 11:42
 */
public enum PolarDbXMainVersion implements MainVersion {

    POLARDBX_5_4_9("5.4.9"),
    POLARDBX_5_4_10("5.4.10"),
    POLARDBX_5_4_11("5.4.11"),
    POLARDBX_5_4_12("5.4.12"),
    POLARDBX_5_4_13("5.4.13"),
    POLARDBX_5_4_14("5.4.14"),
    POLARDBX_5_4_15("5.4.15"),
    POLARDBX_5_4_16("5.4.16"),
    POLARDBX_5_4_17("5.4.17"),
    POLARDBX_5_4_18("5.4.18"),
    POLARDBX_5_4_19("5.4.19"),
    POLARDBX_5_4_20("5.4.20"),
    POLARDBX_5_5_0("5.5.0"),
    POLARDBX_5_5_1("5.5.1"),
    POLARDBX_5_5_2("5.5.2"),
    POLARDBX_5_6_0("5.6.0"),
    POLARDBX_5_6_1("5.6.1"),
    POLARDBX_5_6_2("5.6.2"),
    POLARDBX_6_0_0("6.0.0"),
    POLARDBX_6_0_1("6.0.1"),
    POLARDBX_6_0_2("6.0.2"),
    POLARDBX_6_1_0("6.1.0"),
    POLARDBX_6_1_1("6.1.1"),;

    private final MainVersion mainVersion;

    PolarDbXMainVersion(String mainVersion){
        this.mainVersion = new Version(DsType.PolarDbX, mainVersion);
    }

    @Override
    public DsType getDsType() { return mainVersion.getDsType(); }

    public String getMainVersion() { return this.mainVersion.getMainVersion(); }

    public MainVersion getVersion() { return this.mainVersion; }

    public static PolarDbXMainVersion parserVersion(String version) {
        if (StringUtils.isBlank(version)) {
            return null;
        }

        if (checkInVersion(version, POLARDBX_5_4_9)) {
            return POLARDBX_5_4_9;
        } else if (checkInVersion(version, POLARDBX_5_4_10)) {
            return POLARDBX_5_4_10;
        } else if (checkInVersion(version, POLARDBX_5_4_11)) {
            return POLARDBX_5_4_11;
        } else if (checkInVersion(version, POLARDBX_5_4_12)) {
            return POLARDBX_5_4_12;
        } else if (checkInVersion(version, POLARDBX_5_4_13)) {
            return POLARDBX_5_4_13;
        } else if (checkInVersion(version, POLARDBX_5_4_14)) {
            return POLARDBX_5_4_14;
        } else if (checkInVersion(version, POLARDBX_5_4_15)) {
            return POLARDBX_5_4_15;
        } else if (checkInVersion(version, POLARDBX_5_4_16)) {
            return POLARDBX_5_4_16;
        } else if (checkInVersion(version, POLARDBX_5_4_17)) {
            return POLARDBX_5_4_17;
        } else if (checkInVersion(version, POLARDBX_5_4_18)) {
            return POLARDBX_5_4_18;
        } else if (checkInVersion(version, POLARDBX_5_4_19)) {
            return POLARDBX_5_4_19;
        } else if (checkInVersion(version, POLARDBX_5_4_20)) {
            return POLARDBX_5_4_20;
        } else if (checkInVersion(version, POLARDBX_5_5_0)) {
            return POLARDBX_5_5_0;
        } else if (checkInVersion(version, POLARDBX_5_5_1)) {
            return POLARDBX_5_5_1;
        } else if (checkInVersion(version, POLARDBX_5_5_2)) {
            return POLARDBX_5_5_2;
        } else if (checkInVersion(version, POLARDBX_5_6_0)) {
            return POLARDBX_5_6_0;
        } else if (checkInVersion(version, POLARDBX_5_6_1)) {
            return POLARDBX_5_6_1;
        } else if (checkInVersion(version, POLARDBX_5_6_2)) {
            return POLARDBX_5_6_2;
        } else if (checkInVersion(version, POLARDBX_6_0_0)) {
            return POLARDBX_6_0_0;
        } else if (checkInVersion(version, POLARDBX_6_0_1)) {
            return POLARDBX_6_0_1;
        } else if (checkInVersion(version, POLARDBX_6_0_2)) {
            return POLARDBX_6_0_2;
        } else if (checkInVersion(version, POLARDBX_6_1_0)) {
            return POLARDBX_6_1_0;
        } else if (checkInVersion(version, POLARDBX_6_1_1)) {
            return POLARDBX_6_1_1;
        } else {
            throw new UnsupportedOperationException("Unsupported PolarDbX version " + version);
        }
    }

    private static boolean checkInVersion(String version, MainVersion mainVersion) {
        String referenceVersion = mainVersion.getMainVersion();
        return version.equals(referenceVersion) || version.contains(referenceVersion);
    }
}
