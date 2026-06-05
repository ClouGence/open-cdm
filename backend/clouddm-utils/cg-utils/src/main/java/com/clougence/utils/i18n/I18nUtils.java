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
package com.clougence.utils.i18n;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.text.MessageFormat;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.function.Function;

import com.clougence.utils.ClassUtils;
import com.clougence.utils.ExceptionUtils;
import com.clougence.utils.ResourcesUtils;
import com.clougence.utils.StringUtils;
import com.clougence.utils.ref.LinkedCaseInsensitiveMap;
import com.clougence.utils.token.GenericTokenParser;
import com.clougence.utils.token.TokenHandlerHelper;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

/**
 * i18n
 * @author 赵永春 (zyc@hasor.net)
 */
@Slf4j
public class I18nUtils {

    public static final I18nUtils            DEFAULT     = new I18nUtils();
    private static final Map<String, Locale> LOCAL_CACHE = new LinkedCaseInsensitiveMap<>();

    static {
        for (Locale locale : Locale.getAvailableLocales()) {
            String i18nKey = locale.getLanguage() + "_" + locale.getCountry();
            LOCAL_CACHE.put(i18nKey, locale);
        }
    }

    public static I18nUtils initI18n() {
        return new I18nUtils();
    }

    public static I18nUtils initI18n(I18nUtils parent) {
        I18nUtils utils = new I18nUtils();
        utils.parent = Objects.requireNonNull(parent, "parent is null.");
        return utils;
    }

    public static I18nUtils initI18n(String... i18nResources) {
        I18nUtils utils = new I18nUtils();
        utils.loadResources(i18nResources);
        return utils;
    }

    public static I18nUtils initI18n(ClassLoader resourceLoader, String... i18nResources) {
        I18nUtils utils = new I18nUtils();
        utils.loadResources(resourceLoader, i18nResources);
        return utils;
    }

    public static I18nUtils initI18n(Class<?>... i18nResources) {
        I18nUtils utils = new I18nUtils();
        utils.loadResources(i18nResources);
        return utils;
    }

    public static Locale getLocale(String i18nKey) {
        return LOCAL_CACHE.get(i18nKey);
    }

    public static Locale getLocale(String language, String country) {
        return LOCAL_CACHE.computeIfAbsent(language + "_" + country, s -> new Locale(language, country));
    }

    public static String toI18nKey(String language, String country) {
        if (StringUtils.isNotBlank(language) && StringUtils.isNotBlank(country)) {
            return language + "_" + country;
        } else if (StringUtils.isNotBlank(country)) {
            return country;
        } else {
            return language;
        }
    }

    public static String toI18nKey(Locale locale) {
        if (locale == null) {
            return null;
        }

        if (StringUtils.isNotBlank(locale.getLanguage()) && StringUtils.isNotBlank(locale.getCountry())) {
            return locale.getLanguage() + "_" + locale.getCountry();
        } else if (StringUtils.isNotBlank(locale.getCountry())) {
            return locale.getCountry();
        } else {
            return locale.getLanguage();
        }
    }

    public static interface I18nMessageSource {

        String getMessage(String code, Object[] args, Locale locale);
    }

    private static class I18nMessageSourceImpl implements I18nMessageSource {

        private final Map<String, String>              defaultDictionary = new ConcurrentHashMap<>();
        private final Map<String, Map<String, String>> i18nDictionary    = new ConcurrentHashMap<>();

        @Override
        public String getMessage(String code, Object[] args, Locale locale) {
            if (locale == null) {
                locale = Locale.getDefault();
            }

            String i18nKey = toI18nKey(locale);

            Map<String, String> localeMap = this.i18nDictionary.get(code);
            if (localeMap == null || !localeMap.containsKey(i18nKey)) {
                if (this.defaultDictionary.containsKey(code)) {
                    return this.defaultDictionary.get(code);
                }
                return null;
            }

            return localeMap.get(i18nKey);
        }

        public void putDictionary(String code, String message) {
            this.defaultDictionary.put(code, message);
        }

