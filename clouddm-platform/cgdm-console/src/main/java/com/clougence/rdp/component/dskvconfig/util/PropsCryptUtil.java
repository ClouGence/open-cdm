package com.clougence.rdp.component.dskvconfig.util;

import java.util.*;
import java.util.function.Function;

import com.clougence.clouddm.api.common.crypt.CryptService;
import com.clougence.clouddm.base.metadata.ds.DataSourceType;
import com.clougence.clouddm.base.metadata.rdp.enumeration.DeltaLakePropsKey;
import com.clougence.clouddm.base.metadata.rdp.enumeration.IcebergPropsKey;
import com.clougence.clouddm.base.metadata.rdp.enumeration.PaimonPropsKey;
import com.clougence.rdp.component.dskvconfig.model.DeltaLakeExtraConfig;
import com.clougence.rdp.component.dskvconfig.model.IcebergExtraConfig;
import com.clougence.rdp.component.dskvconfig.model.PaimonExtraConfig;
import com.clougence.utils.CollectionUtils;
import com.clougence.utils.ExceptionUtils;
import com.clougence.utils.JsonUtils;
import com.clougence.utils.StringUtils;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class PropsCryptUtil {

    private static final ObjectMapper                       OBJECT_MAPPER       = new ObjectMapper();

    private static final TypeReference<Map<String, String>> MAP_TYPE_REFERENCE  = new TypeReference<Map<String, String>>() {
                                                                                };

    public static final Map<DataSourceType, List<String>>   SECRET_PROPERTIES   = new LinkedHashMap<>();

    public static final Map<DataSourceType, Set<String>>    CONFIG_SECRET_NAMES = new LinkedHashMap<>();

    static {
        SECRET_PROPERTIES.put(DataSourceType.Iceberg, Arrays.asList( //                    
                IcebergPropsKey.S3_ACCESS_KEY, IcebergPropsKey.S3_SECRET_KEY, // 
                IcebergPropsKey.GLUE_ACCESS_KEY, IcebergPropsKey.GLUE_SECRET_KEY //
        ));
        SECRET_PROPERTIES.put(DataSourceType.Paimon, Arrays.asList( //                    
                PaimonPropsKey.S3_ACCESS_KEY, PaimonPropsKey.S3_SECRET_KEY, // 
                PaimonPropsKey.DLF_ACCESS_ID, PaimonPropsKey.DLF_SECRET_KEY, //
                PaimonPropsKey.COSN_ACCESS_ID, PaimonPropsKey.COSN_SECRET_KEY, //                                    
                PaimonPropsKey.OSS_ACCESS_ID, PaimonPropsKey.OSS_SECRET_ID // 
        ));
        SECRET_PROPERTIES.put(DataSourceType.DeltaLake, Arrays.asList( //
                DeltaLakePropsKey.S3_ACCESS_KEY, DeltaLakePropsKey.S3_SECRET_KEY //
        ));

        CONFIG_SECRET_NAMES.put(DataSourceType.Iceberg, Collections.singleton(IcebergExtraConfig.Fields.catalogProps));
        CONFIG_SECRET_NAMES.put(DataSourceType.Paimon, Collections.singleton(PaimonExtraConfig.Fields.catalogProps));
        CONFIG_SECRET_NAMES.put(DataSourceType.DeltaLake, Collections.singleton(DeltaLakeExtraConfig.Fields.catalogProps));
    }

    public static String diffAndEncryptSecrets(DataSourceType dsType, String oldPropsJson, String newPropsJson) {
        if (!SECRET_PROPERTIES.containsKey(dsType)) {
            throw new RuntimeException("PropsCrypt is unsupported for " + dsType);
        }

        if (StringUtils.isBlank(newPropsJson)) {
            return null;
        }

        Map<String, String> newProps = parsePropsJson(newPropsJson);

        if (StringUtils.isBlank(oldPropsJson)) {
            return encryptAllSecrets(dsType, newProps);
        }

        Map<String, String> oldProps = parsePropsJson(oldPropsJson);
        Map<String, String> resultMap = new HashMap<>(newProps);

        for (String secretKey : SECRET_PROPERTIES.get(dsType)) {
            String newValue = newProps.get(secretKey);
            String oldEncryptedValue = oldProps.get(secretKey);

            if (newValue == null && oldEncryptedValue == null) {
                continue;
            }

            if (isLikelyEncrypted(newValue) && StringUtils.equals(newValue, oldEncryptedValue)) {
                continue;
            }

            if (resultMap.containsKey(secretKey)) {
                String oldPlaintextValue;
                boolean isUnchanged = false;

                if (StringUtils.isNotBlank(oldEncryptedValue)) {
                    try {
                        oldPlaintextValue = CryptService.INSTANCE.decryptUseDefaultKeyAndSalt(oldEncryptedValue);
                        if (Objects.equals(oldPlaintextValue, newValue)) {
                            isUnchanged = true;
                        }
                    } catch (Exception e) {
                        log.warn("Could not decrypt the old property value for key: '{}'. It will be treated as changed.", secretKey, e);
                    }
                }

                if (isUnchanged) {
                    resultMap.put(secretKey, oldEncryptedValue);
                } else {
                    String newEncryptedValue = CryptService.INSTANCE.encryptUseDefaultKeyAndSalt(newValue);
                    resultMap.put(secretKey, newEncryptedValue);
                }
            }
        }

        return JsonUtils.toJson(resultMap);
    }

    public static String encryptAllSecrets(DataSourceType dsType, Map<String, String> propsMap) {
        if (!SECRET_PROPERTIES.containsKey(dsType)) {
            throw new RuntimeException("PropsCrypt is unsupported for " + dsType);
        }

        if (propsMap == null || propsMap.isEmpty()) {
            return null;
        }

        boolean hasChanged = false;
        for (String secretKey : SECRET_PROPERTIES.get(dsType)) {
            if (propsMap.containsKey(secretKey)) {
                String plaintextValue = propsMap.get(secretKey);
                if (StringUtils.isNotBlank(plaintextValue)) {
                    propsMap.put(secretKey, CryptService.INSTANCE.encryptUseDefaultKeyAndSalt(plaintextValue.trim()));
                    hasChanged = true;
                }
            }
        }
        return hasChanged ? JsonUtils.toJson(propsMap) : null;
    }

    public static String encryptSecretPropsToStr(DataSourceType dsType, String catalogProps) {
        return processSecretProperties(dsType, catalogProps, encryptedText -> CryptService.INSTANCE.encryptUseDefaultKeyAndSalt(encryptedText), false);
    }

    public static String decryptSecretPropsToStr(DataSourceType dsType, String catalogProps) {
        return processSecretProperties(dsType, catalogProps, encryptedText -> CryptService.INSTANCE.decryptUseDefaultKeyAndSalt(encryptedText), true);
    }

    private static String processSecretProperties(DataSourceType dsType, String catalogProps, Function<String, String> valueProcessor, boolean checkHex) {
        if (!SECRET_PROPERTIES.containsKey(dsType)) {
            throw new RuntimeException("PropsCrypt is unsupported for " + dsType);
        }

        if (StringUtils.isBlank(catalogProps)) {
            return null;
        }

        try {
            Map<String, String> propsMap = parsePropsJson(catalogProps);
            if (CollectionUtils.isEmpty(propsMap)) {
                return null;
            }

            boolean hasChanged = false;
            for (String secretKey : SECRET_PROPERTIES.get(dsType)) {
                if (!propsMap.containsKey(secretKey)) {
                    continue;
                }

                String originalValue = propsMap.get(secretKey);
                if (StringUtils.isBlank(originalValue)) {
                    continue;
                }

                if (checkHex && !isLikelyEncrypted(originalValue)) {
                    continue;
                }

                try {
                    String processedValue = valueProcessor.apply(originalValue.trim());
                    propsMap.put(secretKey, processedValue);
                    hasChanged = true;
                } catch (Exception e) {
                    log.warn("Failed to process sensitive property '{}'. Skipping. Error: {}", secretKey, e.getMessage());
                }
            }
            return hasChanged ? JsonUtils.toJson(propsMap) : null;
        } catch (Exception e) {
            log.info("An error occurred while processing properties, ignoring. msg:{}", ExceptionUtils.getRootCauseMessage(e), e);
            return null;
        }
    }

    private static boolean isLikelyEncrypted(String value) {
        if (StringUtils.isBlank(value)) {
            return false;
        } else {
            return value.matches("^[a-fA-F0-9]+$");
        }
    }

    private static Map<String, String> parsePropsJson(String catalogProps) {
        try {
            Map<String, String> rawMap = OBJECT_MAPPER.readValue(catalogProps, MAP_TYPE_REFERENCE);
            Map<String, String> trimmedMap = new HashMap<>();
            for (Map.Entry<String, String> entry : rawMap.entrySet()) {
                String key = entry.getKey();
                String value = entry.getValue();
                trimmedMap.put(key, value != null ? value.trim() : null);
            }
            return trimmedMap;
        } catch (Exception e) {
            throw new RuntimeException("Failed to parse catalogProps. catalogProps:" + catalogProps, e);
        }
    }
}
