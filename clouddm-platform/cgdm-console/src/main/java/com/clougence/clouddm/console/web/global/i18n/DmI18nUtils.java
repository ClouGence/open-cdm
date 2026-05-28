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
package com.clougence.clouddm.console.web.global.i18n;

import java.io.InputStream;
import java.util.*;

import org.springframework.context.i18n.LocaleContext;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.ResourceLoader;

import com.clougence.clouddm.platform.dal.i18n.I18nDaoKeys;
import com.clougence.clouddm.platform.plugin.PluginManager;
import com.clougence.clouddm.sdk.security.auth.def.SecAuthI18nKeys;
import com.clougence.clouddm.sdk.security.auth.def.SecSysRole;
import com.clougence.utils.StringUtils;
import com.clougence.utils.i18n.I18nUtils;

import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

/**
 * @author bucketli 2019-12-30 16:23
 * @since 1.1.3
 */
@Slf4j
public class DmI18nUtils {

    protected static final I18nUtils I18N_UTILS    = PluginManager.globalI18nUtils();
    @Setter
    private static String            defaultLocale = "zh_CN";

    static {
        I18N_UTILS.setName("DM");
        I18N_UTILS.setDefaultI18nKey(defaultLocale);
        LocaleContextHolder.setLocale(I18nUtils.getLocale(defaultLocale));

        // loadResources — DM resources
        I18N_UTILS.loadResources("/i18n/dsconfig");
        I18N_UTILS.loadResources("/i18n/request-label");
        I18N_UTILS.loadResources("/i18n/desktop");
        I18N_UTILS.loadResources("/i18n/validation");
        I18N_UTILS.loadResources("/i18n/sec-rule-message");
        I18N_UTILS.loadResources("/i18n/mcp");
        I18N_UTILS.loadResources(I18nDmLabelKeys.class);
        I18N_UTILS.loadResources(I18nDmMsgKeys.class);
        I18N_UTILS.loadResources(I18nDaoKeys.class);
        I18N_UTILS.loadResources(UiMenus18nKey.class);
        I18N_UTILS.loadResources(SecAuthI18nKeys.class);

        // loadResources — RDP resources (merged from DmI18nUtils)
        I18N_UTILS.loadResources("/rdp/static/i18n/validation");
        I18N_UTILS.loadResources("/rdp/static/i18n/request-label");
        I18N_UTILS.loadResources("/rdp/static/i18n/audit-detail-label");
        I18N_UTILS.loadResources(I18nRdpMsgKeys.class);
        I18N_UTILS.loadResources(I18nDsConfigMsgKeys.class);
        I18N_UTILS.loadResources(I18nUserConfigMsgKeys.class);
        I18N_UTILS.loadResources(I18nRdpLabelKeys.class);
        I18N_UTILS.loadResources(SecSysRole.class);

        // check loadResources
        I18N_UTILS.checkDifferenceOnWarn("zh_CN");
        I18N_UTILS.checkDifferenceOnWarn("en_US");
    }

    public static I18nUtils getInstance() { return I18N_UTILS; }

    public static Map<String, Properties> fetchMessageKeys() {
        Map<String, Properties> result = new LinkedHashMap<>();
        for (String f : I18N_UTILS.getI18nSources()) {
            String defaultPropFile = f + ".properties";
            ResourceLoader resourceLoader = new DefaultResourceLoader(I18N_UTILS.getI18nSourceLoader(f));
            try (InputStream srcStream = resourceLoader.getResource(defaultPropFile).getInputStream()) {
                Properties properties = new Properties();
                properties.load(srcStream);
                result.put(f, properties);
            } catch (Exception e) {
                if (StringUtils.endsWith(f, "/desktop")) {
                    continue;
                }

                log.error("open i18n file failed, file :{}", defaultPropFile);
                throw new RuntimeException(e);
            }
        }
        return result;
    }

    public static Locale getLocale() {
        LocaleContext localeContext = LocaleContextHolder.getLocaleContext();
        Locale locale;

        if (localeContext != null) {
            locale = localeContext.getLocale();
        } else if (StringUtils.isNotBlank(defaultLocale)) {
            locale = I18nUtils.getLocale(defaultLocale);
        } else {
            locale = Locale.getDefault();
        }

        return locale;
    }

    public static String getMessage(String key) {
        return getMessage(key, getLocale());
    }

    public static String getMessage(String key, Object... args) {
        return getMessage(key, getLocale(), args);
    }

    public static String getMessage(Enum<?> key, Object... args) {
        return getMessage(key.name(), getLocale(), args);
    }

    public static String getMessage(String key, Locale locale, Object... args) {
        return I18N_UTILS.getMessage(key, args, locale);
    }

    // ==================== bridge methods for DmI18nUtils callers ====================

    public static void loadResources(String... i18nResources) {
        I18N_UTILS.loadResources(i18nResources);
    }

    public static void loadResources(ClassLoader resourceLoader, String... i18nResources) {
        I18N_UTILS.loadResources(resourceLoader, i18nResources);
    }

    public static void loadResources(Class<?>... i18nResources) {
        I18N_UTILS.loadResources(i18nResources);
    }

    public static Set<Class<?>> findI18nTypes() {
        return I18N_UTILS.getI18nTypes();
    }

    public static Map<String, String> genHeaderMap() {
        Map<String, String> headerMap = new java.util.HashMap<>();
        headerMap.put("Accept-Language", getLocale().getLanguage());
        return headerMap;
    }
}