        public void putDictionary(String code, Locale locale, String message) {
            if (locale == null) {
                locale = Locale.getDefault();
            }

            if (!this.i18nDictionary.containsKey(code)) {
                this.i18nDictionary.put(code, new ConcurrentHashMap<>());
            }

            String i18nKey = toI18nKey(locale);
            this.i18nDictionary.get(code).put(i18nKey, message);
        }
    }

    private static class VariablesSourceImpl extends HashMap<String, String> implements Function<String, String> {

        @Override
        public String apply(String s) {
            return super.getOrDefault(s, s);
        }
    }

    private I18nUtils                      parent;
    private final Set<String>              lockedKeys;
    private String                         name;
    private String                         defaultI18nKey;
    private final I18nMessageSource        messageSource;
    private final Function<String, String> variablesSource;
    private final Map<String, ClassLoader> i18nSource;
    private final Set<Class<?>>            i18nSourceOfTypes;
    private final Set<String>              i18nLoaded;

    private I18nUtils(){
        this.defaultI18nKey = toI18nKey(Locale.getDefault());
        this.messageSource = new I18nMessageSourceImpl();
        this.variablesSource = new VariablesSourceImpl();
        this.i18nSource = new LinkedHashMap<>();
        this.i18nLoaded = new CopyOnWriteArraySet<>();
        this.i18nSourceOfTypes = new LinkedHashSet<>();
        this.lockedKeys = new CopyOnWriteArraySet<>();
    }

    public I18nUtils(I18nMessageSource messageSource){
        this.defaultI18nKey = toI18nKey(Locale.getDefault());
        this.messageSource = Objects.requireNonNull(messageSource, "messageSource is null.");
        this.variablesSource = new VariablesSourceImpl();
        this.i18nSource = new LinkedHashMap<>();
        this.i18nLoaded = new CopyOnWriteArraySet<>();
        this.i18nSourceOfTypes = new LinkedHashSet<>();
        this.lockedKeys = new CopyOnWriteArraySet<>();
    }

    public I18nUtils(I18nMessageSource messageSource, Function<String, String> variablesSource){
        this.defaultI18nKey = toI18nKey(Locale.getDefault());
        this.messageSource = Objects.requireNonNull(messageSource, "messageSource is null.");
        this.variablesSource = variablesSource == null ? new VariablesSourceImpl() : variablesSource;
        this.i18nSource = new LinkedHashMap<>();
        this.i18nLoaded = new CopyOnWriteArraySet<>();
        this.i18nSourceOfTypes = new LinkedHashSet<>();
        this.lockedKeys = new CopyOnWriteArraySet<>();
    }

    public I18nUtils getParent() { return parent; }

    public void setParent(I18nUtils parent) { this.parent = parent; }

    /**
     * Lock the given keys at this layer. When a descendant calls {@link #loadResources}
     * and encounters a locked key, that entry is silently skipped — only the layer that
     * declared the lock may store a value for it. Callers retrieve the value via the
     * normal {@link #getMessage} parent-fallback chain.
     */
    public void lockKeys(String... keys) {
        for (String key : keys) {
            if (StringUtils.isNotBlank(key)) {
                this.lockedKeys.add(key);
            }
        }
    }

    public Set<String> getLockedKeys() { return Collections.unmodifiableSet(this.lockedKeys); }

    private I18nUtils findLockedLayer(String key) {
        // Collect ancestors from nearest to root
        List<I18nUtils> chain = new ArrayList<>();
        I18nUtils cursor = this.parent;
        while (cursor != null) {
            chain.add(cursor);
            cursor = cursor.parent;
        }
        // Iterate from root (last element) downward — topmost lock wins
        for (int i = chain.size() - 1; i >= 0; i--) {
            if (chain.get(i).lockedKeys.contains(key)) {
                return chain.get(i);
            }
        }
        return null;
    }

    public String getName() { return name; }

    public void setName(String name) { this.name = name; }

    public String getDefaultI18nKey() { return defaultI18nKey; }

    public void setDefaultI18nKey(String defaultI18nKey) { this.defaultI18nKey = defaultI18nKey; }

