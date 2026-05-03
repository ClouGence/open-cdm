package com.clougence.rdp.dal.enumeration;

import static com.clougence.rdp.dal.enumeration.DeployEnvInfoFetchType.MANUALLY_FILL;
import static com.clougence.rdp.dal.enumeration.DeployEnvInfoFetchType.OPENAPI;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import com.clougence.utils.StringUtils;

import lombok.Getter;

/**
 * deploy environment Enum
 *
 * @author bucketli 2020/2/20 11:31
 */
public enum DeployEnvType {

    SELF_MAINTENANCE("SELF_MAINTENANCE", true, Collections.singletonList(MANUALLY_FILL)),
    ALIBABA_CLOUD_HOSTED("ALIBABA_CLOUD_HOSTED", true, Arrays.asList(OPENAPI, MANUALLY_FILL)),
    AWS_CLOUD_HOSTED("AWS_CLOUD_HOSTED", true, Collections.singletonList(MANUALLY_FILL)),
    MICROSOFT_AZURE_CLOUD_HOSTED("MICROSOFT_AZURE_CLOUD_HOSTED", true, Collections.singletonList(MANUALLY_FILL)),
    HUAWEI_CLOUD_HOSTED("HUAWEI_CLOUD_HOSTED", true, Collections.singletonList(MANUALLY_FILL)),
    TENCENT_CLOUD_HOSTED("TENCENT_CLOUD_HOSTED", true, Collections.singletonList(MANUALLY_FILL)),
    LOCAL("LOCAL", false, Collections.singletonList(MANUALLY_FILL)),
    INDEPENDENT_CLOUD_PLATFORM("INDEPENDENT_CLOUD_PLATFORM", true, Collections.singletonList(MANUALLY_FILL));

    @Getter
    private final String                       typeName;
    @Getter
    private final boolean                      enable;
    @Getter
    private final List<DeployEnvInfoFetchType> fetchTypeList;

    DeployEnvType(String typeName, boolean enable, List<DeployEnvInfoFetchType> list){
        this.typeName = typeName;
        this.enable = enable;
        this.fetchTypeList = list;
    }

    public static DeployEnvType getTypeByName(String typeName) {
        if (StringUtils.isBlank(typeName)) {
            return null;
        }
        DeployEnvType result = null;
        for (DeployEnvType envType : DeployEnvType.values()) {
            if (typeName.equals(envType.getTypeName())) {
                result = envType;
                break;
            }
        }
        return result;
    }
}
