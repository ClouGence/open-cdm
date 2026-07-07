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
import java.io.File;
import java.text.DateFormat;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Map;

import com.clougence.utils.StringUtils;
import com.clougence.utils.io.FilenameUtils;

/**
 * Abstract implementation of the Settings interface.
 * @author 赵永春 (zyc@hasor.net)
 * @version : 2013-4-2
 */
public abstract class AbstractSettings implements Settings {
    protected abstract Map<String, String> envMap();

    /** Parses global configuration parameters and returns the type specified by the type parameter. */
    public abstract <T> T getToType(final String name, final Class<T> toType, final T defaultValue);

    public abstract <T> T[] getToTypeArray(final String name, final Class<T> toType, final T defaultValue);

    public <T> T[] getToTypeArray(final String name, final Class<T> toType) {
        return this.getToTypeArray(name, toType, null);
    }

    /** Parses global configuration parameters and returns the type specified by the type parameter. */
    public final <T> T getToType(final String name, final Class<T> toType) {
        return this.getToType(name, toType, null);
    }

    /** Parses a global configuration parameter and returns an {@link Object}. */
    public Object getObject(final String name) {
        return this.getToType(name, Object.class);
    }

    /** Parses a global configuration parameter and returns an {@link Object}. The second parameter is the default value. */
    public Object getObject(final String name, final Object defaultValue) {
        return this.getToType(name, Object.class, defaultValue);
    }

    /** Parses a global configuration parameter and returns a {@link Character}. */
    public Character getChar(final String name) {
        return this.getToType(name, Character.class);
    }

    /** Parses a global configuration parameter and returns a {@link Character}. The second parameter is the default value. */
    public Character getChar(final String name, final Character defaultValue) {
        return this.getToType(name, Character.class, defaultValue);
    }

    public Character[] getCharArray(final String name) {
        return this.getToTypeArray(name, Character.class);
    }

    public Character[] getCharArray(final String name, final Character defaultValue) {
        return this.getToTypeArray(name, Character.class, defaultValue);
    }

    /** Parses a global configuration parameter and returns a {@link String}. */
    public String getString(final String name) {
        return this.getToType(name, String.class);
    }

    /** Parses a global configuration parameter and returns a {@link String}. The second parameter is the default value. */
    public String getString(final String name, final String defaultValue) {
        return this.getToType(name, String.class, defaultValue);
    }

    public String[] getStringArray(final String name) {
        return this.getToTypeArray(name, String.class);
    }

    public String[] getStringArray(final String name, final String defaultValue) {
        return this.getToTypeArray(name, String.class, defaultValue);
    }

    /** Parses a global configuration parameter and returns a {@link Boolean}. */
    public Boolean getBoolean(final String name) {
        return this.getToType(name, Boolean.class);
    }

    /** Parses a global configuration parameter and returns a {@link Boolean}. The second parameter is the default value. */
    public Boolean getBoolean(final String name, final Boolean defaultValue) {
        return this.getToType(name, Boolean.class, defaultValue);
    }

    public Boolean[] getBooleanArray(final String name) {
        return this.getToTypeArray(name, Boolean.class);
    }

    public Boolean[] getBooleanArray(final String name, final Boolean defaultValue) {
        return this.getToTypeArray(name, Boolean.class, defaultValue);
    }

    /** Parses a global configuration parameter and returns a {@link Short}. */
    public Short getShort(final String name) {
        return this.getToType(name, Short.class);
    }

    /** Parses a global configuration parameter and returns a {@link Short}. The second parameter is the default value. */
    public Short getShort(final String name, final Short defaultValue) {
        return this.getToType(name, Short.class, defaultValue);
    }

    public Short[] getShortArray(final String name) {
        return this.getToTypeArray(name, Short.class);
    }

    public Short[] getShortArray(final String name, final Short defaultValue) {
        return this.getToTypeArray(name, Short.class, defaultValue);
    }

    /** Parses a global configuration parameter and returns an {@link Integer}. */
    public Integer getInteger(final String name) {
        return this.getToType(name, Integer.class);
    }

    /** Parses a global configuration parameter and returns an {@link Integer}. The second parameter is the default value. */
    public Integer getInteger(final String name, final Integer defaultValue) {
        return this.getToType(name, Integer.class, defaultValue);
    }

    public Integer[] getIntegerArray(final String name) {
        return this.getToTypeArray(name, Integer.class);
    }

    public Integer[] getIntegerArray(final String name, final Integer defaultValue) {
        return this.getToTypeArray(name, Integer.class, defaultValue);
    }

    /** Parses a global configuration parameter and returns a {@link Long}. */
    public Long getLong(final String name) {
        return this.getToType(name, Long.class);
    }

