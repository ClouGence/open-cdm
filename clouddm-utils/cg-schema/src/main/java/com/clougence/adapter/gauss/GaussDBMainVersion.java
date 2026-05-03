package com.clougence.adapter.gauss;

import com.clougence.schema.DsType;
import com.clougence.schema.metadata.MainVersion;
import com.clougence.schema.metadata.Version;
import com.clougence.utils.StringUtils;

public enum GaussDBMainVersion implements MainVersion {

    GaussDB_505("505"),;

    private final MainVersion mainVersion;

    GaussDBMainVersion(String mainVersion){
        this.mainVersion = new Version(DsType.GaussDB, mainVersion);
    }

    public static GaussDBMainVersion parserVersion(String fullVersion) {
        if (checkInVersion(fullVersion, GaussDB_505)) {
            return GaussDB_505;
        } else {
            throw new UnsupportedOperationException("Unsupported gaussdb version " + fullVersion);
        }
    }

    private static boolean checkInVersion(String version, MainVersion mainVersion) {
        String referenceVersion = mainVersion.getMainVersion();
        return version.equals(referenceVersion) || StringUtils.containsIgnoreCase(version, referenceVersion);
    }

    @Override
    public DsType getDsType() { return mainVersion.getDsType(); }

    public String getMainVersion() { return this.mainVersion.getMainVersion(); }

    public MainVersion getVersion() { return this.mainVersion; }

}
