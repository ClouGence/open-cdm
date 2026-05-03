package com.clougence.schema.metadata;

import com.clougence.schema.DsType;
import com.clougence.utils.StringUtils;

/**
 * version info
 * @version : 2020-10-31
 * @author 赵永春 (zyc@hasor.net)
 */
public interface MainVersion {

    DsType getDsType();

    String name();

    String getMainVersion();

    default boolean isEq(MainVersion targetVersion) {
        if (targetVersion == null) {
            return false;
        }

        if (this.getDsType() != targetVersion.getDsType()) {
            return false;
        }

        return StringUtils.equals(this.getMainVersion(), targetVersion.getMainVersion());
    }

    default boolean isGe(MainVersion targetVersion) {
        if (targetVersion == null) {
            return false;
        }

        if (this.getDsType() != targetVersion.getDsType()) {
            return false;
        }

        Version source = new Version(this.getDsType(), this.getMainVersion());
        Version target = new Version(targetVersion.getDsType(), targetVersion.getMainVersion());
        return source.compareTo(target) >= 0;
    }

    default boolean isGt(MainVersion targetVersion) {
        if (targetVersion == null) {
            return false;
        }

        if (this.getDsType() != targetVersion.getDsType()) {
            return false;
        }

        Version source = new Version(this.getDsType(), this.getMainVersion());
        Version target = new Version(targetVersion.getDsType(), targetVersion.getMainVersion());
        return source.compareTo(target) > 0;
    }

    default boolean isLe(MainVersion targetVersion) {
        if (targetVersion == null) {
            return false;
        }

        if (this.getDsType() != targetVersion.getDsType()) {
            return false;
        }

        Version source = new Version(this.getDsType(), this.getMainVersion());
        Version target = new Version(targetVersion.getDsType(), targetVersion.getMainVersion());
        return source.compareTo(target) <= 0;
    }

    default boolean isLt(MainVersion targetVersion) {
        if (targetVersion == null) {
            return false;
        }

        if (this.getDsType() != targetVersion.getDsType()) {
            return false;
        }

        Version source = new Version(this.getDsType(), this.getMainVersion());
        Version target = new Version(targetVersion.getDsType(), targetVersion.getMainVersion());
        return source.compareTo(target) < 0;
    }
}
