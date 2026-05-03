package com.clougence.adapter.oracle;

import com.clougence.schema.DsType;
import com.clougence.schema.metadata.MainVersion;
import com.clougence.schema.metadata.Version;
import com.clougence.utils.StringUtils;

/**
 * @author mode 2020/4/18 11:42
 */
public enum OracleMainVersion implements MainVersion {

    Oracle_19("19."),
    Oracle_18("18."),
    Oracle_12("12."),
    Oracle_11("11."),
    Oracle_10("10.");

    private final MainVersion mainVersion;

    OracleMainVersion(String mainVersion){
        this.mainVersion = new Version(DsType.Oracle, mainVersion);
    }

    @Override
    public DsType getDsType() { return mainVersion.getDsType(); }

    public String getMainVersion() { return this.mainVersion.getMainVersion(); }

    public MainVersion getVersion() { return this.mainVersion; }

    public static OracleMainVersion parserVersion(String version) {
        if (StringUtils.isBlank(version)) {
            return null;
        }

        if (checkInVersion(version, Oracle_19)) {
            return Oracle_19;
        } else if (checkInVersion(version, Oracle_18)) {
            return Oracle_18;
        } else if (checkInVersion(version, Oracle_12)) {
            return Oracle_12;
        } else if (checkInVersion(version, Oracle_11)) {
            return Oracle_11;
        } else if (checkInVersion(version, Oracle_10)) {
            return Oracle_10;
        } else {
            throw new UnsupportedOperationException("Unsupported oracle version " + version);
        }
    }

    private static boolean checkInVersion(String version, MainVersion mainVersion) {
        String referenceVersion = mainVersion.getMainVersion();
        return version.equals(referenceVersion) || StringUtils.startsWithIgnoreCase(version, referenceVersion);
    }
}