    /** Parses a global configuration parameter and returns a {@link Long}. The second parameter is the default value. */
    public Long getLong(final String name, final Long defaultValue) {
        return this.getToType(name, Long.class, defaultValue);
    }

    public Long[] getLongArray(final String name) {
        return this.getToTypeArray(name, Long.class);
    }

    public Long[] getLongArray(final String name, final Long defaultValue) {
        return this.getToTypeArray(name, Long.class, defaultValue);
    }

    /** Parses a global configuration parameter and returns a {@link Float}. */
    public Float getFloat(final String name) {
        return this.getToType(name, Float.class);
    }

    /** Parses a global configuration parameter and returns a {@link Float}. The second parameter is the default value. */
    public Float getFloat(final String name, final Float defaultValue) {
        return this.getToType(name, Float.class, defaultValue);
    }

    public Float[] getFloatArray(final String name) {
        return this.getToTypeArray(name, Float.class);
    }

    public Float[] getFloatArray(final String name, final Float defaultValue) {
        return this.getToTypeArray(name, Float.class, defaultValue);
    }

    /** Parses a global configuration parameter and returns a {@link Double}. */
    public Double getDouble(final String name) {
        return this.getToType(name, Double.class);
    }

    /** Parses a global configuration parameter and returns a {@link Double}. The second parameter is the default value. */
    public Double getDouble(final String name, final Double defaultValue) {
        return this.getToType(name, Double.class, defaultValue);
    }

    public Double[] getDoubleArray(final String name) {
        return this.getToTypeArray(name, Double.class);
    }

    public Double[] getDoubleArray(final String name, final Double defaultValue) {
        return this.getToTypeArray(name, Double.class, defaultValue);
    }

    /** Parses a global configuration parameter and returns a {@link Date}. */
    public Date getDate(final String name) {
        return this.getDate(name, getString(name + ".format"), null);
    }

    /** Parses a global configuration parameter and returns a {@link Date}. The second parameter is the default value. */
    public Date getDate(final String name, final Date defaultValue) {
        return this.getDate(name, getString(name + ".format"), defaultValue);
    }

    /** Parses a global configuration parameter and returns a {@link Date}. The second parameter is the default value. */
    public Date getDate(final String name, final long defaultValue) {
        return this.getDate(name, getString(name + ".format"), new Date(defaultValue));
    }

    /** Parses a global configuration parameter and returns a {@link Date}. */
    public Date getDate(final String name, final String format) {
        return this.getDate(name, format, null);
    }

    /** Parses a global configuration parameter and returns a {@link Date}. The third parameter is the default value. */
    public Date getDate(final String name, final String format, final long defaultValue) {
        return this.getDate(name, format, new Date(defaultValue));
    }

    /** Parses a global configuration parameter and returns a {@link Date}. The third parameter is the default value. */
    public Date getDate(final String name, final String format, final Date defaultValue) {
        String oriData = this.getToType(name, String.class);
        if (oriData == null || oriData.length() == 0) {
            return defaultValue;
        }
        //
        DateFormat dateFormat = new SimpleDateFormat(format);
        ParsePosition pos = new ParsePosition(0);
        dateFormat.setLenient(false);
        Date parsedDate = dateFormat.parse(oriData, pos); // ignore the result (use the Calendar)
        if (pos.getErrorIndex() >= 0 || pos.getIndex() != oriData.length() || parsedDate == null) {
            return defaultValue;
        } else {
            return parsedDate;
        }
    }

    public Date[] getDateArray(final String name) {
        return this.getDateArray(name, getString(name + ".format"), null);
    }

    public Date[] getDateArray(final String name, final Date defaultValue) {
        return this.getDateArray(name, getString(name + ".format"), defaultValue);
    }

    public Date[] getDateArray(final String name, final long defaultValue) {
        return this.getDateArray(name, getString(name + ".format"), new Date(defaultValue));
    }

    public Date[] getDateArray(final String name, final String format) {
        return this.getDateArray(name, format, null);
    }

    public Date[] getDateArray(final String name, final String format, final long defaultValue) {
        return this.getDateArray(name, format, new Date(defaultValue));
    }

    public Date[] getDateArray(final String name, final String format, final Date defaultValue) {
        String[] oriDataArray = this.getToTypeArray(name, String.class);
        if (oriDataArray == null || oriDataArray.length == 0) {
            return new Date[0];
        }

        DateFormat dateFormat = new SimpleDateFormat(format);
        dateFormat.setLenient(false);
        Date[] parsedDate = new Date[oriDataArray.length];
        for (int i = 0; i < oriDataArray.length; i++) {
            String oriData = oriDataArray[i];
            ParsePosition pos = new ParsePosition(0);
            parsedDate[i] = dateFormat.parse(oriData, pos); // ignore the result (use the Calendar)
            if (pos.getErrorIndex() >= 0 || pos.getIndex() != oriData.length() || parsedDate[i] == null) {
                parsedDate[i] = defaultValue == null ? null : new Date(defaultValue.getTime());
            }
        }
        return parsedDate;
    }