    public void setDefaultI18nKey(Locale defaultLocale) { this.defaultI18nKey = toI18nKey(defaultLocale); }

    public Set<String> getI18nSources() { return Collections.unmodifiableSet(this.i18nSource.keySet()); }

    public Set<Class<?>> getI18nTypes() { return Collections.unmodifiableSet(this.i18nSourceOfTypes); }

    public ClassLoader getI18nSourceLoader(String i18nResource) {
        return this.i18nSource.get(i18nResource);
    }

    public void loadResources(String... i18nResources) {
        ClassLoader classLoader = ClassUtils.getClassLoader(Thread.currentThread().getContextClassLoader());
        loadResources(classLoader, i18nResources);
    }

    @SneakyThrows
    public void loadResources(ClassLoader resourceLoader, String... i18nResources) {
        if (!(this.messageSource instanceof I18nMessageSourceImpl)) {
            throw new UnsupportedOperationException("I18nMessageSource is external.");
        }

        resourceLoader = resourceLoader == null ? ClassUtils.getClassLoader(Thread.currentThread().getContextClassLoader()) : resourceLoader;

        for (String i18n : i18nResources) {
            if (!this.i18nSource.containsKey(i18n)) {
                this.i18nSource.put(i18n, resourceLoader);

                String i18nResource = i18n + ".properties";
                try (InputStream stream = ResourcesUtils.getResourceAsStream(resourceLoader, i18nResource)) {
                    if (stream != null) {
                        Properties properties = new Properties();
                        properties.load(new InputStreamReader(stream, StandardCharsets.UTF_8));
                        properties.forEach((key, value) -> {
                            String k = key.toString();
                            // Keys locked at an ancestor must be loaded by the owning layer — skip here.
                            if (findLockedLayer(k) == null) {
                                ((I18nMessageSourceImpl) this.messageSource).putDictionary(k, value.toString());
                            }
                        });
                    }
                }
            }
        }

        this.i18nLoaded.clear();
    }

