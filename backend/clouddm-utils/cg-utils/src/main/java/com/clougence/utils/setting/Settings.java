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
package com.clougence.utils.setting;
import java.io.IOException;
import java.util.Date;

/**
 * Configuration settings.
 * Environment variables are loaded in the following order; later sources override duplicate values from earlier sources.
 * 1st, System.getProperties()
 * 2nd, System.getenv()
 * 3rd, profile "hasor.environmentVar"
 * 4th, incoming configuration
 * @author 赵永春 (zyc@hasor.net)
 * @version : 2013-4-23
 */
public interface Settings {
    String DefaultNameSpace = "https://www.hasor.net/sechma/main";

    /** @return Parsed Namespace List */
    String[] getSettingArray();

    /** Getting a Settings interface object in a given named space */
    Settings getSettings(String namespace);

    /** If the configured values use expressions, refresh calculates them. */
    default void refresh() throws IOException {
    }

    /**
     * Sets the parameters. If there are multiple values, the values are overwritten. (Use default naming space: DefaultNameSpace)
     * @see #DefaultNameSpace
     */
    default void setSetting(String key, Object value) {
        if (value instanceof SettingNode) {
            this.setSetting(key, value, ((SettingNode) value).getSpace());
        } else {
            this.setSetting(key, value, DefaultNameSpace);
        }
    }

    /** Set parameters, overwrite if multiple values appear */
    void setSetting(String key, Object value, String namespace);

    /** Remove multiple values from the entire configuration (all namespaces) */
    void removeSetting(String s);

    /** Remove multiple values of the entire configuration item */
    void removeSetting(String key, String namespace);

    /** Add parameters, and add an item if the parameter name is the same */
    default void addSetting(String key, Object value) {
        this.addSetting(key, value, DefaultNameSpace);
    }

    /** Add parameters, and add an item if the parameter name is the same */
    void addSetting(String key, Object var, String namespace);

    /** Calculate strings and replace information such as environment variables */
    String evalSetting(String evalString);

    /** Parses a global configuration parameter and returns a {@link Character} object. */
    Character getChar(String name);

    /** Parses a global configuration parameter and returns a {@link Character} object. The second parameter is the default value. */
    Character getChar(String name, Character defaultValue);

    /** Parses a global configuration parameter and returns a {@link String} object. */
    String getString(String name);

    /** Parses a global configuration parameter and returns a {@link String} object. The second parameter is the default value. */
    String getString(String name, String defaultValue);

    /** Parses a global configuration parameter and returns a {@link Boolean} object. */
    Boolean getBoolean(String name);

    /** Parses a global configuration parameter and returns a {@link Boolean} object. The second parameter is the default value. */
    Boolean getBoolean(String name, Boolean defaultValue);

    /** Parses a global configuration parameter and returns a {@link Short} object. */
    Short getShort(String name);

    /** Parses a global configuration parameter and returns a {@link Short} object. The second parameter is the default value. */
    Short getShort(String name, Short defaultValue);

    /** Parses a global configuration parameter and returns an {@link Integer} object. */
    Integer getInteger(String name);

    /** Parses a global configuration parameter and returns an {@link Integer} object. The second parameter is the default value. */
    Integer getInteger(String name, Integer defaultValue);

    /** Parses a global configuration parameter and returns a {@link Long} object. */
    Long getLong(String name);

    /** Parses a global configuration parameter and returns a {@link Long} object. The second parameter is the default value. */
    Long getLong(String name, Long defaultValue);

    /** Parses a global configuration parameter and returns a {@link Float} object. */
    Float getFloat(String name);

    /** Parses a global configuration parameter and returns a {@link Float} object. The second parameter is the default value. */
    Float getFloat(String name, Float defaultValue);

    /** Parses a global configuration parameter and returns a {@link Double} object. */
    Double getDouble(String name);

    /** Parses a global configuration parameter and returns a {@link Double} object. The second parameter is the default value. */
    Double getDouble(String name, Double defaultValue);

    /** Parses a global configuration parameter and returns a {@link Date} object. */
    Date getDate(String name);

    /** Parses a global configuration parameter and returns a {@link Date} object. The second parameter is the default value. */
    Date getDate(String name, Date defaultValue);

    /** Parses a global configuration parameter and returns a {@link Date} object. The second parameter is the default value. */
    Date getDate(String name, long defaultValue);

    /** Parses a global configuration parameter and returns a {@link Date} object. */
    Date getDate(String name, String format);

    /** Parses a global configuration parameter and returns a {@link Date} object. The third parameter is the default value. */
    Date getDate(String name, String format, Date defaultValue);

    /** Parses a global configuration parameter and returns a {@link Date} object. The third parameter is the default value. */
    Date getDate(String name, String format, long defaultValue);

    /** Parses a global configuration parameter and returns an enum object. */
    <T extends Enum<?>> T getEnum(String name, Class<T> enmType);

    /** Parses a global configuration parameter and returns an enum object. The third parameter is the default value. */
    <T extends Enum<?>> T getEnum(String name, Class<T> enmType, T defaultValue);

    /** Parses a global configuration parameter and returns a file path string without a trailing '/'. */
    String getFilePath(String name);

