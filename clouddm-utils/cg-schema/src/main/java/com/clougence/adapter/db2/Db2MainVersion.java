package com.clougence.adapter.db2;

import com.clougence.schema.DsType;
import com.clougence.schema.metadata.MainVersion;
import com.clougence.schema.metadata.Version;
import com.clougence.utils.StringUtils;

/**
 * @author mode 2020/4/18 11:42
 */
public enum Db2MainVersion implements MainVersion {

    Db2_11_5("v11.5"),
    Db2_11_1("v11.1"),
    Db2_10_5("v10.5"),
    Db2_10_1("v10.1"),
    Db2_9_7("v9.7");

    private final MainVersion mainVersion;

    Db2MainVersion(String mainVersion){
        this.mainVersion = new Version(DsType.Db2, mainVersion);
    }

    @Override
    public DsType getDsType() { return mainVersion.getDsType(); }

    public String getMainVersion() { return this.mainVersion.getMainVersion(); }

    //for compare
    private String gerVersionWithoutPrefix() {
        return this.mainVersion.getMainVersion().substring(1);
    }

    @Override
    public boolean isGe(MainVersion targetVersion) {
        if (targetVersion == null) {
            return false;
        }

        if (this.getDsType() != targetVersion.getDsType()) {
            return false;
        }

        if (targetVersion instanceof Db2MainVersion) {
            Version source = new Version(this.getDsType(), this.gerVersionWithoutPrefix());
            Version target = new Version(targetVersion.getDsType(), ((Db2MainVersion) targetVersion).gerVersionWithoutPrefix());
            return source.compareTo(target) >= 0;
        } else {
            return false;
        }

    }

    @Override
    public boolean isGt(MainVersion targetVersion) {
        if (targetVersion == null) {
            return false;
        }

        if (this.getDsType() != targetVersion.getDsType()) {
            return false;
        }
        if (targetVersion instanceof Db2MainVersion) {
            Version source = new Version(this.getDsType(), this.gerVersionWithoutPrefix());
            Version target = new Version(targetVersion.getDsType(), ((Db2MainVersion) targetVersion).gerVersionWithoutPrefix());
            return source.compareTo(target) > 0;
        } else {
            return false;
        }
    }

    @Override
    public boolean isLe(MainVersion targetVersion) {
        if (targetVersion == null) {
            return false;
        }

        if (this.getDsType() != targetVersion.getDsType()) {
            return false;
        }
        if (targetVersion instanceof Db2MainVersion) {
            Version source = new Version(this.getDsType(), this.gerVersionWithoutPrefix());
            Version target = new Version(targetVersion.getDsType(), ((Db2MainVersion) targetVersion).gerVersionWithoutPrefix());
            return source.compareTo(target) <= 0;
        } else {
            return false;
        }
    }

    @Override
    public boolean isLt(MainVersion targetVersion) {
        if (targetVersion == null) {
            return false;
        }

        if (this.getDsType() != targetVersion.getDsType()) {
            return false;
        }

        if (targetVersion instanceof Db2MainVersion) {
            Version source = new Version(this.getDsType(), this.gerVersionWithoutPrefix());
            Version target = new Version(targetVersion.getDsType(), ((Db2MainVersion) targetVersion).gerVersionWithoutPrefix());
            return source.compareTo(target) < 0;
        } else {
            return false;
        }
    }

    public MainVersion getVersion() { return this.mainVersion; }

    public static Db2MainVersion parserVersion(String version) {
        if (StringUtils.isBlank(version)) {
            return null;
        }

        if (checkInVersion(version, Db2_11_5)) {
            return Db2_11_5;
        } else if (checkInVersion(version, Db2_11_1)) {
            return Db2_11_1;
        } else if (checkInVersion(version, Db2_10_5)) {
            return Db2_10_5;
        } else if (checkInVersion(version, Db2_10_1)) {
            return Db2_10_1;
        } else if (checkInVersion(version, Db2_9_7)) {
            return Db2_9_7;
        } else {
            throw new UnsupportedOperationException("Unsupported Db2 version " + version);
        }
    }

    private static boolean checkInVersion(String version, MainVersion mainVersion) {
        String referenceVersion = mainVersion.getMainVersion();
        return version.equals(referenceVersion) || StringUtils.containsIgnoreCase(version, referenceVersion);
    }
}
