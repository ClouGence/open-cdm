package com.clougence.schema.handlers;

import com.clougence.schema.DsType;
import com.clougence.schema.metadata.MainVersion;
import com.clougence.utils.StringUtils;

public class KeyUtils {

    public static Key[] buildMappingKey(DsType srcDsType, DsType dstDsType, MainVersion mainVersion) {
        if (mainVersion == null) {
            return new Key[] { new Key(srcDsType.getTypeName(), dstDsType.getTypeName()) };
        } else {
            return new Key[] { new Key(srcDsType.getTypeName(), dstDsType.getTypeName()), //
                               new Key(srcDsType.getTypeName(), dstDsType.getTypeName(), mainVersion.getMainVersion()) };
        }
    }

    public static Key[] buildTableKey(DsType dsType, MainVersion mainVersion) {
        if (mainVersion == null) {
            return new Key[] { new Key(dsType.getTypeName()) };
        } else {
            return new Key[] { new Key(dsType.getTypeName()), //
                               new Key(dsType.getTypeName(), mainVersion.getMainVersion()) };
        }
    }

    public static Key[] buildColumnKey(DsType dsType, String columnType, MainVersion mainVersion) {
        if (StringUtils.isBlank(columnType)) {
            return new Key[] { new Key(dsType.getTypeName()) };
        } else {
            if (mainVersion == null) {
                return new Key[] { new Key(dsType.getTypeName()), //
                                   new Key(dsType.getTypeName(), columnType) };
            } else {
                return new Key[] { new Key(dsType.getTypeName()), //
                                   new Key(dsType.getTypeName(), columnType), //
                                   new Key(dsType.getTypeName(), columnType, mainVersion.getMainVersion()) };
            }
        }
    }
}
