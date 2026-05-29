/*
 * Copyright 2026 杭州开云集致科技有限公司
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.clougence.clouddm.console.web.global.init;

import java.lang.reflect.Field;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.clougence.clouddm.api.common.boot.UnifiedPostConstruct;
import com.clougence.clouddm.api.common.exception.ConsoleErrorCode;
import com.clougence.clouddm.base.metadata.ds.ConfigI18nKey;
import com.clougence.clouddm.base.metadata.rdp.enumeration.SecurityType;
import com.clougence.clouddm.console.web.constants.DsResElement;
import com.clougence.clouddm.console.web.global.i18n.*;
import com.clougence.clouddm.sdk.security.auth.def.SecAuthI18nKeys;
import com.clougence.utils.ClassUtils;
import com.clougence.utils.StringUtils;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class DmI18nKeyDiffCheck implements UnifiedPostConstruct {

    @Override
    public void init() throws Exception {
        checkI18nFileByConfigKey();
        checkConfigKeyByI18nFile();
    }

    @Override
    public void stop() {

    }

    public void checkI18nFileByConfigKey() {
        log.info("[DM i18n key has message] start check key in code has i18n message.");
        AtomicInteger count = new AtomicInteger(0);
        allKey().parallelStream().forEach(key -> {
            String i18nMessage = DmI18nUtils.getMessage(key);
            if (StringUtils.equals(i18nMessage, key)) {
                count.incrementAndGet();
                log.warn("[DM i18n] key:{} exist, but message miss", key);
            }
        });
        log.info("[DM i18n key has message] total diff count:{}", count.get());
    }

    public void checkConfigKeyByI18nFile() {
        log.info("[DM i18n message is used] start check i18n message is used.");
        Set<String> allKeys = allKey();
        AtomicInteger count = new AtomicInteger(0);
        DmI18nUtils.fetchMessageKeys().forEach((resource, properties) -> {
            if (resource.endsWith("/validation") || resource.endsWith("/request-label") || resource.endsWith("/ui-menus")
                || resource.endsWith("/sec-rule-message") /*|| resource.endsWith("/label")*/) {
                return;
            }

            properties.stringPropertyNames().parallelStream().forEach(key -> {
                if (!allKeys.contains(key)) {
                    count.incrementAndGet();
                    log.warn("[DM i18n] key:{} not exist, but message exist", key);
                }
            });
        });
        log.info("[DM i18n message is used] total diff count:{}", count.get());
    }

    private Set<String> allKey() {
        HashSet<String> result = new HashSet<>();
        result.addAll(Arrays.stream(SecurityType.values()).map(SecurityType::getI18nKey).collect(Collectors.toList()));
        result.addAll(loadKeys(ConsoleErrorCode.class));
        result.addAll(loadKeys(I18nRdpMsgKeys.class));
        result.addAll(loadKeys(I18nDsConfigMsgKeys.class));
        result.addAll(loadKeys(I18nUserConfigMsgKeys.class));
        result.addAll(loadKeys(I18nRdpLabelKeys.class));

        result.addAll(Arrays.stream(ConfigI18nKey.values()).map(Enum::name).collect(Collectors.toList()));
        result.addAll(Arrays.stream(DsResElement.values()).map(Enum::name).collect(Collectors.toList()));
        result.addAll(Arrays.stream(I18nDmMsgKeys.values()).map(Enum::name).collect(Collectors.toList()));
        result.addAll(Arrays.stream(I18nDmLabelKeys.values()).map(Enum::name).collect(Collectors.toList()));

        result.addAll(loadKeys(SecAuthI18nKeys.class));
        for (Class<?> i18nType : DmI18nUtils.getInstance().getI18nTypes()) {
            if (i18nType.isEnum()) {
                result.addAll(Arrays.stream(((Class<Enum<?>>) i18nType).getEnumConstants()).map(Enum::name).collect(Collectors.toList()));
            } else if (i18nType.isInterface()) {
                result.addAll(loadKeys(i18nType));
            }
        }

        // special skip
        result.remove("UI_LEAF_TITLE_RDB_");
        result.remove("UI_LEAF_TITLE_");
        return result;
    }

    private Set<String> loadKeys(Class<?> i18nType) {
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