    /** Parses a global configuration parameter and returns an enum value. */
    public <T extends Enum<?>> T getEnum(final String name, final Class<T> enmType) {
        return this.getToType(name, enmType, null);
    }

    /** Parses a global configuration parameter and returns an enum value. The third parameter is the default value. */
    public <T extends Enum<?>> T getEnum(final String name, final Class<T> enmType, final T defaultValue) {
        return this.getToType(name, enmType, defaultValue);
    }

    public <T extends Enum<?>> T[] getEnumArray(final String name, final Class<T> enmType) {
        return this.getToTypeArray(name, enmType, null);
    }

    public <T extends Enum<?>> T[] getEnumArray(final String name, final Class<T> enmType, final T defaultValue) {
        return this.getToTypeArray(name, enmType, defaultValue);
    }

    /** Parses a global configuration parameter and returns a file path string. */
    public String getFilePath(final String name) {
        return getFilePath(name, null, true);
    }

    /** Parses a global configuration parameter and returns a file path string. The second parameter is the default value. */
    public String getFilePath(final String name, final String defaultValue) {
        return getFilePath(name, defaultValue, true);
    }

    /** Parses a global configuration parameter and returns a directory path string. */
    public String getDirectoryPath(final String name) {
        return getFilePath(name, null, false);
    }

    /** Parses a global configuration parameter and returns a directory path string. The second parameter is the default value. */
    public String getDirectoryPath(final String name, final String defaultValue) {
        return getFilePath(name, defaultValue, false);
    }

    public String[] getFilePathArray(final String name) {
        return this.getFilePathArray(name, null, true);
    }

    public String[] getFilePathArray(final String name, final String defaultValue) {
        return this.getFilePathArray(name, defaultValue, true);
    }

    public String[] getDirectoryPathArray(final String name) {
        return this.getFilePathArray(name, null, false);
    }

    public String[] getDirectoryPathArray(final String name, final String defaultValue) {
        return this.getFilePathArray(name, defaultValue, false);
    }

    private String getFilePath(final String name, final String defaultValue, boolean includeName) {
        String filePath = this.getToType(name, String.class);
        if (StringUtils.isBlank(filePath)) {
            return defaultValue;// Empty
        }
        if (includeName) {
            String fileName = FilenameUtils.getName(filePath);
            if (StringUtils.isNotBlank(fileName)) {
                return FilenameUtils.getFullPath(filePath) + FilenameUtils.getName(filePath);
            } else {
                return StringUtils.isBlank(defaultValue) ? null : defaultValue;
            }
        } else {
            return FilenameUtils.getFullPath(filePath);
        }
    }

    private String[] getFilePathArray(final String name, final String defaultValue, boolean includeName) {
        ArrayList<String> filePaths = new ArrayList<>();
        for (String url : this.getSettingArray()) {
            Settings targetSettings = this.getSettings(url);
            String filePath = targetSettings.getString(name);
            if (StringUtils.isBlank(filePath)) {
                continue;// Empty
            }
            //
            if (includeName) {
                String fileName = FilenameUtils.getName(filePath);
                if (StringUtils.isNotBlank(fileName)) {
                    filePaths.add(FilenameUtils.getFullPath(filePath) + FilenameUtils.getName(filePath));
                } else {
                    continue;
                }
            } else {
                filePaths.add(FilenameUtils.getFullPath(filePath));
            }
        }
        return filePaths.toArray(new String[0]);
    }

    /** Parses a global configuration parameter and returns a {@link SettingNode}. */
    public SettingNode getNode(final String name) {
        return this.getToType(name, SettingNode.class, null);
    }

    public SettingNode[] getNodeArray(final String name) {
        return this.getToTypeArray(name, SettingNode.class, null);
    }

    @Override
    public String[] getEnvNames() {
        return this.envMap().keySet().toArray(new String[0]);
    }

    @Override
    public String getEnv(String name) {
        return this.envMap().get(name);
    }

    @Override
    public void addEnv(String name, String value) {
        this.envMap().put(name, value);
    }

    @Override
    public void removeEnv(String name) {
        this.envMap().remove(name);
    }

    public String toString() {
        return "Settings[" + this.getClass().getSimpleName() + "]";
    }
}
