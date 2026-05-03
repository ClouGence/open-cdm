package com.clougence.rdp.util;

import static org.apache.http.HttpHeaders.ACCEPT_LANGUAGE;

import java.io.InputStream;
import java.util.*;

import org.springframework.context.i18n.LocaleContext;
import org.springframework.context.i18n.LocaleContextHolder;

import com.clougence.clouddm.sdk.security.auth.def.SecSysRole;
import com.clougence.rdp.constant.*;
import com.clougence.utils.ResourcesUtils;
import com.clougence.utils.StringUtils;
import com.clougence.utils.i18n.I18nUtils;

import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

/**
 * @author bucketli 2019-12-30 16:23
 * @since 1.1.3
 */
@Slf4j
public class RdpI18nUtils {

    public static final I18nUtils I18N_UTILS    = I18nUtils.initI18n();

    @Setter
    private static String         defaultLocale = "zh_CN";

    static {
        I18N_UTILS.setName("RDP");
        I18N_UTILS.setDefaultI18nKey(defaultLocale);
        LocaleContextHolder.setLocale(I18nUtils.getLocale(defaultLocale));

        // loadResources
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

    public static void loadResources(String... i18nResources) {
        I18N_UTILS.loadResources(i18nResources);
    }

    public static void loadResources(ClassLoader resourceLoader, String... i18nResources) {
        I18N_UTILS.loadResources(resourceLoader, i18nResources);
    }

    public static void loadResources(Class<?>... i18nResources) {
        I18N_UTILS.loadResources(i18nResources);
    }

    public static Map<String, Properties> fetchMessageKeys() {
        Map<String, Properties> result = new LinkedHashMap<>();
        for (String f : I18N_UTILS.getI18nSources()) {
            String defaultPropFile = f + ".properties";
            ClassLoader i18nSourceLoader = I18N_UTILS.getI18nSourceLoader(f);
            try (InputStream srcStream = ResourcesUtils.getResourceAsStream(i18nSourceLoader, defaultPropFile)) {
                Properties properties = new Properties();
                properties.load(srcStream);
                result.put(f, properties);
            } catch (Exception e) {
                log.error("open i18n file failed, file :{}", defaultPropFile);
                throw new RuntimeException(e);
            }
        }
        return result;
    }

    public static Set<Class<?>> findI18nTypes() {
        return I18N_UTILS.getI18nTypes();
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

    public static String getMessage(ConsoleErrorCode errorCode) {
        return getMessage(errorCode.name(), getLocale());
    }

    public static String getMessage(ConsoleErrorCode errorCode, Object... args) {
        return getMessage(errorCode.name(), getLocale(), args);
    }

    public static String getMessage(ConsoleErrorCode errorCode, Locale locale) {
        return getMessage(errorCode.name(), locale);
    }

    public static String getMessage(ConsoleErrorCode errorCode, Locale locale, Object... args) {
        return getMessage(errorCode.name(), locale, args);
    }

    public static String getMessage(String key) {
        return getMessage(key, getLocale());
    }

    public static String getMessage(String key, Object... args) {
        return getMessage(key, getLocale(), args);
    }

    public static String getMessage(String key, Locale locale, Object... args) {
        return I18N_UTILS.getMessage(key, args, locale);
    }

    public static Map<String, String> genHeaderMap() {
        Map<String, String> headerMap = new HashMap<>();
        headerMap.put(ACCEPT_LANGUAGE, getLocale().getLanguage());
        return headerMap;
    }
}
