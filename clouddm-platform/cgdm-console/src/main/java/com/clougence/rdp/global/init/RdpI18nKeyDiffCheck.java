package com.clougence.rdp.global.init;

import java.lang.reflect.Field;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.clougence.clouddm.api.common.boot.UnifiedPostConstruct;
import com.clougence.clouddm.api.common.boot.UnifiedPostConstructOrder;
import com.clougence.clouddm.base.metadata.rdp.enumeration.SecurityType;
import com.clougence.rdp.constant.*;
import com.clougence.rdp.util.RdpI18nUtils;
import com.clougence.utils.ClassUtils;
import com.clougence.utils.StringUtils;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@UnifiedPostConstructOrder(1)
public class RdpI18nKeyDiffCheck implements UnifiedPostConstruct {

    @Override
    public void init() throws Exception {
        checkI18nFileByConfigKey();
        checkConfigKeyByI18nFile();
    }

    @Override
    public void stop() {

    }

    public void checkI18nFileByConfigKey() {
        log.info("[RDP i18n key has message] start check key in code has i18n message.");
        AtomicInteger count = new AtomicInteger(0);
        allKey().parallelStream().forEach(key -> {
            String i18nMessage = RdpI18nUtils.getMessage(key);
            if (StringUtils.equals(i18nMessage, key)) {
                count.incrementAndGet();
                log.warn("[RDP i18n] key:{} exist, but message miss", key);
            }
        });
        log.info("[RDP i18n key has message] total diff count:{}", count.get());
    }

    public void checkConfigKeyByI18nFile() {
        log.info("[RDP i18n message is used] start check i18n message is used.");
        Set<String> allKeys = allKey();
        AtomicInteger count = new AtomicInteger(0);
        RdpI18nUtils.fetchMessageKeys().forEach((resource, properties) -> {
            if (resource.endsWith("/validation") || resource.endsWith("/request-label") || resource.endsWith("/label") || resource.endsWith("/audit-detail-label")) {
                return;
            }

            properties.stringPropertyNames().parallelStream().forEach(key -> {
                if (!allKeys.contains(key)) {
                    count.incrementAndGet();
                    log.warn("[RDP i18n] key:{} not exist, but message exist", key);
                }
            });
        });
        log.info("[RDP i18n message is used] total diff count:{}", count.get());
    }

    private Set<String> allKey() {
        HashSet<String> result = new HashSet<>();
        result.addAll(Arrays.stream(SecurityType.values()).map(SecurityType::getI18nKey).collect(Collectors.toList()));
        result.addAll(loadKeys(ConsoleErrorCode.class));
        result.addAll(loadKeys(I18nRdpMsgKeys.class));
        result.addAll(loadKeys(I18nDsConfigMsgKeys.class));
        result.addAll(loadKeys(I18nUserConfigMsgKeys.class));
        result.addAll(loadKeys(I18nRdpLabelKeys.class));
        for (Class<?> t : RdpI18nUtils.findI18nTypes()) {
            result.addAll(loadKeys(t));
        }
        return result;
    }

    private Set<String> loadKeys(Class<?> i18nType) {
        if (i18nType.isEnum()) {
            return Arrays.stream(((Class<Enum<?>>) i18nType).getEnumConstants()).map(Enum::name).collect(Collectors.toSet());
        }

        Set<Class<?>> loaded = new HashSet<>();
        // deep parents
        List<Class<?>> interfaces = ClassUtils.getAllInterfaces(i18nType);
        List<Class<?>> superclasses = ClassUtils.getAllSuperclasses(i18nType);
        Collections.reverse(interfaces);
        Collections.reverse(superclasses);

        // all load type
        List<Class<?>> loadTypes = new ArrayList<>();
        loadTypes.addAll(interfaces);
        loadTypes.addAll(superclasses);
        loadTypes.add(i18nType);

        for (Class<?> type : loadTypes) {
            if (loaded.contains(type)) {
                continue;
            }

            loaded.add(type);
        }

        Set<String> keys = new HashSet<>();
        for (Class<?> type : loaded) {
            for (Field f : type.getFields()) {
                try {
                    keys.add((String) f.get(null));
                } catch (Exception ignored) {

                }
            }
        }
        return keys;
    }
}
