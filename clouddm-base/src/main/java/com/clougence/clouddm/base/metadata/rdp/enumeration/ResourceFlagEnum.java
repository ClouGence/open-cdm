package com.clougence.clouddm.base.metadata.rdp.enumeration;

import lombok.Getter;

/**
 * @author chunlin create time is 2024/7/19
 */
@Getter
public enum ResourceFlagEnum {
    DATASOURCE("DATASOURCE", "InstantId"),
    ACCOUNT("ACCOUNT", "Username"),
    ROLE("ROLE", "RoleName"),
    DS_ENV("DS_ENV", "DsEnvName"),
    DATA_JOB("DATA_JOB", "DataJobName");

    private final String resourceFlag;
    private final String flagDesc;

    ResourceFlagEnum(String resourceFlag, String flagDesc) {
        this.resourceFlag = resourceFlag;
        this.flagDesc = flagDesc;
    }

    public static String getFlagDesc(String resourceFlag) {
        for (ResourceFlagEnum value : values()) {
            if (value.resourceFlag.equals(resourceFlag)) {
                return value.flagDesc;
            }
        }
        return null;
    }
}
