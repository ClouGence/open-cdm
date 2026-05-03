package com.clougence.rdp.util;

import java.util.Locale;

/**
 * @author bucketli 2021/11/16 17:08:09
 */
public class RdpLocal {

    private static final ThreadLocal<Locale> localLanguage = new ThreadLocal<>();

    public static Locale getLocal() { return localLanguage.get(); }

    public static void setLocal(Locale locale) {
        if (localLanguage.get() != null) {
            localLanguage.remove();
        }
        if (locale != null) {
            localLanguage.set(locale);
        }
    }
}
