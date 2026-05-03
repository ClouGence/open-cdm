package com.clougence.adapter.doris;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.clougence.schema.DsType;
import com.clougence.schema.metadata.MainVersion;
import com.clougence.schema.metadata.Version;
import com.clougence.utils.StringUtils;

/**
 * @author wanshao
 */
public enum DorisMainVersion implements MainVersion {

    Doris_1_0("1.0"),
    Doris_1_1("1.1"),
    Doris_1_2_0("1.2.0"),
    Doris_1_2_1("1.2.1"),
    Doris_1_2("1.2"),
    Doris_2_0("2.0"),
    Doris_2_1("2.1"),
    Doris_2_2("2.2"),
    Doris_2_3("2.3"),
    Doris_2_4("2.4"),
    Doris_2_5("2.5"),
    Doris_2_6("2.6"),
    Doris_2_7("2.7"),
    Doris_2_8("2.8"),
    Doris_2_9("2.9"),
    Doris_3_0("3.0"),
    Doris_3_1("3.1"),
    Doris_4_0("4.0"),
    Doris_4_1("4.1"),
    Doris_4_2("4.2"),
    Doris_5_0("5.0"),
    Doris_5_1("5.1");

    private final MainVersion mainVersion;

    DorisMainVersion(String mainVersion){
        this.mainVersion = new Version(DsType.Doris, mainVersion);
    }

    @Override
    public DsType getDsType() { return mainVersion.getDsType(); }

    @Override
    public String getMainVersion() { return this.mainVersion.getMainVersion(); }

    public MainVersion getVersion() { return this.mainVersion; }

    public static DorisMainVersion parserVersion(String fullVersion) {
        if (StringUtils.isBlank(fullVersion)) {
            return null;
        }

        String version = fixedVersion(fullVersion);

        for (DorisMainVersion mainVer : values()) {
            if (checkInVersion(version, mainVer)) {
                return mainVer;
            }
        }
        throw new UnsupportedOperationException("Unsupported Doris version " + version);
    }

    private static String fixedVersion(String fullVersion) {
        Pattern pattern = Pattern.compile("\\d.*");
        Matcher matcher = pattern.matcher(fullVersion);
        if (matcher.find()) {
            return matcher.group();
        }
        return fullVersion;
    }

    private static boolean checkInVersion(String version, MainVersion mainVersion) {
        String refVer = mainVersion.getMainVersion();
        return version.equals(refVer) || StringUtils.startsWithIgnoreCase(version, refVer);
    }
}
