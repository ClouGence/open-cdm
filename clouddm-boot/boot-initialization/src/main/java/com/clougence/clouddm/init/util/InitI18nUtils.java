package com.clougence.clouddm.init.util;

import java.util.Locale;

import org.springframework.context.i18n.LocaleContext;
import org.springframework.context.i18n.LocaleContextHolder;

import com.clougence.clouddm.init.constant.I18nInitFieldKeys;
import com.clougence.utils.StringUtils;
import com.clougence.utils.i18n.I18nUtils;

public final class InitI18nUtils {

    private static final I18nUtils I18N_UTILS     = I18nUtils.initI18n();
    private static final String    DEFAULT_LOCALE = "zh_CN";

    static {
        I18N_UTILS.setName("BOOT_INITIALIZATION");
        I18N_UTILS.setDefaultI18nKey(DEFAULT_LOCALE);
        I18N_UTILS.loadResources(I18nInitFieldKeys.class);
    }

    private InitI18nUtils(){
    }

    public static Locale getLocale() {
        LocaleContext localeContext = LocaleContextHolder.getLocaleContext();
        if (localeContext != null && localeContext.getLocale() != null) {
            return localeContext.getLocale();
        }
        if (StringUtils.isNotBlank(DEFAULT_LOCALE)) {
            return I18nUtils.getLocale(DEFAULT_LOCALE);
        }
        return Locale.getDefault();
    }

    public static String getMessage(String key) {
        return I18N_UTILS.getMessage(key, new Object[0], getLocale());
    }
}