    public void loadResources(Class<?>... i18nResources) {
        for (Class<?> i18nType : i18nResources) {
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
                if (this.i18nSourceOfTypes.contains(type)) {
                    continue;
                }

                this.i18nSourceOfTypes.add(type);
                I18nResource i18nResource = type.getAnnotation(I18nResource.class);
                if (i18nResource != null) {
                    this.loadResources(type.getClassLoader(), i18nResource.value());
                }
            }
        }
        this.i18nLoaded.clear();
    }

    public void addVariables(String varName, String varValue) {
        if (this.variablesSource instanceof VariablesSourceImpl) {
            ((VariablesSourceImpl) this.variablesSource).put(varName, varValue);
        } else {
            throw new UnsupportedOperationException("variablesSource is external.");
        }
    }

    public void putVariables(Map<String, String> variables) {
        if (variables != null && this.variablesSource instanceof VariablesSourceImpl) {
            ((VariablesSourceImpl) this.variablesSource).putAll(variables);
        } else {
            throw new UnsupportedOperationException("variablesSource is external.");
        }
    }

    public String getMessage(String code) {
        return getMessage(code, null, this.defaultI18nKey);
    }

    public String getMessage(String code, Object[] args) {
        return getMessage(code, args, this.defaultI18nKey);
    }

    public String getMessage(String code, Object[] args, String i18nLocal) {
        if (StringUtils.isBlank(code)) {
            return code;
        }

        Locale locale;
        if (LOCAL_CACHE.containsKey(i18nLocal)) {
            locale = LOCAL_CACHE.get(i18nLocal);
        } else {
            locale = Locale.getDefault();
        }
        return this.getMessage(code, args, locale);
    }

    @SneakyThrows
    public String getMessage(String code, Object[] args, Locale locale) {
        String i18nKey = toI18nKey(locale);

        if (!this.i18nLoaded.contains(i18nKey)) {
            synchronized (this) {
                if (!this.i18nLoaded.contains(i18nKey)) {
                    for (String i18nResourcePath : this.i18nSource.keySet()) {
                        String i18nResource = i18nResourcePath + "_" + i18nKey + ".properties";
                        ClassLoader i18nLoader = this.i18nSource.get(i18nResourcePath);

                        try (InputStream stream = ResourcesUtils.getResourceAsStream(i18nLoader, i18nResource)) {
                            if (stream != null) {
                                Properties properties = new Properties();
                                properties.load(new InputStreamReader(stream, StandardCharsets.UTF_8));
                                if (this.messageSource instanceof I18nMessageSourceImpl) {
                                    properties.forEach((key, value) -> ((I18nMessageSourceImpl) this.messageSource).putDictionary(key.toString(), locale, value.toString()));
                                }
                            }
                        }
                    }
                    this.i18nLoaded.add(i18nKey);
                }
            }
        }

        String message = this.messageSource.getMessage(code, args, locale);
        if (message == null) {
            if (this.parent != null) {
                return this.parent.getMessage(code, args, locale);
            }
            return code;
        }

        message = resolveMessageArgs(message);
        if (args == null || args.length == 0) {
            return message;
        }

        return MessageFormat.format(message, args);
    }

    private String resolveMessageArgs(String msg) {
        return new GenericTokenParser(new TokenHandlerHelper("${", "}", content -> {
            String varKey = content;
            String varDefault = "";
            int defaultIndexOf = content.indexOf(":");
            if (defaultIndexOf != -1) {
                varDefault = content.substring(defaultIndexOf + 1);
                varKey = content.substring(0, defaultIndexOf);
            }

            String var = resolveArg(varKey);
            if (StringUtils.isBlank(var) && StringUtils.isNotBlank(varDefault)) {
                var = varDefault;
            }

            if (varKey.equalsIgnoreCase(var)) {
                return varKey;
            } else {
                return var;
            }
        })).parse(msg);
    }

    protected String resolveArg(String argName) {
        if (this.variablesSource != null) {
            return this.variablesSource.apply(argName);
        } else {
            return argName;
        }
    }

    public void checkDifferenceOnThrow(String localName) {
        this.checkDifference(localName, true);
    }

    public void checkDifferenceOnWarn(String localName) {
        this.checkDifference(localName, false);
    }

    private void checkDifference(String localName, boolean throwError) {
        for (String resource : this.getI18nSources()) {
            ClassLoader classLoader = this.i18nSource.get(resource);
            checkDifference(resource, classLoader, localName, throwError);
        }
    }

    private void checkDifference(String resource, ClassLoader classLoader, String localName, boolean throwError) {
        if (StringUtils.isBlank(localName)) {
            throw new IllegalArgumentException("localName is null.");
        }
        String srcPropFile = resource + ".properties";
        String diffPropFile = resource + "_" + localName + ".properties";

        checkDifference(classLoader, srcPropFile, diffPropFile, throwError);
    }

    protected void checkDifference(ClassLoader loader, String srcFile, String dstFile, boolean throwError) {
        try (InputStream srcStream = ResourcesUtils.getResourceAsStream(loader, srcFile); InputStream dstStream = ResourcesUtils.getResourceAsStream(loader, dstFile)) {
            Properties srcProps = new Properties();
            if (srcStream != null) {
                srcProps.load(srcStream);
            }

            Properties dstProps = new Properties();
            if (dstStream != null) {
                dstProps.load(dstStream);
            }

            for (String key : srcProps.stringPropertyNames()) {
                String msgUs = dstProps.getProperty(key, null);
                if (msgUs == null) {
                    String name = StringUtils.isBlank(this.name) ? "[i18n] " : ("[" + this.name + " i18n] ");
                    if (throwError) {
                        throw new IllegalStateException(name + dstFile + " not exist " + key + " entry.");
                    } else {
                        log.warn(name + dstFile + " not exist " + key + " entry.");
                    }
                }
            }
        } catch (IOException e) {
            log.error("open i18n file " + srcFile + "or" + dstFile + " failed.", e);
            throw ExceptionUtils.toRuntime(e);
        }
    }

}
