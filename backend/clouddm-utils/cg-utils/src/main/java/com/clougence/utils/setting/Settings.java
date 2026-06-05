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
 * <p> 配置文件设置 </p>
 * 环境变量来源以及加载顺序，靠后顺位会覆盖前一顺位的重复配置。
 * 1st，System.getProperties()
 * 2st，System.getenv()
 * 3st，配置文件"hasor.environmentVar"
 * 4st，传入的配置
 * @author 赵永春 (zyc@hasor.net)
 * @version : 2013-4-23
 */
public interface Settings {
    String DefaultNameSpace = "https://www.hasor.net/sechma/main";

    /** @return 已解析的命名空间列表 */
    String[] getSettingArray();

    /** 获取指在某个特定命名空间下的Settings接口对象 */
    Settings getSettings(String namespace);

    /** 如果配置的值采用了表达式，那么 refresh 会计算它们 */
    default void refresh() throws IOException {
    }

    /**
     * 设置参数，如果出现多个值，则会覆盖。(使用默认命名空间 : DefaultNameSpace)
     * @see #DefaultNameSpace
     */
    default void setSetting(String key, Object value) {
        if (value instanceof SettingNode) {
            this.setSetting(key, value, ((SettingNode) value).getSpace());
        } else {
            this.setSetting(key, value, DefaultNameSpace);
        }
    }

    /** 设置参数，如果出现多个值，则会覆盖 */
    void setSetting(String key, Object value, String namespace);

    /** 将整个配置项的多个值全部删除（全部命名空间） */
    void removeSetting(String s);

    /** 将整个配置项的多个值全部删除 */
    void removeSetting(String key, String namespace);

    /** 添加参数，如果参数名称相同则追加一项 */
    default void addSetting(String key, Object value) {
        this.addSetting(key, value, DefaultNameSpace);
    }

    /** 添加参数，如果参数名称相同则追加一项 */
    void addSetting(String key, Object var, String namespace);

    /** 计算字符串，并替换环境变量等信息 */
    String evalSetting(String evalString);

    /** 解析全局配置参数，并且返回其{@link Character}形式对象 */
    Character getChar(String name);

    /** 解析全局配置参数，并且返回其{@link Character}形式对象。第二个参数为默认值 */
    Character getChar(String name, Character defaultValue);

    /** 解析全局配置参数，并且返回其{@link String}形式对象 */
    String getString(String name);

    /** 解析全局配置参数，并且返回其{@link String}形式对象。第二个参数为默认值 */
    String getString(String name, String defaultValue);

    /** 解析全局配置参数，并且返回其{@link Boolean}形式对象 */
    Boolean getBoolean(String name);

    /** 解析全局配置参数，并且返回其{@link Boolean}形式对象。第二个参数为默认值 */
    Boolean getBoolean(String name, Boolean defaultValue);

    /** 解析全局配置参数，并且返回其{@link Short}形式对象 */
    Short getShort(String name);

    /** 解析全局配置参数，并且返回其{@link Short}形式对象。第二个参数为默认值 */
    Short getShort(String name, Short defaultValue);

    /** 解析全局配置参数，并且返回其{@link Integer}形式对象 */
    Integer getInteger(String name);

    /** 解析全局配置参数，并且返回其{@link Integer}形式对象。第二个参数为默认值 */
    Integer getInteger(String name, Integer defaultValue);

    /** 解析全局配置参数，并且返回其{@link Long}形式对象 */
    Long getLong(String name);

    /** 解析全局配置参数，并且返回其{@link Long}形式对象。第二个参数为默认值 */
    Long getLong(String name, Long defaultValue);

    /** 解析全局配置参数，并且返回其{@link Float}形式对象 */
    Float getFloat(String name);

    /** 解析全局配置参数，并且返回其{@link Float}形式对象。第二个参数为默认值 */
    Float getFloat(String name, Float defaultValue);

    /** 解析全局配置参数，并且返回其{@link Double}形式对象 */
    Double getDouble(String name);

    /** 解析全局配置参数，并且返回其{@link Double}形式对象。第二个参数为默认值 */
    Double getDouble(String name, Double defaultValue);

    /** 解析全局配置参数，并且返回其{@link Date}形式对象 */
    Date getDate(String name);

    /** 解析全局配置参数，并且返回其{@link Date}形式对象。第二个参数为默认值 */
    Date getDate(String name, Date defaultValue);

    /** 解析全局配置参数，并且返回其{@link Date}形式对象。第二个参数为默认值 */
    Date getDate(String name, long defaultValue);

    /** 解析全局配置参数，并且返回其{@link Date}形式对象 */
    Date getDate(String name, String format);

    /** 解析全局配置参数，并且返回其{@link Date}形式对象。第三个参数为默认值 */
    Date getDate(String name, String format, Date defaultValue);

    /** 解析全局配置参数，并且返回其{@link Date}形式对象。第三个参数为默认值 */
    Date getDate(String name, String format, long defaultValue);

    /** 解析全局配置参数，并且返回其{@link Enum}形式对象 */
    <T extends Enum<?>> T getEnum(String name, Class<T> enmType);

    /** 解析全局配置参数，并且返回其{@link Enum}形式对象。第三个参数为默认值 */
    <T extends Enum<?>> T getEnum(String name, Class<T> enmType, T defaultValue);

    /** 解析全局配置参数，并且返回字符串（用于表示文件，结尾不带‘/’） */
    String getFilePath(String name);

