package com.clougence.adapter.starrocks;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.clougence.schema.DsType;
import com.clougence.schema.metadata.MainVersion;
import com.clougence.schema.metadata.Version;
import com.clougence.utils.StringUtils;

/**
 * @author wanshao
 */
public enum StarRocksMainVersion implements MainVersion {

    StarRocks_1_18("1.18"),
    StarRocks_1_19("1.19"),
    StarRocks_2_0("2.0"),
    StarRocks_2_1("2.1"),
    StarRocks_2_2("2.2"),
    StarRocks_2_3("2.3"),
    StarRocks_2_4("2.4"),
    StarRocks_2_5("2.5"),
    StarRocks_3_0("3.0"),
    StarRocks_3_1("3.1"),
    StarRocks_3_2("3.2"),
    StarRocks_3_3("3.3"),
    StarRocks_3_4("3.4"),
    StarRocks_3_5("3.5"),
    StarRocks_3_6("3.6"),
    StarRocks_3_7("3.7"),
    StarRocks_3_8("3.8"),
    StarRocks_3_9("3.9"),
    StarRocks_4_0("4.0"),
    StarRocks_5_0("5.0"),
    StarRocks_5_1("5.1"),;

    private final MainVersion mainVersion;

    StarRocksMainVersion(String mainVersion){
        this.mainVersion = new Version(DsType.StarRocks, mainVersion);
    }

    @Override
    public DsType getDsType() { return mainVersion.getDsType(); }

    @Override
    public String getMainVersion() { return this.mainVersion.getMainVersion(); }

    public MainVersion getVersion() { return this.mainVersion; }

    public static StarRocksMainVersion parserVersion(String fullVersion) {
        if (StringUtils.isBlank(fullVersion)) {
            return null;
        }

        String version = fixedVersion(fullVersion);

        for (StarRocksMainVersion starRocksMainVersion : values()) {
            if (checkInVersion(version, starRocksMainVersion)) {
                return starRocksMainVersion;
            }
        }
        throw new UnsupportedOperationException("Unsupported StarRocks version " + version);
    }

    public static String fixedVersion(String fullVersion) {
        Pattern pattern = Pattern.compile("\\d.*");
        Matcher matcher = pattern.matcher(fullVersion);
        if (matcher.find()) {
            return matcher.group();
        }
        return fullVersion;
    }

    private static boolean checkInVersion(String version, MainVersion mainVersion) {
        String referenceVersion = mainVersion.getMainVersion();
        return version.equals(referenceVersion) || StringUtils.startsWithIgnoreCase(version, referenceVersion);
    }
}