    /** Parses a global configuration parameter and returns a file path string without a trailing '/'. The second parameter is the default value. */
    String getFilePath(String name, String defaultValue);

    /** Parses a global configuration parameter and returns a directory path string ending with '/'. */
    String getDirectoryPath(String name);

    /** Parses a global configuration parameter and returns a directory path string ending with '/'. The second parameter is the default value. */
    String getDirectoryPath(String name, String defaultValue);

    /** Parses a global configuration parameter and returns a {@link SettingNode} object. */
    SettingNode getNode(String name);

    /** Parses a global configuration parameter and returns {@link Character} objects. */
    Character[] getCharArray(String name);

    /** Parses a global configuration parameter and returns {@link Character} objects. The second parameter is the default value. */
    Character[] getCharArray(String name, Character defaultValue);

    /** Parses a global configuration parameter and returns {@link String} objects. */
    String[] getStringArray(String name);

    /** Parses a global configuration parameter and returns {@link String} objects. The second parameter is the default value. */
    String[] getStringArray(String name, String defaultValue);

    /** Parses a global configuration parameter and returns {@link Boolean} objects. */
    Boolean[] getBooleanArray(String name);

    /** Parses a global configuration parameter and returns {@link Boolean} objects. The second parameter is the default value. */
    Boolean[] getBooleanArray(String name, Boolean defaultValue);

    /** Parses a global configuration parameter and returns {@link Short} objects. */
    Short[] getShortArray(String name);

    /** Parses a global configuration parameter and returns {@link Short} objects. The second parameter is the default value. */
    Short[] getShortArray(String name, Short defaultValue);

    /** Parses a global configuration parameter and returns {@link Integer} objects. */
    Integer[] getIntegerArray(String name);

    /** Parses a global configuration parameter and returns {@link Integer} objects. The second parameter is the default value. */
    Integer[] getIntegerArray(String name, Integer defaultValue);

    /** Parses a global configuration parameter and returns {@link Long} objects. */
    Long[] getLongArray(String name);

    /** Parses a global configuration parameter and returns {@link Long} objects. The second parameter is the default value. */
    Long[] getLongArray(String name, Long defaultValue);

    /** Parses a global configuration parameter and returns {@link Float} objects. */
    Float[] getFloatArray(String name);

    /** Parses a global configuration parameter and returns {@link Float} objects. The second parameter is the default value. */
    Float[] getFloatArray(String name, Float defaultValue);

    /** Parses a global configuration parameter and returns {@link Double} objects. */
    Double[] getDoubleArray(String name);

    /** Parses a global configuration parameter and returns {@link Double} objects. The second parameter is the default value. */
    Double[] getDoubleArray(String name, Double defaultValue);

    /** Parses a global configuration parameter and returns {@link Date} objects. */
    Date[] getDateArray(String name);

    /** Parses a global configuration parameter and returns {@link Date} objects. The second parameter is the default value. */
    Date[] getDateArray(String name, Date defaultValue);

    /** Parses a global configuration parameter and returns {@link Date} objects. The second parameter is the default value. */
    Date[] getDateArray(String name, long defaultValue);

    /** Parses a global configuration parameter and returns {@link Date} objects. */
    Date[] getDateArray(String name, String format);

    /** Parses a global configuration parameter and returns {@link Date} objects. The third parameter is the default value. */
    Date[] getDateArray(String name, String format, Date defaultValue);

    /** Parses a global configuration parameter and returns {@link Date} objects. The third parameter is the default value. */
    Date[] getDateArray(String name, String format, long defaultValue);

    /** Parses a global configuration parameter and returns enum objects. */
    <T extends Enum<?>> T[] getEnumArray(String name, Class<T> enmType);

    /** Parses a global configuration parameter and returns enum objects. The third parameter is the default value. */
    <T extends Enum<?>> T[] getEnumArray(String name, Class<T> enmType, T defaultValue);

    /** Parses a global configuration parameter and returns file path strings without trailing '/'. */
    String[] getFilePathArray(String name);

    /** Parses a global configuration parameter and returns file path strings without trailing '/'. The second parameter is the default value. */
    String[] getFilePathArray(String name, String defaultValue);

    /** Parses a global configuration parameter and returns directory path strings ending with '/'. */
    String[] getDirectoryPathArray(String name);

    /** Parses a global configuration parameter and returns directory path strings ending with '/'. The second parameter is the default value. */
    String[] getDirectoryPathArray(String name, String defaultValue);

    /** Parses a global configuration parameter and returns {@link SettingNode} objects. */
    SettingNode[] getNodeArray(String name);

    String[] getEnvNames();

    /**
     * Gets the environment variable.
     * @param name Environmental variable name.
     */
    String getEnv(String name);

    /**
     * Adding an environment variable does not affect the system environment variable, and it saves the environment variable using an internal Map to avoid affecting JVM operations.
     * @param name Environmental variable name.
     * @param value An environmental variable value or an expression of an environmental variable.
     */
    void addEnv(String name, String value);

    /**
     * Delete the environmental variable, which is stored from the internal Map, in order to avoid affecting the proper operation of JVM.
     * @param name Environmental variable name.
     */
    void removeEnv(String name);
}