    /** 解析全局配置参数，并且返回字符串（用于表示文件，结尾不带‘/’）。第二个参数为默认值 */
    String getFilePath(String name, String defaultValue);

    /** 解析全局配置参数，并且返回字符串（用于表示目录，结尾带‘/’） */
    String getDirectoryPath(String name);

    /** 解析全局配置参数，并且返回字符串（用于表示目录，结尾带‘/’）。第二个参数为默认值 */
    String getDirectoryPath(String name, String defaultValue);

    /** 解析全局配置参数，并且返回其{@link SettingNode}形式对象 */
    SettingNode getNode(String name);

    /** 解析全局配置参数，并且返回其{@link Character}形式对象 */
    Character[] getCharArray(String name);

    /** 解析全局配置参数，并且返回其{@link Character}形式对象。第二个参数为默认值 */
    Character[] getCharArray(String name, Character defaultValue);

    /** 解析全局配置参数，并且返回其{@link String}形式对象 */
    String[] getStringArray(String name);

    /** 解析全局配置参数，并且返回其{@link String}形式对象。第二个参数为默认值 */
    String[] getStringArray(String name, String defaultValue);

    /** 解析全局配置参数，并且返回其{@link Boolean}形式对象 */
    Boolean[] getBooleanArray(String name);

    /** 解析全局配置参数，并且返回其{@link Boolean}形式对象。第二个参数为默认值 */
    Boolean[] getBooleanArray(String name, Boolean defaultValue);

    /** 解析全局配置参数，并且返回其{@link Short}形式对象 */
    Short[] getShortArray(String name);

    /** 解析全局配置参数，并且返回其{@link Short}形式对象。第二个参数为默认值 */
    Short[] getShortArray(String name, Short defaultValue);

    /** 解析全局配置参数，并且返回其{@link Integer}形式对象 */
    Integer[] getIntegerArray(String name);

    /** 解析全局配置参数，并且返回其{@link Integer}形式对象。第二个参数为默认值 */
    Integer[] getIntegerArray(String name, Integer defaultValue);

    /** 解析全局配置参数，并且返回其{@link Long}形式对象 */
    Long[] getLongArray(String name);

    /** 解析全局配置参数，并且返回其{@link Long}形式对象。第二个参数为默认值 */
    Long[] getLongArray(String name, Long defaultValue);

    /** 解析全局配置参数，并且返回其{@link Float}形式对象 */
    Float[] getFloatArray(String name);

    /** 解析全局配置参数，并且返回其{@link Float}形式对象。第二个参数为默认值 */
    Float[] getFloatArray(String name, Float defaultValue);

    /** 解析全局配置参数，并且返回其{@link Double}形式对象 */
    Double[] getDoubleArray(String name);

    /** 解析全局配置参数，并且返回其{@link Double}形式对象。第二个参数为默认值 */
    Double[] getDoubleArray(String name, Double defaultValue);

    /** 解析全局配置参数，并且返回其{@link Date}形式对象 */
    Date[] getDateArray(String name);

    /** 解析全局配置参数，并且返回其{@link Date}形式对象。第二个参数为默认值 */
    Date[] getDateArray(String name, Date defaultValue);

    /** 解析全局配置参数，并且返回其{@link Date}形式对象。第二个参数为默认值 */
    Date[] getDateArray(String name, long defaultValue);

    /** 解析全局配置参数，并且返回其{@link Date}形式对象 */
    Date[] getDateArray(String name, String format);

    /** 解析全局配置参数，并且返回其{@link Date}形式对象。第三个参数为默认值 */
    Date[] getDateArray(String name, String format, Date defaultValue);

    /** 解析全局配置参数，并且返回其{@link Date}形式对象。第三个参数为默认值 */
    Date[] getDateArray(String name, String format, long defaultValue);

    /** 解析全局配置参数，并且返回其{@link Enum}形式对象 */
    <T extends Enum<?>> T[] getEnumArray(String name, Class<T> enmType);

    /** 解析全局配置参数，并且返回其{@link Enum}形式对象。第三个参数为默认值 */
    <T extends Enum<?>> T[] getEnumArray(String name, Class<T> enmType, T defaultValue);

    /** 解析全局配置参数，并且返回字符串（用于表示文件，结尾不带‘/’） */
    String[] getFilePathArray(String name);

    /** 解析全局配置参数，并且返回字符串（用于表示文件，结尾不带‘/’）。第二个参数为默认值 */
    String[] getFilePathArray(String name, String defaultValue);

    /** 解析全局配置参数，并且返回字符串（用于表示目录，结尾带‘/’） */
    String[] getDirectoryPathArray(String name);

    /** 解析全局配置参数，并且返回字符串（用于表示目录，结尾带‘/’）。第二个参数为默认值 */
    String[] getDirectoryPathArray(String name, String defaultValue);

    /** 解析全局配置参数，并且返回其{@link SettingNode}形式对象 */
    SettingNode[] getNodeArray(String name);

    String[] getEnvNames();

    /**
     * 获取环境变量。
     * @param name 环境变量名。
     */
    String getEnv(String name);

    /**
     * 添加环境变量，添加的环境变量并不会影响到系统环境变量，它会使用内部Map保存环境变量从而避免影响JVM正常运行。
     * @param name 环境变量名。
     * @param value 环境变量值或环境变量表达式。
     */
    void addEnv(String name, String value);

    /**
     * 删除环境变量，该方法从内部Map删除所保存的环境变量，这样做的目的是为了避免影响JVM正常运行。
     * @param name 环境变量名。
     */
    void removeEnv(String name);
}
